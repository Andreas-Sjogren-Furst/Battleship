import java.util.Scanner;
public abstract class WelcomeScreen {

    public static int begin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("@@@ WELCOME TO BATTLESHIP @@@");
        System.out.println(" ## Press: \"0\" - TO HOST ##\n ## Press: \"1\" - TO JOIN ##");

        while (true) {
            String input = scanner.next();
            if(input.equals("0") || input.equals("1")){
                return Integer.parseInt(input);
            } System.out.println("!!! Wrong input, please try again. !!!");
        }
    }

    public static void battle(){
        System.out.println("--- BOTH PLAYERS ARE READY TO PLAY ---");
        System.out.println(" - SHOOT:       write the coordinate of the square");
        System.out.println(" - SHOW BOARDS: write \"draw\"");
        System.out.println(" - LEAVE:       write \"leave\"");
        System.out.println();
        System.out.println("### HOST STARTS ###");
    }

    public static String getName(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");
        return scanner.next().toLowerCase();
    }
}
