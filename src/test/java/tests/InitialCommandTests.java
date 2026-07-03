package tests;

import application.ArsTrackerLauncher;
import application.CommandFramework;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class InitialCommandTests {
    @Test
    void testInitialHelpCommand(){
        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(null);

        //Testing whether the initial commands are correctly loaded
        List<String> initialCommandNames = new ArrayList<>();
        initialCommandNames.add("LaunchGUI");
        initialCommandNames.add("CloseCommand");
        initialCommandNames.add("HelpView");

        List<String> loadedCommandNames = new ArrayList<>();
        launcher.getCommands().forEach((name, command) -> {
            loadedCommandNames.add(command.name());
        });

        assertTrue(initialCommandNames.containsAll(loadedCommandNames) && loadedCommandNames.containsAll(initialCommandNames));
    }

    @Test
    void testCampaignSelection(){
        try{
            Terminal terminal = TerminalBuilder.builder().system(false).build();
            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);

            OutputStream outputStream = terminal.output();
            log.info(outputStream.toString());
        }catch (IOException exp){
            log.error("Some sort of IO exception {}", exp.getMessage());
        }
    }
}
