import java.util.*;

public class Agent extends Objet {

    private Objet heldObject;
    private boolean hasObject;
    private boolean isHelping;
    private boolean waitingForHelp;
    private Agent helperAgent;
    private final double k1;
    private final double k2;
    private final int Tsize;
    private double fA;
    private double fB;
    private double fC;
    private final double e;
    private Queue<Class> lastVisited;

    public Agent(String name, double k1, double k2, int Tsize, double e) {
        super(name);
        this.lastVisited = new LinkedList<>();
        this.k1 = k1;
        this.k2 = k2;
        this.e = e;
        this.Tsize = Tsize;
        this.isHelping = false;
        this.waitingForHelp = false;
        this.helperAgent = null;
    }

    public void perception(Environnement environment) {
        if (this.lastVisited.size() == this.Tsize) {
            this.lastVisited.remove();
        }
        if (environment.getObject(environment.findAgent(this)[0], environment.findAgent(this)[1]) != null) {
            this.lastVisited.add(environment.getObject(environment.findAgent(this)[0], environment.findAgent(this)[1]).getClass());
        } else {
            this.lastVisited.add(this.getClass());
        }
        int nbA = 0;
        int nbB = 0;
        int nbC = 0;
        for (int i = 0; i < lastVisited.size(); i++) {
            if (lastVisited.toArray()[i] == ObjetA.class) {
                nbA++;
            }
            if (lastVisited.toArray()[i] == ObjetB.class) {
                nbB++;
            }
            if (lastVisited.toArray()[i] == ObjetC.class) {
                nbC++;
            }
        }
        fA = (double) nbA / Tsize + (nbB+nbC) * e;
        fB = (double) nbB / Tsize + (nbA+nbC) * e;
        fC = (double) nbC / Tsize + (nbA+nbB) * e;

        for (Agent agent : environment.getListeAgents()) {
            if (agent != this) {
                if (agent.waitingForHelp) {
                    if (Math.abs(environment.findAgent(agent)[0] - environment.findAgent(this)[0]) <= 1 && Math.abs(environment.findAgent(agent)[1] - environment.findAgent(this)[1]) <= 1) {
                        helpAgent(agent, environment);
                        break;
                    }
                }
            }
        }
    }

