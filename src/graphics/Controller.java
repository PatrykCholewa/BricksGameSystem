package graphics;

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

    private File logFile;

    Duel rewind;

    private File firstPlayer;
    private File followingPlayer;

    private Tournament tournament;

    private int randBoxNumber = 0;

    private BoardDraw draw = new BoardDraw();
    private Dialogs dialog = new Dialogs();

    private boolean backToTournamentFlag = false;

    @FXML
    void replayLogPressed() {
        setAutoBoardResizing(true);

        replayInitializeUI();

        File displayLogFile = dialog.showFileChooser("SelectLogFile", mainPane, false);
        replayNextButton.setDisable(false);
        replayFromFile(displayLogFile);
    }

    void replayFromFile(File displayLogFile){
        if (displayLogFile != null) {
            try {
                rewind = new Duel(displayLogFile);
                replayNick1Label.setText(rewind.getStartingPlayer());
                replayNick2Label.setText(rewind.getFollowingPlayer());

                draw.setBoardSize(Translator.getSizeFromInitString(rewind.getInitData()));
                draw.setObstaclePoints(Translator.boxesFromInitString(rewind.getInitData()));
                draw.removePlayersCells();
                draw.drawAllObstacles(boardCanvas,boardPane);

            } catch (FileNotFoundException e) {
                replayNextButton.setDisable(true);
                dialog.showErrorDialogWithStack(e);
            } catch (ProtocolException e) {
                replayNextButton.setDisable(true);
                dialog.showErrorDialogWithStack(e);
            } catch (Exception e) {
                replayNextButton.setDisable(true);
                dialog.showErrorDialogWithStack(e);
            }
        }
        else {
            replayNextButton.setDisable(true);
        }
    }

    void replayInitializeUI(){
        tourPane.setVisible(false);
        uniPane.setVisible(true);
        uniPane.toFront();
        replayPane.toFront();

        replayNick1Label.setText("...");
        replayNick2Label.setText("...");
        statusLabel.setText("");
        replayLogText.clear();

        replayBackButton.setVisible(false);
    }

    @FXML
    void nextPressed() {
        if (backToTournamentFlag)
            replayBackButton.setVisible(true);
        else
            replayBackButton.setVisible(false);
        try {
            if (!rewind.isFinished()) {
                readAndPrint();
            } else {
                readAndPrint();
                statusLabel.setText(rewind.getMessage());
                replayNextButton.setDisable(true);
            }

        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e);
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e);
        }
    }

    @FXML
    void replayBackButtonPressed(){
        backToTournamentFlag = false;
        tourInitializeUI();
    }

    private void readAndPrint() throws Exception {
        draw.drawAndAddCells(boardCanvas,boardPane,getPlayerID(rewind.getMoveCounter()),Translator.stringToBoxPair(rewind.getLastMove()));
        replayLogText.appendText(rewind.getLastMove()+"\n");
        rewind.nextMove();
    }

    @FXML
    void closePressed() {
        System.exit(0);
    }

    @FXML
    void setSizePressed() throws Exception {
        duelInitializeUI();
        draw.setBoardSize(dialog.showIntValueSelectDialog("Set board size", "Set board size", 21, 3, 1000));
        System.out.println("Size: " + draw.getBoardSize());
        draw.removeObstaclePoints();
        draw.removePlayersCells();
        draw.drawAllObstacles(boardCanvas,boardPane);
    }

    @FXML
    void randomBarrierPressed() {
        duelInitializeUI();
        randBoxNumber = dialog.showIntValueSelectDialog("Set random", "Set number of random boxes", 0, 0, draw.getBoardSize()*draw.getBoardSize());
        System.out.println("randBoxNumber: " + randBoxNumber);
        draw.clearBoard(boardCanvas);
        try {
            draw.drawNet(boardCanvas, boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void manualBarrierPressed() {
        tourPane.setVisible(false);
        uniPane.setVisible(true);
        uniPane.toFront();
        manualPane.toFront();

        try {
            draw.drawAllObstacles(boardCanvas,boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }

        menuBar.setDisable(true);

        try {
            draw.drawNet(boardCanvas,boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boardCanvas.setOnMouseClicked(this::boardMousePressedDragged);
        boardCanvas.setOnMouseDragged(this::boardMousePressedDragged);
    }

    @FXML
    void manualFinishButtonPressed(){
        boardCanvas.setOnMouseClicked(null);
        boardCanvas.setOnMouseDragged(null);
        startupPane.toFront();

        menuBar.setDisable(false);

    }

    @FXML
    void manualClearButtonPressed(){
        draw.removeObstaclePoints();
        try {
            draw.drawAllObstacles(boardCanvas,boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void boardMousePressedDragged(MouseEvent event)  {
        int x_pos = draw.convertToPos(event.getX());
        int y_pos = draw.convertToPos(event.getY());
        if (draw.verifyPos(x_pos) && draw.verifyPos(y_pos))
        {
            try {
                if (event.getButton() == MouseButton.PRIMARY) {
                    draw.addOneObstacleCell(boardCanvas, boardPane, new Point(x_pos, y_pos));
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    draw.removeOneObstacleCell(boardCanvas, boardPane, new Point(x_pos, y_pos));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void tourButtonPressed() {
        tourInitializeUI();


        tourStartButton.setDisable(false);

        File playersDir = dialog.showDriectoryChooser("Select Players Directory", boardPane);
        File resultsDir = dialog.showDriectoryChooser("Select Results Directory", boardPane);
        if (playersDir != null && resultsDir != null) {
            try {
                tournament = new Tournament(playersDir, resultsDir);

            } catch (IOException e) {
                dialog.showErrorDialogWithStack(e);
                tourStartButton.setDisable(true);
            }
        }
        else {
            dialog.showError(new Exception("Directories not set"));
            tourStartButton.setDisable(true);
        }
    }

    void tourInitializeUI(){
        uniPane.setVisible(false);
        tourPane.setVisible(true);
        tourPane.toFront();
    }

    @FXML
    void tourStartButtonPressed(){
        tourScoreText.clear();
        tourDuelsLog.getItems().clear();
        tourErrorsText.clear();
        try {
            tournament.start(draw.getBoardSize(),draw.getObstaclePoints());
            ////////////////////////////////////////////////////////////////////////////////////////////////////// TODO Only For Preview
            Scanner scn = new Scanner(new File("./test/testFiles/tourTest/results/score.txt"));
            while (scn.hasNext()){
                tourScoreText.appendText(scn.nextLine()+"\n");
            }
            scn.close();
            Scanner scn2 = new Scanner(new File("./test/testFiles/tourTest/results/duels.txt"));
            while (scn2.hasNext()){
                tourDuelsLog.getItems().add(scn2.nextLine()+"\n");
            }
            scn2.close();
            Scanner scn3 = new Scanner(new File("./test/testFiles/tourTest/results/err.txt"));
            while (scn3.hasNext()){
                tourErrorsText.appendText(scn3.nextLine()+"\n");
            }
            scn3.close();

            tourDuelsLog.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void TourReplayButtonPressed(){
        String name = tourDuelsLog.getSelectionModel().getSelectedItem();
        String[] split = name.split(":");
        String number = split[0];

        setAutoBoardResizing(true);
        replayInitializeUI();
        replayNextButton.setDisable(false);
        System.out.println("./test/testFiles/tourTest/results/duels/"+number+".txt");
        backToTournamentFlag = true;
        replayFromFile(new File("./test/testFiles/tourTest/results/duels/"+number+".txt"));
    }

    @FXML
    void duelPressed() {
        duelInitializeUI();

        firstPlayer = dialog.showDriectoryChooser("Select Starting Player Folder", boardPane);
        followingPlayer = dialog.showDriectoryChooser("Select Following Player Folder", boardPane);
        logFile = dialog.showFileChooser("Select Log File", mainPane, true);

        try {
            Duel duel = new Duel(firstPlayer, followingPlayer);
            duelNick1Label.setText(duel.getStartingPlayer());
            duelNick2Label.setText(duel.getFollowingPlayer());
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e, "Player Directory Not Found");
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e, "Can't Read Player Name");
        }
    }

    void duelInitializeUI(){
        tourPane.setVisible(false);
        uniPane.setVisible(true);
        uniPane.toFront();
        duelPane.toFront();

        duelNick1Label.setText("...");
        duelNick2Label.setText("...");
        duelLogText.clear();
    }

    @FXML
    void startPressed() {
        try {
            duelLogText.clear();
            draw.removePlayersCells();
            Duel duel = new Duel(firstPlayer, followingPlayer, logFile);
            if (randBoxNumber != 0) {
                ArrayList<Point> randBoxes = duel.setBoard(draw.getBoardSize(), randBoxNumber);
                draw.setObstaclePoints(randBoxes);
                draw.drawAllObstacles(boardCanvas, boardPane);
            } else {
                duel.setBoard(draw.getBoardSize(), draw.getObstaclePoints());
                draw.drawAllObstacles(boardCanvas, boardPane);
            }
            int i = 0;
            duel.start();
            while (!duel.isFinished()) {

                logAndPrint(duel.getLastMove(), getPlayerID(i++));
                duel.nextMove();
            }
            logAndPrint(duel.getLastMove(), getPlayerID(i++));
            statusLabel.setText(duel.getMessage());
            duel.close();
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e, "Log File Not Found");
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e, "Comunication Protocol Error");
        } catch (NullPointerException e) {
            dialog.showErrorDialogWithStack(e, "Log File Not Found");
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e);
        }
    }

    private int getPlayerID(int counter) {
        return ((counter) % 2) + 1;
    }

    private void logAndPrint(String move, int player) throws Exception {
        duelLogText.appendText(move + " :P" + player + '\n');
        try {
            draw.drawAndAddCells(boardCanvas, boardPane, player, Translator.stringToBoxPair(move));
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e);
        }

    }

    @FXML
    void aboutPressed() {
        dialog.showAbout();
    }

    void setAutoBoardResizing(boolean value) {
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

    ChangeListener<Number> boardPaneSizeListener = (observable, oldValue, newValue) -> {
        try {
            draw.drawAll(boardCanvas,boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}