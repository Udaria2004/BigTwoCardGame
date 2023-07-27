/**
 * Model FLush Hand Type
 * @author Udaria
 *
 */
public class Flush extends Hand 
{
	private static final long serialVersionUID = 1L;
	/**
	 * constructor to create a straight hand
	 * @param player Player playing the hand currently
	 * @param cards list of cards the player can use
	 */
	public Flush(CardGamePlayer player, CardList cards) 
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
		
		if(type == "Straight")
		{
			return true;
		}
		
		else if(type == "FullHouse" || type=="Quad" || type == "StrightFlush")
		{
			return false;
		}
		
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
		if(this.size()!=5) return false;
		
		for(int i=1; i < this.size(); i++)
		{
			if(this.getCard(0).suit != this.getCard(i).suit)
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
		return "Flush";	
	}
	
	
}
