package application.commands;

import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;

public class CharacterQueryCommand implements application.terminal.Command {
    CommandFramework framework;

    public CharacterQueryCommand(CommandFramework source) {
        framework = source;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
