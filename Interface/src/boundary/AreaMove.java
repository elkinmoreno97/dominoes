package boundary;

import domain.Configuration;
import domain.Dominoes;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.FillTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AreaMove extends Pane {

    private ArrayList<Dominoes> dominoes;
    private ArrayList<Group> pieces;
    private final Rectangle background;

    private int indexFirstOperatorMultiplication = -1;
    private int indexSecondOperatorMultiplication = -1;

    private double srcSceneX;
    private double srcSceneY;
    private double srcTranslateX;
    private double srcTranslateY;

    private double padding = Configuration.width;

    /**
     * Class builder with the dimension defined in parameters. here, will create
     * too a background with white color
     *
     */
    public AreaMove() {
        super();
        this.background = new Rectangle();
        this.background.setFill(new Color(1, 1, 1, 1));

        this.getChildren().addAll(background);

        this.dominoes = new ArrayList<>();
        this.pieces = new ArrayList<>();
    }

    /**
     * Add a new Domino in Area Move in position x = 0, y = 0
     *
     * @param domino The Domino information
     */
    public void add(Dominoes domino) {
        this.add(domino, 0, 0);

    }

    /**
     * Add a new Domino in Area Move in position defined for parameters
     *
     * @param domino The Domino information
     * @param x The coordinate X of this new Domino
     * @param y The coordinate Y of this new Domino
     */
    public void add(Dominoes domino, double x, double y) {
        double thisTranslateX = x;
        double thisTranslateY = y;

        ContextMenu minimenu = new ContextMenu();

        MenuItem menuItemTranspose = new MenuItem("Transpose");
        MenuItem menuItemMultiply = new MenuItem("Multiply");
        menuItemMultiply.setDisable(true);
        MenuItem menuItemSaveInList = new MenuItem("Save");
        MenuItem menuItemViewGraph = new MenuItem("View Graph");
        MenuItem menuItemViewMatrix = new MenuItem("View Matrix");
        MenuItem menuItemClose = new MenuItem("Close");

        Group group = domino.drawDominoes();
        group.getChildren().get(Dominoes.GRAPH_HISTORIC).setVisible(Configuration.visibilityHistoric);

        group.setTranslateX(thisTranslateX);
        group.setTranslateY(thisTranslateY);
        if (!this.pieces.isEmpty()) {
            for (Group g : this.pieces) {

                if (g.getTranslateY() + Dominoes.GRAPH_HEIGHT >= background.prefHeight(-1)) {
                    thisTranslateX += Dominoes.GRAPH_WIDTH;
                    thisTranslateY = background.getY();
                    continue;
                }

                if ((thisTranslateX >= g.getTranslateX()
                        && thisTranslateX < g.getTranslateX() + Dominoes.GRAPH_WIDTH
                        && thisTranslateY >= g.getTranslateY()
                        && thisTranslateY < g.getTranslateY() + Dominoes.GRAPH_HEIGHT)
                        || (thisTranslateX + Dominoes.GRAPH_WIDTH >= g.getTranslateX()
                        && thisTranslateX + Dominoes.GRAPH_WIDTH < g.getTranslateX() + Dominoes.GRAPH_WIDTH
                        && thisTranslateY >= g.getTranslateY()
                        && thisTranslateY < g.getTranslateY() + Dominoes.GRAPH_HEIGHT)
                        || (thisTranslateX >= g.getTranslateX()
                        && thisTranslateX < g.getTranslateX() + Dominoes.GRAPH_WIDTH
                        && thisTranslateY + Dominoes.GRAPH_HEIGHT >= g.getTranslateY()
                        && thisTranslateY + Dominoes.GRAPH_HEIGHT < g.getTranslateY() + Dominoes.GRAPH_HEIGHT)
                        || (thisTranslateX + Dominoes.GRAPH_WIDTH >= g.getTranslateX()
                        && thisTranslateX + Dominoes.GRAPH_WIDTH < g.getTranslateX() + Dominoes.GRAPH_WIDTH
                        && thisTranslateY + Dominoes.GRAPH_HEIGHT >= g.getTranslateY()
                        && thisTranslateY + Dominoes.GRAPH_HEIGHT < g.getTranslateY() + Dominoes.GRAPH_HEIGHT)) {

                    thisTranslateY = g.getTranslateY() + Dominoes.GRAPH_HEIGHT;
                }
            }
            group.setTranslateY(thisTranslateY);
            group.setTranslateX(thisTranslateX);
        }

        this.pieces.add(group);
        this.dominoes.add(domino);
        this.getChildren().add(group);

        if (!domino.getIdRow().equals(domino.getIdCol())) {
            menuItemViewGraph.setDisable(true);
        }

        group.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                cursorProperty().set(Cursor.OPEN_HAND);
            }
        });
        group.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                double offsetX = event.getSceneX() - srcSceneX;
                double offsetY = event.getSceneY() - srcSceneY;
                double newTranslateX = srcTranslateX + offsetX;
                double newTranslateY = srcTranslateY + offsetY;

                // detect move out
                boolean detecMoveOutX = false;
                boolean detecMoveOutY = false;
                if (newTranslateX < background.getX()) {
                    ((Group) (event.getSource())).setTranslateX(background.getX());

                    detecMoveOutX = true;
                }
                if (newTranslateY < background.getY()) {
                    ((Group) (event.getSource())).setTranslateY(background.getY());
                    detecMoveOutY = true;
                }
                if (newTranslateX + ((Group) (event.getSource())).prefWidth(-1) > background.getX() + background.getWidth()) {
                    ((Group) (event.getSource())).setTranslateX(background.getX() + background.getWidth() - ((Group) (event.getSource())).prefWidth(-1));
                    detecMoveOutX = true;
                }
                if (newTranslateY + ((Group) (event.getSource())).prefHeight(-1) > background.getY() + background.getHeight()) {
                    ((Group) (event.getSource())).setTranslateY(background.getY() + background.getHeight() - ((Group) (event.getSource())).prefHeight(-1));
                    detecMoveOutY = true;
                }

                if (!detecMoveOutX) {
                    ((Group) (event.getSource())).setTranslateX(newTranslateX);
                }
                if (!detecMoveOutY) {
                    ((Group) (event.getSource())).setTranslateY(newTranslateY);
                }

                // detect multiplication
                int index = pieces.indexOf(group);

                for (int j = 0; j < pieces.size(); j++) {

                    if (index != j && detectMultiplication(index, j)) {
                        menuItemMultiply.setDisable(false);

                        break;
                    } else {
                        menuItemMultiply.setDisable(true);
                    }
                }
            }
        });
        group.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                srcSceneX = event.getSceneX();
                srcSceneY = event.getSceneY();
                srcTranslateX = ((Group) (event.getSource())).getTranslateX();
                srcTranslateY = ((Group) (event.getSource())).getTranslateY();

                group.toFront();
                cursorProperty().set(Cursor.CLOSED_HAND);
            }
        });
        group.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                cursorProperty().set(Cursor.OPEN_HAND);
                try {
                    //                System.out.println("multiplying");
                    multiply();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        group.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                cursorProperty().set(Cursor.DEFAULT);
            }
        });
        group.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            //                        System.out.println("transposing");
                            transpose(group);
                        } catch (IOException ex) {
                            System.err.println(ex.getMessage());
                        }
                    }
                }
            }
        });
        group.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    minimenu.show(group, e.getScreenX(), e.getScreenY());
                } else {
                    minimenu.hide();
                }
            }
        });
        minimenu.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    event.consume();
                }
            }
        });
        minimenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // choise menu item multiply
                if (((MenuItem) event.getTarget()).getText().equals(menuItemTranspose.getText())) {
                    try {
                        //                    System.out.println("transposing");
                        transpose(group);
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                } else if (((MenuItem) event.getTarget()).getText().equals(menuItemMultiply.getText())) {
                    try {
                        System.out.println("multiplying");
                        multiply();
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }

                } else if (((MenuItem) event.getTarget()).getText().equals(menuItemSaveInList.getText())) {
                    System.out.println("saving");
                    try {
                        saveAndSendToList(group);
                        close(group);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (((MenuItem) event.getTarget()).getText().equals(menuItemViewGraph.getText())) {
                    drawGraph(domino);
                } else if (((MenuItem) event.getTarget()).getText().equals(menuItemViewMatrix.getText())) {
                    drawMatrix(domino);
                } else if (((MenuItem) event.getTarget()).getText().equals(menuItemClose.getText())) {
//                    System.out.println("closing");
                    closePiece(group);
                }
            }
        });

        minimenu.getItems().addAll(menuItemTranspose, menuItemMultiply, menuItemSaveInList, menuItemViewGraph, menuItemViewMatrix, menuItemClose);

    }

    /**
     * This function is called to change the parts color
     */
    void changeColor() {
        for (Group group : this.pieces) {
            ((Shape) group.getChildren().get(Dominoes.GRAPH_FILL)).setFill(Dominoes.COLOR_FILL);
            ((Shape) group.getChildren().get(Dominoes.GRAPH_LINE)).setFill(Dominoes.COLOR_LINE);
            ((Shape) group.getChildren().get(Dominoes.GRAPH_BORDER)).setFill(Dominoes.COLOR_LINE);
            ((Shape) group.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_NORMAL_FONT);
            ((Shape) group.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_NORMAL_FONT);
        }
    }

    /**
     * This function remove all parts in this area move
     */
    public void clear() {
        for (int i = 0; i < this.pieces.size(); i++) {
            this.pieces.get(i).setVisible(false);
        }
        this.pieces.removeAll(this.pieces);
        this.dominoes.removeAll(this.dominoes);

        this.pieces = null;
        this.dominoes = null;
    }

    /**
     * This function is used to remove a element of this Area Move
     *
     * @param group A specified element
     */
    private void close(Group group) {
        // removing in area move
        this.remove(group);
    }

    /**
     * Just close the piece defined in the parameter
     *
     * @param group The piece to will be removed
     */
    private void closePiece(Group group) {
        remove(group);
    }

    /**
     * To detect a multiplication will be used the interception between the
     * pieces, detecting by left or detecting by right has different
     * significates. All detectiong are ever in relation to index1 (left ou
     * right)
     *
     * @param index1 - piece index one
     * @param index2 - piece index two
     */
    private boolean detectMultiplication(int index1, int index2) {

        Group g1 = this.pieces.get(index1);
        Group g2 = this.pieces.get(index2);
        Dominoes d1 = this.dominoes.get(index1);
        Dominoes d2 = this.dominoes.get(index2);

        int paddingToCoupling = 1;

        boolean detect = false;

        if ((g1.getTranslateX() >= g2.getTranslateX() + Dominoes.GRAPH_WIDTH / 2
                && g1.getTranslateX() <= g2.getTranslateX() + Dominoes.GRAPH_WIDTH)
                && (g1.getTranslateY() >= g2.getTranslateY()
                && g1.getTranslateY() <= g2.getTranslateY() + Dominoes.GRAPH_HEIGHT)) {

            if (((Text) g1.getChildren().get(Dominoes.GRAPH_ID_ROW)).getText().equals(((Text) g2.getChildren().get(Dominoes.GRAPH_ID_COL)).getText())
                    && d1.getHeight() == d2.getWidth()) {

                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_OPERATE_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_OPERATE_FONT);

                g1.setTranslateX(g2.getTranslateX() + Dominoes.GRAPH_WIDTH - paddingToCoupling);
                g1.setTranslateY(g2.getTranslateY());

                this.indexFirstOperatorMultiplication = index2;
                this.indexSecondOperatorMultiplication = index1;

                return true;
            } else {
                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);
                detect = true;
            }

        } else if ((g1.getTranslateX() >= g2.getTranslateX() + Dominoes.GRAPH_WIDTH / 2
                && g1.getTranslateX() <= g2.getTranslateX() + Dominoes.GRAPH_WIDTH)
                && (g1.getTranslateY() + Dominoes.GRAPH_HEIGHT >= g2.getTranslateY()
                && g1.getTranslateY() + Dominoes.GRAPH_HEIGHT <= g2.getTranslateY()
                + Dominoes.GRAPH_HEIGHT)) {

            if (((Text) g1.getChildren().get(Dominoes.GRAPH_ID_ROW)).getText().equals(((Text) g2.getChildren().get(Dominoes.GRAPH_ID_COL)).getText())
                    && d1.getHeight() == d2.getWidth()) {

                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_OPERATE_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_OPERATE_FONT);

                g1.setTranslateX(g2.getTranslateX() + Dominoes.GRAPH_WIDTH - paddingToCoupling);
                g1.setTranslateY(g2.getTranslateY());

                this.indexFirstOperatorMultiplication = index2;
                this.indexSecondOperatorMultiplication = index1;

                return true;

            } else {
                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);
                detect = true;
            }

        } else if ((g1.getTranslateX() + Dominoes.GRAPH_WIDTH >= g2.getTranslateX()
                && g1.getTranslateX() + Dominoes.GRAPH_WIDTH <= g2.getTranslateX() + Dominoes.GRAPH_WIDTH / 2)
                && (g1.getTranslateY() >= g2.getTranslateY()
                && g1.getTranslateY() <= g2.getTranslateY() + Dominoes.GRAPH_HEIGHT)) {

            if (((Text) g1.getChildren().get(Dominoes.GRAPH_ID_COL)).getText().equals(((Text) g2.getChildren().get(Dominoes.GRAPH_ID_ROW)).getText())
                    && d1.getWidth() == d2.getHeight()) {

                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_OPERATE_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_OPERATE_FONT);

                g1.setTranslateX(g2.getTranslateX() - Dominoes.GRAPH_WIDTH + paddingToCoupling);
                g1.setTranslateY(g2.getTranslateY());

                this.indexFirstOperatorMultiplication = index1;
                this.indexSecondOperatorMultiplication = index2;

                return true;
            } else {
                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);
                detect = true;
            }

        } else if ((g1.getTranslateX() + Dominoes.GRAPH_WIDTH >= g2.getTranslateX()
                && g1.getTranslateX() + Dominoes.GRAPH_WIDTH <= g2.getTranslateX() + Dominoes.GRAPH_WIDTH / 2)
                && (g1.getTranslateY() + Dominoes.GRAPH_HEIGHT >= g2.getTranslateY()
                && g1.getTranslateY() + Dominoes.GRAPH_HEIGHT <= g2.getTranslateY() + Dominoes.GRAPH_HEIGHT)) {

            if (((Text) g1.getChildren().get(Dominoes.GRAPH_ID_COL)).getText().equals(((Text) g2.getChildren().get(Dominoes.GRAPH_ID_ROW)).getText())
                    && d1.getWidth() == d2.getHeight()) {

                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_OPERATE_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_OPERATE_FONT);

                g1.setTranslateX(g2.getTranslateX() - Dominoes.GRAPH_WIDTH + paddingToCoupling);
                g1.setTranslateY(g2.getTranslateY());

                this.indexFirstOperatorMultiplication = index1;
                this.indexSecondOperatorMultiplication = index2;

                return true;
            } else {
                ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);
                ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_NO_OPERATION_FONT);

                detect = true;
            }
        }

        if (!detect) {
            ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_NORMAL_FONT);
            ((Text) g1.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_NORMAL_FONT);
            ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_ROW)).setFill(Dominoes.COLOR_NORMAL_FONT);
            ((Text) g2.getChildren().get(Dominoes.GRAPH_ID_COL)).setFill(Dominoes.COLOR_NORMAL_FONT);

            this.indexFirstOperatorMultiplication = -1;
            this.indexSecondOperatorMultiplication = -1;
        }

        return false;
    }

    /**
     * This will make a multiplication
     */
    private void multiply() throws IOException {

        if (this.indexFirstOperatorMultiplication != -1 && this.indexSecondOperatorMultiplication != -1) {

            Dominoes d1 = this.dominoes.get(this.indexFirstOperatorMultiplication);
            Dominoes d2 = this.dominoes.get(this.indexSecondOperatorMultiplication);

            if (d1.getIdCol().equals(d2.getIdRow())) {

                Dominoes resultOperation = control.Controller.MultiplyMatrices(d1, d2);

                double x = (this.pieces.get(this.dominoes.indexOf(d1)).getTranslateX()
                        + this.pieces.get(this.dominoes.indexOf(d2)).getTranslateX()) / 2;

                double y = (this.pieces.get(this.dominoes.indexOf(d1)).getTranslateY()
                        + this.pieces.get(this.dominoes.indexOf(d2)).getTranslateY()) / 2;

                if (this.remove(this.indexFirstOperatorMultiplication)
                        && this.indexSecondOperatorMultiplication > this.indexFirstOperatorMultiplication) {
                    this.remove(this.indexSecondOperatorMultiplication - 1);
                } else {
                    this.remove(this.indexSecondOperatorMultiplication);
                }

                this.add(resultOperation, x, y);
                if (Configuration.autoSave) {
                    this.saveAndSendToList(pieces.get(dominoes.indexOf(resultOperation)));
                }
            }
            this.indexFirstOperatorMultiplication = -1;
            this.indexSecondOperatorMultiplication = -1;
        }

    }

    /**
     * This function remove the matrix, in the piece and dominoes array, by the
     * element
     *
     * @param group the element to remove
     * @return True in affirmative case
     */
    public boolean remove(Group group) {
        int index = -1;
        index = this.pieces.indexOf(group);
        return remove(index);

    }

    /**
     * This function remove the matrix, in the piece and dominoes array, by the
     * index
     *
     * @param index the index to remove
     * @return True in affirmative case
     */
    public boolean remove(int index) {
        if (index > -1) {
            this.pieces.get(index).setVisible(false);
            this.dominoes.remove(index);
            this.pieces.remove(index);
        }
        return true;
    }

    /**
     * This function save all piece in AreaMove, remove and create a new matrix
     * in the List
     *
     * @throws IOException
     */
    public void saveAllAndSendToList() throws IOException {
        for (int i = 0; i < this.dominoes.size(); i++) {

            control.Controller.saveMatrix(this.dominoes.get(i));

            // adding in list
            App.CopyToList(this.dominoes.get(i));

            this.pieces.get(i).setVisible(false);
        }
        this.dominoes.removeAll(this.dominoes);
        this.pieces.removeAll(this.pieces);

    }

    /**
     * This function save, remove and create a new matrix in the List
     *
     * @param group The matrix which will suffer with this operation
     * @throws IOException
     */
    private void saveAndSendToList(Group group) throws IOException {
        control.Controller.saveMatrix(this.dominoes.get(this.pieces.indexOf(group)));

        // adding in list
        App.CopyToList(this.dominoes.get(this.pieces.indexOf(group)));
    }

    /**
     * This Functions is used to define the moving area size
     *
     * @param width
     * @param height
     */
    public void setSize(double width, double height) {

        this.background.setWidth(width + padding);
        this.background.setHeight(height);

        this.setMinWidth(width - padding);
        this.setPrefWidth(width);
        this.setMaxWidth(width + padding);
        this.setPrefHeight(height);
    }

    /**
     * This function is used to define the visibility of historic
     *
     * @param visibility True to define visible the historic
     */
    void setVisibleHistoric(boolean visibility) {
        for (Group group : pieces) {
            group.getChildren().get(Dominoes.GRAPH_HISTORIC).setVisible(visibility);
        }
        Configuration.visibilityHistoric = visibility;
    }

    /**
     * This function only maked a simple animation to tranpose a matrix
     *
     * @param piece The piece to animate
     */
    private void transpose(Group piece) throws IOException {

        int duration = 500;

        RotateTransition rt = new RotateTransition(Duration.millis(duration));
        rt.setFromAngle(piece.getRotate());
        rt.setToAngle(piece.getRotate() + 180);

        RotateTransition rt1 = new RotateTransition(Duration.millis(duration));
        rt1.setFromAngle(piece.getRotate());
        rt1.setToAngle(piece.getRotate() - 180);

        RotateTransition rt2 = new RotateTransition(Duration.millis(duration));
        rt2.setFromAngle(piece.getRotate());
        rt2.setToAngle(piece.getRotate() - 180);

        FillTransition ft3 = new FillTransition(Duration.millis(duration / 3));
        ft3.setFromValue(Dominoes.COLOR_HISTORIC);
        ft3.setToValue(Dominoes.COLOR_INIVISIBLE);

        RotateTransition rt4 = new RotateTransition(Duration.millis(duration / 3));
        rt4.setFromAngle(piece.getRotate());
        rt4.setToAngle(piece.getRotate() - 180);

        FillTransition ft5 = new FillTransition(Duration.millis(duration / 3));
        ft5.setFromValue(Dominoes.COLOR_INIVISIBLE);
        ft5.setToValue(Dominoes.COLOR_HISTORIC);

        RotateTransition rt5 = new RotateTransition(Duration.millis(duration));
        rt5.setFromAngle(piece.getRotate());
        rt5.setToAngle(piece.getRotate() - 180);

        new SequentialTransition(piece, rt).play();
        new SequentialTransition(piece.getChildren().get(Dominoes.GRAPH_ID_ROW), rt1).play();
        new SequentialTransition(piece.getChildren().get(Dominoes.GRAPH_ID_COL), rt2).play();
        new SequentialTransition(piece.getChildren().get(Dominoes.GRAPH_HISTORIC), ft3, rt4, ft5).play();
        new SequentialTransition(piece.getChildren().get(Dominoes.GRAPH_TYPE), rt5).play();

        // change the position of letters
        Text swap = (Text) piece.getChildren().remove(Dominoes.GRAPH_ID_ROW);
        piece.getChildren().add(swap);

        String historicGraph = ((Text) piece.getChildren().get(Dominoes.GRAPH_HISTORIC)).getText();
        String[] auxHistoric = historicGraph.split(",");

        historicGraph = "";

        for (int i = auxHistoric.length - 1; i >= 0; i--) {
            historicGraph += auxHistoric[i];
            if (i > 0) {
                historicGraph += ",";
            }
        }
        Dominoes domino = control.Controller.tranposeDominoes(this.dominoes.get(this.pieces.indexOf(piece)));

        ((Text) piece.getChildren().get(Dominoes.GRAPH_HISTORIC)).setText(historicGraph);

        String type = "";
        switch (domino.getType()) {
            case Dominoes.TYPE_DERIVED:
                type = Dominoes.TYPE_DERIVED_CODE;
                break;
            case Dominoes.TYPE_SUPPORT:
                type = Dominoes.TYPE_SUPPORT_CODE;
                break;
        }
        ((Text) ((Group) piece.getChildren().get(Dominoes.GRAPH_TYPE)).getChildren().get(1)).setText(type);

        if (Configuration.autoSave) {
            this.saveAndSendToList(piece);
        }

        // remove
        this.dominoes.set(this.pieces.indexOf(piece), domino);

    }

    private void drawGraph(Dominoes domino) {
        App.drawGraph(domino);
    }

    private void drawMatrix(Dominoes domino) {
        App.drawMatrix(domino);
    }
}