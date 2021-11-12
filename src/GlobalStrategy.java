import java.util.*;

public class GlobalStrategy implements Strategy {
    private final Stack<Agent> claimers = new Stack<>(); // Stack which shows who is gonna move
    private final Stack<Agent> sleepers = new Stack<>(); // Stack which shows who is never gonna move
    private final Map<Agent, Integer> destinationIndexes = new HashMap<Agent, Integer>();
    private int nbActions = 0;

    public GlobalStrategy() {
    }


    @Override
    public void perception(Agent agent, Environment environment) {
        if (!(agent.getGoal() instanceof Agent)) {
            if (agent.getGoalAchieved()) {
                this.sleep(agent);
            } else {
                this.addClaimer(environment, agent);
            }
        } else {
            if (agent.getGoalAchieved() && this.getSleepers().contains(agent.getGoal())) {
                this.sleep(agent);
            }
        }
    }

    @Override
    public void beforePerception(List<Agent> agents, Environment environment) {
    }

    @Override
    public Agent getActionAgent(List<Agent> agents, Environment environment) {
        if (this.claimers.size() == 0) {
            Agent lastSleeper = sleepers.lastElement();
            for (Stack<Agent> stack : environment.getPiles()) {
                for (Agent agent : stack) {
                    if (agent.getGoal() == lastSleeper) {
                        claimers.add(agent);
                    }
                }
            }
        }
        return this.claimers.lastElement();
    }


    @Override
    public void action(Agent agent, Environment environment) {
        this.nbActions++;
        if (!(agent.getGoal() instanceof Agent)) {
            firstAgentAction(agent, environment);
        } else {
            if (agent.getPushed()) {
                isPushedAction(agent, environment);
            } else {
                if (agent.getFree()) {
                    isFreeAction(agent, environment);
                } else {
                    isBlockedAction(agent, environment);
                }
            }
        }
    }

    public void sleep(Agent agent) {
        if (!sleepers.contains(agent)) {
            sleepers.add(agent);
        }
    }

    public Stack<Agent> getSleepers() {
        return this.sleepers;
    }

    public void addClaimer(Environment environment, Agent agent) {
        if (!(this.claimers.contains(agent))) {
            this.claimers.add(agent);
        }
        if (environment.verbose) {
            System.out.println("CLAIMERS : " + printClaimers());
        }
    }

    public void removeClaimer(Environment environment) {
        this.claimers.removeElementAt(claimers.size() - 1);
        if (environment.verbose) {
            System.out.println("CLAIMERS : " + printClaimers());
        }
    }


    public String printClaimers() {
        StringBuilder string = new StringBuilder();
        int i = 0;
        for (Agent agent : claimers) {
            if (i < 10) {
                string.append(agent.getName()).append(" ");
                i += 1;
            }
        }
        return string.toString();
    }


