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

    public Simulation(boolean verbose, boolean randomOrdering, Strategy strategy, int nbAgentss, int nbPiles) {
        this.verbose = verbose;
        this.strategy = strategy;
        //TODO : A rendre modulaire
        Objet table = new Objet("Table", null);
        Environment environment = new Environment(nbPiles, table, nbAgentss, verbose);
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
        agents = agents.subList(0, nbAgentss);
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
            //ToDo : add le bon nombre d'agent
            environment.addAgent(C, 0);
            environment.addAgent(D, 0);
            environment.addAgent(A, 0);
            environment.addAgent(B, 0);
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
