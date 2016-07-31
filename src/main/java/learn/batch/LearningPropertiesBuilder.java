package learn.batch;

import graph.Direction;
import learn.update.UpdateAlgorithm;
import learn.update.UpdateAlgorithms;

public class LearningPropertiesBuilder {
    private UpdateAlgorithm updateAlgorithm = UpdateAlgorithms.WEIGHTED_UNCANCELLED;
    private double initialPlasticity = 0.5;
    private double plasticityDecay = 1;
    private int plasticityEpochs = 1;
    private boolean variablePlasticity = false;
    private boolean resample = true;
    private double evaluationNoise = 1.0;
    private Direction direction = Direction.RIGHT;


    public LearningPropertiesBuilder setUpdateAlgorithm(UpdateAlgorithm updateAlgorithm) {
        this.updateAlgorithm = updateAlgorithm;
        return this;
    }

    public LearningPropertiesBuilder setInitialPlasticity(double initialPlasticity) {
        this.initialPlasticity = initialPlasticity;
        return this;
    }

    public LearningPropertiesBuilder setPlasticityDecay(double plasticityDecay) {
        this.plasticityDecay = plasticityDecay;
        return this;
    }

    public LearningPropertiesBuilder setPlasticityEpochs(int plasticityEpochs) {
        this.plasticityEpochs = plasticityEpochs;
        return this;
    }

    public LearningPropertiesBuilder setVariablePlasticity(boolean variablePlasticity) {
        this.variablePlasticity = variablePlasticity;
        return this;
    }

    public LearningPropertiesBuilder setResample(boolean resample) {
        this.resample = resample;
        return this;
    }

    public LearningPropertiesBuilder setEvaluationNoise(double evaluationNoise) {
        this.evaluationNoise = evaluationNoise;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public LearningProperties build() {
        return new LearningProperties(updateAlgorithm, initialPlasticity, plasticityDecay, plasticityEpochs, variablePlasticity, resample, evaluationNoise, direction);
    }

    public void setFromStrings(String parameter, String value) {
        switch (parameter) {
            case "updateAlgorithm":
                setUpdateAlgorithm(UpdateAlgorithms.createFromString(value));
                break;
            case "initialPlasticity":
                setInitialPlasticity(Double.parseDouble(value));
                break;
            case "plasticityDecay":
                setPlasticityDecay(Double.parseDouble(value));
                break;
            case "plasticityEpochs":
                setPlasticityEpochs(Integer.parseInt(value));
                break;
            case "variablePlasticity":
                setVariablePlasticity(Boolean.parseBoolean(value));
                break;
            case "resample":
                setResample(Boolean.parseBoolean(value));
                break;
            case "evaluationNoise":
                setEvaluationNoise(Double.parseDouble(value));
                break;
            case "direction":
                setDirection(Direction.valueOf(value));
                break;
            default:
                throw new IllegalArgumentException("Parameter "+parameter+" not found!");
        }
    }
}