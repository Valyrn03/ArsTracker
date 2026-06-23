package tests;

import application.ArsTrackerLauncher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InitialCommandTests {
    @Test
    void testInitialHelpCommand(){
        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher();

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
}
