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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    Map<String, ControlState> commands;

    public Launcher(String[] arg){
        args = arg;
        source = TextIoFactory.getTextIO();
        source.getTextTerminal().println("Type \"help\" to get a list of commands");
        database = new DatabaseFunction();
        ArrayList<Character> characters = new ArrayList<>();
        terminal = source.getTextTerminal();

        commands = new HashMap<>();
        addLauncherCommands();
    }

    private void addLauncherCommands() {
        commands.put("help", ControlState.HELP);
        commands.put("openGUI", ControlState.GUI);
        commands.put("list", ControlState.LIST);
        commands.put("select", ControlState.SELECT);
        commands.put("create", ControlState.CREATE);
        commands.put("close", ControlState.CLOSE);
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
        }
        ControlState state = commands.get(userInput[0]);
        if(state == null){
            terminal.println("Command Not Found, Please Try Again");
        }else if(state == ControlState.HELP){
            terminal.println("COMMANDS\n\tlist: list all characters\n\tselect [character]: select character by given name\n\tcreate \n\t\tcreate character: Create new character\n\topenGUI: Open GUI");
        }else if(state == ControlState.LIST){
            //todo
            characters = new ArrayList<>();
            terminal.printf("Characters Loaded!\n");
        }else if(state == ControlState.SELECT){
            String characterName = String.join(" ", Arrays.copyOfRange(userInput, 1, userInput.length));
            execute(new CharacterSelector(source, characterName, characters));
        }else if(state == ControlState.CREATE){
            if("character".equals(userInput[1])){
                execute(new CharacterCreator(source, characters));
            }
        }
        return state;
    }

    public static enum ControlState {
        HELP,
        LIST,
        GUI,
        CREATE_CHARACTER,
        SELECT,
        CLOSE,
        CREATE
    }
}
