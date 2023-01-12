import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Login {

    private static RemoteSpace loginSpace;
    private static BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

    public static void begin() throws IOException, InterruptedException {
        String input;
        connectToSpace();
        System.out.println("@@@ BATTLESHIP - MULTIPLAYER @@@");

        while(true){
            System.out.println("Type 1: Login");
            System.out.println("Type 2: Create User");
            input = scanner.readLine();

            if(input.equals("1")){
                login();
            } else if(input.equals("2")) {
                createUser();
            } else System.out.print("!!! Wrong input, please try again !!!");
        }
    }

    private static void login() throws IOException, InterruptedException {
        String username;
        String password;

        while(true){
            System.out.println("--- LOGIN ---");
            System.out.print("Username: ");
            username = scanner.readLine();
            System.out.print("Password: ");
            password = scanner.readLine();
            System.out.println("Logging in...");
            loginSpace.put(username,password);
            Object[] isValidLogin = loginSpace.get(new FormalField(String.class), new ActualField(username));
            if(isValidLogin[0].equals("true")){
                System.out.println("Login in successfully");
                break;
            } else System.out.println("!!! Username or Password does not exist, please try again !!!");
        }
    }

    private static void connectToSpace() throws IOException {
        String loginURI = "tcp://127.0.0.1:9001/login?keep";
        loginSpace = new RemoteSpace(loginURI);
    }

    private static void createUser() throws IOException, InterruptedException {
        String username;
        String password;
        String passwordRedo;
        while(true){
            System.out.println("--- CREATE A USER ---");
            System.out.print("Username: ");
            username = scanner.readLine();
            System.out.print("Password: ");
            password = scanner.readLine();
            System.out.print("Write Password again: ");
            passwordRedo = scanner.readLine();
            if(!password.equals(passwordRedo)){
                System.out.println("!!! Password does not match, please try again !!!");
                continue;
            }

            System.out.println("Creating account...");
            loginSpace.put(username,password,"createAccount");
            Object[] isValidCreateAccount = loginSpace.get(new FormalField(String.class), new ActualField(username), new ActualField("createAccount"));
            if(isValidCreateAccount[0].equals("true")){
                System.out.println("Account was created successfully :)");
                break;
            } else System.out.println("!!! Username does already exist, please try again !!!");
        }
    }
}
