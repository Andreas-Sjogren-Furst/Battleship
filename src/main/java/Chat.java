import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Chat {

    public static void runChat(RemoteSpace chat, String name) throws IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

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
            chat.put(name, message);
        }
    }
}
