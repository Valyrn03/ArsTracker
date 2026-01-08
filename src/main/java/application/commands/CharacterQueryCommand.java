package application.commands;

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
public class CharacterQueryCommand implements Command {
    CommandFramework framework;
    int campaignID;

    public CharacterQueryCommand(CommandFramework source, int campaign_id) {
        framework = source;
        campaignID = campaign_id;
    }

    @Override
    public boolean execute() {
        Connection connection = DataSource.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("SELECT name WHERE campaign_id = ?")){
            statement.setInt(0, campaignID);

            ResultSet resultSet = statement.executeQuery();

            while(!resultSet.isAfterLast()){
                //If name not in getAvailableCharacterNames(), then add it
                    //Or should calling this override the list of character names?
            }
        }catch (SQLException exp){
            log.warn("Selecting Characters from Campaign {} has failed, with the error of {}", campaignID, exp.getMessage());
        }
    }

    /*
    From the list of characters loaded in ArsTrackerLauncher, get all of the names, for the sake of an existence check
     */
    private List<String> getAvailableCharacterNames(){
        return null;
    }
}
