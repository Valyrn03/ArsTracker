package application.commands;

import application.Command;
import application.CommandFramework;

public class CharacterSelectionCommand implements Command {
    CommandFramework framework;

    public CharacterSelectionCommand(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
