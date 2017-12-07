package graphics;

import archiving.Reader;
import archiving.Recorder;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import processes.Court;
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
    private Dialogs dialog = new Dialogs();
    private Recorder rec;
    private Reader reader;
    private File firstPlayer;
    private File followingPlayer;
    private int scale = 10;
    private int boardSize = 21;
    private int randBoxPercent = 0;

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

        firstPlayer =  dialog.showDriectoryChooser("Select Starting Player Folder",boardPane);
        followingPlayer =  dialog.showDriectoryChooser("Select Following Player Folder",boardPane);
        try {
            Court court = new Court( firstPlayer , followingPlayer);
            nickname1.setText(court.getStartingPlayerNick());
            nickname2.setText(court.getFollowingPlayerNick());
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e,"Player Directory Not Found");
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e,"Can't Read Player Name");
        }
    }

    @FXML
    void manualBarrierPressed() {
        dialog.showWipDialog();
    }

    @FXML
    void randomBarrierPressed() {
        randBoxPercent = dialog.showIntValueSelectDialog("Set random","Set percentage of random boxes",25,0,50);
        System.out.println("randBoxPercent: " + randBoxPercent);
        clearBoard();
        drawNet();
    }

    @FXML
    void displayLogPressed() {
        try {
            reader = new Reader(dialog.showFileChooser("SelectLogFile",mainPane,false));
            logText.clear();
            statusLabel.setText("");
            startButton.setVisible(false);
            nextButton.setVisible(true);

            reader.readHeader();
            boardSize = reader.size;
            nickname1.setText(reader.nickname1);
            nickname2.setText(reader.nickname2);

            clearBoard();
            drawNet();
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e,"Log File Not Found");
        }
    }

    @FXML
    void nextPressed()  {
        String nextLine;
        try {
            nextLine = reader.readNext();
            if (nextLine != null) {
                String[] split = nextLine.split(" ");
                if(split[0].equals("G")){
                    String []split2 = split[1].split( "x" );
                    drawGenCell(new Point(Integer.parseInt(split2[0]),Integer.parseInt(split2[1])));
                    nextButton.fire();
                }
                else {
                    Point[] points;
                    points = Translator.stringToBoxPair(split[0]);
                    drawCells(reader.getPlayer(), points[0], points[1]);
                    logText.appendText(nextLine + "\n");
                }
            } else {
                statusLabel.setText("END OF FILE");
            }
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e,"Wrong File Format");
        }
    }

    @FXML
    void selectLogPressed() {
        try {
            rec = new Recorder(dialog.showFileChooser("CreateLogFile",mainPane,true));
        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e,"Log File Not Found");
        }
    }

    @FXML
    void setSizePressed(){
        initialize();
        boardSize = dialog.showIntValueSelectDialog("Set board size","Set board size",21,3,1000);
        System.out.println("Size: " + boardSize);
        clearBoard();
        drawNet();
    }

    @FXML
    void startPressed(){
        initialize();
        try {
            Court court = new Court( firstPlayer , followingPlayer);
            rec.printHeader(boardSize,court.getStartingPlayerNick(),court.getFollowingPlayerNick());
            if(randBoxPercent != 0) {
                ArrayList<Point> boxes = court.setBoard(boardSize, boardSize * boardSize * randBoxPercent /100);
                drawGenCells(boxes);
                rec.printGenCells(boardSize, boxes);
            } else {
                court.setBoard(boardSize, new ArrayList<>() );
            }
            court.start();
            int no=0;
            while (court.getMessage() == "OK") {
                logAndPrint(court.getLastMove(),rec,((no++)%2)+1);
                court.nextMove();
            }
            logAndPrint(court.getLastMove(),rec,((no++)%2)+1);

            statusLabel.setText(court.getMessage());
            rec.logClose();
            court.close();


        } catch (FileNotFoundException e) {
            dialog.showErrorDialogWithStack(e,null);
        } catch (ProtocolException e) {
            dialog.showErrorDialogWithStack(e,"Comunication Protocol Error");
        } catch (IllegalArgumentException e) {
            dialog.showErrorDialogWithStack(e,"Wrong Size of Board");
        } catch (NullPointerException e) {
            dialog.showErrorDialogWithStack(e,"Log File Not Found");
        }

    }

    void logAndPrint(String move, Recorder rec, int player) throws ProtocolException {
        Point[] points = Translator.stringToBoxPair(move);
        drawCells(player, points[0], points[1]);
        logText.appendText(move+" :P"+player+'\n');
        rec.printToLog(move+" :P"+player+'\n');
    }

    ChangeListener<Number> boardPaneSizeListener = (observable, oldValue, newValue) ->
            redrawNet();

    void initialize(){
        boardPane.widthProperty().addListener(boardPaneSizeListener);
        boardPane.heightProperty().addListener(boardPaneSizeListener);
    }

    void redrawNet(){
        clearBoard();
        drawNet();
    }


    void drawNet(){
        calculateMaxScale();
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setLineCap(StrokeLineCap.SQUARE);
        for (int i = 0; i<= boardSize; i++) {
            gc.strokeLine(snap(i*scale),0,snap(i*scale),scale* boardSize);
            gc.strokeLine(0,snap(i*scale),scale* boardSize,snap(i*scale));
        }
    }

    void calculateMaxScale (){
        board.setHeight(boardPane.getHeight()+1);
        board.setWidth(boardPane.getWidth()+1);
        double width = boardPane.getWidth();
        double height = boardPane.getHeight();
        if (height < width) {
            scale = (int)height/(boardSize);
        } else {
            scale = (int) width / (boardSize);
        }
        if (scale < 3) {
            statusLabel.setText("Okno zbyt małe by poprawnie narysować");
        }
    }

    void drawGenCells(ArrayList<Point> generatedPoints) {
        calculateMaxScale();
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFill(Color.GREY);
        for (Point p: generatedPoints) {
            gc.fillRect(p.getX() * scale,p.getY() * scale, scale, scale);
        }
        drawNet();
    }

    void drawGenCell(Point g) {
        calculateMaxScale();
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFill(Color.GREY);
        gc.fillRect(g.getX() * scale,g.getY() * scale, scale, scale);
        drawNet();
    }

    void drawCells(int playerid, Point b1, Point b2) {
        calculateMaxScale();
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        if(playerid ==1) {
            gc.setFill(Color.BLUE);
        } else {
            gc.setFill(Color.RED);
        }
        gc.fillRect(b1.getX() * scale,b1.getY() * scale, scale, scale);
        gc.fillRect(b2.getX() * scale,b2.getY() * scale, scale, scale);
        drawNet();
    }

    void clearBoard() {
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.clearRect(0, 0, board.getWidth(), board.getHeight());
    }

    private double snap(double y) {
        return ((int) y) + .5;
    }
}