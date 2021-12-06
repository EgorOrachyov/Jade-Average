package lv;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;
import java.util.logging.Logger;

public class NumberAgentBehaviour extends Behaviour {
    public static final double alpha = 0.0015f;
    public static final double maxError = 0.01f;
    public static final double probabilityBroken = 0.05f;
    public static final double probabilityDelayed = 0.05f;
    public static final int iterations = 1000100;

    private final Logger logger = Logger.getLogger(NumberAgentBehaviour.class.getName());

    private double sum;
    private int iters = 0;
    private boolean isDone = false;
    private final NumberAgent agent;
    private final Random rnd = new Random();

    NumberAgentBehaviour(NumberAgent agent) {
        this.agent = agent;
        this.sum = agent.getNumber();
    }

    @Override
    public void action() {
        var x = sum;
        var prev = sum;
        var neighbors = agent.getLinks();

        for (var neighbor : neighbors) {
            var msg = new ACLMessage();
            var error = (rnd.nextDouble() * 2.0f - 1.0f) * maxError; // Simulate error
            msg.addReceiver(new AID(neighbor, AID.ISLOCALNAME));
            msg.setContent(String.valueOf(sum + error));
            agent.send(msg);
        }

        var received = 0;

        while (received < neighbors.size()) {
            var msg = agent.receive();

            if (msg != null) {
                received += 1;

                // Simulate broken connection
                if (rnd.nextFloat() <= probabilityBroken) {
                    continue;
                }

                // Simulate delay
                if (rnd.nextFloat() <= probabilityDelayed) {
                    agent.send(msg);
                    continue;
                }

                // Receive as normal
                sum += alpha * (Double.parseDouble(msg.getContent()) - sum);
            }
        }

        iters += 1;

        if (iters >= iterations)
            isDone = true;

        if (iters % 20000 == 0)
            logger.info("TMP RES: " + agent.getLocalName() + " x_" + iters + "=" + sum);

        if (isDone && agent.getLocalName().equals("ag-mt-0"))
            logger.info("computed AVERAGE: " + sum);
    }

    @Override
    public boolean done() {
        return isDone;
    }

}
