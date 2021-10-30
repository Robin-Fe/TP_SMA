import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Simulation {

    private final List<Agent> agents;
    private final Environnement environnement;
    public boolean verbose;
    public Politique AgentElection;

    public Simulation(boolean verbose, boolean randomOrdering, int nbAgents, Politique AgentElection, Politique AgentAction, Politique AgentDirection) {
        this.AgentElection = AgentElection;
        this.verbose = verbose;
        //TODO : A rendre modulaire (partiellement fait)
        Objet table = new Objet("Table", null);
        Environnement environnement = new Environnement(3, table, nbAgents, verbose);
        Agent A = new Agent("A", table, AgentAction, AgentDirection);
        List<Agent> agents = new ArrayList<>();
        agents.add(A);
        for (int i = 1; i < nbAgents; i++) {
            agents.add(new Agent(Character.toString((char) 65+i), agents.get(i-1), AgentAction, AgentDirection));
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
            for (Agent agent : agents) {
                environnement.addAgent(agent, 0);
            }
        }
        this.agents = agents;
        this.environnement = environnement;
    }

    public int runSimulation() {
        int nbTours = 0;
        while (!(testFinSimulation())) {
            nbTours++;
            if (verbose) {
                System.out.println("\n");
                environnement.printEnvironment();
            }
            //ToDo : Comment on choisi l'agent ?
            Agent agentChoisi = (Agent) AgentElection.getChoice(Collections.singletonList(agents));
            agentChoisi.perception(environnement);
            agentChoisi.action(environnement);
            agentChoisi.perception(environnement);
        }
        System.out.println("\n");
        environnement.printEnvironment();
        System.out.println("\nNb Tours = " + nbTours);
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

}
