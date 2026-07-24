package application.gui;

import application.Command;
import application.CommandFramework;

public class LaunchGUI implements Command {
    CommandFramework framework;

    public LaunchGUI(CommandFramework framework){
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
