import GP.GPTreeLoader;
import GameSpecific.*;
import org.neuroph.core.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class M3Bot {
    /**
     *
     * @param args
     * @throws java.io.IOException
     * Playing
     * args[0] - 'NN' or 'GP'
     * args[1] - path to file .nnet or .tree
     * args[2] - playerName -> example M3Bot
     *
     * Learning GP
     * args[0] - 'learnGP'
     * args[1] - parallel - true/false
     * args[2] - iterations
     * args[3] - file to use for learning -> example : DATA_10_10.log
     *
     * Learning NN
     * args[0] - 'learnNN'
     * args[1] - NN layout -> example : '(5,20,10,5)' first and last must be 5.
     * args[2] - learning rate -> example : 0.01
     * args[3] - max iterations -> example : 1000
     * args[4] - file to save .nnet to -> example : MLPv3.nnet
     * args[5] - file to use for learning -> example : DATA_10_10.log
      */
    public static void main(String[] args) throws java.io.IOException {

        if(args[0].equals("NN")){
            playNeuralNetwork(args[2], args[1]);
        }
        if(args[0].equals("GP")){
            playGP(args[2], args[1]);
        }
        if(args[0].equals("learnGP")){
            TrainGP.loadData(args[3]);
            TrainGP learning = new TrainGP();
            learning.run(Boolean.parseBoolean(args[1]), Integer.parseInt(args[2]));
        }if(args[0].equals("learnNN")){
            TrainNN learnNN= new TrainNN();
            String tLayout = args[1].replace("(","").replace(")","");
            String[] data = tLayout.split(",");
            int[] layout = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                layout[i] = Integer.parseInt(data[i]);
            }
            learnNN.run(layout, Double.parseDouble(args[2]), Integer.parseInt(args[3]), args[4], args[5]);
        }
    }

    private static void playGP(String name, String path) {
        String GPTree = new GPTreeLoader().loadGPTree(path);

        final InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;
        final GameMap gameMap = iPackage.map;
        Networking.sendInit(name);

        while (true) {
            List<Move> moves = new ArrayList<Move>();

            Networking.updateFrame(gameMap);

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {
                    final Location location = gameMap.getLocation(x, y);
                    final Site site = location.getSite();

                    if (site.owner == myID) {
                        List<Double> data = new ArrayList<>();
                        StringBuilder action = new StringBuilder();
                        for (Direction dir : Direction.DIRECTIONS) {
                            Site s = gameMap.getSite(location, dir);
                            data.add((double) (s.strength/255f));
                        }

                        double[] input = new double[]{data.get(0), data.get(1), data.get(2), data.get(3), data.get(4)};
                       double result = GPParser.parse(GPTree, input);
                        if (result > 0 && result < 1) result = 0;
                        if (result > 1 && result < 2) result = 1;
                        if (result > 2 && result < 4) result = 2;
                        if (result > 4 && result < 8) result = 3;
                        if (result > 8 && result < 16) result = 4;


                      for (Direction direction : Direction.DIRECTIONS) {
                          if(result == direction.ordinal())
                              moves.add(new Move(location, direction));
                      }

                    }

                }
            }
            Networking.sendFrame(moves);
        }
    }

    private static void playNeuralNetwork(String name, String path) {
        NeuralNetwork neuralNetwork = NeuralNetwork.load(path);
        final InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;
        final GameMap gameMap = iPackage.map;

        Networking.sendInit(name);


        while (true) {
            List<Move> moves = new ArrayList<Move>();

            Networking.updateFrame(gameMap);

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {
                    final Location location = gameMap.getLocation(x, y);
                    final Site site = location.getSite();

                    //   logger.info("X " + location.getX() + "   Y " + location.getY());
                    if (site.owner == myID) {
                        List<Double> data = new ArrayList<>();
                        StringBuilder action = new StringBuilder();
                        for (Direction dir : Direction.DIRECTIONS) {
                            Site s = gameMap.getSite(location, dir);
                            data.add((double) (s.strength/255f));
                        }

                        // set network input
                        neuralNetwork.setInput(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4));
                        // calculate network
                        neuralNetwork.calculate();
                        // get network output
                        double[] networkOutput = neuralNetwork.getOutput();
                        int index = -1;
                        double max = 0;
                        for (int i = 0; i < networkOutput.length; i++) {
                            if(networkOutput[i] > max){
                                max = networkOutput[i];
                                index = i;
                            }
                        }
                        for (Direction direction : Direction.DIRECTIONS) {
                            if(index == direction.ordinal())
                                moves.add(new Move(location, direction));
                        }

                    }

                }
            }
            Networking.sendFrame(moves);
        }
    }
}
