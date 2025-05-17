package ai.codehawk.common.validation;

import ai.codehawk.common.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * Utility class for verifying GitHub webhook signatures.
 * <p>
 * This class provides a method to verify the signature of a GitHub webhook payload using HMAC SHA256.
 * </p>
 */
public final class GitHubSignatureVerifier {
    private static final String HMAC_ALGO = "HmacSHA256";

    private GitHubSignatureVerifier() {
        // Prevent instantiation
    }

    /**
     * Verifies the GitHub webhook signature.
     *
     * @param payload         the payload of the webhook
     * @param signatureHeader the signature header from the webhook request
     * @param secret          the secret used to sign the payload
     * @return true if the signature is valid, false otherwise
     */
    public static boolean isValid(String payload, String signatureHeader, String secret) {
        if (!StringUtils.hasText(signatureHeader) || !signatureHeader.startsWith("sha256=")) {
            return false;
        }
        try {
            String expected = signatureHeader.substring(7);
            Mac hmac = Mac.getInstance(HMAC_ALGO);
            hmac.init(new SecretKeySpec(secret.getBytes(), HMAC_ALGO));
            byte[] actualBytes = hmac.doFinal(payload.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : actualBytes) {
                sb.append(String.format("%02x", b));
            }
            return MessageDigest.isEqual(sb.toString().getBytes(), expected.getBytes());
        } catch (Exception e) {
            return false;
        }
    }
}
