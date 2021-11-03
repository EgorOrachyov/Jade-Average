import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class LeaderBfsBehaviour extends NumberAgentBehaviour {
    private boolean init = true;
    private boolean finish = false;
    private int received = 0;
    private int sum = 0;
    private int n = 0;
    private int no = 0;

    public LeaderBfsBehaviour(NumberAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        if (init) {
            var children = self.getLinks();
            for (var child : children) {
                logger.info("send visit to " + child);
                var msg = new ACLMessage();
                msg.addReceiver(new AID(child, AID.ISLOCALNAME));
                msg.setContent("visit " + self.getLocalName());
                self.send(msg);
            }

            init = false;
        }

        if ((received + no) < self.getLinks().size()) {
            var msg = self.receive();
            if (msg != null) {
                var content = msg.getContent();
                if (content.startsWith("response")) {
                    logger.info("get response from " + msg.getSender().getLocalName());
                    var args = msg.getContent().split(" ");
                    sum += Integer.parseInt(args[1]);
                    n += Integer.parseInt(args[2]);
                    received += 1;
                }
                else if (content.startsWith("no")) {
                    logger.info("get no from " + msg.getSender().getLocalName());
                    no += 1;
                }
                else if (content.startsWith("visit")) {
                    logger.info("get visit from " + msg.getSender().getLocalName() + " (leader, decline)");
                    var response = new ACLMessage();
                    response.addReceiver(msg.getSender());
                    response.setContent("no");
                    self.send(response);
                }
            }
        }

        if ((received + no) == self.getLinks().size()) {
            float average = (float) (sum + self.getNumber()) / (float) (n + 1);
            logger.info("Computed AVERAGE: " + average);
            finish = true;
        }
    }

    @Override
    public boolean done() {
        return finish;
    }
}
