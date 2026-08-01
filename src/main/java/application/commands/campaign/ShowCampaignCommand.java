package application.commands.campaign;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;
import application.data.ICovenantDataSource;
import application.models.Campaign;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ShowCampaignCommand implements Command {
    CommandFramework framework;
    ICovenantDataSource covenantDataSource;

    public ShowCampaignCommand(CommandFramework fr, ICovenantDataSource oDataSrc){
        this.framework = fr;
        this.covenantDataSource = oDataSrc;
    }

    @Override
    public boolean execute() {
        if(framework.getActiveCampaign().isEmpty()){
            log.error("Active campaign is not set");
            return false;
        }

        Campaign campaign = framework.getActiveCampaign().get();
        List<Integer> covenantIds = covenantDataSource.loadCovenantIdsFromCampaign(campaign);

        framework.put("Name: %s", campaign.getName());
        framework.put("Season: %d", campaign.getCurrentSeason());

        if(!covenantIds.isEmpty()){
            framework.put("Covenants:");
            framework.put(covenantIds.stream().map(covenantDataSource::loadCovenantFromId).filter(Optional::isPresent).map(Optional::get).map(Covenant::getName));
        }

        return true;
    }
}
