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
import java.util.*;

/*
Command to list characters from the currently active campaign for usage by CharacterSelector

Should be called by default if there is an active campaign and the user is selecting a new character
 */
@Slf4j
public class LoadCampaignCommand implements Command {
    CommandFramework framework;
    private final String campaignQuery = "SELECT id, name FROM campaigns;";

    public LoadCampaignCommand(CommandFramework source) {
        framework = source;
    }

    /*
    In this execute call, we need to...

    1. Query the campaigns table to get a list of loaded campaigns
    2. Query the selected campaign for which characters it contains
    3. Print out each of those characters, in order for the user to be able to choose one
        Ideally would use getOptions in combination, but am unsure if I want to pass in the already existing list for that
     */
    @Override
    public boolean execute() {
        Map<String, Integer> campaigns = getCampaigns();

        if(campaigns.isEmpty()){
            framework.put("Failed to Load Campaigns");
            return false;
        }

        campaigns.forEach((name, id) -> {
            framework.put(id + ": " + name);
        });

        int selectedCampaign = framework.getInt("Select Campaign via ID");

        List<Character> characters = getCharactersFromCampaign(selectedCampaign);

        if(characters.isEmpty()){
            framework.put("Failed to Load Characters from Campaign {}", selectedCampaign);
            return false;
        }

        characters.forEach(character -> {
            framework.put(character.getName() + " (" + character.getCharacterType().toString() + ")");
        });

        return true;
    }

    public List<Character> getCharactersFromCampaign(int campaignID){
        Connection connection = DataSource.getConnection();
        List<Character> characters = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement("SELECT name WHERE campaign_id = ?")){
            statement.setInt(0, campaignID);

            ResultSet resultSet = statement.executeQuery();

            while(!resultSet.isAfterLast()){
                characters.add(constructCharacter(resultSet, resultSet.getRow()));
            }
        }catch (SQLException exp){
            log.error("Selecting Characters from Campaign {} has failed, with the error of {}", campaignID, exp.getMessage());
        }

        return characters;
    }

    private Character constructCharacter(ResultSet resultSet, int row) {
        return null;
    }

    public Map<String, Integer> getCampaigns(){
        Map<String, Integer> campaigns = new HashMap<>();
        try(Connection conn = DataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(campaignQuery)){
            ResultSet resultSet = statement.executeQuery();

            while(!resultSet.isAfterLast()){
                campaigns.put(resultSet.getString("name"), resultSet.getInt("id"));
            }

            resultSet.close();
        }catch(SQLException exp){
            log.error("Failed to Query Campaigns, {}", exp.getMessage());
        }

        return campaigns;
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
