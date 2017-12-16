package graphics;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import management.Duel;
import management.Tournament;
import tools.Translator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Paweł Zych
 */

public class Controller {
    @FXML
    AnchorPane mainPane = new AnchorPane();
    @FXML
    AnchorPane boardPane = new AnchorPane();
    @FXML
    AnchorPane startupPane = new AnchorPane();
    @FXML
    AnchorPane uniPane = new AnchorPane();
    @FXML
    AnchorPane duelPane = new AnchorPane();
    @FXML
    AnchorPane replayPane = new AnchorPane();
    @FXML
    AnchorPane manualPane = new AnchorPane();
    @FXML
    AnchorPane tourPane = new AnchorPane();
    @FXML
    Button tourStartButton = new Button();
    @FXML
    TextArea tourScoreText = new TextArea();
    @FXML
    ListView<String> tourDuelsLog = new ListView<>();
    @FXML
    TextArea tourErrorsText = new TextArea();
    @FXML
    Button tourReplayButton = new Button();
    @FXML
    Button duelStartButton = new Button();
    @FXML
    Button replayNextButton = new Button();
    @FXML
    Button replayFForwardButton = new Button();
    @FXML
    Button replayEndButton = new Button();
    @FXML
    Button replayBackButton = new Button();
    @FXML
    Label replayNick1Label = new Label();
    @FXML
    Label replayNick2Label = new Label();
    @FXML
    Label duelNick1Label = new Label();
    @FXML
    Label duelNick2Label = new Label();
    @FXML
    Button manualFinishButton = new Button();
    @FXML
    Button manualClearButton = new Button();
    @FXML
    TextArea replayLogText = new TextArea();
    @FXML
    TextArea duelLogText = new TextArea();
    @FXML
    Canvas boardCanvas = new Canvas();
    @FXML
    Label statusLabel = new Label();
    @FXML
    MenuBar menuBar = new MenuBar();
    @FXML
    MenuItem setSizeMenu = new MenuItem();
    @FXML
    MenuItem replayMenu = new MenuItem();
    @FXML
    MenuItem closeMenu = new MenuItem();
    @FXML
    MenuItem randomBarrierMenu = new MenuItem();
    @FXML
    MenuItem manualBarrierMenu = new MenuItem();
    @FXML
    MenuItem tournamentMenu = new MenuItem();
    @FXML
    MenuItem duelMenu = new MenuItem();
    @FXML
    MenuItem aboutMenu = new MenuItem();
    @FXML
    Button duelRefreshButton = new Button();
    @FXML
    Button duelStopButton = new Button();
    @FXML
    CheckBox duelAutorefreshChBox = new CheckBox();
    @FXML
    ProgressBar tourProgressBar = new ProgressBar();

    private BoardDraw draw = new BoardDraw();
    private Dialogs dialog = new Dialogs();

    private Duel rewind;
    private Duel duel;

    private Tournament tournament;
    private File tourResultDir;
    private boolean backToTournamentFlag = false;

    private int randBoxNumber = 0;

    @FXML
    void initialize() {
        tourPane.setVisible(false);
        uniPane.setVisible(true);
        uniPane.toFront();
        startupPane.toFront();

        disableReplayButtons(true);
        disableDuelButtons(true);
        duelStartButton.setDisable(true);

        setupTimer();
    }

    @FXML
    void replayLogPressed() {
        backToTournamentFlag = false;
        setAutoBoardResizing(true);
        replayInitializeUI();

        File displayLogFile = dialog.showFileChooser("SelectLogFile", mainPane, false);
        replayFromFile(displayLogFile);
    }

    private void replayFromFile(File displayLogFile) {
        if (displayLogFile != null) {
            try {
                rewind = new Duel(displayLogFile);
                replayNick1Label.setText(rewind.getStartingPlayer());
                replayNick2Label.setText(rewind.getFollowingPlayer());

                draw.setBoardSize(Translator.getSizeFromInitString(rewind.getInitData()));
                draw.setObstaclePoints(Translator.boxesFromInitString(rewind.getInitData()));
                draw.removePlayersCells();
                draw.drawAllObstacles(boardCanvas, boardPane);
            } catch (FileNotFoundException e) {
                disableReplayButtons(true);
                dialog.showErrorDialogWithStack(e);
            } catch (ProtocolException e) {
                disableReplayButtons(true);
                dialog.showErrorDialogWithStack(e);
            } catch (Exception e) { //Drawing
                disableReplayButtons(true);
                dialog.showErrorDialogWithStack(e);
            }
        } else {
            disableReplayButtons(true);
        }
    }

