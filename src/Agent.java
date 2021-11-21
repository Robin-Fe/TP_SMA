import java.util.Random;

public class Agent extends Objet {

    private Objet heldObject;
    private boolean hasObject;

    public Agent(String name) {
        super(name);
    }

    public void perception(Environnement environment) {
        //TODO : Implement Agent perception
    }

    public void action(Environnement environment) {
        //TODO : Implement Agent action

        //PICK OBJECT
        if (!this.getHasObject() && !environment.isFreeOfObject(environment.findAgent(this)[0], environment.findAgent(this)[1])) {
            int pickProb = 50;
            if (pickProb < new Random().nextInt(100)) {
                setHeldObject(environment.pickObject(environment.findAgent(this)[0], environment.findAgent(this)[1]));
                setHasObject(true);
            }
        }

        //DROP OBJECT
        if (this.getHasObject() && environment.isFreeOfObject(environment.findAgent(this)[0], environment.findAgent(this)[1])) {
            int dropProb = 50;
            if (dropProb < new Random().nextInt(100)) {
                environment.dropObject(environment.findAgent(this)[0], environment.findAgent(this)[1], getHeldObject());
                setHeldObject(null);
                setHasObject(false);
            }
        }

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
                System.out.println("ALED X-");
                return;
            }
            if (yDirection == 0) {
                if (environment.findAgent(this)[1] - yMove < 0) {
                    System.out.println("ALED Y-");
                    return;
                }
                if (environment.isFreeOfAgent(environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] - yMove)) {
                    environment.moveAgent(this, environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] - yMove);
                }
            } else {
                if (environment.findAgent(this)[1] + yMove >= environment.getLargeMap()) {
                    System.out.println("ALED Y+");
                    return;
                }
                if (environment.isFreeOfAgent(environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] + yMove)) {
                    environment.moveAgent(this, environment.findAgent(this)[0] - xMove, environment.findAgent(this)[1] + yMove);
                }
            }
        } else {
            if (environment.findAgent(this)[0] + xMove >= environment.getLongMap()) {
                System.out.println("OSKOUR X+");
                return;
            }
            if (yDirection == 0) {
                if (environment.findAgent(this)[1] - yMove < 0) {
                    System.out.println("OSKOUR Y-");
                    return;
                }
                if (environment.isFreeOfAgent(environment.findAgent(this)[0] + xMove, environment.findAgent(this)[1] - yMove)) {
                    environment.moveAgent(this, environment.findAgent(this)[0] + xMove, environment.findAgent(this)[1] - yMove);
                }
            } else {
                if (environment.findAgent(this)[1] + yMove >= environment.getLargeMap()) {
                    System.out.println("OSKOUR Y+");
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
}
