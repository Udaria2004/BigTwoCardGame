/**
 * Models a Straight Hand 
 * @author Udaria
 *
 */
public class Straight extends Hand
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * constructor to create a straight hand
	 * @param player Player playing the hand currently
	 * @param cards list of cards the player can use
	 */
	public Straight(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	/**
	 * a method for checking if this hand beats another hand
	 * @param hand The hand we are against
	 * @return returns true or false if this hand beats another hand
	 * 
	 */
	public boolean beats(Hand hand)
	{
		if (hand == null || !hand.isValid() || !this.isValid() || this.size() != hand.size()) return false;
		
		String type = hand.getType();
		if (type == "Flush" || type == "FullHouse" || type == "Quad" || type == "StraightFlush") return false;
		
		this.sort();
		Card c1 = hand.getTopCard();
		Card c2 = this.getTopCard();
		
		return (c2.compareTo(c1)>0);
		
	}
	/**
	 *  method for checking if its Valid or Not.
	 *  @returns checks if straight hand is valid or not
	 */
	public boolean isValid()
	{
		if(this.size() != 5) 
		{
			return false;
		}
		
		this.sort();
		
		for (int i = 0; i < 5 - 1; i++) 
		{
			int currentRank = this.getCard(i).getRank();
			
			currentRank = (currentRank - 2 < 0) ? 13 + currentRank - 2 : currentRank - 2;
			
			int nextRank = this.getCard(i + 1).getRank();
			
			nextRank = (nextRank - 2 < 0) ? 13 + nextRank - 2 : nextRank - 2;
			
			//  They are not of consecutive ranks.
			if (nextRank - currentRank != 1) 
			{
				return false;
			}
		}
		return true;
			
	}
	/**
	 * method for returning string "Straight" hand
	 * @return returns the Straight Hand Type
	 */
	public String getType() 
	{
		return "Straight";
	}

}