    private void replayInitializeUI() {
        tourPane.setVisible(false);
        uniPane.setVisible(true);
        uniPane.toFront();
        replayPane.toFront();

        replayNick1Label.setText("...");
        replayNick2Label.setText("...");
        statusLabel.setText("");
        replayLogText.clear();


        disableReplayButtons(false);
        if (backToTournamentFlag)
            replayBackButton.setVisible(true);
        else
            replayBackButton.setVisible(false);
    }

    @FXML
    void nextPressed() {
        if (!rewind.isFinished()) {
            readAndPrint();
        } else {
            readAndPrint();
            statusLabel.setText(rewind.getMessage());
            disableReplayButtons(true);
        }
    }

    @FXML
    void replayBackButtonPressed() {
        backToTournamentFlag = false;
        tourInitializeUI();
    }

    @FXML
    void replayFForwardButtonPressed() {
        for (int i = 0; i < 10; i++) {
            if (!rewind.isFinished()) {
                readAndPrint();
            } else {
                readAndPrint();
                statusLabel.setText(rewind.getMessage());
                disableReplayButtons(true);
                break;
            }
        }
    }

    @FXML
    void replayEndButtonPressed() {
        while (!rewind.isFinished()) {
            readAndPrint();
        }
        readAndPrint();
        statusLabel.setText(rewind.getMessage());
        disableReplayButtons(true);
    }

    private void disableReplayButtons(boolean value) {
        replayNextButton.setDisable(value);
        replayFForwardButton.setDisable(value);
        replayEndButton.setDisable(value);
    }


    private void readAndPrint() {
        int player = getPlayerID(rewind.getMoveCounter());
        try {
            draw.addCells(player, Translator.stringToBoxPair(rewind.getLastMove()));
            draw.drawAll(boardCanvas, boardPane);
        } catch ( ProtocolException e ){
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        replayLogText.appendText(rewind.getLastMove() + " :P" + player + '\n');
        rewind.nextMove();
    }

    @FXML
    void closePressed() {
        System.exit(0);
    }

    @FXML
    void setSizePressed() {
        setAutoBoardResizing(true);
        duelInitializeUI();
        draw.setBoardSize(dialog.showIntValueSelectDialog("Set board size", "Set board size", 21, 3, 1000));
        System.out.println("Size: " + draw.getBoardSize());
        draw.removeObstaclePoints();
        draw.removePlayersCells();
        try {
            draw.drawAllObstacles(boardCanvas, boardPane);
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e, "Drawing Area Too Small");
        }
    }

