package lv;

import jade.core.Agent;

import java.util.HashSet;
import java.util.Set;

public class NumberAgent extends Agent {
    private int number;     // Random number we have (generated externally for test purposes only)
    private final Set<String> links = new HashSet<>();

    @Override
    protected void setup() {
        super.setup();

        var args = getArguments();

        if (args != null && args.length > 0) {
            number = Integer.parseInt(args[0].toString());

            for (int i = 1; i < args.length; i++)
                links.add(args[i].toString());
        }

        addBehaviour(new NumberAgentBehaviour(this));
    }

    public int getNumber() {
        return number;
    }

    public Set<String> getLinks() {
        return links;
    }
}
