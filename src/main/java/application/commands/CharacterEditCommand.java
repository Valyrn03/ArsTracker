package application.commands;

import application.Command;
import application.CommandFramework;

public class CharacterEditCommand implements Command {
    CommandFramework framework;

    public CharacterEditCommand(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public String name() {
        return "CharacterEditCommand";
    }
}
