package application.commands;

import org.beryx.textio.TextIO;

public class CloseCommand extends application.terminal.Command {
    public CloseCommand(TextIO io) {
        super(io);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
