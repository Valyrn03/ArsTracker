package application.commands;

import application.terminal.Command;
import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;

public class LaunchGUICommand implements Command {
    CommandFramework framework;
    public LaunchGUICommand(CommandFramework framework) {
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
