package application;

import application.characters.Character;
import application.commands.CharacterCreator;
import application.displays.LandingPage;
import application.commands.CharacterSelector;
import application.terminal.Command;
import application.terminal.DatabaseFunction;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;

public class Launcher {
    public static void main(String[] args){
        Launcher launcher = new Launcher(args);
        launcher.coreLoop();
    }

    String[] args;
    DatabaseFunction database;
    TextIO source;
    TextTerminal terminal;
    ArrayList<Character> characters;

    public Launcher(String[] arg){
        args = arg;
        source = TextIoFactory.getTextIO();
        source.getTextTerminal().println("Type \"help\" to get a list of commands");
        database = new DatabaseFunction();
        ArrayList<Character> characters = database.query("");
        terminal = source.getTextTerminal();
    }

    public void coreLoop(){
        ControlState result = controlInput();
        while(result != ControlState.CLOSE){
            if(result == ControlState.GUI){
                LandingPage.launch(LandingPage.class, args);
            }else if(result == ControlState.CREATE_CHARACTER){
                database.add(createCharacter());
            }
            result = controlInput();
        }
    }

    private Character createCharacter() {
        return null;
    }

    public boolean execute(Command command){
        return command.execute();
    }

    public ControlState controlInput(){
        String command = source.newStringInputReader().read("> ");
        String[] userInput = command.split(" ");
        if(userInput.length == 0){
            return ControlState.CLOSE;
        }else if("help".equals(userInput[0])){
            terminal.println("COMMANDS\n\tlist: list all characters\n\tselect [character]: select character by given name\n\tcreate \n\t\tcreate character: Create new character\n\topenGUI: Open GUI");
            return ControlState.HELP;
        }else if("openGUI".equals(userInput[0])){
            return ControlState.GUI;
        }else if("close".equals(userInput[0])){
            return ControlState.CLOSE;
        }else if("list".equals(userInput[0])){
            characters = database.query("");
            terminal.printf("Characters Loaded!\n");
            return ControlState.LIST;
        }else if("select".equals(userInput[0])){
            String characterName = command.substring(7);
            execute(new CharacterSelector(source, characterName, characters));
            return ControlState.SELECT;
        }else if("create".equals(userInput[0])){
            if("character".equals(userInput[1])){
                execute(new CharacterCreator(source, characters));
                return ControlState.CREATE_CHARACTER;
            }
        }
        terminal.println("Command Not Found, Please Try Again");
        return ControlState.CLOSE;
    }

    public static enum ControlState {
        HELP,
        LIST,
        GUI,
        CREATE_CHARACTER,
        SELECT,
        CLOSE
    }
}
