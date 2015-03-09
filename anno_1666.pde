import java.util.Random;
import java.lang.Math;

int UNIT = 8; 			// podstawowa jednostka miary (połowa wysokości pola)

float CAM_X = -450;		// zmienne do obsługi kamery
float CAM_Y = 0;
float xOffset = 0.0; 
float yOffset = 0.0;
float currCAM_X = 0;
float currCAM_Y = 0;

int mouseTileX = 0;		// na którym polu jest aktualnie kursor
int mouseTileY = 0;

int prevHoverX = 0;		// poprzednie podświetlone pole
int prevHoverY = 0;

Random rand;

int SPEED;
int Q;					// spowalnianie czasu (robocze)

Scene scene;
Agent agent;

boolean hasActiveObject = false;	// wtf

void setup() {

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

// główna pętla

void draw() {

	// obliczamy współrzędne kursora na mapie
	calculateTileCoords();

	// rysowanie tła
	background(190);

	if( mouseTileX >= 0 
		&& mouseTileX < scene.width() 
		&& mouseTileY >= 0 
		&& mouseTileY < scene.height()) {
		// jeśli jesteśmy w obrębie planszy
		
		scene.get(prevHoverX, prevHoverY).setHover(false);
		prevHoverX = mouseTileX;
		prevHoverY = mouseTileY;
		scene.get(mouseTileX, mouseTileY).setHover(true);
	}

	// rysowanie planszy
	drawScene();

	// chodząca kulka, just for testing
	if ( Q % SPEED == 0 ) {
		agent.move( (int)Math.floor(rand.nextFloat()*4) );
		Q = 1;
	} else {
		Q++;
	}

	// wyświetlanie agenta
	agent.display();


	// POKAŻ WSPÓŁRZĘDNE PÓL:
	
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

void calculateTileCoords() {
	// na jakim polu mamy kursor?
	float isoX = mouseX;
	float isoY = mouseY;
	float cartX = (2 * (isoY + CAM_Y) + (isoX + CAM_X)) / 2;
	float cartY = (2 * (isoY + CAM_Y) - (isoX + CAM_X)) / 2;
	mouseTileX = (int)Math.floor((cartX - 2 + 2 * UNIT) / (4 * UNIT));	
	mouseTileY = (int)Math.floor((cartY - 2 + 2 * UNIT) / (4 * UNIT));	
	// -2 to poprawka na... kontur pola? nie mam pojęcia
	// ale jest potrzebna dla precyzji
}

void draw_field(float x, float y, int col) {	// x, y - współrzędne izometryczne
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

void drawScene() {
	for(int i = 0; i < scene.height(); i++) {
		for(int j = 0; j < scene.width(); j++) {
			float x = j * 4*UNIT;
			float y = i * 4*UNIT;
			float isoX = x - y;
			float isoY = (x + y) / 2;
			// rysujemy tylko te pola, które są widoczne:
			if(isoX - CAM_X > (-4)*UNIT && isoX - CAM_X < 800 + 4*UNIT && isoY - CAM_Y > -2*UNIT && isoY - CAM_Y < 600 + 2*UNIT) {
				draw_field(isoX, isoY, scene.get(j,i).col());
			}
		}
	}
}

// ruch kamery 

void mousePressed() {
	xOffset = mouseX; 
	yOffset = mouseY; 
	currCAM_X = CAM_X;
	currCAM_Y = CAM_Y;
}
void mouseDragged() {
	CAM_X = currCAM_X + xOffset - mouseX; 
	CAM_Y = currCAM_Y + yOffset - mouseY; 
}
void mouseReleased() {
	xOffset = 0.0;
	yOffset = 0.0;
}
void keyPressed() {
	if( keyCode == UP ) CAM_Y -= 20;
	else if( keyCode == DOWN ) CAM_Y += 20;
	else if( keyCode == RIGHT ) CAM_X += 20;
	else if( keyCode == LEFT ) CAM_X -= 20;
}

// obsługa kliknięć

void mouseClicked() {
	
	System.out.println("Clicked: ("+mouseTileX+", "+mouseTileY+")");

	if( mouseTileX >= 0 
		&& mouseTileX < scene.width() 
		&& mouseTileY >= 0 
		&& mouseTileY < scene.height()) {
		// jeśli jesteśmy w obrębie planszy
		
		scene.get(mouseTileX,mouseTileY).setActive(true);
		// kliknięcie aktywizuje pole
	}
} 