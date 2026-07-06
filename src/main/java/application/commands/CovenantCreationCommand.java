package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.ICovenantDataSource;
import application.models.Covenant;
import application.models.enums.Art;
import application.models.enums.Tribunal;

import java.util.*;

public class CovenantCreationCommand implements Command {
    CommandFramework framework;
    ICovenantDataSource dataSource;
    final static List<String> seasons = Arrays.asList(new String[]{"winter", "spring", "summer", "autumn"});

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

        int year = framework.getInt("Date of Establishment>");
        int season = framework.getOptionsIndex(seasons.stream()) - 1;
        season += year * 4;

        Map<Art, Integer> map = new HashMap<>();
        for(Art art : Art.values()){
            map.put(art, framework.getInt("Amount of {} Vis", art.name()));
        }

        Covenant covenant = Covenant.buildCovenant(covenantName, tribunal, season, map);

        //Now need to add hooks and boons...

        return dataSource.addCovenant(covenant);
    }

    @Override
    public String name() {
        return "CovenantCreationCommand";
    }
}
