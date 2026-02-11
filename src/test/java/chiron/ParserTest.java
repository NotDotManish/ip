package chiron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Parser class.
 */
public class ParserTest {

    /**
     * Tests that the "bye" command is correctly parsed.
     *
     * @throws Exception If parsing fails.
     */
    @Test
    public void parse_byeCommand_success() throws Exception {
        // Ensure "bye" returns the correct command type
        Command c = Parser.parse("bye");
        assertEquals(ByeCommand.class, c.getClass());
    }

    /**
     * Tests that the "todo" command is correctly parsed.
     *
     * @throws Exception If parsing fails.
     */
    @Test
    public void parse_todoCommand_success() throws Exception {
        // Ensure "todo read book" returns a TodoCommand
        Command c = Parser.parse("todo read book");
        assertEquals(TodoCommand.class, c.getClass());
    }

    /**
     * Tests that empty input throws an exception.
     */
    @Test
    public void parse_emptyInput_exceptionThrown() {
        // Ensure empty input throws an exception
        try {
            Parser.parse("   ");
            fail(); // Should not reach this line
        } catch (ChironException e) {
            assertEquals("Speak, or don’t. But don’t waste my time.", e.getMessage());
        }
    }

    /**
     * Tests that unknown commands throw an exception.
     */
    @Test
    public void parse_unknownCommand_exceptionThrown() {
        // Ensure unknown commands throw an exception
        try {
            Parser.parse("blah");
            fail();
        } catch (ChironException e) {
            assertEquals("That path doesn’t make sense yet.", e.getMessage());
        }
    }
}
