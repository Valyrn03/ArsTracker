package application.commands;

import application.Command;
import application.CommandFramework;
import application.data.IDataSource;
import application.models.Covenant;

import java.util.ArrayList;
import java.util.List;

public class ListCovenantsCommand implements Command {
    CommandFramework framework;
    IDataSource dataSource;

    public ListCovenantsCommand(CommandFramework fr, IDataSource src){
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
}
