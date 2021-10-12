import java.util.Random;

public class Agent {
    private final String name;
    private final Agent goal;
    private Boolean goalAchieved;
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


    public Boolean getGoalAchieved() {
        return goalAchieved;
    }


    public void perception(Environnement environment) {
        Agent underAgent = environment.getPreviousAgent(this);
        this.isFree = environment.getIsFree(this);
        this.goalAchieved = underAgent == goal;
    }

    public void action(Environnement environment) {
        // ToDo : Comment l'agent choisit son action ?
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
            // ToDo : comment l'agent choisi sa pile d'arrivee
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
