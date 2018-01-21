
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.Tree;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class TrainGP {

    static final Op<Double> ifgt = Op.of("if>",
            (Serializable & BinaryOperator<Double>) (a, b) -> (a >= b) ? a : b);
    static final Op<Double> iflt = Op.of("if<",
            (Serializable & BinaryOperator<Double>) (a, b) -> a < b ? a : b);
    static final Op<Double> ifsum = Op.of("ifsum",
            (Serializable & BinaryOperator<Double>) (a, b) -> a < b ? a + b : a);

    static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
            MathOp.ADD,
            MathOp.SUB,
            MathOp.MUL,
            MathOp.EXP,
            MathOp.SQRT,
            MathOp.MOD,
            ifgt,
            iflt,
            ifsum

    );

    static final ISeq<Op<Double>> TERMINALS = ISeq.of(
            Var.of("x1", 0),
            Var.of("x2", 1),
            Var.of("x3", 2),
            Var.of("x4", 3),
            Var.of("x5", 4),
            EphemeralConst.of(() ->
                    RandomRegistry.getRandom().nextDouble()),
            EphemeralConst.of(() ->
                    (double) RandomRegistry.getRandom().nextInt(128) + 1)
    );


    static double[][] SAMPLES = null;

    static double error(final ProgramGene<Double> program) {
        double err = Arrays.stream(SAMPLES).mapToDouble(sample -> {
            final double x1 = sample[0];
            final double x2 = sample[1];
            final double x3 = sample[2];
            final double x4 = sample[3];
            final double x5 = sample[4];
            final double y = sample[5];
            double result = program.eval(x1, x2, x3, x4, x5);
            result=abs(result);
            if (result > 0 && result < 1) result = 1;
            if (result > 1 && result < 2) result = 2;
            if (result > 2 && result < 4) result = 4;
            if (result > 4 && result < 8) result = 8;
            if (result > 8 && result < 16) result = 16;
            return pow(y - result, 2);
        })
                .sum();
        return err;
    }

    static final EvolutionStatistics<Double, ?>
            statistics = EvolutionStatistics.ofNumber();


    static final Codec<ProgramGene<Double>, ProgramGene<Double>> CODEC =
            Codec.of(
                    Genotype.of(ProgramChromosome.of(
                            // Program tree depth.
                            5,
                            // Chromosome validator.
                            ch -> ch.getRoot().size() <= 8000,
                            OPERATIONS,
                            TERMINALS
                    )),
                    Genotype::getGene
            );

    static final Engine<ProgramGene<Double>, Double> engine = Engine
            .builder(TrainGP::error, CODEC)

            .minimizing()
            .alterers(
                    new SingleNodeCrossover<>(),
                    new Mutator<>())
            .build();

     static void loadData(String file){
        SAMPLES =  loadSamples(file);
    }

    public void run(boolean parallel, int iterations) throws IOException {
        System.out.println("Started program");

        ProgramGene<Double> program = null;

        System.out.println("Starting engine");
        if (parallel) {
            System.out.println("Parallel mode");
            program = engine.stream()
                    .limit(Long.parseLong(String.valueOf(iterations)))
                    .parallel()
                    .peek(statistics)
                    .peek(r -> System.out.println(statistics))
                    .collect(EvolutionResult.toBestGenotype()).getGene();
        } else {
            System.out.println("Single core mode");
            program = engine.stream()
                    .limit(Long.parseLong(String.valueOf(iterations)))
                    .peek(statistics)
                    .peek(r -> System.out.println(statistics))
                    .collect(EvolutionResult.toBestGenotype()).getGene();
        }

        for (int i = 0; i < 20; i++) {
            double[] sample = SAMPLES[i];
            final double x1 = sample[0];
            final double x2 = sample[1];
            final double x3 = sample[2];
            final double x4 = sample[3];
            final double x5 = sample[4];
            final double y = sample[5];
            double eval = program.eval(x1, x2, x3, x4, x5);
            if (eval > 0 && eval < 1) eval = 1;
            if (eval > 1 && eval < 2) eval = 2;
            if (eval > 2 && eval < 4) eval = 4;
            if (eval > 4 && eval < 8) eval = 8;
            if (eval > 8 && eval < 16) eval = 16;
            System.out.println("Predicted : " + eval + "  REAL :" + y);
        }

        //System.out.println(Tree.toDottyString(program));
        System.out.println(Tree.toString(program));
        System.out.println("Tree.toCompactString(program) = " + Tree.toCompactString(program));

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
                tData.add(new double[6]);

                //if (k == 10000) break;
                for (int i = 0; i < 6; i++) {
                    tData.get(k)[i] = Double.parseDouble(data[i]);
                }
                k++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] tOutput = new double[tData.size()][6];
        for (int i = 0; i < tData.size(); i++) {
            for (int j = 0; j < 6; j++) {
                tOutput[i][j] = tData.get(i)[j];
            }
        }
        return tOutput;
    }


}
