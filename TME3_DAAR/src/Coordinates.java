import java.io.Serializable;

public class Coordinates implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 191549283036495317L;
	private int x, y;
	 public Coordinates(int x, int y) {
		 this.x = x;
		 this.y = y;
	 }
	 
	 public String toString() {
		 return "[" + x + ", " + y + "]";
	 }

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
