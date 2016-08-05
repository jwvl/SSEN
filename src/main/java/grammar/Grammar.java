/**
 *
 */
package grammar;

import constraints.Constraint;
import constraints.hierarchy.reimpl.Hierarchy;
import eval.Evaluation;
import forms.FormPair;
import grammar.levels.Level;
import grammar.levels.LevelSpace;
import grammar.subgraph.CandidateSpaces;
import learn.ViolatedCandidate;
import learn.batch.LearningProperties;

/**
 * @author jwvl
 * @date 18/10/2014
 * Following Prince & Smolensky 1993, a Grammar is a tuple <GEN,CON>.
 */
public abstract class Grammar {

    private final LevelSpace levelSpace;
    private final String name;
    private final Hierarchy con;
    private LearningProperties defaultLearningProperties;


    private CandidateSpaces candidateSpaces;

    /**
     * @param levelSpace
     * @param name
     * @param learningProperties
     */
    protected Grammar(LevelSpace levelSpace, String name, Hierarchy con, LearningProperties learningProperties) {
        this.levelSpace = levelSpace;
        this.name = name;
        this.con = con;
        this.defaultLearningProperties = learningProperties;
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
    public Hierarchy getHierarchy() {
        return con;
    }

    public LearningProperties getDefaultLearningProperties() {
        return defaultLearningProperties;
    }


    public CandidateSpaces getCandidateSpaces() {
        return candidateSpaces;
    }

    public void addCandidateSpaces(CandidateSpaces candidateSpaces) {
        this.candidateSpaces = candidateSpaces;
    }


    public void setDefaultLearningProperties(LearningProperties defaultLearningProperties) {
        this.defaultLearningProperties = defaultLearningProperties;
    }


    public void resetConstraints(double value) {
        Hierarchy h = getHierarchy();
        for (Constraint c: getHierarchy()) {
            h.putValue(c,value);
        }
    }
}
