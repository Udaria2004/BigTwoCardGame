/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a 
 * Big Two card game. It should override the compareTo() method it inherits from the Card 
 * class to reflect the ordering of cards used in a Big Two card game. Below is a detailed 
	description for the BigTwoCard class
 * @author Udaria
 *
 */
public class BigTwoCard extends Card
{

	private static final long serialVersionUID = 1L;
	/**
	 * a constructor for building a card with the specified 
		suit and rank. suit is an integer between 0 and 3, and rank is an integer between 0 and 
		12.

	 * @param suit The Suit of the Card
	 * @param rank The Rank of the Card
	 */
	public BigTwoCard(int suit, int rank)
	{
		super(suit,rank);
	}
	/**
	 * a method for comparing the order of this card with the specified card. 
     * Returns a negative integer, zero, or a positive integer when this card is less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card)
	{
		int r1 = this.rank;
		
		int r2 = card.rank;
		
		if(r1 == 0) 
			r1 = 13;
		
		else if(r1 == 1) 
			r1 = 14;
		
		if(r2 == 0) 
			r2 = 13;
		
		else if(r2 == 1) 
			r2 = 14;
		
		if(r1 < r2) 
			return -1;
		
		else if(r1 > r2) 
			return 1;
		
		else if(this.getSuit()< card.getSuit()) 
			return -1;
		
		else if(this.getSuit() > card.getSuit()) 
			return 1;
		
		else return 0;
	}
	
}
	