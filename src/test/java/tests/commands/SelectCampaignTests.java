package tests.commands;

import application.ArsTrackerLauncher;
import application.data.CampaignDataSource;
import application.models.Campaign;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class SelectCampaignTests {
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

        Assertions.assertEquals(">>select\n0 Campaigns Loaded\n>>close\nExiting...\n", utils.outputStreamToReadable(outputStream, simulatedInput.length() + 2));
    }

    @Test
    void testSelectSingleCampaign() throws IOException {
        Campaign testCampaign = utils.generateCampaign();
        List<String> mockCampaignId = List.of(testCampaign.id.toString());

        String simulatedInput = "select\n1\nclose\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, mockCampaignId);
        CampaignDataSource dataSource = new CampaignDataSource(launcher.getDataSource());
        dataSource.addCampaign(testCampaign); //trusting this works due to other tests...

        launcher.coreLoop();
        terminal.close();

        String idealOutput = ">>select\n1. Campaign\n>>1\nSelected Campaign \"Faux\"\n>>close\nExiting...\n";
        assertEquals(idealOutput, utils.outputStreamToReadable(outputStream, simulatedInput.length() + 2));

        assertTrue(launcher.getFramework().getActiveCampaign().isPresent());
        assertEquals(testCampaign, launcher.getFramework().getActiveCampaign().get());
    }

    @Test
    void testSelectMultipleCampaigns(){

    }

    @Test
    void testSelectAndShowCampaign(){

    }
}
