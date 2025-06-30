package application;

import application.characters.Character;
import application.characters.CharacterFeature;
import application.displays.LandingPage;
import application.terminal.CharacterCreator;
import application.terminal.CommandController;
import application.terminal.DatabaseFunction;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;

public class Launcher {
    public static void main(String[] args){
        Launcher launcher = new Launcher(args);
        launcher.coreLoop();
    }

    String[] args;
    DatabaseFunction database;
    CommandController controller;
    TextIO IOSource;
    TextTerminal terminal;

    public Launcher(String[] arg){
        args = arg;
        IOSource = TextIoFactory.getTextIO();
        IOSource.getTextTerminal().println("Type \"help\" to get a list of commands");
        database = new DatabaseFunction();
        ArrayList<Character> characters = database.query("");
        controller = new CommandController(characters);
        terminal = IOSource.getTextTerminal();
    }

    public void coreLoop(){
        int result = controlInput();
        while(result != 0){
            if(result == 3){
                LandingPage.launch(LandingPage.class, args);
            }else if(result == 2){
                database.add(createCharacter());
            }
            result = controlInput();
        }
    }

    public int controlInput(){
        String command = IOSource.newStringInputReader().read("> ");
        if(command.equals("help")){
            terminal.println("COMMANDS\n\tlist: list all characters\n\tselect [character]: select character by given name\n\tcreate \n\t\tcreate character: Create new character\n\topenGUI: Open GUI");
            return 1;
        }else if(command.equals("openGUI")){
            return 2;
        }else if(command.equals("close")){
            return 0;
        }else if(command.equals("list")){ 
            terminal.printf("%s\n", controller.loadCharacters());
            return 1;
        }else if(command.startsWith("select")){
            String characterName = command.substring(7);
            terminal.println(controller.selectCharacter(characterName));
            return 1;
        }else if(command.equals("create character")){
            return 2;
        }
        terminal.println("Command Not Found, Please Try Again");
        return 1;
    }

    public Character createCharacter(){
        CharacterCreator creator = new CharacterCreator();
        String characterName = IOSource.newStringInputReader().read("Character Name >");
        int category = IOSource.newIntInputReader().read("[" + characterName + "] Category\n\tType \"1\" for Magus\n\tType \"2\" for Companion\n\tType \"3\" for Grog\n\t\t>");
        switch (category){
            case 1:
                creator.initialize(characterName, Character.CharacterType.MAGUS);
                break;
            case 2:
                creator.initialize(characterName, Character.CharacterType.COMPANION);
                break;
            default:
                creator.initialize(characterName, Character.CharacterType.GROG);
        }

        terminal.println("Enter Virtues, submit an empty line to show the listing is complete:");
        String featureInput = IOSource.newStringInputReader().read("\tName >");
        while(featureInput.length() > 1){
            char strengthSelector = IOSource.newCharInputReader().read("\tMajor ([M]) or Minor ([m]) >");
            String description = IOSource.newStringInputReader().read("\tDescription >");

            CharacterFeature feature;
            if(strengthSelector == 'M'){
                feature = new CharacterFeature(featureInput, description, true, true);
            }else{
                feature = new CharacterFeature(featureInput, description, true, false);
            }

            creator.addNewFeature(feature);
        }

        return creator.close();
    }
}
