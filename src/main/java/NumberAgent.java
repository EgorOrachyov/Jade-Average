import jade.core.Agent;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class NumberAgent extends Agent {
    private final Logger logger = Logger.getLogger(NumberAgent.class.getName());
    private int id;         // Unique if of the agent
    private int n;          // Max number of agents in the network
    private int number;     // Random number we have (generated externally for test purposes only)
    private int leader;     // Identifier of the leader
    private final Set<String> links = new HashSet<>();

    @Override
    protected void setup() {
        super.setup();
        var args = getArguments();

        if (args != null && args.length > 0) {
            leader = id = Integer.parseInt(args[0].toString());
            n = Integer.parseInt(args[1].toString());
            number = Integer.parseInt(args[2].toString());

            for (int i = 3; i < args.length; i++)
                links.add(args[i].toString());
        }

        // Executed first to choose leader
        addBehaviour(new ChooseLeaderBehaviour(this));
    }

    @Override
    protected void takeDown() {
        logger.info("Finish " + getLocalName());
    }

    public int getId() {
        return id;
    }

    public int getN() {
        return n;
    }

    public int getNumber() {
        return number;
    }

    public Set<String> getLinks() {
        return links;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }
}
