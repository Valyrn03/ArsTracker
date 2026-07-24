package application.commands.covenant;

import application.Command;
import application.CommandFramework;
import application.data.ICovenantDataSource;

public class CovenantSelectionCommand implements Command {
    CommandFramework framework;
    ICovenantDataSource dataSource;

    public CovenantSelectionCommand(CommandFramework fr, ICovenantDataSource ds){
        framework = fr;
        dataSource = ds;
    }
    @Override
    public boolean execute() {
        return false;
    }
}
