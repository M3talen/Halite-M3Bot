import org.neuroph.core.learning.error.ErrorFunction;

import java.io.Serializable;

public final class NNErrorFunction implements ErrorFunction, Serializable {
    private static final long serialVersionUID = 1L;
    private transient double totalError;
    private transient double patternCount;

    public NNErrorFunction() {
        this.reset();
    }

    public double[] addPatternError(double[] predictedOutput, double[] targetOutput) {
        double sum[] = new double[predictedOutput.length];
        for (int i = 0; i < predictedOutput.length; i++) {
            if(predictedOutput[i] >= 0.5)
                predictedOutput[i] = 1;
            else
                predictedOutput[i] = 0;
            sum[i] = predictedOutput[i] - targetOutput[i];
            this.totalError+= sum[i]*sum[i];
        }

        ++this.patternCount;
        return sum;
    }

    public void reset() {
        this.totalError = 0.0D;
        this.patternCount = 0.0D;
    }

    public double getTotalError() {
        return this.totalError / (2.0D * this.patternCount);
    }
}
