package application.commands.campaign;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;

import java.util.ArrayList;
import java.util.List;

public class CampaignCreationCommand implements Command {
    CommandFramework framework;
    ICampaignDataSource dataSource;
    List<String> seasons = new ArrayList<>(List.of(new String[]{"Winter", "Spring", "Summer", "Autumn"}));

    public CampaignCreationCommand(CommandFramework fr, ICampaignDataSource src){
        framework = fr;
        dataSource = src;
    }

    @Override
    public boolean execute() {
        String name = framework.getString("Campaign Name");
        int season = framework.getInt("Year") * 4;
        season += framework.getOptionsIndex(seasons);

        boolean result = dataSource.addCampaign(name, season);
        if(result){
            framework.put("Campaign \"%s\" has been created!", name);
        }else{
            framework.put("Failed to create campaign");
        }
        return result;
    }
}
