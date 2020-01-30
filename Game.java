public class Game {

  public char[][] board = new char[PANJANG][LEBAR];
  final static int PANJANG = 8;
  final static int LEBAR = 8;
  final static char BLACK = 'X';
  final static char WHITE = 'O';

  public Game(){
    for(int i=0; i<PANJANG; i++){
      for(int j=0; j<LEBAR; j++){
          board[i][j] = ' ';
      }
    }
    board[PANJANG/2-1][PANJANG/2-1] = 'O';
    board[PANJANG/2][PANJANG/2] = 'O';
    board[PANJANG/2-1][PANJANG/2] = 'X';
    board[PANJANG/2][PANJANG/2-1] = 'X';
  }

  public Game(Game another)
	{
		for (int i = 0; i < PANJANG; i++)
		{
			for (int j = 0; j < LEBAR; j++)
			{
				this.board[i][j] = another.board[i][j];
			}
		}
	}

    //mengecek apakah move legal
     //color      warna player - Black 'X' or White 'O'
    //flip       true jika player ingin melakukan flip
  public boolean legalMove(int baris, int kolom, char color, boolean flip ){
    boolean legal = false;
    //bila cell berisi tidak ada guna dicek
    if (board[baris][kolom] == ' '){
      int posX;
      int posY;
      boolean found;
      char current;

      //search in 9 direction
      for (int x = -1; x<= 1; x++){
        for (int y = -1; y <=1; y++){
          posX = kolom + x;
          posY = baris + y;
          // index out of bound
          if (posX < 0 || posX>LEBAR-1 || posY < 0 || posY>LEBAR-1)
          {
            continue;
          }

          found = false;
          current = board[posY][posX];

          if (current == ' '|| current == color)
          {
            continue;
          }
          while(!found)
          {
            posX += x;
            posY += y;
            if (posX<0 ||  posY<0 || posX>LEBAR-1 ||  posY>LEBAR-1)
            {
              break;
            }
            current = board[posY][posX];

            if (current == color){
              found = true;
              legal = true;

              if (flip)
              {
                posX -= x;
                posY -= y;
                // index out of bound
                if (posX < 0 || posX > LEBAR-1 || posY < 0 || posY > LEBAR-1)
                {
                  continue;
                }
                current = board[posY][posX];

                while(current != ' '){
                  board[posY][posX] = color;
                  posX -= x;
                  posY -= y;
                  current = board[posY][posX];
                }
              }
            }else if (current == ' '){
              found = true;
            }
          }
        }
      }
    }
    return legal;
  }

     // A modification of legalMove() that uses a simple class to hold data
	 //  about the potential move
    public Move pointMove(int r, int c, char color, boolean flip, int[][] point)
	{
		// Initialize a default Move object
		Move newMove = new Move();

		if (board[r][c] == ' ')
		{
			int posX;
			int posY;
			boolean found;
			int current;
			int sum;

			for (int x = -1; x <= 1; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					posX = c + x;
					posY = r + y;
                    if (posX < 0 || posX > LEBAR-1 || posY < 0 || posY > LEBAR-1)
                    {
                      continue;
                    }
					found = false;
					current = board[posY][posX];
					sum = 0;

					if (current == ' ' || current == color)
					{
						continue;
					}
					else
					{
						// First piece is an enemy so add to the point count
						sum += point[posY][posX];
					}

					while (!found)
					{
						posX += x;
						posY += y;
                        if (posX < 0 || posX > LEBAR-1 || posY < 0 || posY > LEBAR-1)
                        {
                            sum = 0;
                            break;
                        }
						current = board[posY][posX];

						if (current == color)
						{
							found = true;
							newMove.legal = true;
							newMove.x = c;
							newMove.y = r;
							newMove.points += point[c][r];

							if (flip)
							{
								posX -= x;
								posY -= y;
								current = board[posY][posX];

								while(current != ' ')
								{
									board[posY][posX] = color;
									posX -= x;
									posY -= y;
									current = board[posY][posX];
								}
							}
						}
						else if (current == ' ')
						{
							// The pieces in this direction won't be flipped so reset sum to 0
							sum = 0;
							found = true;
						}
						else
						{
							// Piece is an enemy so add to the point count
							sum += point[posY][posX];
						}
					}

					// Done checking this direction so add the sum to the Move object point co
					newMove.points += sum;
				}
			}
		}
        return newMove;
    }

  public boolean isBoardFull(){
    for(int i=0; i<PANJANG; i++){
      for(int j=0; j<LEBAR; j++){
        if (board[i][j] == ' '){
          return false;
        };
      }
    }
    return true;
  }

  public boolean noLegalMove(Game game,char currentPlayer){
    // Game game= new Game();
    for(int i=0; i<PANJANG; i++){
      for(int j=0; j<LEBAR; j++){
        if (game.legalMove(j,i,currentPlayer,false)){
          return false;
        };
      }
    }
    return true;
  }

  public void printBoard(){
    System.out.print("   ");
    for (int i=0; i<PANJANG; i++){
      System.out.print(" "+i+" ");
    }
    System.out.println();
    for(int i=0; i<PANJANG; i++){
      System.out.print(" "+i+" ");
      for(int j=0; j<LEBAR; j++){
        System.out.print('|');
        System.out.print(board[i][j]);
        System.out.print('|');
      }
    System.out.println();
    }
  }

  public void countXO() {
    int countX, countO;
    countX = 0;
    countO = 0;
    for (int i=0; i<PANJANG; i++) {
      for (int j=0; j<LEBAR; j++) {
        if (board[i][j]=='O') {
          countO++;
        } else if (board[i][j] == 'X'){
          countX++;
        }
        }
      }
    System.out.print("Banyakya X : ");
    System.out.println(countX);
    System.out.print("Banyakya O : ");
    System.out.println(countO);
    }
}
