/**
 * model a Quad Hand Class
 * @author Udaria
 *
 */
public class Quad extends Hand 
{
	private static final long serialVersionUID = 1L;
	/**
	 * constructor to make a quad hand
	 * @param player current player playing
	 * @param cards list of cards to choose from
	 */
	public Quad(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	/**
	 * method returns the top of the card
	 * @return returns the top of the card list
	 */
	public Card getTopCard()
	{
		this.sort();
		if(this.getCard(0).getRank() == this.getCard(1).getRank()) 
		{
			return this.getCard(3);
		}
		else return this.getCard(4);
		
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
		if (type == "Flush" || type == "FullHouse" || type == "Straight") return true;
		
		else if(type == "StraightFlush") return false;
		
		this.sort();
		Card c1 = hand.getTopCard();
		Card c2 = this.getTopCard();
		
		return (c2.compareTo(c1)>0);
		
	}
	/**
	 *  method for checking if its Valid or Not.
	 *  @returns checks if Quad hand is valid or not
	 */
	public boolean isValid()
	{
		if(this.size()!=5) return false;
		
		int rank1 = this.getCard(0).getRank();
		int rank2 = -1;
		
		// To store quad and single
		CardList c1 = new CardList();
		CardList c2 = new CardList();
		
		// Get the second type of rank
		for (int i = 0; i < 5; i++) 
		{
			if (this.getCard(i).getRank() != rank1) 
			{
				rank2 = this.getCard(i).getRank();
				break;
			}
		}
		
		// Fill c1 and c2
		for (int i = 0; i < 5; i++) {
			if (this.getCard(i).getRank() == rank1) 
			{
				c1.addCard(this.getCard(i));
			} 
			else if (this.getCard(i).getRank() == rank2) 
			{
				c2.addCard(this.getCard(i));
			}
		}
		
		if (c1.size() == 4 && c2.size() == 1 || c1.size() == 1 && c2.size() == 4) 
		{
			return true;
		}
		return false;
		
	}
	/**
	 * method for returning string "Quad" hand
	 * @return returns the Quad Hand Type
	 */
	public String getType() 
	{
		return "Quad";
	}

}
