package application.commands;

import application.Command;
import application.CommandFramework;

public class ReturnCommand implements Command {
    CommandFramework framework;

    public ReturnCommand(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute(){
        return false;
    }
}
