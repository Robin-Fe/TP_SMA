import java.util.Random;

public class Main {

    public static void main(String[] args) {
        SimulationTesting(true, 10, new SmartAgentPickingPolitique(), new SmartDestinationPolitique(67, 51));
        //ParametersTesting(100);
        //StrategyTesting(1000);
    }

    public static void SimulationTesting(boolean randomOrdering, int nbAgents, Politique AgentElection, Politique AgentDestination) {
        System.out.println("Score : " + new Simulation(true, randomOrdering, nbAgents, AgentElection, AgentDestination).runSimulation());
    }

    public static void StrategyTesting(int nombreSimulations) {

        //POLITIQUES
        Politique randomPolitique = new RandomPolitique();
        Politique smartAgentPickingPolitique = new SmartAgentPickingPolitique();
        Politique smartDestinationPolitique = new SmartDestinationPolitique(67, 51);

        int score1 = 0;

        for (int j = 0; j < 10; j++) {
            System.out.println("\n\n");
            for (int i = 0; i < nombreSimulations; i++) {
                Simulation simulation1 = new Simulation(false, false, 10, randomPolitique, randomPolitique);
                score1 += simulation1.runSimulation();
            }
            System.out.println("randomPolitique, randomPolitique - Score : " + score1/nombreSimulations);
            score1 = 0;

            for (int i = 0; i < nombreSimulations; i++) {
                Simulation simulation1 = new Simulation(false, false, 10, randomPolitique, smartDestinationPolitique);
                score1 += simulation1.runSimulation();
            }
            System.out.println("randomPolitique, smartDestinationPolitique - Score : " + score1/nombreSimulations);
            score1 = 0;

            for (int i = 0; i < nombreSimulations; i++) {
                Simulation simulation1 = new Simulation(false, false, 10, smartAgentPickingPolitique, randomPolitique);
                score1 += simulation1.runSimulation();
            }
            System.out.println("smartAgentPickingPolitique, randomPolitique - Score : " + score1/nombreSimulations);
            score1 = 0;

            for (int i = 0; i < nombreSimulations; i++) {
                Simulation simulation1 = new Simulation(false, false, 10, smartAgentPickingPolitique, smartDestinationPolitique);
                score1 += simulation1.runSimulation();
            }
            System.out.println("smartAgentPickingPolitique, smartDestinationPolitique - Score : " + score1/nombreSimulations);
        }
    }

    public static void ParametersTesting(int nombreSimulations) {
        Politique smartAgentPickingPolitique = new SmartAgentPickingPolitique();

        //SCORES
        int score1 = 0;
        int score2 = 0;
        int score3 = 0;
        int score4 = 0;
        int nbSimulations = 1000;

        //PARAMETRES
        int percent1 = 50;
        int percent2 = 50;

        int bestScore = 1000000;
        for (int j = 0; j < 100; j++) {
            score1 = 0;
            score2 = 0;
            score3 = 0;
            score4 = 0;

            //SIMULATIONS
            for (int i = 0; i < nbSimulations; i++) {
                Simulation simulation1 = new Simulation(false, false, 10, smartAgentPickingPolitique, new SmartDestinationPolitique(percent1+2, percent2));
                Simulation simulation2 = new Simulation(false, false, 10, smartAgentPickingPolitique, new SmartDestinationPolitique(percent1-2, percent2));
                Simulation simulation3 = new Simulation(false, false, 10, smartAgentPickingPolitique, new SmartDestinationPolitique(percent1, percent2+2));
                Simulation simulation4 = new Simulation(false, false, 10, smartAgentPickingPolitique, new SmartDestinationPolitique(percent1, percent2-2));
                score1 += simulation1.runSimulation();
                score2 += simulation2.runSimulation();
                score3 += simulation3.runSimulation();
                score4 += simulation4.runSimulation();
            }

            //ACTUALISATION DES PARAMETRES
            if (score1 < bestScore || score2 < bestScore || score3 < bestScore || score4 < bestScore || new Random().nextInt(100) < 20) {
                if (score1 < score2 && score1 < score3 && score1 < score4) {
                    bestScore = score1;
                    percent1 += 2;
                }
                if (score2 < score1 && score2 < score3 && score2 < score4) {
                    bestScore = score2;
                    percent1 -= 2;
                }
                if (score3 < score1 && score3 < score2 && score3 < score4) {
                    percent2 += 2;
                    bestScore = score3;
                }
                if (score4 < score1 && score4 < score2 && score4 < score3) {
                    percent2 -= 2;
                    bestScore = score4;
                }
            }
            System.out.println("\n\n");
            System.out.println("bestScore " + bestScore);
            System.out.println("percent1 " + percent1 + "   percent2 " + percent2);
            System.out.println("score1 " + score1 + "   score2 " + score2 + "   score3 " + score3 + "   score4 " + score4);
        }
    }
}
