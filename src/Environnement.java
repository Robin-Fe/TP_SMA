import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Environnement {

    private List<Stack<Agent>> piles;
    private int nbPiles;
    private Agent table;

    public Environnement(int nbPiles, Agent table) {
        this.nbPiles = nbPiles;
        this.piles = new ArrayList<>(3);
        for (int i = 0; i < nbPiles; i++) {
            Stack<Agent> pile = new Stack<>();
            this.piles.add(pile);
        }
        this.table = table;
    }


    public void seDeplacer(Agent agent, int indexPileArrivee) {
        if (indexPileArrivee != getPlace(agent)) {
            agent.setPush(false);
        }
        Stack<Agent> pileArrivee = getPile(indexPileArrivee);
        Stack<Agent> pileDepart = getPile(getPlace(agent));
        Agent agentPop = pileDepart.pop();
        pileArrivee.push(agentPop);
    }

    public Agent getNextAgent(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        int index = pile.indexOf(agent);
        if (index == pile.size() - 1)
            return null;
        return pile.get(index+1);
    }

    public Agent getPreviousAgent(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        int index = pile.indexOf(agent);
        if (index == 0)
            return this.table;
        return pile.get(index-1);
    }

    public Stack<Agent> getPile(int place) {
        return this.getPiles().get(place);
    }

    public List<Stack<Agent>> getPiles() {
        return this.piles;
    }

    public int getPlace(Agent agent) {
        for (int i = 0; i < nbPiles; i++) {
            if (getPile(i).contains(agent)) {
                return i;
            }
        }
        return -100;
    }


    public boolean getIsFree(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        return pile.lastElement() == agent;
    }


    public void push(Agent agent) {
        if (getNextAgent(agent) != null)
            getNextAgent(agent).setPush(true);
    }

    public void printEnvironment() {
        for (int i = 2; i >= 0; i--) {
            Stack<Agent> pile = piles.get(i);
            String string = "";
            for (Agent agent : pile) {
                string += agent.getName() + " ";
            }
            if (string != "") {
                System.out.println(string);
            } else {
                System.out.println("[     ]");
            }

        }
    }

    public void addAgent(Agent agent, int indexPile) {
        this.getPile(indexPile).push(agent);
    }
}
