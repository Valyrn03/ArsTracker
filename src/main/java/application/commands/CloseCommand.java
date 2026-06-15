package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.DataSource;
import application.models.ArsCharacter;
import application.models.Campaign;
import application.models.ArsCharacter;
import lombok.extern.slf4j.Slf4j;

/*
Safely closes the program, saving all changes (if changes were made)
 */
@Slf4j
public class CloseCommand implements Command {
    CommandFramework framework;
    DataSource dataSource;

    public CloseCommand(CommandFramework framework, DataSource dataSource){
        this.framework = framework;
        this.dataSource = dataSource;
    }

    @Override
    public boolean execute() {
        for(Campaign campaign : framework.getAccessedCampaigns()){
            log.info("Saving campaign {}", campaign.id);
            for(ArsCharacter character : campaign.accessedCharacters){
                log.info("\tSaving character {}", character.getId());

                dataSource.updateCharacter(character);
            }
            dataSource.updateCampaign(campaign);
        }

        dataSource.close();
        return true;
    }
}
