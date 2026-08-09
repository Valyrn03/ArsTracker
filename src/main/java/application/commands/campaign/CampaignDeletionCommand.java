package application.commands.campaign;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;
import application.data.IDataSource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CampaignDeletionCommand implements Command {
    CommandFramework framework;
    IDataSource dataSource;

    public CampaignDeletionCommand(CommandFramework framework, IDataSource dataSource){
        this.framework = framework;
        this.dataSource = dataSource;
    }

    @Override
    public boolean execute() {
        if(framework.getActiveCampaign().isEmpty()){
            log.error("Campaign already removed?");
            return true;
        }
        framework.put("WARNING: This will remove all covenants and characters associated with this campaign.\nAre you sure you want to delete campaign \"%s\"?", framework.getActiveCampaign().get().getName());
        boolean decision = framework.getOptionsIndex(List.of(new String[]{"Yes", "No"})) == 0;

        if(decision){
            return dataSource.deleteCampaign(framework.getActiveCampaign().get());
        }else{
            return true;
        }
    }
}
