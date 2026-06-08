package application.commands;

import application.Command;
import application.CommandFramework;

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
