package application.commands;

import application.Command;
import application.CommandFramework;
import application.DataSource;
import application.models.Campaign;

import java.util.List;

public class CampaignSelectionCommand implements Command {
    CommandFramework framework;
    DataSource dataSource;

    public CampaignSelectionCommand(CommandFramework framework, DataSource dataSource){
        this.framework = framework;
        this.dataSource = dataSource;
    }

    @Override
    public boolean execute(){
        List<Campaign> campaigns = dataSource.getCampaigns();

        int selection = framework.getOptions(campaigns.stream().map(Campaign::getName));

        framework.setActiveCampaign(campaigns.get(selection));
        return true;
    }
}
