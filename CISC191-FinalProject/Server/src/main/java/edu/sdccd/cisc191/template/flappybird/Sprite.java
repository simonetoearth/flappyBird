package edu.sdccd.cisc191.template.flappybird;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *  handles basics that would be used for both bird and pipes in the game.
 *  created to avoid duplicating code for each class and making it easier to understand.
 */
public class Sprite {
    protected Rectangle rectangle;
    protected double velocityX = 0;
    protected double velocityY = 0;

    public Sprite(double x, double y, double width, double height, Color color) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.rectangle.setFill(color);
    }

    public void update() {
        rectangle.setX(rectangle.getX() + velocityX);
        rectangle.setY(rectangle.getY() + velocityY);
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
