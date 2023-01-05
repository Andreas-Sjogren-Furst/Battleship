import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class Host {
    String name;
    String uri, spaceName;
    SpaceRepository repository;
    SequentialSpace sequentialSpace;
    RemoteSpace chat;

    public Host(String name, String uri, String spaceName) throws URISyntaxException, InterruptedException, IOException {
        // Open a gate
        this.name = name;
        this.uri = uri;
        this.spaceName = spaceName;
        this.sequentialSpace = new SequentialSpace();
        repository = new SpaceRepository();
        URI myUri = new URI(uri);
        String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() +  "?keep" ;
        System.out.println("Opening repository gate at " + gateUri + "...");
        //repository.addGate(gateUri);
        repository.add(spaceName, sequentialSpace);

        this.name = name;
        String lobbyUri = "tcp://127.0.0.1:9001/Lobby?keep";
        System.out.println("Connecting to chat space " + uri + "...");
        chat = new RemoteSpace(lobbyUri);

        chat.get(new ActualField("lock"));
        chat.put(myUri.getHost() + ":" + myUri.getPort(), spaceName,name);
        chat.put(name,0);
        chat.put("lock");
    }
}
