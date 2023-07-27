/**
 * Model a Single Hand Class
 * @author Udaria
 *
 */
public class Single extends Hand 
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * construct to make a single hand
	 * @param player current player playing
	 * @param cards list of cards to choose
	 */
	public Single(CardGamePlayer player, CardList cards) 
	{
		super(player,cards);
	}
	/**
	 * checks if its valid or not
	 * @return if size is 1 returns true
	 */
	public boolean isValid()
	{
		if(this.size()==1)
		{
			return true;
		}
		
		else return false;
	}
	/**
	 * method for returning string "Single" hand
	 * @return returns the Single Hand Type
	 */
	
	public String getType()
	{
		return "Single";
	}
}
