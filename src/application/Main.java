package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    PingPongModel model;
    PingPongView view;

    @Override
    public void start(Stage primaryStage) {

        model = new PingPongModel();
        view = new PingPongView(primaryStage, model);

        view.initializeUI();

        // JavaFX Timeline - free form animation defined by KeyFrames and their duration
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> runGame()));

        // Number of cycles in animation INDEFINITE = repeat indefinitely, it can be defined but must be > 0
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    private void runGame() {

        view.updateUI();

        if (model.isGameStarted()) {
            model.movePlayerTwo();
            model.updateBallPositionAndSpeed();
            model.checkScore();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
