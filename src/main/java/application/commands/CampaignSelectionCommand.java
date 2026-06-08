package application.commands;

import application.Command;
import application.CommandFramework;

public class CampaignSelectionCommand implements Command {
    CommandFramework framework;

    public CampaignSelectionCommand(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute(){
        return false;
    }
}
