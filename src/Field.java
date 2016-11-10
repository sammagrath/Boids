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
	int width = 800, height = 600;
	double x = 100, y = 100, z = 15, dx = 0f, dy = 0f, px = 0, py = 0, vx = 0, vy = 0;
	ArrayList<Boid> boids = new ArrayList<Boid>();
	ArrayList<Boid> proximityArray = new ArrayList<Boid>();
	Random rand = new Random();

	double rule1dx;
	double rule1dy;

	double rule2dx;
	double rule2dy;

	double rule3dx;
	double rule3dy;

	// method for flying towards centre of flock for an individual boid
	public void rule1(Boid b) {

		rule1dx = 0;
		rule1dy = 0;
		double centreMassX = 0;
		double centreMassY = 0;

		// loops through all other boids, adds their coordinates to the
		// perceived centre of mass of the individual boid
		for (Boid other : boids) {

			if (other != b) {

				boolean xClose = Math.abs(other.getCenterX() + other.getTranslateX())
						- (b.getCenterX() + b.getTranslateX()) < 50;
				boolean yClose = Math.abs(other.getCenterY() + other.getTranslateY())
						- (b.getCenterY() + b.getTranslateY()) < 50;

				if (xClose && yClose) {
					proximityArray.add(other);
					centreMassX = (centreMassX + other.getCenterX() + other.getTranslateX());
					centreMassY = (centreMassY + other.getCenterY() + other.getTranslateY());

					// adjust this for size of nearby boids rather than all
					// boids
					centreMassX = centreMassX / proximityArray.size();
					centreMassY = centreMassY / proximityArray.size();

				}
			}

			// b.setpX((centreMassX - b.getCenterX() - b.getTranslateX()) /
			// 100);
			// b.setpY((centreMassY - b.getCenterY() - b.getTranslateY()) /
			// 100);

			// add vector onto boid's velocity
			// b.setDx(b.getDx() + (centreMassX - (b.getCenterX() +
			// b.getTranslateX())) / 10000);
			// b.setDy(b.getDy() + (centreMassY - (b.getCenterY() +
			// b.getTranslateY())) / 10000);

			rule1dx = (centreMassX - (b.getCenterX() + b.getTranslateX())) / 1000;
			rule1dy = (centreMassY - (b.getCenterY() + b.getTranslateY())) / 1000;
		}

		proximityArray.clear();
	}

	// second rule to prevent crowding
	public void rule2(Boid b) {

		rule2dx = 0;
		rule2dy = 0;
		double vectorX = 0;
		double vectorY = 0;

		for (Boid other : boids) {

			if (other != b) {

				boolean xClose = Math.abs(other.getCenterX() + other.getTranslateX())
						- (b.getCenterX() + b.getTranslateX()) < 25;
				boolean yClose = Math.abs(other.getCenterY() + other.getTranslateY())
						- (b.getCenterY() + b.getTranslateY()) < 25;

				if (xClose && yClose) {

					vectorX = vectorX
							- ((other.getCenterX() + other.getTranslateX()) - (b.getCenterX() + b.getTranslateX()));
					vectorY = vectorY
							- ((other.getCenterY() + other.getTranslateY()) - (b.getCenterY() + b.getTranslateY()));

				}
				vectorX *= 0.01;
				vectorY *= 0.01;

			}
		}

		// b.setDx(b.getDx() + vectorX);
		// b.setDy(b.getDy() + vectorY);

		rule2dx = vectorX;
		rule2dy = vectorY;
	}

	// rule for adjusting speed to match nearby boids
	public void rule3(Boid b) {

		rule3dx = 0;
		rule3dy = 0;

		double boidDX = 0;
		double boidDY = 0;

		for (Boid other : boids) {

			if ((other != b) && b.calcDistance(other)) {

				boidDX = boidDX + other.getDx();
				boidDY = boidDY + other.getDy();
			}
		}
		boidDX = boidDX / (boids.size() - 1);
		boidDY = boidDY / (boids.size() - 1);

		// b.setDx(b.getDx() + (boidDX - b.getDx()) / 8);
		// b.setDy(b.getDx() + (boidDY - b.getDy()) / 8);

		rule3dx = (boidDX - b.getDx()) / 8;
		rule3dy = (boidDY - b.getDy()) / 8;
	}

	public void limitSpeed(Boid b) {

		double limitMin = -5;
		double limitMax = 5;
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

	public void moveBoids() {
		for (Boid boid : boids) {
			// call each rule on boid
			rule1(boid);
			rule2(boid);
			// rule3(boid);
			boid.setDx(boid.getDx() + rule1dx + rule2dx + rule3dx);
			boid.setDy(boid.getDy() + rule1dy + rule2dy + rule3dy);
			limitSpeed(boid);
					
					
//			 if (boid.getCenterX() + boid.getTranslateX() <
//			 boid.getRadius()) {
//			
//			 boid.setDx(2);
//			
//			 }
//			
//			 if (boid.getCenterX() + boid.getTranslateX() +
//			 boid.getRadius() > width) {
//			
//			 boid.setDx(-2);
//			 }
//			
//			 if (boid.getCenterY() + boid.getTranslateY() <
//			 boid.getRadius()) {
//			
//			 boid.setDy(2);
//			 }
//			
//			 if (boid.getCenterY() + boid.getTranslateY() +
//			 boid.getRadius() > height) {
//			
//			 boid.setDy(-2);
//			 }
			

					// alternative dx mode
					if (boid.getCenterX() + boid.getTranslateX() < boid.getRadius()) {

						boid.setDx(boid.getDx() * -1);

					}

					if (boid.getCenterX() + boid.getTranslateX() + boid.getRadius() > width) {

						boid.setDy(boid.getDy() * -1);
					}

					if (boid.getCenterY() + boid.getTranslateY() < boid.getRadius()) {

						boid.setDx(boid.getDx() * -1);
					}

					if (boid.getCenterY() + boid.getTranslateY() + boid.getRadius() > height) {

						boid.setDy(boid.getDy() * -1);
					}

					boid.setTranslateX(boid.getTranslateX() + boid.getDx());
					boid.setTranslateY(boid.getTranslateY() + boid.getDy());
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Group root = new Group();

		for (int i = 0; i < 50; i++) {

			x = rand.nextInt(width) + 1;
			y = rand.nextInt(height) + 1;
			// z = rand.nextInt(10) + 5;
			float vel = rand.nextFloat() + 2;

			Boid boid = new Boid(x, y, z, dx, dy);
			boid.randomiseDirection();

			boids.add(boid);
			root.getChildren().add(boid);
			// x = x+100;
			// System.out.println("x: " + x + ", y: " + y);
		}

		Scene scene = new Scene(root, width, height);

		KeyFrame frame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				// TODO Auto-generated method stub

				moveBoids();

				for (Boid boid : boids) {
//
//					 if (boid.getCenterX() + boid.getTranslateX() <
//					 boid.getRadius()) {
//					
//					 boid.setDx(2);
//					
//					 }
//					
//					 if (boid.getCenterX() + boid.getTranslateX() +
//					 boid.getRadius() > scene.getWidth()) {
//					
//					 boid.setDx(-2);
//					 }
//					
//					 if (boid.getCenterY() + boid.getTranslateY() <
//					 boid.getRadius()) {
//					
//					 boid.setDy(2);
//					 }
//					
//					 if (boid.getCenterY() + boid.getTranslateY() +
//					 boid.getRadius() > scene.getHeight()) {
//					
//					 boid.setDy(-2);
//					 }
//
//					// alternative dx mode
//					if (boid.getCenterX() + boid.getTranslateX() < boid.getRadius()) {
//
//						boid.setDx(boid.getDx() * -1);
//
//					}
//
//					if (boid.getCenterX() + boid.getTranslateX() + boid.getRadius() > scene.getWidth()) {
//
//						boid.setDy(boid.getDy() * -1);
//					}
//
//					if (boid.getCenterY() + boid.getTranslateY() < boid.getRadius()) {
//
//						boid.setDx(boid.getDx() * -1);
//					}
//
//					if (boid.getCenterY() + boid.getTranslateY() + boid.getRadius() > scene.getHeight()) {
//
//						boid.setDy(boid.getDy() * -1);
//					}
//
//					boid.setTranslateX(boid.getTranslateX() + boid.getDx());
//					boid.setTranslateY(boid.getTranslateY() + boid.getDy());

				}
				/*
				  for (Boid boidA : boids) {
				  
				  for (Boid boidB : boids) {
				  
				  if (boidA != boidB) {
				  
				  double a = (boidA.getCenterX() + boidA.getTranslateX()) -
				  (boidB.getCenterX() + boidB.getTranslateX()); double b =
				  (boidA.getCenterY() + boidA.getTranslateY()) -
				  (boidB.getCenterY() + boidB.getTranslateY());
				  
				  double sum = (a * a) + (b * b);
				  
				  double distance = Math.sqrt(sum);
				  
				  double collisionPointX = (((boidA.getCenterX() +
				  boidA.getTranslateX())) (boidB.getRadius()) +
				  ((boidB.getCenterX() + boidB.getTranslateX()) *
				  (boidA.getRadius())) / (boidA.getRadius() +
				  boidB.getRadius()));
				  
				  double collisionPointY = (((boidA.getCenterY() +
				  boidA.getTranslateY())) (boidB.getRadius()) +
				  ((boidB.getCenterY() + boidB.getTranslateY()) *
				  (boidA.getRadius()) / (boidA.getRadius() +
				  boidB.getRadius())));
				  
				  double velX1 = (boidA.getDx() * (boidA.getRadius() -
				  boidB.getRadius()) + (2 * boidB.getRadius() * boidB.getDx()))
				  / (boidA.getRadius() + boidB.getRadius());
				  
				  double velY1 = (boidA.getDy() * (boidA.getRadius() -
				  boidB.getRadius()) + (2 * boidB.getRadius() * boidB.getDy()))
				  / (boidA.getRadius() + boidB.getRadius());
				  
				  double velX2 = (boidB.getDx() * (boidB.getRadius() -
				  boidA.getRadius()) + (2 * boidA.getRadius() * boidA.getDx()))
				  / (boidA.getRadius() + boidB.getRadius());
				  
				  double velY2 = (boidB.getDy() * (boidB.getRadius() -
				  boidA.getRadius()) + (2 * boidA.getRadius() * boidA.getDy()))
				  / (boidA.getRadius() + boidB.getRadius());
				  
				  if (distance < 0) { distance = distance * -1;
				  
				  }
				  
				  // if (distance > (boidA.getRadius() + // boidB.getRadius()))
				  { // boidA.setFill(Color.BLACK); //
				  boidB.setFill(Color.BLACK); // }
				  
				  if (distance <= (boidA.getRadius() + boidB.getRadius())) {
				  
				  boidA.setDx(velX1); boidA.setDy(velY1); boidB.setDx(velX2);
				  boidB.setDy(velY2);
				  
				  boidA.setTranslateX(boidA.getTranslateX() + velX1);
				  boidA.setTranslateY(boidA.getTranslateY() + velY1);
				  boidB.setTranslateX(boidB.getTranslateX() + velX2);
				  boidB.setTranslateY(boidB.getTranslateY() + velY2);
				  
				  }
				  
				  } } }
				 */

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
