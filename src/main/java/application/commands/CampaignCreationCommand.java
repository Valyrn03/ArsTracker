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
        String name = framework.getString("Campaign Name");
        int season = framework.getInt("Year") * 4;
        season += framework.getIntLimited("Season", 1, 4);

        return dataSource.addCampaign(name, season);
    }
}
