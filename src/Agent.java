import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Agent extends Objet {

    private Objet heldObject;
    private boolean hasObject;
    private final double k1;
    private final double k2;
    private final int Tsize;
    private double fA;
    private double fB;
    private double e;
    private Queue<Class> lastVisited;

    public Agent(String name, double k1, double k2, int Tsize, double e) {
        super(name);
        this.lastVisited = new LinkedList<>();
        this.k1 = k1;
        this.k2 = k2;
        this.e = e;
        this.Tsize = Tsize;
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
        for (int i = 0; i < lastVisited.size(); i++) {
            if (lastVisited.toArray()[i] == ObjetA.class) {
                nbA++;
            }
            if (lastVisited.toArray()[i] == ObjetB.class) {
                nbB++;
            }
        }
        fA = (double) nbA / Tsize + nbB * e;
        fB = (double) nbB / Tsize + nbA * e;
    }

    public void action(Environnement environment) {
        //PICK OBJECT
        if (!this.getHasObject() && !environment.isFreeOfObject(environment.findAgent(this)[0], environment.findAgent(this)[1])) {
            double pickProb = 0;
            if (environment.getObject(environment.findAgent(this)[0], environment.findAgent(this)[1]) instanceof ObjetA) {
                pickProb = Math.pow((k1 / (k1 + fA)), 2);
            }
            if (environment.getObject(environment.findAgent(this)[0], environment.findAgent(this)[1]) instanceof ObjetB) {
                pickProb = Math.pow((k1 / (k1 + fB)), 2);
            }
            if (pickProb >= new Random().nextDouble()) {
                setHeldObject(environment.pickObject(environment.findAgent(this)[0], environment.findAgent(this)[1]));
                setHasObject(true);
            }
        }
        perception(environment);

        //DROP OBJECT
        if (this.getHasObject() && environment.isFreeOfObject(environment.findAgent(this)[0], environment.findAgent(this)[1])) {
            double dropProb = 0.0;
            if (this.getHeldObject() instanceof ObjetA) {
                dropProb = Math.pow((fA / (k2 + fA)), 2);
            }
            if (this.getHeldObject() instanceof ObjetB) {
                dropProb = Math.pow((fB / (k2 + fB)), 2);
            }
            if (dropProb >= new Random().nextDouble()) {
                environment.dropObject(environment.findAgent(this)[0], environment.findAgent(this)[1], getHeldObject());
                setHeldObject(null);
                setHasObject(false);
            }
        }
        perception(environment);

        //MOVE
        int xMove = new Random().nextInt(2);
        int yMove = new Random().nextInt(2);
        while (xMove + yMove < 1) {
            xMove = new Random().nextInt(2);
            yMove = new Random().nextInt(2);
        }
        int xDirection = new Random().nextInt(2);
        int yDirection = new Random().nextInt(2);

        if (xDirection == 0) {
            if (environment.findAgent(this)[0] - xMove < 0) {
                return;
            }
            if (yDirection == 0) {
                if (environment.findAgent(this)[1] - yMove < 0) {
                    return;
                }
                if (environment.isFreeOfAgent(environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] - yMove)) {
                    environment.moveAgent(this, environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] - yMove);
                }
            } else {
                if (environment.findAgent(this)[1] + yMove >= environment.getLargeMap()) {
                    return;
                }
                if (environment.isFreeOfAgent(environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] + yMove)) {
                    environment.moveAgent(this, environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] + yMove);
                }
            }
        } else {
            if (environment.findAgent(this)[0] + xMove >= environment.getLongMap()) {
                return;
            }
            if (yDirection == 0) {
                if (environment.findAgent(this)[1] - yMove < 0) {
                    return;
                }
                if (environment.isFreeOfAgent(environment.findAgent(this)[0] + xMove, environment.findAgent(this)[1] - yMove)) {
                    environment.moveAgent(this, environment.findAgent(this)[0] + xMove, environment.findAgent(this)[1] - yMove);
                }
            } else {
                if (environment.findAgent(this)[1] + yMove >= environment.getLargeMap()) {
                    return;
                }
                if (environment.isFreeOfAgent(environment.findAgent(this)[0] + xMove, environment.findAgent(this)[1] + yMove)) {
                    environment.moveAgent(this, environment.findAgent(this)[0] + xMove, environment.findAgent(this)[1] + yMove);
                }
            }
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
}