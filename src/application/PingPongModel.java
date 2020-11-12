package application;

import java.util.Random;

public class PingPongModel {

    // Constants
    final double GAME_WIDTH = 800;
    final double GAME_HEIGHT = 600;

    final double PLAYER_HEIGHT = 100;
    final double PLAYER_WIDTH = 15;

    final double BALL_DIAMETER = 15;

    final double PLAYER_ONE_X_POS = 0;
    final double PLAYER_TWO_X_POS = GAME_WIDTH - PLAYER_WIDTH;

    // Variables
    private double playerOneYPos = GAME_HEIGHT / 2 - PLAYER_HEIGHT / 2;
    private double playerTwoYPos = GAME_HEIGHT / 2 - PLAYER_HEIGHT / 2;
    private double ballXPos = GAME_WIDTH / 2 - BALL_DIAMETER / 2;
    private double ballYPos = GAME_HEIGHT / 2 - BALL_DIAMETER / 2;
    private double ballXSpeed = 0.0;
    private double ballYSpeed = 0.0;

    private boolean gameStarted = false;
    private int scoreP1 = 0;
    private int scoreP2 = 0;

    private double initialBallXSpeed;
    private double initialBallYSpeed;
    private double playerTwoAccuracyOffset = 0.0;

    private final Random random = new Random();


    public PingPongModel(){
        resetGame();
    }

    private int getRandomDirection() {
        return random.nextInt(2) == 0 ? 1 : -1;
    }

    private double getRandomSpeed() {
        return random.nextDouble() + 1.0;
    }

    public void startGame() {
        gameStarted = true;

        // Set player position
        setPlayerOneYPos(GAME_HEIGHT / 2 - PLAYER_HEIGHT / 2);
        setPlayerTwoYPos(GAME_HEIGHT / 2 - PLAYER_HEIGHT / 2);

        // Set ball start position
        ballXPos = GAME_WIDTH / 2 - BALL_DIAMETER / 2;
        ballYPos = GAME_HEIGHT / 2 - BALL_DIAMETER / 2;

        // Set ball speed and direction
        ballXSpeed = getRandomSpeed() * getRandomDirection();
        ballYSpeed = getRandomSpeed() * getRandomDirection();

        // Initiate values that are used throughout this game
        initialBallXSpeed = ballXSpeed;
        initialBallYSpeed = ballYSpeed;
    }

    public void stopGame() {
        gameStarted = false;
    }

    public void resetGame() {
        stopGame();
        scoreP1 = 0;
        scoreP2 = 0;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public double getPlayerOneYPos() {
        return playerOneYPos;
    }

    public void setPlayerOneYPos(double yPosition) {
        playerOneYPos = limitPlayerPosition(yPosition);
    }

    public double getPlayerTwoYPos() {
        return playerTwoYPos;
    }

    public void setPlayerTwoYPos(double yPosition) {
        playerTwoYPos = limitPlayerPosition(yPosition);
    }

    private void changePlayerTwoAccuracy() {
        // Offset factor denominator deducted by trial and error, 1.5 is too easy, 2.0 is too hard
        double offsetFactor = getRandomDirection() * random.nextDouble() / 1.7;

        // Offset is based on the size of the board and speed of the ball
        // This way the computer gets less accurate as the ball speed increases
        playerTwoAccuracyOffset = (PLAYER_HEIGHT + Math.abs(ballYSpeed)) * offsetFactor;
    }

    public void movePlayerTwo() {
        // Set computer opponent following the ball
        setPlayerTwoYPos((ballYPos + BALL_DIAMETER / 2 + ballYSpeed - PLAYER_HEIGHT / 2) + playerTwoAccuracyOffset);
    }

    double limitPlayerPosition(double position) {
        if (position < 0) {
            return 0;
        } else {
            return Math.min(position, GAME_HEIGHT - PLAYER_HEIGHT);
        }
    }

    public int getPlayerOneScore() {
        return scoreP1;
    }

    public int getPlayerTwoScore() {
        return scoreP2;
    }

    public double getBallXPos() {
        return ballXPos;
    }

    public double getBallYPos() {
        return ballYPos;
    }

    public void updateBallPositionAndSpeed() {
        // Update ball position
        ballXPos += ballXSpeed;
        ballYPos += ballYSpeed;

        // Check collision with upper and lower limit of the game area
        if (ballYPos > GAME_HEIGHT - BALL_DIAMETER || ballYPos < 0) {
            // Change Y speed on collision, bounce of wall
            ballYSpeed *= -1;

            // Update the accuracy of the computer player
            // This is done when the ball and the computer player board is either at the top or the bottom
            // of the game area, to avoid sudden "jumps" in the computer player boards position
            changePlayerTwoAccuracy();
        }

        // Increase speed of the ball after player hits it
        if (detectBoardHit()) {
            // Change X speed direction
            ballXSpeed *= -1.0;

            // Increase speed to increase difficulty
            ballXSpeed += 0.2 * Math.abs(initialBallXSpeed) * Math.signum(ballXSpeed);
            ballYSpeed += 0.2 * Math.abs(initialBallYSpeed) * Math.signum(ballYSpeed);
        }
    }

    private boolean detectBoardHit() {
        double ballDirection = Math.signum(ballXSpeed);

        // Check if hit is possible
        if ((ballXPos > PLAYER_WIDTH) && ((ballXPos + BALL_DIAMETER) < PLAYER_TWO_X_POS)) {
            // Ball is somewhere between the player boards
            return false;
        }

        // Check player boards
        if (ballDirection == 1.0) {
            // Check player 2 board
            return ballHitsBoard(playerTwoYPos);
        } else {
            // Check player 1 board
            return ballHitsBoard(playerOneYPos);
        }
    }

    private boolean ballHitsBoard(double boardPosition) {
        double ballMiddleY = ballYPos + BALL_DIAMETER / 2;

        return ((ballMiddleY >= boardPosition) && (ballMiddleY <= (boardPosition + PLAYER_HEIGHT)));
    }

    public void checkScore() {
        // If player misses the ball, computer gets a point + stop game
        if (ballXPos < 0 && !ballHitsBoard(playerOneYPos)) {
            scoreP2++;
            stopGame();
        }

        // If computer misses the ball, player gets a point + stop game
        if(ballXPos + BALL_DIAMETER > GAME_WIDTH && !ballHitsBoard(playerTwoYPos)) {
            scoreP1++;
            stopGame();
        }
    }
}
