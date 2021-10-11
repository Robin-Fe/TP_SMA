public class Agent {
    private String name;
    private Agent goal;
    private Boolean goalAchieved;
    private Agent underAgent;
    private Boolean isPushed;
    private Boolean isFree;

    public Agent(String name, Agent goal) {
        this.name = name;
        this.goal = goal;
        this.goalAchieved = false;
        this.isPushed = false;
    }

    public String getName() {
        return name;
    }

    public Agent getGoal() {
        return goal;
    }

    public Boolean getGoalAchieved() {
        return goalAchieved;
    }

    public Agent getUnderAgent() {
        return underAgent;
    }

    public Boolean getPushed() {
        return isPushed;
    }

    public Boolean getFree() {
        return isFree;
    }



    public void perception(Environnement environment) {
        this.underAgent = environment.getNextAgent(this);
        this.isFree = environment.getIsFree(this);
        System.out.println("IS FREEEE ?" + this.isFree);
        this.goalAchieved = underAgent == goal || goal == null;
    }

    public void action(Environnement environment) {
        if (isPushed || !goalAchieved) {
            if (this.isFree) {

                // ToDo : choose index on the table
                int index = 1;
                environment.seDeplacer(this, index);
                this.isPushed = false;
            } else {
                environment.push(this);
            }
        }
    }

    public void setPush(boolean isPushed) {
        this.isPushed = isPushed;
    }

}
