import jade.core.behaviours.Behaviour;

import java.util.logging.Logger;

public class NumberAgentBehaviour extends Behaviour {
    private final Logger logger = Logger.getLogger(Behaviour.class.getName());
    private boolean finished = false;

    @Override
    public void action() {
        logger.info("action");
        finished = true;
    }

    @Override
    public boolean done() {
        return finished;
    }
}
