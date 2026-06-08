package application;

import application.gui.LaunchGUI;
import application.models.Character;
import application.commands.*;
import application.terminal.HelpView;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ArsTrackerLauncher {
    public static void main(String[] args){
        ArsTrackerLauncher launcher = new ArsTrackerLauncher(args);
        log.info("{} Commands Loaded", launcher.addInitialCommands());
        launcher.coreLoop();
    }

    String[] args;
    CommandFramework framework;
    Map<String, Command> commands;

    public ArsTrackerLauncher(String[] arg){
        args = arg;
        TextIO source = TextIoFactory.getTextIO();
        source.getTextTerminal().println("Type \"help\" to get a list of commands");
        framework = new CommandFramework(source);

        commands = new HashMap<>();
    }

    /*
    Commands Always Loaded:
        Open GUI
        Close Program
        Help
     */
    public int addDefaultLauncherCommands() {
        commands.put("openGUI", new LaunchGUI(framework));
        commands.put("close", new CloseCommand(framework));
        commands.put("help", new HelpView(framework, commands.keySet()));
        return commands.size();
    }

    /*
    Commands loaded on booting the program:
        List known campaigns
        Select from listed campaigns
        Create new campaign
     */
    public int addInitialCommands(){
        commands.put("select", new CampaignSelectionCommand(framework));
        commands.put("create", new CreateCampaign(framework));
        return commands.size();
    }

    /*
    Commands available once a campaign is selected:
        List characters
        Select character
        Create new character
        Delete campaign
     */
    public int addCampaignCommands(){
        commands.entrySet().removeIf(entry -> !entry.getKey().equals("openGUI") && !entry.getKey().equals("close") && !entry.getKey().equals("help"));

        commands.put("back", new ReturnCommand(framework));
        commands.put("list", new ListCharacterCommand(framework));
        commands.put("select", new CharacterSelectionCommand(framework));
        commands.put("create", new CharacterCreationCommand(framework));
        commands.put("delete", new CampaignDeletionCommand(framework));

        return commands.size();
    }

    /*
    Commands available after selecting a character:
        Go back to campaign list
        Show/Print character
        Edit character
        Delete character
     */
    public int addCharacterCommands(){
        commands.entrySet().removeIf(entry -> !entry.getKey().equals("openGUI") && !entry.getKey().equals("close") && !entry.getKey().equals("help"));

        commands.put("back", new ReturnCommand(framework));
        commands.put("show", new CharacterOutputCommand(framework));
        commands.put("edit", new CharacterEditCommand(framework));
        commands.put("delete", new DeleteCharacterCommand(framework));

        return commands.size();
    }

    public void coreLoop(){
        boolean result = false;
        do{
            result = controlInput();
        }while (result);

        framework.put("Exiting...");
    }

    private Character createCharacter() {
        return null;
    }

    public boolean execute(Command command){
        return command.execute();
    }

    public boolean controlInput(){
        String commandInput = framework.getString("");
        String[] userInput = commandInput.split(" ");
        if(userInput.length == 0){
            log.info("Incorrect Command {}", commandInput);
            Command command = commands.get("close");
            return command.execute();
        }
        log.info("User Command:{}", userInput[0]);

        Command command = null;
        if(userInput.length > 1 && "create".equals(userInput[0])){
            if("character".equals(userInput[1])){
                command = commands.get("character");
            }else{
                command = commands.get("campaign");
            }
        }else{
            command = commands.get(userInput[0]);
        }
        if(command == null){
            log.info("\tNull Command");
        }
        return command.execute();
    }
}
