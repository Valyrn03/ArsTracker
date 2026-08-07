package application.commands.character;

import application.data.CharacterDataSource;
import application.data.ICharacterDataSource;
import application.data.IDataSource;
import application.models.Ability;
import application.models.ArsCharacter;
import application.models.CharacterFeature;
import application.Command;
import application.CommandFramework;
import application.models.enums.Art;
import application.models.enums.Attribute;

import java.util.ArrayList;
import java.util.List;

import static application.utils.artExperienceToScore;
import static application.utils.format;
import static java.lang.Math.max;

public class ShowCharacterCommand implements Command {
    private CommandFramework framework;
    IDataSource dataSource;

    public ShowCharacterCommand(CommandFramework framework, IDataSource dDataSource){
        this.framework = framework;
        this.dataSource = dDataSource;
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

        ArsCharacter character = framework.getActiveCharacter().orElseThrow();
        framework.put("%s (%s), of %s\nAttributes:", character.getName(), format(character.getCharacterType()), framework.getActiveCovenant().get().getName());

        for(Attribute attribute : Attribute.values()){
            framework.put("\t%s: %d", format(attribute), character.getAttribute(attribute));
        }

        ICharacterDataSource characterDataSource = new CharacterDataSource(dataSource);
        characterDataSource.loadFeaturesFromCharacter(character).forEach((feature) -> {
            if(!character.getFeatures().contains(feature)){
                character.addFeature(feature);
            }
        });
        if(!character.getFeatures().isEmpty()){
            character.getFeatures().sort(null);

            framework.put("Virtues:");

            List<CharacterFeature> flaws = new ArrayList<>();

            for(CharacterFeature feature : character.getFeatures()){
                if(feature.getType() == CharacterFeature.FeatureType.FLAW){
                    flaws.add(feature);
                    continue;
                }

                framework.put("\t%s", feature.toString());
            }

            framework.put("Flaws:");

            for(CharacterFeature feature: flaws){
                framework.put("\t%s", feature.toString());
            }
        }

        if(character.getCharacterType().equals(ArsCharacter.CharacterType.MAGUS)){
            int highest = 0;
            for(Art art : Art.values()){
                characterDataSource.loadCharacterArt(character, art);
                highest = max(highest, character.getArt(art));
            }
            if(highest > 0){
                framework.put("Arts:");

                for(Art art : Art.values()){
                    if(character.getArt(art) > 0){
                        framework.put("\t%s: %d", format(art), artExperienceToScore(character.getArt(art)));
                    }
                }
            }
        }

        for(Ability ability : dataSource.loadAbilitiesById(character.getId())){
            character.addAbility(ability);
        }
        if(!character.getAbilities().isEmpty()){
            framework.put("Abilities:");

            for(Ability ability : character.getAbilities()){
                framework.put("\t%s", ability.toString());
            }
        }

        return true;
    }
}
