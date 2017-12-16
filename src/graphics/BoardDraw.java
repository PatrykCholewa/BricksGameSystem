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

public class BoardDraw {
    private int size;
    private int scale;
    private boolean resizable;
    private boolean drawingNet;
    private ArrayList<Point> obstaclePoints;
    private ArrayList<Point> firstPoints;
    private ArrayList<Point> secondPoints;

    public BoardDraw() {
        this.size = 21;
        this.scale = 10;
        this.resizable = true;
        this.drawingNet = true;
        this.obstaclePoints = new ArrayList<>(10);
        this.firstPoints = new ArrayList<>(10);
        this.secondPoints = new ArrayList<>(10);
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


    public void setObstaclePoints(ArrayList<Point> obstaclePoints) {
        this.obstaclePoints = obstaclePoints;
    }

    public ArrayList<Point> getObstaclePoints() {
        return obstaclePoints;
    }

    public void removeObstaclePoints() {
        this.obstaclePoints.clear();
    }

    void addOneObstacleCell(Canvas board, AnchorPane boardPane, Point point) throws Exception {
        if (!obstaclePoints.contains(point)) {
            obstaclePoints.add(point);
        }
        drawAllObstacles(board, boardPane);
    }

    void removeOneObstacleCell(Canvas board, AnchorPane boardPane, Point point) throws Exception {
        obstaclePoints.remove(point);
        drawAllObstacles(board, boardPane);
    }

    void drawAllObstacles(Canvas board, AnchorPane boardPane) throws Exception {
        clearBoard(board);
        calculateMaxScale(board, boardPane);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFill(Color.YELLOW);
        for (Point p : obstaclePoints) {
            gc.fillRect(p.getX() * scale, p.getY() * scale, scale, scale);
        }
        drawNet(board, boardPane);
    }

    public void removePlayersCells() {
        this.firstPoints.clear();
        this.secondPoints.clear();
    }

    void addCells(int playerid, Point[] points) {
        if (playerid == 1) {
            for (Point p : points) {
                synchronized (firstPoints) {
                    firstPoints.add(p);
                }
            }
        } else {
            for (Point p : points) {
                synchronized (secondPoints) {
                    secondPoints.add(p);
                }
            }
        }
    }

    void drawAll(Canvas board, AnchorPane boardPane) throws Exception {
        calculateMaxScale(board, boardPane);
        clearBoard(board);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        if (drawingNet) {
            gc.setFill(Color.YELLOW);
            synchronized (obstaclePoints) {
                for (Point p : obstaclePoints) {
                    gc.fillRect(p.getX() * scale, p.getY() * scale, scale, scale);
                }
            }
            gc.setFill(Color.BLUE);
            synchronized (firstPoints) {
                for (Point p : firstPoints) {
                    gc.fillRect(p.getX() * scale, p.getY() * scale, scale, scale);
                }
            }
            gc.setFill(Color.RED);
            synchronized (secondPoints) {
                for (Point p : secondPoints) {
                    gc.fillRect(p.getX() * scale, p.getY() * scale, scale, scale);
                }
            }
            drawNet(board, boardPane);
        } else {
            synchronized (obstaclePoints) {
                gc.setFill(Color.YELLOW);
                for (Point p : obstaclePoints) {
                    gc.fillRect(p.getX(), p.getY(), 1, 1);
                }
            }
            synchronized (firstPoints) {
                gc.setFill(Color.BLUE);
                for (Point p : firstPoints) {
                    gc.fillRect(p.getX(), p.getY(), 1, 1);
                }
            }
            synchronized (secondPoints) {
                gc.setFill(Color.RED);
                for (Point p : secondPoints) {
                    gc.fillRect(p.getX(), p.getY(), 1, 1);
                }
            }
        }
    }

    void clearBoard(Canvas board) {
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.clearRect(0, 0, board.getWidth(), board.getHeight());
    }

    void drawNet(Canvas board, AnchorPane boardPane) throws Exception {
        calculateMaxScale(board, boardPane);
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setLineCap(StrokeLineCap.SQUARE);
        if (drawingNet) {
            for (int i = 0; i <= size; i++) {
                gc.strokeLine(snap(i * scale), 0, snap(i * scale), scale * size);
                gc.strokeLine(0, snap(i * scale), scale * size, snap(i * scale));
            }
        } else {
            gc.strokeLine(snap(size * scale), 0, snap(size * scale), scale * size);
            gc.strokeLine(0, snap(size * scale), scale * size, snap(size * scale));
        }
    }

    private void calculateMaxScale(Canvas board, AnchorPane boardPane) throws Exception {
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
            if (scale < 1) {
                throw new Exception("Window too small to draw");
            } else if (scale > 0 && scale < 2) {
                drawingNet = false;
            } else {
                drawingNet = true;
            }
        }
    }

    private double snap(double y) {
        return ((int) y) + .5;
    }

    public boolean verifyPos(int pos) {
        return (pos >= 0) && (pos < size);
    }

    public int convertToPos(double coord) {
        return (int) coord / scale;
    }
}
