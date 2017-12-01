package graphics;

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
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.Optional;

public class Controller {
    int scale=10;
    int size;
    int i =1;

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
    MenuItem duel = new MenuItem();
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
        showWIP();
    }

    @FXML
    void duelPressed() {
        showWIP();
    }

    @FXML
    void manualBarrierPressed() {
        showWIP();
    }

    @FXML
    void randomBarrierPressed() {
        showWIP();
    }

    @FXML
    void displayLogPressed() {
        System.out.println(selectLogFile(true,false).toString());
    }

    @FXML
    void selectLogPressed() {
        System.out.println((selectLogFile(false,true)).toString());
    }

    File selectLogFile(boolean read,boolean write){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select log file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog((Stage) boardPane.getScene().getWindow());
        if (selectedFile != null) {
            return selectedFile;
        }
        return null;
    }

    void showWIP(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Work in progress");
        alert.setContentText("");

        alert.showAndWait();
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
}


