package application.commands.covenant;

import application.Command;
import application.CommandFramework;
import application.data.ICovenantDataSource;
import application.models.Covenant;
import application.models.CovenantFeature;
import application.models.Feature;
import application.models.enums.Art;
import application.models.enums.Tribunal;
import application.utils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CovenantCreationCommand implements Command {
    CommandFramework framework;
    ICovenantDataSource dataSource;
    final static List<String> seasons = Arrays.asList(new String[]{"winter", "spring", "summer", "autumn"});
    List<CovenantFeature> features;

    public CovenantCreationCommand(CommandFramework fr, ICovenantDataSource dataSource){
        this.framework = fr;
        this.dataSource = dataSource;
    }

    /*
    In order to create a new covenant, need to decide the following:
        The name
        The tribunal
        Which season the covenant was built in
        Current Vis stores
            Vis sources?
        Books and lab texts that belong to the covenant <- skip for now
        Hooks and Boons
     */
    @Override
    public boolean execute() {
        String covenantName = framework.getString("Name");
        String tribunal = framework.getOptions(Arrays.stream(Tribunal.values()).map(Tribunal::name));

        int year = framework.getInt("Year of Establishment>");
        int season = framework.getOptionsIndex(seasons.stream()) - 1;
        season += year * 4;

        //HOOKS AND BOONS
        List<CovenantFeature> loadedHooks = dataSource.loadFeatures(CovenantFeature.FeatureType.HOOK);
        List<CovenantFeature> loadedBoons = dataSource.loadFeatures(CovenantFeature.FeatureType.BOON);
        features = new ArrayList<>();
        String input = framework.getOptions(List.of(new String[]{"Boon", "Hook"}));
        while(!"Finished".equals(input)){
            if("boon".equalsIgnoreCase(input)){
                chooseFeature(loadedBoons, true).ifPresent(features::add);
            }else{
                chooseFeature(loadedHooks, false).ifPresent(features::add);
            }

            input = framework.getOptions(List.of(new String[]{"Boon", "Hook", "Finished"}), "Add Boon or Hook");
        }

        while (utils.calculateCovenantFeatureBalance(features) != 0){
            if(utils.calculateCovenantFeatureBalance(features) < 0){
                String option = framework.getOptions(List.of(new String[]{"Add Hook", "Remove Boon"}), "Too many boons have been selected, need to add an additional hook or remove a boon");

                if("Add Hook".equals(option) && utils.calculateCovenantFeatureBalance(features) > -3){
                    chooseFeature(getMinorFeatures(loadedHooks), false).ifPresent(features::add);
                }else if("Add Hook".equals(option)){
                    chooseFeature(loadedHooks, false).ifPresent(features::add);
                }else{
                    chooseFeatureWithoutCreation(getSelectedBoons()).ifPresent(features::remove);
                }
            }else{
                String option = framework.getOptions(List.of(new String[]{"Add Boon", "Remove Hook"}), "Too many hooks have been selected, need to add an additional boon or remove a hook");

                if("Add Boon".equals(option) && utils.calculateCovenantFeatureBalance(features) < 3){
                    chooseFeature(getMajorFeatures(loadedBoons), true).ifPresent(features::add);
                }else if("Add Boon".equals(option)){
                    chooseFeature(loadedBoons, true).ifPresent(features::add);
                }else{
                    chooseFeatureWithoutCreation(getSelectedHooks()).ifPresent(features::remove);
                }
            }
        }

        //VIS
        Map<Art, Integer> map = new HashMap<>();
        for(Art art : Art.values()){
            map.put(art, framework.getInt("Amount of {} Vis", art.name()));
        }

        //PUTTING IT ALL TOGETHER
        Covenant covenant = Covenant.buildCovenant(covenantName, tribunal, season, map);

        if(framework.getActiveCampaign().isEmpty()){
            return false;
        }

        if(!dataSource.addCovenant(covenant, framework.getActiveCampaign().get())){
            log.error("Failed to add covenant to DB!");
            return false;
        }

        for(CovenantFeature feature : features){
            covenant.addFeature(feature);
            dataSource.addFeatureToCovenant(covenant, feature);
        }

        return true;
    }

    private Optional<CovenantFeature> chooseFeature(List<CovenantFeature> loadedFeatures, boolean isBoon){
        List<String> options = new ArrayList<>(Collections.singleton("Create New Feature"));
        options.addAll(loadedFeatures.stream().map(CovenantFeature::toStringShortened).toList());
        int index = framework.getOptionsIndex(options);

        if(index == 0){
            CovenantFeature feature = createFeature(isBoon);

            boolean add = "yes".equals(framework.getOptions(List.of(new String[]{"Yes", "No"}), "Confirm you want to choose this feature"));

            if(add){
                return Optional.of(feature);
            }
        }else{
            boolean add = "yes".equals(framework.getOptions(List.of(new String[]{"Yes", "No"}), "Confirm you want to choose this feature"));

            if(add){
                return Optional.of(features.get(index - 1));
            }
        }

        return Optional.empty();
    }

    private Optional<CovenantFeature> chooseFeatureWithoutCreation(List<CovenantFeature> listOfFeatures){
        List<String> options = listOfFeatures.stream().map(CovenantFeature::toStringShortened).toList();
        int index = framework.getOptionsIndex(options);

        boolean add = "yes".equals(framework.getOptions(List.of(new String[]{"Yes", "No"}), "Confirm you want to choose this feature"));

        if(add){
            return Optional.of(features.get(index));
        }else{
            return Optional.empty();
        }
    }

    private CovenantFeature createFeature(boolean isBoon){
        String name = framework.getString("New Feature Name");
        boolean isMajor = framework.getOptionsIndex(List.of(new String[]{"Major", "Minor"})) == 0;
        String description = framework.getString("Description");

        CovenantFeature feature = new CovenantFeature();
        feature.setName(name);
        feature.setType(isBoon);
        feature.setMajor(isMajor);
        feature.setDescription(description);

        dataSource.saveCovenantFeature(feature);
        return feature;
    }

    private List<CovenantFeature> getMajorFeatures(List<CovenantFeature> listOfFeatures){
        return listOfFeatures.stream().filter(CovenantFeature::isMajor).toList();
    }

    private List<CovenantFeature> getMinorFeatures(List<CovenantFeature> listOfFeatures){
        return listOfFeatures.stream().filter(feature -> {return !feature.isMajor();}).toList();
    }

    private List<CovenantFeature> getSelectedBoons(){
        return features.stream().filter(feature -> {return CovenantFeature.FeatureType.BOON.equals(feature.getType());}).toList();
    }

    private List<CovenantFeature> getSelectedHooks(){
        return features.stream().filter(feature -> {return CovenantFeature.FeatureType.HOOK.equals(feature.getType());}).toList();
    }
}
