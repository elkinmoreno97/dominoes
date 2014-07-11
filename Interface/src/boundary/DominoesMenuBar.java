/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import domain.Configuration;
import domain.Dominoes;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author Daniel
 */
public class DominoesMenuBar extends MenuBar {

//------DOMINOES MENU ITENS-----------------------------------------------------
    private final Menu MDominoes;

    private final MenuItem MDominoes_new;
    private final MenuItem MDominoes_load;
    private final MenuItem MDominoes_exit;
    private final MenuItem MDominoes_exitAndSave;
    private final Menu MDominoes_save;
    private final MenuItem MDominoes_save_saveAll;
    private final CheckMenuItem MDominoes_save_autoSave;

//------EDIT MENU ITENS---------------------------------------------------------
    private final Menu MEdit;

    private final Menu MEdit_fillColor;
    private final CustomMenuItem MEdit_fillColor_custom;
    private Slider MEdit_fillColor_slider;
    private final CheckMenuItem MEdit_showHistoric;

//------COFIGURATION MENU ITENS-------------------------------------------------
    private final Menu MConfiguration;
    private final CheckMenuItem MConfiguration_fullScreen;
    private final Menu MConfiguration_database;
    private final RadioMenuItem MConfiguration_database_accessTXT;
    private final RadioMenuItem MConfiguration_database_accessSQL;
    private final ToggleGroup MConfiguration_database_accessGroup;
    
    private final SeparatorMenuItem separator;

    /**
     * Builder class
     */
    public DominoesMenuBar() {
        this.setHeight(30);
//------DOMINOES MENU ITENS-----------------------------------------------------
        this.MDominoes = new Menu("Dominoes");

        this.MDominoes_new = new MenuItem("New");
        this.MDominoes_load = new MenuItem("Load");
        this.MDominoes_load.setDisable(true);
        this.MDominoes_save = new Menu("Save");
        this.MDominoes_save_saveAll = new MenuItem("Save All");
        this.MDominoes_save_autoSave = new CheckMenuItem("Auto Save");
        this.MDominoes_save_autoSave.setSelected(Configuration.autoSave);
        this.MDominoes_exit = new MenuItem("Exit");
        this.MDominoes_exitAndSave = new MenuItem("Exit And Save");

        this.MDominoes_save.getItems().addAll(this.MDominoes_save_saveAll,
                this.MDominoes_save_autoSave);

        this.separator = new SeparatorMenuItem();

        this.MDominoes.getItems().addAll(this.MDominoes_new, this.MDominoes_load,
                this.MDominoes_save, this.separator, MDominoes_exitAndSave,
                this.MDominoes_exit);

//------EDIT MENU ITENS---------------------------------------------------------
        this.MEdit = new Menu("Edit");

        this.MEdit_fillColor = new Menu("Color Fill");
        this.MEdit_fillColor_slider = new Slider(0, 1, Dominoes.COLOR_FILL.getBrightness());
        this.MEdit_fillColor_custom = new CustomMenuItem(this.MEdit_fillColor_slider);
        this.MEdit_fillColor_custom.setHideOnClick(false);
        this.MEdit_fillColor.getItems().addAll(this.MEdit_fillColor_custom);

        this.MEdit_showHistoric = new CheckMenuItem("Show Historic");
        this.MEdit_showHistoric.setSelected(Configuration.visibilityHistoric);

        this.MEdit.getItems().addAll(this.MEdit_fillColor, this.MEdit_showHistoric);

//------CONFIGURATION MENU ITENS------------------------------------------------
        this.MConfiguration = new Menu("Configuration");

        this.MConfiguration_fullScreen = new CheckMenuItem("Full Screen");
        this.MConfiguration_fullScreen.setSelected(Configuration.fullscreen);
        this.MConfiguration_database = new Menu("Access Mode");
        this.MConfiguration_database_accessGroup = new ToggleGroup();
        this.MConfiguration_database_accessTXT = new RadioMenuItem("TXT Access");
        this.MConfiguration_database_accessSQL = new RadioMenuItem("SQL Access");
        this.MConfiguration_database_accessTXT.setToggleGroup(MConfiguration_database_accessGroup);
        this.MConfiguration_database_accessSQL.setToggleGroup(MConfiguration_database_accessGroup);
        this.MConfiguration_database_accessTXT.setSelected(true);
        
        this.MConfiguration_database.getItems().addAll(this.MConfiguration_database_accessTXT, this.MConfiguration_database_accessSQL);
        
        this.MConfiguration.getItems().addAll(this.MConfiguration_database, this.separator, this.MConfiguration_fullScreen);

//------MENU ITENS--------------------------------------------------------------
        this.getMenus().addAll(this.MDominoes, this.MEdit, this.MConfiguration);

//------ADD LISTENERS-----------------------------------------------------------
//----------DOMINOES MENU ITENS-------------------------------------------------
        this.MDominoes_new.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                App.clear();

                /*this.*/MDominoes_new.setDisable(true);
                /*this.*/MDominoes_load.setDisable(false);
                /*this.*/MDominoes_save.setDisable(true);
                /*this.*/MDominoes_exit.setDisable(true);
                /*this.*/MDominoes_exitAndSave.setDisable(true);
                /*this.*/MEdit.setDisable(true);
                /*this.*/MConfiguration.setDisable(true);

            }
        });
        this.MDominoes_load.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                /*this.*/MDominoes_new.setDisable(false);
                /*this.*/MDominoes_load.setDisable(true);
                /*this.*/MDominoes_save.setDisable(false);
                /*this.*/MDominoes_exit.setDisable(false);
                /*this.*/MDominoes_exitAndSave.setDisable(false);
                /*this.*/MEdit.setDisable(false);
                /*this.*/MConfiguration.setDisable(false);

                App.set();
            }
        });

        this.MDominoes_save_saveAll.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    App.saveAll();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

        this.MDominoes_save_autoSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Configuration.autoSave = /*this.*/MDominoes_save_autoSave.isSelected();
                
            }
        });

        this.MDominoes_exitAndSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    App.saveAll();
                    System.exit(0);
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

        this.MDominoes_exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

//----------EDIT MENU ITENS-----------------------------------------------------
        this.MEdit_fillColor_slider.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Dominoes.COLOR_FILL = new Color(/*this.*/MEdit_fillColor_slider.getValue(),
                        /*this.*/ MEdit_fillColor_slider.getValue(),
                        /*this.*/ MEdit_fillColor_slider.getValue(),
                        Dominoes.COLOR_FILL.getOpacity());
                Dominoes.COLOR_LINE = new Color(0.14 * /*this.*/ MEdit_fillColor_slider.getValue(),
                        0.14 * /*this.*/ MEdit_fillColor_slider.getValue(),
                        0.14 * MEdit_fillColor_slider.getValue(),
                        Dominoes.COLOR_FILL.getOpacity()).invert();
                Dominoes.COLOR_NORMAL_FONT = Dominoes.COLOR_FILL.invert();
                App.changeColor();
            }
        });

        this.MEdit_showHistoric.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Configuration.visibilityHistoric = MEdit_showHistoric.isSelected();
                App.setVisibleHistoric(Configuration.visibilityHistoric);
            }
        });

//----------CONFIGURATION MENU ITENS--------------------------------------------
        this.MConfiguration_fullScreen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Configuration.fullscreen = MConfiguration_fullScreen.isSelected();
                App.setFullscreen(Configuration.fullscreen);
            }
        });
    }
}