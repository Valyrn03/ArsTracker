package application.models;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class Campaign implements Cloneable{
    public int id;
//    private List<Character> playerCharacters;
    private List<Covenant> covenants;
    @Getter String name;
    @Getter int currentSeason;
    public List<Covenant> accessedCovenants;

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

        return campaign;
    }

    public List<Character> getCharacters(){
        List<Character> characters = new ArrayList<>();

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

    //Not completely correct because ignores covenants
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Campaign other)){
            return false;
        }

        if(this.id != other.id){
            return false;
        }

        if(!this.name.equals(other.name)){
            return false;
        }

        if(this.currentSeason != other.currentSeason){
            return false;
        }

        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
