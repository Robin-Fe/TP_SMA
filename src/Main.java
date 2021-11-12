public class Main {

    public static void main(String[] args) {
        int randomScore = 0;
        int communicationScore = 0;
        int globalScore = 0;
        int nbSimulations = 200;
        RandomStrategy randomStrategy = new RandomStrategy();
        CommunicateStrategy communicateStrategy = new CommunicateStrategy();
        GlobalStrategy globalStrategy = new GlobalStrategy();
        for (int i = 0; i < nbSimulations; i++) {
            Simulation randomSimulation = new Simulation(false, true, randomStrategy, 10, 3);
            Simulation communicateSimulation = new Simulation(false, true, communicateStrategy, 10, 3);
            Simulation globalSimulation = new Simulation(false, true, globalStrategy, 10, 3);
            randomScore += randomSimulation.runSimulation();
            communicationScore += communicateSimulation.runSimulation();
            globalScore += globalSimulation.runSimulation();
        }
        System.out.println("Pour " + nbSimulations + " Simulations : \n");
        System.out.println("Score moyen random : " + randomScore/nbSimulations );
        System.out.println("Score moyen communication : " + communicationScore/nbSimulations );
        System.out.println("Score moyen global : " + globalScore/nbSimulations );
    }
}
