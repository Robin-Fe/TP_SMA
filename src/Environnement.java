import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Environnement {

    private List<Stack<Agent>> piles;
    public int nbPiles;
    private int test;

    public Environnement(int nbPiles) {
        this.nbPiles = nbPiles;
        this.piles =  new ArrayList<>(3);
    }

    public boolean isEmpty (Stack<Agent> pile) {
        return pile.isEmpty();
    }

    public boolean isMoveable (Agent agent, Stack<Agent> pile) {
        return pile.firstElement() == agent;
    }

    public boolean moveAgent (Agent agent, Stack<Agent> pileArrivee) {
        Stack<Agent> pileDepart = getPile(getPlace(agent));
        if (isMoveable(agent, pileDepart)) {
            pileDepart.pop();
            pileArrivee.add(agent);
            return true;
        }
        //ToDo check
        // getPreviousAgent(agent).pushed();
        return false;
    }

    public Agent getNextAgent(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        return pile.elementAt(pile.search(agent)+1);
    }

    public Agent getPreviousAgent(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        return pile.elementAt(pile.search(agent)-1);
    }

    public Stack<Agent> getPile (int place) {
        return this.getPiles().get(place);
    }

    public List<Stack<Agent>> getPiles() {
        return this.piles;
    }

    public int getPlace (Agent agent) {
        for (int i = 0; i < nbPiles; i++) {
            if (getPile(i).contains(agent)) {
                return i;
            }
        }
        return -1;
    }



    public boolean getIsFree(Agent agent) {
        // ToDo : return real agent
        return true;
    }
    public boolean getIsPushed(Agent agent) {
        // ToDo : return real agent
        return true;
    }
    public void seDeplacer(Agent agent, int index) {
        test = 0;
    }


    public void pousser(Agent agent) {
        test = 0;
    }


}
