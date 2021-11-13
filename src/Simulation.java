import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Simulation {
    private final List<Agent> agents;
    private final Strategy strategy;
    private final Environment environment;
    public boolean verbose;
    private int deplacementScore = 0;
    private int actionScore = 0;

    public Simulation(boolean verbose, boolean randomOrdering, Strategy strategy, int nbAgents, int nbPiles) {
        this.verbose = verbose;
        this.strategy = strategy;
        //TODO : A rendre modulaire
        Objet table = new Objet("Table", null);
        Environment environment = new Environment(nbPiles, table, nbAgents, verbose);
        Agent A = new Agent("A", table);
        List<Agent> agents = new ArrayList<>();
        agents.add(A);
        Agent objective = A;
        String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
                "A1","B1","C1","D1","E1", "F1","G1","H1","I1","J1","K1","L1","M1","N1","O1","P1","Q1","R1","S1","T1","U1","V1","W1","X1","Y1","Z1",
                "A2","B2","C2","D2","E2", "F2","G2","H2","I2","J2","K2","L2","M2","N2","O2","P2","Q2","R2","S2","T2","U2","V2","W2","X2","Y2","Z2",
                "A3","B3","C3","D3","E3", "F3","G3","H3","I3","J3","K3","L3","M3","N3","O3","P3","Q3","R3","S3","T3","U3","V3","W3","X3","Y3","Z3",
        };

        for (int i=1; i<nbAgents; i++){
            Agent newAgent = new Agent(alphabet[i], objective);
            agents.add(newAgent);
            objective = newAgent;
        }
        if (randomOrdering) {
            int r1 = new Random().nextInt(agents.size());
            for (int i = 0; i < agents.size(); i++) {
                while (environment.getPlace(agents.get(r1)) != -1) {
                    r1 = new Random().nextInt(agents.size());
                }
                int r2 = new Random().nextInt(environment.getNbPiles());
                environment.addAgent(agents.get(r1), r2);
            }
        } else {
            for (int i = agents.size()-1; i >= 0; i--) {
                environment.addAgent(agents.get(i), 0);
            }
        }
        this.agents = agents;
        this.environment = environment;
    }


    public void runSimulation() {
        this.strategy.resetNbActions();
        while (!(testFinSimulation())) {
            if (verbose) {
                System.out.println("\n");
                environment.printEnvironment();
            }
            strategy.beforePerception(agents, environment);
            allAgentPerception(strategy, environment);
            strategy.getActionAgent(agents, environment).action(strategy, environment);
            allAgentPerception(strategy, environment);
        }
        if (verbose) {
            System.out.println("\n");
            environment.printEnvironment();
            System.out.println("\nNb Tours = " + environment.getNbDeplacements());
            System.out.println("//////////////////////////////////////////////////////////////////////////////");
        }
        this.deplacementScore = environment.getNbDeplacements();
        this.actionScore = strategy.getNbActions();
    }

    public boolean testFinSimulation() {
        for (Agent agent : agents) {
            if (!agent.getGoalAchieved()) {
                return false;
            }
        }
        return true;
    }


    public void allAgentPerception(Strategy strategy, Environment environment) {
        for (Agent agent : agents) {
            agent.perception(strategy, environment);
        }
    }

    public int getDeplacementScore() {
        return deplacementScore;
    }

    public int getActionScore() {
        return actionScore;
    }

}
