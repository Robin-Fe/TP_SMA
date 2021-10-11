import java.util.Random;

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
        this.underAgent = environment.getPreviousAgent(this);
        this.isFree = environment.getIsFree(this);
        this.goalAchieved = underAgent == goal;
    }

    public void action(Environnement environment) {
        if (!goalAchieved) {
            tryToMove(environment);
        } else {
            if (isPushed) {
                tryToMove(environment);
            } else {
                System.out.println(this.getName() + " ne fait rien");
            }
        }

    }

    private void tryToMove(Environnement environment) {
        if (isFree) {
            // ToDo : change 3 to number of piles
            int index = new Random().nextInt(3);
            System.out.println(this.getName() + " bouge vers " + index);
            environment.seDeplacer(this, index);
        } else {
            System.out.println(this.getName() + " push");
            environment.push(this);
        }
    }

    public void setPush(boolean isPushed) {
        this.isPushed = isPushed;
    }

}
