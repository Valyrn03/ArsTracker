package tests.commands;

import application.ArsTrackerLauncher;
import application.data.*;
import application.models.Campaign;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tests.utils.*;

@Slf4j
public class ListSelectAndShowCovenantTests {
    IDataSource superSource;
    ICharacterDataSource dataSource;
    ICovenantDataSource covenantDataSource;
    ICampaignDataSource campaignDataSource;
    Campaign campaign;

    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        dataSource = new CharacterDataSource(superSource);
        covenantDataSource = new CovenantDataSource(superSource);
        campaignDataSource = new CampaignDataSource(superSource);

        campaign = generateCampaign();
        campaignDataSource.addCampaign(campaign);
    }

    @Nested
    @DisplayName("List covenants belonging to the active campaign")
    class ListCovenants{
        @Test
        void listSingularCovenantAlreadyLoaded() throws IOException {
            Covenant covenant = generateCovenant();
            campaign.addCovenant(covenant);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> list\n" +
                    "Covenants:\n" +
                    "\t%s\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat, covenant.getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listSingularCovenantFromQuery() throws IOException {
            Covenant covenant = generateCovenant();

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            covenantDataSource.addCovenant(covenant, campaign);

            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> list\n" +
                    "Covenants:\n" +
                    "\t%s\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat, covenant.getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listMultipleCovenantsAlreadyLoaded() throws IOException {
            List<Covenant> covenants = new ArrayList<>();
            covenants.add(generateCovenant());
            covenants.add(generateCovenant());
            covenants.sort(null);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            campaign.addCovenant(covenants.get(0));
            campaign.addCovenant(covenants.get(1));

            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> list\n" +
                    "Covenants:\n" +
                    "\t%s\n" +
                    "\t%s\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat, covenants.get(0).getName(), covenants.get(1).getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listMultipleCovenantsFromQuery() throws IOException {
            List<Covenant> covenants = new ArrayList<>();
            covenants.add(generateCovenant());
            covenants.add(generateCovenant());
            covenants.sort(null);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            covenantDataSource.addCovenant(covenants.get(0), campaign);
            covenantDataSource.addCovenant(covenants.get(1), campaign);

            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> list\n" +
                    "Covenants:\n" +
                    "\t%s\n" +
                    "\t%s\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat, covenants.get(0).getName(), covenants.get(1).getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listMultipleCovenantsLoadedAndFromQuery() throws IOException {
            List<Covenant> covenants = new ArrayList<>();
            covenants.add(generateCovenant());
            covenants.add(generateCovenant());
            covenants.sort(null);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            covenantDataSource.addCovenant(covenants.get(0), campaign);
            campaign.addCovenant(covenants.get(1));

            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> list\n" +
                    "Covenants:\n" +
                    "\t%s\n" +
                    "\t%s\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat, covenants.get(0).getName(), covenants.get(1).getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void saveSingleQueryCovenantToCampaign() throws IOException{
            Covenant covenant = generateCovenant();

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            covenantDataSource.addCovenant(covenant, campaign);

            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            assertTrue(launcher.getFramework().getActiveCampaign().isPresent());
            assertTrue(launcher.getFramework().getActiveCampaign().get().getCovenants().contains(covenant));
        }

        @Test
        void saveMultipleQueriedCovenantsToCampaign() throws IOException {
            List<Covenant> covenants = new ArrayList<>();
            covenants.add(generateCovenant());
            covenants.add(generateCovenant());
            covenants.sort(null);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            covenantDataSource.addCovenant(covenants.get(0), campaign);
            covenantDataSource.addCovenant(covenants.get(1), campaign);

            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            assertTrue(launcher.getFramework().getActiveCampaign().isPresent());
            assertTrue(launcher.getFramework().getActiveCampaign().get().getCovenants().containsAll(covenants));
        }
    }

    @Nested
    class SelectCovenant{
        @Test
        void selectOnZeroCovenants() throws IOException {
            String simulatedInput = "select\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> select\n" +
                    "0 Covenants Loaded\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat);

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void selectSingularCovenant() throws IOException {
            Covenant covenant = generateCovenant();
            campaign.addCovenant(covenant);

            String simulatedInput = "select\n1\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> select\n" +
                    "Choose one of the following options:\n" +
                    "\t1. %s\n" +
                    ">> 1\n" +
                    "Selected Covenant %s\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat, covenant.getName(), covenant.getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void selectFromMultipleCovenants() throws IOException {
            List<Covenant> covenants = new ArrayList<>();
            for (int i = 0; i < 2; i++){
                covenants.add(generateCovenant());
                campaign.addCovenant(covenants.getLast());
            }
            covenants.sort(null);

            String simulatedInput = "select\n1\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = ">> select\n" +
                    "Choose one of the following options:\n" +
                    "\t1. %s\n" +
                    "\t2. %s\n" +
                    ">> 1\n" +
                    "Selected Covenant %s\n" +
                    ">> close\n" +
                    "Exiting...\n";
            String idealOutput = String.format(idealOutputFormat, covenants.get(0).getName(), covenants.get(1).getName(), covenants.get(0).getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }
    }

    @Nested
    class ShowCovenant{
        @Test
        void showCovenantWithNoFeaturesNoCharacters(){
            fail();
        }

        @Test
        void showCovenantWithSingularFeatureNoCharacters(){
            fail();
        }

        @Test
        void showCovenantWithMultipleFeaturesNoCharacters(){
            fail();
        }

        @Test
        void showCovenantWithSingularCharacterNoFeatures(){
            fail();
        }

        @Test
        void showCovenantWithMultipleCharactersNoFeatures(){
            fail();
        }

        @Test
        void showCovenantWithSingularCharacterSingularFeature(){
            fail();
        }

        @Test
        void showCovenantWithMultipleCharactersMultipleFeatures(){
            fail();
        }
    }
}
