public class Main {

    public static void main(String[] args) {
        SimulationTesting(30, 30, 30, 30, 10, 1000);
    }

    public static void SimulationTesting(int tailleMapLong, int tailleMapLarge, int nbObjectsA, int nbObjectsB, int nbAgents, int nbToursMax) {
        System.out.println("Score : " + new Simulation(true, tailleMapLong, tailleMapLarge, nbObjectsA, nbObjectsB, nbAgents, nbToursMax).runSimulation());
    }
}
