package application.commands.covenant;

import application.Command;
import application.CommandFramework;

public class CovenantDeletionCommand implements Command {
    CommandFramework framework;

    public CovenantDeletionCommand(CommandFramework fr){
        this.framework = fr;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
