package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PingPongView {
    Stage stage;
    PingPongModel model;
    Canvas canvas;
    GraphicsContext graphicsContext;

    public PingPongView(Stage stage, PingPongModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void initializeUI() {
        // Primary stage settings
        stage.setTitle("Ping-Pong Game");
        stage.setResizable(false);

        // Layout for scene 1
        BorderPane borderPane = new BorderPane();
        canvas = new Canvas(model.GAME_WIDTH, model.GAME_HEIGHT);
        borderPane.setCenter(canvas);
        HBox hbButtons = new HBox();
        hbButtons.setSpacing(33.0);
        borderPane.setBottom(hbButtons);
        HBox hbTitle = new HBox();
        borderPane.setTop(hbTitle);
        Scene scene1 = new Scene((new StackPane(borderPane)));

        // Set title
        Label titleText = new Label("PING-PONG GAME");
        titleText.setId("titleText");

        hbTitle.getChildren().addAll(titleText);
        hbTitle.setAlignment(Pos.CENTER);

        // Set start button
        Button startButton = new Button("START GAME");
        startButton.setId("startButton");

        // Set reset button
        Button resetButton = new Button("RESET GAME");
        resetButton.setId("resetButton");

        hbButtons.getChildren().addAll(startButton, resetButton);
        hbButtons.setAlignment(Pos.CENTER);


        // Button "start" control
        startButton.setOnAction(e ->  model.startGame());

        // Button "reset" control
        resetButton.setOnAction(e -> model.resetGame());

        scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene1);
        stage.show();

        // Set graphics
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void updateUI() {
        // Set background color
        graphicsContext.setFill(Color.FORESTGREEN);
        graphicsContext.fillRect(0, 0, model.GAME_WIDTH, model.GAME_HEIGHT);

        // Set text
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(Font.font("Verdana", 25));

        // Draw player 1 & 2
        graphicsContext.fillRect(model.PLAYER_ONE_X_POS, model.getPlayerOneYPos(),
                model.PLAYER_WIDTH, model.PLAYER_HEIGHT);
        graphicsContext.fillRect(model.PLAYER_TWO_X_POS, model.getPlayerTwoYPos(),
                model.PLAYER_WIDTH, model.PLAYER_HEIGHT);

        // Draw scores
        graphicsContext.fillText(model.getPlayerOneScore() + "\t\t\t\t\t\t\t" + model.getPlayerTwoScore(),
                model.GAME_WIDTH / 2, 50);

        if (model.isGameStarted()) {
            // Draw line in the middle
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(1);
            graphicsContext.strokeLine(model.GAME_WIDTH / 2, model.GAME_HEIGHT, model.GAME_WIDTH / 2, 0);

            // Mouse control on move
            canvas.setOnMouseMoved(e -> model.setPlayerOneYPos(e.getY() - model.PLAYER_HEIGHT / 2));

            // Draw the ball
            graphicsContext.fillOval(model.getBallXPos(), model.getBallYPos(),
                    model.BALL_DIAMETER, model.BALL_DIAMETER);

            // Set reflection effect to null
            graphicsContext.setEffect(null);
        } else {
            // Set the start text
            graphicsContext.setFill(Color.YELLOW);
            graphicsContext.setFont(new Font("Verdana", 30));
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.fillText("Click the button to start game",
                    model.GAME_WIDTH / 2, model.GAME_HEIGHT / 2);

            // Set reflection effect
            Reflection r = new Reflection();
            r.setFraction(0.7f);
            graphicsContext.setEffect(r);

            // Mouse control on move stopped
            canvas.setOnMouseMoved(null);
        }
    }
}
