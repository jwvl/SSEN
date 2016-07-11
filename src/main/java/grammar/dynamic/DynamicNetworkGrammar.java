/**
 *
 */
package grammar.dynamic;

import eval.Evaluation;
import forms.Form;
import forms.FormPair;
import forms.GraphForm;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import grammar.Grammar;
import grammar.levels.Level;
import grammar.levels.LevelSpace;
import learn.batch.LearningProperties;
import ranking.DynamicSampledHierarchy;
import ranking.GrammarHierarchy;
import ranking.constraints.Constraint;
import ranking.constraints.helper.ConstraintArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 8, 2015
 */
public class DynamicNetworkGrammar extends Grammar {

    private SubGen<?, ?>[] subGensByLevel;
    private DynamicSampledHierarchy lastSampledHierarchy;
    private GrammarHierarchy con;


    private DynamicNetworkGrammar(LevelSpace levels, String name, GrammarHierarchy con, LearningProperties learningProperties) {
        super(levels, name, con, learningProperties);
        this.subGensByLevel = new SubGen<?, ?>[getLevelSpace().getSize()];
        this.con = con;
    }

    public static DynamicNetworkGrammar createInstance(LevelSpace levels, String name) {
        GrammarHierarchy con = new GrammarHierarchy();
        return new DynamicNetworkGrammar(levels, "Grammar", con, LearningProperties.fromConfiguration());
    }

    public void addSubGen(SubGen<?, ?> toAdd) {
        Level left = toAdd.getLevels().getLeft();
        Level right = toAdd.getLevels().getRight();
        if (getLevelSpace().containsLevel(left) && getLevelSpace().containsLevel(right)) {
            subGensByLevel[left.myIndex()] = toAdd;
        } else {
            System.err.println("Could not find levels " + left + ", " + right + "in space");
        }
    }


    /**
     * @param formLevel
     * @return
     */
    public SubGen<?, ?> getSubGenForLevel(Level formLevel) {
        int levelIndex = formLevel.myIndex();
        if (levelIndex < getLevelSpace().getSize()) {
            return subGensByLevel[levelIndex];
        } else {
            return null;
        }
    }

    /**
     * @return
     */
    public GrammarHierarchy getCon() {
        return con;
    }


    @Override
    public GrammarHierarchy getRankedCon() {
        return con;
    }

    @Override
    public void resetConstraints() {
        for (Constraint c: con) {
            con.put(c,100.0);
        }
    }


    public SubGen<?, ?>[] getSubGensByLevel() {
        return subGensByLevel;
    }

    /* (non-Javadoc)
     * @see grammar.Grammar#evaluate(forms.Form, forms.Form, boolean)
     */
    @Override
    public Evaluation evaluate(FormPair formPair, boolean resample, double evaluationNoise) {
        DynamicNetworkEvaluation result;
        if (resample) {
            result = new DynamicNetworkEvaluation(this, evaluationNoise);
        } else {
            result = new DynamicNetworkEvaluation(this, lastSampledHierarchy);
        }
        result.setStartAndEndForm(formPair);
        result.run();
        lastSampledHierarchy = result.getSampledHierarchy();
        return result;
    }


    public List<Form> getSuccessors(Form form) {
        Level level = form.getLevel();
        SubGen<?, ?> subGen = getSubGenForLevel(level);
        if (subGen == null) {
            return Collections.singletonList(GraphForm.getSinkInstance());
        }
        SubCandidateSet subCandidates = subGen.generateRight(form);
        List<Form> result = new ArrayList<>(subCandidates.size());
        for (FormMapping mapping : subCandidates) {
            result.add(mapping.right());
        }
        return result;
    }

    public ConstraintArrayList getViolators(FormMapping formMapping) {
        if (formMapping.left() == null) {
            return ConstraintArrayList.EMPTY;
        }
        Level level = formMapping.left().getLevel();
        SubGen<?, ?> subGen = getSubGenForLevel(level);
        return subGen.getAssociatedConstraints(formMapping);
    }

    public ConstraintArrayList getViolators(List<FormMapping> formMappings) {
        ConstraintArrayList constraints = ConstraintArrayList.create();
        for (FormMapping formMapping : formMappings) {
            constraints.append(getViolators(formMapping));
        }
        return constraints;

    }

}
