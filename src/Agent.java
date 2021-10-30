import java.util.Random;

public class Agent extends Objet {

    public Politique AgentAction;
    public Politique AgentDirection;

    public Agent(String name, Objet goal, Politique AgentAction, Politique AgentDirection) {
        super(name, goal);
        this.AgentAction = AgentAction;
        this.AgentDirection = AgentDirection;
    }

    public void perception(Environnement environment) {
        Objet underAgent = environment.getPreviousAgent(this);
        this.setFree(environment.getIsFree(this));
        this.setGoalAchieved(underAgent == this.getGoal());
    }

    public void action(Environnement environment) {
        // ToDo : Comment l'agent choisit son action ?
        if (!getGoalAchieved()) {
            tryToMove(environment);
        } else {
            if (getPushed()) {
                tryToMove(environment);
            } else {
                if (environment.verbose) {
                    System.out.println(this.getName() + " ne fait rien");
                }
            }
        }
    }

    private void tryToMove(Environnement environment) {
        if (getFree()) {
            // ToDo : Comment l'agent choisi sa pile d'arrivee
            int index = new Random().nextInt(environment.getNbPiles());
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
