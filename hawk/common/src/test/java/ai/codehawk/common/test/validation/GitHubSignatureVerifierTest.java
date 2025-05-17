package ai.codehawk.common.test.validation;

import ai.codehawk.common.validation.GitHubSignatureVerifier;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubSignatureVerifierTest {
    private static final String HMAC_ALGO = "HmacSHA256";

    /** Utility to compute the hex-encoded HMAC-SHA256 of a payload. */
    private String computeSignature(String payload, String secret) throws Exception {
        Mac hmac = Mac.getInstance(HMAC_ALGO);
        hmac.init(new SecretKeySpec(secret.getBytes(), HMAC_ALGO));
        byte[] bytes = hmac.doFinal(payload.getBytes());
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Test
    void isValid_withCorrectSignature_returnsTrue() throws Exception {
        String payload = "{\"action\":\"opened\",\"number\":42}";
        String secret  = "my-test-secret";
        String sig     = "sha256=" + computeSignature(payload, secret);

        assertTrue(
                GitHubSignatureVerifier.isValid(payload, sig, secret),
                "Expected valid HMAC-SHA256 signature to be accepted"
        );
    }

    @Test
    void isValid_withIncorrectSignature_returnsFalse() {
        String payload = "some payload";
        String secret  = "topsecret";
        // wrong signature (not matching payload+secret)
        String sig     = "sha256=deadbeef";

        assertFalse(
                GitHubSignatureVerifier.isValid(payload, sig, secret),
                "Expected random signature to be rejected"
        );
    }

    @Test
    void isValid_withNullOrEmptySignatureHeader_returnsFalse() {
        String payload = "irrelevant";
        String secret  = "whatever";

        assertFalse(GitHubSignatureVerifier.isValid(payload, null, secret));
        assertFalse(GitHubSignatureVerifier.isValid(payload, "", secret));
        assertFalse(GitHubSignatureVerifier.isValid(payload, "   ", secret));
    }

    @Test
    void isValid_withMissingPrefix_returnsFalse() {
        String payload = "data";
        String secret  = "secret";
        // valid hex but missing "sha256=" prefix
        String hexSig  = "012345abcdef";
        assertFalse(
                GitHubSignatureVerifier.isValid(payload, hexSig, secret),
                "Signature header without 'sha256=' should be rejected"
        );
    }

    @Test
    void isValid_whenMacThrows_returnsFalse() {
        // to simulate an exception inside Mac.getInstance by passing a bad algorithm via reflection,
        // we'll cheat by invoking the method with a null payload to force a NPE
        assertFalse(
                GitHubSignatureVerifier.isValid(null, "sha256=anything", "secret"),
                "Null payload should cause exception and return false"
        );
    }
}
