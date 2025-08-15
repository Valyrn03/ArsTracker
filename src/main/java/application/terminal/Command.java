package application.terminal;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public TextIO source;
    private TextTerminal terminal;

    public Command(TextIO io){
        source = io;
        terminal = source.getTextTerminal();
    }

    public abstract boolean execute();

    int getInt(String prompt){
        return source.newIntInputReader().read(prompt + " >");
    }

    int getOptions(List<String> options){
        terminal.println("Choose one of the following options:");
        for(int i = 0; i < options.size(); i++){
            terminal.printf("\t%d: %s\n", i, options.get(i));
        }
        return source.newIntInputReader().read(" >");
    }

    String getString(String prompt){
        return source.newStringInputReader().read(prompt + " >");
    }
}
