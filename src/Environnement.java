import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class Environnement extends Observable {
    private final List<Agent> listeAgents;
    private Objet[][][] map;
    private HashMap<Agent, int[]> agentHashMap;
    private final int nbObjectsA;
    private final int nbObjectsB;
    public boolean verbose;

    public Environnement(int tailleMapLong, int tailleMapLarge, int nbObjectsA, int nbObjectsB, List<Agent> listeAgents, boolean verbose) {
        this.map = new Objet[tailleMapLong][tailleMapLarge][2];
        HashMap<Agent, int[]> agentHashMap = new HashMap<>();
        int r1 = new Random().nextInt(tailleMapLong);
        int r2 = new Random().nextInt(tailleMapLarge);
        for (Agent agent : listeAgents) {
            while (!isFreeOfAgent(r1, r2)) {
                r1 = new Random().nextInt(tailleMapLong);
                r2 = new Random().nextInt(tailleMapLarge);
            }
            map[r1][r2][0] = agent;
            agentHashMap.put(agent, new int[]{r1, r2});
        }
        for (int i = 0; i < nbObjectsA; i++) {
            while (!isFreeOfObject(r1, r2)) {
                r1 = new Random().nextInt(tailleMapLong);
                r2 = new Random().nextInt(tailleMapLarge);
            }
            map[r1][r2][1] = new ObjetA("");
        }
        for (int i = 0; i < nbObjectsB; i++) {
            while (!isFreeOfObject(r1, r2)) {
                r1 = new Random().nextInt(tailleMapLong);
                r2 = new Random().nextInt(tailleMapLarge);
            }
            map[r1][r2][1] = new ObjetB("");
        }

        this.verbose = verbose;
        this.listeAgents = listeAgents;
        this.agentHashMap = agentHashMap;
        this.nbObjectsA = nbObjectsA;
        this.nbObjectsB = nbObjectsB;
    }

    public void moveAgent(Agent agent, int x, int y) {
        if (verbose) {
            System.out.println("Agent " + agent.getName() + " moves to " + x + ", " + y);
        }
        map[findAgent(agent)[0]][findAgent(agent)[1]][0] = null;
        map[x][y][0] = agent;
        agentHashMap.put(agent, new int[]{x, y});
        setChanged();
        notifyObservers();
    }

    public boolean isFreeOfAgent(int x, int y) {
        return map[x][y][0] == null;
    }

    public boolean isFreeOfObject(int x, int y) {
        return map[x][y][1] == null;
    }

    public Agent getAgent(int x, int y) {
        return (Agent) map[x][y][0];
    }

    public Objet getObject(int x, int y) {
        return map[x][y][1];
    }

    public Objet pickObject(int x, int y) {
        Objet object;
        if (getObject(x, y) instanceof ObjetA) {
            object = new ObjetA("");
        } else {
            object = new ObjetB("");
        }
        map[x][y][1] = null;
        setChanged();
        notifyObservers();
        return object;
    }

    public void dropObject(int x, int y, Objet objet) {
        map[x][y][1] = objet;
        setChanged();
        notifyObservers();
    }

    public void printEnvironment() {
        for (int i = 0; i < map.length; i++) {
            System.out.println("-------".repeat(map[0].length));
            StringBuilder string = new StringBuilder();
            string.append("|");
            for (int j = 0; j < map[0].length; j++) {
                if (!isFreeOfAgent(i, j)) {
                    string.append(getAgent(i, j).getName()).append(" ");
                    if (getAgent(i, j).getHasObject()) {
                        string.append(getAgent(i, j).getHeldObject().getName()).append(" ");
                    } else {
                        string.append("  ");
                    }
                } else {
                    string.append("    ");
                }
                if (!isFreeOfObject(i, j)) {
                    string.append(getObject(i, j).getName()).append(" ");
                } else {
                    string.append("  ");
                }
                string.append("|");
            }
            System.out.println(string);
        }
        System.out.println("-------".repeat(map[0].length) + "-");
    }

    public int getNbAgents() {
        return this.listeAgents.size();
    }

    public int[] findAgent(Agent agent) {
        return this.agentHashMap.get(agent);
    }

    public int getLongMap() {
        return this.map.length;
    }

    public int getLargeMap() {
        return this.map[0].length;
    }

    public int getNbObjectsA() {
        return this.nbObjectsA;
    }

    public int getNbObjectsB() {
        return this.nbObjectsB;
    }

}
