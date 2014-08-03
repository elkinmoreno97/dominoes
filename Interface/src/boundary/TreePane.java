package boundary;

import domain.Dominoes;
import domain.Historic;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
//import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Color;
import java.awt.Dimension;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.Pane;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

public class TreePane extends Pane {

    VisualizationViewer<Historic, Integer> vv;

    VisualizationServer.Paintable rings;

    String root;

    TreeLayout<Historic, Integer> treeLayout;


    /**
     * the graph
     */
    Forest<Historic, Integer> graph;

    Factory<Integer> edgeFactory = new Factory<Integer>() {
        int i = 0;

        public Integer create() {
            return i++;
        }
    };

    public TreePane(Dominoes domino) {

        
        // create a simple graph for the demo
        graph = new DelegateForest<>();
        graph.addVertex(domino.getHistoric());

        this.createTree(domino.getHistoric());

        treeLayout = new TreeLayout<>(graph);

        vv = new VisualizationViewer<>(treeLayout, new Dimension(600, 600));
        vv.setBackground(Color.white);
        vv.getRenderContext().setVertexFillPaintTransformer(new ConstantTransformer(Color.GRAY));
        vv.getRenderContext().setEdgeFillPaintTransformer(new ConstantTransformer(Color.BLACK));
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        // add a listener for ToolTips
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.WHITE));

        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);

        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

        vv.setGraphMouse(graphMouse);

        SwingNode s = new SwingNode();
        s.setContent(panel);
        this.getChildren().add(s);

    }

    private void createTree(Historic historic) {
    	
        if (historic.getHistoricLeft() == null
                || historic.getHistoricRight() == null ) {
            return;
        }
        
        graph.addEdge(edgeFactory.create(), historic, historic.getHistoricLeft());
        graph.addEdge(edgeFactory.create(), historic, historic.getHistoricRight());

        createTree(historic.getHistoricLeft());
        createTree(historic.getHistoricRight());
    }
}