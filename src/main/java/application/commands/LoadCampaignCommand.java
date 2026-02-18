package application.commands;

import application.ArsTrackerLauncher;
import application.characters.Character;
import application.terminal.Command;
import application.terminal.CommandFramework;
import application.terminal.DataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Command to list characters from the currently active campaign for usage by CharacterSelector

Should be called by default if there is an active campaign and the user is selecting a new character
 */
@Slf4j
public class LoadCampaignCommand implements Command {
    CommandFramework framework;
    int campaignID;

    public LoadCampaignCommand(CommandFramework source, int campaign_id) {
        framework = source;
        campaignID = campaign_id;
    }

    @Override
    public boolean execute() {
        Connection connection = DataSource.getConnection();
        List<String> characters = getAvailableCharacterNames();

        try(PreparedStatement statement = connection.prepareStatement("SELECT name WHERE campaign_id = ?")){
            statement.setInt(0, campaignID);

            ResultSet resultSet = statement.executeQuery();

            while(!resultSet.isAfterLast()){
                String name = resultSet.getString("name");
                if(!characters.contains(name)){
                    ArsTrackerLauncher.characters.add(constructCharacter(resultSet, resultSet.getRow()));
                }
            }
        }catch (SQLException exp){
            log.warn("Selecting Characters from Campaign {} has failed, with the error of {}", campaignID, exp.getMessage());
        }
    }

    private Character constructCharacter(ResultSet resultSet, int row) {

    }

    /*
    From the list of characters loaded in ArsTrackerLauncher, get all of the names, for the sake of an existence check
     */
    private List<String> getAvailableCharacterNames(){
        List<String> names = new ArrayList<>();
        for(Character character : ArsTrackerLauncher.characters){
            names.add(character.getName());
        }

        return names;
    }
}
