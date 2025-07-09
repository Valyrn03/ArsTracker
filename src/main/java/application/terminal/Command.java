package application.terminal;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

public abstract class Command {
    public TextIO source;
    private TextTerminal terminal;

    public Command(TextIO io){
        source = io;
        terminal = source.getTextTerminal();
    }

    public abstract boolean execute();

    private int getInt(String prompt){
        return source.newIntInputReader().read(prompt + " >");
    }
}
