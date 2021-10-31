public class Main {

    public static void main(String[] args) {
        int score = 0;
        int nbSimulations = 1;
        for (int i = 0; i < nbSimulations; i++) {
            //Simulation simulation = new Simulation(false, true, 10);
            SmartSimulation simulation = new SmartSimulation(true, true, 5, new Strategy());
            score += simulation.runSimulation();
        }
        System.out.println("Pour " + nbSimulations + " Simulation, score total : " + score);
    }
}
