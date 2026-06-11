package application.commands;

import application.models.Ability;
import application.models.Character;
import application.models.CharacterFeature;
import application.Command;
import application.CommandFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterOutputCommand implements Command {
    private CommandFramework framework;

    public CharacterOutputCommand(CommandFramework framework){
        this.framework = framework;
    }

    /*
    Print out the major details of the chosen character

    This will consist of a name, character type, the attributes, the abilities (score & exp), and the virtues & flaws

    The extraneous attributes will not be printed, due being optional
     */
    @Override
    public boolean execute() {
        if(framework.getActiveCharacter().isEmpty()){
            framework.put("There Is No Active Character");
            return false;
        }

        Character character = framework.getActiveCharacter().orElseThrow();
        framework.put("%s (%s)\n----------------------------\nAttributes: ", character.getName(), character.getCharacterType());

        for(Map.Entry<String, Integer> attribute: character.getAttributes().entrySet()){
            framework.put("\t%s: %d", attribute.getKey(), attribute.getValue());
        }

        framework.put("----------------------------\nAbilities: ");

        for(Ability ability : character.getAbilities()){
            framework.put("\t%s", ability.toString());
        }

        framework.put("----------------------------\nVirtues: ");

        List<CharacterFeature> flaws = new ArrayList<>();

        for(CharacterFeature feature : character.getFeatures()){
            if(feature.isFlaw()){
                flaws.add(feature);
                continue;
            }

            framework.put("\t%s", feature.toString());
        }

        framework.put("----------------------------\nFlaws: ");

        for(CharacterFeature feature: flaws){
            framework.put("\t%s", feature.toString());
        }

        return true;
    }
}
