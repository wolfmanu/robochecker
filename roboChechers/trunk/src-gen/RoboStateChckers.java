
    
	import de.nordakademie.lejos.statemachine.*;
	import lejos.nxt.*;
	import lejos.navigation.*;
     public class RoboStateChckers {
        
	     private static CheckersNavigator navigator  =  SimpleNavigator.getInstance();
	
	     private static ColorSensor CS  =  new ColorSensor(SensorPort.S1);
	
	     private static Board board = new Board();;
	
	     private static int pieceRobot = CheckersConstants.WHITE;;
	
	     private static int piecehuman = CheckersConstants.BLACK;;
	  
        private boolean initialized=false;
        
        
 
      public  IState calibration = new  AbstractState(){
			  
			
           
           public void doIt() throws InterruptedException{
                 navigator.calibrate();
           }
           
           
           
           public String getName(){
                 return "Calibration";
           }  
      }; 
  
      public  IState home = new  AbstractState(){
			  
			
           
           public void doIt() throws InterruptedException{
                 navigator.goHome();
           }
           
           
           
           public String getName(){
                 return "Home";
           }  
      }; 
  
      public  IState think = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "Think";
           }  
      }; 
  
      public  IState guessMoveFrom = new  AbstractState(){
			  
			
           
           public void doIt() throws InterruptedException{
                 Move[] mosse = board.getPossibleMoves(pieceHuman)
           }
           
           
           
           public String getName(){
                 return "GuessMoveFrom";
           }  
      }; 
  
      public  IState guessMoveTo = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "GuessMoveTo";
           }  
      }; 
  
      public  IState robotWins = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "RobotWins";
           }  
      }; 
  
      public  IState humanWins = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "HumanWins";
           }  
      }; 
  
      public  IState sensorRead = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "SensorRead";
           }  
      }; 
  
      public  IState updateBoard = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "UpdateBoard";
           }  
      }; 
  
      public  IState showFrom = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "ShowFrom";
           }  
      }; 
  
      public  IState showTo = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "ShowTo";
           }  
      }; 
  
      public  IState calculateMoves = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "CalculateMoves";
           }  
      }; 
  
        
        
      public ITransition humanMoved = new AbstractTransition(calculateMoves){
           public boolean guard(){
                return false;//Insert java condition here;
           }
           public String getName(){
                return "HumanMoved";
           }
      };
  
        
        public static void main(String[] args){
               RoboStateChckers roboStateChckers= new RoboStateChckers();
               roboStateChckers.arbitrate();
        }
        
        public synchronized void arbitrate() {
              init();
              BTChangeListener bTChangeListener = new BTChangeListener();
              
  	    
  	    calibration.setListener(bTChangeListener);
  	    
  		calibration.arbitrate(0);
  

        }
        
        public String getName(){
           return "RoboStateChckers";
        }
        
        public boolean isRunning(){
              return initialized
     && calibration.isRunning()
  ;
        }
        
        private void init(){
               if (!initialized){
               
      calibration.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(home)
         {
               
         }
      
      }
      );
       
  
      
      
      home.setTransitions(new ITransition[]{
        humanMoved
        });
        
  
      think.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(showFrom)
         {
               
         }
      ,
         new AbstractTransition(humanWins)
         {
               
         }
      
      }
      );
       
  
      guessMoveFrom.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(sensorRead)
         {
               
         }
      ,
         new AbstractTransition(robotWins)
         {
               
         }
      
      }
      );
       
  
      guessMoveTo.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(sensorRead)
         {
               
         }
      
      }
      );
       
  
      
       
  
      
       
  
      sensorRead.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(guessMoveFrom)
         {
               
         }
      ,
         new AbstractTransition(guessMoveTo)
         {
               
         }
      ,
         new AbstractTransition(updateBoard)
         {
               
         }
      
      }
      );
       
  
      updateBoard.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(think)
         {
               
         }
      
      }
      );
       
  
      showFrom.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(showTo)
         {
          public int getDelay(){return 3000;}     
         }
      
      }
      );
       
  
      showTo.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(home)
         {
          public int getDelay(){return 3000;}     
         }
      
      }
      );
       
  
      calculateMoves.setImplicitTransitions(
      new ITransition[] {
      
         new AbstractTransition(guessMoveFrom)
         {
               
         }
      
      }
      );
       
  
               initialized=true;
               }
        }
        
     }
 