    public void askToMove(Agent agent, int objectiveIndex, Environment environment) {
        // Ask the other to free de destination Stack
        int usedStackIndex = environment.getPlace(agent);
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i <= environment.getNbPiles() - 1; i++) {
            choices.add(i);
        }
        choices.remove((Object) usedStackIndex);
        choices.remove((Object) objectiveIndex);

        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationIndex)) {
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        Stack<Agent> destinationStack = environment.getPile(objectiveIndex);
        for (Agent oneAgent : destinationStack) {
            if (destinationStack.indexOf(oneAgent) > destinationStack.indexOf(agent) + 1 && !sleepers.contains(oneAgent)) {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " push " + oneAgent.getName() + " a la destination " + destinationIndex);
                }
                setPush(oneAgent, true, destinationIndex);
                addClaimer(environment, oneAgent);
            }
        }
    }


    public void askToBeFree(Agent agent, Environment environment) {
        // Push Everoyone at the top, and ask them to let the objective stack free
        int usedStackIndex = environment.getPlace(agent);
        int destinationStackIndex;
        if (agent.getGoal() instanceof Agent) {
            destinationStackIndex = environment.getPlace((Agent) agent.getGoal());
        } else {
            destinationStackIndex = environment.getLowestStack();
        }
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i <= environment.getNbPiles() - 1; i++) {
            choices.add(i);
        }
        choices.remove((Object) usedStackIndex);
        choices.remove((Object) destinationStackIndex);
        Stack<Agent> usedStack = environment.getPile(usedStackIndex);
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationIndex)) {
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        for (Agent oneAgent : usedStack) {
            if (usedStack.indexOf(oneAgent) > usedStack.indexOf(agent) && !sleepers.contains(oneAgent)) {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " push " + oneAgent.getName() + " a la destination " + destinationIndex);
                }
                setPush(oneAgent, true, destinationIndex);
                addClaimer(environment, oneAgent);
            }
        }
    }

    public void askEmptyStack(Environment environment, Agent agent) {
        int usedStackIndex = environment.getPlace(agent);
        int lowestStackIndex = environment.getLowestStack();
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i <= environment.getNbPiles() - 1; i++) {
            choices.add(i);
        }
        choices.remove((Object) usedStackIndex);
        choices.remove((Object) lowestStackIndex);
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationIndex)) {
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        for (Agent oneAgent : environment.getPile(lowestStackIndex)) {
            if (environment.verbose) {
                System.out.println(agent.getName() + " demande à " + oneAgent.getName() + " de bouger vers " + destinationIndex);
            }
            setPush(oneAgent, true, destinationIndex);
            addClaimer(environment, oneAgent);
        }
    }


    public void firstAgentAction(Agent agent, Environment environment) {
        if (agent.getFree()) {
            if (environment.isOneStackFree()) {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " bouge vers " + environment.getStackFree());
                }
                environment.seDeplacer(agent, environment.getStackFree());
                this.removeClaimer(environment);

            } else {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " demande une empty stack ");
                }
                this.askEmptyStack(environment, agent);
            }
        } else {
            if (environment.verbose) {
                System.out.println(agent.getName() + " push");
            }
            this.askToBeFree(agent, environment);
            environment.push(agent);
        }
    }

    public void isPushedAction(Agent agent, Environment environment) {
        if (environment.verbose) {
            System.out.println(agent.getName() + " bouge vers " + destinationIndexes.get(agent));
        }
        environment.seDeplacer(agent, destinationIndexes.get(agent));
        this.removeClaimer(environment);
    }

    public void isFreeAction(Agent agent, Environment environment) {
        int destinationIndex = environment.getPlace((Agent) agent.getGoal());
        int agentIndex = environment.getPlace(agent);
        if (agentIndex == destinationIndex) {
            ArrayList<Integer> choices = new ArrayList<>();
            for (int i = 0; i <= environment.getNbPiles() - 1; i++) {
                choices.add(i);
            }
            choices.remove((Object) agentIndex);
            destinationIndex = new Random().nextInt(environment.getNbPiles());
            while (!choices.contains(destinationIndex)) {
                destinationIndex = new Random().nextInt(environment.getNbPiles());
            }
            if (environment.verbose) {
                System.out.println(agent.getName() + " bouge vers " + destinationIndex);
            }
            environment.seDeplacer(agent, destinationIndex);
            this.removeClaimer(environment);
        } else {
            if (environment.getPile(destinationIndex).lastElement() == agent.getGoal()) {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " bouge vers " + destinationIndex);
                }
                environment.seDeplacer(agent, destinationIndex);
                this.removeClaimer(environment);
            } else {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " demande a liberer la stack " + destinationIndex);
                }
                this.askToMove(agent, destinationIndex, environment);
            }
        }
    }

    public void isBlockedAction(Agent agent, Environment environment) {
        if (environment.verbose) {
            System.out.println(agent.getName() + " ask to be free");
        }
        this.askToBeFree(agent, environment);
    }

    public void setPush(Agent agent, boolean isPushed, int destinationIndex) {
        agent.setPushed(isPushed);
        if (isPushed) {
            destinationIndexes.put(agent, destinationIndex);
        } else {
            destinationIndexes.remove(agent);
        }
    }

    @Override
    public int getNbActions() {
        return this.nbActions;
    }

    @Override
    public void resetNbActions() {
        this.nbActions = 0;
    }
}

