import java.util.*;

public class Environnement extends Observable {
    private final List<Agent> listeAgents;
    private Objet[][][] map;
    private HashMap<Agent, int[]> agentHashMap;
    private final int nbObjectsA;
    private final int nbObjectsB;
    private final int nbObjectsC;
    public boolean verbose;

    public Environnement(int tailleMapLong, int tailleMapLarge, int nbObjectsA, int nbObjectsB, int nbObjectsC, List<Agent> listeAgents, boolean verbose) {
        this.map = new Objet[tailleMapLong][tailleMapLarge][3];
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
        for (int i = 0; i < nbObjectsC; i++) {
            while (!isFreeOfObject(r1, r2)) {
                r1 = new Random().nextInt(tailleMapLong);
                r2 = new Random().nextInt(tailleMapLarge);
            }
            map[r1][r2][1] = new ObjetC("");
        }

        this.verbose = verbose;
        this.listeAgents = listeAgents;
        this.agentHashMap = agentHashMap;
        this.nbObjectsA = nbObjectsA;
        this.nbObjectsB = nbObjectsB;
        this.nbObjectsC = nbObjectsC;
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

    public void moveHelper(Agent agent, int x, int y) {
        if (verbose) {
            System.out.println("Agent Helper " + agent.getName() + " moves to " + x + ", " + y);
        }
        map[findAgent(agent)[0]][findAgent(agent)[1]][2] = null;
        map[x][y][2] = agent;
        agentHashMap.put(agent, new int[]{x, y});
        setChanged();
        notifyObservers();
    }

    public void switchHelper(Agent agent) {
        if (agent.getIsHelping()) {
            map[findAgent(agent)[0]][findAgent(agent)[1]][0] = null;
        } else {
            map[findAgent(agent)[0]][findAgent(agent)[1]][2] = null;
        }
        setChanged();
        notifyObservers();
    }

    public boolean isFreeOfAgent(int x, int y) {
        return map[x][y][0] == null;
    }

    public boolean isFreeOfHelper(int x, int y) {
        return map[x][y][2] == null;
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
        } else if (getObject(x, y) instanceof ObjetB){
            object = new ObjetB("");
        } else {
            object = new ObjetC("");
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

    public int getNbObjectsC() {
        return this.nbObjectsC;
    }

    public int getNbTas(Objet objetType) {
        int nbTas = 0;
        List<Objet> seen = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                Objet objet = map[x][y][1];
                if (objetType instanceof ObjetA) {
                    if (objet instanceof ObjetA) {
                        nbTas = calculateNbTas(nbTas, seen, x, y, objet);
                    }
                } else if (objetType instanceof ObjetB) {
                    if (objet instanceof ObjetB) {
                        nbTas = calculateNbTas(nbTas, seen, x, y, objet);
                    }
                } else if (objetType instanceof ObjetC) {
                if (objet instanceof ObjetC) {
                    nbTas = calculateNbTas(nbTas, seen, x, y, objet);
                }
            }
            }
        }
        return nbTas;
    }

    private int calculateNbTas(int nbTas, List<Objet> seen, int x, int y, Objet objet) {
        List<Coordinate> neighbours = getNeighbours(x, y, objet);
        boolean isAlone = true;
        for (Coordinate coordinate : neighbours) {
            Objet newObjet = map[coordinate.getX()][coordinate.getY()][1];
            if (seen.contains(newObjet)) {
                isAlone = false;
                break;
            }
        }
        if (isAlone) {
            nbTas++;
        }
        seen.add(objet);
        return nbTas;
    }

    public List<Coordinate> getNeighbours(int x, int y, Objet objet) {
        List<Coordinate> freeDirections = new ArrayList<>();
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;
        boolean upLeft = false;
        boolean upRight = false;
        boolean downLeft = false;
        boolean downRight = false;
        if (objet instanceof ObjetA) {
            if (y + 1 < map[0].length)
                up = map[x][y + 1][1] instanceof ObjetA;
            if (y - 1 >= 0)
                down = map[x][y - 1][1] instanceof ObjetA;
            if (x - 1 >= 0)
                left = map[x - 1][y][1] instanceof ObjetA;
            if (x + 1 < map.length)
                right = map[x + 1][y][1] instanceof ObjetA;
            if (x - 1 >= 0 && y + 1 < map[0].length)
                upLeft = map[x - 1][y + 1][1] instanceof ObjetA;
            if (x + 1 < map.length && y + 1 < map[0].length)
                upRight = map[x + 1][y + 1][1] instanceof ObjetA;
            if (x - 1 >= 0 && y - 1 >= 0)
                downLeft = map[x - 1][y - 1][1] instanceof ObjetA;
            if (x + 1 < map.length && y - 1 >= 0)
                downRight = map[x + 1][y - 1][1] instanceof ObjetA;
        }
        if (objet instanceof ObjetB) {
            if (y + 1 < map[0].length)
                up = map[x][y + 1][1] instanceof ObjetB;
            if (y - 1 >= 0)
                down = map[x][y - 1][1] instanceof ObjetB;
            if (x - 1 >= 0)
                left = map[x - 1][y][1] instanceof ObjetB;
            if (x + 1 < map.length)
                right = map[x + 1][y][1] instanceof ObjetB;
            if (x - 1 >= 0 && y + 1 < map[0].length)
                upLeft = map[x - 1][y + 1][1] instanceof ObjetB;
            if (x + 1 < map.length && y + 1 < map[0].length)
                upRight = map[x + 1][y + 1][1] instanceof ObjetB;
            if (x - 1 >= 0 && y - 1 >= 0)
                downLeft = map[x - 1][y - 1][1] instanceof ObjetB;
            if (x + 1 < map.length && y - 1 >= 0)
                downRight = map[x + 1][y - 1][1] instanceof ObjetB;
        }
        if (objet instanceof ObjetC) {
            if (y + 1 < map[0].length)
                up = map[x][y + 1][1] instanceof ObjetC;
            if (y - 1 >= 0)
                down = map[x][y - 1][1] instanceof ObjetC;
            if (x - 1 >= 0)
                left = map[x - 1][y][1] instanceof ObjetC;
            if (x + 1 < map.length)
                right = map[x + 1][y][1] instanceof ObjetC;
            if (x - 1 >= 0 && y + 1 < map[0].length)
                upLeft = map[x - 1][y + 1][1] instanceof ObjetC;
            if (x + 1 < map.length && y + 1 < map[0].length)
                upRight = map[x + 1][y + 1][1] instanceof ObjetC;
            if (x - 1 >= 0 && y - 1 >= 0)
                downLeft = map[x - 1][y - 1][1] instanceof ObjetC;
            if (x + 1 < map.length && y - 1 >= 0)
                downRight = map[x + 1][y - 1][1] instanceof ObjetC;
        }
        if (up)
            freeDirections.add(new Coordinate(x, y + 1));
        if (down)
            freeDirections.add(new Coordinate(x, y - 1));
        if (left)
            freeDirections.add(new Coordinate(x - 1, y));
        if (right)
            freeDirections.add(new Coordinate(x + 1, y));
        if (upRight)
            freeDirections.add(new Coordinate(x+1, y+1));
        if (upLeft)
            freeDirections.add(new Coordinate(x-1, y+1));
        if (downRight)
            freeDirections.add(new Coordinate(x+1, y-1));
        if (downLeft)
            freeDirections.add(new Coordinate(x-1, y-1));
        return freeDirections;
    }

    public List<Agent> getListeAgents() {
        return listeAgents;
    }
}
