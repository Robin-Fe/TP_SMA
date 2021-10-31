import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Simulation {

    private final List<Agent> agents;
    private final Environnement environment;
    public boolean verbose;

    public Simulation(boolean verbose, boolean randomOrdering, int nbAgents) {
        this.verbose = verbose;
        //TODO : A rendre modulaire
        Objet table = new Objet("Table", null);
        Environnement environment = new Environnement(3, table, nbAgents, verbose);
        Agent A = new Agent("A", table);
        Agent B = new Agent("B", A);
        Agent C = new Agent("C", B);
        Agent D = new Agent("D", C);
        Agent E = new Agent("E", D);
        Agent F = new Agent("F", E);
        Agent G = new Agent("G", F);
        Agent H = new Agent("H", G);
        Agent I = new Agent("I", H);
        Agent J = new Agent("J", I);
        List<Agent> agents = Arrays.asList(A, B, C, D, E, F, G, H, I, J);
        agents = agents.subList(0, nbAgents);
        if (randomOrdering) {
            int r1 = new Random().nextInt(agents.size());
            for (int i = 0; i < agents.size(); i++) {
                while (environment.getPlace(agents.get(r1)) != -100) {
                    r1 = new Random().nextInt(agents.size());
                }
                int r2 = new Random().nextInt(environment.getNbPiles());
                environment.addAgent(agents.get(r1), r2);
            }
        } else {
            //ToDo : add le bon nombre d'agent
            environment.addAgent(C, 0);
            environment.addAgent(D, 0);
            environment.addAgent(A, 0);
            environment.addAgent(B, 0);
        }
        this.agents = agents;
        this.environment = environment;
    }

    public int runSimulation() {
        int nbTours = 0;
        while (!(testFinSimulation())) {
            nbTours++;
            if (verbose) {
                System.out.println("\n");
                environment.printEnvironment();
            }
            //ToDo : Comment on choisi l'agent ?
            Agent agentChoisi = choisirAgentRandom();
            agentChoisi.perception(environment);
            agentChoisi.action(environment);
            agentChoisi.perception(environment);
        }
        System.out.println("\n");
        environment.printEnvironment();
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

    public Agent choisirAgentRandom() {
        int r = new Random().nextInt(agents.size());
        return agents.get(r);
    }
}