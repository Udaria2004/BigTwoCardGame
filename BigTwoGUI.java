import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build a GUI for 
   the Big Two card game and handle all user actions. Below is a detailed description for the 
   BigTwoGUI class
 * @author Udaria
 *
 */


public class BigTwoGUI implements CardGameUI
{
	
	/*
	 * A Big Two game associates with this GUIw
	 */
	private BigTwo game;
	
	/*
	 * A boolean array indicating which cards are being selected.
	 */
	private boolean[] selected;
	
	/*
	 * An integer specifying the index of the active player.
	 */
	private int activePlayer;
	
	/*
	 * The main window of the application.
	 */
	private JFrame frame;
	
	/*
	 * A panel for showing the cards of each player and the cards played on the table.
	 */
	private JPanel bigTwoPanel;
	
	/*
	 * A “Play” button for the active player to play the selected cards.
	 */
	private JButton playButton;
	
	/*
	 * A “Pass” button for the active player to pass his/her turn to the next player.
	 */
	private JButton passButton;
	
	/*
	 * A text area for showing the current game status as well as end of game messages.
	 */
	private JTextArea msgArea;
	/*
	 * A text area showing the chat messages sent by Players
	 */
	private JTextArea chatArea;
	/*
	 *  A text area for players to input the chat messages
	 */
	private JTextField chatInput;
	
	/*
	 * A 2D array storing the images for the faces of the cards.
	 */

	private Image cardImages[][];
	
	/*
	 * An image for the backs of the cards.
	 */
	private Image backcardimage;
	
	/*
	 * An array storing the images for the avatars.
	 */
	private Image avatars[];
	
	/*
	 * A private method to load the various images used in the application.
	 */
	
	private void Images() 
	{
		char[] suit = {'d','c','h','s'};
		char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		
		cardImages = new Image[4][13];
		
		for(int i=0;i<4;i++) 
		{
			for(int j=0;j<13;j++) 
			{
				String location="cards/" + rank[j] + suit[i] + ".gif";
				cardImages[i][j] = new ImageIcon(location).getImage();
			}
		}
		
		avatars = new Image[4];
		avatars[0] = new ImageIcon("src/avatars/1.png").getImage();
		avatars[1] = new ImageIcon("src/avatars/2.png").getImage();
		avatars[2] = new ImageIcon("src/avatars/3.jpg").getImage();
		avatars[3] = new ImageIcon("src/avatars/4.jpg").getImage();
		
		backcardimage = new ImageIcon("src/cards/b.gif").getImage();
	}
	
