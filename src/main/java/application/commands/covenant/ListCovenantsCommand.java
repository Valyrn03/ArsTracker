package application.commands.covenant;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;
import application.data.ICovenantDataSource;
import application.models.Campaign;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ListCovenantsCommand implements Command {
    CommandFramework framework;
    ICampaignDataSource campaignDataSource;
    ICovenantDataSource covenantDataSource;

    public ListCovenantsCommand(CommandFramework fr, ICampaignDataSource src, ICovenantDataSource csrc){
        this.framework = fr;
        this.campaignDataSource = src;
        this.covenantDataSource = csrc;
    }

    @Override
    public boolean execute() {
        if(framework.getActiveCampaign().isEmpty()){
            log.info("Active campaign is not set");
            return false;
        }

        log.info(framework.getActiveCampaign().get().getName());
        List<Covenant> covenants = framework.getActiveCampaign().get().getCovenants();

        Campaign campaign = framework.getActiveCampaign().get();
        List<Integer> covenantIds = campaignDataSource.loadCovenantIdsFromCampaign(campaign);
//        log.info(campaignDataSource.loadCovenantIdsFromCampaign(campaign).toString());
//        List<Integer> covenantIds = framework.getActiveCampaign().map(campaignDataSource::loadCovenantIdsFromCampaign).orElseThrow();
        covenantIds.removeAll(covenants.stream().map(Covenant::getId).toList());

        for(int id : covenantIds){
            Optional<Covenant> covenant = covenantDataSource.loadCovenantFromId(id);

            covenant.ifPresent(covenants::add);
//            covenant.ifPresent(framework.getActiveCampaign().get()::addCovenant);
        }

        covenants.sort(null);
        framework.put("Covenants:");
        framework.put(covenants.stream().map(Covenant::getName));
        return true;
    }
}
