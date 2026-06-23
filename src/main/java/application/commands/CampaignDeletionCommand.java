package application.commands;

import application.Command;
import application.CommandFramework;

public class CampaignDeletionCommand implements Command {
    CommandFramework framework;

    public CampaignDeletionCommand(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public String name() {
        return "CampaignDeletionCommand";
    }
}
