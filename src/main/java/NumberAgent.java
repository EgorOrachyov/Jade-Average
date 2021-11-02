import jade.core.Agent;

import java.util.logging.Logger;

public class NumberAgent extends Agent {
    private final Logger logger = Logger.getLogger(NumberAgent.class.getName());

    @Override
    protected void setup() {
        super.setup();
        logger.info("setup");
    }
}
