public class TransitionBehaviour extends NumberAgentBehaviour {
    TransitionBehaviour(NumberAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        var isLeader = self.getId() == self.getLeader();

        if (isLeader) {
            logger.info(self.getLocalName() + " I am leader, go to bfs");
            self.addBehaviour(new LeaderBfsBehaviour(self));
        }
        else {
            logger.info(self.getLocalName() + " I am child, go to bfs");
            self.addBehaviour(new ChildBfsBehaviour(self));
        }
    }

    @Override
    public boolean done() {
        return true;
    }
}
