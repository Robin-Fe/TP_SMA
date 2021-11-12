import java.util.*;

public class CommunicateStrategy implements Strategy {
    private final Stack<Agent> claimers = new Stack<>(); // Stack which shows who is gonna move
    private final Stack<Agent> sleepers = new Stack<>(); // Stack which shows who is never gonna move
    private Mapping map = new Mapping();
    private final Map<Agent, Objet> destinationAgents = new HashMap<>();
    private int nbActions = 0;

    @Override
    public void beforePerception(List<Agent> agents, Environment environment) {
        map.resetMap();
        for (Agent agent : agents) {
            this.map = map.shareAgentPosition(agent, environment.getPreviousAgent(agent), environment.getNbPiles());
        }
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


    public void askToMove(Agent agent, Agent objectiveAgent, Environment environment) {
        // Ask the other to free de destination Stack
        int usedStackIndex = map.getPlace(agent);
        int objectiveIndex = map.getPlace(objectiveAgent);
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

        Objet destinationAgent;
        if (map.get(destinationIndex).size() != 0) {
            destinationAgent = map.get(destinationIndex).lastElement();
        } else {
            destinationAgent = environment.getTable();
        }

        Stack<Agent> objectiveStack = environment.getPile(environment.getPlace(objectiveAgent));
        for (int i = 0; i < objectiveStack.size(); i++) {
            Agent oneAgent = objectiveStack.get(i);
            Objet nextAgent = destinationAgent;
            if (i + 1 < objectiveStack.size()) {
                nextAgent = objectiveStack.get(i + 1);
            }
            if (objectiveStack.indexOf(oneAgent) > objectiveStack.indexOf(objectiveAgent) && !sleepers.contains(oneAgent)) {
                if (environment.verbose) {
                    System.out.println("* * " + agent.getName() + " push " + oneAgent.getName() + " a la destination de " + nextAgent.getName());
                }
                setPush(oneAgent, true, nextAgent);
                addClaimer(environment, oneAgent);
            }
        }
    }


    public void askToBeFree(Agent agent, Environment environment) {
        // Push Everoyone at the top, and ask them to let the objective stack free
        int usedStackIndex = map.getPlace(agent); // Les agents ne doivent pas surcharger la pile de celui qui veut se libérer
        int destinationStackIndex; // C'est la que l'agent qui pousse doit aller
        if (agent.getGoal() instanceof Agent) {
            destinationStackIndex = map.getPlace((Agent) agent.getGoal());
        } else {
            destinationStackIndex = map.getLowestStack(usedStackIndex);
        }
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i <= environment.getNbPiles() - 1; i++) {
            choices.add(i);
        }
        choices.remove((Object) usedStackIndex);
        choices.remove((Object) destinationStackIndex);
        int destinationStackIndexTest = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationStackIndexTest)) {
            destinationStackIndexTest = new Random().nextInt(environment.getNbPiles());
        }

        Stack<Agent> usedStack = map.get(usedStackIndex);
        Objet destinationAgent;
        if (map.get(destinationStackIndexTest).size() != 0) {
            destinationAgent = map.get(destinationStackIndexTest).lastElement();
        } else {
            destinationAgent = environment.getTable();
        }
        for (int i = 0; i < usedStack.size(); i++) {
            Agent oneAgent = usedStack.get(i);
            Objet nextObjet = destinationAgent;
            if (i + 1 < usedStack.size()) {
                nextObjet = usedStack.get(i + 1);
            }
            if (usedStack.indexOf(oneAgent) > usedStack.indexOf(agent) && !sleepers.contains(oneAgent)) {
                if (environment.verbose) {
                    System.out.println(" -- " + agent.getName() + " push " + oneAgent.getName() + " a la destination " + nextObjet.getName());
                }
                setPush(oneAgent, true, nextObjet);
                addClaimer(environment, oneAgent);
            }
        }
    }

    public void askEmptyStack(Environment environment, Agent agent) {
        int usedStackIndex = map.getPlace(agent);
        int lowestStackIndex = map.getLowestStack(usedStackIndex);
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

        Agent destinationAgent = map.get(destinationIndex).lastElement();
        int lowestStackSize = map.get(lowestStackIndex).size();
        for (int i = lowestStackSize - 1; i >= 0; i--) {
            Agent oneAgent = map.get(lowestStackIndex).get(i);
            if (environment.verbose) {
                System.out.println(" / / " + agent.getName() + " demande à " + oneAgent.getName() + " de bouger vers " + destinationAgent.getName());
            }
            setPush(oneAgent, true, destinationAgent);
            destinationAgent = oneAgent;
            addClaimer(environment, map.get(lowestStackIndex).get(lowestStackSize - 1 - i));
        }
    }


    public void firstAgentAction(Agent agent, Environment environment) {
        if (agent.getFree()) {
            if (map.isOneStackFree()) {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " essaie d'aller sur la stack libre");
                }
                int i = 0;
                while ((environment.getPreviousAgent(agent) instanceof Agent)) {
                    int destinationIndex = new Random().nextInt(environment.getNbPiles());
                    while (destinationIndex == environment.getPlace(agent)) {
                        destinationIndex = new Random().nextInt(environment.getNbPiles());
                    }
                    if (environment.verbose) {
                        System.out.println(agent.getName() + " bouge vers " + destinationIndex);
                    }
                    environment.seDeplacer(agent, destinationIndex);
                    i++;
                }
                this.removeClaimer(environment);
                this.nbActions+=i;
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
            System.out.println(agent.getName() + " essaie de bouger vers " + destinationAgents.get(agent).getName());
        }
        int i = 0;
        while (environment.getPreviousAgent(agent) != destinationAgents.get(agent)) {
            int destinationIndex = new Random().nextInt(environment.getNbPiles());
            while (destinationIndex == environment.getPlace(agent)) { // L'agent doit se déplacer sur une pile autre que la sienne
                destinationIndex = new Random().nextInt(environment.getNbPiles());
            }
            if (environment.verbose) {
                System.out.println(agent.getName() + " bouge vers " + destinationIndex);
            }
            environment.seDeplacer(agent, destinationIndex);
            i++;
        }
        this.removeClaimer(environment);
        this.nbActions += i;
    }

    public void isFreeAction(Agent agent, Environment environment) {
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (destinationIndex == environment.getPlace(agent)) { // L'agent doit se déplacer sur une pile autre que la sienne
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        if (environment.getPlace(agent) == environment.getPlace((Agent) agent.getGoal())) {
            if (environment.verbose) {
                System.out.println(agent.getName() + " essaie de bouger sur une autre pile");
            }
            if (environment.verbose) {
                System.out.println(agent.getName() + " bouge vers " + destinationIndex);
            }
            environment.seDeplacer(agent, destinationIndex);
            this.removeClaimer(environment);
        } else {
            if (destinationAgents.get(agent) == agent.getGoal()) {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " essaie d'aller sur " + destinationAgents.get(agent).getName());
                }
                int i = 0;
                while (environment.getPreviousAgent(agent) != destinationAgents.get(agent)) {
                    int destinationIndexTest = new Random().nextInt(environment.getNbPiles());
                    while (destinationIndexTest != environment.getPlace(agent)) {
                        destinationIndexTest = new Random().nextInt(environment.getNbPiles());
                    }
                    if (environment.verbose) {
                        System.out.println(agent.getName() + " bouge vers " + destinationIndexTest);
                    }
                    environment.seDeplacer(agent, destinationIndexTest);
                    i++;
                }
                this.nbActions += i;
                this.removeClaimer(environment);
            } else {
                if (map.isAgentFree((Agent) agent.getGoal())) {
                    if (environment.verbose) {
                        System.out.println(agent.getName() + " tente de bouger vers " + agent.getGoal().getName());
                    }
                    int i = 0;
                    while (environment.getPreviousAgent(agent) != agent.getGoal()) {
                        destinationIndex = new Random().nextInt(environment.getNbPiles());
                        while (destinationIndex == environment.getPlace(agent)) { // L'agent doit se déplacer sur une pile autre que la sienne
                            destinationIndex = new Random().nextInt(environment.getNbPiles());
                        }
                        if (environment.verbose) {
                            System.out.println(agent.getName() + " bouge vers " + destinationIndex);
                        }
                        environment.seDeplacer(agent, destinationIndex);
                        i++;
                    }
                    this.removeClaimer(environment);
                    this.nbActions += i;
                } else {
                    if (environment.verbose) {
                        System.out.println(agent.getName() + " demande a liberer la stack de " + agent.getGoal().getName());
                    }
                    this.askToMove(agent, (Agent) agent.getGoal(), environment);
                }
            }
        }

    }

    public void isBlockedAction(Agent agent, Environment environment) {
        if (environment.verbose) {
            System.out.println(agent.getName() + " ask to be free");
        }
        this.askToBeFree(agent, environment);
    }

    public void setPush(Agent agent, boolean isPushed, Objet destinationAgent) {
        agent.setPushed(isPushed);
        if (isPushed) {
            destinationAgents.put(agent, destinationAgent);
        } else {
            destinationAgents.remove(agent);
        }
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

    @Override
    public int getNbActions() {
        return this.nbActions;
    }

    @Override
    public void resetNbActions() {
        this.nbActions = 0;
    }
}
