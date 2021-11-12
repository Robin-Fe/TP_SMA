import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartAgentPickingPolitique implements Politique{
    public Object getChoice(List<Object> choices) {
        ArrayList<Agent> possibleChoices = new ArrayList<>();
        for (Agent agent : ((ArrayList<Agent>) choices.get(0))) {
            if (agent.getGoalAchieved() && !agent.getPushed()) {
                continue;
            }
            if (agent.getPushed() && agent.getFree() && new Random().nextInt(100)<70) {
                return agent;
            }
            if (agent.getPushed() || !agent.getGoalAchieved()) {
                possibleChoices.add(agent);
            }
        }
        if (new Random().nextInt(100) > 50) {
            return possibleChoices.get(0);
        }
        return possibleChoices.get(new Random().nextInt(possibleChoices.size()));
    }
}
