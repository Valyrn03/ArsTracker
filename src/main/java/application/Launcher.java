package application;

import application.characters.Character;
import application.commands.*;
import application.displays.LandingPage;
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
    Map<String, Command> commands;

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
        commands.put("help", new HelpCommand(source));
        commands.put("openGUI", new LaunchGUICommand(source));
        commands.put("list", new CharacterQueryCommand(source));
        commands.put("select", new CharacterSelector(source, characters));
        commands.put("create", new CharacterCreator(source, characters));
        commands.put("close", new CloseCommand(source));
    }

    public void coreLoop(){
        boolean result = false;
        do{
            result = controlInput();
        }while (result);
    }

    private Character createCharacter() {
        return null;
    }

    public boolean execute(Command command){
        return command.execute();
    }

    public boolean controlInput(){
        String commandInput = source.newStringInputReader().read("> ");
        String[] userInput = commandInput.split(" ");
        if(userInput.length == 0){
            Command command = commands.get("close");
            return command.execute();
        }
        Command command = commands.get(userInput[0]);
        return command.execute();
    }
}
