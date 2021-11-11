import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GlobalSimulation {
    private final List<GlobalAgent> agents;
    private final Environnement environment;
    private final GlobalStrategy strategy;
    public boolean verbose;
    public final int nbPiles;

    public GlobalSimulation(boolean verbose, boolean randomOrdering, int nbAgents, GlobalStrategy strategy, int nbPiles) {
        this.nbPiles = nbPiles;
        this.verbose = verbose;
        this.strategy = strategy;
        //TODO : A rendre modulaire
        Objet table = new Objet("Table", null);
        Environnement environment = new Environnement(nbPiles, table, nbAgents, verbose);
        GlobalAgent A = new GlobalAgent("A", table, strategy);
        GlobalAgent B = new GlobalAgent("B", A, strategy);
        GlobalAgent C = new GlobalAgent("C", B, strategy);
        GlobalAgent D = new GlobalAgent("D", C, strategy);
        GlobalAgent E = new GlobalAgent("E", D, strategy);
        GlobalAgent F = new GlobalAgent("F", E, strategy);
        GlobalAgent G = new GlobalAgent("G", F, strategy);
        GlobalAgent H = new GlobalAgent("H", G, strategy);
        GlobalAgent I = new GlobalAgent("I", H, strategy);
        GlobalAgent J = new GlobalAgent("J", I, strategy);
        List<GlobalAgent> agents = Arrays.asList(A, B, C, D, E, F, G, H, I, J);
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
            allAgentSharePosition(strategy, environment);
            //System.out.println("MAPPING");
            //strategy.printEnvironment();
            allAgentPerception(environment);
            getClaimerAgent().action(environment);
            allAgentPerception(environment);
        }
        System.out.println("\n");
        environment.printEnvironment();
        System.out.println("\nNb Tours = " + nbTours);
        System.out.println("//////////////////////////////////////////////////////////////////////////////");

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

    public GlobalAgent getClaimerAgent() {
        return strategy.getLastClaimer(environment);
    }

    public void allAgentPerception(Environnement environment) {
        for (GlobalAgent agent : agents) {
            agent.perception(environment);
        }
    }

    public void allAgentSharePosition(GlobalStrategy strategy, Environnement environment){
        strategy.resetMap();
        for (GlobalAgent agent : agents){
            strategy.sharePosition(agent, environment.getPreviousAgent(agent), this.nbPiles);

        }
    }
}
