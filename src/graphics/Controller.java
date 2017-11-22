package graphics;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.awt.*;

public class Controller {
    @FXML
    Button doit = new Button();
    @FXML
    Button drawRect = new Button();
    @FXML
    Label status = new Label();
    @FXML
    Label nickname1 = new Label();
    @FXML
    Label nickname2 = new Label();
    @FXML
    Canvas board = new Canvas();

    @FXML
    void doitPressed(){
        status.setText("Pressed");
        nickname1.setText("Player 1");
        nickname2.setText("Player too");
        drawBoard(10);
    }

    @FXML
    void drawRectPressed(){
        status.setText("Draw");
        drawCell(10,new Point(1,1),new Point(1,2),1);
        drawCell(10,new Point(4,4),new Point(5,4),2);

    }

    void drawBoard(int n){
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.setLineCap(StrokeLineCap.SQUARE);
        int size = (int) board.getHeight();
        int cellsize = size/n;
        for (int i = 0; i<=n;i++) {
            gc.strokeLine(i*cellsize,0,i*cellsize,size);
            gc.strokeLine(0,i*cellsize,size,i*cellsize);
        }
    }

    void drawCell(int n, Point b1, Point b2, int playerid) {
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        if(playerid ==1) {
            gc.setFill(Color.RED);
        } else if (playerid == 2) {
            gc.setFill(Color.BLUE);
        }
        int cellsize = (int) board.getHeight()/n;
        gc.fillRect(b1.getX() * cellsize,b1.getY() * cellsize, cellsize,cellsize);
        gc.strokeRect(b1.getX() * cellsize,b1.getY() * cellsize, cellsize,cellsize);
        gc.fillRect(b2.getX() * cellsize,b2.getY() * cellsize, cellsize,cellsize);
        gc.strokeRect(b2.getX() * cellsize,b2.getY() * cellsize, cellsize,cellsize);
    }
}
