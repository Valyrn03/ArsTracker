package application.commands;

import org.beryx.textio.TextIO;

public class CharacterQueryCommand extends application.terminal.Command {
    public CharacterQueryCommand(TextIO io) {
        super(io);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
