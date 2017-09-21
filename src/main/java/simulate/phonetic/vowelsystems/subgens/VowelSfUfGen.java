package simulate.phonetic.vowelsystems.subgens;

import com.google.common.base.Function;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;
import forms.primitives.feature.SurfaceRangeFeature;
import forms.primitives.feature.map.ScalarFeatureMap;
import forms.primitives.feature.map.ScalarFeatureMapBuilder;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import simulate.phonetic.vowelsystems.constraints.VowelFaithConstraint;
import simulate.phonetic.vowelsystems.levels.FeatureValueForm;
import simulate.phonetic.vowelsystems.levels.UnderlyingVowelForm;
import simulate.phonetic.vowelsystems.levels.VowelSimLevels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VowelSfUfGen extends SubGen<FeatureValueForm, UnderlyingVowelForm> {

    private final List<UnderlyingVowelForm> vowelForms;
    private Table<SurfaceRangeFeature,String, Constraint> faithConstraints;
    private ScalarFeatureMap<SubCandidateSet> subcandidates;
    private Map<FormMapping,ConstraintArrayList> mappingsToConstraintLists;

    public VowelSfUfGen(List<List<SurfaceRangeFeature>> featureLists, List<FeatureValueForm> surfaceForms, List<UnderlyingVowelForm> vowelForms) {
        super(VowelSimLevels.getSurfaceFormLevel(), VowelSimLevels.getUnderlyingFormLevel());
        this.vowelForms = vowelForms;
        faithConstraints = createConstraints(featureLists,vowelForms);
        mappingsToConstraintLists = new HashMap<>();
        int[] sizes = new int[featureLists.size()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = featureLists.get(i).size();
        }
        subcandidates = ScalarFeatureMapBuilder.buildConstraintMap(sizes);
        buildMappings(surfaceForms,vowelForms);
    }

    private void buildMappings(List<FeatureValueForm> surfaceForms, List<UnderlyingVowelForm> vowelForms) {
        for (FeatureValueForm surfaceForm: surfaceForms) {
            List<FormMapping> formMappingList = Lists.newArrayListWithExpectedSize(vowelForms.size());
            for (UnderlyingVowelForm underlyingVowelForm: vowelForms) {
                ConstraintArrayList relevantConstraints = ConstraintArrayList.create(surfaceForm.getNumSubForms());
                FormMapping toAdd = new VowelSfUfMapping(surfaceForm,underlyingVowelForm);
                formMappingList.add(toAdd);
                for (SurfaceRangeFeature feature: surfaceForm.getFeatures()) {
                    relevantConstraints.add(faithConstraints.get(feature,underlyingVowelForm.getContent()));
                }
                mappingsToConstraintLists.put(toAdd,relevantConstraints);
            }
            SubCandidateSet subCandidateSet = SubCandidateSet.of(formMappingList);
            subcandidates.put(subCandidateSet,surfaceForm.getContents());
        }
    }

    private Table<SurfaceRangeFeature, String, Constraint> createConstraints(List<List<SurfaceRangeFeature>> featureLists, List<UnderlyingVowelForm> vowelForms) {
        Table<SurfaceRangeFeature,String,Constraint> result = HashBasedTable.create();
        for (List<SurfaceRangeFeature> featureList: featureLists) {
            for (SurfaceRangeFeature feature: featureList) {
                for (UnderlyingVowelForm vowelForm: vowelForms) {
                    String vowel = vowelForm.getContent();
                    Constraint toAdd = new VowelFaithConstraint(0.0,feature,vowel);
                    System.out.println("Created constraint " + toAdd);
                    result.put(feature,vowel,toAdd);
                }
            }
        }
        return result;
    }

    @Override
    protected Function<FeatureValueForm, SubCandidateSet> getRightFunction() {
        return input -> subcandidates.getObject(input.getContents());
    }

    @Override
    public ConstraintArrayList getAssociatedConstraints(FormMapping mapping) {
        return mappingsToConstraintLists.getOrDefault(mapping,ConstraintArrayList.EMPTY);
    }
}
