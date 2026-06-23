package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.ICampaignDataSource;
import application.models.Covenant;

import java.util.ArrayList;
import java.util.List;

public class ListCovenantsCommand implements Command {
    CommandFramework framework;
    ICampaignDataSource dataSource;

    public ListCovenantsCommand(CommandFramework fr, ICampaignDataSource src){
        this.framework = fr;
        this.dataSource = src;
    }

    @Override
    public boolean execute() {
        List<Covenant> covenants = new ArrayList<>();
        framework.getActiveCampaign().map(dataSource::loadCovenantsFromCampaign).map(covenants::addAll);

        covenants.forEach((covenant -> {framework.put(covenant.getName());}));

        return !covenants.isEmpty();
    }

    @Override
    public String name() {
        return "ListCovenantsCommand";
    }
}
