public class TransitionBehaviour extends NumberAgentBehaviour {
    TransitionBehaviour(NumberAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        var isLeader = self.getId() == self.getLeader();
        if (isLeader) self.addBehaviour(new LeaderBfsBehaviour(self));
        else self.addBehaviour(new ChildBfsBehaviour(self));
    }

    @Override
    public boolean done() {
        return true;
    }
}
