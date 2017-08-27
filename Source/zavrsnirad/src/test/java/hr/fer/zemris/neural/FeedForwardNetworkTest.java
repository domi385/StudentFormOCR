package hr.fer.zemris.neural;

import hr.fer.zemris.TestUtility;
import hr.fer.zemris.neural.trainers.ClassicBackPropagationTrainer;
import hr.fer.zemris.neural.trainers.INeuralNetworkTrainer;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FeedForwardNetworkTest {

    @Test
    public void fourLayersNetworkOutput() {
        FeedForwardNetwork network = initilizeNetworkFourLayersTests();
        network.calcOutput(new double[] { 1., 2., 3. });
        double[] netoutput = network.getNetworkOutput();
        // network output
        Assert.assertEquals(3, netoutput.length);
        Assert.assertTrue(TestUtility.compareDoubles(0.9592239303, netoutput[0]));
        Assert.assertTrue(TestUtility.compareDoubles(0.9857648611, netoutput[1]));
        Assert.assertTrue(TestUtility.compareDoubles(0.9940161152, netoutput[2]));

        Assert.assertTrue(TestUtility.compareDoubles(1, network.getNeuronOutput(0, 0)));
        Assert.assertTrue(TestUtility.compareDoubles(2, network.getNeuronOutput(0, 1)));
        Assert.assertTrue(TestUtility.compareDoubles(3, network.getNeuronOutput(0, 2)));

        Assert.assertTrue(TestUtility.compareDoubles(0.9939401985, network.getNeuronOutput(1, 0)));
        Assert.assertTrue(TestUtility.compareDoubles(0.9989932292, network.getNeuronOutput(1, 1)));
        Assert.assertTrue(TestUtility.compareDoubles(0.9997965730, network.getNeuronOutput(1, 2)));

        Assert.assertTrue(TestUtility.compareDoubles(0.9167224828, network.getNeuronOutput(2, 0)));
        Assert.assertTrue(TestUtility.compareDoubles(0.9643511112, network.getNeuronOutput(2, 1)));
        Assert.assertTrue(TestUtility.compareDoubles(0.9878318199, network.getNeuronOutput(2, 2)));

        Assert.assertTrue(TestUtility.compareDoubles(0.9592239303, network.getNeuronOutput(3, 0)));
        Assert.assertTrue(TestUtility.compareDoubles(0.9857648611, network.getNeuronOutput(3, 1)));
        Assert.assertTrue(TestUtility.compareDoubles(0.9940161152, network.getNeuronOutput(3, 2)));

    }

    @Test
    public void fourLayersNetworkTest() {
        FeedForwardNetwork network = initilizeNetworkFourLayersTests();
    }

    @Test
    public void fourLayersNetworkWeightsTest() {
        FeedForwardNetwork network = initilizeNetworkFourLayersTests();

        // FIRST LAYER

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 0, 0), 0.1));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 0, 1), 0.2));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 0, 2), 0.3));

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 1, 0), 0.5));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 1, 1), 0.7));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 1, 2), 1.1));

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 2, 0), 1.3));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 2, 1), 1.7));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(0, 2, 2), 1.9));

        // SECOND LAYER

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 0, 0), 0.1));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 0, 1), 0.2));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 0, 2), 0.3));

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 1, 0), 0.5));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 1, 1), 0.7));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 1, 2), 1.1));

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 2, 0), 1.3));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 2, 1), 1.7));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(1, 2, 2), 1.9));
        // THIRD LAYER

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 0, 0), 0.1));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 0, 1), 0.2));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 0, 2), 0.3));

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 1, 0), 0.5));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 1, 1), 0.7));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 1, 2), 1.1));

        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 2, 0), 1.3));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 2, 1), 1.7));
        Assert.assertTrue(TestUtility.compareDoubles(network.getConnectionWeight(2, 2, 2), 1.9));
    }

    private FeedForwardNetwork initializeThreeLayerNetwork() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(2);
        network.addLayer(2);
        network.addLayer(2);

        // first layer
        network.setWeight(0, 0.15);
        network.setWeight(1, 0.25);
        network.setWeight(2, 0.2);
        network.setWeight(3, 0.3);
        network.setThresholdWeight(2, 0.35);
        network.setThresholdWeight(3, 0.35);

        // second layer
        network.setWeight(4, 0.4);
        network.setWeight(5, 0.50);
        network.setWeight(6, 0.45);
        network.setWeight(7, 0.55);
        network.setThresholdWeight(4, 0.6);
        network.setThresholdWeight(5, 0.6);
        return network;
    }

    private FeedForwardNetwork initilizeNetworkFourLayersTests() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(3);
        network.addLayer(3);
        network.addLayer(3);

        // first layer
        network.setWeight(0, 0.1);
        network.setWeight(1, 0.2);
        network.setWeight(2, 0.3);
        network.setWeight(3, 0.5);
        network.setWeight(4, 0.7);
        network.setWeight(5, 1.1);
        network.setWeight(6, 1.3);
        network.setWeight(7, 1.7);
        network.setWeight(8, 1.9);
        network.setThresholdWeight(3, 0.1);
        network.setThresholdWeight(4, 0.2);
        network.setThresholdWeight(5, 0.3);

        // second layer
        network.setWeight(9, 0.1);
        network.setWeight(10, 0.2);
        network.setWeight(11, 0.3);
        network.setWeight(12, 0.5);
        network.setWeight(13, 0.7);
        network.setWeight(14, 1.1);
        network.setWeight(15, 1.3);
        network.setWeight(16, 1.7);
        network.setWeight(17, 1.9);
        network.setThresholdWeight(6, 0.5);
        network.setThresholdWeight(7, 0.7);
        network.setThresholdWeight(8, 1.1);

        // third layer
        network.setWeight(18, 0.1);
        network.setWeight(19, 0.2);
        network.setWeight(20, 0.3);
        network.setWeight(21, 0.5);
        network.setWeight(22, 0.7);
        network.setWeight(23, 1.1);
        network.setWeight(24, 1.3);
        network.setWeight(25, 1.7);
        network.setWeight(26, 1.9);
        network.setThresholdWeight(9, 1.3);
        network.setThresholdWeight(10, 1.7);
        network.setThresholdWeight(11, 1.9);

        return network;

    }

    @Test
    public void outputLayerErrorFourLayers() {
        FeedForwardNetwork network = initilizeNetworkFourLayersTests();
        network.calcOutput(new double[] { 1., 2., 3. });
        network.calcError(new double[] { 0.1, 0.2, 0.3 });
        // TODO
    }

    @Test
    public void singleLayerNeuralNetworkIdentityOutput() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(2);
        network.calcOutput(new double[] { 1, 2 });
        double[] outputs = network.getNetworkOutput();
        Assert.assertTrue(outputs.length == 2);
        Assert.assertTrue(Math.abs(1 - outputs[0]) < 1e-9);
        Assert.assertTrue(Math.abs(2 - outputs[1]) < 1e-9);
    }

    @Test
    public void testGetWeightThreeLayers() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(2);
        network.addLayer(2);

        network.setWeight(6, 1);
        network.setWeight(7, 2);
        network.setWeight(8, 3);
        network.setWeight(9, 4);

        Assert.assertTrue(Math.abs(network.getConnectionWeight(1, 0, 0) - 1) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(1, 0, 1) - 2) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(1, 1, 0) - 3) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(1, 1, 1) - 4) < 1e-9);
    }

    @Test
    public void testGetWeightTwoLayers() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(2);
        network.setWeight(0, 1);
        network.setWeight(1, 2);
        network.setWeight(2, 3);
        network.setWeight(3, 4);
        network.setWeight(4, 5);
        network.setWeight(5, 6);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(0, 0, 0) - 1) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(0, 0, 1) - 2) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(0, 1, 0) - 3) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(0, 1, 1) - 4) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(0, 2, 0) - 5) < 1e-9);
        Assert.assertTrue(Math.abs(network.getConnectionWeight(0, 2, 1) - 6) < 1e-9);
    }

    @Test
    public void testOutputErrorHiddenLayer() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(2);
        double firstNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 1 + 2 * 3 + 3 * 5)));
        double secondNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 2 + 2 * 4 + 3 * 6)));
        network.addLayer(2);

        double thirdNeuronExpectedOutput = 1. / (1 + Math
                .exp(-(firstNeuronExpectedOutput * 7 + secondNeuronExpectedOutput * 9)));
        double fourthNeuronExpectedOutput = 1. / (1 + Math
                .exp(-(firstNeuronExpectedOutput * 8 + secondNeuronExpectedOutput * 10)));

        network.setWeight(0, 1);
        network.setWeight(1, 2);
        network.setWeight(2, 3);
        network.setWeight(3, 4);
        network.setWeight(4, 5);
        network.setWeight(5, 6);
        network.setWeight(6, 7);
        network.setWeight(7, 8);
        network.setWeight(8, 9);
        network.setWeight(9, 10);

        network.calcOutput(new double[] { 1, 2, 3 });

        Assert.assertTrue(Math.abs(network.getNeuronOutput(1, 0) - firstNeuronExpectedOutput) < 1e-11);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(1, 1) - secondNeuronExpectedOutput) < 1e-11);

        Assert.assertTrue(Math.abs(network.getNeuronOutput(2, 0) - thirdNeuronExpectedOutput) < 1e-9);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(2, 1) - fourthNeuronExpectedOutput) < 1e-9);

        network.calcError(new double[] { 0.5, 0.5 });
        double[] error = network.getOutputsError();

        Assert.assertEquals(7, error.length);

        double errorFirstOutput = thirdNeuronExpectedOutput * (1 - thirdNeuronExpectedOutput)
                * (0.5 - thirdNeuronExpectedOutput);

        Assert.assertTrue(Math.abs(error[5] - errorFirstOutput) < 1e-9);
        double errorSecondOutput = fourthNeuronExpectedOutput * (1 - fourthNeuronExpectedOutput)
                * (0.5 - fourthNeuronExpectedOutput);
        Assert.assertTrue(Math.abs(error[6] - errorSecondOutput) < 1e-9);

        double errorFirstHidden = firstNeuronExpectedOutput * (1 - firstNeuronExpectedOutput)
                * (7 * errorFirstOutput + 9 * errorSecondOutput);
        double errorSecondHidden = secondNeuronExpectedOutput * (1 - secondNeuronExpectedOutput)
                * (8 * errorFirstOutput + 10 * errorSecondOutput);
        Assert.assertTrue(Math.abs(error[3] - errorFirstHidden) < 1e-9);
        Assert.assertTrue(Math.abs(error[4] - errorSecondHidden) < 1e-9);
    }

    @Test
    public void testOutputErrorOutputLayer() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(2);

        network.setWeight(0, 1);
        network.setWeight(1, 2);
        network.setWeight(2, 3);
        network.setWeight(3, 4);
        network.setWeight(4, 5);
        network.setWeight(5, 6);
        network.calcOutput(new double[] { 1, 1, 1 });
        double firstNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 1 + 1 * 3 + 1 * 5)));
        double secondNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 2 + 1 * 4 + 1 * 6)));

        Assert.assertTrue(Math.abs(network.getNeuronOutput(1, 0) - firstNeuronExpectedOutput) < 1e-9);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(1, 1) - secondNeuronExpectedOutput) < 1e-9);

        network.calcError(new double[] { 0.5, 0.5 });
        double[] error = network.getOutputsError();

        Assert.assertEquals(5, error.length);
        double error4 = firstNeuronExpectedOutput * (1 - firstNeuronExpectedOutput) * (0.5 - firstNeuronExpectedOutput);
        Assert.assertTrue(Math.abs(error[3] - error4) < 1e-9);
        double error5 = secondNeuronExpectedOutput * (1 - secondNeuronExpectedOutput)
                * (0.5 - secondNeuronExpectedOutput);
        Assert.assertTrue(Math.abs(error[4] - error5) < 1e-9);

    }

    @Test
    public void testWeightError() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(2);
        network.addLayer(2);

        double finput = 1;
        double sinput = 2;
        double tinput = 3;

        double firstNeuronExpectedOutput = 1. / (1 + Math.exp(-(finput * 1 + sinput * 3 + tinput * 5)));
        double secondNeuronExpectedOutput = 1. / (1 + Math.exp(-(finput * 2 + sinput * 4 + tinput * 6)));
        double thirdNeuronExpectedOutput = 1. / (1 + Math
                .exp(-(firstNeuronExpectedOutput * 7 + secondNeuronExpectedOutput * 9)));
        double fourthNeuronExpectedOutput = 1. / (1 + Math
                .exp(-(firstNeuronExpectedOutput * 8 + secondNeuronExpectedOutput * 10)));

        network.setWeight(0, 1);
        network.setWeight(1, 2);
        network.setWeight(2, 3);
        network.setWeight(3, 4);
        network.setWeight(4, 5);
        network.setWeight(5, 6);
        network.setWeight(6, 7);
        network.setWeight(7, 8);
        network.setWeight(8, 9);
        network.setWeight(9, 10);

        network.calcOutput(new double[] { finput, sinput, tinput });
        network.calcError(new double[] { 0.5, 0.5 });

        double errorFirstOutput = thirdNeuronExpectedOutput * (1 - thirdNeuronExpectedOutput)
                * (0.5 - thirdNeuronExpectedOutput);
        double errorSecondOutput = fourthNeuronExpectedOutput * (1 - fourthNeuronExpectedOutput)
                * (0.5 - fourthNeuronExpectedOutput);

        double errorFirstHidden = firstNeuronExpectedOutput * (1 - firstNeuronExpectedOutput)
                * (7 * errorFirstOutput + 8 * errorSecondOutput);
        double errorSecondHidden = secondNeuronExpectedOutput * (1 - secondNeuronExpectedOutput)
                * (9 * errorFirstOutput + 10 * errorSecondOutput);

        double[] weightError = network.getWeightsError();
        Assert.assertTrue(weightError.length == 10);
        // third (output) layer

        Assert.assertTrue(Math.abs(errorFirstOutput * firstNeuronExpectedOutput - weightError[6]) < 1e-9);
        Assert.assertTrue(Math.abs(errorFirstOutput * secondNeuronExpectedOutput - weightError[8]) < 1e-9);
        Assert.assertTrue(Math.abs(errorSecondOutput * firstNeuronExpectedOutput - weightError[7]) < 1e-9);
        Assert.assertTrue(Math.abs(errorSecondOutput * secondNeuronExpectedOutput - weightError[9]) < 1e-9);
        // second (hidden layer)
        Assert.assertTrue(Math.abs(errorFirstHidden * finput - weightError[0]) < 1e-9);
        Assert.assertTrue(Math.abs(errorFirstHidden * sinput - weightError[1]) < 1e-9);
        Assert.assertTrue(Math.abs(errorFirstHidden * tinput - weightError[2]) < 1e-9);
        Assert.assertTrue(Math.abs(errorSecondHidden * finput - weightError[3]) < 1e-9);
        Assert.assertTrue(Math.abs(errorSecondHidden * sinput - weightError[4]) < 1e-9);
        Assert.assertTrue(Math.abs(errorSecondHidden * tinput - weightError[5]) < 1e-9);

    }

    @Test
    public void threeLayersHiddenOutputs() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(2);
        network.addLayer(2);

        network.setWeight(0, 1);
        network.setWeight(1, 2);
        network.setWeight(2, 3);
        network.setWeight(3, 4);
        network.setWeight(4, 5);
        network.setWeight(5, 6);
        network.calcOutput(new double[] { 1, 2, 3 });
        double firstNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 1 + 2 * 3 + 3 * 5)));
        double secondNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 2 + 2 * 4 + 3 * 6)));

        Assert.assertTrue(Math.abs(network.getNeuronOutput(0, 0) - 1) < 1e-9);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(0, 1) - 2) < 1e-9);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(0, 2) - 3) < 1e-9);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(1, 0) - firstNeuronExpectedOutput) < 1e-9);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(1, 1) - secondNeuronExpectedOutput) < 1e-9);

    }

    @Test
    public void threeLayersNetworkFullTest() {
        FeedForwardNetwork network = initializeThreeLayerNetwork();
        network.calcOutput(new double[] { 0.05, 0.1 });
        double[] output = network.getNetworkOutput();
        Assert.assertEquals(2, output.length);
        Assert.assertTrue(TestUtility.compareDoubles(0.75136507, output[0]));
        Assert.assertTrue(TestUtility.compareDoubles(0.772928465, output[1]));

        network = initializeThreeLayerNetwork();
        INeuralNetworkTrainer trainer = new ClassicBackPropagationTrainer(network, 0.5);
        trainer.train(new double[][] { { 0.05, 0.1 } }, new double[][] { { 0.01, 0.99 } }, 1);
        List<Double> weights = network.getWeights();
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(0), 0.149780716));
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(1), 0.24975114));
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(2), 0.19956143));
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(3), 0.29950229));
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(4), 0.35891648));
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(5), 0.511301270));
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(6), 0.408666186));
        Assert.assertTrue(TestUtility.compareDoubles(weights.get(7), 0.561370121));

    }

    @Test
    public void threeLayersNetworkOutputs() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(3);
        network.addLayer(2);
        network.addLayer(2);

        network.setWeight(0, 1);
        network.setWeight(1, 2);
        network.setWeight(2, 3);
        network.setWeight(3, 4);
        network.setWeight(4, 5);
        network.setWeight(5, 6);
        network.setWeight(6, 7);
        network.setWeight(7, 8);
        network.setWeight(8, 9);
        network.setWeight(9, 10);

        network.calcOutput(new double[] { 1, 2, 3 });
        double firstNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 1 + 2 * 3 + 3 * 5)));
        double secondNeuronExpectedOutput = 1. / (1 + Math.exp(-(1 * 2 + 2 * 4 + 3 * 6)));

        double thirdNeuronExpectedOutput = 1. / (1. + Math
                .exp(-(7 * firstNeuronExpectedOutput + 9 * secondNeuronExpectedOutput)));
        double fourthNeuronExpectedOutput = 1. / (1. + Math
                .exp(-(8 * firstNeuronExpectedOutput + 10 * secondNeuronExpectedOutput)));

        Assert.assertTrue("Wrong first output neuron",
                Math.abs(network.getNeuronOutput(2, 0) - thirdNeuronExpectedOutput) < 1e-9);
        Assert.assertTrue("Wrong second output neuron",
                Math.abs(network.getNeuronOutput(2, 1) - fourthNeuronExpectedOutput) < 1e-9);

    }

    @Test
    public void twoLayerNeuralNetworkIdentityOutputSingleNeuronPerLayer() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(1);
        network.addLayer(1);
        network.setWeight(0, 1);
        network.calcOutput(new double[] { 2 });
        double[] outputs = network.getNetworkOutput();
        Assert.assertTrue(outputs.length == 1);
        Assert.assertTrue(Math.abs(1. / (1 + Math.exp(-2)) - outputs[0]) < 1e-9);
    }

    @Test
    public void twoLayersHiddenOutputs() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(2);
        network.addLayer(1);
        network.setWeight(0, 1);
        network.setWeight(1, 1);
        network.setThresholdWeight(2, 1);
        network.calcOutput(new double[] { 2, 3 });

        Assert.assertTrue(Math.abs(network.getNeuronOutput(0, 0) - 2) < 1e-9);
        Assert.assertTrue(Math.abs(network.getNeuronOutput(0, 1) - 3) < 1e-9);

    }

    @Test
    public void twoNeuronsConnectedToOneOutputNeuronWithoutThreshold() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(2);
        network.addLayer(1);
        network.setWeight(0, 1);
        network.setWeight(1, 1);
        network.calcOutput(new double[] { 2, 3 });
        double[] outputs = network.getNetworkOutput();
        Assert.assertTrue(outputs.length == 1);
        Assert.assertTrue(Math.abs(1. / (1 + Math.exp(-5)) - outputs[0]) < 1e-9);
    }

    @Test
    public void twoNeuronsConnectedToOneOutputNeuronWithThreshold() {
        FeedForwardNetwork network = new FeedForwardNetwork();
        network.addLayer(2);
        network.addLayer(1);
        network.setWeight(0, 1);
        network.setWeight(1, 1);
        network.setThresholdWeight(2, 1);
        network.calcOutput(new double[] { 2, 3 });
        double[] outputs = network.getNetworkOutput();
        Assert.assertTrue(outputs.length == 1);
        Assert.assertTrue(Math.abs(1. / (1 + Math.exp(-6)) - outputs[0]) < 1e-9);
    }

}
