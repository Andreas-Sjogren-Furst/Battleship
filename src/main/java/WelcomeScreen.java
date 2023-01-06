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

    public static String getName(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");
        String name = scanner.next();
        return name;
    }
}
