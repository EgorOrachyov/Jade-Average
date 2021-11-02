import jade.core.behaviours.Behaviour;

import java.util.logging.Logger;

public abstract class NumberAgentBehaviour extends Behaviour {
    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    protected final NumberAgent self;

    NumberAgentBehaviour(NumberAgent agent) {
        this.self = agent;
    }
}
