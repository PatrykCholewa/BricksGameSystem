package graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Paweł Zych
 */

public class Drawing { ;
    private int size;
    private int scale;
    private boolean resizable;

    public Drawing() {
        this.size = 21;
        this.scale = 10;
        this.resizable = false;
    }

    public int getBoardSize() {
        return size;
    }

    public int getScale() {
        return scale;
    }

    public void setBoardSize(int boardSize) {
        this.size = boardSize;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    void drawNet(Canvas board, AnchorPane boardPane) throws Exception {
        calculateMaxScale(board,boardPane);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setLineCap(StrokeLineCap.SQUARE);
        for (int i = 0; i<= size; i++) {
            gc.strokeLine(snap(i*scale),0,snap(i*scale),scale* size);
            gc.strokeLine(0,snap(i*scale),scale* size,snap(i*scale));
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
        gc.setFill(Color.YELLOW);
        for (Point p: generatedPoints) {
            gc.fillRect(p.getX() * scale,p.getY() * scale, scale, scale);
        }
        drawNet(board,boardPane);
    }

    void drawManCell(Canvas board, AnchorPane boardPane,Point point,boolean clear) throws Exception {
        calculateMaxScale(board,boardPane);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        if (clear)
            gc.setFill(Color.WHITE);
        else
            gc.setFill(Color.YELLOW);
        gc.fillRect(point.getX() * scale,point.getY() * scale, scale, scale);
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
        if (resizable) {
            board.setHeight(boardPane.getHeight() + 1);
            board.setWidth(boardPane.getWidth() + 1);
            double width = boardPane.getWidth();
            double height = boardPane.getHeight();
            if (height < width) {
                scale = (int) height / (size);
            } else {
                scale = (int) width / (size);
            }
            if (scale < 3) {
                throw new Exception("Window too small to draw");
            }
        }
    }

    private double snap(double y) {
        return ((int) y) + .5;
    }

    public boolean verifyPos(int pos){
        return (pos >= 0) && (pos < size);
    }

    public int convertToPos(double coord){
        return (int)coord/scale;
    }
}