    @FXML
    void randomBarrierPressed() {
        duelInitializeUI();
        randBoxNumber = dialog.showIntValueSelectDialog("Set random", "Set number of random boxes", 0, 0, draw.getBoardSize() * draw.getBoardSize());
        System.out.println("randBoxNumber: " + randBoxNumber);
        draw.clearBoard(boardCanvas);
        try {
            draw.drawNet(boardCanvas, boardPane);
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e, "Drawing Area Too Small");
        }
    }

    @FXML
    void manualBarrierPressed() {
        tourPane.setVisible(false);
        uniPane.setVisible(true);
        uniPane.toFront();
        manualPane.toFront();
        statusLabel.setText("");

        try {
            draw.drawAllObstacles(boardCanvas, boardPane);
            menuBar.setDisable(true);
            draw.drawNet(boardCanvas, boardPane);
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e, "Drawing Area Too Small");
        }

        boardCanvas.setOnMouseClicked(this::boardMousePressedDragged);
        boardCanvas.setOnMouseDragged(this::boardMousePressedDragged);
    }

    @FXML
    void manualFinishButtonPressed() {
        boardCanvas.setOnMouseClicked(null);
        boardCanvas.setOnMouseDragged(null);
        startupPane.toFront();

        menuBar.setDisable(false);
    }

    @FXML
    void manualClearButtonPressed() {
        draw.removeObstaclePoints();
        try {
            draw.drawAllObstacles(boardCanvas, boardPane);
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e, "Drawing Area Too Small");
        }
    }

    private void boardMousePressedDragged(MouseEvent event) {
        int x_pos = draw.convertToPos(event.getX());
        int y_pos = draw.convertToPos(event.getY());
        if (draw.verifyPos(x_pos) && draw.verifyPos(y_pos)) {
            try {
                if (event.getButton() == MouseButton.PRIMARY) {
                    draw.addOneObstacleCell(boardCanvas, boardPane, new Point(x_pos, y_pos));
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    draw.removeOneObstacleCell(boardCanvas, boardPane, new Point(x_pos, y_pos));
                }
            } catch (Exception e) {
                dialog.showErrorDialogWithStack(e, "Drawing Area Too Small");
            }
        }
    }

    @FXML
    void tourButtonPressed() {
        tourInitializeUI();

        File playersDir = dialog.showDriectoryChooser("Select Players Directory", boardPane);
        tourResultDir = dialog.showDriectoryChooser("Select Results Directory", boardPane);
        if (playersDir != null && tourResultDir != null) {
            try {
                tournament = new Tournament(playersDir, tourResultDir);

            } catch (IOException e) {
                dialog.showErrorDialogWithStack(e);
                tourStartButton.setDisable(true);
            }
        } else {
            dialog.showError(new Exception("Directories not set, Try again"));
            tourStartButton.setDisable(true);
        }
    }

    private void tourInitializeUI() {
        uniPane.setVisible(false);
        tourPane.setVisible(true);
        tourPane.toFront();
        tourStartButton.setDisable(false);
        statusLabel.setText("");
    }

    @FXML
    void tourStartButtonPressed() {
        tourScoreText.clear();
        tourDuelsLog.getItems().clear();
        tourErrorsText.clear();
        tourProgressBar.setVisible(true);
        try {
            tournament.start(draw.getBoardSize(), draw.getObstaclePoints());
            tourThread = new Thread(() -> {
                while( !tournament.isFinished() ) {
                    {
                        tournament.nextDuel();
                        Platform.runLater(() -> tourProgressBar.setProgress(tournament.progressPercentage()));
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        Platform.runLater(() -> statusLabel.setText("Tournament Aborted"));
                        break;
                    }
                }
                try {
                    Scanner scn = new Scanner(new File(tourResultDir.getPath() + "/score.txt"));
                    while (scn.hasNext()) {
                        tourScoreText.appendText(scn.nextLine() + "\n");
                    }
                    scn.close();
                    scn = new Scanner(new File(tourResultDir.getPath() + "/duels.txt"));
                    while (scn.hasNext()) {
                        tourDuelsLog.getItems().add(scn.nextLine() + "\n");
                    }
                    scn.close();
                    scn = new Scanner(new File(tourResultDir.getPath() + "/err.txt"));
                    while (scn.hasNext()) {
                        tourErrorsText.appendText(scn.nextLine() + "\n");
                    }
                    scn.close();
                    tourDuelsLog.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> tourProgressBar.setVisible(false));
                System.out.println("END OF THREAD");
                System.out.println(Thread.currentThread().getState().toString());
            });
            tourThread.setDaemon(true);
            tourThread.start();
            tourStartButton.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Thread tourThread;

    @FXML
    void tourReplayButtonPressed() {
        String name = tourDuelsLog.getSelectionModel().getSelectedItem();
        String[] split = name.split(":");
        String number = split[0];

        setAutoBoardResizing(true);
        replayNextButton.setDisable(false);
        backToTournamentFlag = true;
        replayInitializeUI();
        replayFromFile(new File(tourResultDir.getPath() + "/duels/" + number + ".txt"));
    }

    @FXML
    void duelPressed() {
        setAutoBoardResizing(true);
        duelStartButton.setDisable(false);
        duelInitializeUI();
        disableDuelButtons(true);
        duelStartButton.setDisable(false);

        File firstPlayer = dialog.showDriectoryChooser("Select Starting Player Folder", boardPane);
        File followingPlayer = dialog.showDriectoryChooser("Select Following Player Folder", boardPane);
        File replayLogFile = dialog.showFileChooser("Select Log File", mainPane, true);

        if (firstPlayer != null && followingPlayer != null && replayLogFile != null) {
            try {
                duel = new Duel(firstPlayer, followingPlayer, replayLogFile);
                duelNick1Label.setText(duel.getStartingPlayer());
                duelNick2Label.setText(duel.getFollowingPlayer());
            } catch (FileNotFoundException e) {
                dialog.showErrorDialogWithStack(e, "Player Directory Not Found");
            } catch (ProtocolException e) {
                dialog.showErrorDialogWithStack(e);
            }
        }
        else {
            dialog.showError(new Exception("Directories not set, Try again"));
            duelStartButton.setDisable(true);
        }
    }

    private void duelInitializeUI() {
        tourPane.setVisible(false);
        uniPane.setVisible(true);
        uniPane.toFront();
        duelPane.toFront();

        duelNick1Label.setText("...");
        duelNick2Label.setText("...");
        duelLogText.clear();
        statusLabel.setText("");

        duelAutorefreshChBox.setSelected(false);
        timeline.stop();
        messageBuffer.setLength(0);
    }

    @FXML
    void startPressed() {
        disableDuelButtons(false);
        duelStartButton.setDisable(true);
        try {
            duelLogText.clear();
            duelStartButton.setDisable(true);
            draw.removePlayersCells();
            if (randBoxNumber != 0) {
                ArrayList<Point> randBoxes = duel.setBoard(draw.getBoardSize(), randBoxNumber);
                draw.setObstaclePoints(randBoxes);
                draw.drawAllObstacles(boardCanvas, boardPane);
            } else {
                duel.setBoard(draw.getBoardSize(), draw.getObstaclePoints());
                draw.drawAllObstacles(boardCanvas, boardPane);
            }
            duel.start();
            duelThread = new Thread(() -> {
                int i = 0;
                while (!duel.isFinished()) {
                    logAndPrint(duel.getLastMove(), getPlayerID(i++));
                    duel.nextMove();
                    if (Thread.currentThread().isInterrupted()) {
                        Platform.runLater(() -> statusLabel.setText("Duel Aborted"));
                        break;
                    }
                }
                if (duel.isFinished()) {
                    logAndPrint(duel.getLastMove(), getPlayerID(i++));
                    Platform.runLater(() -> statusLabel.setText(duel.getMessage()));
                }
                try {
                    duelRefreshButtonPressed();
                } catch (Exception e) {
                   ;
                }
                duel.close();
                disableDuelButtons(true);
                duelStartButton.setDisable(true);
                System.out.println("END OF THREAD");
            });
            duelThread.setDaemon(true);
            duelThread.start();
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e, "Log File Not Found");
        } catch (ProtocolException e) {
            disableDuelButtons(true);
            duelStartButton.setDisable(true);
            dialog.showErrorDialogWithStack(e, "Communication Protocol Error");
        } catch (NullPointerException e) {
            duelStartButton.setDisable(true);
            dialog.showErrorDialogWithStack(e, "Log File Not Found");
        } catch (Exception e) {
            disableDuelButtons(true);
            duelStartButton.setDisable(true);
            dialog.showErrorDialogWithStack(e);
        }
    }

    private Thread duelThread;
    private StringBuilder messageBuffer = new StringBuilder();

    private void logAndPrint(String move, int player) {
        synchronized (messageBuffer) {
            messageBuffer.append(move + " :P" + player + '\n');
        }
        try {
            draw.addCells(player, Translator.stringToBoxPair(move));
        } catch (ProtocolException | NullPointerException e) {
            e.printStackTrace();
            //Platform.runLater(() -> dialog.showErrorDialogWithStack(e));
        }

    }

    private int getPlayerID(int counter) {
        return ((counter) % 2) + 1;
    }

    @FXML
    void duelRefreshButtonPressed() {
        try {
            draw.drawAll(boardCanvas, boardPane);
            synchronized (messageBuffer) {
                duelLogText.appendText(messageBuffer.toString());
                messageBuffer.setLength(0);
            }
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e, "Drawing Area Too Small");
        }
    }

    @FXML
    void duelStopButtonPressed() {
        duelThread.interrupt();
    }

    Timeline timeline;

    void setupTimer() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> duelRefreshButtonPressed()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    void duelAutorefreshChBoxPressed() {
        if (duelAutorefreshChBox.isSelected()) {
            timeline.play();
        } else {
            timeline.stop();
        }
    }

    void disableDuelButtons(boolean value) {
        duelStopButton.setDisable(value);
        duelRefreshButton.setDisable(value);
        duelAutorefreshChBox.setDisable(value);
    }

    @FXML
    void aboutPressed() {
        dialog.showAbout();
    }

    private void setAutoBoardResizing(boolean value) {
        if (value) {
            boardPane.widthProperty().addListener(boardPaneSizeListener);
            boardPane.heightProperty().addListener(boardPaneSizeListener);
            draw.setResizable(true);
        } else {
            boardPane.widthProperty().removeListener(boardPaneSizeListener);
            boardPane.heightProperty().removeListener(boardPaneSizeListener);
            draw.setResizable(false);
        }
    }

    private ChangeListener<Number> boardPaneSizeListener = (observable, oldValue, newValue) -> {
        try {
            draw.drawAll(boardCanvas, boardPane);
        } catch (Exception e) {
        }
    };
}