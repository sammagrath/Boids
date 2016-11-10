package BOIDSv2;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.TimelineBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Field extends Application {

	int p = 0;
	int width = 600, height = 1200;
	double x = 100, y = 100, z = 5, dx = 0f, dy = 0f, px = 0, py = 0, vx = 0, vy = 0;
	ArrayList<Boid> boids = new ArrayList<Boid>();
	ArrayList<Boid> proximityArray = new ArrayList<Boid>();
	Random rand = new Random();
	
	double vector1x;
	double vector1y;
	
	double vector2x;
	double vector2y;

	double vector3x;
	double vector3y;

	// method for flying towards centre of flock for an individual boid
	public void rule1(Boid b) {

		vector1x = 0;
		vector1y = 0;
		
		double centreMassX = 0;
		double centreMassY = 0;

		// loops through all other boids, adds their coordinates to the
		// perceived centre of mass of the individual boid
		for (Boid other : boids) {

			if (!other.equals(b)) {

				boolean xClose = Math.abs(b.getCenterX())
						- (other.getCenterX()) < 50;
				boolean yClose = Math.abs(b.getCenterY())
						- (other.getCenterY()) < 50;

				if (xClose && yClose) {
					proximityArray.add(other);
					centreMassX = (centreMassX + other.getCenterX());
					centreMassY = (centreMassY + other.getCenterY());

					// adjust this for size of nearby boids rather than all
					// boids
					centreMassX = centreMassX / proximityArray.size();
					centreMassY = centreMassY / proximityArray.size();

				}
			}

						
		}
		
		vector1x = (centreMassX - (b.getCenterX())) / 1000;
		vector1y = (centreMassY - (b.getCenterY())) / 1000;
		System.out.println("v1x = " + vector1x + ", v1y = " + vector1y);
		
		proximityArray.clear();
	}

	// second rule to prevent crowding
	public void rule2(Boid b) {

		vector2x = 0;
		vector2y = 0;
		double vectorX = 0;
		double vectorY = 0;

		for (Boid other : boids) {

			if (other != b) {

				boolean xClose = Math.abs(other.getCenterX())
						- (b.getCenterX()) < 30;
				boolean yClose = Math.abs(other.getCenterY())
						- (b.getCenterY()) < 30;

				if (xClose && yClose) {

					vectorX = vectorX
							- ((other.getCenterX()) - (b.getCenterX()));
					vectorY = vectorY
							- ((other.getCenterY()) - (b.getCenterY()));

				}
				vectorX *= 0.00001;
				vectorY *= 0.00001;

			}
		}

		vector2x = vectorX;
		vector2y = vectorY;
		
		System.out.println("v2x = " + vector2x + ", v2y = " + vector2y);
	}

	// rule for adjusting speed to match nearby boids
	public void rule3(Boid b) {

		vector3x = 0;
		vector3y = 0;

		double boidDX = 0;
		double boidDY = 0;

		for (Boid other : boids) {

			if ((!other.equals(b))) {
				//&& b.calcDistance(other, 25)
				boolean xClose = Math.abs(other.getCenterX())
						- (b.getCenterX()) < 20;
				boolean yClose = Math.abs(other.getCenterY())
						- (b.getCenterY()) < 20;

				if (xClose && yClose) {	
					
					proximityArray.add(other);
					boidDX = boidDX + other.getDx();
					boidDY = boidDY + other.getDy();
					
					boidDX = boidDX / (proximityArray.size());
					boidDY = boidDY / (proximityArray.size());
				}
			}
		}
		

		vector3x = (boidDX - b.getDx()) / 8;
		vector3y = (boidDY - b.getDy()) / 8;
		System.out.println("v1x = " + vector1x + ", v1y = " + vector1y);
		proximityArray.clear();
	}

	public void limitSpeed(Boid b) {

		double limitMin = -3;
		double limitMax = 3;
		double vector4;

		for (Boid boid : boids) {

			if (boid.getDx() < limitMin) {

				boid.setDx((boid.getDx() / boid.getDx()) * limitMin);
			}
			if (boid.getDy() < limitMin) {

				boid.setDy((boid.getDy() / boid.getDy()) * limitMin);
			}
			if (boid.getDx() > limitMax) {

				boid.setDx((boid.getDx() / boid.getDx()) * limitMax);
			}
			if (boid.getDy() > limitMax) {

				boid.setDy((boid.getDy() / boid.getDy()) * limitMax);
			}

		}

	}
	
	public void fixedRebound(Boid b) {
		
		if (b.getCenterX() <
				 b.getRadius()) {
				
				 b.setDx(2);
				
				 }
				
				 if (b.getCenterX() +
				 b.getRadius() > width) {
				
				 b.setDx(-2);
				 }
				
				 if (b.getCenterY() <
				 b.getRadius()) {
				
				 b.setDy(2);
				 }
				
				 if (b.getCenterY() +
				 b.getRadius() > height) {
				
				 b.setDy(-2);
				 }
	}
	
	public void alternateRebound(Boid b) {
		
		if (b.getCenterX() < b.getRadius()) {

			b.setDx(b.getDx() * -1);

		}

		if (b.getCenterX() + b.getRadius() > width) {

			b.setDy(b.getDy() * -1);
		}

		if (b.getCenterY() < b.getRadius()) {

			b.setDx(b.getDx() * -1);
		}

		if (b.getCenterY() + b.getRadius() > height) {

			b.setDy(b.getDy() * -1);
		}

//		b.setCenterX(b.getCenterX() + b.getDx());
//		b.setCenterY(b.getCenterY() + b.getDy());
	}
	
	public void newRebound(Boid b) {
		
		if (b.getCenterX() < (b.getRadius()*2)) {

			b.setDx(b.getDx() * -1);

		}

		if (b.getCenterX() > width - (b.getRadius()*2)) {

			b.setDy(b.getDy() * -1);
		}

		if (b.getCenterY() < (b.getRadius()*2)) {

			b.setDx(b.getDx() * -1);
		}

		if (b.getCenterY() > (height - b.getRadius()*2)) {

			b.setDy(b.getDy() * -1);
		}

//		b.setCenterX(b.getCenterX() + b.getDx());
//		b.setCenterY(b.getCenterY() + b.getDy());
	}
	
	public void newFixedRebound(Boid b) {
		
		if (b.getCenterX() < b.getRadius()) {
				
				 b.setDx(2);
				
				 }
				
				 if (b.getCenterX() +
				 b.getRadius() > (width-b.getRadius()*2)) {
				
				 b.setDx(-2);
				 }
				
				 if (b.getCenterY() <
				 b.getRadius()) {
				
				 b.setDy(2);
				 }
				
				 if (b.getCenterY() +
				 b.getRadius() > (height-b.getRadius()*2)) {
				
				 b.setDy(-2);
				 }
				 
//		b.setCenterX(b.getCenterX() + b.getDx());
//		b.setCenterY(b.getCenterY() + b.getDy());		 
	}
	
	public void teleport(Boid b) {
		
		if (b.getCenterX() <
				 b.getRadius()) {
				
				 b.setCenterX(width);
				
				 }
				
				 if (b.getCenterX() +
				 b.getRadius() > width) {
				
				 b.setCenterX(0);
				 }
				
				 if (b.getCenterY() <
				 b.getRadius()) {
				
				 b.setCenterY(height);
				 }
				
				 if (b.getCenterY() +
				 b.getRadius() > height) {
				
				 b.setCenterY(0);
				 }
				 
//		b.setCenterX(b.getCenterX() + b.getDx());
//		b.setCenterY(b.getCenterY() + b.getDy());
	}

	public void moveBoids() {
		
		for (Boid boid : boids) {
			
			// call each rule on boid
			
			rule1(boid);			
			rule2(boid);			
//			rule3(boid);
			boid.setDx(boid.getDx() + vector1x + vector2x + vector3x);
			boid.setDy(boid.getDy() + vector1y + vector2y + vector3y);
			limitSpeed(boid);
			fixedRebound(boid);
//			alternateRebound(boid);
//			newRebound(boid);
//			newFixedRebound(boid);
//			teleport(boid);
			
			boid.setCenterX(boid.getCenterX() + boid.getDx());
			boid.setCenterY(boid.getCenterY() + boid.getDy());
					
		}

	}
	
	public void collisionDetection() {
		
		for (Boid boidA : boids) {
			  
		 	for (Boid boidB : boids) {
		  
		 		if (!boidA.equals(boidB)) {
		  
		  		double a = (boidA.getCenterX() + boidA.getTranslateX()) -
		  		(boidB.getCenterX() + boidB.getTranslateX()); 
		  
		  		double b = (boidA.getCenterY() + boidA.getTranslateY()) -
		  		(boidB.getCenterY() + boidB.getTranslateY());
		  		
		  		double sum = (a * a) + (b * b);
		  
		  		double distance = Math.sqrt(sum);
		  
		  		double collisionPointX = (((boidA.getCenterX())) * (boidB.getRadius()) +
		  			((boidB.getCenterX()) * (boidA.getRadius())) / (boidA.getRadius() + boidB.getRadius()));
		  
		  		double collisionPointY = (((boidA.getCenterY())) * (boidB.getRadius()) +
		  			((boidB.getCenterY()) * (boidA.getRadius()) / (boidA.getRadius() + boidB.getRadius())));
		  
		  		double velX1 = (boidA.getDx() * (boidA.getRadius() - boidB.getRadius()) + 
		  			(2 * boidB.getRadius() * boidB.getDx())) / (boidA.getRadius() + boidB.getRadius());
		  
		  		double velY1 = (boidA.getDy() * (boidA.getRadius() - boidB.getRadius()) + 
		  		(2 * boidB.getRadius() * boidB.getDy())) / (boidA.getRadius() + boidB.getRadius());
		  
		  		double velX2 = (boidB.getDx() * (boidB.getRadius() - boidA.getRadius()) + 
		  			(2 * boidA.getRadius() * boidA.getDx())) / (boidA.getRadius() + boidB.getRadius());
		  
		  		double velY2 = (boidB.getDy() * (boidB.getRadius() - boidA.getRadius()) + 
		  			(2 * boidA.getRadius() * boidA.getDy())) / (boidA.getRadius() + boidB.getRadius());
		  
		  		if (distance < 0) { 
		  			distance = distance * -1;
		  
		  		}
		  
		  		if (distance > (boidA.getRadius() + boidB.getRadius())) {
		  			boidA.setFill(Color.BLACK);
		  			boidB.setFill(Color.BLACK);
		  		}
		  
		  		if (distance <= (boidA.getRadius() + boidB.getRadius())) {
		  
		  			boidA.setDx(velX1); boidA.setDy(velY1); boidB.setDx(velX2);
		  			boidB.setDy(velY2);
		  
		  			boidA.setTranslateX(boidA.getTranslateX() + velX1);
		  			boidA.setTranslateY(boidA.getTranslateY() + velY1);
		  			boidB.setTranslateX(boidB.getTranslateX() + velX2);
		  			boidB.setTranslateY(boidB.getTranslateY() + velY2);
		  			
		  			boidA.setCenterX(boidA.getCenterX() + velX1);
		  			boidA.setCenterY(boidA.getCenterY() + velY1);
		  			boidB.setCenterX(boidB.getCenterX() + velX2);
		  			boidB.setCenterY(boidB.getCenterY() + velY2);
		  
		  		}
		  		
		 	}
		 		
		 	
		 } 
	}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Group root = new Group();

		for (int i = 0; i < 5; i++) {

			x = rand.nextInt(width-100) + 100;
			y = rand.nextInt(height-100) + 100;
			//z = rand.nextInt(20) + 5;
			float vel = rand.nextFloat() + 2;

			Boid boid = new Boid(x, y, z, dx, dy);
			boid.randomiseDirection();

			boids.add(boid);
			root.getChildren().add(boid);
			// x = x+100;
		}

		Scene scene = new Scene(root, width, height);

		KeyFrame frame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				// TODO Auto-generated method stub

				moveBoids();
//				collisionDetection();
				
		}
		});

		TimelineBuilder.create().cycleCount(javafx.animation.Animation.INDEFINITE).keyFrames(frame).build().play();

		primaryStage.setTitle("Hello Ballers");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	}

}
