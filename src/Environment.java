import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Environment {
    private final List<Stack<Agent>> piles;
    private final Objet table;
    private final int nbAgents;
    private int nbDeplacements = 0;
    public boolean verbose;

    public Objet getTable(){
        return this.table;
    }

    public Environment(int nbPiles, Objet table, int nbAgents, boolean verbose) {
        this.verbose = verbose;
        this.nbAgents = nbAgents;
        this.piles = new ArrayList<>(nbPiles);
        for (int i = 0; i < nbPiles; i++) {
            Stack<Agent> pile = new Stack<>();
            this.piles.add(pile);
        }
        this.table = table;
    }


    public void seDeplacer(Agent agent, int indexPileArrivee) {
        if (indexPileArrivee != getPlace(agent)) {
            agent.setPushed(false);
        }
        Stack<Agent> pileArrivee = getPile(indexPileArrivee);
        Stack<Agent> pileDepart = getPile(getPlace(agent));
        Agent agentPop = pileDepart.pop();
        pileArrivee.push(agentPop);
        this.nbDeplacements++;
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
        return this.piles.get(place);
    }

    public List<Stack<Agent>> getPiles() {
        return this.piles;
    }

    public int getPlace(Agent agent) {
        for (int i = 0; i < piles.size(); i++) {
            if (getPile(i).contains(agent)) {
                return i;
            }
        }
        return -1;
    }


    public boolean getIsFree(Agent agent) {
        Stack<Agent> pile = getPile(getPlace(agent));
        return pile.lastElement() == agent;
    }


    public void push(Agent agent) {
        if (getNextAgent(agent) != null)
            getNextAgent(agent).setPushed(true);
    }

    public void printEnvironment() {
        for (int i = piles.size()-1; i >= 0; i--) {
            Stack<Agent> pile = piles.get(i);
            StringBuilder string = new StringBuilder();
            for (Agent agent : pile) {
                string.append(agent.getName()).append(" ");
            }
            if (!string.toString().equals("")) {
                int nb = 2 * getnbAgentss() - string.length();
                string.append(" ".repeat(Math.max(0, nb)));
                System.out.println("[ " + string + "]");
            } else {
                string.append(" ".repeat(Math.max(0, 1 + 2 * getnbAgentss())));
                System.out.println("[" + string + "]");
            }
        }
    }

    public void addAgent(Agent agent, int indexPile) {
        this.getPile(indexPile).push(agent);
    }

    public int getNbPiles() {
        return this.piles.size();
    }

    public int getnbAgentss() {
        return this.nbAgents;
    }




    public boolean isOneStackFree() {
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

    public int getnbDeplacements() {
        return nbDeplacements;
    }
}
