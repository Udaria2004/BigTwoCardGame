
/**
 * Models a Triple Hand in Big Two
 * @author Udaria
 *
 */
public class Triple extends Hand 
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param player Current Player playing the hand
	 * @param cards  The list of cards the player is playing  
	 */

	public Triple(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	/** method to check if the card is a valid triple hand
	 * @return It returns a boolean to check if the Triple Hand is valid
	 */
	public boolean isValid() 
	{
		if (this.size() != 3) 
		{
			return false;
		}
		
		// Rank of the first card
		int rank = this.getCard(0).getRank();
		for (int i = 0; i < 3; i++) 
		{
			
			if (this.getCard(i).getRank() != rank) 
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * a method which returns a string specifying the type of card
	 * @return It returns the Triple Hand Card
	 */
	public String getType() 
	{
		return "Triple";	
	}
}
