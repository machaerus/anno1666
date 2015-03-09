import java.awt.Point;

abstract class GameObject {
/**
 * Abstrakcyjny obiekt - każdy obiekt ruchomy lub nieruchomy,
 * który może pojawić się na planszy i wchodzić w jakiekolwiek
 * interakcje z otoczeniem.
 */

	protected int size;				// ilość zajmowanych pól
	protected Point[] area;			// lista względnych współrzędnych pól zajmowanych przez obiekt
	protected Point[] occupies;		// lista pól, które OBECNIE zajmuje obiekt [generowana na bieżąco]
	protected Point position;		// pole od którego liczymy względne odległości i które pozycjonujemy
	protected boolean crossable;	// czy agent może przejść przez ten obiekt

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

	abstract void display();	// wyświetlanie obiektu
	
	// to trzeba wywołać kiedy obiekt się przemieści,
	// żeby zaktualizować stan planszy pod kątem tego,
	// które pola są zajęte
	void updateOccupies() {
		for(int i = 0; i < size; i++) {
			occupies[i].setLocation(
				(int)area[i].getX() + (int)position.getX(),
				(int)area[i].getY() + (int)position.getY()
			);
		}
		// TODO: aktualizowanie zajętości pól na planszy
	}

}