package forms.primitives.feature.map;

import constraints.Constraint;
import gen.mapping.SubCandidateSet;
import phonetics.DiscretizedScale;

import java.util.List;

public class ScalarFeatureMapBuilder {
    private static Class<Constraint> constraintClass = Constraint.class;
    private static Class<SubCandidateSet> subCandidateSetClass = SubCandidateSet.class;

    public static ScalarFeatureMap<Constraint> buildConstraintMap(DiscretizedScale... scales) {
        int length = scales.length;
        if (length == 1) {
            return new ScalarFeatureMap1D<>(constraintClass,scales[0].getNumSteps());

        } else if (length == 2) {
            return new ScalarFeatureMap2D<Constraint>(constraintClass,scales[0].getNumSteps(), scales[1].getNumSteps());
        } else if (length == 3) {
            return new ScalarFeatureMap3D<Constraint>(constraintClass,scales[0].getNumSteps(), scales[1].getNumSteps(),scales[2].getNumSteps());
        } else if (length == 4) {
            return new ScalarFeatureMap4D<Constraint>(constraintClass,scales[0].getNumSteps(), scales[1].getNumSteps(),scales[2].getNumSteps(),scales[3].getNumSteps());
        }
        else return null;

    }

    public static ScalarFeatureMap<SubCandidateSet> buildSubCandidateMap(List<DiscretizedScale> scales) {
        int length = scales.size();
        if (length == 1) {
            return new ScalarFeatureMap1D<SubCandidateSet>(subCandidateSetClass,scales.get(0).getNumSteps());

        } else if (length == 2) {
            return new ScalarFeatureMap2D<SubCandidateSet>(subCandidateSetClass,scales.get(0).getNumSteps(), scales.get(1).getNumSteps());
        } else if (length == 3) {
            return new ScalarFeatureMap3D<SubCandidateSet>(subCandidateSetClass,scales.get(0).getNumSteps(), scales.get(1).getNumSteps(),scales.get(2).getNumSteps());
        } else if (length == 4) {
            return new ScalarFeatureMap4D<SubCandidateSet>(subCandidateSetClass,scales.get(0).getNumSteps(), scales.get(1).getNumSteps(),scales.get(2).getNumSteps(),scales.get(3).getNumSteps());
        }else if (length == 5) {
            return new ScalarFeatureMap5D<SubCandidateSet>(subCandidateSetClass,scales.get(0).getNumSteps(), scales.get(1).getNumSteps(),scales.get(2).getNumSteps(),scales.get(3).getNumSteps(),scales.get(4).getNumSteps());
        }
        else return null;
    }

    public static ScalarFeatureMap<SubCandidateSet> buildConstraintMap(int[] sizes) {
        int length = sizes.length;
        if (length == 1) {
            return new ScalarFeatureMap1D<SubCandidateSet>(subCandidateSetClass,sizes[0]);

        } else if (length == 2) {
            return new ScalarFeatureMap2D<SubCandidateSet>(subCandidateSetClass,sizes[0], sizes[1]);
        } else if (length == 3) {
            return new ScalarFeatureMap3D<SubCandidateSet>(subCandidateSetClass,sizes[0], sizes[1],sizes[2]);
        } else if (length == 4) {
            return new ScalarFeatureMap4D<SubCandidateSet>(subCandidateSetClass,sizes[0], sizes[1],sizes[2],sizes[3]);
        } else if (length == 5) {
            return new ScalarFeatureMap5D<SubCandidateSet>(subCandidateSetClass,sizes[0], sizes[1],sizes[2],sizes[3],sizes[4]);
        }
        else return null;
    }
}
