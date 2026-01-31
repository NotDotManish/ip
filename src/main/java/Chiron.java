import java.util.Scanner;

public class Chiron {

    private static final String LINE =
            "____________________________________________________________";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printGreeting();

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                printGoodbye();
                break;
            }

            // Echo (with Chiron personality - trying to be wise yet youthful)
            System.out.println(LINE);
            System.out.println("Chiron: \"" + input + "\"");
            System.out.println("Chiron: Noted. Now—what’s the real question behind it?");
            System.out.println(LINE);
        }

        scanner.close();
    }

    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Chiron!");
        System.out.println("Wise enough to guide, young enough to roast you a little.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void printGoodbye() {
        System.out.println(LINE);
        System.out.println("Chiron: Farewell. Train well—come back sharper.");
        System.out.println(LINE);
    }
}
