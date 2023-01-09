import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
    char[][] board;
    String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String[] integers = {"0","1","2","3","4","5","6","7","8","9"};
    ArrayList<String> alphabetList = new ArrayList(List.of(alphabet));
    ArrayList<String> integersList = new ArrayList(List.of(integers));
    public Board(int length){
        this.board = new char[length][length];
        for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                board[i][j] = ' ';
            }
        }
    }
    public boolean isValidAttack(Board opponent, String attack){
        if(attack.length() != 2) return false;

        int x = alphabetList.indexOf(attack.split("")[0]);
        int y = integersList.indexOf(attack.split("")[1]);

        if(x == -1 || y == -1) return false;

        if(opponent.board[y][x] == '-' || opponent.board[y][x] == ' ') return true;
        else return false;
    }

    public void updateDefense(Board opponent, String attack){
        int x = alphabetList.indexOf(attack.split("")[0]);
        int y = integersList.indexOf(attack.split("")[1]);
        if(opponent.board[y][x] == '-'){
            opponent.board[y][x] = 'X';
            board[y][x] = 'X';
        } else {
            opponent.board[y][x] = 'O';
            board[y][x] = 'O';
        }
    }

    public void updateAttack(Board opponent, String attack, boolean hit){
        int x = alphabetList.indexOf(attack.split("")[0]);
        int y = integersList.indexOf(attack.split("")[1]);
        if(hit){
            opponent.board[y][x] = 'X';
            board[y][x] = 'X';
        } else {
            opponent.board[y][x] = 'O';
            board[y][x] = 'O';
        }
    }

    public boolean validPlacement(String coordinate1, String coordinate2, int lengthShip){

        // x and y for positions for coordinate 1
        int x1 = alphabetList.indexOf(coordinate1.split("")[0]);
        int y1 = Integer.parseInt(coordinate1.split("")[1]);

        // x and y positions for coordinate 2
        int x2 = alphabetList.indexOf(coordinate2.split("")[0]);
        int y2 = Integer.parseInt(coordinate2.split("")[1]);


        // Is coordinates outside the board
        if((x1 >= board.length) || (x2 >= board.length) || (y1 >= board.length) ||(y2 >= board.length)) return false;
        else if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) return false;

        // Check if ship is already placed on that position
        if(x1 == x2){
            if (Math.abs(y2-y1) != lengthShip - 1) return false;
            if(y2 > y1){
                for(int i = y1; i <= y2; i++){
                    if(board[i][x1] == '-') return false;
                }
            } else {
                for(int i = y2; i <= y1; i++){
                    if(board[i][x1] == '-') return false;
                }
            }
        } else if (y1 == y2){
            if (Math.abs(x2-x1) != lengthShip - 1) return false;
            if(x2 > x1){
                for(int i = x1; i <= x2; i++){
                    if(board[y1][i] == '-') return false;
                }
            }
        } else {
            for(int i = x2; i <= x1; i++){
                if(board[y1][i] == '-') return false;
            }
        }
        return true;
    }

    public boolean placeAllShips() {
        draw();
        Scanner scanner = new Scanner(System.in);
        //int[] ships = {2,3,3,4,5};
        int[] ships = {2};
        System.out.println("## Are you ready to place ships? ##");
        System.out.println(" -When placing your ships, write the two end coordinates of the ship");
        int index = 0;
        String[] input = new String[2];
        while(index < ships.length){
            System.out.println("Place ship with size: " + ships[index]);
            System.out.print("Enter coordinate for front of the ship: ");
            input[0] = scanner.next();
            System.out.print("Enter the coordinate for end of the ship: ");
            input[1] = scanner.next();
            System.out.println();
            if(validPlacement(input[0],input[1],ships[index])){
                placeShip(input[0],input[1],ships[index]);
                index++;
            } else {
                System.out.println("The ship placement is not valid, please try again !");
            } draw();
        }
        return true;
    }

    public void placeShip(String coordinate1, String coordinate2, int lengthShip){
        // x and y for positions for coordinate 1
        int x1 = alphabetList.indexOf(coordinate1.split("")[0]);
        int y1 = Integer.parseInt(coordinate1.split("")[1]);

        // x and y positions for coordinate 2
        int x2 = alphabetList.indexOf(coordinate2.split("")[0]);
        int y2 = Integer.parseInt(coordinate2.split("")[1]);

        if(x1 == x2){
            if(y2 > y1){
                for(int i = y1; i <= y2; i++){
                    board[i][x1] = '-';
                }
            } else {
                for(int i = y2; i <= y1; i++){
                    board[i][x1] = '-';
                }
            }
        } else {
            if(x2 > x1){
                for(int i = x1; i <= x2; i++){
                    board[y1][i] = '-';
                }
            } else {
                for(int i = x2; i <= x1; i++){
                    board[y1][i] = '-';
                }
            }
        }
    }

    public void draw() {
        for (int i = 0; i < board.length; i++){
            System.out.print(i + " ");
            for (int j = 0; j < board.length; j++){
                System.out.print("|");
                System.out.print(board[i][j]);
            } System.out.print("|");
            System.out.println();
        } System.out.print("   ");
        for(int i = 0; i < board.length; i++){
            System.out.print(alphabet[i] + " ");
        } System.out.println();
    }

    public boolean shoot(String attack, Board attackBoard, Board defenseBoard){
        int coordinate1 = alphabetList.indexOf(attack.split("")[0]);
        int coordinate2 = Integer.parseInt(attack.split("")[1]);

        if(coordinate1 == -1 || coordinate2 > 9 || coordinate2 < 0) return false;

        if(isValidAttack(attackBoard, attack)){
            if(attackBoard.board[coordinate1][coordinate2] == '-') {
                attackBoard.board[coordinate1][coordinate2] = 'X';
                defenseBoard.board[coordinate1][coordinate2] = 'X';
            } else {
                attackBoard.board[coordinate1][coordinate2] = 'O';
                defenseBoard.board[coordinate1][coordinate2] = 'O';
            }
            return true;
        } else return false;
    }
}