public class Main {

    public static void main(String[] args) {
        int score = 0;
        int nbSimulations = 500;
        NormalStrategy normalStrategy = new NormalStrategy();
        CommunicateStrategy communicateStrategy = new CommunicateStrategy();
        GlobalStrategy globalStrategy = new GlobalStrategy();
        for (int i = 0; i < nbSimulations; i++) {
            Simulation simulation = new Simulation(false, true, globalStrategy, 10, 3);
            score += simulation.runSimulation();
        }
        System.out.println("Pour " + nbSimulations + " Simulation, score total : " + score);
    }
}
