
public class test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	    Player pl1 = new ComputerPlayer(CheckersConstants.WHITE,2);
	    Player pl2 = new ComputerPlayer(CheckersConstants.BLACK,1);
	    Game game = new Game(pl1,pl2);
	    int result = game.play();
	    switch(result){
	    	case CheckersConstants.WHITE_WIN:
	    		System.out.println("White wins");
	    		break;
	    	case CheckersConstants.BLACK_WIN:
	    		System.out.println("Black wins");
	    		break;
	    }
	}

}
