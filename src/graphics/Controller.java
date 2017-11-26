package graphics;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.awt.*;

public class Controller {
    @FXML
    Button drawNet = new Button();
    @FXML
    Button drawRect = new Button();
    @FXML
    Label status = new Label();
    @FXML
    Label nickname1 = new Label();
    @FXML
    Label nickname2 = new Label();
    @FXML
    TextField tablesize = new TextField();
    @FXML
    Canvas board = new Canvas();

    @FXML
    void drawNetPressed(){
        status.setText("Pressed");
        nickname1.setText("Player 1");
        nickname2.setText("Player too");
        clearBoard();
        drawBoard(Integer.parseInt(tablesize.getText()));
    }

    @FXML
    void drawRectPressed(){
        status.setText("Draw");
        drawCell(Integer.parseInt(tablesize.getText()),new Point(1,1),new Point(1,2),1);
        drawCell(Integer.parseInt(tablesize.getText()),new Point(4,4),new Point(5,4),2);
        drawCell(Integer.parseInt(tablesize.getText()),new Point(Integer.parseInt(tablesize.getText())-1,Integer.parseInt(tablesize.getText())-1),new Point(0,0),0);

    }

    void drawBoard(int n){
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setLineCap(StrokeLineCap.SQUARE);
        int size = (int) board.getHeight();
        int cellsize = size/n;
        for (int i = 0; i<=n;i++) {
            gc.strokeLine(snap(i*cellsize),0,snap(i*cellsize),size);
            gc.strokeLine(0,snap(i*cellsize),size,snap(i*cellsize));
        }
    }

    void drawCell(int n, Point b1, Point b2, int playerid) {
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
        int cellsize = (int) board.getHeight()/n;
        gc.fillRect(b1.getX() * cellsize,b1.getY() * cellsize, cellsize,cellsize);
        gc.fillRect(b2.getX() * cellsize,b2.getY() * cellsize, cellsize,cellsize);
        drawBoard(n);
    }

    void clearBoard() {
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.clearRect(0, 0, board.getWidth(), board.getHeight());
    }

    private double snap(double y) {
        return ((int) y) + .5;
    }
}


