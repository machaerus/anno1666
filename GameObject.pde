import java.awt.Point;

abstract class GameObject {

	int size;			// ilość zajmowanych pól
	Point[] area;		// lista względnych współrzędnych pól zajmowanych przez obiekt
	Point[] occupies;	// lista pól, które OBECNIE zajmuje obiekt [generowana na bieżąco]
	Point position;		// pole od którego liczymy względne odległości i które pozycjonujemy
	boolean crossable;	// czy agent może przejść przez ten obiekt

	GameObject(int size, Point position, boolean crossable) {
		this.size = size;
		//area = new Point[size];
		occupies = new Point[size];
		for(int i = 0; i < size; i++) {
			occupies[i] = new Point(0,0);
		}
		this.position = position;
		this.crossable = crossable;
	}

	abstract void display();			// wyświetlanie obiektu
	
	void updateOccupies() {
		for(int i = 0; i < size; i++) {
			occupies[i].setLocation(
				(int)area[i].getX() + (int)position.getX(),
				(int)area[i].getY() + (int)position.getY()
			);
		}
	}

}