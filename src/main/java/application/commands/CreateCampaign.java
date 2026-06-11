package application.commands;

import application.Command;
import application.CommandFramework;

/*
For a given campaign, the requirements are a name and the starting season. Each one must also contain at least one covenant.
 */
public class CreateCampaign implements Command {
    CommandFramework framework;

    public CreateCampaign(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute(){
        return false;
    }
}
