package starter.graphical;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyValue;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.*;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class App extends Application {

    // Window Variables
    static final int Window_Width = 1280;
    static final int Window_Height = 800;

    // Game Variables
    static int block_size = 25;
    static final int width = 40;
    static final int height = 32;
    static final int speed = 5;
    static final int frameRate = 1000000000 / speed;
    static Direction direction = Direction.RIGHT;
    static LinkedList<Point> snake = new LinkedList<>();
    static LinkedList<Point> foods = new LinkedList<>();
    static boolean gameOver = false;
    static boolean paused = false;
    static boolean timeUp = false;
    static int level = 1;
    static Canvas canvas;
    static GraphicsContext gc;
    static StackPane level1;
    static StackPane level2;
    static StackPane level3;
    static Scene scene1;
    static Scene scene2;
    static Scene scene3;
    static Scene splash;
    static StackPane endScreen;
    static Scene endScene;
    static Clock clock;

    //////////////////////////////////////////////////////
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public static class Clock extends Pane {

        private Timeline timeline;
        private int countdown = 30;
        private String S = "";

        Label label = new Label("Countdown: " + "\n" + "30");

        private Clock() {
            timeUp = false;
            label.setFont(new Font(20));
            label.setTranslateX(10);
            label.setTranslateY(600);
            label.setTextFill(Color.ORANGERED);

            getChildren().add(label);
            timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (countdown > 0 && !paused) {
                                countdown --;
                            }
                            S = "Countdown: " + "\n" + countdown + "";
                            label.setText(S);
                            if (countdown == 0) {
                                timeUp = true;
                                timeline.stop();
                            }
                        }
                    }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }

    }

    public static void generateFood(int level) {
        while (!foods.isEmpty()) {
            foods.removeFirst();
        }
        switch (level) {
            case 1:
                for (int i = 0; i < 5; i++) {
                    Point food = new Point(2*i + 3, 3*i + 1);
                    foods.add(food);
                }
                break;
            case 2:
                for (int i = 0; i < 10; i++) {
                    Point food = new Point(i + i/2 + 3, 2*i + 2);
                    foods.add(food);
                }
                break;
            case 3:
                for (int i = 0; i < 15; i++) {
                    Point food = new Point(2*i + 1, i + i%3);
                    foods.add(food);
                }
                break;
        }
    }

    public static void newFood() {
        boolean invalid = true;
        boolean repeat = false;
        while (invalid) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            for (Point s : snake) {
                if (s.x == x && s.y == y) {
                    repeat = true;
                    break;
                }
            }
            for (Point f : foods) {
                if (f.x == x && f.y == y) {
                    repeat = true;
                    break;
                }
            }
            if (repeat) {
                continue;
            }
            invalid = false;
            foods.add(new Point(x, y));
        }

    }

    public static void initializeSnake() {
        while (!snake.isEmpty()) {
            snake.removeFirst();
        }
        snake.add(new Point(width/2, height/2));
        snake.add(new Point(width/2-1, height/2));
        snake.add(new Point(width/2-2, height/2));
    }

    public static void initialize() {
        gameOver = false;
        paused = false;
        timeUp = false;
        direction = Direction.RIGHT;
    }

    public static void reset(Stage stage, Scene splash) {
        initializeSnake();
        initialize();
        level = 1;
        stage.setTitle("Snake Game");
        stage.setScene(splash);
    }

    @Override
    public void start(Stage stage) {

        // FIRST SCREEN:
        // SPLASH SCREEN ////////////////////////////////
        stage.setTitle("Snake Game");
        // Create a scene graph with a root node
        VBox root = new VBox(10);
        splash = new Scene(root, Window_Width, Window_Height);
        stage.setResizable(false);

        // create splash_screen
        Image image = new Image("assets/splash_screen.png", 880, 716, true, true);
        ImageView imageView = new ImageView(image);
        // create a label
        String intro = " Yilin Zhang " + "\n" + " y2785zha " + "\n" + "\n" + " Press Space To Start" + "\n";
        intro = intro + "\n" + " Use left and right arrow keys " + "\n" + " to control the snake";
        Label label = new Label(intro, imageView);
        label.setFont(new Font("Ayuthaya", 20));
        label.setAlignment(Pos.CENTER);
        root.getChildren().add(label);


        // Initialize Game
        canvas = new Canvas(Window_Width, Window_Height);
        gc = canvas.getGraphicsContext2D();

        // SECOND SCREEN
        // LEVEL 1 /////////////////////////////////////
        level1 = new StackPane();
        scene1 = new Scene(level1, Window_Width, Window_Height);

        // THIRD SCREEN
        // LEVEL 2 /////////////////////////////////////
        level2 = new StackPane();
        scene2 = new Scene(level2, Window_Width, Window_Height);

        // FOURTH SCREEN
        // LEVEL 3 /////////////////////////////////////
        level3 = new StackPane();
        scene3 = new Scene(level3, Window_Width, Window_Height);

        // FINAL SCREEN
        // SCORE
        endScreen = new StackPane();
        endScene = new Scene(endScreen, Window_Width, Window_Height);
        ////////////////////////////////////////////////////////////////////////////////////

        splash.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                   level = 1;
                   initialize();
                   initializeSnake();
                   generateFood(level);
                   clock = new Clock();
                   level1.getChildren().add(canvas);
                   level1.getChildren().add(clock);
                   startGame(stage);
                   stage.setTitle("Level 1");
                   stage.setScene(scene1);
             }
            }
        });

        EventHandler<KeyEvent> sceneHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.getKeyCode("1")) {
                    level = 1;
                    if (timeUp) {
                        clock = new Clock();
                    }
                    initialize();
                    generateFood(level);
                    initializeSnake();
                    level1.getChildren().add(canvas);
                    level1.getChildren().add(clock);
                    startGame(stage);
                    stage.setTitle("Level 1");
                    stage.setScene(scene1);
                } else if (event.getCode() == KeyCode.getKeyCode("2")) {
                    level = 2;
                    if (timeUp) {
                        clock = new Clock();
                    }
                    initialize();
                    generateFood(level);
                    initializeSnake();
                    level2.getChildren().add(canvas);
                    level2.getChildren().add(clock);
                    startGame(stage);
                    stage.setTitle("Level 2");
                    stage.setScene(scene2);
                } else if (event.getCode() == KeyCode.getKeyCode("3")) {
                    level = 3;
                    initialize();
                    generateFood(level);
                    initializeSnake();
                    level3.getChildren().add(canvas);
                    level3.getChildren().add(clock);
                    startGame(stage);
                    stage.setTitle("Level 3");
                    stage.setScene(scene3);
                } else if (event.getCode() == KeyCode.R) {
                    reset(stage, splash);
                } else if (event.getCode() == KeyCode.Q) {
                    gameOver = true;
                    startGame(stage);
                } else if (event.getCode() == KeyCode.P) {
                    if (paused) {
                        paused = false;
                        startGame(stage);
                    } else {
                        paused = true;
                    }
                }
            }
        };

        scene1.addEventFilter(KeyEvent.KEY_PRESSED, sceneHandler);
        scene2.addEventFilter(KeyEvent.KEY_PRESSED, sceneHandler);
        scene3.addEventFilter(KeyEvent.KEY_PRESSED, sceneHandler);
        endScene.addEventFilter(KeyEvent.KEY_PRESSED, sceneHandler);

        scene1.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                switch(direction) {
                    case UP:
                        direction = Direction.LEFT;
                        break;
                    case DOWN:
                        direction = Direction.RIGHT;
                        break;
                    case LEFT:
                        direction = Direction.DOWN;
                        break;
                    case RIGHT:
                        direction = Direction.UP;
                        break;
                }
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                switch (direction) {
                    case UP:
                        direction = Direction.RIGHT;
                        break;
                    case DOWN:
                        direction = Direction.LEFT;
                        break;
                    case LEFT:
                        direction = Direction.UP;
                        break;
                    case RIGHT:
                        direction = Direction.DOWN;
                        break;
                }
            }
        });

        scene2.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                switch(direction) {
                    case UP:
                        direction = Direction.LEFT;
                        break;
                    case DOWN:
                        direction = Direction.RIGHT;
                        break;
                    case LEFT:
                        direction = Direction.DOWN;
                        break;
                    case RIGHT:
                        direction = Direction.UP;
                        break;
                }
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                switch (direction) {
                    case UP:
                        direction = Direction.RIGHT;
                        break;
                    case DOWN:
                        direction = Direction.LEFT;
                        break;
                    case LEFT:
                        direction = Direction.UP;
                        break;
                    case RIGHT:
                        direction = Direction.DOWN;
                        break;
                }
            }
        });

        scene3.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                switch(direction) {
                    case UP:
                        direction = Direction.LEFT;
                        break;
                    case DOWN:
                        direction = Direction.RIGHT;
                        break;
                    case LEFT:
                        direction = Direction.DOWN;
                        break;
                    case RIGHT:
                        direction = Direction.UP;
                        break;
                }
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                switch (direction) {
                    case UP:
                        direction = Direction.RIGHT;
                        break;
                    case DOWN:
                        direction = Direction.LEFT;
                        break;
                    case LEFT:
                        direction = Direction.UP;
                        break;
                    case RIGHT:
                        direction = Direction.DOWN;
                        break;
                }
            }
        });
        
        stage.setScene(splash);
        stage.show();
    }

    public static void startGame(Stage stage) {
         new AnimationTimer() {
            long lastMove = 0;

            public void handle(long now) {
                    if (lastMove == 0) {
                        if (paused) {
                            this.stop();
                        } else {
                            this.start();
                        }
                        lastMove = now;
                        draw(gc);
                        if (gameOver) {
                            showScore(gc);
                            endScreen.getChildren().add(canvas);
                            stage.setTitle("Your Score");
                            stage.setScene(endScene);
                            this.stop();
                        }
                        return;
                    }
                    if (now - lastMove > frameRate) {
                        if (paused) {
                            this.stop();
                        } else {
                            this.start();
                        }
                        lastMove = now;
                        draw(gc);
                        if (gameOver) {
                            showScore(gc);
                            endScreen.getChildren().add(canvas);
                            stage.setTitle("Your Score");
                            stage.setScene(endScene);
                            this.stop();
                        }
                        if (timeUp) {
                            if (level == 1) {
                                this.stop();
                                level = 2;
                                initialize();
                                initializeSnake();
                                generateFood(level);
                                clock = new Clock();
                                level2.getChildren().add(canvas);
                                level2.getChildren().add(clock);
                                startGame(stage);
                                stage.setTitle("Level 2");
                                stage.setScene(scene2);
                            } else if (level == 2) {
                                this.stop();
                                level = 3;
                                initialize();
                                initializeSnake();
                                generateFood(level);
                                clock = new Clock();
                                level3.getChildren().add(canvas);
                                level3.getChildren().add(clock);
                                startGame(stage);
                                stage.setTitle("Level 3");
                                stage.setScene(scene3);
                            }
                        }
                    }

            }
        }.start();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static void showScore(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0,0, Window_Width, Window_Height);
        gc.setFill(Color.GREENYELLOW);
        gc.setFont(new Font(34));
        gc.fillText("Game Over \n" + "\n" + "Score : " + 100 * snake.size(), 540, 200);
        gc.setFill(Color.AQUAMARINE);
        gc.fillText("Press R to reset.", 520, 600);
    }

    public static void draw(GraphicsContext gc) {

        // move snake
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        // move head
        switch (direction) {
            case UP:
                snake.get(0).y -= 1;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case DOWN:
                snake.get(0).y += 1;
                if (snake.get(0).y > height-1) {
                    gameOver = true;
                }
                break;
            case LEFT:
                snake.get(0).x -= 1;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case RIGHT:
                snake.get(0).x += 1;
                if (snake.get(0).x > width-1) {
                    gameOver = true;
                }
                break;
        }

        // self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }

        // eat food
        for (Point f: foods) {
            if (f.x == snake.get(0).x && f.y == snake.get(0).y) {
                snake.add(new Point(-1,-1));
                foods.remove(f);
                newFood();
            }
        }

        //draw background
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(0,0, App.Window_Width, App.Window_Height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i + j) % 2 == 0) {
                    if (level == 1) {
                        gc.setFill(Color.LIGHTYELLOW);
                    } else if (level == 2) {
                        gc.setFill(Color.LAVENDER);
                    } else if (level == 3) {
                        gc.setFill(Color.LIGHTCYAN);
                    }
                    gc.fillRect(140 + i * block_size, j * block_size, block_size, block_size);
                } else {
                    gc.setFill(Color.LIGHTGREEN);
                    gc.fillRect(140 + i * block_size, j * block_size, block_size, block_size);
                }
            }
        }


        //draw food
        if (level == 1) {
            gc.setFill(Color.LIGHTCORAL);
        } else if (level == 2) {
            gc.setFill(Color.LIGHTPINK);
        } else if (level == 3) {
            gc.setFill(Color.GOLDENROD);
        }
        for (Point f: foods) {
            gc.fillRect(140+f.x * block_size, f.y * block_size, block_size, block_size);
        }

        //draw snake
        for (Point s: snake) {
            if (s == snake.get(0)) {
                gc.setFill(Color.ORANGERED);
                gc.fillRect(140+s.x * block_size, s.y * block_size, block_size, block_size);
            } else {
                gc.setFill(Color.DARKGREEN);
                gc.fillRect(140+s.x * block_size, s.y * block_size, block_size, block_size);
            }

        }

        //draw score
        gc.setFill(Color.BEIGE);
        gc.setFont(new Font(20));
        gc.fillText("Score : " + 100 * snake.size(), 20, 100);

    }
}



