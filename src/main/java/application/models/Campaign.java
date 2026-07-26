package application.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class Campaign implements Cloneable{
//    private List<Character> playerCharacters;
    private List<Covenant> covenants;
    @Getter String name;
    @Getter int currentSeason;
    @EqualsAndHashCode.Exclude public List<Covenant> accessedCovenants;

    private Campaign(int id, String name, int season){
        this.currentSeason = season;
        this.name = name;

        this.covenants = new ArrayList<>();
    }

    private Campaign(){

    }

    public static Campaign buildCampaign(Map<String, String> map){
        Campaign campaign = new Campaign();
        campaign.name = map.get("name");
        campaign.currentSeason = Integer.parseInt(map.get("current_season"));
        campaign.covenants = new ArrayList<>();
        campaign.accessedCovenants = new ArrayList<>();

        return campaign;
    }

    public List<ArsCharacter> getCharacters(){
        List<ArsCharacter> characters = new ArrayList<>();

        for(Covenant covenant : covenants){
            characters.addAll(covenant.getPlayerCharacters());
        }

        return characters;
    }

    public static Campaign createCampaign(String name, int season){
        Campaign campaign = new Campaign();
        campaign.name = name;
        campaign.currentSeason = season;
        campaign.covenants = new ArrayList<>();

        return campaign;
    }

    /*
    In an ideal case also triggers updates for all covenants and characters
     */
    public void advanceSeason(){
        currentSeason++;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
