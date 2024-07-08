package edu.sdccd.cisc191.template.flappybird;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bird extends Sprite {

    private Circle circle;
    private double velocityY = 0;
    private double gravity = 0.2;

    public Bird(double x, double y, double radius, Color blue) {
        super(x, y, radius * 2, radius * 2, Color.YELLOW);
        this.circle = new Circle(x + radius, y + radius, radius, Color.YELLOW);
        this.velocityY = 0;
    }

    public Circle getCircle() {
        return circle;
    }

    public void update() {
        velocityY += gravity;
        circle.setCenterY(circle.getCenterY() + velocityY);
    }

    public void flap() {
        // the 'jump' mechanism for the game
        velocityY = -5;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}
