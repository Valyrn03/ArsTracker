package application.commands.covenant;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;
import application.data.ICovenantDataSource;
import application.models.Covenant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        List<Integer> covenantIds = framework.getActiveCampaign().map(campaignDataSource::loadCovenantIdsFromCampaign).orElse(Collections.emptyList());
        framework.put("Covenants:");
        framework.put(covenantIds.stream().map(covenantDataSource::loadCovenantFromId).filter(Optional::isPresent).map(Optional::get).map(Covenant::getName));
        return true;
    }
}
