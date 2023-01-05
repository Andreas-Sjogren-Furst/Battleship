import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Lobby {
    public static final String uri = "tcp://127.0.0.1:9001/?keep";
    public static final String spaceName = "Lobby";
    public static final SpaceRepository repository = new SpaceRepository();
    public static final SequentialSpace lobbySpace = new SequentialSpace();

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        // Open a gate
        URI myUri = new URI(uri);
        String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() +  "?keep" ;
        System.out.println("Opening repository gate at " + gateUri + "...");
        repository.addGate(gateUri);
        repository.add(spaceName, lobbySpace);
        Lobby.lobbySpace.put("lock");
    }
}