	/*
	 * A private method to setup the GUI of the application.
	 */
	private void SetupGUI() {
		
		//initializing the frame
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("Big Two Game - Uday");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Setting the menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Game");
		menuBar.add(menu);
		
		frame.setJMenuBar(menuBar);
		
		JMenuItem restart = new JMenuItem("Restart");
		JMenuItem quit = new JMenuItem("Quit");
		
		menu.add(restart);
		menu.add(quit);
		
		restart.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		
		//Buttons Panel
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JPanel playpassButtonsPanel = new JPanel();
		
		//Buttons inside panel
		playButton = new JButton("Play");
		playButton.setMargin(new Insets(2, 5, 2, 5));
		passButton = new JButton("Pass");
		passButton.setMargin(new Insets(2, 5, 2, 5));
		
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		
		playpassButtonsPanel.add(playButton);
		playpassButtonsPanel.add(passButton);
	
		
		buttonPanel.add(playpassButtonsPanel,BorderLayout.SOUTH);
		frame.add(buttonPanel);
		
		//Playing area
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));
		
		//Chat Area + Show Messages
		JPanel Communitcation = new JPanel();
		JPanel msgchatPanel =new JPanel();
		msgchatPanel.setLayout(new GridLayout(2,1));
		
		msgArea = new JTextArea();
		msgArea.setEditable(false);
		
		//Creating Messaging Area with Scroll
		JScrollPane msgAreascroll = new JScrollPane (msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		msgAreascroll.setPreferredSize(new Dimension(500, 150));
		DefaultCaret msgAreacaret = (DefaultCaret)msgArea.getCaret();
		msgAreacaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgchatPanel.add(msgAreascroll);
		
		//Creating Chat Area with Scroll
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		JScrollPane chatAreaScroll = new JScrollPane (chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		DefaultCaret chatAreaCaret = (DefaultCaret) chatArea.getCaret();
		chatAreaScroll.setPreferredSize(new Dimension(500, 150));
		chatAreaCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgchatPanel.add(chatAreaScroll);
		
		Communitcation.add(msgchatPanel);
		
		JPanel chatInputPanel = new JPanel();
		chatInputPanel.setLayout(new BorderLayout());
		
		JLabel msgLabel = new JLabel("Message: ");
		chatInput = new JTextField(" ", 47);
		chatInput.addActionListener(new pntchat());
		
		chatInputPanel.add(msgLabel, BorderLayout.WEST);
		chatInputPanel.add(chatInput);
		
		buttonPanel.add(chatInputPanel, BorderLayout.NORTH);
		//buttonPanel.add(msgLabel, BorderLayout.WEST);
		//buttonPanel.add(chatInput, BorderLayout.EAST);
		
		
		frame.add(BorderLayout.EAST, Communitcation);
		
		frame.add(BorderLayout.CENTER, bigTwoPanel);
		frame.add(BorderLayout.SOUTH, buttonPanel);
		frame.setSize(new Dimension(1280,800));  
	    frame.setVisible(true); 
	}
	
	/**
	 * A constructor for creating a BigTwoGUI. The parameter game is a reference to a card game associates with this table.
	 * 
	 * @param game A Big Two Card Game  play through this GUI.
	 */
	public BigTwoGUI(BigTwo game) 
	{
		
		this.game = game;
		selected = new boolean[13];
		this.setActivePlayer(game.getCurrentPlayerIdx()); 
		this.Images();
		this.SetupGUI();	
	}
	
	/**
	 * A setter method that sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer an into value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) 
	{
		this.activePlayer = activePlayer;
	}
	
	/**
	 * Returns an array of cards selected.
	 * 
	 * @return an array of the cards selected by the user.
	 */
	public int[] getSelected() 
	{
		
		int select[];
		int c1 = 0;
		
		for(int i=0;i<selected.length;i++)
		{
			if(selected[i]==true) 
			{
				c1++;
			}
		}
		
		if(c1==0) 
		{
			return null;
		}
		
		select = new int[c1];
		int c2 = 0;
		for(int i=0;i<selected.length;i++) 
		{
			if(selected[i]==true) 
			{
				select[c2] = i;
				c2++;
			}
		}
		return select;	
	}
	
	/**
	 * A method that resets the list of cards to an empty list.
	 */
	public void resetSelected() 
	{
		for(int i=0;i<selected.length;i++) {
			selected[i] = false;
		}
	}
	
	/**
	 * A method that repaints the GUI.
	 */
	public void repaint() 
	{
		resetSelected();
		frame.repaint();
	}
	
	/**
	 * A method that prints the specified string to the message area
	 * 
	 * @param msg the string to be printed 
	 */
	public void printMsg(String msg) 
	{
		msgArea.append(msg+"\n");
	}
	
	/**
	 * A method that clears the message area of the GUI
	 */
	public void clearMsgArea() 
	{
		msgArea.setText(null);
	}
	
	/**
	 * A method that resets the GUI.
	 */
	public void reset() 
	{
		this.resetSelected();
		this.clearMsgArea();
		this.enable();
	}
	
	/**
	 * A method that enables user interactions.
	 */
	public void enable() 
	{
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	
	/**
	 * A method that disables user interactions.
	 */
	public void disable() 
	{
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	public void disableTable()
	{
		bigTwoPanel.setEnabled(false);
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}
	
	public void enableTable()
	{
		bigTwoPanel.setEnabled(true);
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}
	
	/**
	 * An inner class that extends the JPanel class and implements the MouseListener interface. 
	 * Overrides the paintComponent() method inherited from the JPanel class to draw the card game table. 
	 * Implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
	 * 
	 * @author Udaria
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		
		// private variables to help draw the output of various graphics to the screen
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int nameX = 40; //name coordinates
		private int nameY = 20; 
		private int avaX = 5; //avatar coordinates
		private int avaY = 30;
		private int lineX = 160; //line coordinates
		private int lineY = 1600;
		private int downCardY = 43;  //card coordinates
		private int upCardY = 23;
		private int cardX = 155;
		private int cardwt = 40;
		private int cardIncrement = 160;
		
		private int getSuitofPlayer(int Player, int Suit) 
		{
			return game.getPlayerList().get(Player).getCardsInHand().getCard(Suit).getSuit();
		}
		
		private int getRankofPlayer(int Player, int Rank) 
		{
			return game.getPlayerList().get(Player).getCardsInHand().getCard(Rank).getRank();
		}
		
		/**
		 * BigTwoPanel constructor which adds the Mouse Listener and sets background of the card table.
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		/**
		 * Draws the avatars, text and cards on card table.
		 * 
		 * @param g Provided by system to allow drawing.
		 */
		public void paintComponent(Graphics g)
		{
			
			super.paintComponent(g);
			this.setBackground(Color.GREEN.darker().darker());
			g.setColor(Color.WHITE);
			int plyflag = 0;
			
			while(plyflag<4) 
			{
				if(game.getPlayerID()==plyflag) 
				{
					if(plyflag==game.getCurrentPlayerIdx())
					{
						g.setColor(Color.CYAN);
						g.drawString(game.getPlayerList().get(plyflag).getName() +" (You) ", nameX , nameY + 160*plyflag);
					}
					else g.setColor(Color .WHITE);
				}
				else 
				{
					g.drawString(game.getPlayerList().get(plyflag).getName(), nameX , nameY + 160*plyflag); 
				}
				
				//Others
				g.setColor(Color.WHITE);
				g.drawImage(avatars[plyflag], avaX, avaY + 160*plyflag, this);
			    g.drawLine(0, lineX*(plyflag+1), lineY, lineX*(plyflag+1));

			    //cards shown if players is active
			    if (game.getPlayerID() == plyflag) 
			    {
			    	for (int i = 0; i < game.getPlayerList().get(plyflag).getNumOfCards(); i++) 
			    	{
			    		int suit = getSuitofPlayer(plyflag, i);
			    		int rank = getRankofPlayer(plyflag, i);
			    		//if selected then painted in a raised fashion otherwise painted normally
			    		if (selected[i])
			    		{
			    			g.drawImage(cardImages[suit][rank], cardX+cardwt*i, upCardY+cardIncrement*plyflag, this);
			    		}	
			    		else
			    		{
			    			g.drawImage(cardImages[suit][rank], cardX+cardwt*i, downCardY+cardIncrement*plyflag, this);
			    		}		
			    	}
			    } 
			    else
			    {
			    	for (int i = 0; i < game.getPlayerList().get(plyflag).getCardsInHand().size(); i++)
			    	{
			    		g.drawImage(backcardimage, cardX+cardwt*i, downCardY+cardIncrement*plyflag, this);
			    	}
			    }
				plyflag++;
			}
		    
			//draws the previous hand
		    g.drawString("Previous Hand", 10, 660);
		     
		    //displays the previous hand played by whom
		    if (!game.getHandsOnTable().isEmpty())
		    {
		    	int handsize = game.getHandsOnTable().size();
		    	Hand currentHand = game.getHandsOnTable().get(handsize - 1);
		    	g.drawString("Hand Type:\n " + game.getHandsOnTable().get(handsize - 1).getType(), 10, 700);
		    	for (int i = 0; i < currentHand.size(); i++)
	    		{
		    		int suit = currentHand.getCard(i).getSuit();
		    		int rank = currentHand.getCard(i).getRank();
	    			g.drawImage(cardImages[suit][rank], 160 + 40*i, 690, this);
	    		}
	    		
	    		g.drawString("Played by " + game.getHandsOnTable().get(handsize-1).getPlayer().getName(), 10, 725);
		    }
		    repaint();
		}
		
		/**
		 * A method used to catch all the mouse click events.
		 * It overrides the MouseClicked method of JPanel.
		 * 
		 * @param e This is a MouseEvent object which has been used to get the coordinates of the mouseClick
		 */
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if(activePlayer == ((BigTwo)game).getPlayerID())
			{
				boolean flag = false; 
				int cardno = game.getPlayerList().get(activePlayer).getNumOfCards();
				int check = cardno-1;
				
				if (e.getX() >= (cardX+check*40) && e.getX() <= (cardX+check*40+73)) 
				{
					if(!selected[check] && e.getY() >= (downCardY + cardIncrement*activePlayer) && e.getY() <= (downCardY + cardIncrement*activePlayer+97))
					{
						selected[check] = true;
						flag = true;
					} 
					else if (selected[check] && e.getY() >= (upCardY + cardIncrement*activePlayer) && e.getY() <= (upCardY + cardIncrement*activePlayer+97))
					{
						selected[check] = false;
						flag = true;
					}
				}
				for (check = cardno-2; check >= 0 && !flag; check--) 
				{
					if (e.getX() >= (cardX+check*cardwt) && e.getX() <= (cardX+(check+1)*cardwt)) 
					{
						if(!selected[check] && e.getY() >= (downCardY+cardIncrement*activePlayer) && e.getY() <= (downCardY+cardIncrement*activePlayer+97))
						{
							selected[check] = true;
							flag = true;
						} 
						else if (selected[check] && e.getY() >= (upCardY+cardIncrement*activePlayer) && e.getY() <= (upCardY+cardIncrement*activePlayer+97))
						{
							selected[check] = false;
							flag = true;
						}
					}
					else if (e.getX() >= (cardX+(check+1)*cardwt) && e.getX() <= (cardX+check*cardwt+73) && e.getY() >= (downCardY+cardIncrement*activePlayer) && e.getY() <= (downCardY+cardIncrement*activePlayer+97)) 
					{
						if (selected[check+1] && !selected[check]) 
						{
							selected[check] = true;
							flag = true;
						}
					}
					else if (e.getX() >= (cardX+(check+1)*cardwt) && e.getX() <= (cardX+check*cardwt+73) && e.getY() >= (upCardY + cardIncrement*activePlayer) && e.getY() <= (upCardY + cardIncrement*activePlayer+97))
					{
						if (!selected[check+1] && selected[check])
						{
							selected[check] = false;
							flag = true;
						}
					}
				}
				this.repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}	

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Play” button. 
	 * When the “Play” button is clicked, it calls the makeMove() method of CardGame object to make a move.
	 * 
	 * @author Udaria
	 */
	class PlayButtonListener implements ActionListener{
		
		/**
		 * The function is overridden from the ActionListener Interface 
		 * and is used to perform the requisite function when the button is clicked.
		 * 
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(game.getPlayerID() == game.getCurrentPlayerIdx())
			{
				if (getSelected() == null)
				{
					printMsg("Select cards to play.\n");
				}
				
				else 
				{
					game.makeMove(activePlayer, getSelected());
				}
			}
			repaint();
		}
		
	}
	
	/**
	 * This inner class implements the ActionListener interface and is used to detect the clicks on the passButton 
	 * and call the makeMove function based on the click.
	 *
	 * @author Udaria
	 **/
	class PassButtonListener implements ActionListener{
		
		/**
		 * The function is overridden from the ActionListener Interface 
		 * and is used to perform the requisite function when the button is clicked.
		 * 
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			repaint();
			game.makeMove(activePlayer, null);
		}	
	}
	
	
	/**
	 * This inner class implements the actionListener interface for the Quit Menu Item in the JMenuBar to quit the game on click. 
	 * 
	 * @author Udaria
	 */
	class QuitMenuItemListener implements ActionListener{

		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 *  @param e This is a ActionEvent object to detect if some user interaction was performed on the given object
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			game.disconnect();
			System.exit(0);	
		}
		
		
	}
	/*
	 * Prompts the player to take their turn
	 */
	public void promptActivePlayer() 
	{
		printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: \n");
	}
	
	class ConnectMenuItemListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
				
			game.makeConnection();
			
		}
	}
	
	class ClearChatListener implements ActionListener{
		
		
		public void actionPerformed(ActionEvent e) {
			chatArea.setText("");
		}
	}
	public void displaymsg(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}

	
	private class pntchat implements ActionListener 
	{
		
		/**
		 *  Helps out in input and printing of messages
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String msg = chatInput.getText();
			
			if (msg!=null && msg.trim()!="") 
			{
				CardGameMessage message= new CardGameMessage(CardGameMessage.MSG, -1, msg+"\n");
				game.sendMessage(message);
				chatInput.setText("");
				chatInput.requestFocus();
			}
		}
	}


}