package application.data;

import application.models.Campaign;
import application.models.Covenant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICampaignDataSource {
    /*
    Load campaign table in order to access details about it, getting the characters will be a separate function.
     */
    Optional<Campaign> loadCampaignFromName(String name);

    /*
    Due to the campaign name being the primary key, for now will not allow renaming of the campaign.

    As such, the only thing that can be updated is the season the campaign is in.
     */
    boolean updateCampaign(Campaign campaign);

    List<Campaign> getCampaigns();

    boolean addCampaign(String name, int seasons);

    boolean addCampaign(Campaign campaign);
}
