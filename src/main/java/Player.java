import org.jspace.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
public class Player {

    public static boolean gameOver = false;
    public static Scanner scanner = new Scanner(System.in);
    public static String playerName = WelcomeScreen.getName();
    public static String input;

    public static void main(String[] argv) throws InterruptedException, IOException, URISyntaxException {
        Login.begin();
        while(true){
            if (WelcomeScreen.begin() == 1){ // Joining a game

                // Creates a player as joining
                Join join = new Join(playerName);

                // Joining a game
                if(joinHost(join) == 1) continue;

                // Place ships on board
                join.defenseBoard.placeAllShips(join.attackBoard, join.defenseBoard);

                // Upload ships to board space
                uploadShips(join.boardSpace,join.defenseBoard,join.name);

                //Put the lock for being ready
                join.chatSpace.put("lockJoin");
                join.boardSpace.put(join.name,"nameJoin");

                // Get opponent name
                Object[] opponentsName = join.boardSpace.get(new FormalField(String.class),new ActualField("nameHost"));

                // Checks if game is over
                startGameOver(join.boardSpace,opponentsName[0].toString());

                // Checks if join has left the game
                startLeaveGame(join.boardSpace);

                // Start thread to draw board in terminal
                drawBoardInTerminal(join.chatSpace,join.attackBoard,join.defenseBoard,join.playerId);

                // Receives shoot from host
                startShootJoin(join, opponentsName[0].toString());

                // Start thread for chatting host
                Chat.startChat(join.chatSpace, join.boardSpace, playerName, join.attackBoard,join.playerId,"joinCanShoot");

            } else { // Hosting a game

                // Creates a player as Host
                Host host = new Host(playerName);

                // Place ships on board
                host.defenseBoard.placeAllShips(host.attackBoard,host.defenseBoard);

                // Upload ships to board space
                uploadShips(host.boardSpace,host.defenseBoard,host.name);

                //Put the lock for being ready
                host.chatSpace.put("lockHost");
                host.boardSpace.put(host.name,"nameHost");

                // Get opponent name
                Object[] opponentsName = host.boardSpace.get(new FormalField(String.class),new ActualField("nameJoin"));

                // Checks if game is over
                startGameOver(host.boardSpace,opponentsName[0].toString());

                // Checks if user has left the game
                startLeaveGame(host.boardSpace);

                // Start thread for drawing board in terminal
                drawBoardInTerminal(host.chatSpace,host.attackBoard,host.defenseBoard,host.playerId);

                // Start thread to shooting in terminal
                startShootHost(host, opponentsName[0].toString());

                // Start thread for chatting with join
                Chat.startChat(host.chatSpace, host.boardSpace, playerName, host.attackBoard,host.playerId, "hostCanShoot");
            }
        }
    }

