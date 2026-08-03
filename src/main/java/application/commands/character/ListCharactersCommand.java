package application.commands.character;

import application.Command;
import application.CommandFramework;
import application.data.ICharacterDataSource;
import application.models.ArsCharacter;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ListCharactersCommand implements Command {
    CommandFramework framework;
    ICharacterDataSource dataSource;

    public ListCharactersCommand(CommandFramework framework, ICharacterDataSource characterDataSource){
        this.framework = framework;
        this.dataSource = characterDataSource;
    }

    @Override
    public boolean execute() {
        if(framework.getActiveCovenant().isEmpty()){
            log.error("Active covenant is not set");
            return false;
        }

        Covenant covenant = framework.getActiveCovenant().get();
        List<ArsCharacter> queriedCharacters = dataSource.loadCovenantCharacters(covenant);

        queriedCharacters.forEach(character -> {
            if(!covenant.getPlayerCharacters().contains(character)){
                covenant.addCharacter(character);
            }
        });

        if(covenant.getPlayerCharacters().isEmpty()){
            framework.put("0 Characters Found");
            return true;
        }

        framework.put("Characters:");
        framework.put(covenant.getPlayerCharacters().stream().map(ArsCharacter::toStringShortened));
        return true;
    }
}
