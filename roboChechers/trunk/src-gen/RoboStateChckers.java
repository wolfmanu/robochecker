
    
	import de.nordakademie.lejos.statemachine.*;
	import lejos.nxt.*;
	import lejos.navigation.*;
     public class RoboStateChckers {
          
        private boolean initialized=false;
        
        
 
      public  IState calibration = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "Calibration";
           }  
      }; 
  
      public  IState home = new  AbstractState(){
			  
			
           
           
           
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
  
      public  IState update Board = new  AbstractState(){
			  
			
           
           
           
           public String getName(){
                 return "Update Board";
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
  
        
        
      public ITransition humanMoved = new AbstractTransition(guessMoveFrom){
           public boolean guard(){
                return //Insert java condition here;
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
         new AbstractTransition(update Board)
         {
               
         }
      
      }
      );
       
  
      update Board.setImplicitTransitions(
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
       
  
               initialized=true;
               }
        }
        
     }
 