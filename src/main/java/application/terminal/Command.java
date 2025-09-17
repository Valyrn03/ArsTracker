package application.terminal;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;
import java.util.List;

public interface Command {
    public boolean execute();
}
