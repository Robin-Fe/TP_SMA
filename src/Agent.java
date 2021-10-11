public class Agent {

    private int id;
    private String name;
    private Agent goal;
    private Environnement environnement;
    private boolean isPushed;
    private boolean satisfied;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Environnement getEnvironnement() {
        return environnement;
    }

    public boolean seDeplacer(int x) {
        if (this.getEnvironnement().moveAgent(this, this.getEnvironnement().getPile(x))) {
            this.notPushed();
            return true;
        }
        this.getEnvironnement().getPreviousAgent(this).pushed();
        return false;
    }

    public void pushed() {
        isPushed = true;
    }

    public void notPushed() {
        isPushed = false;
    }

    public void setGoal(Agent agent) {
        this.goal = agent;
    }

    public Agent getGoal() {
        return this.goal;
    }

    public boolean checkSatisfied() {
        if (getGoal() == getEnvironnement().getNextAgent(this)) {
            this.satisfied = true;
            return true;
        }
        this.satisfied = false;
        return false;
    }


}
