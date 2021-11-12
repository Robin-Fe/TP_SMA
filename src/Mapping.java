import java.util.Stack;

public class Mapping {
    private Stack<Stack<Agent>> map = new Stack<>(); // Map of the environment;

    public Mapping() {
    }


    public void resetMap() {
        this.map = new Stack<>();
    }

    public Mapping shareAgentPosition(Agent agent, Objet pointer, int nbPiles) {
        Stack<Stack<Agent>> removeStacks = new Stack<>();
        Stack<Stack<Agent>> addStacks = new Stack<>();
        if (!(pointer instanceof Agent)) {
            // Si l'agent doit aller sur la table, il se mappe sur une nouvelle pile
            Stack<Agent> newStack = new Stack<>();
            newStack.add(agent);
            map.add(newStack);
        } else {
            if (this.map.size() == 0) {
                // S'il n'y a pas de piles mappées, l'agent crée une pile et ajoute son obj et lui-même
                Stack<Agent> newStack = new Stack<>();
                newStack.add((Agent) pointer);
                newStack.add(agent);
                this.map.add(newStack);
            } else {
                // S'il y a des pile, il va voir s'il trouve son objectif
                boolean isPointerHere = false;
                for (Stack<Agent> stack : this.map) {
                    if (stack.contains(pointer)) {
                        // Si oui, il va se mapper dessus
                        isPointerHere = true;
                        stack.add(agent);
                    }
                }
                if (!isPointerHere) {
                    // Sinon, il va créer une pile et mapper son objectif et lui-même
                    Stack<Agent> newStack = new Stack<>();
                    newStack.add((Agent) pointer);
                    newStack.add(agent);
                    this.map.add(newStack);
                }
            }
        }
        for (Stack<Agent> firstStack : this.map) {
            if (firstStack.size() != 0) {
                Agent firstAgent = firstStack.get(0);
                for (Stack<Agent> secondStack : this.map) {
                    if (firstStack != secondStack) {
                        if (secondStack.size() != 0) {
                            if (secondStack.get(secondStack.size() - 1) == firstAgent) {
                                for (Agent oneAgent : firstStack) {
                                    if (oneAgent != firstAgent) {
                                        secondStack.add(oneAgent);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Stack<Agent> stack1 : this.map) {
            for (Stack<Agent> stack2 : this.map) {
                if (stack1 != stack2) {
                    if (stack1.containsAll(stack2)) {
                        if (stack2.containsAll(stack1)) {
                            addStacks.add(stack1);
                        }
                        removeStacks.add(stack2);
                    }
                }
            }
        }
        for (Stack<Agent> stack : removeStacks) {
            this.map.remove(stack);
        }
        for (int index = 0; index < addStacks.size(); index += 2) {
            this.map.add(addStacks.get(index));
        }
        while (this.map.size() < nbPiles) {
            this.map.add(new Stack<>());
        }
        return this;
    }

    public void printMap() {
        System.out.println("MAPPING DE LA CARTE :");
        for (int i = map.size() - 1; i >= 0; i--) {
            Stack<Agent> pile = map.get(i);
            StringBuilder string = new StringBuilder();
            for (Agent agent : pile) {
                string.append(agent.getName()).append(" ");
            }
            if (!string.toString().equals("")) {
                int nb = 2 * 5 - string.length();
                string.append(" ".repeat(Math.max(0, nb)));
                System.out.println("[ " + string + "]");
            } else {
                string.append(" ".repeat(Math.max(0, 1 + 2 * 5)));
                System.out.println("[" + string + "]");
            }
        }
    }

    public Stack<Agent> get(int index) {
        return this.map.get(index);
    }


    public boolean isAgentFree(Agent agent) {
        for (Stack<Agent> agents : map) {
            if (agents.contains(agent)) {
                if (agents.lastElement() == agent) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isOneStackFree() {
        boolean isOneStackFree = false;
        for (Stack<Agent> stack : this.map) {
            if (stack.size() == 0) {
                isOneStackFree = true;
                break;
            }
        }
        return isOneStackFree;
    }

    public int getPlace(Agent agent) {
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).contains(agent)) {
                return i;
            }
        }
        return -1;
    }

    public int getLowestStack(Integer usedStackIndex) {
        int lowestStackIndex = 0;
        if (usedStackIndex == 0) {
            lowestStackIndex = 1;
        }
        Stack<Agent> lowestStack = map.get(lowestStackIndex);
        for (int index = 0; index < map.size(); index++) {
            Stack<Agent> stack = map.get(index);
            if (stack.size() < lowestStack.size() && index != usedStackIndex) {
                lowestStack = stack;
                lowestStackIndex = index;
            }
        }
        return lowestStackIndex;
    }


}
