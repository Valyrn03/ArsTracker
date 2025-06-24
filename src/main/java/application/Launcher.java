package application;

import application.characters.Character;
import application.displays.LandingPage;
import application.utils.CommandController;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;

import static application.utils.DatabaseFunctions.connectCharacterDatabase;

public class Launcher {
    static String[] argv;
    public static void main(String[] args){
        argv = args;
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();
        textIO.getTextTerminal().println("Type \"help\" to get a list of commands");
        ArrayList<Character> characters = connectCharacterDatabase();
        CommandController controller = new CommandController(characters);

        int result = controlInput(textIO, controller);
        while(result != 0){
            if(result == 2){
                LandingPage.launch(LandingPage.class, argv);
            }
            result = controlInput(textIO, controller);
        }

        characters = null;
    }

    public static int controlInput(TextIO textIO, CommandController controller){
        TextTerminal terminal = textIO.getTextTerminal();
        String command = textIO.newStringInputReader().read("> ");
        if(command.equals("help")){
            terminal.println("COMMANDS\n\tlist: list all characters\n\tselect [character/id]: select character by given name or id\n\tcreate: Create new character\n\topenGUI: Open GUI");
            return 1;
        }else if(command.equals("openGUI")){
            return 2;
        }else if(command.equals("close")){
            return 0;
        }else if(command.equals("list")){ 
            terminal.printf("%s\n", controller.loadCharacters());
            return 1;
        }else if(command.startsWith("select")){
            String characterName = command.substring(7);
            terminal.println(controller.selectCharacter(characterName));
            return 1;
        }
        terminal.println("Command Not Found, Please Try Again");
        return 1;
    }
}
