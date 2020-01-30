import java.util.Scanner;
import java.util.*;

public class Othello{
    public void mode1(int DIF){
        Scanner sc = new Scanner(System.in);  // Create a Scanner object
        Game game= new Game();

        game.printBoard();
        game.countXO();
        while(!game.isBoardFull() && (!game.noLegalMove(game,'O') || !game.noLegalMove(game,'X'))){
          System.out.println("player1 O : Enter row column");
          int row = sc.nextInt();
          int column = sc.nextInt();
          while(!game.legalMove(row,column,'O',true)){
              System.out.println("MOVE TIDAK LEGAL!!!");
              System.out.println("player1 O : Enter row column");
              row = sc.nextInt();
              column = sc.nextInt();
          }
          game.board[row][column] = 'O';
          game.printBoard();
          game.countXO();

          Engine bot = new Engine(DIF);
          bot.searchStrategy(game, game.noLegalMove(game,'X'), 'X');
          game.printBoard();
          game.countXO();
        }
    }

    //game mode bot random melawan manusia
    public void mode2(){
        Scanner sc = new Scanner(System.in);  // Create a Scanner object
        Game game= new Game();

        //Create class random
        Random rand = new Random();

        game.printBoard();
        while(!game.isBoardFull() && (!game.noLegalMove(game,'O') || !game.noLegalMove(game,'X'))){
          System.out.println("player1 O : Enter row column");
          int row = sc.nextInt();
          int column = sc.nextInt();
          while(!game.legalMove(row,column,'O',true)){
              System.out.println("MOVE TIDAK LEGAL!!!");
              System.out.println("player1 O : Enter row column");
              row = sc.nextInt();
              column = sc.nextInt();
          }
          game.board[row][column] = 'O';
          game.printBoard();


          int row_bot = rand.nextInt(8);
          int column_bot = rand.nextInt(8);
          while(!game.legalMove(row_bot,column_bot,'X',true) && !game.noLegalMove(game,'X')){
              row_bot = rand.nextInt(8);
              column_bot = rand.nextInt(8);
          }
          System.out.println("row bot : "+ row_bot);
          System.out.println("column bot : "+ column_bot);
          game.board[row_bot][column_bot] = 'X';
          game.printBoard();
          game.countXO();
        }
    }

    public void mode3(){
        Game game= new Game();

        //Create class random
        Random rand = new Random();

        game.printBoard();
        game.countXO();
        while(!game.isBoardFull() && (!game.noLegalMove(game,'O') || !game.noLegalMove(game,'X'))){
            int row_bot = rand.nextInt(8);
            int column_bot = rand.nextInt(8);
            while(!game.legalMove(row_bot,column_bot,'O',true) && !game.noLegalMove(game,'O')){
                row_bot = rand.nextInt(8);
                column_bot = rand.nextInt(8);
            }
              game.board[row_bot][column_bot] = 'O';
              game.printBoard();
              game.countXO();

              Engine bot = new Engine(0);
              bot.searchStrategy(game, game.noLegalMove(game,'X'), 'X');
              game.printBoard();
              game.countXO();
        }
    }


  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);  // Create a Scanner object
    Othello othello = new Othello();
    System.out.println("Pilih mode yang akan dimainkan");
    System.out.println("mode 1 : Player(O) vs Bot minimax(X)");
    System.out.println("mode 2 : Player(O) vs Bot random(X)");
    System.out.println("mode 3 : Bot random(O) vs Bot minimax(X)");
    System.out.print("Enter nomor mode : ");
    int mode = sc.nextInt();
		System.out.println("0 = hard, 1 = easy");
		System.out.print("Enter nomor kesulitan : ");
		int DIFFICULTY = sc.nextInt();
    if(mode == 1)
        othello.mode1(DIFFICULTY);
    else if(mode == 2)
        othello.mode2();
    else if(mode == 3)
        othello.mode3();
    else
        othello.mode3();
  }

}
