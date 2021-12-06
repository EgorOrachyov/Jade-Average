package bfs;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ChildBfsBehaviour extends NumberAgentBehaviour {
    private boolean visited = false;
    private boolean forward = false;
    private boolean feedback = false;
    private boolean sent = false;
    private int no = 0;
    private int response = 0;
    private int sum;
    private int k = 1;
    private String parent;

    public ChildBfsBehaviour(NumberAgent agent) {
        super(agent);
        sum = self.getNumber();
    }

    @Override
    public void action() {
        var msg = self.receive();
        if (msg != null) {
            var content = msg.getContent();
            // If not visited and get incoming visit -> mark as visited
            if (!visited && content.startsWith("visit")) {
                logger.info(self.getLocalName() + " get visit from " + msg.getSender().getLocalName() + " (yet not visited, accept)");
                var args = content.split(" ");
                parent = args[1];
                forward = true;
                visited = true;
            }
            // If visited say that we already visited (no fwd)
            else if (visited && content.startsWith("visit")) {
                logger.info(self.getLocalName() + " get visit " + msg.getSender().getLocalName() + " (already visited, decline)");
                var response = new ACLMessage();
                response.addReceiver(msg.getSender());
                response.setContent("no");
                self.send(response);
            }
            // If we wait for children - no says, that no response (to know how much to wait)
            else if (feedback && content.startsWith("no")) {
                logger.info(self.getLocalName() + " get no from " + msg.getSender().getLocalName());
                no += 1;
            }
            // If we wait for children - response with data
            else if (feedback && content.startsWith("response")) {
                logger.info(self.getLocalName() + " get response from " + msg.getSender().getLocalName());
                var args = content.split(" ");
                sum += Integer.parseInt(args[1]);
                k += Integer.parseInt(args[2]);
                response += 1;
            }
        }

        // If we visited - try to forward response to children
        if (forward) {
            var children = self.getLinks();
            for (var child : children) {
                if (!child.equals(parent)) {
                    logger.info(self.getLocalName() + " send visit to " + child);
                    var fwd = new ACLMessage();
                    fwd.addReceiver(new AID(child, AID.ISLOCALNAME));
                    fwd.setContent("visit " + self.getLocalName());
                    self.send(fwd);
                }
            }
            forward = false;
            feedback = true;
        }

        // If response + no is all, we can send result to our parent
        if (visited && !sent) {
            if ((response + no) == (self.getLinks().size() - 1)) {
                logger.info(self.getLocalName() + " send response to " + parent);
                var response = new ACLMessage();
                response.addReceiver(new AID(parent, AID.ISLOCALNAME));
                response.setContent("response " + sum + " " + k);
                self.send(response);
                sent = true;
            }
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
