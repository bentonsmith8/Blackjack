
public class Card implements Comparable<Card> {
	
	// Declare private variables
	private int rank;
	private char suit;
	
	// Constructor with rank and suit
	public Card(int rank, char suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	// Returns rank of card
	public int getRank() {
		return rank;
	}

	public int getValue() 
	{
		if (rank >= 2 && rank <= 10)
			return rank;
		if (rank > 10)
			return 10;
		else
			return -1;
	}
	
	// Returns suit of card
	public char getSuit() {
		return suit;
	}
	
	// Returns rank + suit of card to a string for printing
	public String toString() {
		if (rank > 1 && rank < 11) 
			return "" + rank + suit;
		else if (rank == 11)
			return "J" + suit;
		else if (rank == 12)
			return "Q" + suit;
		else if (rank == 13)
			return "K" + suit;
		else
			return "A" + suit;
	}
	
	// Compares cards for sorting
	// Clubs > Diamonds > Hearts > Spades
	// largest to smallest rank-wise
	public int compareTo(Card card) {
		if (this.suit > card.getSuit())
			return 1;
		else if (this.suit == card.getSuit())
		{
			if (rank > card.getRank())
				return 1;
			else if (rank < card.getRank())
				return -1;
			else return 0;
		}
		else 
			return -1;
	}
}
