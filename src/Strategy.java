import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Strategy {
    private Stack<SmartAgent> claimers = new Stack<>(); // Stack which shows who is gonna move
    private Stack<SmartAgent> sleepers = new Stack<>(); // Stack which shows who is never gonna move

    public Strategy() {
    }

    public void addClaimer(SmartAgent agent) {
        if (!(this.claimers.contains(agent))) {
            this.claimers.add(agent);
        }
        System.out.println("CLAIMERS : " + printClaimers());
    }

    public void removeClaimer() {
        this.claimers.removeElementAt(claimers.size() - 1);
        System.out.println("CLAIMERS : " + printClaimers());

    }

    public SmartAgent getLastClaimer(Environnement environnement) {
        if (this.claimers.size() == 0) {
            SmartAgent lastSleeper = sleepers.lastElement();
            for (Stack<Agent> stack : environnement.getPiles()) {
                for (Agent agent : stack) {
                    if (agent.getGoal() == lastSleeper) {
                        claimers.add((SmartAgent) agent);
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


    public void askToMove(SmartAgent agent, int objectiveIndex, Environnement environment) {
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
                System.out.println(agent.getName() + " push " + oneAgent.getName() + " a la destination " + destinationIndex);
                ((SmartAgent) oneAgent).setPush(true, destinationIndex);
                ((SmartAgent) oneAgent).claim();
                //addClaimer((SmartAgent) oneAgent);

            }
        }
    }

    public void sleep(SmartAgent agent) {
        if (!sleepers.contains(agent)) {
            sleepers.add(agent);
        }
    }

    public Stack<SmartAgent> getSleepers() {
        return this.sleepers;
    }

    public void askToBeFree(SmartAgent agent, Environnement environment) {
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
                System.out.println(agent.getName() + " push " + oneAgent.getName() + " a la destination " + destinationIndex);
                ((SmartAgent) oneAgent).setPush(true, destinationIndex);
                ((SmartAgent) oneAgent).claim();
                //addClaimer((SmartAgent) oneAgent);

            }
        }
    }

    public void askEmptyStack(Environnement environment, SmartAgent agent) {
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
            System.out.println(agent.getName() + " demande à " + oneAgent.getName() + " de bouger vers " + destinationIndex);
            ((SmartAgent) oneAgent).setPush(true, destinationIndex);
            ((SmartAgent) oneAgent).claim();
            //addClaimer((SmartAgent) oneAgent);
        }
    }

}
