import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
public class Player {

    public static void main(String[] argv) throws InterruptedException, IOException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        String playerName = WelcomeScreen.getName();
        String input;
        Board attackBoard;
        Board defenseBoard;

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
            while (true){
                input = scanner.next().toLowerCase();
                Object[] t = join.mySpace.queryp(new ActualField(input));
                if(t != null) break;
                System.out.println("Not a valid host name. Please try again!: ");
            }
            Object[] t = join.mySpace.get(new FormalField(String.class), new FormalField(String.class), new ActualField(input));
            join.connect(t[0].toString(),t[1].toString());

            attackBoard = new Board(10);
            defenseBoard = new Board(10);

            defenseBoard.placeAllShips();
            for(int i = 0; i < defenseBoard.board.length; i++){
                for(int j = 0; j < defenseBoard.board.length; j++){
                    if(defenseBoard.board[j][i] == '-'){
                        join.myBoard.put(defenseBoard.alphabetList.get(j).toString() + i,"join");
                    }
                }
            }
            System.out.println("what up");
            join.mySpace.put("lockJoin");

            // To draw the boards
            new Thread(() -> {
                while (true) {
                    try {
                        join.mySpace.get(new ActualField("draw"),new ActualField(2));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    attackBoard.draw();
                        defenseBoard.draw();
                    }
            }).start();

            new Thread(() -> {
                try{
                    while(true){
                        join.mySpace.get(new ActualField("p2"));
                        Object[] messageFromChat = join.mySpace.get(new FormalField(String.class), new ActualField(2), new ActualField(1));
                        if(join.myBoard.getp(new ActualField(messageFromChat[0]),new ActualField("host")) != null) {
                            attackBoard.updateAttack(attackBoard, messageFromChat[0].toString(), true);
                        } else attackBoard.updateAttack(attackBoard,messageFromChat[0].toString(),false);
                        join.mySpace.put("p1");
                        Object[] messageFromHost = join.mySpace.get(new FormalField(String.class),new ActualField(1), new ActualField(2));
                        defenseBoard.updateDefense(defenseBoard,messageFromHost[0].toString());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            Chat.connectToChat(join.mySpace, playerName, attackBoard);

        } else { // Hosting a game
            String uri = "tcp://127.0.0.1:9002/?keep";
            String spaceName = playerName + "Space";
            Host host = new Host(playerName,uri,spaceName);


            attackBoard = new Board(10);
            defenseBoard = new Board(10);
            defenseBoard.placeAllShips();
            for(int i = 0; i < defenseBoard.board.length; i++){
                for(int j = 0; j < defenseBoard.board.length; j++){
                    if(defenseBoard.board[j][i] == '-'){
                        host.myBoard.put(defenseBoard.alphabetList.get(j).toString() + i,"host");
                    }
                }
            }
            System.out.println("lockaplied");
            host.mySpace.put("lockHost");

            // To draw the boards
            new Thread(() -> {
                while (true) {
                    try {
                        host.mySpace.get(new ActualField("draw"),new ActualField(1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    attackBoard.draw();
                    defenseBoard.draw();
                }
            }).start();

            new Thread(() -> {
                try {
                    while(true){
                        host.mySpace.get(new ActualField("p1"));
                        Object[] messageFromChat = host.mySpace.get(new FormalField(String.class), new ActualField(1), new ActualField(2));
                        if(host.myBoard.getp(new ActualField(messageFromChat[0]),new ActualField("join")) != null){
                            attackBoard.updateAttack(attackBoard,messageFromChat[0].toString(),true);
                        } else attackBoard.updateAttack(attackBoard,messageFromChat[0].toString(),false);
                        host.mySpace.put("p2");
                        Object[] messageFromJoin = host.mySpace.get(new FormalField(String.class),new ActualField(2), new ActualField(1));
                        defenseBoard.updateDefense(defenseBoard,messageFromJoin[0].toString());
                    }
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }).start();

            Chat.createChat(host.mySpace, playerName, attackBoard);
        }
    }
}
