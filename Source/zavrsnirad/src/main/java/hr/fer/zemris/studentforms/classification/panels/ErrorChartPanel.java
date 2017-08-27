package hr.fer.zemris.studentforms.classification.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Panel for plotting error of a neural network on training and evaulation set.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 20.5.2017.
 */
public class ErrorChartPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Error series on training set.
     */
    private XYSeries trainingErrorSeries;
    /**
     * Error series on evaluation set.
     */
    private XYSeries evaluationErrorSeries;
    /**
     * Error dataset.
     */
    private XYSeriesCollection dataset;

    /**
     * JFreeChart component.
     */
    private JFreeChart chart;

    /**
     * Current number of iteration.
     */
    private int currIteration = 0;

    /**
     * Constructor initializes char panel.
     */
    public ErrorChartPanel() {
        super();
        this.trainingErrorSeries = new XYSeries("Training set error");
        this.evaluationErrorSeries = new XYSeries("Evaluation set error");
        dataset = new XYSeriesCollection();
        dataset.addSeries(trainingErrorSeries);
        dataset.addSeries(evaluationErrorSeries);
        chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

    }

    /**
     * Method adds training value to the chart.
     *
     * @param trainingErrorValue
     *            error value on training set
     * @param evaluationErrorValue
     *            error value on evaluation set
     */
    public synchronized void addValue(double trainingErrorValue, double evaluationErrorValue, int epoch) {
        this.trainingErrorSeries.add(epoch, trainingErrorValue);
        this.evaluationErrorSeries.add(epoch, evaluationErrorValue);
        currIteration++;
        System.out.println(epoch + "\t" + trainingErrorValue);
    }

    /**
     * Method clears error chart graph.
     */
    public void clearGraph() {
        currIteration = 0;
        this.trainingErrorSeries.clear();
        this.evaluationErrorSeries.clear();

    }

    /**
     * Method creates JFreeChart in the panel and initializes it with title, axis names and data series.
     *
     * @param dataset
     *            dataset to display
     * @return JFreeChar component
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final int maxX = 200;
        final int minX = 0;
        final JFreeChart result = ChartFactory.createXYLineChart("Training progress", "Epochs", "Error value", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        final XYPlot plot = result.getXYPlot();

        result.setBackgroundPaint(getBackground());

        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);

        axis.setRange(minX, maxX);
        axis.setAutoRange(true);
        axis = plot.getRangeAxis();
        axis.setRange(0, 1.0);

        return result;
    }
}
