import java.util.List;

public abstract class Strategy {
    public void beforePerception(List<Agent> agents, Environment environment, int nbPiles) {}

    public abstract void perception(Agent agent, Environment environment);

    public abstract void action(Agent agent, Environment environment);

    public abstract Agent getActionAgent(List<Agent> agents, Environment environment);
}
