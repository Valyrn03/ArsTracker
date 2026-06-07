package application.commands;

import application.terminal.Command;
import application.terminal.CommandFramework;
import application.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static java.util.UUID.randomUUID;

public class CreateCampaign implements Command {
    private CommandFramework framework;
    private final String query_string = "INSERT INTO campaigns VALUES (?, ?, ?);";
    private final List<String> seasons = Arrays.asList(new String[]{"Winter", "Spring", "Summer", "Fall"});

    public CreateCampaign(CommandFramework fr){
        framework = fr;
    }

    /*
    For the sake of this option, we need to get a campaign's name. Current season should start at 0.

    ID will be via randomUUID method
     */
    @Override
    public boolean execute() {
        String campaignName = framework.getString("Campaign Name");
        int start_year = framework.getInt("Starting Year");
        int start_season = framework.getOptions(seasons);

        try(Connection conn = DataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query_string)){
            statement.setString(0, randomUUID().toString());
            statement.setString(1, campaignName);
            statement.setInt(2, start_year * 4 + start_season);

            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }
}
