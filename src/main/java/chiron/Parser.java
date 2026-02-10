package chiron;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into commands.
 * Handles date/time parsing and command string processing.
 */
public class Parser {

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_DATE_TIME = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    /**
     * Helper record to hold parsed LocalDateTime and a boolean flag indicating if
     * time was provided.
     */
    public record ParsedDateTime(LocalDateTime value, boolean hasTime) {
    }

    /**
     * Parses the raw input string from the user and returns the corresponding
     * Command object.
     *
     * @param raw The full command string input by the user.
     * @return The Command object corresponding to the user's input.
     * @throws ChironException If the command is invalid or input is empty.
     */
    public static Command parse(String raw) throws ChironException {
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new ChironException("Speak, or don’t. But don’t waste my time.");
        }

        String[] parts = trimmed.split("\\s+", 2);
        String word = parts[0].toLowerCase();
        String args = (parts.length < 2) ? "" : parts[1];

        return switch (word) {
            case "bye" -> new ByeCommand();
            case "list" -> new ListCommand();
            case "todo" -> new TodoCommand(args);
            case "deadline" -> new DeadlineCommand(args);
            case "event" -> new EventCommand(args);
            case "mark" -> new MarkCommand(args);
            case "unmark" -> new UnmarkCommand(args);
            case "delete" -> new DeleteCommand(args);
            case "find" -> new FindCommand(args);
            case "help" -> new HelpCommand();
            default -> throw new ChironException("That path doesn’t make sense yet.");
        };
    }

    /**
     * Parses a date/time string into a ParsedDateTime object.
     * Supports formats: "yyyy-MM-dd" (date only) and "yyyy-MM-dd HHmm" (date and
     * time).
     *
     * @param raw The raw date/time string.
     * @return A ParsedDateTime object containing the LocalDateTime and a flag for
     *         time presence, or null if parsing fails.
     */
    public static ParsedDateTime parseDateTime(String raw) {
        String s = raw.trim();
        if (s.isEmpty())
            return null;

        if (s.contains(" ")) {
            try {
                return new ParsedDateTime(LocalDateTime.parse(s, INPUT_DATE_TIME), true);
            } catch (DateTimeParseException ignored) {
                return null;
            }
        }

        try {
            LocalDate d = LocalDate.parse(s, INPUT_DATE);
            return new ParsedDateTime(LocalDateTime.of(d, LocalTime.MIDNIGHT), false);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    /**
     * Formats a LocalDateTime into a user-friendly string (e.g., "MMM dd yyyy" or
     * "MMM dd yyyy HHmm").
     *
     * @param dt      The LocalDateTime object to format.
     * @param hasTime Whether the time component should be included.
     * @return The formatted date string.
     */
    public static String formatDateTime(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(OUTPUT_DATE_TIME) : dt.toLocalDate().format(OUTPUT_DATE);
    }

    /**
     * Formats a LocalDateTime into a storage-friendly string (e.g., "yyyy-MM-dd" or
     * "yyyy-MM-dd HHmm").
     *
     * @param dt      The LocalDateTime object to format.
     * @param hasTime Whether the time component should be included.
     * @return The formatted storage string.
     */
    public static String storeDateTime(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(INPUT_DATE_TIME) : dt.toLocalDate().format(INPUT_DATE);
    }

    /**
     * Parses an integer index from a string argument.
     *
     * @param arg The string containing the index.
     * @return The parsed integer index.
     * @throws ChironException If the argument is empty or not a valid number.
     */
    public static int parseIndex(String arg) throws ChironException {
        String s = arg.trim();
        if (s.isEmpty()) {
            throw new ChironException("Give me a task number.");
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new ChironException("That wasn’t a number. Precision matters here.");
        }
    }
}
