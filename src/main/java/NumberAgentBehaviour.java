import jade.core.behaviours.Behaviour;

import java.util.logging.Logger;

public abstract class NumberAgentBehaviour extends Behaviour {
    protected final Logger logger;
    protected final NumberAgent self;

    NumberAgentBehaviour(NumberAgent agent) {
        this.self = agent;
        this.logger = Logger.getLogger(this.getClass().getName());
    }
}
