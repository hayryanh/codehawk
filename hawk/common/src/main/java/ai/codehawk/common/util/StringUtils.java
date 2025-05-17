package ai.codehawk.common.util;

/**
 * Utility class for string operations.
 * <p>
 * This class provides static methods for common string operations.
 * </p>
 */
public final class StringUtils {

    private StringUtils() {
        // Prevent instantiation
    }

    /**
     * Checks if a string is not null and not empty.
     *
     * @param text the string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean hasText(String text) {
        return text != null && !text.isEmpty();
    }
}
