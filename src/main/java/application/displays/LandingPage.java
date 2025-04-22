package application.displays;

import application.characters.CharacterBase;
import application.controllers.LandingPageController;
import application.utils.characterUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class LandingPage extends Application {
    static Stage stage;
    static ArrayList<CharacterBase> characters;

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        if(stage == null){
            stage = primaryStage;
        }
        stage.setTitle("Character Selector");
        stage.setScene(getScene());
        stage.show();
    }

    public static Scene getScene() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(LandingPage.class.getResource("landingPage.fxml"));

        connectCharacterDatabase();

        Parent root = (Parent) loader.load();
        LandingPageController controller = loader.getController();
        if(characters == null || characters.isEmpty()){
            characters = new ArrayList<>();
            characters.add(createSampleCharacter());
        }
        controller.setCharacters(characters);

        return new Scene(root, 1024, 760);
    }

    public static void resetScene() throws IOException, SQLException {
        stage.setScene(getScene());
    }

    private static void connectCharacterDatabase() throws SQLException {
        Logger logger = Logger.getLogger(LandingPage.class.getName());

        String databaseURL = "jdbc:sqlite:" + LandingPage.class.getResource("arsTrackerDB").getPath();

        Connection connection;
        try{
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException e){
            logger.severe("Failed to Connect to DB");
            return;
        }

        String createTableStatement = "CREATE TABLE IF NOT EXISTS characters (\n\tid UUID PRIMARY KEY,\n\tname STRING)";

        Statement statement = connection.createStatement();
        statement.executeUpdate(createTableStatement);

        logger.info("Table Created");

        String getPlayers = "SELECT * FROM characters";

        ResultSet result = statement.executeQuery(getPlayers);

        while(result.next()){
            String name = result.getString("name");
            logger.info("Character: " + name);
        }

        logger.info("Table Queried");

        statement.close();
        connection.close();
    }

    private static CharacterBase createSampleCharacter(){
        return new CharacterBase("Sample", "Name", 20, false);
    }
}
