package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;
import application.data.ICovenantDataSource;
import application.models.Covenant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<Covenant> covenants = new ArrayList<>();
        List<Integer> covenantIds = framework.getActiveCampaign().map(campaignDataSource::loadCovenantIdsFromCampaign).orElse(Collections.emptyList());

        for(int id : covenantIds){
            covenantDataSource.loadCovenantFromId(id).ifPresent(covenants::add);
        }

        return !covenants.isEmpty();
    }
}
