import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_DATE_TIME = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    public record ParsedDateTime(LocalDateTime value, boolean hasTime) {}

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
            case "help" -> new HelpCommand();
            default -> throw new ChironException("That path doesn’t make sense yet.");
        };
    }

    public static ParsedDateTime parseDateTime(String raw) {
        String s = raw.trim();
        if (s.isEmpty()) return null;

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

    public static String formatDateTime(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(OUTPUT_DATE_TIME) : dt.toLocalDate().format(OUTPUT_DATE);
    }

    public static String storeDateTime(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(INPUT_DATE_TIME) : dt.toLocalDate().format(INPUT_DATE);
    }

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
