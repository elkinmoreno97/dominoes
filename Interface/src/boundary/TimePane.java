package boundary;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;

import arch.Cell;
import domain.Configuration;
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
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.geometry.*;


public class TimePane extends Pane{

	private final BarChart<String, Number> bc;
	private ComboBox b;
	private IntervalSlider slider;
	
	public TimePane(){
		
		b = new ComboBox();
		ObservableList<String> items = FXCollections.observableArrayList();
		
        items.add("1 Month");
        items.add("3 Month");
        items.add("6 Month");
        
        b.setItems(items);
        b.setValue(items.get(0));
        
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("");
        yAxis.setLabel("");
		bc = new BarChart<String, Number>(xAxis, yAxis);
		
		bc.setPrefHeight(Configuration.height/4);
		
		slider = new IntervalSlider(0, 1, 0.2, 0.8, 200);
		System.out.println("\n" + slider.getHeight());
		
		GridPane pane = new GridPane();
		pane.add(b, 0, 0, 1, 2);
		pane.add(bc, 1, 0);
		pane.add(slider, 1, 1);
		//pane.setGridLinesVisible(true);
		
		pane.setPrefHeight(Configuration.width/2);
		this.getChildren().add(pane);
		
	}
}