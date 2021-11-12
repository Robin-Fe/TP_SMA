import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Simulation {

    private final List<Agent> agents;
    private final Environnement environnement;
    public boolean verbose;
    public Politique AgentElection;

    public Simulation(boolean verbose, boolean randomOrdering, int nbAgents, Politique AgentElection, Politique AgentDestination) {
        this.AgentElection = AgentElection;
        this.verbose = verbose;
        Objet table = new Objet("Table", null);
        Environnement environnement = new Environnement(3, table, nbAgents, verbose);
        Agent A = new Agent("A", table, AgentDestination);
        List<Agent> agents = new ArrayList<>();
        agents.add(A);
        for (int i = 1; i < nbAgents; i++) {
            agents.add(new Agent(Character.toString((char) 65+i), agents.get(i-1), AgentDestination));
        }

        if (randomOrdering) {
            int r1 = new Random().nextInt(agents.size());
            for (int i = 0; i < agents.size(); i++) {
                while (environnement.getPlace(agents.get(r1)) != -100) {
                    r1 = new Random().nextInt(agents.size());
                }
                int r2 = new Random().nextInt(environnement.getNbPiles());
                environnement.addAgent(agents.get(r1), r2);
            }
        } else {
            int i = 0;
            for (Agent agent : agents) {
                environnement.addAgent(agent, i);
                i++;
                if (i >= environnement.getNbPiles()) {
                    i=0;
                }
            }
        }
        this.agents = agents;
        this.environnement = environnement;
        for (Agent agent : this.agents) {
            agent.perception(this.environnement);
        }
    }

    public int runSimulation() {
        int nbTours = 0;
        while (!(testFinSimulation())) {
            nbTours++;
            if (verbose) {
                System.out.println("\n");
                environnement.printEnvironment();
            }

            allAgentsPerception();
            Agent agentChoisi = (Agent) AgentElection.getChoice(Collections.singletonList(agents));
            agentChoisi.action(environnement);
            allAgentsPerception();
        }
        if (verbose) {
            System.out.println("\n");
            environnement.printEnvironment();
            System.out.println("\nNb Tours = " + nbTours);
        }
        return nbTours;
    }

    public boolean testFinSimulation() {
        for (Agent agent : agents) {
            if (!agent.getGoalAchieved()) {
                return false;
            }
        }
        return true;
    }

    public void allAgentsPerception() {
        for (Agent agent : this.agents) {
            agent.perception(this.environnement);
        }
    }

}
