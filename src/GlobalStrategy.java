import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GlobalStrategy {
    private Stack<GlobalAgent> claimers = new Stack<>(); // Stack which shows who is gonna move
    private Stack<GlobalAgent> sleepers = new Stack<>(); // Stack which shows who is never gonna move

    public GlobalStrategy() {
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
        for (int i = 0; i <= environment.getNbPiles(); i++) {
            choices.add(i);
        }
        choices.remove(usedStackIndex);
        choices.remove(objectiveIndex);
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationIndex)) {
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        Stack<Agent> destinationStack = environment.getPile(objectiveIndex);
        for (Agent oneAgent : destinationStack) {
            if (destinationStack.indexOf(oneAgent) > destinationStack.indexOf(agent) + 1) {
                if (environment.verbose){
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
        for (int i = 0; i <= environment.getNbPiles(); i++) {
            choices.add(i);
        }
        choices.remove(usedStackIndex);
        choices.remove(destinationStackIndex);

        Stack<Agent> usedStack = environment.getPile(usedStackIndex);
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationIndex)) {
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        for (Agent oneAgent : usedStack) {
            if (usedStack.indexOf(oneAgent) > usedStack.indexOf(agent)) {
                if (environment.verbose){
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
        for (int i = 0; i <= environment.getNbPiles(); i++) {
            choices.add(i);
        }
        choices.remove(usedStackIndex);
        choices.remove(lowestStackIndex);
        int destinationIndex = new Random().nextInt(environment.getNbPiles());
        while (!choices.contains(destinationIndex)) {
            destinationIndex = new Random().nextInt(environment.getNbPiles());
        }
        for (Agent oneAgent : environment.getPile(lowestStackIndex)) {
            if (environment.verbose){
                System.out.println(agent.getName() + " demande à " + oneAgent.getName() + " de bouger vers " + destinationIndex);
            }
            ((GlobalAgent) oneAgent).setPush(true, destinationIndex);
            ((GlobalAgent) oneAgent).claim(environment);
            //addClaimer((SmartAgent) oneAgent);
        }
    }

}
