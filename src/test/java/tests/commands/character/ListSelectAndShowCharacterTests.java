package tests.commands.character;

import application.ArsTrackerLauncher;
import application.data.*;
import application.models.*;
import application.models.enums.Art;
import application.models.enums.Attribute;
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
import java.util.Random;

import static application.utils.CharacterUtils.artExperienceToScore;
import static application.utils.CharacterUtils.format;
import static org.junit.jupiter.api.Assertions.*;
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
        void showBaseCharacterNoAbilities() throws IOException {
            ArsCharacter character = generateCharacter();
            covenant.addCharacter(character);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS));

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        /*
        >> show
        %s (%s)
        Attributes:
        \tIntelligence: %d
        \tPerception: %d
        \tStrength: %d
        \tStamina: %d
        \tPresence: %d
        \tCommunication: %d
        \tDexterity: %d
        \tQuickness: %d
        Virtues: ...
        Flaws: ...
        Abilities:
        \t%s (%s) lvl%d...
        Arts: ...
        >> close
        Exiting...
         */
        @Test
        void showWithOneAbilityAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            character.addAbility(generateAbility());

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Abilities:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    character.getAbilities().getFirst().toString());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithOneAbilityFromQuery() throws IOException {
            ArsCharacter character = generateCharacter();
            Ability ability = generateAbility();

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);
            assertTrue(superSource.addAbility(character.getId(), ability));

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Abilities:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    ability.toString());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithMultipleAbilitiesAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            List<Ability> abilities = new ArrayList<>(List.of(new Ability[]{generateAbility(), generateAbility()}));
            character.addAbility(abilities.get(0));
            character.addAbility(abilities.get(1));
            abilities.sort(null);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Abilities:
                    \t%s
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    character.getAbilities().get(0).toString(), character.getAbilities().get(1).toString());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithMultipleAbilitiesFromQuery() throws IOException {
            ArsCharacter character = generateCharacter();
            List<Ability> abilities = new ArrayList<>(List.of(new Ability[]{generateAbility(), generateAbility()}));
            abilities.sort(null);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);
            superSource.addAbility(character.getId(), abilities.get(0));
            superSource.addAbility(character.getId(), abilities.get(1));

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Abilities:
                    \t%s
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    character.getAbilities().get(0).toString(), character.getAbilities().get(1).toString());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithOneVirtueAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            character.addFeature(generateCharacterFeature(true));

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    \t%s
                    Flaws:
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    character.getFeatures().getFirst());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithOneFlawAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            character.addFeature(generateCharacterFeature(false));

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    Flaws:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    character.getFeatures().getFirst());

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithOneFlawFromQuery() throws IOException {
            ArsCharacter character = generateCharacter();
            CharacterFeature feature = generateCharacterFeature(false);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);
            dataSource.saveNewFeature(feature);
            dataSource.addFeatureToCharacter(character, feature);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    Flaws:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    feature);

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithMultipleVirtuesAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            List<CharacterFeature> features = new ArrayList<>(List.of(new CharacterFeature[]{generateCharacterFeature(true), generateCharacterFeature(true)}));
            character.addFeature(features.get(0));
            character.addFeature(features.get(1));
            features.sort(null);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    \t%s
                    \t%s
                    Flaws:
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    features.get(0), features.get(1));

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithMultipleFlawsAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            List<CharacterFeature> features = new ArrayList<>(List.of(new CharacterFeature[]{generateCharacterFeature(false), generateCharacterFeature(false)}));
            character.addFeature(features.get(0));
            character.addFeature(features.get(1));
            features.sort(null);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    Flaws:
                    \t%s
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    features.get(0), features.get(1));

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithBothVirtuesAndFlawsAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter();
            CharacterFeature virtue = generateCharacterFeature(true);
            CharacterFeature flaw = generateCharacterFeature(false);

            character.addFeature(virtue);
            character.addFeature(flaw);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    \t%s
                    Flaws:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    virtue, flaw);

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithVirtueAndFlawFromQuery() throws IOException {
            ArsCharacter character = generateCharacter();
            CharacterFeature virtue = generateCharacterFeature(true);
            CharacterFeature flaw = generateCharacterFeature(false);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);
            dataSource.saveNewFeature(virtue);
            dataSource.saveNewFeature(flaw);
            dataSource.addFeatureToCharacter(character, virtue);
            dataSource.addFeatureToCharacter(character, flaw);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    \t%s
                    Flaws:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    virtue, flaw);

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithMultipleVirtuesFromQuery() throws IOException {
            ArsCharacter character = generateCharacter();
            List<CharacterFeature> features = new ArrayList<>(List.of(new CharacterFeature[]{generateCharacterFeature(true), generateCharacterFeature(true)}));
            features.sort(null);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);
            features.forEach((feature) -> {
                dataSource.saveNewFeature(feature);
                dataSource.addFeatureToCharacter(character, feature);
            });

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    \t%s
                    \t%s
                    Flaws:
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    features.get(0), features.get(1));

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showCharacterWithAbilitiesAndFeatures() throws IOException {
            ArsCharacter character = generateCharacter();
            CharacterFeature virtue = generateCharacterFeature(true);
            CharacterFeature flaw = generateCharacterFeature(false);
            Ability ability = generateAbility();

            character.addFeature(virtue);
            character.addFeature(flaw);
            character.addAbility(ability);

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Virtues:
                    \t%s
                    Flaws:
                    \t%s
                    Abilities:
                    \t%s
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    virtue, flaw, ability);

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithOneArtAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            Random random = new Random();
            Art art = Art.values()[random.nextInt(Art.values().length)];
            character.incrementArt(art, random.nextInt(1000));

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Arts:
                    \t%s: %d
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    format(art), artExperienceToScore(character.getArt(art)));

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithOneArtFromQuery() throws IOException {
            Random random = new Random();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            Art art = Art.values()[random.nextInt(Art.values().length)];
            character.incrementArt(art, random.nextInt(1000));

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal, superSource);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            dataSource.addBaseCharacterToCovenant(covenant, character);
            dataSource.updateCharacterArts(character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Arts:
                    \t%s: %d
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    format(art.name()), dataSource.loadCharacterArt(character, art));

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithMultipleArtsAlreadyLoaded() throws IOException {
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            Random random = new Random();
            List<Art> arts = new ArrayList<>();
            arts.add(Art.values()[random.nextInt(Art.values().length)]);
            arts.add(Art.values()[random.nextInt(Art.values().length)]);
            while (arts.get(0).equals(arts.get(1))){
                arts.removeLast();
                arts.add(Art.values()[random.nextInt(Art.values().length)]);
            }
            arts.sort(null);

            for(Art art : arts){
                character.incrementArt(art, random.nextInt(1000));
            }

            String simulatedInput = "show\nclose\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Terminal terminal = TerminalBuilder.builder().system(false).dumb(true).streams(inputStream, outputStream).build();

            ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(terminal);
            launcher.getFramework().setActiveCampaign(campaign);
            launcher.getFramework().setActiveCovenant(covenant);
            launcher.getFramework().setActiveCharacter(character);

            launcher.coreLoop();
            terminal.close();

            String idealOutputFormat = """
                    >> show
                    %s (%s), of %s
                    Attributes:
                    \tIntelligence: %d
                    \tPerception: %d
                    \tStrength: %d
                    \tStamina: %d
                    \tPresence: %d
                    \tCommunication: %d
                    \tDexterity: %d
                    \tQuickness: %d
                    Arts:
                    \t%s: %d
                    \t%s: %d
                    >> close
                    Exiting...
                    """;
            String idealOutput = String.format(idealOutputFormat, character.getName(), format(character.getCharacterType()), covenant.getName(),
                    character.getAttribute(Attribute.INTELLIGENCE), character.getAttribute(Attribute.PERCEPTION), character.getAttribute(Attribute.STRENGTH), character.getAttribute(Attribute.STAMINA),
                    character.getAttribute(Attribute.PRESENCE), character.getAttribute(Attribute.COMMUNICATION), character.getAttribute(Attribute.DEXTERITY), character.getAttribute(Attribute.QUICKNESS),
                    format(arts.get(0)), artExperienceToScore(character.getArt(arts.get(0))), format(arts.get(1)), artExperienceToScore(character.getArt(arts.get(1))));

            assertEquals(idealOutput, outputStreamToReadable(outputStream, simulatedInput));
        }

        @Test
        void showWithMultipleArtsFromQuery(){
            fail();
        }

        @Test
        void ignoreArtsIfNotMagus(){
            fail();
        }

        @Test
        void showAbilitiesAndArts(){

        }
    }
}
