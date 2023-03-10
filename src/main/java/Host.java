import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.Remote;
import java.util.Enumeration;
import java.util.Scanner;

public class Host {

    Board attackBoard = new Board();
    Board defenseBoard = new Board();
    SpaceRepository repository = new SpaceRepository();
    SequentialSpace chatSpace = new SequentialSpace();
    SequentialSpace boardSpace = new SequentialSpace();
    RemoteSpace lobbySpace;
    URI myUri = new URI("tcp://" + getIpAddress() + ":9002/?keep");
    String name;

    int playerId = 1;
    int opponentId = 2;

    public Host(String hostName) throws URISyntaxException, InterruptedException, IOException {

        // Initialising variables
        this.name = hostName;

        // Open a gate, so people can join your own game
        openGate();

        // Connecting to global lobby space
        connectToLobby();
    }

    private void connectToLobby() throws IOException, InterruptedException {
        String lobbyUri = "tcp://10.209.121.213:9001/lobby?keep"; // Change to lobby ip address here
        System.out.println("Connecting to Lobby space at: " + lobbyUri + "...");
        System.out.println("Waiting for a player to join...");
        lobbySpace = new RemoteSpace(lobbyUri);
        lobbySpace.put(myUri.getHost() + ":" + myUri.getPort(), name, name);
        Object[] t = chatSpace.query(new FormalField(String.class), new ActualField(-1));
        System.out.println(t[0] + " has joined the game");
    }

    private void openGate() throws InterruptedException {
        String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() + "/?keep";
        System.out.println("Opening host repository gate at: " + gateUri + "...");
        repository.addGate(gateUri);
        repository.add(name, chatSpace);
        repository.add("board", boardSpace);
        chatSpace.put("p1");
    }

    public static String getIpAddress() throws UnknownHostException {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        }
}
