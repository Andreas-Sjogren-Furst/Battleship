import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.Remote;
import java.util.Enumeration;

public class Host {

    SpaceRepository repository = new SpaceRepository();
    SequentialSpace mySpace = new SequentialSpace();
    RemoteSpace lobbySpace;
    URI myUri;
    String mySpaceName;
    String hostName;

    public Host(String hostName, String uri, String spaceName) throws URISyntaxException, InterruptedException, IOException {

        // Initialising variables
        this.myUri = new URI(uri);
        this.hostName = hostName;
        this.mySpaceName = spaceName;

        // Open a gate, so people can join your own network
        openGate();

        // Connecting to global lobby space
        connectToLobby();
    }

    private void connectToLobby() throws IOException, InterruptedException {
        String lobbyUri = "tcp://127.0.0.1:9001/lobby?keep";
        System.out.println("Connecting to Lobby space at: " + lobbyUri + "...");
        lobbySpace = new RemoteSpace(lobbyUri);
        lobbySpace.put(myUri.getHost() + ":" + myUri.getPort(), mySpaceName, hostName);
        lobbySpace.put(hostName);
    }

    private void openGate() throws URISyntaxException {
        String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() + "/?keep";
        System.out.println("Opening host repository gate at: " + gateUri + "...");
        repository.addGate(gateUri);
        repository.add(mySpaceName, mySpace);

    }

    private String getIpAddress() throws SocketException {
        String ip = "";
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            // filters out 127.0.0.1 and inactive interfaces
            if (iface.isLoopback() || !iface.isUp()) continue;
            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            InetAddress address = addresses.nextElement();
            ip = address.getHostAddress();
            System.out.println(iface.getDisplayName() + " " + ip);
        } return ip;
    }
}
