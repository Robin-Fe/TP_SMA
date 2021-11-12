public class Main {

    public static void main(String[] args) {
        int deplacementsRandomScore = 0;
        int deplacementsCommunicationScore = 0;
        int deplacementsGlobalScore = 0;
        int actionRandomScore = 0;
        int actionCommunicationScore = 0;
        int actionGlobalScore = 0;
        int nbSimulations = 100;
        RandomStrategy randomStrategy = new RandomStrategy();
        CommunicateStrategy communicateStrategy = new CommunicateStrategy();
        GlobalStrategy globalStrategy = new GlobalStrategy();
        for (int i = 0; i < nbSimulations; i++) {
            Simulation randomSimulation = new Simulation(false, true, randomStrategy, 10, 3);
            Simulation communicateSimulation = new Simulation(false, true, communicateStrategy, 10, 3);
            Simulation globalSimulation = new Simulation(false, true, globalStrategy, 10, 3);
            randomSimulation.runSimulation();
            communicateSimulation.runSimulation();
            globalSimulation.runSimulation();
            deplacementsRandomScore += randomSimulation.getDeplacementScore();
            deplacementsCommunicationScore += communicateSimulation.getDeplacementScore();
            deplacementsGlobalScore += globalSimulation.getDeplacementScore();
            actionRandomScore += randomSimulation.getActionScore();
            actionCommunicationScore += communicateSimulation.getActionScore();
            actionGlobalScore += globalSimulation.getActionScore();
        }
        System.out.println("Pour " + nbSimulations + " Simulations : \n");
        System.out.println("Deplacements - Score moyen random : " + deplacementsRandomScore/nbSimulations );
        System.out.println("Deplacements - Score moyen communication : " + deplacementsCommunicationScore/nbSimulations );
        System.out.println("Deplacements - Score moyen global : " + deplacementsGlobalScore/nbSimulations );
        System.out.println("\n");
        System.out.println("Actions - Score moyen random : " + actionRandomScore/nbSimulations );
        System.out.println("Actions - Score moyen communication : " + actionCommunicationScore/nbSimulations );
        System.out.println("Actions - Score moyen global : " + actionGlobalScore/nbSimulations );
    }
}
