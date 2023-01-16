import org.jspace.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public abstract class Chat {

    public static void startChat(Space chatSpace, Space boardSpace, String name, Board attackBoard, int playerId,String whichTurn, String playerName) throws IOException, InterruptedException {
        chatSpace.query(new ActualField("lockHost"));
        chatSpace.query(new ActualField("lockJoin"));
        WelcomeScreen.battle(playerName);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (input.ready()) {
            input.readLine();
        }

        new Thread(() -> {
            while (true) {
                try {
                    Object[] message = chatSpace.query(new FormalField(String.class), new FormalField(String.class));
                    if (!(message[0]).equals(name)) {
                        chatSpace.get(new FormalField(String.class), new FormalField(String.class));
                        System.out.println(message[0] + ": " + message[1]);
                    }
                } catch (InterruptedException e) {
                }
            }
        }).start();

        while (true) {
            String message = input.readLine();
            if(message.equals("leave")) {
                boardSpace.put("leave", name);
            }
            else if (message.equals("draw")) {
                chatSpace.put("draw", playerId);
            } else if(attackBoard.isValidAttack(message) && !Player.gameOver && chatSpace.queryp(new ActualField(whichTurn)) != null){
                // Host attacks Join
                chatSpace.put(message,playerId,"toJoin");
                chatSpace.put(message,playerId,"toHost");
            } else chatSpace.put(name, message);
        }
    }
}
