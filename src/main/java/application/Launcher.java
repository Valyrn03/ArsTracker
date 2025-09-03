package application;

import application.characters.Character;
import application.commands.*;
import application.terminal.Command;
import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Launcher {
    public static void main(String[] args){
        Launcher launcher = new Launcher(args);
        launcher.coreLoop();
    }

    String[] args;
    CommandFramework framework;
    TextTerminal terminal;
    ArrayList<Character> characters;
    Map<String, Command> commands;

    public Launcher(String[] arg){
        args = arg;
        TextIO source = TextIoFactory.getTextIO();
        source.getTextTerminal().println("Type \"help\" to get a list of commands");
        framework = new CommandFramework(source);
        ArrayList<Character> characters = new ArrayList<>();
        terminal = source.getTextTerminal();

        commands = new HashMap<>();
        addLauncherCommands();
    }

    private void addLauncherCommands() {
        commands.put("help", new HelpCommand(framework));
        commands.put("openGUI", new LaunchGUICommand(framework));
        commands.put("list", new CharacterQueryCommand(framework));
        commands.put("select", new CharacterSelector(framework, characters));
        commands.put("create", new CharacterCreator(framework, characters));
        commands.put("close", new CloseCommand(framework));
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
        String commandInput = framework.getString(" >");
        String[] userInput = commandInput.split(" ");
        if(userInput.length == 0){
            Command command = commands.get("close");
            return command.execute();
        }
        Command command = commands.get(userInput[0]);
        return command.execute();
    }
}
