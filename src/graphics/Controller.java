package graphics;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import management.Duel;
import tools.Translator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * @author Paweł Zych
 */

public class Controller {
    @FXML
    AnchorPane mainPane = new AnchorPane();
    @FXML
    Button startButton = new Button();
    @FXML
    Button nextButton = new Button();
    @FXML
    Label nickname1 = new Label();
    @FXML
    Label nickname2 = new Label();
    @FXML
    Canvas board = new Canvas();
    @FXML
    AnchorPane boardPane = new AnchorPane();
    @FXML
    Label statusLabel = new Label();
    @FXML
    TextArea logText = new TextArea();
    @FXML
    MenuItem setSize = new MenuItem();
    @FXML
    MenuItem selectLog = new MenuItem();
    @FXML
    MenuItem displayLog = new MenuItem();
    @FXML
    MenuItem close = new MenuItem();
    @FXML
    MenuItem randomBarrier = new MenuItem();
    @FXML
    MenuItem manualBarrier = new MenuItem();
    @FXML
    MenuItem tournament = new MenuItem();
    @FXML
    MenuItem duelButton = new MenuItem();
    @FXML
    MenuItem about = new MenuItem();

    private File logFile;

    Duel rewind;

    private File firstPlayer;
    private File followingPlayer;

    private int randBoxPercent = 0;
    private Drawing draw = new Drawing();
    private Dialogs dialog = new Dialogs();


    @FXML
    void aboutPressed() {
        dialog.showAbout();
    }

    @FXML
    void closePressed() {
        System.exit(0);
    }

    @FXML
    void tournamentPressed() {
        dialog.showWipDialog();
    }

    @FXML
    void duelPressed() {
        startButton.setVisible(true);
        nextButton.setVisible(false);

        firstPlayer = dialog.showDriectoryChooser("Select Starting Player Folder", boardPane);
        followingPlayer = dialog.showDriectoryChooser("Select Following Player Folder", boardPane);
        try {
            Duel duel = new Duel(firstPlayer, followingPlayer);
            nickname1.setText(duel.getStrtingPlayer());
            nickname2.setText(duel.getFollowingPlayer());
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e, "Player Directory Not Found");
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e, "Can't Read Player Name");
        }
    }

    @FXML
    void manualBarrierPressed() {
        dialog.showWipDialog();
    }


    @FXML
    void randomBarrierPressed() {
        randBoxPercent = dialog.showIntValueSelectDialog("Set random", "Set percentage of random boxes", 25, 0, 50);
        //TODO Max to nie 50 tylko size*size (ale to są procenty //PZ)
        System.out.println("randBoxPercent: " + randBoxPercent);
        draw.clearBoard(board);
        try {
            draw.drawNet(board, boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void displayLogPressed() {
        File displayLogFile = dialog.showFileChooser("SelectLogFile", mainPane, false);
        logText.clear();

        statusLabel.setText("");
        startButton.setVisible(false);
        nextButton.setVisible(true);

        try {
            rewind = new Duel(displayLogFile);
            nickname1.setText(rewind.getStrtingPlayer());
            nickname2.setText(rewind.getFollowingPlayer());
            draw.setBoardSize(Translator.getSizeFromInitString(rewind.getInitData()));
            draw.clearBoard(board);
            draw.drawGenCells(board,boardPane,Translator.boxesFromInitString(rewind.getInitData()));
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e);
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e);
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e);
        }
    }

    @FXML
    void nextPressed() {
        try {
        if (!rewind.isFinished()) {

               readAndPrint();

        } else {
            statusLabel.setText(rewind.getMessage());
        }
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e);
        } catch (Exception e) {
            dialog.showErrorDialogWithStack(e);
        }
    }

    private void readAndPrint() throws Exception {
        draw.drawCells(board,boardPane,getPlayerID(rewind.getMoveCounter()),Translator.stringToBoxPair(rewind.getLastMove()));
        logText.appendText(rewind.getLastMove()+"\n");
        rewind.nextMove();
    }

    @FXML
    void selectLogPressed() {
        logFile = dialog.showFileChooser("CreateLogFile", mainPane, true);
    }

    @FXML
    void setSizePressed() throws Exception {
        initialize();
        draw.setBoardSize(dialog.showIntValueSelectDialog("Set board size", "Set board size", 21, 3, 1000));
        System.out.println("Size: " + draw.getBoardSize());
        draw.clearBoard(board);
        draw.drawNet(board, boardPane);
    }

    @FXML
    void startPressed() {
        try {
            draw.redrawNet(board, boardPane);
            logText.clear();
            Duel duel = new Duel(firstPlayer, followingPlayer, logFile);
            if (randBoxPercent != 0) {
                ArrayList<Point> boxes = duel.setBoard(draw.getBoardSize(), draw.getBoardSize() * draw.getBoardSize() * randBoxPercent / 100);
                //TODO Wartość MUSI być zczytywana od użytkownika.
                draw.drawGenCells(board, boardPane, boxes);
            } else {
                duel.setBoard(draw.getBoardSize(), new ArrayList<>());
            }
            int i = 0;
            duel.start();
            while (!duel.isFinished()) {
                //TODO Pamiętaj, że musisz dawać możliwość ręcznego przewijania.
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

    private int getPlayerID(int counter) {                              //TODO Takie metody nie w kontrolerze.
        return ((counter) % 2) + 1;
    }

    private void logAndPrint(String move, int player) throws Exception {
        logText.appendText(move + " :P" + player + '\n');
        try {
            draw.drawCells(board, boardPane, player, Translator.stringToBoxPair(move));
        } catch (ProtocolException e) {
            ;
        }

    }

    ChangeListener<Number> boardPaneSizeListener = (observable, oldValue, newValue) -> {
        try {
            draw.redrawNet(board, boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    void initialize() {
        boardPane.widthProperty().addListener(boardPaneSizeListener);
        boardPane.heightProperty().addListener(boardPaneSizeListener);
    }
}