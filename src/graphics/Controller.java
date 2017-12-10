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

    private int randBoxNumber = 0;
    private ArrayList<Point> manBoxes = new ArrayList<>();

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
        uniPane.toFront();
        duelPane.toFront();
        nickname1.setText("...");
        nickname2.setText("...");
        logText.clear();

        firstPlayer = dialog.showDriectoryChooser("Select Starting Player Folder", boardPane);
        followingPlayer = dialog.showDriectoryChooser("Select Following Player Folder", boardPane);
        try {
            Duel duel = new Duel(firstPlayer, followingPlayer);
            nickname1.setText(duel.getStartingPlayer());
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
    void boardMousePressedDragged(MouseEvent event) throws Exception {
        int x_pos = draw.convertToPos(event.getX());
        int y_pos = draw.convertToPos(event.getY());
        if (draw.verifyPos(x_pos) && draw.verifyPos(y_pos))
        {
            if (event.getButton() == MouseButton.PRIMARY) {
                draw.drawManCell(board, boardPane, new Point(x_pos, y_pos), false);
                if (!manBoxes.contains(new Point(x_pos,y_pos)))
                        manBoxes.add(new Point(x_pos,y_pos));
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                draw.drawManCell(board, boardPane, new Point(x_pos, y_pos), true);
                manBoxes.remove(new Point(x_pos,y_pos));
            }
        }
    }


    @FXML
    void randomBarrierPressed() {
        randBoxNumber = dialog.showIntValueSelectDialog("Set random", "Set number of random boxes", 25, 0, draw.getBoardSize()*draw.getBoardSize());
        System.out.println("randBoxNumber: " + randBoxNumber);
        draw.clearBoard(board);
        try {
            draw.drawNet(board, boardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void displayLogPressed() {
        uniPane.toFront();
        replayPane.toFront();
        nickname1.setText("...");
        nickname2.setText("...");
        logText.clear();

        File displayLogFile = dialog.showFileChooser("SelectLogFile", mainPane, false);
        logText.clear();

        statusLabel.setText("");
        nextButton.setDisable(false);

        if (displayLogFile != null) {
            try {
                rewind = new Duel(displayLogFile);
                nickname1.setText(rewind.getStartingPlayer());
                nickname2.setText(rewind.getFollowingPlayer());
                draw.setBoardSize(Translator.getSizeFromInitString(rewind.getInitData()));
                draw.clearBoard(board);
                draw.drawGenCells(board, boardPane, Translator.boxesFromInitString(rewind.getInitData()));
            } catch (FileNotFoundException e) {
                nextButton.setDisable(true);
                dialog.showErrorDialogWithStack(e);
            } catch (ProtocolException e) {
                nextButton.setDisable(true);
                dialog.showErrorDialogWithStack(e);
            } catch (Exception e) {
                nextButton.setDisable(true);
                dialog.showErrorDialogWithStack(e);
            }
        }
        else {
            nextButton.setDisable(true);
        }
    }

    @FXML
    void nextPressed() {
        try {
        if (!rewind.isFinished()) {
               readAndPrint();
        } else {
            readAndPrint();
            statusLabel.setText(rewind.getMessage());
            nextButton.setDisable(true);
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
            if (randBoxNumber != 0) {
                ArrayList<Point> randBoxes = duel.setBoard(draw.getBoardSize(), randBoxNumber);
                draw.drawGenCells(board, boardPane, randBoxes);
            } else {
                duel.setBoard(draw.getBoardSize(), manBoxes);
                draw.drawGenCells(board, boardPane, manBoxes);
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
        //TODO Rozważ move + " :" + getLastPlayer to będzie można wywalić metodę wyżej.
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