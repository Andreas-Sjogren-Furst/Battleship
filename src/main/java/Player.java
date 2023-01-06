import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
public class Player {
    public static void main(String[] argv) throws InterruptedException, IOException, URISyntaxException {

        // Get name from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");
        String name = scanner.next();

        if (new WelcomeScreeen().begin() == 1){
            Join player = new Join(name);
            System.out.println("Please connect to a player below:");
            List<Object[]> hosts = player.showPlayers();
            if(hosts.isEmpty()) System.out.println("No players are hosting :(");
            else{
                for(Object[] host : hosts){
                    System.out.println(host[1] + ": " + host[0]);
                }
            } int input = scanner.nextInt();
            Object[] t = Lobby.lobbySpace.get(new FormalField(String.class), new FormalField(String.class),new ActualField(input));
            RemoteSpace chat = player.connect(t[0].toString(),t[1].toString());
            Chat.runChat(chat, name);

        } else {
            String uri = "tcp://127.0.0.1:9001/?keep";
            String spaceName = "oscarSpace";
            Host host = new Host(name,uri,spaceName);

            Chat.runChat(host.chat, name);
        }
    }
}
