import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;

import java.io.IOException;
import java.util.List;

public class Join {
    public String name;
    public RemoteSpace chat;

    Join(String name) throws IOException {
        // Connect to the remote chat space
        this.name = name;
        String uri = "tcp://127.0.0.1:9001/Lobby?keep";
        System.out.println("Connecting to chat space " + uri + "...");
        chat = new RemoteSpace(uri);
    };

    public List<Object[]> showPlayers() throws InterruptedException {
        return chat.queryAll(new FormalField(String.class),new FormalField(Integer.class));
    }

    public RemoteSpace connect(String ipAddress, String spaceName) throws IOException {
        // Connect to the remote chat space
        String uri = "tcp://" + ipAddress + "/" + spaceName + "?keep";
        System.out.println("Connecting to chat space " + uri + "...");
        chat = new RemoteSpace(uri);
        return chat;
    }
}