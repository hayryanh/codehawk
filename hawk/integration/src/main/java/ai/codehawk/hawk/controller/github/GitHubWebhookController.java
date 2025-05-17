package ai.codehawk.hawk.controller.github;

import ai.codehawk.common.entity.github.PRMetadata;
import ai.codehawk.common.validation.GitHubSignatureVerifier;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/webhook")
public class GitHubWebhookController {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubWebhookController.class);

    private final ObjectMapper mapper;
    private final String secret;

    public GitHubWebhookController(ObjectMapper mapper, @Value("${github.webhook.secret}") String secret) {
        this.mapper = mapper;
        this.secret = secret;
    }

    @PostMapping(path = "/github", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> onPrEvent(
            @RequestHeader("X-Hub-Signature-256") String signature,
            @RequestHeader("X-GitHub-Event") String event,
            @RequestBody String payload
    ) throws Exception {
        // Verify signature
        if (!GitHubSignatureVerifier.isValid(payload, signature, secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        // Only handle pull_request events
        if (!"pull_request".equals(event)) {
            return ResponseEntity.ok("Ignored event: " + event);
        }

        // Parse JSON
        JsonNode root = mapper.readTree(payload);
        String action = root.path("action").asText();
        // interested only in opened, edited, closed
        if (!("opened".equals(action) || "edited".equals(action) || "closed".equals(action))) {
            return ResponseEntity.ok("Ignored PR action: " + action);
        }

        JsonNode pr = root.path("pull_request");
        PRMetadata meta = new PRMetadata();
        meta.setPrNumber(pr.path("number").asLong());
        meta.setRepository(root.path("repository").path("full_name").asText());
        meta.setTitle(pr.path("title").asText());
        meta.setUrl(pr.path("html_url").asText());
        meta.setUserLogin(pr.path("user").path("login").asText());
        meta.setAction(action);
        meta.setState(pr.path("state").asText());
        meta.setTimestamp(OffsetDateTime.parse(pr.path("updated_at").asText()));

        // Process the PR metadata (e.g., store it in a database, trigger a CI/CD pipeline, etc.)
        LOG.info("Processing PR: {} - {} - {} - {}", meta.getPrNumber(), meta.getTitle(), meta.getAction(), meta.getState());

        return ResponseEntity.ok("Processed PR#" + meta.getPrNumber() + " " + action);
    }
}
