package application.data;

import application.models.Campaign;
import application.models.Covenant;

import java.util.List;
import java.util.Optional;

public interface ICampaignDataSource {
    /*
    Load campaign table in order to access details about it, getting the characters will be a separate function.
     */
    Optional<Campaign> loadCampaignFromId(String campaignID);

    boolean updateCampaign(Campaign campaign);

    List<Campaign> getCampaigns();

    List<Covenant> loadCovenantsFromCampaign(Campaign campaign);

    boolean addCampaign(Campaign campaign);
}
