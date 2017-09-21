package simulate.phonetic.vowelsystems.subgens;

import com.google.common.base.Function;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;
import forms.primitives.feature.ScaleFeature;
import forms.primitives.feature.SurfaceRangeFeature;
import forms.primitives.feature.map.ScalarFeatureMap;
import forms.primitives.feature.map.ScalarFeatureMapBuilder;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import grammar.levels.Level;
import phonetics.DiscretizedScale;
import simulate.phonetic.vowelsystems.constraints.VowelCueConstraint;
import simulate.phonetic.vowelsystems.levels.FeatureValueForm;
import simulate.phonetic.vowelsystems.levels.PhoneticValueForm;
import simulate.phonetic.vowelsystems.levels.VowelSimLevels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VowelPfSfGen extends SubGen<PhoneticValueForm,FeatureValueForm> {
    List<DiscretizedScale> inputScales;
    Map<String,List<SurfaceRangeFeature>> parentsToFeatures;
    ScalarFeatureMap<SubCandidateSet> subCandidatesPerInput;
    Table<ScaleFeature,SurfaceRangeFeature, Constraint> submappingToConstraintTable;
    Map<FormMapping,ConstraintArrayList> mappingToConstraintArrayList;




    public VowelPfSfGen(List<DiscretizedScale> inputScales, Map<String,List<SurfaceRangeFeature>> parentsToFeatures) {
        this(VowelSimLevels.getPhoneticLevel(), VowelSimLevels.getSurfaceFormLevel());
        this.inputScales = inputScales;
        this.parentsToFeatures = parentsToFeatures;
        this.subCandidatesPerInput = ScalarFeatureMapBuilder.buildSubCandidateMap(inputScales);
        submappingToConstraintTable = buildSubmappingToConstraintTable();
        buildMappings();
        System.out.println("Done");
        List<PhoneticValueForm> inputs = RangeFeatureFactory.getAllPfs(inputScales);
    }

    private void buildMappings() {
        List<PhoneticValueForm> inputs = RangeFeatureFactory.getAllPfs(inputScales);
        List<List<SurfaceRangeFeature>> surfaceFeatureLists = getSurfaceFeatureLists();
        List<FeatureValueForm> outputs = RangeFeatureFactory.getAllSfs(surfaceFeatureLists);
        mappingToConstraintArrayList = new HashMap<>();
        for (PhoneticValueForm phoneticValueForm: inputs) {
            List<FormMapping> mappingsToAdd = Lists.newArrayList();
            for (FeatureValueForm featureValueForm: outputs) {
                FormMapping mappingToAdd = new VowelPfSfMapping(phoneticValueForm,featureValueForm);
                mappingsToAdd.add(mappingToAdd);
                addConstraintList(mappingToAdd, phoneticValueForm, featureValueForm);

            }
            SubCandidateSet subCandidateSet = SubCandidateSet.of(mappingsToAdd);
            subCandidatesPerInput.put(subCandidateSet,phoneticValueForm.getContents());
        }
    }

    private void addConstraintList(FormMapping mappingToAdd, PhoneticValueForm phoneticValueForm, FeatureValueForm featureValueForm) {
        ConstraintArrayList list = ConstraintArrayList.create(phoneticValueForm.getNumSubForms());
        for (ScaleFeature phoneticFeature: phoneticValueForm.getFeatures()) {
            String parentKey = phoneticFeature.getMeasure().getUnitName();
            for (SurfaceRangeFeature surfaceRangeFeature: featureValueForm.getFeatures()) {
                Constraint toAdd = submappingToConstraintTable.get(phoneticFeature,surfaceRangeFeature);
                if (toAdd != null) {
                    list.add(toAdd);
                }
            }
        }
        mappingToConstraintArrayList.put(mappingToAdd,list);
    }

    private List<List<SurfaceRangeFeature>> getSurfaceFeatureLists() {
        List<List<SurfaceRangeFeature>> result = Lists.newArrayList();
        for (DiscretizedScale phoneticScale: inputScales) {
            String parentKey = phoneticScale.getMeasure().getUnitName();
            result.add(parentsToFeatures.get(parentKey));
        }
        return result;
    }

    private Table<ScaleFeature, SurfaceRangeFeature, Constraint> buildSubmappingToConstraintTable() {
        Table<ScaleFeature,SurfaceRangeFeature,Constraint> result = HashBasedTable.create();
        for (DiscretizedScale phoneticScale: inputScales) {
            String parentKey = phoneticScale.getMeasure().getUnitName();
            for (SurfaceRangeFeature surfaceRangeFeature: parentsToFeatures.get(parentKey)) {
                double surfaceDoubleValue = surfaceRangeFeature.getRelativeValue();
                for (ScaleFeature scaleFeature: phoneticScale.getValues()) {
                    double phoneticDoubleValue = scaleFeature.getRelativeValue();
                    double addedBias = calculateBias(surfaceDoubleValue,phoneticDoubleValue);
                    Constraint toAdd = new VowelCueConstraint(addedBias, scaleFeature, surfaceRangeFeature);
                    System.out.println("Created constraint " +toAdd);
                    result.put(scaleFeature,surfaceRangeFeature,toAdd);
                }
            }
        }
        return result;
    }

    private double calculateBias(double surfaceDoubleValue, double phoneticDoubleValue) {
        //return 4 * Math.pow(surfaceDoubleValue - phoneticDoubleValue, 2.0);
        return 4 * Math.abs(surfaceDoubleValue - phoneticDoubleValue);
    }

    protected VowelPfSfGen(Level leftLevel, Level rightLevel) {
        super(leftLevel, rightLevel);
    }

    @Override
    protected Function<PhoneticValueForm, SubCandidateSet> getRightFunction() {
        return input -> subCandidatesPerInput.getObject(input.getContents());
    }

    @Override
    public ConstraintArrayList getAssociatedConstraints(FormMapping mapping) {
        return mappingToConstraintArrayList.get(mapping);
    }
}
