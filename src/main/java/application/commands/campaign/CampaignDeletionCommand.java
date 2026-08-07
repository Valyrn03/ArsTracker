package application.commands.campaign;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;

import java.util.List;

public class CampaignDeletionCommand implements Command {
    CommandFramework framework;
    ICampaignDataSource dataSource;

    public CampaignDeletionCommand(CommandFramework framework, ICampaignDataSource dataSource){
        this.framework = framework;
        this.dataSource = dataSource;
    }

    @Override
    public boolean execute() {
        if(framework.getActiveCampaign().isEmpty()){
            return true;
        }
        framework.put("WARNING: This will remove all covenants and characters associated with this campaign.\nAre you sure you want to delete campaign \"%s\"?", framework.getActiveCampaign().get().getName());
        int decision = framework.getOptionsIndex(List.of(new String[]{"Yes", "No"}));

        if(decision == 0){
            boolean result = dataSource.deleteCampaign(framework.getActiveCampaign().get());
            if(result){
                framework.put("Deleted Campaign \"%s\"", framework.getActiveCampaign().get().getName());
            }else{
                framework.put("Failed to delete campaign");
            }
            return result;
        }else{
            return true;
        }
    }
}
