Playing
     * args[0] - 'NN' or 'GP'
     * args[1] - path to file .nnet or .tree
     * args[2] - playerName -> example M3Bot
     
Learning GP
     * args[0] - 'learnGP'
     * args[1] - parallel - true/false
     * args[2] - iterations
     * args[3] - file to use for learning -> example : DATA_10_10.log
     
Learning NN
     * args[0] - 'learnNN'
     * args[1] - NN layout -> example : '(5,20,10,5)' first and last must be 5.
     * args[2] - learning rate -> example : 0.01
     * args[3] - max iterations -> example : 1000
     * args[4] - file to save .nnet to -> example : MLPv3.nnet
     * args[5] - file to use for learning -> example : DATA_10_10.log

Starting the engine:
     * ./halite-engine -d "100 100" "java -jar HaliteBot-1.9.jar GP GP-Tree.tree M3GP1" "java -jar HaliteBot-1.9.jar GP GP-Tree.tree M3GP2"

