package tests;

import application.ArsTrackerLauncher;
import application.data.CampaignDataSource;
import application.models.Campaign;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandAvailabilityTests {
    @Test
    void testInitialHelpCommand(){
        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(null, null);

        //Testing whether the initial commands are correctly loaded
        List<String> initialCommandNames = new ArrayList<>();
        initialCommandNames.add("LaunchGUI");
        initialCommandNames.add("CloseCommand");
        initialCommandNames.add("HelpView");
        initialCommandNames.add("CampaignCreationCommand");
        initialCommandNames.add("SelectCampaignCommand");

        List<String> loadedCommandNames = new ArrayList<>();
        launcher.getCommands().forEach((name, command) -> {
            loadedCommandNames.add(command.getClass().getSimpleName());
        });

        assertTrue(initialCommandNames.containsAll(loadedCommandNames) && loadedCommandNames.containsAll(initialCommandNames));
    }

    @Test
    void availableMethodsAfterSelectingCampaign(){
        ArsTrackerLauncher launcher = ArsTrackerLauncher.getMockLauncher(null, null);

        List<String> commandNames = new ArrayList<>();
        commandNames.add("LaunchGUI");
        commandNames.add("CloseCommand");
        commandNames.add("HelpView");
        commandNames.add("ReturnCommand");
        commandNames.add("ListCovenantsCommand");
        commandNames.add("CharacterSelectionCommand");
        commandNames.add("CharacterCreationCommand");
        commandNames.add("CampaignDeletionCommand");

        Campaign campaign = utils.generateCampaign();
        CampaignDataSource dataSource = new CampaignDataSource(launcher.getDataSource());
        dataSource.addCampaign(campaign);

        launcher.getFramework().setActiveCampaign(campaign);
        assertEquals(8, launcher.updateCommands());

        List<String> loadedCommandNames = new ArrayList<>();
        launcher.getCommands().forEach((name, command) -> {
            loadedCommandNames.add(command.getClass().getSimpleName());
        });

        assertTrue(commandNames.containsAll(loadedCommandNames) && loadedCommandNames.containsAll(commandNames));
    }
}
