
public class Move {
	
	private int Player = Checkers.EMPTY;
	private Coords from;
	private Coords[] to;

	public Move (int[] from, int[] to) {
		this.from = new Coords(from[0],from[1]);
		this.to = new Coords[1];
		this.to[0] = new Coords(to[0],to[1]);
	}
	
	public Move (int[] from, int[] to, int Player) {
		this.from = new Coords(from[0],from[1]);
		this.to = new Coords[1];
		this.to[0] = new Coords(to[0],to[1]);
		this.Player = Player;
	}
	
	public void setFrom (int[] from) {
		this.from = new Coords(from[0],from[1]);
	}

	public void setTo (int[] to) {
		this.to = new Coords[1];
		this.to[0] = new Coords(to[0],to[1]);
	}
	
	public void addTo (int[] to) {
		int prevLen = this.to.length;
		Coords[] newTo = new Coords[prevLen+1];
		System.arraycopy(this.to, 0, newTo, 0, prevLen);
		newTo[prevLen] = new Coords(to[0],to[1]);
		this.to = newTo;
	}
	
	public void setPlayer (int Player) {
		this.Player = Player;
	}
	
	public int getPlayer () { return this.Player; }
	
	public boolean isCapture () { return (Math.abs(from.x-to[0].x)>1); }
	public boolean isWalk () { return (Math.abs(from.x-to[0].x)==1); }
	public boolean multipleCapture () { return (this.to.length > 1); }
	
	private class Coords {
		public int x,y;
		public Coords (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
