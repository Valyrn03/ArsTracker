package application.terminal;

import application.Command;
import application.CommandFramework;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class HelpView implements Command {
    CommandFramework framework;
    Set<String> commands;

    public HelpView(CommandFramework framework, Set<String> commands) {
        this.framework = framework;
        this.commands = commands;
    }

    @Override
    public boolean execute() {
        for(String command: commands){
            framework.put(command);
        }

        return true;
    }
}
