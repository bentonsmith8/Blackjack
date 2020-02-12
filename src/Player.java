import java.util.ArrayList;
import java.util.Collections;

public class Player {
	
	// Declare player's name and define player's hand
	private String name;
	private ArrayList<Card> hand = new ArrayList<>();
	private boolean standing = false;
	
	// Constructor that initializes a player with a given name
	public Player(String name) {
		this.name = name;
		hand.ensureCapacity(5);
	}
	
	// Returns name of Player object
	public String getName() {
		return name;
	}
	
	// Returns a copy of the player's current hand
	public ArrayList<Card> getHand() {
		return new ArrayList<>(hand);
	}

	// Clears a player's hand for the next round
	public void clearHand()
	{
		hand.clear();
		standing = false;
	}
	// Returns how many cards left in player's hand
	public int getHandSize() {
		return hand.size();
	}
	
	// Adds a passed card to the player's hand and sorts it
	public void takeCard(Card card) {
		hand.add(card);
		Collections.sort(hand);
	}
	
	// Returns and removes a specified card from the player's hand 
	// Returns null if there are no more cards
	public Card playCard(int cardidx) {
		if ((cardidx < hand.size()) && (cardidx >= 0))
		{
			Card temp = hand.get(cardidx);
			hand.remove(cardidx);
			return temp;
		}
		else
			return null;
	}
	public int getValue()
	{
		int totalVal = 0;
		int aces = 0;
		for (Card c : hand) {
			if (c.getValue() != -1)
			{
				totalVal += c.getValue();
			}
			else
			{
				aces++;
			}
		}
		if (aces == 0) {
			return totalVal;
		} else {
			for (int i = 0; i < aces; i++) {
				if (totalVal + 11 > 21)
				{
					totalVal++;
				}
				else
				{
					totalVal += 11;
				}	
			}
			return totalVal;	
		}
	}
	public boolean isStanding()
	{
		return standing;
	}
	public boolean isFolded()
	{
		return (this.getValue() > 21);
	}
	public void stand()
	{
		standing = true;
	}
}

