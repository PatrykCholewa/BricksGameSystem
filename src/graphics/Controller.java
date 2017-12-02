package graphics;

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
import processes.Trial;


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
    File logFile;
    private int scale=10;
    private int size =17;
    private int i =1;

    @FXML
    AnchorPane mainPane = new AnchorPane();
    @FXML
    Button startButton = new Button();
    @FXML
    Button drawTestButton = new Button();
    @FXML
    Label status = new Label();
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
        File startingPlayer =  showDriectoryChooser("Select Starting Player Folder",boardPane);
        File followingPlayer =  showDriectoryChooser("Select Following Player Folder",boardPane);
        try {
            Recorder rec = new Recorder(logFile);
            rec.printToLog("Trial Start\n");
            rec.logClose();

            Trial duel = new Trial(startingPlayer,followingPlayer);
            nickname1.setText(duel.getStartingPlayerNick());
            nickname2.setText(duel.getFollowingPlayerNick());
            duel.setBoard(size,new ArrayList<Point>());
            duel.start();
        } catch (FileNotFoundException e) {
             showErrorDialog(e);
        } catch (ProtocolException e) {
             showErrorDialog(e);
        } catch (Exception e) {
            showErrorDialog(e);
        }
    }

    @FXML
    void manualBarrierPressed() {
         showWipDialog();
    }

    @FXML
    void randomBarrierPressed() {
         showWipDialog();
    }

    @FXML
    void displayLogPressed() {
        System.out.println( showFileChooser("SelectLogFile",mainPane,false).toString());
    }

    @FXML
    void selectLogPressed() {
        logFile = showFileChooser("CreateLogFile",mainPane,true);
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
        logText.appendText("Text test x" + i++ + "\n");
    }

    @FXML
    void drawTestPressed(){
        status.setText("Test draw");
        drawSingleCell(1,new Point(1,1),new Point(1,2));
        drawSingleCell(2,new Point(4,4),new Point(5,4));
        drawSingleCell(0,new Point(size-1,size-1),new Point(0,0));

    }

    ChangeListener<Number> boardPaneSizeListener = (observable, oldValue, newValue) ->
            redrawNet();

    void initialize(){
        boardPane.widthProperty().addListener(boardPaneSizeListener);
        boardPane.heightProperty().addListener(boardPaneSizeListener);
        nickname1.setText("Player 1");
        nickname2.setText("Player 2");
    }

    void redrawNet(){
        clearBoard();
        drawNet();
    }


    void drawNet(){
        calculateMaxScale();
        System.out.println("Board size: "+ board.getHeight()+ " x " + board.getWidth());
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
        System.out.println("scale = " + scale);
        if (scale < 3) {
            statusLabel.setText("Okno zbyt małe by poprawnie narysować");
        }
    }

    void drawSingleCell(int playerid, Point b1, Point b2) {
        calculateMaxScale();
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        if(playerid ==1) {
            gc.setFill(Color.RED);
        } else if (playerid == 2) {
            gc.setFill(Color.BLUE);
        } else {
            gc.setFill(Color.GREY);
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

    void showErrorDialog(Exception ex){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("Oops! There was an exception.");
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
        File selectedDirectory = directoryChooser.showDialog(component.getScene().getWindow());

        if(selectedDirectory == null){
            return selectedDirectory;
        }
        return null;
    }

    File showFileChooser(String title, Pane component, boolean save){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
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


