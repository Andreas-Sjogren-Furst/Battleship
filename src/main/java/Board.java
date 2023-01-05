import java.util.ArrayList;
import java.util.List;

public class Board {
    char[][] board;
    char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    ArrayList<Character> alphabetList = new ArrayList(List.of(alphabet));
    public Board(int length){
        this.board = new char[length][length];
        for(int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                board[i][j] = ' ';
            }
        }
    }
    public boolean isValidAttack(Board opponent, String attack){
        int x = alphabetList.indexOf(attack.toCharArray()[0]);
        int y = attack.toCharArray()[1];

        if(opponent.board[x][y] == '-' || opponent.board[x][y] == ' ') return true;
        else return false;
    }

    public void update(Board opponent, String attack){
        int x = alphabetList.indexOf(attack.toCharArray()[0]);
        int y = attack.toCharArray()[1];
        if(opponent.board[x][y] == '-'){
            opponent.board[x][y] = 'X';
            board[x][y] = 'X';
        } else {
            opponent.board[x][y] = 'O';
            board[x][y] = 'O';
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
        }
    }
}