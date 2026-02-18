package application.commands;

import application.terminal.Command;
import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;

public class HelpCommand implements Command {
    CommandFramework framework;

    public HelpCommand(CommandFramework framework) {
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
