package application.commands;

import application.Command;
import application.CommandFramework;

public class CharacterCreationCommand implements Command {
    CommandFramework framework;

    public CharacterCreationCommand(CommandFramework framework){
        this.framework = framework;
    }


    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public String name() {
        return "CharacterCreationCommand";
    }
}
