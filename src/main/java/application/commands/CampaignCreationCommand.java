package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;
import application.models.Campaign;

import java.util.UUID;

public class CampaignCreationCommand implements Command {
    CommandFramework framework;
    ICampaignDataSource dataSource;

    public CampaignCreationCommand(CommandFramework fr, ICampaignDataSource src){
        framework = fr;
        dataSource = src;
    }

    @Override
    public boolean execute() {
        UUID id = framework.getId();
        String name = framework.getString("Campaign Name");
        int season = framework.getInt("Year") * 4;
        season += framework.getInt("Season", 1, 4);

        Campaign campaign = Campaign.createCampaign(id, name, season);
        return dataSource.addCampaign(campaign);
    }
}
