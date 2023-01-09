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
            } System.out.println("Wrong input, please try again: ");
        }
    }

    public static void battle(){
        System.out.println("Both players have placed their ships, let the battle begin !");
        System.out.println("To shoot write the coordinate of the square");
        System.out.println("To see the boards write: update");
        System.out.println("Host will begin shooting"); // Todo make it random
    }

    public static String getName(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");
        return scanner.next().toLowerCase();
    }
}
