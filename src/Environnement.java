import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Environnement {

    private List<Stack<Agent>> piles;
    private int nbPiles;

    public Environnement(int nbPiles) {
        this.nbPiles = nbPiles;
        this.piles = new ArrayList<>(3);
        for (int i = 0; i < nbPiles; i++) {
            Stack<Agent> pile = new Stack<>();
            this.piles.add(pile);
        }
    }


    public void seDeplacer(Agent agent, int indexPileArrivee) {
        Stack<Agent> pileArrivee = getPile(indexPileArrivee);
        Stack<Agent> pileDepart = getPile(getPlace(agent));
        Agent agentPop = pileDepart.pop();
        pileArrivee.push(agentPop);
    }

    public Agent getNextAgent(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        if (pile.size() > pile.search(agent) + 1){
            return pile.elementAt(pile.search(agent) + 1);
        }
        return null;
    }

    public Agent getPreviousAgent(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        return pile.elementAt(pile.search(agent) - 1);
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
        System.out.println("test");
        System.out.println(getPile(getPlace(agent)).firstElement().getName());
        System.out.println(agent.getName());
        System.out.println(pile.firstElement() == agent);

        return pile.firstElement() == agent;
    }


    public void push(Agent agent) {
        getPreviousAgent(agent).setPush(true);
    }

    public void printEnvironment() {
        for (int i = 0; i < piles.size(); i++) {
            Stack<Agent> pile = piles.get(i);
            System.out.println("\nPile " + i + "\n");
            for (Agent agent : pile) {
                System.out.println("Agent " + agent.getName() + "\n");
            }

        }
    }

    public void addAgent(Agent agent, int indexPile) {
        this.getPile(indexPile).push(agent);
    }
}
