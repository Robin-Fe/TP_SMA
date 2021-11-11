import java.util.ArrayList;
import java.util.Random;

public class GlobalAgent extends Agent {
    private final GlobalStrategy strategy;
    private Integer destinationIndex;

    public GlobalAgent(String name, Objet goal, GlobalStrategy strategy) {
        super(name, goal);
        this.strategy = strategy;
    }

    public void perception(Environnement environment) {
        Objet underAgent = environment.getPreviousAgent(this);
        this.setFree(environment.getIsFree(this));
        this.setGoalAchieved(underAgent == this.getGoal());
        if (!(getGoal() instanceof Agent)) {
            if (getGoalAchieved()) {
                this.strategy.sleep(this);
            } else {
                this.strategy.addClaimer(environment, this);
            }
        } else {
            if (getGoalAchieved() && strategy.getSleepers().contains((GlobalAgent) getGoal())) {
                strategy.sleep(this);
            }
        }
    }


    public void action(Environnement environment) {
        if (!(getGoal() instanceof GlobalAgent)) {
            firstAgentAction(environment);
        } else {
            if (getPushed()) {
                isPushedAction(environment);
            } else {
                if (getFree()) {
                    isFreeAction(environment);
                } else {
                    isBlockedAction(environment);
                }
            }
        }
    }

    public void firstAgentAction(Environnement environment) {
        if (getFree()) {
            if (environment.getIsOneStackFree()) {
                if (environment.verbose) {
                    System.out.println(getName() + " bouge vers " + environment.getStackFree());
                }
                environment.seDeplacer(this, environment.getStackFree());
                strategy.removeClaimer(environment);

            } else {
                if (environment.verbose) {
                    System.out.println(getName() + " demande une empty stack ");
                }
                strategy.askEmptyStack(environment, this);
            }
        } else {
            if (environment.verbose) {
                System.out.println(getName() + " push");
            }
            strategy.askToBeFree(this, environment);
            environment.push(this);
        }
    }

    public void isPushedAction(Environnement environment) {
        if (environment.verbose) {
            System.out.println(getName() + " bouge vers " + destinationIndex);
        }
        environment.seDeplacer(this, destinationIndex);
        strategy.removeClaimer(environment);
    }

    public void isFreeAction(Environnement environment) {
        destinationIndex = environment.getPlace((Agent) getGoal());
        int agentIndex = environment.getPlace(this);
        if (agentIndex == destinationIndex){
            ArrayList<Integer> choices = new ArrayList<>();
            for (int i = 0; i <= environment.getNbPiles() - 1; i++) {
                choices.add(i);
            }
            choices.remove((Object)agentIndex);
            int destinationIndex = new Random().nextInt(environment.getNbPiles());
            while (!choices.contains(destinationIndex)) {
                destinationIndex = new Random().nextInt(environment.getNbPiles());
            }
            if (environment.verbose) {
                System.out.println(getName() + " bouge vers " + destinationIndex);
            }
            environment.seDeplacer(this, destinationIndex);
            strategy.removeClaimer(environment);
        }
        else{
            if (environment.getPile(destinationIndex).lastElement() == getGoal()) {
                if (environment.verbose) {
                    System.out.println(getName() + " bouge vers " + destinationIndex);
                }
                environment.seDeplacer(this, destinationIndex);
                strategy.removeClaimer(environment);
            } else {
                if (environment.verbose) {
                    System.out.println(getName() + " demande a liberer la stack " + destinationIndex);
                }
                strategy.askToMove(this, destinationIndex, environment);
            }
        }

    }

    public void isBlockedAction(Environnement environment) {
        if (environment.verbose) {
            System.out.println(getName() + " ask to be free");
        }
        strategy.askToBeFree(this, environment);
    }

    public void setPush(boolean isPushed, int destinationIndex) {
        this.setPushed(isPushed);
        if (isPushed) {
            this.destinationIndex = destinationIndex;
        } else {
            this.destinationIndex = null;
        }
    }

    public void claim(Environnement environment) {
        strategy.addClaimer(environment, this);
    }


}
