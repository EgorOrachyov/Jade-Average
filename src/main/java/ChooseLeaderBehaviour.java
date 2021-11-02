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
            var neighbors = self.getLinks();
            for (var neighbor : neighbors) {
                var msg = new ACLMessage();
                msg.addReceiver(new AID(neighbor, AID.ISLOCALNAME));
                msg.setContent(String.valueOf(round) + " " + self.getLeader());
                self.send(msg);
            }
            send = false;
        }

        if (received < self.getLinks().size()) {
            var msg = self.receive();
            if (msg != null) {
                var content = msg.getContent();
                var args = content.split(" ");
                var roundId = Integer.parseInt(args[0]);
                var leaderId = Integer.parseInt(args[1]);
                assert roundId == round;

                self.setLeader(Math.min(self.getLeader(), leaderId));
                received += 1;
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
        return round >= self.getN();
    }
}
