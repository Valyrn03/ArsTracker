package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.DataSource;
import application.data.ICampaignDataSource;
import application.models.Campaign;

import java.util.List;

public class SelectCampaignCommand implements Command {
    CommandFramework framework;
    ICampaignDataSource dataSource;

    public SelectCampaignCommand(CommandFramework framework, ICampaignDataSource dataSource){
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

    @Override
    public String name() {
        return "SelectCampaignCommand";
    }
}
