/**
 * Models a Straight Flush Hand in Big Two
 * @author Udaria
 *
 */
public class StraightFlush extends Hand 
{

	private static final long serialVersionUID = 1L;
	/**
	 * Constructor to create Straight Flush Hand
	 * @param player Player playing currently
	 * @param cards The card the player is playing
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	/** Method returns the top card of the hand
	 *  @return It returns the Top Card 
	 */
	
	public Card getTopCard()
	{
		if(!(this.isEmpty()))
		{
			this.sort();
			return getCard(this.size()-1);
		}
		else return null;
		
	}
	/**
	 * Method for checking if this hand beats other hands
	 * @return boolean value if current hand beats the other hand
	 */
	public boolean beats(Hand hand)
	{
		if (hand == null || !hand.isValid() || !this.isValid() || this.size() != hand.size()) return false;
		
		String type = hand.getType();
		
		if (type == "Flush" || type == "FullHouse" || type == "Quad" || type == "Straight") return true;
		
		this.sort();
		Card c1 = hand.getTopCard();
		Card c2 = this.getTopCard();
		
		return (c2.compareTo(c1)>0);
		
	}
	/**
	 *  method for checking if its Valid or Not.
	 *  @returns checks if quad hand is valid or not
	 */
	public boolean isValid()
	{
		if(this.size()!=5) return false;
		
		// Suit of the first card to compare to every other card's suit.
		int suit = this.getCard(0).getSuit();
		
		for (int i = 0; i < 5 - 1; i++) 
		{
			int currentRank = this.getCard(i).getRank();
			
			currentRank = (currentRank - 2 < 0) ? 13 + currentRank - 2 : currentRank - 2;
			
			int nextRank = this.getCard(i + 1).getRank();
			
			nextRank = (nextRank - 2 < 0) ? 13 + nextRank - 2 : nextRank - 2;
			
			// They are not consecutive ranks.
			if (nextRank - currentRank != 1) return false;
			// They are not of the same suit as the first card. 
			if (this.getCard(i).getSuit() != suit) return false;
		}
		return true;
		
	}
	/**
	 * method for returning string "StraightFlush" hand
	 * @return returns the Straight Flush Hand Type
	 */
	
	public String getType() 
	{
		return "StraightFlush";	
	}
	
}
