import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public abstract class Chat {

    public static void connectToChat(RemoteSpace chat, String name, Board attackBoard) throws IOException, InterruptedException {
        chat.query(new ActualField("lockJoin"));
        chat.query(new ActualField("lockHost"));
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (input.ready()) {
            input.readLine();
        } WelcomeScreen.battle();

        new Thread(() -> {
            while (true) {
                try {
                    Object[] message = chat.query(new FormalField(String.class), new FormalField(String.class));
                    if (!(message[0]).equals(name)) {
                        chat.get(new FormalField(String.class), new FormalField(String.class));
                        System.out.println(message[0] + ": " + message[1]);
                    }
                } catch (InterruptedException e) {
                }
            }
        }).start();

        while (true) {
            String message = input.readLine();
            if (message.equals("draw")) {
                chat.put("draw", 2);
            } else if (attackBoard.isValidAttack(attackBoard, message)) {
                chat.put(message, 1, 1);
                chat.put(message, 1, 1);// Join attacks Host
            } else chat.put(name, message);
        }
    }

    public static void createChat(SequentialSpace chat, String name, Board attackBoard) throws IOException, InterruptedException {
        chat.query(new ActualField("lockHost"));
        chat.query(new ActualField("lockJoin"));
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (input.ready()) {
            input.readLine();
        } WelcomeScreen.battle();

        new Thread(() -> {
            while (true) {
                try {
                    Object[] message = chat.query(new FormalField(String.class), new FormalField(String.class));
                    if (!(message[0]).equals(name)) {
                        chat.get(new FormalField(String.class), new FormalField(String.class));
                        System.out.println(message[0] + ": " + message[1]);
                    }
                } catch (InterruptedException e) {
                }
            }
        }).start();

        while (true) {
            String message = input.readLine();
            if (message.equals("draw")) {
                chat.put("draw", 1);
            } else if(attackBoard.isValidAttack(attackBoard,message)){
                chat.put(message,2,2);
                chat.put(message,2,2);// Host attacks Join
            } else chat.put(name, message);
        }
    }
}
