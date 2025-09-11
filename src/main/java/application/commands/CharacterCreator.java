package application.commands;

import application.characters.Character;
import application.terminal.CommandFramework;

import java.util.ArrayList;
import java.util.List;

/*
    This command will build off of the editing command. The execute function will run through each requirement, rather than giving options and allowing players to select from those.

    It may also override a few of the other methods, but I am currently unsure

    Character Table Structure:
        id
        name
        campaign_id
        birth_season
        character_type
        characteristics*
 */
public class CharacterCreator extends CharacterEditorCommand {
    Character character;

    public CharacterCreator(CommandFramework framework) {
        super(framework, null);
    }

    @Override
    public boolean execute(){
        String name = super.getString("Name >");
        int age = super.getInt("Current Age >");
        int birthSeason = getSeason(age);

        ArrayList<String> characterTypes = new ArrayList<>(List.of(new String[]{"MAGUS", "COMPANION", "GROG"}));
        String type = characterTypes.get(super.getOptions(characterTypes));

        character = new Character(name, birthSeason, type);

        CharacterEditorCommand editor = new CharacterEditorCommand(framework, character);
        List<Integer> characteristics = editor.getCharacteristics("Characteristics >");

        //Add Abilities

        //Add Features

        return addCharacter();
    }

    /*
    Add created character to the database
     */
    public boolean addCharacter(){
        return false;
    }

    /*
    Method to convert a given age to the season where the character was born

    Will need to implement with different campaigns / birth seasons in mind later
     */
    public static int getSeason(int age){
        return 1220 - age * 4;
    }
}
