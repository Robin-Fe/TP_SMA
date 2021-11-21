public class Main {

    public static void main(String[] args) {
        SimulationTesting(50, 50, 200, 200, 20, 20000, 0.1, 0.3, 50);
    }

    public static void SimulationTesting(int tailleMapLong, int tailleMapLarge, int nbObjectsA, int nbObjectsB, int nbAgents, int nbToursMax, double k1, double k2, int Tsize) {
        System.out.println("Score : " + new Simulation(false, nbAgents, tailleMapLong, tailleMapLarge, nbObjectsA, nbObjectsB, nbToursMax, k1, k2, Tsize).runSimulation());
    }
}
