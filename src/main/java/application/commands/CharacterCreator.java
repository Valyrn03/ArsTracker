package application.commands;

import application.characters.Ability;
import application.characters.Character;
import application.commands.characterEditor.AbilityEditor;
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
    private static final int seasonsPerYear = 4;

    public CharacterCreator(CommandFramework framework) {
        super(framework, null);
    }

    @Override
    public boolean execute(){
        String name = super.getString("Name >");
        int age = super.getInt("Current Age >");
        int birthSeason = getSeason(age);

        List<String> characterTypes = new ArrayList<>(List.of(new String[]{"MAGUS", "COMPANION", "GROG"}));
        String type = characterTypes.get(super.getOptions(characterTypes));

        character = new Character(name, birthSeason, type);

        CharacterEditorCommand editor = new CharacterEditorCommand(framework, character);
        List<Integer> characteristics = editor.getCharacteristics("Characteristics >");

        List<String> continueOptions = new ArrayList<>();
        continueOptions.add("Add Another?");
        continueOptions.add("Confirm");
        //Add Abilities
        while(super.getOptions(continueOptions) == 0){
            Ability ability = editor.createAbility();
            if(!AbilityEditor.verifyAbility(character, ability)){
                //Find a way to see why it's invalid
                framework.printToTerminal("Ability Not Valid");
            }
        }

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
        return 1220 - age * seasonsPerYear;
    }
}
