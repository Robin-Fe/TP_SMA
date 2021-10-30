public class Main {

    public static void main(String[] args) {
        int score = 0;
        int nbSimulations = 10;
        Politique randomPolitique = new RandomPolitique();
        for (int i = 0; i < nbSimulations; i++) {
            Simulation simulation = new Simulation(true, true, 15, randomPolitique, randomPolitique, randomPolitique);
            score += simulation.runSimulation();
        }
        System.out.println("Pour " + nbSimulations + " Simulation, score total : " + score);
    }
}
