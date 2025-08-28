package application.commands;

import org.beryx.textio.TextIO;

public class HelpCommand extends application.terminal.Command {
    public HelpCommand(TextIO io) {
        super(io);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
