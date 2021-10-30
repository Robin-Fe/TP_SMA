import java.util.Collections;

public class Agent extends Objet {

    public Politique AgentAction;
    public Politique AgentDestination;

    public Agent(String name, Objet goal, Politique AgentAction, Politique AgentDestination) {
        super(name, goal);
        this.AgentAction = AgentAction;
        this.AgentDestination = AgentDestination;
    }

    public void perception(Environnement environment) {
        Objet underAgent = environment.getPreviousAgent(this);
        this.setFree(environment.getIsFree(this));
        this.setGoalAchieved(underAgent == this.getGoal());
    }

    public void action(Environnement environment) {
        // ToDo : Comment l'agent choisit son action ?
        if (!getGoalAchieved() || getPushed()) {
            tryToMove(environment);
        } else {
            if (environment.verbose) {
                System.out.println(this.getName() + " ne fait rien");
            }
        }
    }

    private void tryToMove(Environnement environment) {
        if (getFree()) {
            int startingIndex = environment.getPiles().lastIndexOf(environment.getPile(environment.getPlace(this)));
            int index = startingIndex;
            while (startingIndex == index) {
                index = environment.getPiles().lastIndexOf(AgentDestination.getChoice(Collections.singletonList(environment.getPiles())));
            }
            if (environment.verbose) {
                System.out.println(this.getName() + " bouge vers " + index);
            }
            environment.seDeplacer(this, index);
        } else {
            if (environment.verbose) {
                System.out.println(this.getName() + " push");
            }
            environment.push(this);
        }
    }

    public void setPush(boolean isPushed) {
        this.setPushed(isPushed);
    }

}
