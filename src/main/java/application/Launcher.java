package application;

import application.characters.Character;
import application.displays.LandingPage;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;

import static application.utils.DatabaseFunctions.connectCharacterDatabase;

public class Launcher {
    static String[] argv;
    static ArrayList<Character> characters;
    public static void main(String[] args){
        argv = args;
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();
        textIO.getTextTerminal().println("Type \"help\" to get a list of commands");
        characters = connectCharacterDatabase();

        boolean result = controlInput(textIO);
        while(result){
            result = controlInput(textIO);
        }

        characters = null;
    }

    public static boolean controlInput(TextIO textIO){
        TextTerminal terminal = textIO.getTextTerminal();
        String command = textIO.newStringInputReader().read("> ");
        if(command.equals("help")){
            terminal.println("COMMANDS\n\tlist: list all characters\n\tselect [character/id]: select character by given name or id\n\tcreate: Create new character\n\topenGUI: Open GUI");
            return true;
        }else if(command.equals("openGUI")){
            LandingPage.launch(LandingPage.class, argv);
        }else if(command.equals("close")){
            return false;
        }else if(command.equals("list")){
            list(terminal);
            return true;
        }
        terminal.println("Command Not Found, Please Try Again");
        return true;
    }

    public static void list(TextTerminal terminal){
        if(characters.size() == 0){
            terminal.println("No Characters Found, use the \"create\" command to begin character creation");
            return;
        }
        terminal.println("Characters:");
        for(Character character : characters){
            terminal.printf("\t%s\n", character.getName());
        }
        terminal.println("Use the \"select\" command in order to choose a character");
    }
}
