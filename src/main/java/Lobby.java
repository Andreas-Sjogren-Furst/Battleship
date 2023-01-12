import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class Lobby {
    public static final String lobbyName = "lobby";
    public static final String loginName = "login";
    public static URI myUri;

    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<String> passwords = new ArrayList<>();

    private static final String uri = "tcp://127.0.0.1:9001/?keep";
    private static final SpaceRepository repository = new SpaceRepository();
    private static final SequentialSpace lobbySpace = new SequentialSpace();
    private static final SequentialSpace loginSpace = new SequentialSpace();

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        // Open a gate
        myUri = new URI(uri);
        String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() +  "/?keep" ;
        System.out.println("Opening Lobby repository gate at: " + gateUri + "...");
        repository.addGate(gateUri);
        repository.add(lobbyName, lobbySpace);
        repository.add(loginName, loginSpace);
        isValidLogin();
        isValidCreateAccount();
    }

    public static void isValidLogin(){
        new Thread(() -> {
            try {
                while(true){
                    Object[] loginCredentials = loginSpace.get(new FormalField(String.class), new FormalField(String.class));
                    if(usernames.contains(loginCredentials[0].toString()) && passwords.contains(loginCredentials[1].toString())){
                        loginSpace.put("true", loginCredentials[0].toString());
                    } loginSpace.put("false", loginCredentials[0].toString());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }}).start();
    }

    public static void isValidCreateAccount(){
        new Thread(() -> {
            try {
                while(true){
                    Object[] loginCredentials = loginSpace.get(new FormalField(String.class), new FormalField(String.class), new ActualField("createAccount"));
                    if(usernames.contains(loginCredentials[0].toString())){
                        loginSpace.put("true", loginCredentials[0].toString(), "createAccount");
                        System.out.println("hhhhhhhh");
                    } loginSpace.put("false", loginCredentials[0].toString(), "createAccount");
                    System.out.println("ooooooooo");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }}).start();
    }
}
