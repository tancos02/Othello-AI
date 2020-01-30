import java.util.ArrayList;

public class Node
{
	// Move last is the move used to reach this game, root nodes have a default Move object as last
	// Game state is the state of the game after attempting last
	// int turn is the player whose actions will be used to generate new children, if any
	Move last;
	Game state;
	char turn;

	// Position is calculated by taking the amount of points acquired by Move last
	int position = 0;
	// Mobility represents every possible move that can occur immediately after this state
	int mobility = 0;
	// End indicates when the game is over and the winner, if any,
	// -1: the game is not over
	// 0: the game is a tie
	// 1: Black won the game
	// 2: White won the game
	int end = -1;

	// Parent and children
	Node parent;
	ArrayList<Node> children = new ArrayList<Node>();

	public Node(Move last, Game state, char turn)
	{
		this.last = last;
		this.state = state;
		this.turn = turn;
	}

	public void addChild(Node newChild)
	{
		if (children == null)
		{
			System.out.println("Yes");
		}
		else
		{
			children.add(newChild);
		}
	}
}
