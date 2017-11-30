package graphics;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.awt.*;

public class Controller {
    int scale=10;
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
    AnchorPane pane = new AnchorPane();

    @FXML
    void drawNetPressed(){
        status.setText(String.valueOf(pane.getHeight()));
        nickname1.setText("Player 1");
        nickname2.setText("Player too");
        clearBoard();
        drawBoard(Integer.parseInt(tablesize.getText()));
    }

    @FXML
    void drawRectPressed(){
        status.setText("Draw");
        drawCell(Integer.parseInt(tablesize.getText()),1,new Point(1,1),new Point(1,2));
        drawCell(Integer.parseInt(tablesize.getText()),2,new Point(4,4),new Point(5,4));
        drawCell(Integer.parseInt(tablesize.getText()),0,new Point(Integer.parseInt(tablesize.getText())-1,Integer.parseInt(tablesize.getText())-1),new Point(0,0));

    }

    void drawBoard(int n){
        calculateMaxScale(n);
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

    void calculateMaxScale (int n){
        board.setHeight(pane.getHeight());
        board.setWidth(pane.getWidth());
        double width = board.getWidth();
        double height = board.getHeight();
        if (height < width) {
            scale = (int)height/(n);
        } else {
            scale = (int) width / (n);
        }
        System.out.println("scale = " + scale);
        if (scale < 3) {
            nickname1.setText("Za małe okno!!!");
        }
    }

    void drawCell(int n, int playerid, Point b1, Point b2) {
        calculateMaxScale(n);
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


