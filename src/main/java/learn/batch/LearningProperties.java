/**
 *
 */
package learn.batch;

import com.google.common.base.Strings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import graph.Direction;
import learn.update.UpdateAlgorithm;
import learn.update.UpdateAlgorithms;

import java.util.Map;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class LearningProperties {
    private final UpdateAlgorithm updateAlgorithm;
    private final double initialPlasticity;
    private final double plasticityDecay;
    private final int plasticityEpochs;
    private final boolean variablePlasticity;
    private final boolean resample;
    private final double evaluationNoise;
    private final Direction direction;

    public boolean isResample() {
        return resample;
    }

    public Direction getDirection() {
        return direction;
    }

    public UpdateAlgorithm getUpdateAlgorithm() {
        return updateAlgorithm;
    }

    public double getInitialPlasticity() {
        return initialPlasticity;
    }

    public double getPlasticityDecay() {
        return plasticityDecay;
    }

    public int getPlasticityEpochs() {
        return plasticityEpochs;
    }

    public boolean isVariablePlasticity() {
        return variablePlasticity;
    }

    public double getEvaluationNoise() {
        return evaluationNoise;
    }

    public static LearningProperties fromConfigurationInField(String withString) {
        String prefix = "learning." + (Strings.isNullOrEmpty(withString) ? "" : ".") + withString;
        Config config = ConfigFactory.load();
        Direction direction = Direction.valueOf(config.getString(prefix + "direction"));
        UpdateAlgorithm updateAlgorithm = UpdateAlgorithms.createFromString(config.getString(prefix + "updateAlgorithm"));
        double initialPlasticity = config.getDouble(prefix + "initialPlasticity");
        double plasticityDecay = config.getDouble(prefix + "plasticityDecay");
        int plasticityEpochs = config.getInt(prefix + "plasticityEpochs");
        boolean variablePlasticity = config.getBoolean(prefix + "variablePlasticity");
        boolean resample = config.getBoolean(prefix + "resample");
        double evaluationNoise = config.getDouble(prefix + "evaluationNoise");
        for (Map.Entry<String, ConfigValue> entry : config.entrySet()) {
            System.out.println(entry);
        }
        return new LearningProperties(updateAlgorithm, initialPlasticity, plasticityDecay, plasticityEpochs, variablePlasticity, resample, evaluationNoise, direction);
    }

    public static LearningProperties fromConfiguration() {
        return LearningProperties.fromConfigurationInField("");
    }

    LearningProperties(UpdateAlgorithm updateAlgorithm, double initialPlasticity, double plasticityDecay, int plasticityEpochs, boolean variablePlasticity, boolean resample, double evaluationNoise, Direction direction) {
        this.updateAlgorithm = updateAlgorithm;
        this.initialPlasticity = initialPlasticity;
        this.plasticityDecay = plasticityDecay;
        this.plasticityEpochs = plasticityEpochs;
        this.variablePlasticity = variablePlasticity;
        this.resample = resample;
        this.evaluationNoise = evaluationNoise;
        this.direction = direction;
    }

    public static LearningPropertiesBuilder builder() {
        return new LearningPropertiesBuilder();
    }

}
