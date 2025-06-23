package application;

import application.displays.LandingPage;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class Launcher {
    static String[] argv;
    public static void main(String[] args){
        argv = args;
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();

        String command = textIO.newStringInputReader().read("Input \"help\" for a list of commands\n> ");

        while(controlInput(textIO, command)){
            command = textIO.newStringInputReader().read("> ");
        }
        LandingPage.launch(LandingPage.class, args);
    }

    public static boolean controlInput(TextIO textIO, String command){
        TextTerminal terminal = textIO.getTextTerminal();
        if(command.equals("help")){
            terminal.println("COMMANDS\n\tlist: list all characters\n\tselect [character/id]: select character by given name or id\n\tcreate: Create new character\n\topenGUI: Open GUI");
            return true;
        }else if(command.equals("openGUI")){
            LandingPage.launch(LandingPage.class, argv);
        }
        return false;
    }
}
