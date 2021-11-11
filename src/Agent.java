public class Agent extends Objet {

    public Agent(String name, Objet goal) {
        super(name, goal);
    }

    public void perception(Strategy strategy, Environment environment) {
        Objet underAgent = environment.getPreviousAgent(this);
        this.setFree(environment.getIsFree(this));
        this.setGoalAchieved(underAgent == this.getGoal());
        strategy.perception(this, environment);
    }


    public void action(Strategy strategy, Environment environment) {
        strategy.action(this, environment);
    }

    public void setPush(boolean isPushed) {
        this.setPushed(isPushed);
    }

}
