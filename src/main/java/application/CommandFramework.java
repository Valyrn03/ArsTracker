package application;

import application.models.Campaign;
import application.models.Character;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
public class CommandFramework {
    public TextIO source;
    private TextTerminal terminal;
    @Getter private Optional<Campaign> activeCampaign;
    @Getter private Optional<Character> activeCharacter;
    @Getter List<Campaign> accessedCampaigns;

    public CommandFramework(TextIO io){
        source = io;
        terminal = source.getTextTerminal();
        log.info("Terminal Properties: {}", terminal.getProperties().getAllKeys().toString());

        activeCampaign = Optional.empty();
        activeCharacter = Optional.empty();
    }

    public void setActiveCampaign(Campaign campaign){
        activeCampaign = Optional.of(campaign);
        accessedCampaigns.add(campaign);
    }

    public void setActiveCharacter(Character character){
        activeCharacter = Optional.of(character);
        try{
            activeCampaign.orElseThrow(NoSuchElementException::new).accessedCharacters.add(character);
        }catch (NoSuchElementException exp){
            log.error("Attempted to add active character when active campaign is not set");
        }
    }

    public int getInt(String prompt){
        return source.newIntInputReader().read(prompt + " >");
    }

    public int getOptions(Stream<String> options){
        terminal.println("Choose one of the following options:");
        AtomicInteger i = new AtomicInteger();
        options.forEachOrdered((option) -> {
            terminal.printf("\t%d: %s\n", i, option);
            i.getAndIncrement();
        });
        return source.newIntInputReader().read(" >");
    }

    public String getString(String prompt){
        return source.newStringInputReader().read(prompt + " >");
    }

    public void put(String prompt, Object... args){
        terminal.println(String.format(prompt, args));
    }

    public void put(List<Object> list){
        for(Object obj : list){
            terminal.println(obj.toString());
        }
    }
}
