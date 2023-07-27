/**
 * Model Full House Hand 
 * @author Udaria
 *
 */
public class FullHouse extends Hand 
{

	private static final long serialVersionUID = 1L;
	/**
	 * constructor to create a Full House hand
	 * @param player Player playing the hand currently
	 * @param cards list of cards the player can use
	 */
	public FullHouse(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	/**
	 * Method to retrieve the top card of the hand
	 * @return Top card of the hand
	 */
	public Card getTopCard()
	{
		this.sort();
		int r1=-1,r2=-1;
		int c1=0;
		int c2=0;
		for(int i = 0; i<this.size(); i++)
		{
			if(r1==-1)
				r1=this.getCard(i).rank;
			
			if(r1==-1 && r2==-1 && r1!=this.getCard(i).rank)
				r2=this.getCard(i).rank;
			
			if(r1==this.getCard(i).rank)
				c1++;
			else if(r2==this.getCard(i).rank)
				c2+=1;
		}
		
		if(c1==3) return this.getCard(2);
		else if(c2==3) return this.getCard(4);
		return null;
		
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
		
		String type= hand.getType();
		if(type == "Straight" || type =="Flush")
			return true;
		else if(type == "Quad" || type == "StraightFlush" )
			return false;
		
		this.sort();
		Card c1 = hand.getTopCard();
		Card c2 = this.getTopCard();
		
		return (c2.compareTo(c1)>0);
		
	}
	/**
	 *  method for checking if its Valid or Not.
	 *  @returns checks if FullHouse hand is valid or not
	 */
	public boolean isValid()
	{
		if(this.size()!=5) return false;
		
		this.sort();
		
		int r1 = -1 , r2 = -1;
		int c1 = 0 , c2 = 0;
		
		for(int i = 0; i<this.size(); i++)
		{
			if(r1==-1)
				r1=this.getCard(i).rank;
			
			if(r1==-1 && r2==-1 && r1!=this.getCard(i).rank)
				r2=this.getCard(i).rank;
			
			if(r1==this.getCard(i).rank)
				c1++;
			else if(r2==this.getCard(i).rank)
				c2++;
		}
		
		if((c1==2 && c2==3) || (c1==3 && c2==2))
			return true;
		return false;
	}
	/**
	 * method for returning string "Straight" hand
	 * @return returns the Full House Hand Type
	 */
	public String getType() 
	{
		return "FullHouse";
	}	
	
}
