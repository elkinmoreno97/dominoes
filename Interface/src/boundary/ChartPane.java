/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import domain.Dominoes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Daniel
 */
public class ChartPane extends Pane {

    private ComboBox<String> box;

    private final BarChart<String, Number> bc;

    private double maxZoom = 20;
    private double minZoom = 0.05;

    private double srcSceneX;
    private double srcSceneY;
    private double srcTranslateX;
    private double srcTranslateY;

    
    public ChartPane(Dominoes domino) {
        VBox vbox = new VBox();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(domino.getIdRow());
        yAxis.setLabel(domino.getIdCol());

        bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle(domino.getHistoric().toString());

        box = new ComboBox<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < domino.getMat().getMatrixDescriptor().getNumRows(); i++) {
            items.add(domino.getMat().getMatrixDescriptor().getRowAt(i));

        }
        box.setItems(items);
        box.getSelectionModel().select(0);

        box.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                // draw chart
                drawChart(domino);
            }
        });

        drawChart(domino);
        
        this.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                double srcX = event.getX() - bc.getTranslateX() - bc.prefWidth(-1) / 2;
                double srcY = event.getY() - bc.getTranslateY() - bc.prefHeight(-1) / 2;
                double trgX = srcX;
                double trgY = srcY;

                double factor = 0.05;

                if (event.getDeltaY() < 0 && bc.getScaleX() > minZoom) {
                    bc.setScaleX(bc.getScaleX() * (1 - factor));
                    bc.setScaleY(bc.getScaleY() * (1 - factor));
                    trgX = srcX * (1 - factor);
                    trgY = srcY * (1 - factor);
                } else if (event.getDeltaY() > 0 && bc.getScaleX() < maxZoom) {
                    bc.setScaleX(bc.getScaleX() * (1 + factor));
                    bc.setScaleY(bc.getScaleY() * (1 + factor));
                    trgX = srcX * (1 + factor);
                    trgY = srcY * (1 + factor);
                }
                bc.setTranslateX(bc.getTranslateX() - (trgX - srcX));
                bc.setTranslateY(bc.getTranslateY() - (trgY - srcY));

            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                double offsetX = event.getSceneX() - srcSceneX;
                double offsetY = event.getSceneY() - srcSceneY;
                double newTranslateX = srcTranslateX + offsetX;
                double newTranslateY = srcTranslateY + offsetY;

                bc.setTranslateX(newTranslateX);
                bc.setTranslateY(newTranslateY);

            }
        });
        this.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                srcSceneX = event.getSceneX();
                srcSceneY = event.getSceneY();
                srcTranslateX = bc.getTranslateX();
                srcTranslateY = bc.getTranslateY();

                cursorProperty().set(Cursor.CLOSED_HAND);
            }
        });
        this.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                cursorProperty().set(Cursor.OPEN_HAND);
            }
        });
        
        vbox.getChildren().add(box);
        vbox.getChildren().add(bc);
        this.getChildren().add(vbox);
        

    }

    private void drawChart(Dominoes domino) {
        bc.getData().removeAll(bc.getData());
        XYChart.Series series = new XYChart.Series();
        
        String itemSelected = box.getSelectionModel().getSelectedItem();
        int indexSelected = box.getSelectionModel().getSelectedIndex(); 
        
        series = new XYChart.Series();
        series.setName("row " + itemSelected); //row name

        for (int j = 0; j < domino.getMat().getRow(itemSelected).length; j++) {

            series.getData().add(new XYChart.Data("(" + indexSelected + "," + j + ")" // column name
                    , domino.getMat().getRow(itemSelected)[j]));
            System.out.println(domino.getMat().getRow(itemSelected)[j]);

        }
        System.out.println("--");
        bc.getData().add(series);
    }

}