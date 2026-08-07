package application;

import application.models.ArsCharacter;
import application.models.Campaign;
import application.models.Covenant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.util.*;
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

        accessedCampaigns = new ArrayList<>();
    }

    public void setActiveCampaign(Campaign campaign){
        activeCampaign = Optional.of(campaign);
        accessedCampaigns.add(campaign);

        activeCovenant = Optional.empty();
        activeCharacter = Optional.empty();
    }

    public void setActiveCovenant(Covenant covenant){
        activeCovenant = Optional.of(covenant);
        try{
            activeCampaign.orElseThrow(NoSuchElementException::new).accessedCovenants.add(covenant);
        }catch (NoSuchElementException exp){
            log.error("Attempted to select covenant while no campaign was active");
        }

        activeCharacter = Optional.empty();
    }

    public void setActiveCharacter(ArsCharacter character){
        activeCharacter = Optional.of(character);
        try{
            activeCovenant.orElseThrow(NoSuchElementException::new).accessedCharacters.add(character);
        }catch (NoSuchElementException exp){
            log.error("Attempted to add active character when active campaign is not set");
        }
    }

    public int getInt(String prompt, Object... values){
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        for(Object val : values){
            prompt = prompt.replace("{}", val.toString());
        }

        String line = reader.readLine(prompt + "> ");

        try{
            return Integer.parseInt(line);
        }catch (NumberFormatException exp){
            return getInt("\tMust be a number");
        }
    }

    public int getIntLimited(String prompt, int lower, int higher, Object... values){
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        for(Object val : values){
            prompt = prompt.replace("{}", val.toString());
        }

        String line = reader.readLine(prompt + "> ");

        try{
            int result = Integer.parseInt(line);
            if (result < lower || result > higher){
                throw new IndexOutOfBoundsException();
            }
            return result;
        }catch (NumberFormatException exp){
            return getIntLimited("\tMust be a number", lower, higher);
        }catch (IndexOutOfBoundsException exp){
            return getIntLimited("\tMust be within the range of {} and {}", lower, higher);
        }
    }

    public int getOptionsIndex(Stream<String> options){
        terminal.writer().println("Choose one of the following options:");
        List<String> list = options.toList();
        return getOptionsIndex(options.toList());
    }

    public int getOptionsIndex(List<String> options){
        terminal.writer().println("Choose one of the following options:");
        for(int i = 0; i < options.size(); i++){
            terminal.writer().println(String.format("\t%d. %s", i, options.get(i)));
        }

        String input = getString(">");
        try{
            int index = Integer.parseInt(input);
            if(index < 1 || index > options.size() + 1){
                return getIntLimited(">", 1, options.size() + 1);
            }
            return index;
        }catch (NumberFormatException exp){
            return options.stream().map(String::toLowerCase).toList().indexOf(input.toLowerCase());
        }
    }

    public String getOptions(List<String> options){
        terminal.writer().println("Choose one of the following options:");

        for(int i = 0; i < options.size(); i++){
            terminal.writer().println(String.format("\t%d: %s\n", i, options.get(i)));
        }

        String input = getString("> ");
        try{
            int index = Integer.parseInt(input);
            if(index < 1 || index > options.size() + 1){
                index = getIntLimited(">", 1, options.size() + 1);
            }
            return options.get(index - 1);
        }catch (NumberFormatException exp){
            return input;
        }
    }

    public String getOptions(Stream<String> options){
        terminal.writer().println("Choose one of the following options:");
        return getOptions(options.toList());
    }



    public String getString(String prompt){
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        return reader.readLine(prompt + ">> ");
    }

    public void put(String prompt, Object... args){
        terminal.writer().println(String.format(prompt, args));
    }

    public void put(List<Object> list){
        for(Object obj : list){
            terminal.writer().println(obj.toString());
        }
    }

    public void put(Stream<Object> stream){
        stream.forEach((obj) -> {
            terminal.writer().println(String.format("\t%s", obj.toString()));
        });
    }
}
