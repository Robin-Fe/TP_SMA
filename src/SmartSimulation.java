import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SmartSimulation {

    private final List<SmartAgent> agents;
    private final Environnement environment;
    private final Strategy strategy;
    public boolean verbose;

    public SmartSimulation(boolean verbose, boolean randomOrdering, int nbAgents, Strategy strategy) {
        this.verbose = verbose;
        this.strategy = strategy;
        //TODO : A rendre modulaire
        Objet table = new Objet("Table", null);
        Environnement environment = new Environnement(3, table, nbAgents, verbose);
        SmartAgent A = new SmartAgent("A", table, strategy);
        SmartAgent B = new SmartAgent("B", A, strategy);
        SmartAgent C = new SmartAgent("C", B, strategy);
        SmartAgent D = new SmartAgent("D", C, strategy);
        SmartAgent E = new SmartAgent("E", D, strategy);
        SmartAgent F = new SmartAgent("F", E, strategy);
        SmartAgent G = new SmartAgent("G", F, strategy);
        SmartAgent H = new SmartAgent("H", G, strategy);
        SmartAgent I = new SmartAgent("I", H, strategy);
        SmartAgent J = new SmartAgent("J", I, strategy);
        List<SmartAgent> agents = Arrays.asList(A, B, C, D, E, F, G, H, I, J);
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

            allAgentPerception(environment);
            getClaimerAgent().action(environment);
            allAgentPerception(environment);
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

    public SmartAgent getClaimerAgent() {
        return strategy.getLastClaimer(environment);
    }

    public void allAgentPerception(Environnement environment) {
        for (SmartAgent agent : agents) {
            agent.perception(environment);
        }
    }
}
