import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Environnement {
    //ToDo : simplifier certaines méthodes
    private final List<Stack<Agent>> piles;
    private final int nbPiles;
    private final Objet table;
    private final int nbAgent;
    public boolean verbose;



    public Environnement(int nbPiles, Objet table, int nbAgent, boolean verbose) {
        this.verbose = verbose;
        this.nbPiles = nbPiles;
        this.nbAgent = nbAgent;
        this.piles = new ArrayList<>(nbPiles);
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
        return pile.get(index + 1);
    }

    public Objet getPreviousAgent(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        int index = pile.indexOf(agent);
        if (index == 0)
            return this.table;
        return pile.get(index - 1);
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
        for (int i = nbPiles-1; i >= 0; i--) {
            Stack<Agent> pile = piles.get(i);
            StringBuilder string = new StringBuilder();
            for (Agent agent : pile) {
                string.append(agent.getName()).append(" ");
            }
            if (!string.toString().equals("")) {
                int nb = 2 * getNbAgents() - string.length();
                string.append(" ".repeat(Math.max(0, nb)));
                System.out.println("[ " + string + "]");
            } else {
                string.append(" ".repeat(Math.max(0, 1 + 2 * getNbAgents())));
                System.out.println("[" + string + "]");
            }
        }
    }

    public void addAgent(Agent agent, int indexPile) {
        this.getPile(indexPile).push(agent);
    }

    public int getNbPiles() {
        return this.nbPiles;
    }

    public int getNbAgents() {
        return this.nbAgent;
    }




    public boolean getIsOneStackFree() {
        boolean isOneStackFree = false;
        for (Stack<Agent> pile : piles) {
            if (pile.size() == 0) {
                isOneStackFree = true;
                break;
            }
        }
        return isOneStackFree;
    }

    public int getStackFree() {
        for (Stack<Agent> pile : piles) {
            if (pile.size() == 0) {
                return piles.indexOf(pile);
            }
        }
        return -1;
    }


    public int getLowestStack(){
        int lowestStackIndex = 0;
        Stack<Agent> lowestStack = getPile(lowestStackIndex);
        for (int index = 0; index < piles.size(); index++) {
            Stack<Agent> stack = piles.get(index);
            if (stack.size() < lowestStack.size()){
                lowestStack = stack;
                lowestStackIndex = index;
            }
        }
        return lowestStackIndex;
    }


}
