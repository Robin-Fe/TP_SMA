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

    public Simulation(boolean verbose, int nbAgents, int tailleMapLong, int tailleMapLarge, int nbObjectsA, int nbObjectsB, int nbObjectsC, int nbToursMax, double k1, double k2, int Tsize, double e) {
        List<Agent> listeAgents = new ArrayList<>();
        for (int i = 0; i < nbAgents; i++) {
            listeAgents.add(new Agent(Character.toString((char) 65 + i), k1, k2, Tsize, e));
        }
        Environnement environnement = new Environnement(tailleMapLong, tailleMapLarge, nbObjectsA, nbObjectsB, nbObjectsC, listeAgents, verbose);

        this.listeAgents = listeAgents;
        this.environnement = environnement;
        this.verbose = verbose;
        this.score = 0;
        this.nbToursMax = nbToursMax;
    }

    public Simulation(boolean verbose, List<Agent> listeAgents, Environnement environnement, int nbToursMax) {
        this.listeAgents = listeAgents;
        this.environnement = environnement;
        this.verbose = verbose;
        this.score = 0;
        this.nbToursMax = nbToursMax;
    }

    public int runSimulation() {
        int nbTours = 0;
        while (nbTours < nbToursMax) {
            if (verbose) {
                getEnvironnement().printEnvironment();
            } else {
                if (nbTours % 100000 == 0) {
                    System.out.println("Tour " + nbTours);
                    //getEnvironnement().printEnvironment();
                    System.out.println("Nombre de tas A : " + getEnvironnement().getNbTas(new ObjetA("")));
                    System.out.println("Nombre de tas B : " + getEnvironnement().getNbTas(new ObjetB("")));
                    System.out.println("Nombre de tas C : " + getEnvironnement().getNbTas(new ObjetC("")) + "\n");
                }
            }
            nbTours++;

            for (Agent agent : this.listeAgents) {
                agent.perception(this.environnement);
                agent.action(environnement);
            }
            environnement.updateMap();
            environnement.attenuatePheromones();
            this.score = nbTours;
        }
        System.out.println("Nombre de tas A : " + getEnvironnement().getNbTas(new ObjetA("")));
        System.out.println("Nombre de tas B : " + getEnvironnement().getNbTas(new ObjetB("")));
        System.out.println("Nombre de tas C : " + getEnvironnement().getNbTas(new ObjetC("")) + "\n");
        return score;
    }

    @Override
    public void run() {
        int nbTours = 0;
        long tempspause = 1;
        while (nbTours < nbToursMax) {
            if (verbose)
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
            environnement.updateMap();
            environnement.attenuatePheromones();
            this.score = nbTours;
            setChanged();
            notifyObservers();
            try {
                Thread.sleep(500);
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

    public int getScore() {
        return this.score;
    }

    public int getNbToursMax() {
        return this.nbToursMax;
    }

    public Environnement getEnvironnement() {
        return this.environnement;
    }

    public List<Agent> getListeAgents() {
        return listeAgents;
    }
}