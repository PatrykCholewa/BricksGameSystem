package graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.awt.*;
import java.util.ArrayList;

public class Drawing { ;
    private int boardSize;
    private int scale;

    public Drawing() {
        this.boardSize = 21;
        this.scale = 10;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getScale() {
        return scale;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    void drawNet(Canvas board, AnchorPane boardPane) throws Exception {
        calculateMaxScale(board,boardPane);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setLineCap(StrokeLineCap.SQUARE);
        for (int i = 0; i<= boardSize; i++) {
            gc.strokeLine(snap(i*scale),0,snap(i*scale),scale* boardSize);
            gc.strokeLine(0,snap(i*scale),scale* boardSize,snap(i*scale));
        }
    }

    void redrawNet(Canvas board,AnchorPane boardPane) throws Exception {
        clearBoard(board);
        drawNet(board,boardPane);
    }

    void drawGenCells(Canvas board, AnchorPane boardPane,  ArrayList<Point> generatedPoints) throws Exception {
        calculateMaxScale(board,boardPane);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFill(Color.GREY);
        for (Point p: generatedPoints) {
            gc.fillRect(p.getX() * scale,p.getY() * scale, scale, scale);
        }
        drawNet(board,boardPane);
    }

    void drawCells(Canvas board, AnchorPane boardPane, int playerid, Point[] points) throws Exception {
        calculateMaxScale(board,boardPane);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        if(playerid ==1) {
            gc.setFill(Color.BLUE);
        } else {
            gc.setFill(Color.RED);
        }
        for (Point p:points) {
            gc.fillRect(p.getX() * scale,p.getY() * scale, scale, scale);
        }
        drawNet(board,boardPane);
    }

    void clearBoard(Canvas board) {
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.clearRect(0, 0, board.getWidth(), board.getHeight());
    }

    private void calculateMaxScale (Canvas board, AnchorPane boardPane) throws Exception {
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
            throw new Exception("Window too small to draw");
        }
    }

    private double snap(double y) {
        return ((int) y) + .5;
    }
}
