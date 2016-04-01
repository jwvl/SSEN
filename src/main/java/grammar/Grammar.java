/**
 *
 */
package grammar;

import com.typesafe.config.ConfigFactory;
import eval.Evaluation;
import forms.FormPair;
import grammar.levels.Level;
import grammar.levels.LevelSpace;
import grammar.subgraph.CandidateSpaces;
import learn.ViolatedCandidate;
import learn.batch.LearningProperties;
import ranking.Con;
import ranking.GrammarHierarchy;

/**
 * @author jwvl
 * @date 18/10/2014
 * Following Prince & Smolensky 1993, a Grammar is a tuple <GEN,CON>.
 */
public abstract class Grammar {

    private final LevelSpace levelSpace;
    private final String name;
    private final Con con;
    private final LearningProperties defaultLearningProperties;
    private final double stratumMultiplier;
    private CandidateSpaces candidateSpaces;

    /**
     * @param levelSpace
     * @param name
     * @param learningProperties
     */
    protected Grammar(LevelSpace levelSpace, String name, Con con, LearningProperties learningProperties) {
        this.levelSpace = levelSpace;
        this.name = name;
        this.con = con;
        this.defaultLearningProperties = learningProperties;
        this.stratumMultiplier = ConfigFactory.load().getDouble("system.stratumMultiplier");
    }


    public LevelSpace getLevelSpace() {
        return levelSpace;
    }


    public String getName() {
        return name;
    }


    public abstract Evaluation evaluate(FormPair formPair, boolean newEvaluation, double evaluationNoise);

    public Evaluation evaluate(FormPair formPair, boolean newEvaluation) {
        return evaluate(formPair, newEvaluation, defaultLearningProperties.getEvaluationNoise());
    }

    public ViolatedCandidate getWinner(FormPair formPair, boolean newEvaluation, double evaluationNoise) {
        return evaluate(formPair, newEvaluation, evaluationNoise).getWinner();
    }


    /**
     * @param string
     * @return
     */
    public Level getLevel(String string) {
        for (Level level : levelSpace) {
            if (level.getInfo().getAbbreviation().equals(string)) {
                return level;
            }
        }
        return Level.NULL_LEVEL;
    }


    /**
     * @return the con
     */
    public Con getCon() {
        return con;
    }

    public abstract GrammarHierarchy getRankedCon();

    public LearningProperties getDefaultLearningProperties() {
        return defaultLearningProperties;
    }


    public CandidateSpaces getCandidateSpaces() {
        return candidateSpaces;
    }

    public void addCandidateSpaces(CandidateSpaces candidateSpaces) {
        this.candidateSpaces = candidateSpaces;
    }


}
