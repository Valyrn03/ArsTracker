package tests.commands;

import application.ArsTrackerLauncher;
import application.data.CampaignDataSource;
import application.data.CovenantDataSource;
import application.models.Campaign;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;
import tests.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class SelectAndShowCampaignTests {
    @Test
    void testEmptyCampaignSelection() throws IOException{
        String simulatedInput = "select\nclose\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();
        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
        launcher.coreLoop();
        terminal.close();

        assertEquals(">> select\n0 Campaigns Loaded\n>> close\nExiting...\n", utils.outputStreamToReadable(outputStream, simulatedInput));
    }

    @Test
    void testSelectSingleCampaign() throws IOException {
        Campaign testCampaign = utils.generateCampaign();

        String simulatedInput = "select\n1\nclose\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
        CampaignDataSource dataSource = new CampaignDataSource(launcher.getDataSource());
        dataSource.addCampaign(testCampaign.getName(), testCampaign.getCurrentSeason()); //trusting this works due to other tests...

        launcher.coreLoop();
        terminal.close();

        String idealOutputFormat = ">> select\nChoose one of the following options:\n\t1. %s\n>> 1\nSelected Campaign \"%s\"\n>> close\nExiting...\n";
        String idealOutput = String.format(idealOutputFormat, testCampaign.getName(), testCampaign.getName());

        assertEquals(idealOutput, utils.outputStreamToReadable(outputStream, simulatedInput));
        assertTrue(launcher.getFramework().getActiveCampaign().isPresent());
        assertEquals(testCampaign, launcher.getFramework().getActiveCampaign().get());

        assertEquals(1, launcher.getFramework().getAccessedCampaigns().size());
        assertEquals(testCampaign, launcher.getFramework().getAccessedCampaigns().getFirst());
    }

    @Test
    void testSelectMultipleCampaigns() throws IOException {
        Campaign testCampaignOne = utils.generateCampaign();
        Campaign testCampaignTwo = utils.generateCampaign();

        String simulatedInput = "select\n1\nclose\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
        CampaignDataSource dataSource = new CampaignDataSource(launcher.getDataSource());
        dataSource.addCampaign(testCampaignOne);
        dataSource.addCampaign(testCampaignTwo);

        launcher.coreLoop();
        terminal.close();

        String idealOutputFormat = ">> select\n" +
                "Choose one of the following options:\n" +
                "\t1. %s\n" +
                "\t2. %s\n" +
                ">> 1\n" +
                "Selected Campaign \"%s\"\n" +
                ">> close\n" +
                "Exiting...\n";
        String idealOutput = String.format(idealOutputFormat, testCampaignOne.getName(), testCampaignTwo.getName(), testCampaignOne.getName());

        assertEquals(idealOutput, utils.outputStreamToReadable(outputStream, simulatedInput));
        assertTrue(launcher.getFramework().getActiveCampaign().isPresent());
        assertEquals(testCampaignOne, launcher.getFramework().getActiveCampaign().get());

        assertEquals(1, launcher.getFramework().getAccessedCampaigns().size());
        assertEquals(testCampaignOne, launcher.getFramework().getAccessedCampaigns().getFirst());
    }

    @Test
    void testSelectAndShowCampaignWithNoCovenants() throws IOException {
        Campaign testCampaign = utils.generateCampaign();

        String simulatedInput = "select\n1\nshow\nclose\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
        CampaignDataSource dataSource = new CampaignDataSource(launcher.getDataSource());
        dataSource.addCampaign(testCampaign);

        launcher.coreLoop();
        terminal.close();

        String idealOutputFormat = ">> select\n" +
                "Choose one of the following options:\n" +
                "\t1. %s\n" +
                ">> 1\n" +
                "Selected Campaign \"%s\"\n" +
                ">> show\n" +
                "Name: %s\n" +
                "Season: %s\n" +
                ">> close\n" +
                "Exiting...\n";
        String idealOutput = String.format(idealOutputFormat, testCampaign.getName(), testCampaign.getName(), testCampaign.getName(), testCampaign.getCurrentSeason());

        assertEquals(idealOutput, utils.outputStreamToReadable(outputStream, simulatedInput));
    }

    @Test
    void testSelectAndShowCampaignWithCovenants() throws IOException {
        Campaign campaign = utils.generateCampaign();
        Covenant covenant = utils.generateCovenant();

        String simulatedInput = "select\n1\nshow\nclose\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
        CampaignDataSource dataSource = new CampaignDataSource(launcher.getDataSource());
        CovenantDataSource covenantDataSource = new CovenantDataSource(launcher.getDataSource());
        dataSource.addCampaign(campaign);
        covenantDataSource.addCovenant(covenant, campaign);

        launcher.coreLoop();
        terminal.close();

        String idealOutputFormat = ">> select\n" +
                "Choose one of the following options:\n" +
                "\t1. %s\n" +
                ">> 1\n" +
                "Selected Campaign \"%s\"\n" +
                ">> show\n" +
                "Name: %s\n" +
                "Season: %s\n" +
                "Covenants:\n" +
                "\t%s\n" +
                ">> close\n" +
                "Exiting...\n";
        String idealOutput = String.format(idealOutputFormat, campaign.getName(), campaign.getName(), campaign.getName(), campaign.getCurrentSeason(), covenant.getName());

        assertEquals(idealOutput, utils.outputStreamToReadable(outputStream, simulatedInput));
    }
}
