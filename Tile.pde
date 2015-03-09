import java.awt.Point;

class Tile {

	private Point coords;
	private boolean occupied;
	private boolean active;
	private boolean hover;
	private int col;
	/*  klucz wartości zmiennej col:
	 *	0 		- occupied
	 * 	1 		- active
	 * 	2		- hover
	 * 	3, ... 	- typy podłoża
	 */

	Tile(Point coords, boolean occupied, int col) {
		this.coords = coords;
		this.occupied = occupied;
		this.active = false;
		this.hover = false;
		this.col = col;
	}

	int x() {
		return (int)coords.getX();
	}
	int y() {
		return (int)coords.getY();
	}
	Point coords() {
		return coords;
	}
	int col() {
		if ( occupied ) 	return 0;
		else if ( active ) 	return 1;
		else if ( hover )	return 2;
		else 				return col;
	}

	void setColor(int c) {
		this.col = c;
	}

	boolean isActive() {
		return active;
	}

	boolean isOccupied() {
		return occupied;
	}

	boolean isHover() {
		return hover;
	}

	void setActive(boolean val) {
		this.active = val;
	}

	void setOccupied(boolean val) {
		this.occupied = val;
	}

	void setHover(boolean val) {
		this.hover = val;
	}
}