import java.util.*;

public class GlobalStrategy {
    private Stack<GlobalAgent> claimers = new Stack<>(); // Stack which shows who is gonna move
    private Stack<GlobalAgent> sleepers = new Stack<>(); // Stack which shows who is never gonna move
    private Stack<Stack<GlobalAgent>> map = new Stack<>(); // Map of the environment;

    public GlobalStrategy() { }

    public void sharePosition(Agent agent, Object pointer, int nbPiles) {
        Stack<Stack<GlobalAgent>> removeStacks = new Stack<>();
        Stack<Stack<GlobalAgent>> addStacks = new Stack<>();
        if (!(pointer instanceof Agent)){
            // Si l'agent doit aller sur la table, il se mappe sur une nouvelle pile
            Stack<GlobalAgent> newStack = new Stack<>();
            newStack.add((GlobalAgent) agent);
            map.add(newStack);
        }
        else{
            if (this.map.size()==0){
                // S'il n'y a pas de piles mappées, l'agent crée une pile et ajoute son obj et lui-même
                Stack<GlobalAgent> newStack = new Stack<>();
                newStack.add((GlobalAgent) pointer);
                newStack.add((GlobalAgent) agent);
                this.map.add(newStack);
            }
            else{
                // S'il y a des pile, il va voir s'il trouve son objectif
                boolean isPointerHere = false;
                for (Stack<GlobalAgent> stack : this.map){
                    if (stack.contains(pointer)){
                        // Si oui, il va se mapper dessus
                        isPointerHere = true;
                        stack.add((GlobalAgent) agent);
                    }
                }
                if (!isPointerHere){
                    // Sinon, il va créer une pile et mapper son objectif et lui-même
                    Stack<GlobalAgent> newStack = new Stack<>();
                    newStack.add((GlobalAgent) pointer);
                    newStack.add((GlobalAgent) agent);
                    this.map.add(newStack);
                }
            }
        }
        for (Stack<GlobalAgent> firstStack : this.map){
            if (firstStack.size()!= 0){
                GlobalAgent firstAgent = firstStack.get(0);
                for (Stack<GlobalAgent> secondStack : this.map){
                    if (firstStack != secondStack){
                        if (secondStack.size() != 0){
                            if (secondStack.get(secondStack.size()-1) == firstAgent){
                                for (GlobalAgent oneAgent : firstStack){
                                    if (oneAgent != firstAgent){
                                        secondStack.add(oneAgent);
                                    }
                                    //removeStacks.add(firstStack);
                                }
                            }
                        }

                    }
                }
            }

        }
        for (Stack<GlobalAgent> stack1 : this.map){
            for (Stack<GlobalAgent> stack2 : this.map){
                if (stack1 != stack2){
                    if (stack1.containsAll(stack2)){
                        if(stack2.containsAll(stack1)){
                            addStacks.add(stack1);
                        }
                        removeStacks.add(stack2);
                    }
                }
            }
        }
        for (Stack<GlobalAgent> stack : removeStacks){
            this.map.remove(stack);
        }
        for (int index=0; index<addStacks.size(); index+=2){
            this.map.add(addStacks.get(index));
        }
        while (this.map.size() < nbPiles){
            this.map.add(new Stack<>());
        }
    }

    public void printEnvironment() {
        for (int i = map.size()-1; i >= 0; i--) {
            Stack<GlobalAgent> pile = map.get(i);
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


    public void addClaimer(Environnement environment, GlobalAgent agent) {
        if (!(this.claimers.contains(agent))) {
            this.claimers.add(agent);
        }
        if (environment.verbose) {
            System.out.println("CLAIMERS : " + printClaimers());
        }
    }

    public void removeClaimer(Environnement environment) {
        this.claimers.removeElementAt(claimers.size() - 1);
        if (environment.verbose) {
            System.out.println("CLAIMERS : " + printClaimers());
        }
    }

    public GlobalAgent getLastClaimer(Environnement environnement) {
        if (this.claimers.size() == 0) {
            GlobalAgent lastSleeper = sleepers.lastElement();
            for (Stack<Agent> stack : environnement.getPiles()) {
                for (Agent agent : stack) {
                    if (agent.getGoal() == lastSleeper) {
                        claimers.add((GlobalAgent) agent);
                    }
                }
            }
        }
        return this.claimers.lastElement();
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


    public void askToMove(GlobalAgent agent, int objectiveIndex, Environnement environment) {
        // Ask the other to free de destination Stack
        int usedStackIndex = environment.getPlace(agent);
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i <= environment.getNbPiles()-1; i++) {
            choices.add(i);
        }
        choices.remove((Object)usedStackIndex);
        choices.remove((Object)objectiveIndex);
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
                ((GlobalAgent) oneAgent).setPush(true, destinationIndex);
                ((GlobalAgent) oneAgent).claim(environment);
                //addClaimer((SmartAgent) oneAgent);
            }
        }
    }

    public void sleep(GlobalAgent agent) {
        if (!sleepers.contains(agent)) {
            sleepers.add(agent);
        }
    }

    public Stack<GlobalAgent> getSleepers() {
        return this.sleepers;
    }

    public void askToBeFree(GlobalAgent agent, Environnement environment) {
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
        choices.remove((Object)usedStackIndex);
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
                ((GlobalAgent) oneAgent).setPush(true, destinationIndex);
                ((GlobalAgent) oneAgent).claim(environment);
                //addClaimer((SmartAgent) oneAgent);

            }
        }
    }

    public void askEmptyStack(Environnement environment, GlobalAgent agent) {
        int usedStackIndex = environment.getPlace(agent);
        int lowestStackIndex = environment.getLowestStack();
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i <= environment.getNbPiles() - 1; i++) {
            choices.add(i);
        }
        choices.remove((Object)usedStackIndex);
        choices.remove((Object)lowestStackIndex);
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationIndex)) {
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        for (Agent oneAgent : environment.getPile(lowestStackIndex)) {
            if (environment.verbose) {
                System.out.println(agent.getName() + " demande à " + oneAgent.getName() + " de bouger vers " + destinationIndex);
            }
            ((GlobalAgent) oneAgent).setPush(true, destinationIndex);
            ((GlobalAgent) oneAgent).claim(environment);
            //addClaimer((SmartAgent) oneAgent);
        }
    }
    public void resetMap(){
        this.map = new Stack<>();
    }
}
