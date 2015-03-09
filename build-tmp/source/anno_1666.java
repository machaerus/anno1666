import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Random; 
import java.lang.Math; 
import java.awt.Point; 
import java.awt.Point; 
import java.util.Random; 
import java.lang.Math; 
import java.awt.Point; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class anno_1666 extends PApplet {




int UNIT = 8; // podstawowa jednostka miary (po\u0142owa wysoko\u015bci pola)

float CAM_X = -450;			// zmienne do obs\u0142ugi kamery
float CAM_Y = 0;
float xOffset = 0.0f; 
float yOffset = 0.0f;
float currCAM_X = 0;
float currCAM_Y = 0;

int mouseTileX = 0;		// na kt\u00f3rym polu jest aktualnie kursor
int mouseTileY = 0;

int prevHoverX = 0;		// poprzednie pod\u015bwietlone pole
int prevHoverY = 0;

Random rand;

int SPEED;
int Q;

Scene scene;
Agent agent;

boolean hasActiveObject = false;

public void setup() {
	frameRate(20);
	size(
		800,
		600
	);

	SPEED = 5;
	Q = 0;

	rand = new Random();

	scene = new Scene();
	agent = new Agent(1, new Point(2,2));

	Point[] a = new Point[1];
	a[0] = new Point(0,0);
	agent.setArea(a);
	agent.updateOccupies();
}



public void draw() {

	// obliczamy wsp\u00f3\u0142rz\u0119dne kursora na mapie
	calculateTileCoords();
	// rysowanie t\u0142a
	background(190);

	if( mouseTileX >= 0 
		&& mouseTileX < scene.width() 
		&& mouseTileY >= 0 
		&& mouseTileY < scene.height()) {
		// je\u015bli jeste\u015bmy w obr\u0119bie planszy
		
		scene.get(prevHoverX, prevHoverY).setHover(false);
		prevHoverX = mouseTileX;
		prevHoverY = mouseTileY;
		scene.get(mouseTileX, mouseTileY).setHover(true);
	}

	// rysowanie planszy
	drawScene();

	if ( Q % SPEED == 0 ) {
		agent.move( (int)Math.floor(rand.nextFloat()*4) );
		Q = 1;
	} else {
		Q++;
	}

	// wy\u015bwietlanie agenta
	agent.display();


	// POKA\u017b WSP\u00d3\u0141RZ\u0118DNE P\u00d3L:
	
	// fill(0);
	// noStroke();
	// textSize(12);
	// for(int i = 0; i < scene.height(); i++) {
	// 	for(int j = 0; j < scene.width(); j++) {
	// 		float x = j * 4*UNIT;
	// 		float y = i * 4*UNIT;
	// 		float isoX = x - y - CAM_X - 20;
	// 		float isoY = (x + y) / 2 - CAM_Y;
	// 		if(isoX > 100 && isoX < 400 && isoY > 100 && isoY < 300) {
	// 			String coords = "(" + scene.get(j,i).x() + ", " + scene.get(j,i).y() + ")";
	// 			text(coords, isoX, isoY); 
	// 		}
	// 	}
	// }
	
}

public void calculateTileCoords() {
	// na jakim polu mamy kursor?
	float isoX = mouseX;
	float isoY = mouseY;
	float cartX = (2 * (isoY + CAM_Y) + (isoX + CAM_X)) / 2;
	float cartY = (2 * (isoY + CAM_Y) - (isoX + CAM_X)) / 2;
	mouseTileX = (int)Math.floor((cartX - 2 + 2 * UNIT) / (4 * UNIT));	
	mouseTileY = (int)Math.floor((cartY - 2 + 2 * UNIT) / (4 * UNIT));	
	// -2 to poprawka na... kontur pola? nie mam poj\u0119cia
	// ale jest potrzebna dla precyzji
}

public void draw_field(float x, float y, int col) {	// x, y - wsp\u00f3\u0142rz\u0119dne izometryczne
	if 		( col == 0 ) 	fill(38, 100, 91);	// occupied
	else if ( col == 1 ) 	fill(38, 190, 91);	// active
	else if ( col == 2 ) 	fill(78, 220, 101);	// hover
	else if ( col == 3 )	fill(38, 166, 91);	// kolor #1
	else if ( col == 4 )	fill(38, 150, 91);	// kolor #2
	stroke(38, 180, 91);
	quad(
		x - 4*UNIT - CAM_X, y - CAM_Y,
		x - CAM_X, 			y - 2*UNIT - CAM_Y,
		x + 4*UNIT - CAM_X, y - CAM_Y,
		x - CAM_X, 			y + 2*UNIT - CAM_Y
	);
}

public void drawScene() {
	for(int i = 0; i < scene.height(); i++) {
		for(int j = 0; j < scene.width(); j++) {
			float x = j * 4*UNIT;
			float y = i * 4*UNIT;
			float isoX = x - y;
			float isoY = (x + y) / 2;
			// rysujemy tylko te pola, kt\u00f3re s\u0105 widoczne:
			if(isoX - CAM_X > (-4)*UNIT && isoX - CAM_X < 800 + 4*UNIT && isoY - CAM_Y > -2*UNIT && isoY - CAM_Y < 600 + 2*UNIT) {
				draw_field(isoX, isoY, scene.get(j,i).col());
			}
		}
	}
}

// ruch kamery 

public void mousePressed() {
	xOffset = mouseX; 
	yOffset = mouseY; 
	currCAM_X = CAM_X;
	currCAM_Y = CAM_Y;
}
public void mouseDragged() {
	CAM_X = currCAM_X + xOffset - mouseX; 
	CAM_Y = currCAM_Y + yOffset - mouseY; 
}
public void mouseReleased() {
	xOffset = 0.0f;
	yOffset = 0.0f;
}
public void keyPressed() {
	if( keyCode == UP ) CAM_Y -= 20;
	else if( keyCode == DOWN ) CAM_Y += 20;
	else if( keyCode == RIGHT ) CAM_X += 20;
	else if( keyCode == LEFT ) CAM_X -= 20;
}

public void mouseClicked() {
	
	System.out.println("Clicked: ("+mouseTileX+", "+mouseTileY+")");

	if( mouseTileX >= 0 
		&& mouseTileX < scene.width() 
		&& mouseTileY >= 0 
		&& mouseTileY < scene.height()) {
		// je\u015bli jeste\u015bmy w obr\u0119bie planszy
		
		scene.get(mouseTileX,mouseTileY).setActive(true);
		// klikni\u0119cie aktywizuje pole
	}
} 


class Agent extends GameObject {

	boolean active;
	
	Agent(int size, Point position) {
		super(size, position, false);
		active = false;
	}

	public void setArea(Point[] area) {
		this.area = area;
	}

	public void display() {
		stroke(50);
		fill(150);
		int x = (int)position.getX() * 4*UNIT;
		int y = (int)position.getY() * 4*UNIT;
		float isoX = x - y - CAM_X;
		float isoY = (x + y) / 2 - CAM_Y - 6;
		ellipse(isoX, isoY, 20, 20);
	}

	public boolean move(int dir) {
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


abstract class GameObject {

	int size;			// ilo\u015b\u0107 zajmowanych p\u00f3l
	Point[] area;		// lista wzgl\u0119dnych wsp\u00f3\u0142rz\u0119dnych p\u00f3l zajmowanych przez obiekt
	Point[] occupies;	// lista p\u00f3l, kt\u00f3re OBECNIE zajmuje obiekt [generowana na bie\u017c\u0105co]
	Point position;		// pole od kt\u00f3rego liczymy wzgl\u0119dne odleg\u0142o\u015bci i kt\u00f3re pozycjonujemy
	boolean crossable;	// czy agent mo\u017ce przej\u015b\u0107 przez ten obiekt

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

	public abstract void display();			// wy\u015bwietlanie obiektu
	
	public void updateOccupies() {
		for(int i = 0; i < size; i++) {
			occupies[i].setLocation(
				(int)area[i].getX() + (int)position.getX(),
				(int)area[i].getY() + (int)position.getY()
			);
		}
	}

}



class Scene {

	private int width;
	private int height;
	private Tile[][] tileTable;

	Scene() {
		generate();
	}

	public void generate() {
		this.height = 50;
		this.width = 50;
		tileTable = new Tile[width][height];
		Random rand = new Random();

		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				tileTable[j][i] = new Tile(
					new Point(j,i), 
					false, 
					(int)Math.ceil(rand.nextFloat()*1.9f + 2.099f)
					// liczba od 0 do 1.9
					// +0.099, \u017ceby zmie\u015bci\u0107 si\u0119 w (0,2], dzi\u0119ki temu z ceil mamy zawsze 1 albo 2
					// +2, bo chcemy 3 albo 4 (0, 1 i 2 to occupied, active i hover)
				);
			}
		}
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public Tile get(int x, int y) {
		return tileTable[x][y];
	}

}


class Tile {

	private Point coords;
	private boolean occupied;
	private boolean active;
	private boolean hover;
	private int col;
	/*  klucz warto\u015bci zmiennej col:
	 *	0 		- occupied
	 * 	1 		- active
	 * 	2		- hover
	 * 	3, ... 	- typy pod\u0142o\u017ca
	 */

	Tile(Point coords, boolean occupied, int col) {
		this.coords = coords;
		this.occupied = occupied;
		this.active = false;
		this.hover = false;
		this.col = col;
	}

	public int x() {
		return (int)coords.getX();
	}
	public int y() {
		return (int)coords.getY();
	}
	public Point coords() {
		return coords;
	}
	public int col() {
		if ( occupied ) 	return 0;
		else if ( active ) 	return 1;
		else if ( hover )	return 2;
		else 				return col;
	}

	public void setColor(int c) {
		this.col = c;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public boolean isHover() {
		return hover;
	}

	public void setActive(boolean val) {
		this.active = val;
	}

	public void setOccupied(boolean val) {
		this.occupied = val;
	}

	public void setHover(boolean val) {
		this.hover = val;
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "anno_1666" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
