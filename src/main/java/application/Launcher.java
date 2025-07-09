package application;

import application.characters.Character;
import application.characters.CharacterFeature;
import application.displays.LandingPage;
import application.terminal.CharacterCreator;
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
    CharacterSelector controller;
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
        }else if(userInput[0].equals("openGUI")){
            return ControlState.GUI;
        }else if(userInput[0].equals("close")){
            return ControlState.CLOSE;
        }else if(userInput[0].equals("list")){
            characters = database.query("");
            terminal.printf("Characters Loaded!\n");
            return ControlState.LIST;
        }else if(userInput[0].equals("select")){
            String characterName = command.substring(7);
            execute(new CharacterSelector(source, characterName, characters));
            return ControlState.SELECT;
        }else if(userInput[0].equals("create character")){
            return ControlState.CREATE_CHARACTER;
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
