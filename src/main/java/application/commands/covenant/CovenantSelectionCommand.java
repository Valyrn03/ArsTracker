package application.commands.covenant;

import application.Command;
import application.CommandFramework;
import application.data.ICovenantDataSource;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CovenantSelectionCommand implements Command {
    CommandFramework framework;
    ICovenantDataSource dataSource;

    public CovenantSelectionCommand(CommandFramework fr, ICovenantDataSource ds){
        framework = fr;
        dataSource = ds;
    }
    @Override
    public boolean execute() {
        if(framework.getActiveCampaign().isEmpty()){
            log.error("Framework does not have campaign set");
            return false;
        }
        List<Covenant> covenantList = framework.getActiveCampaign().get().getCovenants();

        List<Integer> covenantIds = dataSource.loadCovenantIdsFromCampaign(framework.getActiveCampaign().get());
        covenantIds.removeAll(covenantList.stream().map(Covenant::getId).toList());

        for(int id : covenantIds){
            dataSource.loadCovenantFromId(id).map(covenantList::add);
        }

        if(covenantList.isEmpty()){
            framework.put("0 Covenants Found");
            return true;
        }

        int chosenCovenant = framework.getOptionsIndex(covenantList.stream().map(Covenant::getName));
        framework.setActiveCovenant(covenantList.get(chosenCovenant));

        if(framework.getActiveCovenant().isEmpty()){
            log.error("Didn't set active covenant");
            return false;
        }
        framework.put("Selected Covenant %s", framework.getActiveCovenant().get().getName());
        return true;
    }
}
