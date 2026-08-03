package tests.commands.character;

import application.ArsTrackerLauncher;
import application.data.*;
import application.models.ArsCharacter;
import application.models.Campaign;
import application.models.Covenant;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.utils.*;

public class ListSelectAndShowCharacterTests {
    IDataSource superSource;
    ICharacterDataSource dataSource;
    ICovenantDataSource covenantDataSource;
    ICampaignDataSource campaignDataSource;
    Campaign campaign;
    Covenant covenant;

    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        dataSource = new CharacterDataSource(superSource);
        covenantDataSource = new CovenantDataSource(superSource);
        campaignDataSource = new CampaignDataSource(superSource);

        campaign = generateCampaign();
        campaignDataSource.addCampaign(campaign);
        covenant = generateCovenant();
        covenantDataSource.addCovenant(covenant, campaign);
    }

    @Nested
    class ListCharacters{
        @Test
        void listNoCharacters() throws IOException {
            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> list
                    0 Characters Found
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat);

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listOneCharacterAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            covenant.addCharacter(character);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> list
                    Characters:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.toStringShortened());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listOneCharacterFromQuery() throws IOException {
            ArsCharacter character = generateCharacter();
            dataSource.addBaseCharacterToCovenant(covenant, character);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> list
                    Characters:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.toStringShortened());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listMultipleCharactersAlreadyLoaded() throws IOException {
            List<ArsCharacter> characters = new ArrayList<>();
            for(int i = 0; i < 2; i++){
                characters.add(generateCharacter());
                covenant.addCharacter(characters.getLast());
            }
            characters.sort(null);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> list
                    Characters:
                    \t%s
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, characters.get(0).toStringShortened(), characters.get(1).toStringShortened());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listMultipleCharactersFromQuery() throws IOException {
            List<ArsCharacter> characters = new ArrayList<>();
            for(int i = 0; i < 2; i++){
                characters.add(generateCharacter());
                dataSource.addBaseCharacterToCovenant(covenant, characters.getLast());
            }
            characters.sort(null);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> list
                    Characters:
                    \t%s
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, characters.get(0).toStringShortened(), characters.get(1).toStringShortened());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void listMultipleCharactersFromQueryAndAlreadyLoaded() throws IOException {
            List<ArsCharacter> characters = new ArrayList<>();
            characters.add(generateCharacter());
            characters.add(generateCharacter());

            covenant.addCharacter(characters.getFirst());
            dataSource.addBaseCharacterToCovenant(covenant, characters.getLast());
            characters.sort(null);

            String simulatedInput = "list\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> list
                    Characters:
                    \t%s
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, characters.get(0).toStringShortened(), characters.get(1).toStringShortened());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }
    }

    @Nested
    class SelectCharacter{
        @Test
        void selectOnZeroCharacters() throws IOException {
            String simulatedInput = "select\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> select
                    0 Characters Found
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat);

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void selectOnSingularCharacter() throws IOException {
            ArsCharacter character = generateCharacter();
            covenant.addCharacter(character);

            String simulatedInput = "select\n1\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> select
                    Choose one of the following options:
                    \t1. %s
                    >> 1
                    Selected Character %s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.toStringShortened(), character.getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void selectFromMultipleCharacters() throws IOException {
            List<ArsCharacter> characters = new ArrayList<>();
            characters.add(generateCharacter());
            characters.add(generateCharacter());
            characters.forEach(covenant::addCharacter);
            characters.sort(null);

            String simulatedInput = "select\n1\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> select
                    Choose one of the following options:
                    \t1. %s
                    \t2. %s
                    >> 1
                    Selected Character %s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, characters.get(0).toStringShortened(), characters.get(1).toStringShortened(), characters.get(0).getName());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }
    }

    @Nested
    class ShowCharacter{
        @Test
        void showBaseCharacter(){

        }

        @Test
        void showBaseMagus(){

        }

        @Test
        void showWithOneAbilityAlreadyLoaded(){

        }

        @Test
        void showWithOneAbilityFromQuery(){

        }

        @Test
        void showWithMultipleAbilitiesAlreadyLoaded(){

        }

        @Test
        void showWithMultipleAbilitiesFromQuery(){

        }

        @Test
        void showWithOneFeatureAlreadyLoaded(){

        }

        @Test
        void showWithOneFeatureFromQuery(){

        }

        @Test
        void showWithMultipleFeaturesAlreadyLoaded(){

        }

        @Test
        void showWithMultipleFeaturesFromQuery(){

        }

        @Test
        void showWithNoArtsSet(){

        }

        @Test
        void showWithOneArtSetFromQuery(){

        }

        @Test
        void showWithOneArtSetAlreadyLoaded(){

        }

        @Test
        void showWithMultipleArtsFromQuery(){

        }

        @Test
        void showWithMultipleArtsAlreadyLoaded(){

        }

        @Test
        void ignoreArtsIfNotMagus(){

        }
    }
}
