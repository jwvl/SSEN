package simulate.phonetic.vowelsystems.data;

import eval.Evaluation;
import forms.Form;
import forms.FormPair;
import gen.rule.string.Side;
import grammar.Grammar;
import graph.Direction;
import learn.data.PairDistribution;
import util.collections.ConfusionMatrix;

public class ConfusionMatrixBuilder {

    public static ConfusionMatrix fromPairDistribution(PairDistribution distribution, Grammar grammar, int numPairs, double evaluationNoise) {
        ConfusionMatrix result = new ConfusionMatrix();
        for (int i=0; i < numPairs; i++) {
            FormPair pairToTest = distribution.drawFormPair();
            Form right = pairToTest.right();
            Evaluation evaluation = grammar.evaluate(FormPair.createUnlabeled(pairToTest.left(), Direction.RIGHT), true, evaluationNoise);
            Form found = evaluation.getWinner().getCandidate().getEndForm(Side.RIGHT, false);
            result.addMapping(right,found);
        }
        return result;
    }
}
