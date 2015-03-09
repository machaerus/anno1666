import java.awt.Point;

class Agent extends GameObject {

	boolean active;
	
	Agent(int size, Point position) {
		super(size, position, false);
		active = false;
	}

	void setArea(Point[] area) {
		this.area = area;
	}

	void display() {
		stroke(50);
		fill(150);
		int x = (int)position.getX() * 4*UNIT;
		int y = (int)position.getY() * 4*UNIT;
		float isoX = x - y - CAM_X;
		float isoY = (x + y) / 2 - CAM_Y - 6;
		ellipse(isoX, isoY, 20, 20);
	}

	boolean move(int dir) {
		if ( dir == 0 ) { 		
			if ( position.getY() > 0 ) {
				position.translate(0,-1);
				updateOccupies();
				return true;
			} else {
				return false;
			}
		} else if ( dir == 1 ) { 	
			if ( position.getX() < scene.width() ) {
				position.translate(1,0);
				updateOccupies();
				return true;
			} else {
				return false;
			}
		} else if ( dir == 2 ) {	
			if ( position.getY() < scene.height() ) {
				position.translate(0,1);
				updateOccupies();
				return true;
			} else {
				return false;
			}
		} else if ( dir == 3 ) {	
			if ( position.getX() > 0 ) {
				position.translate(-1,0);
				updateOccupies();
				return true;
			} else {
				return false;
			}
		} else return false;

	}

}