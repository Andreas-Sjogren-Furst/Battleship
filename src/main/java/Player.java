import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
public class Player {

    public static void main(String[] argv) throws InterruptedException, IOException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        String playerName = WelcomeScreen.getName();

        if (WelcomeScreen.begin() == 1){ // Joining a game
            Join join = new Join(playerName);
            System.out.println("Please connect to a player below by writing the lobby name:");
            List<Object[]> hosts = join.showPlayers();
            if(hosts.isEmpty()) System.out.println("No players are hosting :(");
            else{
                for(Object[] host : hosts){
                    System.out.println(host[0]);
                }
            } System.out.print("Enter host name: ");
            String input = scanner.next();
            Object[] t = join.chatSpace.get(new FormalField(String.class), new FormalField(String.class),new ActualField(input));
            join.connect(t[0].toString(),t[1].toString());
            Chat.connectToChat(join.chatSpace, playerName);


        } else { // Hosting a game
            String uri = "tcp://127.0.0.1:9002/?keep";
            String spaceName = playerName + "Space";
            Host host = new Host(playerName,uri,spaceName);
            Chat.createChat(host.mySpace, playerName);
        }
    }
}
