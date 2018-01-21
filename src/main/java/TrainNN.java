import GP.GPTreeLoader;
import GameSpecific.Direction;
import GameSpecific.Move;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.BufferedDataSet;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.error.ErrorFunction;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.QuickPropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.NguyenWidrowRandomizer;
import org.neuroph.util.random.WeightsRandomizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TrainNN implements LearningEventListener {

    //public static void main(String[] args) {
//
    //    GPParser.TestParse();
//
    //    NeuralNetwork neuralNetwork = NeuralNetwork.load("MLPv4.nnet");
    //    // set network input
    //    neuralNetwork.setInput(0.9, 0.1, 0.1, 0.1, 0.1);
    //    // calculate network
    //    neuralNetwork.calculate();
    //    // get network output
    //    double[] networkOutput = neuralNetwork.getOutput();
    //    System.out.println(Arrays.toString(networkOutput));
    //}

    public static void main(String[] args) throws FileNotFoundException {

    }

    /**
     * Runs this sample
     * [5,3,6,5]
     * 0.01
     * 10000
     * out.nnet
     * TrainData/NN_10_10_TRAIN.log
     */
    public void run(int[] networkLayout, double learningRate, int maxIter, String fileName, String dataFile) {
        DataSet trainingSet = new DataSet(5, 5);
        double[][] data = loadSamples(dataFile);
        for (int i = 0; i < data.length; i++) {

            trainingSet.addRow(new DataSetRow(new double[]{data[i][0], data[i][1], data[i][2], data[i][3], data[i][4]}, new double[]{data[i][5],data[i][6],data[i][7],data[i][8],data[i][9]}));
        }

        // create multi layer perceptron
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, networkLayout);
        myMlPerceptron.randomizeWeights(new NguyenWidrowRandomizer(0.2, 0.8));

        myMlPerceptron.setLearningRule(new BackPropagation());
        myMlPerceptron.getLearningRule().setLearningRate(learningRate);
        myMlPerceptron.getLearningRule().setMaxIterations(maxIter);
        myMlPerceptron.getLearningRule().setErrorFunction(new NNErrorFunction());
        // enable batch if using MomentumBackpropagation
        //if( myMlPerceptron.getLearningRule() instanceof MomentumBackpropagation )
        //	myMlPerceptron.getLearningRule().setBatchMode(false);

        LearningRule learningRule = myMlPerceptron.getLearningRule();
        learningRule.addListener(this);

        // learn the training set
        System.out.println("Training neural network...");
        myMlPerceptron.learn(trainingSet);

        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(myMlPerceptron, trainingSet);

        // save trained neural network
        myMlPerceptron.save(fileName);

      // // load saved neural network
      // NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");

      // // test loaded neural network
      // System.out.println("Testing loaded neural network");
      // testNeuralNetwork(loadedMlPerceptron, trainingSet);
    }

    /**
     * Prints network output for the each element from the specified training set.
     *
     * @param neuralNet neural network
     * @param testSet   test set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for (DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString(testSetRow.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
            System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
    }

    static double[][] loadSamples(String file) {

        List<double[]> tData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            int k = 0;
            while (line != null) {
                line = br.readLine();
                if (line == null) break;
                String[] data = line.split(",");
                tData.add(new double[10]);

                //if (k == 10000) break;
                for (int i = 0; i < 10; i++) {
                    tData.get(k)[i] = Double.parseDouble(data[i]);
                }
                k++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] tOutput = new double[tData.size()][10];
        for (int i = 0; i < tData.size(); i++) {
            for (int j = 0; j < 10; j++) {
                tOutput[i][j] = tData.get(i)[j];
            }
        }
        return tOutput;
    }
}
