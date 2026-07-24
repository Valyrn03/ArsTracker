package application.commands.covenant;

import application.Command;
import application.CommandFramework;
import application.commands.campaign.ShowCampaignCommand;
import application.data.ICovenantDataSource;
import application.models.ArsCharacter;
import application.models.Covenant;
import application.models.enums.Art;

public class ShowCovenantCommand implements Command {
    CommandFramework framework;
    ICovenantDataSource covenantDataSource;

    public ShowCovenantCommand(CommandFramework fr, ICovenantDataSource cds){
        framework = fr;
        covenantDataSource = cds;
    }

    private String capitalization(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public boolean execute() {
        if(framework.getActiveCovenant().isEmpty()){
            return false;
        }

        Covenant covenant = framework.getActiveCovenant().get();
        framework.put("Name: %s", covenant.getName());
        framework.put("Tribunal: %s", covenant.getTribunal());

        int currentSeason = framework.getActiveCampaign().orElseThrow().getCurrentSeason();
        framework.put("Age: %d years, %d seasons", (currentSeason - covenant.getEstablishmentSeason()) / 4, (currentSeason - covenant.getEstablishmentSeason()) % 4);

        if(!covenant.getBooks().isEmpty()){
            framework.put("Books:");
            framework.put(covenant.getBooks().stream().map(Object::toString));
        }

        framework.put("Vis Stores:");
        for(Art art : Art.values()){
            framework.put("\t%s: %d", capitalization(art.name()), covenant.getVis(art));
        }

        covenant.updateCharacters(covenantDataSource.loadCovenantCharacters(covenant));
        if(!covenant.getPlayerCharacters().isEmpty()){
            framework.put("Characters: ");
            framework.put(covenant.getPlayerCharacters().stream().map(ArsCharacter::getName));
        }

        if(!covenant.getFeatures().isEmpty()){
            framework.put("Features: ");
            framework.put(covenant.getFeatures().stream().map(Object::toString));
        }

        return true;
    }
}
