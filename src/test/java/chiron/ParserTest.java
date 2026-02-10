package chiron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {

    @Test
    public void parse_byeCommand_success() throws Exception {
        // Ensure "bye" returns the correct command type
        Command c = Parser.parse("bye");
        assertEquals(ByeCommand.class, c.getClass());
    }

    @Test
    public void parse_todoCommand_success() throws Exception {
        // Ensure "todo read book" returns a TodoCommand
        Command c = Parser.parse("todo read book");
        assertEquals(TodoCommand.class, c.getClass());
    }

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
