package it.unipi.chesspuzzles.client.controllers;

import it.unipi.chesspuzzles.client.App;
import it.unipi.chesspuzzles.client.shared.Attemp;
import it.unipi.chesspuzzles.client.ChessPuzzlesApi;
import it.unipi.chesspuzzles.client.shared.Puzzle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayController {

    private static final Logger logger = LogManager.getLogger(PlayController.class);

    @FXML
    private Button confirmMoveBtn;
    @FXML
    private Button nextPuzzleBtn;
    @FXML
    private Button hintBtn;
    @FXML
    private Button giveUpBtn;
    @FXML
    private Button addToFavouritesBtn;
    @FXML
    private ImageView chessboardImage;
    @FXML
    private Label timerLabel;
    @FXML
    private Label puzzleTitleLabel;
    @FXML
    private Label hintLabel;
    @FXML
    private Label colorToMoveLabel;
    @FXML
    private TextField userMoveField;

    // The index of the puzzle's puzzleStates array
    private int currentState;
    private Timeline timerTimeLine;
    private Puzzle puzzle;
    private int secondsElapsed;
    
    private static String formatClock(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private static String getHintFromMove(String move) {
        char pieceToMove = move.charAt(0);
        switch (pieceToMove) {
            case 'r':
            case 'R':
                return "Move a rook";
            case 'q':
            case 'Q':
                return "Move the queen";
            case 'k':
            case 'K':
                return "Move the king";
            case 'n':
            case 'N':
                return "Move a knight";
            case 'b':
                if (move.length() == 2) {
                    return "Move a pawn";
                }
                return "Move a bishop or a pawn";
            case 'B':
                return "Move a bishop";
            default:
                return "Move a pawn";
        }
    }

    @FXML
    private void hintBtnClick() {
        hintBtn.setDisable(true);
        hintLabel.setText(getHintFromMove(puzzle.puzzleStates.get(currentState).correctMove));
        hintLabel.setVisible(true);
    }

    @FXML
    private void giveUpBtnClick() {
        hintBtn.setDisable(true);
        giveUpBtn.setDisable(true);
        hintLabel.setText("The correct move was " + puzzle.puzzleStates.get(currentState).correctMove);
        hintLabel.setVisible(true);
    }

    @FXML
    private void addToFavouritesBtnClick() {
        addToFavouritesBtn.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    ChessPuzzlesApi.addPuzzleToFavourites(App.getUserId(), puzzle);
                    Platform.runLater(() -> addToFavouritesBtn.setText("Added to favourites"));
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> addToFavouritesBtn.setText("Something went wrong"));
                    logger.error("Unable to add puzzle to favourites -> {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    private void mainMenuBtnClick() {
        App.setRoot("main");
    }

    @FXML
    private void initialize() {
        userMoveField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                userMoveField.getStyleClass().remove("move-wrong");
                userMoveField.getStyleClass().remove("move-correct");
            }
        });
        nextPuzzleBtnClick();
    }

    private void updateColorToMoveLabel() {
        Pattern colorToMovePattern = Pattern.compile(" [wb] ");
        Matcher colorToMoveMatcher = colorToMovePattern.matcher(puzzle.puzzleStates.get(currentState).fen);
        String playerToMove = colorToMoveMatcher.find() ? colorToMoveMatcher.group() : null;
        
        if (playerToMove == null) {
            colorToMoveLabel.setText("Something went wrong");
            logger.fatal("Unable to determine the color to move of fen {}",
                    puzzle.puzzleStates.get(currentState).fen);
            throw new RuntimeException("Unable to parse color to move");
        }

        if (playerToMove.equals(" w ")) {
            colorToMoveLabel.setText("White to move");
        } else {
            colorToMoveLabel.setText("Black to move");
        }
    }

    private void startTimer() {
        secondsElapsed = 0;
        timerTimeLine = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                secondsElapsed++;
                timerLabel.setText(formatClock(secondsElapsed));
            }
        ));
        timerTimeLine.setCycleCount(Animation.INDEFINITE);
        timerTimeLine.play();
    }

    private void stopTimerIfRunning() {
        if (timerTimeLine != null) {
            timerTimeLine.stop();
        }
    }

    private void updateChessboard(byte[] image) {
        chessboardImage.setImage(new Image(new ByteArrayInputStream(image)));
    }

    private void disableControls() {
        nextPuzzleBtn.setDisable(true);
        userMoveField.setDisable(true);
        confirmMoveBtn.setDisable(true);
        hintBtn.setDisable(true);
        giveUpBtn.setDisable(true);
        addToFavouritesBtn.setDisable(true);
    }

    private void enableControls() {
        nextPuzzleBtn.setDisable(false);
        userMoveField.setDisable(false);
        confirmMoveBtn.setDisable(false);
        hintBtn.setDisable(false);
        giveUpBtn.setDisable(false);

        if (App.getFavouritePuzzle() != null) {
            addToFavouritesBtn.setDisable(true);
            addToFavouritesBtn.setText("Added to favourites");
        }
        else {
            addToFavouritesBtn.setDisable(false);
        }
    }

    private void resetUi() {
        puzzleTitleLabel.setText("");
        puzzleTitleLabel.getStyleClass().remove("puzzle-solved");
        chessboardImage.setImage(new Image(Objects.requireNonNull(
                App.class.getResource("media/emptyBoard.png")).toExternalForm()));
        stopTimerIfRunning();
        timerLabel.setText(formatClock(0));
        colorToMoveLabel.setText("");
        userMoveField.setText("");
        userMoveField.getStyleClass().remove("move-correct");
        userMoveField.getStyleClass().remove("move-wrong");
        hintLabel.setVisible(false);
        addToFavouritesBtn.setText("Add to favourites");
    }

    @FXML
    private void nextPuzzleBtnClick() {
        disableControls();
        resetUi();
        currentState = -1;

        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    // Fetch puzzle
                    if (App.getFavouritePuzzle() == null) {
                        puzzle = ChessPuzzlesApi.getRandomPuzzle();
                    } else {
                        puzzle = App.getFavouritePuzzle();
                    }

                    playNextMove();

                    // Update UI
                    Platform.runLater(() -> {
                        puzzleTitleLabel.setText(puzzle.title);
                        updateColorToMoveLabel();
                        startTimer();
                        enableControls();

                        // Clear favourite puzzle
                        App.setFavouritePuzzle(null);
                    });

                } catch (IOException | InterruptedException e) {
                    logger.error("Unable to fetch new puzzle and update ui -> {}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private Attemp.TYPE calculateAttempType() {
        if (giveUpBtn.isDisabled()) {
            return Attemp.TYPE.FAIL;
        } else if (hintBtn.isDisabled()) {
            return Attemp.TYPE.HINT;
        } else {
            return Attemp.TYPE.SOLVE;
        }
    }

    @FXML
    private void confirmMoveBtnClick() {
        confirmMoveBtn.setDisable(true);

        if (userMoveField.getText().equals(puzzle.puzzleStates.get(currentState).correctMove)) {
            userMoveField.getStyleClass().add("move-correct");
            Attemp.TYPE attempType = calculateAttempType();
            new Thread(createAddAttempTask(attempType)).start();
            new Thread(createConfirmMoveTask()).start();
        } else {
            userMoveField.getStyleClass().add("move-wrong");
            confirmMoveBtn.setDisable(false);
            new Thread(createAddAttempTask(Attemp.TYPE.FAIL)).start();
        }
    }

    private Task<Void> createAddAttempTask(Attemp.TYPE attempType) {
        return new Task<>() {
            @Override
            public Void call() {
                try {
                    ChessPuzzlesApi.addAttemp(App.getUserId(), attempType);
                } catch (IOException | InterruptedException e) {
                    logger.warn("Unable to register new attemp -> {}", e.getMessage());
                }
                return null;
            }
        };
    }

    private boolean isSolved() {
        return currentState + 1 == puzzle.puzzleStates.size();
    }

    private void solvePuzzle() {
        stopTimerIfRunning();
        puzzleTitleLabel.getStyleClass().add("puzzle-solved");
        colorToMoveLabel.setText("Puzzle solved!");
        hintLabel.setVisible(false);
        hintBtn.setDisable(true);
        giveUpBtn.setDisable(true);
        userMoveField.setDisable(true);
        confirmMoveBtn.setDisable(true);
    }


    /**
     * Meant to be called from a task thread
      */
    private void playNextMove() throws IOException, InterruptedException {
        currentState++;
        byte[] firstImageBytes = ChessPuzzlesApi.drawBoard(puzzle.puzzleStates.get(currentState).fen);
        Platform.runLater(() -> updateChessboard(firstImageBytes));
    }

    private Task<Void> createConfirmMoveTask() {
        return new Task<>() {
            @Override
            public Void call() {
                try {
                    // Play user move
                    playNextMove();

                    if (isSolved()) {
                        Platform.runLater(() -> solvePuzzle());
                        return null;
                    }

                    // Intended sleep to let the user see each move better
                    Thread.sleep(500);

                    // Play puzzle response
                    playNextMove();

                    // Update ui
                    Platform.runLater(() -> {
                        userMoveField.getStyleClass().remove("move-correct");
                        userMoveField.setText("");
                        hintLabel.setVisible(false);

                        confirmMoveBtn.setDisable(false);
                        hintBtn.setDisable(false);
                        giveUpBtn.setDisable(false);
                    });
                } catch (IOException | InterruptedException e) {
                    logger.error("Unable to confirm move -> {}", e.getMessage());
                }
                return null;
            }
        };
    }

}