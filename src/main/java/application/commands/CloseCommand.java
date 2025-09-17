package application.commands;

import application.terminal.Command;
import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;

public class CloseCommand implements Command {
    CommandFramework framework;
    public CloseCommand(CommandFramework framework) {
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
