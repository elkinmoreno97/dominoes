package boundary;

import domain.Configuration;
import domain.Dominoes;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private static ListViewDominoes list;
    private static AreaMove area;
    private static DominoesMenuBar menu;
    private static Visual visual;

    private static Scene scene;
    private static Stage stage;

    private static SplitPane splitPane;
            
    private static double width = Configuration.width;
    private static double height = Configuration.height;

    /**
     *
     */
    public static void start() {
        launch((String[]) null);
    }

    static void drawGraph(Dominoes domino) {
        visual.addTabGraph(domino);
    }

    static void drawMatrix(Dominoes domino) {
        visual.addTabMatrix(domino);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            App.stage = primaryStage;
            App.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            App.stage.centerOnScreen();

            App.menu = new DominoesMenuBar();

            App.set();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * This function is used to exit this program
     *
     * @param status status required by the operating system
     */
    private static void exit(int status) {
        System.exit(status);
    }

    /**
     * This function is called to change the parts color
     */
    static void changeColor() {
        App.list.changeColor();
        App.area.changeColor();
    }

    /**
     * This function remove all parts in this area move
     */
    public static void clear() {
        App.list.clear();
        App.area.clear();
    }

    /**
     * This function adds in List a matrix specified
     *
     * @param dominoes the matrix to be added
     */
    public static void CopyToList(Dominoes dominoes) {
        App.list.add(dominoes);
    }

    /**
     * This function adds in Area a matrix specified
     *
     * @param dominoes the matrix to be added
     */
    public static void copyToArea(Dominoes dominoes) {
        App.area.add(dominoes);
    }

    /**
     * This function remove the element of the list and of the move area
     *
     * @param dominoes Element to remove
     * @param group Element to remove
     * @return true, in affirmative case
     * @throws IOException
     */
    public static boolean removeMatrix(Dominoes dominoes, Group group) throws IOException {
        boolean result = control.Controller.removeMatrix(dominoes);

        // if not removed both, then we have which resultMultiplication
        if (App.area.remove(group)) {
            if (App.list.remove(group)) {
                if (result) {
                    return true;
                } else {
                    App.area.add(dominoes);
                    App.list.add(dominoes);
                    return false;
                }
            } else {
                App.area.add(dominoes);
                return false;
            }
        }
        return false;

    }

    /**
     * This functions is called when user want save each alteration
     *
     * @throws IOException
     */
    public static void saveAll() throws IOException {
        App.area.saveAllAndSendToList();
    }

    /**
     * Set the basic configuration of this Application
     */
    public static void set() {
        App.stage.setTitle("Dominoes Interface");
        App.stage.setResizable(Configuration.resizable);

        App.list = new ListViewDominoes();
        App.visual = new Visual();
        App.area = new AreaMove();
        
        App.scene = new Scene(new Group());
        VBox back = new VBox(2);
        splitPane = new SplitPane();
//        HBox splitPane = new HBox(3);
        
        App.scene.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                App.scene.setCursor(Cursor.DEFAULT);
            }
        });

        splitPane.getItems().add(App.list);
        splitPane.getItems().add(App.area);
        splitPane.getItems().add(App.visual);
        
        back.getChildren().add(menu);
        back.getChildren().add(splitPane);
        
        App.scene.setRoot(back);
        App.stage.setScene(App.scene);
        App.stage.show();
        App.setFullscreen(Configuration.fullscreen);
    }

    /**
     * This function is used to define the visibility of historic
     *
     * @param visibility True to define visible the historic
     */
    public static void setVisibleHistoric(boolean visibility) {
        area.setVisibleHistoric(visibility);
        list.setVisibleHistoric(visibility);
    }

    /**
     * This function is used to make full screen in this Application
     *
     * @param fullscreen
     */
    static void setFullscreen(boolean fullscreen) {
        double padding = menu.getHeight();
        App.stage.setFullScreen(fullscreen);
        if (!fullscreen) {
            padding += 30;
            
            App.stage.setWidth(Configuration.width);
            App.stage.setHeight(Configuration.height);
            App.stage.centerOnScreen();
        }

        App.width = App.stage.getWidth();
        App.height = App.stage.getHeight();
        
        App.list.setSize(Configuration.listWidth , App.height - padding);
        App.visual.setSize(App.width, App.height - padding);
        App.area.setSize(App.width, App.height - padding);

    }
}