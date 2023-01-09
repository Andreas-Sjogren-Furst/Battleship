import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;

import java.io.IOException;
import java.util.List;

public class Join {
    public String name;
    public RemoteSpace mySpace;
    public RemoteSpace myBoard;

    Join(String name) throws IOException {
        // Connect to the remote chat space
        this.name = name;

        String uri = "tcp://127.0.0.1:9001/lobby?keep";
        System.out.println("Connecting to lobby space at: " + uri + "...");
        mySpace = new RemoteSpace(uri);
    };

    public List<Object[]> showPlayers() throws InterruptedException {
        return mySpace.queryAll(new FormalField(String.class));
    }

    public void connect(String ipAddress, String spaceName) throws IOException {
        // Connect to the remote chat space
        String uri = "tcp://" + ipAddress + "/" + spaceName + "?keep";
        System.out.println("Connecting to chat space " + uri + "...");
        mySpace = new RemoteSpace(uri);
        myBoard = new RemoteSpace("tcp://" + ipAddress + "/board?keep");
    }
}
