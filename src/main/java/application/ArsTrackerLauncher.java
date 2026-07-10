package application;

import application.data.CampaignDataSource;
import application.data.DataSource;
import application.data.IDataSource;
import application.data.MockDataSource;
import application.gui.LaunchGUI;
import application.commands.*;
import application.terminal.HelpView;
import application.utils.CharacterUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.WriterOutputStream;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ArsTrackerLauncher {
    public static void main(String[] args){
        log.info("RUNNING");
        Class<?> stream = WriterOutputStream.builder().getClass();
        log.info("{}", stream.getCanonicalName());
        ArsTrackerLauncher launcher = new ArsTrackerLauncher(args);
        log.info("{} Commands Loaded", launcher.addDefaultLauncherCommands());
        log.info("{} Commands Loaded", launcher.addInitialCommands());
        launcher.coreLoop();
    }

    String[] args;
    @Getter CommandFramework framework;
    @Getter Map<String, Command> commands;
    @Getter IDataSource dataSource;

    public ArsTrackerLauncher(String[] arg){
        args = arg;
        try{
            framework = new CommandFramework(TerminalBuilder.builder().system(true).build());
        }catch (IOException exp){
            log.error("Failed to open terminal with error {}", exp.getMessage());
        }
        dataSource = new DataSource();
        commands = new HashMap<>();
        assert addDefaultLauncherCommands() == 3;
    }

    private ArsTrackerLauncher(){

    }

    public static ArsTrackerLauncher getMockLauncher(Terminal io, List<String> ids){
        ArsTrackerLauncher launcher = new ArsTrackerLauncher();
        launcher.commands = new HashMap<>();
        launcher.dataSource = new MockDataSource();
        launcher.framework = new CommandFramework(io);

        assert launcher.addDefaultLauncherCommands() == 3;
        assert launcher.addInitialCommands() == 5;

        return launcher;
    }

    /*
    Commands Always Loaded:
        Open GUI
        Close Program
        Help
     */
    public int addDefaultLauncherCommands() {
        log.info("Adding default commands");
        commands.put("openGUI", new LaunchGUI(framework));
        commands.put("close", new CloseCommand(framework, dataSource));
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
        commands.put("select", new SelectCampaignCommand(framework, new CampaignDataSource(dataSource)));
        commands.put("create", new CampaignCreationCommand(framework, new CampaignDataSource(dataSource)));
        return commands.size();
    }

    /*
    Commands available once a campaign is selected:
        List covenants
        Select covenant
        Create new covenant
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
    Commands available once a covenant is selected:
        List characters
        Select character
        Create new character
        Delete covenant
     */
    public int addCovenantCommands(){
        commands.entrySet().removeIf(entry -> !entry.getKey().equals("openGUI") && !entry.getKey().equals("close") && !entry.getKey().equals("help"));

        commands.put("back", new ReturnCommand(framework));
        commands.put("list", new ListCharacterCommand(framework));
        commands.put("select", new CharacterSelectionCommand(framework));
        commands.put("create", new CharacterCreationCommand(framework));
        commands.put("delete", new CovenantDeletionCommand(framework));

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
        framework.put("In order to get the list of commands, type \"help\"");
        do{
            Command command = null;
            String user = framework.getString(">");
            if(user.isEmpty()){
                log.info("Incorrect Command {}", user);
                command = commands.get("close");
            }else{
                command = commands.getOrDefault(user, commands.get("close"));
            }

            result = command.execute();
            log.info("Loop Result: {} on command {}", result, command.getClass().getSimpleName());

            log.info(command.getClass().getName());
            if(command.getClass().getName().endsWith("CloseCommand")){
                result = !result;
            }
        }while (result);

        framework.put("Exiting...");
    }
}
