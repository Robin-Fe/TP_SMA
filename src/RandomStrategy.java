import java.util.List;
import java.util.Random;

public class RandomStrategy implements Strategy {
    private int nbActions = 0;

    @Override
    public void beforePerception(List<Agent> agents, Environment environment) {
    }

    @Override
    public Agent getActionAgent(List<Agent> agents, Environment environment) {
        int r = new Random().nextInt(agents.size());
        return agents.get(r);
    }

    @Override
    public void perception(Agent agent, Environment environment) {
        Objet underAgent = environment.getPreviousAgent(agent);
        agent.setFree(environment.getIsFree(agent));
        agent.setGoalAchieved(underAgent == agent.getGoal());
    }

    @Override
    public void action(Agent agent, Environment environment) {
        this.nbActions++;
        if (!agent.getGoalAchieved()) {
            tryToMove(agent, environment);
        } else {
            if (agent.getPushed()) {
                tryToMove(agent, environment);
            } else {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " ne fait rien");
                }
            }
        }
    }


    @Override
    public int getNbActions() {
        return this.nbActions;
    }

    public void tryToMove(Agent agent, Environment environment) {
        if (agent.getFree()) {
            int index = new Random().nextInt(environment.getNbPiles());
            if (environment.verbose) {
                System.out.println(agent.getName() + " bouge vers " + index);
            }
            environment.seDeplacer(agent, index);
        } else {
            if (environment.verbose) {
                System.out.println(agent.getName() + " push");
            }
            environment.push(agent);
        }
    }

    @Override
    public void resetNbActions() {
        this.nbActions = 0;
    }
}
