package application.commands.covenant;

import application.Command;
import application.CommandFramework;
import application.data.ICovenantDataSource;
import application.models.Covenant;
import application.models.CovenantFeature;
import application.models.enums.Art;
import application.models.enums.Tribunal;

import java.util.*;

public class CovenantCreationCommand implements Command {
    CommandFramework framework;
    ICovenantDataSource dataSource;
    final static List<String> seasons = Arrays.asList(new String[]{"winter", "spring", "summer", "autumn"});
    List<CovenantFeature> features;

    public CovenantCreationCommand(CommandFramework fr, ICovenantDataSource dataSource){
        this.framework = fr;
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
        features = new ArrayList<>();
        int cont = 0;
        while(cont == 0){
            framework.getOptions(List.of(new String[]{"Boon", "Hook"}));

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
        return dataSource.addCovenant(covenant, framework.getActiveCampaign().get());
    }

    /*
    Returns -1 to create a new feature
    Returns 0 if the user wants to be done
    Returns 1 if a new feature is added
     */
    private int addFeature(){

    }
}
