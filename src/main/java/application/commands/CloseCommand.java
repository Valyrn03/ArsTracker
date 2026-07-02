package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.*;
import application.models.ArsCharacter;
import application.models.Campaign;
import application.models.ArsCharacter;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;

/*
Safely closes the program, saving all changes (if changes were made)

Should be the only command to require all 3 data sources, so for the ease of readability the instances of each are formed in the constructor
 */
@Slf4j
public class CloseCommand implements Command {
    CommandFramework framework;
    ICampaignDataSource campaignDataSource;
    ICovenantDataSource covenantDataSource;
    ICharacterDataSource characterDataSource;

    public CloseCommand(CommandFramework framework, IDataSource dataSource){
        this.framework = framework;
        this.campaignDataSource = new CampaignDataSource(dataSource);
        this.covenantDataSource = new CovenantDataSource(dataSource);
        this.characterDataSource = new CharacterDataSource(dataSource);
    }

    @Override
    public boolean execute() {
        for(Campaign campaign : framework.getAccessedCampaigns()){
            log.info("Saving campaign {}", campaign.id);
            for(Covenant covenant : campaign.accessedCovenants){
                log.info("\tSaving covenant {}", covenant.getName());

                for(ArsCharacter character : covenant.accessedCharacters){
                    log.info("\tSaving character {}", character.getName());

                    characterDataSource.updateCharacter(character);
                }

                covenantDataSource.updateCovenant(covenant);
            }
            campaignDataSource.updateCampaign(campaign);
        }

        log.debug("Exiting Program");
        return false;
    }

    @Override
    public String name() {
        return "CloseCommand";
    }
}
