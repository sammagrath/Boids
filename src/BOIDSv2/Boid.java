package BOIDSv2;
import java.util.Random;

import javafx.scene.shape.Circle;

public class Boid extends Circle {
	
	protected double dx = 0, dy = 0;
	protected boolean collisionCheck = false;
	
	public boolean isCollisionCheck() {
		return collisionCheck;
	}
	public void setCollisionCheck(boolean collisionCheck) {
		this.collisionCheck = collisionCheck;
	}
	public double getDx() {
		return dx;
	}
	public void setDx(double dx) {
		this.dx = dx;
	}
	public double getDy() {
		return dy;
	}
	public void setDy(double dy) {
		this.dy = dy;
	}
	
	public void randomiseDirection() {
		
		Random rand = new Random();
		int num = rand.nextInt(4)+1;
		int state = rand.nextInt(2);
		
		if (state == 1) num*=-1;
		System.out.println("state: " + state);
		this.setDx(num);
		
		num = rand.nextInt(4)+1;
		state = rand.nextInt(2);
		
		if (state == 1) num*=-1;
		
		this.setDy(num);
	}
	
	public boolean calcDistance(Boid other, int x) {
		
		double a = (this.getCenterX()) - (other.getCenterX());
		
		double b = (this.getCenterY()) - (other.getCenterY());
		
		double c = Math.sqrt((a*a) + (b*b));
		
		if (c < x) {
			
			return true;
		}
		
		return false;
		
	}

	
	public Boid(double centerX, double centerY, double radius,  double dx, double dy) {
		super(centerX, centerY, radius);
		
		this.dx = dx;
		this.dy = dy;

	}
	
	

}
