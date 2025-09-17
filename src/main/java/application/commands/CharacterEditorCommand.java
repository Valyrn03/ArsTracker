package application.commands;

import application.characters.Ability;
import application.characters.AbilityCategory;
import application.characters.Attribute;
import application.characters.Character;
import application.commands.characterEditor.AbilityEditor;
import application.commands.characterEditor.CharacteristicEditor;
import application.terminal.CharacterController;
import application.terminal.CommandFramework;
import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/*
Steps in Character Creation:

1. Virtues & Flaws
2. Characteristics X
3. Early Childhood
4. Later Life / Apprenticeship
    4.5 Spells
5. Personality

Arts & Characteristics Formula: n(n+1)/2
Abilities Formula: 5n(n+1)/2 OR 5*arts
    Cost to raise to tier: 5*n

This class will only hold the methods that require user input. All verification methods will be put in their respective editor in the characterEditor directory
 */
public class CharacterEditorCommand extends CharacterController {
    static final Logger logger = LoggerFactory.getLogger(CharacterEditorCommand.class);
    private Character character;
    CommandFramework framework;
    //Thank you stack overflow, I did not know it was a thing
    static final List<AbilityCategory> categoricalIDs = new ArrayList<>() {
        {
            add(AbilityCategory.AREA_LORE);
            add(AbilityCategory.CRAFT);
            add(AbilityCategory.LIVING_LANGUAGE);
            add(AbilityCategory.MYSTERY_CULT_LORE);
            add(AbilityCategory.ORGANIZATION_LORE);
            add(AbilityCategory.PROFESSION);
            add(AbilityCategory.DEAD_LANGUAGE);
            add(AbilityCategory.ENCHANTING);
        }
    };

    public CharacterEditorCommand(CommandFramework framework, Character character) {
        super(framework);
        this.character = character;
    }

    @Override
    public boolean execute() {
        return false;
    }

    public void changeCharacter(Character newCharacter){
        character = newCharacter;
    }

    /**
     * CHARACTERISTICS
     *
     * Handles user input & repeating if there is an issue
     *
     * @param prompt is the message that should be passed to the user, in order to use recursive calls with different messages
     *
     * @return a list of valid characteristics
     */
    public List<Integer> getCharacteristics(String prompt){
        List<Integer> characteristics = new ArrayList<>();

        for(Attribute attribute : Attribute.values()){
            characteristics.add(super.getInt(String.format("%s: ", attribute)));
        }

        if(CharacteristicEditor.verifyCharacteristics(characteristics) == null){
            return characteristics;
        }else{
            //Print Options + Costs
            Attribute[] attributeList = Attribute.values();

            framework.printToTerminal("This array is not valid. Try again");
            for(int i = 0; i < attributeList.length; i++){
                framework.printToTerminal(String.format("%d. %s: %d", i, attributeList[i], characteristics.get(i)));
            }
            //Call getCharacteristics() again, but with a "do better" as the string prompt
                //Add a "if prompt is not null, then print, else continue"
            return getCharacteristics("\n");
        }
    }

    /**
     * Method to handle the creation of a new ability.
     *
     * Will need to get the category, then if it's a categorical ability the exact kind. Then the XP value
     *
     * Requirements:
     *     1. Get Overarching Category
     *     2. List all abilities, and allow a selection from those
     *     3. Get subtype, if applicable
     *     4. Get XP value
     *     5. Add to applicable character
     *     6. Call addAbilityToDatabase() in order to add the newly created Ability
     */
    public Ability createAbility(){
        List<String> abilityOptions = AbilityEditor.getAbilityOptions(character);

        String selectedAbility = abilityOptions.get(super.getOptions(abilityOptions));
        String subtype = null;

        //Need to figure out a good way to check if an ability needs a subtype or not
        if(AbilityEditor.isCategorical(selectedAbility)){
            subtype = super.getString("\tName of Ability");
        }

        int xpValue = super.getInt("Initial Experience");

        String speciality = super.getString("Speciality");

        Ability ability = Ability.createAbility(selectedAbility, subtype, speciality, xpValue);

        if(ability == null){
            framework.printToTerminal("Error in Creating Ability");
            return null;
        }

        return ability;
    }
}
