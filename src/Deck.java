import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Deck {
	
	// Define static variables for generating decks
	static private final int MIN_RANK = 1;
	static private final int MAX_RANK = 13;
	static private final char[] SUITS = {'C','D','H','S'};
	
	// Declare the card ArrayList but don't define it
	private ArrayList<Card> cards;
	
	// Default constructor initializes a new, shuffled deck of 28 cards 
	// from ranks 2-8 with all four suits
	public Deck() {
		cards = new ArrayList<>();
		cards.ensureCapacity(52);
		for (int i = MIN_RANK; i <= MAX_RANK; i++) 
		{
			for (int j = 0; j < SUITS.length; j++) 
			{
				cards.add(new Card(i, SUITS[j]));
			}
		}
		Collections.shuffle(cards);
	}
	
	// Constructs a new deck and shuffles from an ArrayList of existing cards
	public Deck(ArrayList<Card> cards) {
		this.cards = new ArrayList<>(cards);
		Collections.shuffle(this.cards);
	}
	
	// Returns a copy of the static and immutable SUITS array
	static public char[] getSuits() {
		return Arrays.copyOf(SUITS, 4);
	}
	
	// Returns the current amount of cards left in the deck
	public int size() {
		return cards.size();
	}
	
	// Returns the most last card and removes it from the deck
	// unless there is no card in which case null is returned
	public Card drawCard() {
		if (!cards.isEmpty())
		{
			Card temp = cards.get(cards.size() - 1);
			cards.remove(cards.size() - 1);
			return temp;
		}
		else
			return null;
	}

	public void add(Deck deck) {
		for (int i = 0; i < deck.size(); i++) {
			this.cards.add(deck.drawCard());
		}
	}	
	
}