    public void action(Environnement environment) {
        if (waitingForHelp) {
            if (new Random().nextDouble() > 0.8) {
                waitingForHelp = false;
            }
            return;
        }

        if (isHelping) {
            return;
        }
        //PICK OBJECT
        if (!this.getHasObject() && !environment.isFreeOfObject(environment.findAgent(this)[0], environment.findAgent(this)[1])) {
            double pickProb = 0;
            Objet objectHere = environment.getObject(environment.findAgent(this)[0], environment.findAgent(this)[1]);
            if (objectHere instanceof ObjetA) {
                pickProb = Math.pow((k1 / (k1 + fA)), 2);
            }
            if (objectHere instanceof ObjetB) {
                pickProb = Math.pow((k1 / (k1 + fB)), 2);
            }
            if (objectHere instanceof ObjetC) {
                pickProb = Math.pow((k1 / (k1 + fC)), 2);
            }
            if (pickProb >= new Random().nextDouble()) {
                if (objectHere instanceof ObjetC) {
                    environment.addPheromones(environment.findAgent(this)[0], environment.findAgent(this)[1]);
                    waitingForHelp = true;
                    return;
                } else {
                    setHeldObject(environment.pickObject(environment.findAgent(this)[0], environment.findAgent(this)[1]));
                    setHasObject(true);
                }
            }
        }

        //DROP OBJECT
        if (this.getHasObject() && environment.isFreeOfObject(environment.findAgent(this)[0], environment.findAgent(this)[1])) {
            double dropProb = 0.0;
            if (this.getHeldObject() instanceof ObjetA) {
                dropProb = Math.pow((fA / (k2 + fA)), 2);
            }
            else if (this.getHeldObject() instanceof ObjetB) {
                dropProb = Math.pow((fB / (k2 + fB)), 2);
            }
            else if (this.getHeldObject() instanceof ObjetC) {
                dropProb = Math.pow((fC / (k2 + fC)), 2);
            }
            else {
                dropProb = 1.0;
            }
            if (dropProb >= new Random().nextDouble()) {
                environment.dropObject(environment.findAgent(this)[0], environment.findAgent(this)[1], getHeldObject());
                setHeldObject(null);
                setHasObject(false);
                if (helperAgent != null) {
                    helperAgent.freeHelper(environment);
                    helperAgent = null;
                }
            }
        }

        //MOVE
        HashMap<Coordinate, Integer> freeDirections = environment.getFreeDirections(environment.findAgent(this)[0], environment.findAgent(this)[1]);
        Coordinate bestCoordinate;
        if (!hasObject) {
            int maxPheromone = -1;
            List<Coordinate> bestCoordinates = new ArrayList<>();
            for (Map.Entry<Coordinate, Integer> entry : freeDirections.entrySet()) {
                Coordinate coordinate = entry.getKey();
                int pheromone = entry.getValue();
                if (pheromone > maxPheromone) {
                    maxPheromone = pheromone;
                    bestCoordinates = new ArrayList<>();
                    bestCoordinates.add(coordinate);
                }
                if (pheromone == maxPheromone) {
                    bestCoordinates.add(coordinate);
                }
            }
            if (new Random().nextDouble() > 0.8 && bestCoordinates.size() > 0) {
                bestCoordinate = bestCoordinates.get(new Random().nextInt(bestCoordinates.size()));
            } else {
                if (freeDirections.keySet().size() > 0) {
                    bestCoordinate = (Coordinate) freeDirections.keySet().toArray()[new Random().nextInt(freeDirections.keySet().size())];
                } else {
                    bestCoordinate = new Coordinate(environment.findAgent(this)[0], environment.findAgent(this)[1]);
                }
            }
        } else {
            if (freeDirections.keySet().size() > 0) {
                bestCoordinate = (Coordinate) freeDirections.keySet().toArray()[new Random().nextInt(freeDirections.keySet().size())];
            } else {
                bestCoordinate = new Coordinate(environment.findAgent(this)[0], environment.findAgent(this)[1]);
            }
        }
        if (environment.isFreeOfAgent(bestCoordinate.getX(), bestCoordinate.getY())) {
            environment.moveAgent(this, bestCoordinate.getX(), bestCoordinate.getY());
        }

        if (helperAgent != null) {
            environment.moveHelper(helperAgent, environment.findAgent(this)[0], environment.findAgent(this)[1]);
        }
    }

    public void setHeldObject(Objet object) {
        this.heldObject = object;
    }

    public boolean getHasObject() {
        return this.hasObject;
    }

    public void setHasObject(boolean bool) {
        this.hasObject = bool;
    }

    public Objet getHeldObject() {
        return this.heldObject;
    }

    public double getK1() {
        return k1;
    }

    public double getK2() {
        return k1;
    }

    public int getTsize() {
        return Tsize;
    }

    public double getE() {
        return e;
    }

    public boolean getIsHelping() {
        return isHelping;
    }

    public void freeHelper(Environnement environment) {
        isHelping = false;
        environment.switchHelper(this);
    }

    public void helpAgent(Agent agent, Environnement environment) {
        if (agent.waitingForHelp) {
            agent.waitingForHelp = false;
            agent.setHelperAgent(this, environment);
            agent.setHeldObject(environment.pickObject(environment.findAgent(agent)[0], environment.findAgent(agent)[1]));
            agent.setHasObject(true);
            environment.removePheromones(environment.findAgent(this)[0], environment.findAgent(this)[1]);
            isHelping = true;
        }
    }

    public void setHelperAgent(Agent agent, Environnement environment) {
        helperAgent = agent;
        environment.switchHelper(agent);
    }
}