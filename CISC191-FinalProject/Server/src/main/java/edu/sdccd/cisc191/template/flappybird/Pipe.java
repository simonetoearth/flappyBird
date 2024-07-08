package edu.sdccd.cisc191.template.flappybird;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pipe {
    private Rectangle rectangle;
    private double velocityX = -2;
    private boolean passed = false;

    public Pipe(double x, double y, double width, double height) {
        rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(Color.GREEN);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void update() {
        rectangle.setX(rectangle.getX() + velocityX);
    }

    public double getX() {
        return rectangle.getX();
    }

    public double getWidth() {
        return rectangle.getWidth();
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isBirdPassing(double birdX, double birdY, double birdRadius) {
        // check if the bird's x-coordinate is beyond the pipe's x-coordinate
        if (birdX > rectangle.getX() + rectangle.getWidth()) {
            passed = true;
            return true;
        }
        return false;
    }
}
