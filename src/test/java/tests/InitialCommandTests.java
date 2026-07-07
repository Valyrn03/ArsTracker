package tests;

import application.ArsTrackerLauncher;
import application.CommandFramework;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class InitialCommandTests {
    @Test
    void testInitialHelpCommand(){
        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(null, null);

        //Testing whether the initial commands are correctly loaded
        List<String> initialCommandNames = new ArrayList<>();
        initialCommandNames.add("LaunchGUI");
        initialCommandNames.add("CloseCommand");
        initialCommandNames.add("HelpView");

        List<String> loadedCommandNames = new ArrayList<>();
        launcher.getCommands().forEach((name, command) -> {
            loadedCommandNames.add(command.getClass().getSimpleName());
        });

        assertTrue(initialCommandNames.containsAll(loadedCommandNames) && loadedCommandNames.containsAll(initialCommandNames));
    }

    @Test
    void testEmptyCampaignSelection() throws IOException{
        String simulatedInput = "select\nclose\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();
        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, null);
        launcher.coreLoop();
        terminal.close();

        assertEquals(">>select\n0 Campaigns Loaded\n>>close\nExiting...\n", TestUtils.outputStreamToReadable(outputStream, simulatedInput.length() + 2));
    }
}
