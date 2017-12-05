package graphics;

import archiving.Reader;
import archiving.Recorder;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import processes.Court;
import tools.Translator;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Paweł Zych
 */


public class Controller {
    private Recorder rec;
    private Reader reader;
    private File firstPlayer;
    private File followingPlayer;
    private int scale=10;
    private int size =17;
    private int genRandom= 0;

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Autorzy:");
        alert.setContentText("Patryk \nPaweł");

        alert.showAndWait();
    }

    @FXML
    void closePressed() {
        System.exit(0);
    }

    @FXML
    void tournamentPressed() {
        showWipDialog();
    }

    @FXML
    void duelPressed() {
        startButton.setVisible(true);
        nextButton.setVisible(false);
        firstPlayer =  showDriectoryChooser("Select Starting Player Folder",boardPane);
        followingPlayer =  showDriectoryChooser("Select Following Player Folder",boardPane);
        try {
            Court court = new Court( firstPlayer , followingPlayer);
            nickname1.setText(court.getStartingPlayerNick());
            nickname2.setText(court.getFollowingPlayerNick());
        } catch (FileNotFoundException e) {
            showErrorDialog(e,"Player Directory Not Found");
        } catch (ProtocolException e) {
            showErrorDialog(e,"Can't Read Player Name");
        }
    }

    @FXML
    void manualBarrierPressed() {
        showWipDialog();
    }

    @FXML
    void randomBarrierPressed() {
        TextInputDialog dialog = new TextInputDialog("17");
        dialog.setTitle("Set random");
        dialog.setHeaderText("Set % of random");
        dialog.setContentText("(int)");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                genRandom = Integer.parseInt(result.get());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(result.get()+" is not an number!!!");
                alert.setContentText("");
                alert.showAndWait();
                statusLabel.setText("Wrong Number");
            }
            System.out.println("genRandom: " + result.get());
            clearBoard();
            drawNet();
        }
    }

    @FXML
    void displayLogPressed() {
        try {
            reader = new Reader(showFileChooser("SelectLogFile",mainPane,false));
            logText.clear();
            statusLabel.setText("");
            startButton.setVisible(false);
            nextButton.setVisible(true);

            reader.readHeader();
            size = reader.size;
            nickname1.setText(reader.nickname1);
            nickname2.setText(reader.nickname2);

            clearBoard();
            drawNet();
        } catch (FileNotFoundException e) {
            showErrorDialog(e,"Log File Not Found");
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
            showErrorDialog(e,"Wrong File Format");
        }
    }

    @FXML
    void selectLogPressed() {
        try {
            rec = new Recorder(showFileChooser("CreateLogFile",mainPane,true));
        } catch (FileNotFoundException e) {
            showErrorDialog(e,"Log File Not Found");
        }
    }

    @FXML
    void setSizePressed(){
        initialize();
        TextInputDialog dialog = new TextInputDialog("17");
        dialog.setTitle("Set board size");
        dialog.setHeaderText("Set board size");
        dialog.setContentText("size");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                size = Integer.parseInt(result.get());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(result.get()+" is not an number!!!");
                alert.setContentText("");
                alert.showAndWait();
                statusLabel.setText("Wrong Number");
            }
            System.out.println("Size: " + result.get());
            clearBoard();
            drawNet();
        }
    }

    @FXML
    void startPressed(){
        initialize();
        try {
            Court court = new Court( firstPlayer , followingPlayer);
            rec.printHeader(size,court.getStartingPlayerNick(),court.getFollowingPlayerNick());
            if(genRandom != 0) {
                ArrayList<Point> boxes = court.setBoard(size, size * size * genRandom/100);
                drawGenCells(boxes);
                rec.printGenCells(boxes);
            } else {
                court.setBoard( size , new ArrayList<>() );
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
            showErrorDialog(e,null);
        } catch (ProtocolException e) {
            showErrorDialog(e,"Comunication Protocol Error");
        } catch (IllegalArgumentException e) {
            showErrorDialog(e,"Wrong Size of Board");
        } catch (NullPointerException e) {
            showErrorDialog(e,"Log File Not Found");
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
        for (int i = 0; i<= size; i++) {
            gc.strokeLine(snap(i*scale),0,snap(i*scale),scale* size);
            gc.strokeLine(0,snap(i*scale),scale* size,snap(i*scale));
        }
    }

    void calculateMaxScale (){
        board.setHeight(boardPane.getHeight()+1);
        board.setWidth(boardPane.getWidth()+1);
        double width = boardPane.getWidth();
        double height = boardPane.getHeight();
        if (height < width) {
            scale = (int)height/(size);
        } else {
            scale = (int) width / (size);
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

    void showErrorDialog(Exception ex, String myMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        if (myMessage == null)
            alert.setHeaderText("Oops! There was an exception.");
        else
            alert.setHeaderText(myMessage);
        alert.setContentText("Exception Type: " + ex.getClass().getSimpleName());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Exception Stack Trace:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    void showWipDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Work in progress");
        alert.setContentText("");

        alert.showAndWait();
    }

    File showDriectoryChooser(String title, Pane component){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(new File("./test/testFiles"));
        File selectedDirectory = directoryChooser.showDialog(component.getScene().getWindow());

        if(selectedDirectory != null){
            return selectedDirectory;
        }
        return null;
    }

    File showFileChooser(String title, Pane component, boolean save){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File("./src/archiving"));
        fileChooser.setInitialFileName("defaultLog.txt");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile;
        if (save)
            selectedFile = fileChooser.showSaveDialog(component.getScene().getWindow());
        else
            selectedFile = fileChooser.showOpenDialog(component.getScene().getWindow());

        if (selectedFile != null) {
            return selectedFile;
        }
        return null;
    }
}