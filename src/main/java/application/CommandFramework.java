package application;

import application.models.ArsCharacter;
import application.models.Campaign;
import application.models.ArsCharacter;
import application.models.Covenant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.mock.MockTextTerminal;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
public class CommandFramework {
    @Setter private Terminal terminal;
    @Getter private Optional<Campaign> activeCampaign;
    @Getter private Optional<Covenant> activeCovenant;
    @Getter private Optional<ArsCharacter> activeCharacter;
    @Getter List<Campaign> accessedCampaigns;

    public CommandFramework(Terminal io){
        terminal = io;

        activeCampaign = Optional.empty();
        activeCovenant = Optional.empty();
        activeCharacter = Optional.empty();
    }

    public void setActiveCampaign(Campaign campaign){
        activeCampaign = Optional.of(campaign);
        accessedCampaigns.add(campaign);
    }

    public void setActiveCovenant(Covenant covenant){
        activeCovenant = Optional.of(covenant);
        try{
            activeCampaign.orElseThrow(NoSuchElementException::new).accessedCovenants.add(covenant);
        }catch (NoSuchElementException exp){
            log.error("Attempted to select covenant while no campaign was active");
        }
    }

    public void setActiveCharacter(ArsCharacter character){
        activeCharacter = Optional.of(character);
        try{
            activeCovenant.orElseThrow(NoSuchElementException::new).accessedCharacters.add(character);
        }catch (NoSuchElementException exp){
            log.error("Attempted to add active character when active campaign is not set");
        }
    }

    public int getInt(String prompt){
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        String line = reader.readLine(prompt + "> ");

        try{
            return Integer.parseInt(line);
        }catch (NumberFormatException exp){
            return getInt("\t");
        }
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
