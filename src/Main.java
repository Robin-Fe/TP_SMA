public class Main {

    public static void main(String[] args) {
        int score = 0;
        int nbSimulations = 10;
        for (int i = 0; i < nbSimulations; i++) {
            //Simulation simulation = new Simulation(false, true, 10);
           SmartSimulation simulation = new SmartSimulation(false, true, 10, new Strategy());
            score += simulation.runSimulation();
        }
        System.out.println("Pour " + nbSimulations + " Simulation, score total : " + score);
    }
}
