package graphics;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    AnchorPane tourPane = new AnchorPane();
    @FXML
    Button tourStartButton = new Button();
    @FXML
    TextArea tourScoreText = new TextArea();
    @FXML
    TextArea tourDuelsText = new TextArea();
    @FXML
    TextArea tourErrorsText = new TextArea();
    @FXML
    Button duelStartButton = new Button();
    @FXML
    Button replayNextButton = new Button();
    @FXML
    Label replayNick1Label = new Label();
    @FXML
    Label replayNick2Label = new Label();
    @FXML
    Label duelNick1Label = new Label();
    @FXML
    Label duelNick2Label = new Label();
    @FXML
    TextArea replayLogText = new TextArea();
    @FXML
    TextArea duelLogText = new TextArea();
    @FXML
    Canvas boardCanvas = new Canvas();
    @FXML
    Label statusLabel = new Label();
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
    private ArrayList<Point> manBoxes = new ArrayList<>();

    private Drawing draw = new Drawing();
    private Dialogs dialog = new Dialogs();

    @FXML
    void displayLogPressed() {
        replayInitializeUI();
        setAutoBoardResizing(true);

        File displayLogFile = dialog.showFileChooser("SelectLogFile", mainPane, false);

        replayNextButton.setDisable(false);

        if (displayLogFile != null) {
            try {
                rewind = new Duel(displayLogFile);
                replayNick1Label.setText(rewind.getStartingPlayer());
                replayNick2Label.setText(rewind.getFollowingPlayer());
                draw.setBoardSize(Translator.getSizeFromInitString(rewind.getInitData()));
                draw.clearBoard(boardCanvas);
                draw.drawGenCells(boardCanvas, boardPane, Translator.boxesFromInitString(rewind.getInitData()));
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
    }

    @FXML
    void nextPressed() {
        setAutoBoardResizing(false);
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

    private void readAndPrint() throws Exception {
        draw.drawCells(boardCanvas,boardPane,getPlayerID(rewind.getMoveCounter()),Translator.stringToBoxPair(rewind.getLastMove()));
        replayLogText.appendText(rewind.getLastMove()+"\n");
        rewind.nextMove();
    }

    @FXML
    void closePressed() {
        System.exit(0);
    }

    @FXML
    void setSizePressed() throws Exception {
        draw.setBoardSize(dialog.showIntValueSelectDialog("Set board size", "Set board size", 21, 3, 1000));
        System.out.println("Size: " + draw.getBoardSize());
        draw.clearBoard(boardCanvas);
        draw.drawNet(boardCanvas, boardPane);
    }

    @FXML
    void randomBarrierPressed() {
        randBoxNumber = dialog.showIntValueSelectDialog("Set random", "Set number of random boxes", 25, 0, draw.getBoardSize()*draw.getBoardSize());
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
        dialog.showWipDialog();
    }

    @FXML
    void boardMousePressedDragged(MouseEvent event) throws Exception {
        int x_pos = draw.convertToPos(event.getX());
        int y_pos = draw.convertToPos(event.getY());
        if (draw.verifyPos(x_pos) && draw.verifyPos(y_pos))
        {
            if (event.getButton() == MouseButton.PRIMARY) {
                draw.drawManCell(boardCanvas, boardPane, new Point(x_pos, y_pos), false);
                if (!manBoxes.contains(new Point(x_pos,y_pos)))
                    manBoxes.add(new Point(x_pos,y_pos));
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                draw.drawManCell(boardCanvas, boardPane, new Point(x_pos, y_pos), true);
                manBoxes.remove(new Point(x_pos,y_pos));
            }
        }
    }

    @FXML
    void tourButtonPressed() {
        uniPane.setVisible(false);
        tourPane.setVisible(true);
        tourPane.toFront();

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

    @FXML
    void tourStartButtonPressed(){
        try {
            tournament.start("3");
            ////////////////////////////////////////////////////////////////////////////////////////////////////// TODO Only For Preview
            Scanner scn = new Scanner(new File("./test/testFiles/tourTest/results/score.txt"));
            while (scn.hasNext()){
                tourScoreText.appendText(scn.nextLine()+"\n");
            }
            scn.close();
            Scanner scn2 = new Scanner(new File("./test/testFiles/tourTest/results/duels.txt"));
            while (scn2.hasNext()){
                tourDuelsText.appendText(scn2.nextLine()+"\n");
            }
            scn2.close();
            Scanner scn3 = new Scanner(new File("./test/testFiles/tourTest/results/err.txt"));
            while (scn3.hasNext()){
                tourErrorsText.appendText(scn3.nextLine()+"\n");
            }
            scn3.close();
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            draw.redrawNet(boardCanvas, boardPane);
            duelLogText.clear();
            Duel duel = new Duel(firstPlayer, followingPlayer, logFile);
            if (randBoxNumber != 0) {
                ArrayList<Point> randBoxes = duel.setBoard(draw.getBoardSize(), randBoxNumber);
                draw.drawGenCells(boardCanvas, boardPane, randBoxes);
            } else {
                duel.setBoard(draw.getBoardSize(), manBoxes);
                draw.drawGenCells(boardCanvas, boardPane, manBoxes);
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
            draw.drawCells(boardCanvas, boardPane, player, Translator.stringToBoxPair(move));
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
            draw.redrawNet(boardCanvas, boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}