import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ChildBfsBehaviour extends NumberAgentBehaviour {
    private boolean visited = false;
    private boolean forward = false;
    private boolean feedback = false;
    private boolean finish = false;
    private int no = 0;
    private int response = 0;
    private int sum = 0;
    private int k = 0;
    private String parent;

    public ChildBfsBehaviour(NumberAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        var msg = self.receive();
        if (msg != null) {
            var content = msg.getContent();
            // If not visited and get incoming visit -> mark as visited
            if (!visited && content.startsWith("visit")) {
                var response = new ACLMessage();
                response.addReceiver(msg.getSender());
                response.setContent("ok");
                self.send(response);
                var args = content.split(" ");
                parent = args[1];
                forward = true;
                visited = true;
                // If visited say that we already visited (no fwd)
            } else if (visited && content.startsWith("visit")) {
                var response = new ACLMessage();
                response.addReceiver(msg.getSender());
                response.setContent("no");
                self.send(response);
            }
            // If we wait for children - no says, that no response (to know how much to wait)
            else if (feedback && content.startsWith("no")) {
                no += 1;
                // If all said no - we are leaf node
                if (no == self.getLinks().size() - 1) {
                    var response = new ACLMessage();
                    response.addReceiver(new AID(parent, AID.ISLOCALNAME));
                    response.setContent("response " + self.getNumber() + " " + 1);
                    self.send(response);
                    finish = true;
                }
            }
            // If we wait for children - response with data
            else if (feedback && content.startsWith("response")) {
                var args = content.split(" ");
                sum += Integer.parseInt(args[1]);
                k += Integer.parseInt(args[2]);
                response += 1;
                // If response + no is all, we can send result to our parent
                if ((response + no) == (self.getLinks().size() - 1)) {
                    var response = new ACLMessage();
                    response.addReceiver(new AID(parent, AID.ISLOCALNAME));
                    response.setContent("response " + (sum + self.getNumber()) + " " + (k + 1));
                    self.send(response);
                    finish = true;
                }
            }
        }

        // If we visited - try to forward response to children
        if (forward) {
            var children = self.getLinks();
            for (var child : children) {
                if (!child.equals(parent)) {
                    var fwd = new ACLMessage();
                    fwd.addReceiver(new AID(child, AID.ISLOCALNAME));
                    fwd.setContent("visit " + self.getName());
                }
            }
            forward = false;
            feedback = true;
        }
    }

    @Override
    public boolean done() {
        return finish;
    }
}
