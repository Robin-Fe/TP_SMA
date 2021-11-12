public class Main {

    public static void main(String[] args) {
        int deplacementsRandomScore = 0;
        int deplacementsCommunicationScore = 0;
        int deplacementsGlobalScore = 0;
        int actionRandomScore = 0;
        int actionCommunicationScore = 0;
        int actionGlobalScore = 0;
        int nbSimulations = 500;
        boolean verbose = false;
        boolean isRandom = true;
        int nbAgents = 20;
        int nbPiles = 10;
        RandomStrategy randomStrategy = new RandomStrategy();
        CommunicateStrategy communicateStrategy = new CommunicateStrategy();
        GlobalStrategy globalStrategy = new GlobalStrategy();
        for (int i = 0; i < nbSimulations; i++) { ;
            System.out.println(i);
            Simulation randomSimulation = new Simulation(verbose, isRandom, randomStrategy, nbAgents, nbPiles);
            randomSimulation.runSimulation();
            deplacementsRandomScore += randomSimulation.getDeplacementScore();
            actionRandomScore += randomSimulation.getActionScore();

            Simulation communicateSimulation = new Simulation(verbose, isRandom, communicateStrategy, nbAgents, nbPiles);
            communicateSimulation.runSimulation();
            deplacementsCommunicationScore += communicateSimulation.getDeplacementScore();
            actionCommunicationScore += communicateSimulation.getActionScore();


            Simulation globalSimulation = new Simulation(verbose, isRandom, globalStrategy, nbAgents, nbPiles);
            globalSimulation.runSimulation();
            deplacementsGlobalScore += globalSimulation.getDeplacementScore();
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
