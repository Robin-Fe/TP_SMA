import java.util.*;

public class CommunicateStrategy extends Strategy {
    private Stack<Agent> claimers = new Stack<>(); // Stack which shows who is gonna move
    private Stack<Agent> sleepers = new Stack<>(); // Stack which shows who is never gonna move
    private Stack<Stack<Agent>> map = new Stack<>(); // Map of the environment;
    Map<Agent, Objet> destinationAgents = new HashMap<Agent, Objet>();

    @Override
    public void beforePerception(List<Agent> agents, Environment environment, int nbPiles) {
        resetMap();
        for (Agent agent : agents) {
            sharePosition(agent, environment.getPreviousAgent(agent), nbPiles);
        }
        //System.out.println("MAPPING");
        //printEnvironment();
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
    public Agent getActionAgent(List<Agent> agents, Environment environment) {
        if (this.claimers.size() == 0) {
            Agent lastSleeper = sleepers.lastElement();
            for (Stack<Agent> stack : environment.getPiles()) {
                for (Agent agent : stack) {
                    if (agent.getGoal() == lastSleeper) {
                        claimers.add((Agent) agent);
                    }
                }
            }
        }
        return this.claimers.lastElement();
    }

    public void sleep(Agent agent) {
        if (!sleepers.contains(agent)) {
            sleepers.add(agent);
        }
    }

    public Stack<Agent> getSleepers() {
        return this.sleepers;
    }

    public void sharePosition(Agent agent, Object pointer, int nbPiles) {
        Stack<Stack<Agent>> removeStacks = new Stack<>();
        Stack<Stack<Agent>> addStacks = new Stack<>();
        if (!(pointer instanceof Agent)) {
            // Si l'agent doit aller sur la table, il se mappe sur une nouvelle pile
            Stack<Agent> newStack = new Stack<>();
            newStack.add(agent);
            map.add(newStack);
        } else {
            if (this.map.size() == 0) {
                // S'il n'y a pas de piles mappées, l'agent crée une pile et ajoute son obj et lui-même
                Stack<Agent> newStack = new Stack<>();
                newStack.add((Agent) pointer);
                newStack.add(agent);
                this.map.add(newStack);
            } else {
                // S'il y a des pile, il va voir s'il trouve son objectif
                boolean isPointerHere = false;
                for (Stack<Agent> stack : this.map) {
                    if (stack.contains(pointer)) {
                        // Si oui, il va se mapper dessus
                        isPointerHere = true;
                        stack.add(agent);
                    }
                }
                if (!isPointerHere) {
                    // Sinon, il va créer une pile et mapper son objectif et lui-même
                    Stack<Agent> newStack = new Stack<>();
                    newStack.add((Agent) pointer);
                    newStack.add(agent);
                    this.map.add(newStack);
                }
            }
        }
        for (Stack<Agent> firstStack : this.map) {
            if (firstStack.size() != 0) {
                Agent firstAgent = firstStack.get(0);
                for (Stack<Agent> secondStack : this.map) {
                    if (firstStack != secondStack) {
                        if (secondStack.size() != 0) {
                            if (secondStack.get(secondStack.size() - 1) == firstAgent) {
                                for (Agent oneAgent : firstStack) {
                                    if (oneAgent != firstAgent) {
                                        secondStack.add(oneAgent);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Stack<Agent> stack1 : this.map) {
            for (Stack<Agent> stack2 : this.map) {
                if (stack1 != stack2) {
                    if (stack1.containsAll(stack2)) {
                        if (stack2.containsAll(stack1)) {
                            addStacks.add(stack1);
                        }
                        removeStacks.add(stack2);
                    }
                }
            }
        }
        for (Stack<Agent> stack : removeStacks) {
            this.map.remove(stack);
        }
        for (int index = 0; index < addStacks.size(); index += 2) {
            this.map.add(addStacks.get(index));
        }
        while (this.map.size() < nbPiles) {
            this.map.add(new Stack<>());
        }
    }

    public void printEnvironment() {
        for (int i = map.size() - 1; i >= 0; i--) {
            Stack<Agent> pile = map.get(i);
            StringBuilder string = new StringBuilder();
            for (Agent agent : pile) {
                string.append(agent.getName()).append(" ");
            }
            if (!string.toString().equals("")) {
                int nb = 2 * 5 - string.length();
                string.append(" ".repeat(Math.max(0, nb)));
                System.out.println("[ " + string + "]");
            } else {
                string.append(" ".repeat(Math.max(0, 1 + 2 * 5)));
                System.out.println("[" + string + "]");
            }
        }
    }

    public void resetMap() {
        this.map = new Stack<>();
    }

    @Override
    public void action(Agent agent, Environment environment) {
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

    public void askToMove(Agent agent, Agent objectiveAgent, Environment environment) {
        // Ask the other to free de destination Stack
        int usedStackIndex = getPlace(agent);
        int objectiveIndex = getPlace(objectiveAgent);
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
        if (map.get(destinationIndex).size() != 0){
            destinationAgent = map.get(destinationIndex).lastElement();
        }
        else {
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
        int usedStackIndex = getPlace(agent); // Les agents ne doivent pas surcharger la pile de celui qui veut se libérer
        int destinationStackIndex; // C'est la que l'agent qui pousse doit aller
        if (agent.getGoal() instanceof Agent) {
            destinationStackIndex = getPlace((Agent) agent.getGoal());
        } else {
            destinationStackIndex = getLowestStack(usedStackIndex);
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
        if (map.get(destinationStackIndexTest).size() != 0){
            destinationAgent = map.get(destinationStackIndexTest).lastElement();
        }
        else {
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
        int usedStackIndex = getPlace(agent);
        int lowestStackIndex = getLowestStack(usedStackIndex);
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
            if (getIsOneStackFree()) {
                if (environment.verbose) {
                    System.out.println(agent.getName() + " essaie d'aller sur la stack libre");
                }
                while ((environment.getPreviousAgent(agent) instanceof Agent)) {
                    int destinationIndex = new Random().nextInt(environment.getNbPiles());
                    while (destinationIndex == environment.getPlace(agent)) {
                        destinationIndex = new Random().nextInt(environment.getNbPiles());
                    }
                    if (environment.verbose) {
                        System.out.println(agent.getName() + " bouge vers " + destinationIndex);
                    }
                    environment.seDeplacer(agent, destinationIndex);
                }
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
            System.out.println(agent.getName() + " essaie de bouger vers " + destinationAgents.get(agent).getName());
        }
        while (environment.getPreviousAgent(agent) != destinationAgents.get(agent)) {
            int destinationIndex = new Random().nextInt(environment.getNbPiles());
            while (destinationIndex == environment.getPlace(agent)) { // L'agent doit se déplacer sur une pile autre que la sienne
                destinationIndex = new Random().nextInt(environment.getNbPiles());
            }
            if (environment.verbose) {
                System.out.println(agent.getName() + " bouge vers " + destinationIndex);
            }
            environment.seDeplacer(agent, destinationIndex);
        }
        this.removeClaimer(environment);
    }

    public void isFreeAction(Agent agent, Environment environment) {
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (destinationIndex == environment.getPlace(agent)) { // L'agent doit se déplacer sur une pile autre que la sienne
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        if (environment.getPlace(agent) == environment.getPlace((Agent) agent.getGoal())) {
            if (environment.verbose) {
                System.out.println(agent.getName() + " essaie de bouger sur une autre pile" );
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
                while (environment.getPreviousAgent(agent) != destinationAgents.get(agent)) {
                    int destinationIndexTest = new Random().nextInt(environment.getNbPiles());
                    while (destinationIndexTest != environment.getPlace(agent)) {
                        destinationIndexTest = new Random().nextInt(environment.getNbPiles());
                    }
                    if (environment.verbose) {
                        System.out.println(agent.getName() + " bouge vers " + destinationIndexTest);
                    }
                    environment.seDeplacer(agent, destinationIndexTest);
                }
                this.removeClaimer(environment);
            } else {
                if (isAgentFree((Agent) agent.getGoal())) {
                    if (environment.verbose) {
                        System.out.println(agent.getName() + " tente de bouger vers " + agent.getGoal().getName());
                    }
                    while (environment.getPreviousAgent(agent) != agent.getGoal()) {
                        destinationIndex = new Random().nextInt(environment.getNbPiles());
                        while (destinationIndex == environment.getPlace(agent)) { // L'agent doit se déplacer sur une pile autre que la sienne
                            destinationIndex = new Random().nextInt(environment.getNbPiles());
                        }
                        if (environment.verbose) {
                            System.out.println(agent.getName() + " bouge vers " + destinationIndex);
                        }
                        environment.seDeplacer(agent, destinationIndex);
                    }
                    this.removeClaimer(environment);
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

    public boolean getIsOneStackFree() {
        boolean isOneStackFree = false;
        for (Stack<Agent> stack : map) {
            if (stack.size() == 0) {
                isOneStackFree = true;
                break;
            }
        }
        return isOneStackFree;
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

    public int getLowestStack(Integer usedStackIndex) {
        int lowestStackIndex = 0;
        if (usedStackIndex == 0) {
            lowestStackIndex = 1;
        }
        Stack<Agent> lowestStack = map.get(lowestStackIndex);
        for (int index = 0; index < map.size(); index++) {
            Stack<Agent> stack = map.get(index);
            if (stack.size() < lowestStack.size() && index != usedStackIndex) {
                lowestStack = stack;
                lowestStackIndex = index;
            }
        }
        return lowestStackIndex;
    }

    public int getPlace(Agent agent) {
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).contains(agent)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isAgentFree(Agent agent) {
        for (Stack<Agent> agents : map) {
            if (agents.contains(agent)) {
                if (agents.lastElement() == agent) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

}
