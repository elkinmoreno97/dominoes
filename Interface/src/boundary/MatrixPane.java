/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import java.sql.NClob;
import java.util.ArrayList;

import arch.Cell;
import arch.MatrixDescriptor;
import domain.Dominoes;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Daniel
 */
public class MatrixPane extends Pane {

    private double maxZoom = 2;
    private double minZoom = 0.05;

    private double srcSceneX;
    private double srcSceneY;
    private double srcTranslateX;
    private double srcTranslateY;

    public MatrixPane(Dominoes domino) {
    	
        System.out.println("Rows: " + domino.getMat().getMatrixDescriptor().getNumRows() +
        		" Cols: " + domino.getMat().getMatrixDescriptor().getNumCols());
        
        Group group = new Group();
        MatrixDescriptor _descriptor = domino.getMat().getMatrixDescriptor();
        
        float min = domino.getMat().findMinValue();
        float max = domino.getMat().findMaxValue();
        
        double beginRowHead;
        double endRowHead;
        double beginColumnHead;
        double endColumnHead;
        
        double width;
    	double height;
        
        double padding = 0;
        double cellSpace = 20;
        double charSpace = 7;
        double largerSize = 0;
        
        
        int _nRows = _descriptor.getNumRows();
        int _nCols = _descriptor.getNumCols();
        
        for(int i = 0; i < domino.getMat().getMatrixDescriptor().getNumRows(); i++){
        	if(domino.getMat().getMatrixDescriptor().getRowAt(i).length() > largerSize){
        		largerSize = domino.getMat().getMatrixDescriptor().getRowAt(i).length();
        	}
        }
        
        beginRowHead = -1 * largerSize * charSpace;
        endRowHead = 0;
        
        for(int i = 0; i < domino.getMat().getMatrixDescriptor().getNumCols(); i++){
        	if(domino.getMat().getMatrixDescriptor().getColumnAt(i).length() > largerSize){
        		largerSize = domino.getMat().getMatrixDescriptor().getColumnAt(i).length();
        	}
        }
        
        beginColumnHead = -1 * largerSize * charSpace;
        endColumnHead = 0;
        
        width = Math.abs(endRowHead - beginRowHead);
    	height = cellSpace;
    	
        // draw the label of the matrix row/columns
        for (int i = 0; i < _descriptor.getNumRows(); i++) {
        	largerSize = domino.getMat().getMatrixDescriptor().getRowAt(i).length();
        	Rectangle back = new Rectangle(width, height);
            back.setFill(new Color(0.5, 0.5, 0.5, 0.1));
            back.setTranslateX(0);
            back.setTranslateY(0);
            back.toBack();
            
            Rectangle front = new Rectangle(width, height);
            front.setFill(new Color(0, 0, 1, 0.5 + (0.5 * ((-1) * i%2))));
            front.setTranslateX(0);
            front.setTranslateY(0);
            front.toFront();

            Group cell = new Group(back, front);
            cell.setTranslateX(beginRowHead);
            cell.setTranslateY(i * (cellSpace + padding) + padding);
            
            Text text = new Text(domino.getMat().getMatrixDescriptor().getRowAt(i));
            text.setTranslateX(beginRowHead);
            text.setTranslateY(i * (cellSpace + padding) + padding + height);
            if(i%2 == 0){
            	text.setFill(Color.WHITE);
            }else{
            	text.setFill(Color.BLACK);
            }
            text.toFront();

            group.getChildren().add(new Group(cell, text));
        	
        }
        
        width = Math.abs(endColumnHead - beginColumnHead);
    	height = cellSpace;
        
        for (int i = 0; i < _descriptor.getNumCols(); i++) {
        	Rectangle back = new Rectangle(width, height);
        	back.setTranslateX(0);
        	back.setTranslateY(0);
            back.setFill(new Color(0.5, 0.5, 0.5, 0.1));

            Rectangle front = new Rectangle(width, height);
            front.setTranslateX(0);
        	front.setTranslateY(0);
            front.setFill(new Color(0, 0, 1, 0.5 + (0.5 * ((-1) * i%2))));

            front.toFront();
            
            Group cell = new Group(back, front);
            
            Text text = new Text(domino.getMat().getMatrixDescriptor().getColumnAt(i));
            text.setTranslateX(endColumnHead);
            text.setTranslateY(height);

            if(i%2 == 0){
            	text.setFill(Color.WHITE);
            }else{
            	text.setFill(Color.BLACK);
            }
            text.toFront();
            
            Group g = new Group(cell, text);
            g.setTranslateX(1 + (i * (cellSpace + padding) + padding) + (height/2 - width/2));
            g.setTranslateY(-1 + ((-1) * (cellSpace + padding)) - (width/2 - height/2));
            g.setRotate(-90);
            
            group.getChildren().add(g);
        }
        
        // draw the matrix information
        ArrayList<Cell> cells = domino.getMat().getNonZeroData();
        
        for (Cell _matCell : cells){
        	Rectangle back = new Rectangle(cellSpace, cellSpace);
            back.setFill(new Color(1, 1, 1, 1));
            Rectangle front = new Rectangle(cellSpace, cellSpace);
            front.setFill( new Color(0.0f, 0.0f, 1.0f, (_matCell.value - min) / (max - min) ));           
            front.toFront();
            
            Group cell = new Group(back, front);
            cell.setTranslateX(_matCell.col * (cellSpace + padding) + padding);
            cell.setTranslateY(_matCell.row * (cellSpace + padding) + padding);

            Tooltip.install(cell, new Tooltip(String.valueOf(_matCell.value)));
                
            group.getChildren().add(cell);
        }

        this.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                double srcX = event.getX() - group.getTranslateX() - group.prefWidth(-1) / 2;
                double srcY = event.getY() - group.getTranslateY() - group.prefHeight(-1) / 2;
                double trgX = srcX;
                double trgY = srcY;

                double factor = 0.05;

                if (event.getDeltaY() < 0 && group.getScaleX() > minZoom) {
                    group.setScaleX(group.getScaleX() * (1 - factor));
                    group.setScaleY(group.getScaleY() * (1 - factor));
                    trgX = srcX * (1 - factor);
                    trgY = srcY * (1 - factor);
                } else if (event.getDeltaY() > 0 && group.getScaleX() < maxZoom) {
                    group.setScaleX(group.getScaleX() * (1 + factor));
                    group.setScaleY(group.getScaleY() * (1 + factor));
                    trgX = srcX * (1 + factor);
                    trgY = srcY * (1 + factor);
                }
                group.setTranslateX(group.getTranslateX() - (trgX - srcX));
                group.setTranslateY(group.getTranslateY() - (trgY - srcY));

            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                double offsetX = event.getSceneX() - srcSceneX;
                double offsetY = event.getSceneY() - srcSceneY;
                double newTranslateX = srcTranslateX + offsetX;
                double newTranslateY = srcTranslateY + offsetY;

                group.setTranslateX(newTranslateX);
                group.setTranslateY(newTranslateY);

            }
        });
        this.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                srcSceneX = event.getSceneX();
                srcSceneY = event.getSceneY();
                srcTranslateX = group.getTranslateX();
                srcTranslateY = group.getTranslateY();

                cursorProperty().set(Cursor.CLOSED_HAND);
            }
        });
        this.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                cursorProperty().set(Cursor.OPEN_HAND);
            }
        });
        
        this.getChildren().add(new FlowPane(group));
        
    }

}
