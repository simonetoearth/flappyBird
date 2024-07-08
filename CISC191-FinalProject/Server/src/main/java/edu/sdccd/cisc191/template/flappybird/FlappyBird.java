package edu.sdccd.cisc191.template.flappybird;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBird extends Application {

    private Bird bird;
    private Pane root;
    private List<Pipe> pipes = new ArrayList<>();
    private long lastPipeSpawnTime;
    private int score = 0;
    private int highScore = 0;
    private final String highScoreFile = "highScore.txt";
    private final String resourcePath = "CISC191-FinalProject/Server/src/main/resources/";
    private boolean gameStarted = false;
    private Text scoreText;
    private Text startText;

    /**
     *  creates the window of the game, sets the title
     *  shows a score counter after starting game
     */
    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        bird = new Bird(100, 300, 15, Color.YELLOW);
        root.getChildren().add(bird.getCircle());

        scoreText = new Text(15, 25, "Score: 0");
        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        root.getChildren().add(scoreText);

        startText = new Text(275, 300, "Press SPACE to Start");
        startText.setFill(Color.BLACK);
        startText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        root.getChildren().add(startText);

        highScore = loadHighScore();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameStarted) {
                    bird.update();
                    updatePipes(now);
                    checkCollisions();
                    checkIfBirdHitsBottom();
                }
            }
        };
        timer.start();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (!gameStarted) {
                    gameStarted = true;
                    root.getChildren().remove(startText);
                } else {
                    bird.flap();
                }
            }
        });

        primaryStage.setTitle("Flappy Bird");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updatePipes(long now) {
        if (now - lastPipeSpawnTime > 2000000000L) {
            generatePipes();
            lastPipeSpawnTime = now;
        }

        List<Pipe> offScreenPipes = new ArrayList<>();
        for (Pipe pipe : pipes) {
            pipe.update();
            if (pipe.getX() + pipe.getWidth() < 0) {
                offScreenPipes.add(pipe);
            }

            if (!pipe.isPassed() && bird.getCircle().getCenterX() > pipe.getX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score++;
                scoreText.setText("Score: " + score);
                if (score > highScore) {
                    highScore = score;
                    saveHighScore(highScore);
                }
                System.out.println("Score: " + score);
            }
        }
        pipes.removeAll(offScreenPipes);
        offScreenPipes.forEach(pipe -> root.getChildren().remove(pipe.getRectangle()));
    }

    private void generatePipes() {
        double pipeGap = 250;
        double pipeWidth = 80;
        double pipeHeight = new Random().nextInt(200) + 100;

        Pipe upPipe = new Pipe(800, 0, pipeWidth, pipeHeight);
        Pipe downPipe = new Pipe(800, pipeHeight + pipeGap, pipeWidth, 600 - pipeHeight - pipeGap);

        pipes.add(upPipe);
        pipes.add(downPipe);

        root.getChildren().addAll(upPipe.getRectangle(), downPipe.getRectangle());
    }

    private void checkCollisions() {
        for (Pipe pipe : pipes) {
            if (bird.getCircle().getBoundsInParent().intersects(pipe.getRectangle().getBoundsInParent())) {
                System.out.println("Game Over! Your score: " + score + ". High score: " + highScore);
                resetGame();
                return;
            }
        }
    }

    private void checkIfBirdHitsBottom() {
        if (bird.getCircle().getCenterY() >= 600) { // Assuming the window height is 600
            System.out.println("Game Over! Your score: " + score + ". High score: " + highScore);
            resetGame();
        }
    }

    private int loadHighScore() {
        try (InputStream inputStream = getClass().getResourceAsStream("/" + highScoreFile)) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    return Integer.parseInt(reader.readLine());
                }
            }
            return 0;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void saveHighScore(int highScore) {
        try (OutputStream outputStream = new FileOutputStream(resourcePath + highScoreFile)) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                writer.write(String.valueOf(highScore));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetGame() {
        score = 0;
        scoreText.setText("Score: 0");
        bird.getCircle().setCenterY(300);
        bird.setVelocityY(0);
        pipes.forEach(pipe -> root.getChildren().remove(pipe.getRectangle()));
        pipes.clear();
        gameStarted = false;
        root.getChildren().add(startText);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
