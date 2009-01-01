package it.polito.gomoku;


public class test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	    Player human = new HumanPlayer(GomokuConstants.WHITE_PIECE);
	    Player compPlayer = new ComputerPlayer(GomokuConstants.BLACK_PIECE);
	    human.startNewGame();
	    compPlayer.startNewGame();
	    Game game = new Game(human, compPlayer, (byte)6, (byte)6);
	    byte result = game.play();
	    switch(result){
	    	case GomokuConstants.DRAW:
	    		System.out.println("Draw");
	    		break;
	    	case GomokuConstants.WHITE_WIN:
	    		System.out.println("White win");
	    		break;
	    	case GomokuConstants.BLACK_WIN:
	    		System.out.println("Black win");
	    		break;
	    }
	}

}
