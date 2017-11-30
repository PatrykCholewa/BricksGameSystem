package graphics;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import java.awt.*;
import java.util.IllegalFormatException;

public class Controller {
    int scale=10;
    int n;
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
    TextField tablesizeLabel = new TextField();
    @FXML
    Button tablesizeButton = new Button();
    @FXML
    Canvas board = new Canvas();
    @FXML
    AnchorPane boardPane = new AnchorPane();
    @FXML
    Label statusLabel = new Label();
    @FXML
    TextArea logText = new TextArea();


    @FXML
    void tablesizePressed(){
        try {
            n = Integer.parseInt(tablesizeLabel.getText());
        } catch (IllegalFormatException e) {
            statusLabel.setText("Wrong Number");
        }

        status.setText("Size: "+n);
        clearBoard();
        drawNet();
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
        drawSingleCell(0,new Point(Integer.parseInt(tablesizeLabel.getText())-1,Integer.parseInt(tablesizeLabel.getText())-1),new Point(0,0));

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
        for (int i = 0; i<=n;i++) {
            gc.strokeLine(snap(i*scale),0,snap(i*scale),scale*n);
            gc.strokeLine(0,snap(i*scale),scale*n,snap(i*scale));
        }
    }

    void calculateMaxScale (){
        board.setHeight(boardPane.getHeight()+1);
        board.setWidth(boardPane.getWidth()+1);
        double width = boardPane.getWidth();
        double height = boardPane.getHeight();
        if (height < width) {
            scale = (int)height/(n);
        } else {
            scale = (int) width / (n);
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


