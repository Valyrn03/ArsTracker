package application.terminal;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.util.List;

public class CommandFramework {
    public TextIO source;
    private TextTerminal terminal;

    public CommandFramework(TextIO io){
        source = io;
        terminal = source.getTextTerminal();
    }

    public int getInt(String prompt){
        return source.newIntInputReader().read(prompt + " >");
    }

    public int getOptions(List<String> options){
        terminal.println("Choose one of the following options:");
        for(int i = 0; i < options.size(); i++){
            terminal.printf("\t%d: %s\n", i, options.get(i));
        }
        return source.newIntInputReader().read(" >");
    }

    public String getString(String prompt){
        return source.newStringInputReader().read(prompt + " >");
    }

    public void put(String prompt, Object... args){
        terminal.println(String.format(prompt, args));
    }

    public void put(List<Object> list){
        for(Object obj : list){
            terminal.println(obj.toString());
        }
    }
}
