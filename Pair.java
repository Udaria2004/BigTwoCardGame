
/**
 * model Pair Hand
 * @author Udaria
 *
 */
public class Pair extends Hand 
{

	private static final long serialVersionUID = 1L;
	/**
	 * constructor to make Pair Hand
	 * @param player current player playing this hand
	 * @param cards list of cards to choose from
	 */
	public Pair(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	/**
	 *  method for checking if its Valid or Not.
	 *  @returns checks if pair hand is valid or not
	 */
	public boolean isValid() 
	{
		if(this.size() == 2) 
		{
			if(this.getCard(0).getRank() == this.getCard(1).getRank()) 
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * method for returning string "Pair" hand
	 * @return returns the Pair Hand Type
	 */
	public String getType() 
	{
		return "Pair";
	}
	
}
