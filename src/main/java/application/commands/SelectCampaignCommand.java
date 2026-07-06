package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.DataSource;
import application.data.ICampaignDataSource;
import application.models.Campaign;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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

        if(campaigns.isEmpty()){
            log.info("Empty, returning");
            framework.put("0 Campaigns Loaded");
            return true;
        }

        log.info("Choosing selection from {} options", campaigns.size());
        int selection = framework.getOptionsIndex(campaigns.stream().map(Campaign::getName));

        framework.setActiveCampaign(campaigns.get(selection));
        return true;
    }

    @Override
    public String name() {
        return "SelectCampaignCommand";
    }
}
