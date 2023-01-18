import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;

import java.io.IOException;
import java.util.List;

public class Join
{
    String name;
    int playerId = 2;
    int opponentId = 1;

    RemoteSpace chatSpace;
    RemoteSpace boardSpace;
    Board attackBoard = new Board();
    Board defenseBoard = new Board();

    Join(String name) throws IOException {
        // Connect to the remote chat space
        this.name = name;
        String uri = "tcp://10.209.121.213:9001/lobby?keep"; // Change to lobby ip address here
        System.out.println("Connecting to lobby space at: " + uri + "...");
        chatSpace = new RemoteSpace(uri);
    }

    public List<Object[]> showPlayers() throws InterruptedException {
        return chatSpace.queryAll(new FormalField(String.class), new FormalField(String.class),new FormalField(String.class));
    }

    public void connect(String ipAddress, String spaceName) throws IOException, InterruptedException {
        // Connect to the remote chat space
        String boardSpaceName = "board";
        String chatURI = "tcp://" + ipAddress + "/" + spaceName + "?keep";
        String boardURI = "tcp://" + ipAddress + "/" + boardSpaceName + "?keep";
        System.out.println("Connecting to chat chatSpace " + chatURI + "...");
        System.out.println("Connecting to chat boardSpace " + boardURI + "...");
        chatSpace = new RemoteSpace("tcp://" + ipAddress + "/" + spaceName + "?keep");
        boardSpace = new RemoteSpace("tcp://" + ipAddress + "/" + boardSpaceName + "?keep");
        chatSpace.put(name, -1);
    }
}
