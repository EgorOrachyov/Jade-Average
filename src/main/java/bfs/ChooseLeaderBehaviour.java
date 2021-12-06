package bfs;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ChooseLeaderBehaviour extends NumberAgentBehaviour {
    private int round = 0;
    private int received = 0;
    private boolean send = true;

    public ChooseLeaderBehaviour(NumberAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        if (send) {
            logger.info(self.getLocalName() + " vote round " + round);
            var neighbors = self.getLinks();
            for (var neighbor : neighbors) {
                var msg = new ACLMessage();
                msg.addReceiver(new AID(neighbor, AID.ISLOCALNAME));
                msg.setContent("vote " + round + " " + self.getLeader());
                self.send(msg);
            }
            send = false;
        }

        if (received < self.getLinks().size()) {
            var msg = self.receive();
            if (msg != null) {
                var content = msg.getContent();
                if (content.startsWith("vote")) {
                    var args = content.split(" ");
                    var roundId = Integer.parseInt(args[1]);
                    var leaderId = Integer.parseInt(args[2]);
                    assert roundId == round;

                    self.setLeader(Math.min(self.getLeader(), leaderId));
                    received += 1;
                }
                else
                    self.send(msg);
            }
        }

        if (received == self.getLinks().size()) {
            received = 0;
            round += 1;
            send = true;
        }
    }

    @Override
    public boolean done() {
        var finish = round >= self.getN();

        if (finish)
            // Defines how we will behave: leader or child
            self.addBehaviour(new TransitionBehaviour(self));

        return finish;
    }
}
