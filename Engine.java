import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Engine {
    // Board for the point
	static int pointTable[][] = new int[8][8];
	// Value list that will be used to choose between hard or easy bot
	// The first one is hard, the second one is easy
	static int pointList[][] = new int[2][7];
	// Difficulty in game, 0 for hard, 1 for easy
	int DIFFICULTY;
	// Values for the board
    int CORNER;
	int DIAGONAL;
	int SECOND;
	int THIRD;
	int FOURTH;
	int COMMON;
	int STARTER;

    // Weight values for potential moves
    double POSITIONWEIGHT = 5;
	double MOBILITYWEIGHT = 15;
    double ENDWEIGHT = 300;

    // Infinite number for searching in minimax
    double INFINITE = 100000000;

    // Max depth for the tree
    int MAXDEPTH = 2;

    // Making the board
    public void makePointBoard(int DIF) {
		// Assigning pointList Element
		// Hard
		pointList[0][0] = 50;
		pointList[0][1] = -10;
		pointList[0][2] = -1;
		pointList[0][3] = 5;
		pointList[0][4] = 2;
		pointList[0][5] = 1;
		pointList[0][6] = 0;
		// Easy
		pointList[0][0] = 5;
		pointList[1][1] = 1;
		pointList[1][2] = 2;
		pointList[1][3] = 5;
		pointList[1][4] = 4;
		pointList[1][5] = 3;
		pointList[1][6] = 6;
		// Assigning values for the board from pointList
		DIFFICULTY = DIF;
		// In this case, we use the easy bot
		int CORNER = pointList[DIFFICULTY][1];
		int DIAGONAL = pointList[DIFFICULTY][2];
		int SECOND = pointList[DIFFICULTY][3];
		int THIRD = pointList[DIFFICULTY][3];
		int FOURTH = pointList[DIFFICULTY][4];
		int COMMON = pointList[DIFFICULTY][5];
    	int STARTER = pointList[DIFFICULTY][6];
        // Values for top left quadrant
        pointTable[0][0] = CORNER;
		pointTable[1][1] = DIAGONAL;
		pointTable[0][1] = pointTable[1][0] = SECOND;
		pointTable[0][2] = pointTable[2][0] = THIRD;
		pointTable[0][3] = pointTable[3][0] = FOURTH;
		pointTable[1][2] = pointTable[1][3] = pointTable[3][2] = pointTable[3][3] =
		pointTable[2][3] = pointTable[3][1] = pointTable[4][3] = COMMON;
        pointTable[3][3] = STARTER;

        // Values for top right quadrant
        for (int i = 4; i <= 7; i++)
		{
			for (int j = 0; j <= 3; j++)
			{
				pointTable[j][i] = pointTable[(j)][9-i];
			}
        }

        // Values for bottom quadrants
        for (int i = 0; i <= 7; i++)
		{
			for (int j = 4; j <= 7; j++)
			{
				pointTable[j][i] = pointTable[9-j][(i)];
			}
        }
    }

    // Constructor
    public Engine(int DIF) {
        makePointBoard(DIF);
    }

    public Game searchStrategy(Game game, boolean isDone, char color) {
        if(isDone == false) {
            Node currentState = new Node(new Move(), game, color);
			currentState = buildTree(currentState, MAXDEPTH);
			//printTree(currentState);

			Move bestMove = minimax(currentState, color);
			//System.out.println("Best move is " + testMove.x + " " + testMove.y);

			if (bestMove.legal)
			{
				game.pointMove(bestMove.y, bestMove.x, color, true, pointTable);
				game.board[bestMove.y][bestMove.x] = color;
			}
        }
        return game;
    }

    public Node buildTree(Node parent, int depth)
	{
		// Check to see if there is still more to expand and if the game isn't done
		if (depth > 0 && parent.end == -1)
		{
			// Decrease the depth count
			depth--;

			// Determine the subsequent turn ahead of time
			char nextTurn;
			if (parent.turn == Game.WHITE)
				nextTurn = Game.BLACK;
			else
				nextTurn = Game.WHITE;

			// Searches for legal moves
			for (int i = 0; i <= 7; i++)
			{
				for (int j = 0; j <= 7; j++)
				{
					// Store the current move being checked
					Move currentMove = parent.state.pointMove(i, j,parent.turn, false, pointTable);

					// If it is legal
					if (currentMove.legal)
					{
						// Create a Game object that attempts the current move
						Game futureGame = new Game(parent.state);
						futureGame = makeMove(futureGame, false,(char)parent.turn, currentMove);

						// Create a Node that holds the current move, the future game
						Node newNode = new Node(currentMove, futureGame, nextTurn);

						// The position value of the new node is the number of points gained by the move leading up to that node
						newNode.position = currentMove.points;
						// Check the number of potential moves of the future game
						newNode.mobility = mobilityCheck(futureGame, nextTurn);
						// Checked whether or not the future game has ended
						newNode.end =  endCheck(futureGame);

						parent.addChild(newNode);
						newNode.parent = parent;
					}
				}
			}

			// Check again to see if there is still more to expand
			if (depth > 0)
			{
				// Build a sub tree for each child
				for (Node n : parent.children)
				{
					n = buildTree(n, depth);
				}
			}

			// Calculate mobility while recursing backwards
			parent.mobility = parent.children.size();
		}
		return parent;
	}

    public Move minimax(Node root, int color)
	{
		double max = 0;
		Node bestNode = root;

		// Check every node in children
		for (Node n : root.children)
		{
			// Find the node with the best value
			double tempMin = minValue(n, color);
			if (bestNode == root)
			{
				max = tempMin;
				bestNode = n;
			}
			else if (tempMin > max)
			{
				max = tempMin;
				bestNode = n;
			}
			else if (tempMin <= max)  // If the maximum value of the next node < current min value(alpha < beta)
			{						  // , then continue to next node without searching the child(alpha-beta pruning)
				break;
			}
		}

		return bestNode.last;
	}

	 //Calculates the min-value of a given state

	public double minValue(Node check, int color)
	{
		double min = INFINITE;

		// Check to see if this Node is a leaf
		if (check.children.size() == 0)
		{
			// Check to see if the game stored in this Node has ended
			if (check.end == -1)
			{
				// Calculate the value based on position and mobility
				min = check.position * POSITIONWEIGHT - check.mobility * MOBILITYWEIGHT;

				//System.out.println("Node: " + check.last.x + " " + check.last.y + " scores " + min + " points");
			}
			else if (check.end == color)
			{
				// The player has won, return
				min = ENDWEIGHT;
				return min;
			}
			else if (check.end != color)
			{
				// The player has lost or it is a tie, return
				min = -ENDWEIGHT;
				return min;
			}
		}

		// Look for the node among the children with the least value
		for (Node n : check.children)
		{
			double tempMin = maxValue(n, color);
			if (tempMin < min)
			{
				min = tempMin;
			}
		}

		return min;
	}

	//  Calculates the max-value of a given state
	public double maxValue(Node check, int color)
	{
		double max = -INFINITE;

		// Check to see if this Node is a leaf
		if (check.children.size() == 0)
		{
			// Check to see if the game stored in this Node has ended
			if (check.end == -1)
			{
				// Calculate the value based on position and mobility
				max = -check.position * POSITIONWEIGHT + check.mobility * MOBILITYWEIGHT;

				//System.out.println("Node: " + check.last.x + " " + check.last.y + " scores " + max + " points");
			}
			else if (check.end == color)
			{
				// The player has won, return
				max = ENDWEIGHT;
				return max;
			}
			else if (check.end != color)
			{
				// The player has lost or it is a tie, return
				max = -ENDWEIGHT;
				return max;
			}
		}

		// Look for the node among the children with the least value
		for (Node n : check.children)
		{
			double tempMax = minValue(n, color);
			if (tempMax > max)
			{
				max = tempMax;
			}
		}

		return max;
    }

    public Game makeMove(Game game, boolean done, char color, Move move)
	{
		if (!done)
		{
			if (move.legal)
			{
				game.pointMove(move.y, move.x, color, true, pointTable);
				game.board[move.y][move.x] = color;
			}
		}

        return game;
    }

    //  Checks to see how many potential moves can be made from this game
    public int mobilityCheck(Game game, char color)
	{
		int result = 0;

		for (int i=1; i<game.PANJANG-1; i++)
		{
            for (int j=1; j<game.LEBAR-1; j++)
			{
				if ((game.legalMove(i, j, color, false)))
				{
                    result++;
				}
			}
		}

        return result;
    }

    //Checks to see if a given game is finished
    public int endCheck(Game game)
	{
		int result = -1;
		int whiteSum = 0;
		int blackSum = 0;

		for (int i=0; i<game.PANJANG-1; i++)
		{
            for (int j=0; j<game.LEBAR-1; j++)
			{
				if ((game.legalMove(i, j, Game.BLACK, false)) ||
                   (game.legalMove(i, j, Game.WHITE, false)))
				{
                    result = -1;
					return result;
				}

				if (game.board[i][j] == Game.BLACK)
				{
					blackSum++;
				}
				else if (game.board[i][j] == Game.WHITE)
				{
					whiteSum++;
				}
			}
		}

		if (blackSum > whiteSum)
		{
			result = 1;//Black Won
		}
		else if (whiteSum > blackSum)
		{
			result = 2;//White won
		}
		else
		{
			result = 0;
		}

        return result;
    }

    public void printTree(Node parent)
	{
		ArrayList<Node> q = new ArrayList<Node>();
		Node temp = parent;
		for (Node n : temp.children)
		{
			q.add(n);
		}

		System.out.println("Node: " + temp.last.x + " " + temp.last.y);

		while (!q.isEmpty())
		{
			temp = (Node)q.remove(0);
			System.out.println("Node: " + temp.last.x + " " + temp.last.y +
								" (Parent: " + temp.parent.last.x + " " + temp.parent.last.y + ")");
			System.out.println("Position: " + temp.position);
			System.out.println("Mobility: " + temp.mobility);
			System.out.println("End: " + temp.end);

			for (Node n : temp.children)
			{
				q.add(n);
			}
		}
	}

}
