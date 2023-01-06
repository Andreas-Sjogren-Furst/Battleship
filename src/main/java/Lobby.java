import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public abstract class Lobby {
    public static final String spaceName = "lobby";
    public static URI myUri;

    private static final String uri = "tcp://127.0.0.1:9001/?keep";
    private static final SpaceRepository repository = new SpaceRepository();
    private static final SequentialSpace lobbySpace = new SequentialSpace();

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        // Open a gate
        myUri = new URI(uri);
        String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() +  "/?keep" ;
        System.out.println("Opening Lobby repository gate at: " + gateUri + "...");
        repository.addGate(gateUri);
        repository.add(spaceName, lobbySpace);
    }
}
