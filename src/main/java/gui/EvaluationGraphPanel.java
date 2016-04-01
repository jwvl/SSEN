/**
 *
 */
package gui;

import graph.visual.GraphVisualizer;
import gui.evaluation.TableauTable;
import io.tableau.SimpleTableau;

import javax.swing.*;
import java.awt.*;

/**
 * @author jwvl
 * @date Sep 8, 2015
 */
public class EvaluationGraphPanel extends JFrame {

    private GraphVisualizer visualizer;
    private JPanel graphPanel;
    private Dimension dimension;
    private JPanel tableauScrollPane;

    public EvaluationGraphPanel(GraphVisualizer visualizer) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        graphPanel = visualizer.getVisualizationServer();
        tableauScrollPane = new JPanel(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);
        add(tableauScrollPane, BorderLayout.NORTH);
        pack();
        setVisible(true);
    }

    private void setView(JPanel newGraphPanel) {
        remove(graphPanel);
        tableauScrollPane.removeAll();
        add(newGraphPanel, BorderLayout.CENTER);
        this.graphPanel = newGraphPanel;
        pack();
    }

    public void loadNewGraph(GraphVisualizer visualizer) {
        setView(visualizer.getVisualizationServer());
    }

    /**
     * @param tableau
     */
    public void addTableauPanel(SimpleTableau tableau) {
        TableauTable tableauTable = TableauTable.createFromSimpleTableau(tableau);
        tableauScrollPane.add(tableauTable, BorderLayout.CENTER);
        tableauScrollPane.add(tableauTable.getTableHeader(), BorderLayout.NORTH);
        //tableauScrollPane.setSize(graphPanel.getWidth(), 200);
        pack();

    }
}
