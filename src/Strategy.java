import java.util.List;

public interface Strategy {
    void beforePerception(List<Agent> agents, Environment environment);

    Agent getActionAgent(List<Agent> agents, Environment environment);

    void perception(Agent agent, Environment environment);

    void action(Agent agent, Environment environment);

}