    public static void startShootHost(Host host, String joinName) throws InterruptedException {
        new Thread(() -> {
            try {
                while(!gameOver){
                        host.chatSpace.get(new ActualField("hostUpdate"));
                        Object[] messageFromHost = host.chatSpace.get(new FormalField(String.class),new ActualField(host.opponentId), new ActualField(host.opponentId));
                        host.defenseBoard.updateDefense(messageFromHost[0].toString());
                        if(host.boardSpace.queryAll(new FormalField(String.class), new ActualField(host.name)).isEmpty()){
                            host.boardSpace.put("gameOver",host.name);
                            gameOver = true;
                            host.defenseBoard.draw(host.attackBoard,host.defenseBoard);
                            System.out.println("You lost the game :(");
                        } else host.defenseBoard.draw(host.attackBoard,host.defenseBoard);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            while(!gameOver){
                try{
                    while(host.chatSpace.queryp(new ActualField("p1")) != null){
                        host.chatSpace.get(new ActualField("p1"));
                        host.chatSpace.put("hostCanShoot");
                        Object[] messageFromChat = host.chatSpace.get(new FormalField(String.class), new ActualField(host.playerId), new ActualField(host.playerId));
                        if(host.boardSpace.getp(new ActualField(messageFromChat[0]),new ActualField(joinName)) != null) {
                            host.attackBoard.updateAttack(messageFromChat[0].toString(), true);
                            host.chatSpace.put("p1");
                        } else {
                            host.attackBoard.updateAttack(messageFromChat[0].toString(),false);
                            host.chatSpace.put("p2");
                        } host.chatSpace.put("joinUpdate");
                        host.attackBoard.draw(host.attackBoard,host.defenseBoard);
                        host.chatSpace.get(new ActualField("hostCanShoot"));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void startShootJoin(Join join, String hostName){

        new Thread(() -> {
            try {
                while(!gameOver){
                        join.chatSpace.get(new ActualField("joinUpdate"));
                        Object[] messageFromHost = join.chatSpace.get(new FormalField(String.class),new ActualField(join.opponentId), new ActualField(join.opponentId));
                        join.defenseBoard.updateDefense(messageFromHost[0].toString());
                        if(join.boardSpace.queryAll(new FormalField(String.class), new ActualField(join.name)).isEmpty()){
                            join.boardSpace.put("gameOver",join.name);
                            gameOver = true;
                            join.defenseBoard.draw(join.attackBoard,join.defenseBoard);
                            System.out.println("You lost the game :(");
                         } else join.defenseBoard.draw(join.attackBoard,join.defenseBoard);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> { // For Sending attacks
            try{
                while(!gameOver){
                    while(join.chatSpace.queryp(new ActualField("p2")) != null){
                        join.chatSpace.get(new ActualField("p2"));
                        join.chatSpace.put("joinCanShoot");
                        Object[] messageFromChat = join.chatSpace.get(new FormalField(String.class), new ActualField(join.playerId), new ActualField(join.playerId));
                        if(join.boardSpace.getp(new ActualField(messageFromChat[0]),new ActualField(hostName)) != null) {
                            join.attackBoard.updateAttack(messageFromChat[0].toString(), true);
                            join.chatSpace.put("p2");
                        } else {
                            join.attackBoard.updateAttack(messageFromChat[0].toString(),false);
                            join.chatSpace.put("p1");
                        } join.chatSpace.put("hostUpdate");
                        join.attackBoard.draw(join.attackBoard,join.defenseBoard);
                        join.chatSpace.get(new ActualField("joinCanShoot"));
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void drawBoardInTerminal(Space chatSpace, Board attackBoard, Board defenseBoard, int playerId){
        // To draw the boards
        new Thread(() -> {
            while (!gameOver) {
                try {
                    chatSpace.get(new ActualField("draw"),new ActualField(playerId));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                attackBoard.draw(attackBoard,defenseBoard);
            }
        }).start();
    }

    public static void uploadShips(Space boardSpace, Board defenseBoard, String name) throws InterruptedException {
        for(int i = 0; i < defenseBoard.board.length; i++){
            for(int j = 0; j < defenseBoard.board.length; j++){
                if(defenseBoard.board[i][j] == '-'){
                    boardSpace.put(defenseBoard.alphabetList.get(j).toString() + i,name);
                }
            }
        }
    }

    public static void startGameOver(Space boardSpace, String opponentsName) {
        new Thread(() -> {
            while (!gameOver) {
                try {
                    boardSpace.get(new ActualField("gameOver"), new ActualField(opponentsName));
                    gameOver = true;
                    System.out.println("Congratulations you won the game :)");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void startLeaveGame(Space boardSpace){
        new Thread(() -> {
            while(!gameOver){
                try {
                    Object[] t = boardSpace.query(new ActualField("leave"),new FormalField(String.class));
                    System.out.println(t[1] + " Left the game");
                    gameOver = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static int joinHost(Join join) throws InterruptedException, IOException {
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        while (scanner.ready()) {
            scanner.readLine();
        }
        Object[] hostName = null;
        System.out.println("--- PLEASE CONNECT TO A LOBBY ---");
        List<Object[]> hosts = join.showPlayers();
        while(hosts.isEmpty()){
            System.out.println("No players are hosting a game :(");
            System.out.println("Write: 1 for reset, Write: 2 for hosting a game");
            input = scanner.readLine();
            if(input.equals("2")) return 1;
            else hosts = join.showPlayers();
        }

        for(Object[] host : hosts) System.out.println(host[2]);
        while(hostName == null) {
            System.out.print("ENTER HOST NAME: ");
            input = scanner.readLine().toLowerCase();
            hostName = join.chatSpace.getp(new FormalField(String.class), new FormalField(String.class), new ActualField(input));
            if(hostName == null) System.out.println("--- NO HOST WITH THAT NAME, TRY AGAIN: ---: ");
        } join.connect(hostName[0].toString(),hostName[1].toString());
        return 0;
    }
}


