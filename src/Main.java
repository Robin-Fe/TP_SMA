public class Main {

    public static void main(String[] args) {
        int score = 0;
        int nbSimulations = 1;
        for (int i = 0; i < nbSimulations; i++) {
            //Simulation simulation = new Simulation(false, true, 10);
            GlobalSimulation simulation = new GlobalSimulation(true, true, 10, new GlobalStrategy(), 3);
            score += simulation.runSimulation();
        }
        System.out.println("Pour " + nbSimulations + " Simulation, score total : " + score);
    }
}
