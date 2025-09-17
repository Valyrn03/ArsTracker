package application.commands;

import application.terminal.Command;
import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;

public class CharacterQueryCommand implements Command {
    CommandFramework framework;

    public CharacterQueryCommand(CommandFramework source) {
        framework = source;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
