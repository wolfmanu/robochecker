package it.polito.test;

public class MainThread {

	static Thread t;
	
	public static void main(String[] args) {
		childCode();
		for(int i = 0; i < 20; i++){
			try{
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			System.out.println("Parent " + i);
		}
	}

	protected synchronized static boolean childCode() {
    	t = new Thread() {
    		public void run() {
    			for(int i = 0; i < 20; i++){
    				try{
    					Thread.sleep(3000);
    				} catch (InterruptedException e) {
    					
    				}
    				System.out.println("Child " + i);
    			}
    		}
        };
    	t.start();
        return true;
    }
	
}
