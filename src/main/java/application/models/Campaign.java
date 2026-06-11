package application.models;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class Campaign {
    public UUID id;
//    private List<Character> playerCharacters;
    private List<Covenant> covenants;
    @Getter String name;
    @Getter int currentSeason;
    public List<Character> accessedCharacters;

    private Campaign(UUID id, String name, int season){
        this.id = id;
        this.currentSeason = season;
        this.name = name;

        this.covenants = new ArrayList<>();
    }

    private Campaign(){

    }

    public static Campaign buildCampaign(Map<String, String> map){
        Campaign campaign = new Campaign();
        campaign.id = UUID.fromString(map.get("id"));
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
        campaign.id = UUID.randomUUID();
        campaign.name = name;
        campaign.currentSeason = season;
        campaign.covenants = new ArrayList<>();

        return campaign;
    }
}
