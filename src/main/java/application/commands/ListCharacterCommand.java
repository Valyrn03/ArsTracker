package application.commands;

import application.Command;
import application.CommandFramework;

public class ListCharacterCommand implements Command {
    CommandFramework framework;

    public ListCharacterCommand(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public String name() {
        return "ListCharacterCommand";
    }
}
