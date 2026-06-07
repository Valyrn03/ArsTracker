package application.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Campaign {
    public UUID id;
    private List<Character> playerCharacters;
    @Getter String name;
    @Getter int currentSeason;

    private Campaign(UUID id, String name, int season){
        this.id = id;
        this.currentSeason = season;
        this.name = name;

        this.playerCharacters = new ArrayList<>();
    }

    private Campaign(){

    }

    public static Campaign buildCampaign(Map<String, String> map){
        Campaign campaign = new Campaign();
        campaign.id = UUID.fromString(map.get("id"));
        campaign.name = map.get("name");
        campaign.currentSeason = Integer.parseInt(map.get("current_season"));
        campaign.playerCharacters = new ArrayList<>();

        return campaign;
    }

    public List<Character> getCharacters(){
        return this.playerCharacters;
    }

    public static Campaign createCampaign(String name, int season){

    }

    public static Campaign loadCampaignFromName(String name){

    }
}
