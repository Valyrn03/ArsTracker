package application.commands;

import org.beryx.textio.TextIO;

public class LaunchGUICommand extends application.terminal.Command {
    public LaunchGUICommand(TextIO io) {
        super(io);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
