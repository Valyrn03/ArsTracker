package application.commands;

import application.Command;
import application.CommandFramework;

public class DeleteCharacterCommand implements Command {
    CommandFramework framework;

    public DeleteCharacterCommand(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
