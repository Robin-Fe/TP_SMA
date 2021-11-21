import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Simulation extends Observable implements Runnable {

    public Thread worker;
    private final List<Agent> listeAgents;
    private final Environnement environnement;
    public boolean verbose;
    private int score;
    private final int nbToursMax;

    public Simulation(boolean verbose, int nbAgents, int tailleMapLong, int tailleMapLarge, int nbObjectsA, int nbObjectsB, int nbToursMax) {
        List<Agent> listeAgents = new ArrayList<>();
        for (int i = 0; i < nbAgents; i++) {
            listeAgents.add(new Agent(Character.toString((char) 65 + i)));
        }
        Environnement environnement = new Environnement(tailleMapLong, tailleMapLarge, nbObjectsA, nbObjectsB, listeAgents, verbose);

        this.listeAgents = listeAgents;
        this.environnement = environnement;
        this.verbose = verbose;
        this.score = 0;
        this.nbToursMax = nbToursMax;
    }

    public int runSimulation() {
        int nbTours = 0;
        while (nbTours < 1000) {
            getEnvironnement().printEnvironment();
            nbTours++;

            for (Agent agent : this.listeAgents) {
                agent.perception(this.environnement);
                agent.action(environnement);
            }
            this.score = nbTours;
        }
        return score;
    }

    @Override
    public void run() {
        int nbTours = 0;
        long tempspause = 30;
        while (nbTours < 1000) {
            getEnvironnement().printEnvironment();
            nbTours++;

            for (Agent agent : this.listeAgents) {
                agent.perception(this.environnement);
                agent.action(environnement);
                setChanged();
                notifyObservers();
                try {
                    Thread.sleep(tempspause);
                } catch (InterruptedException ignored) {

                }
            }
            this.score = nbTours;
            setChanged();
            notifyObservers();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }
        }
    }


    public void interrupt() {
        worker.interrupt();
        setChanged();
        notifyObservers();
    }

    public void start() {
        Thread worker = new Thread(this);
        worker.start();
        setChanged();
        notifyObservers();
    }

    public int getScore() { return this.score; }

    public int getNbToursMax() { return this.nbToursMax; }

    public Environnement getEnvironnement() { return this.environnement; }

}
