package application.commands;

import application.terminal.Command;
import application.terminal.CommandFramework;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class HelpCommand implements Command {
    CommandFramework framework;
    private final List<String> commands = Arrays.asList(
            new String[]{"help", "select", "openGUI", "list", "create campaign", "create character"}
    );

    public HelpCommand(CommandFramework framework) {
        this.framework = framework;
    }

    @Override
    public boolean execute() {
        log.info("Printing Commands, {} Found", commands.size());
        for(Object command: commands){
            framework.put(command.toString());
        }

        return true;
    }
}
