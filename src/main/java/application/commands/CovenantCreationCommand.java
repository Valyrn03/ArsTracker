package application.commands;

import application.Command;
import application.CommandFramework;

public class CovenantCreationCommand implements Command {
    CommandFramework framework;

    public CovenantCreationCommand(CommandFramework fr){
        this.framework = fr;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public String name() {
        return "CovenantCreationCommand";
    }
}
