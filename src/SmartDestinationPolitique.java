import java.util.List;
import java.util.Random;
import java.util.Stack;

public class SmartDestinationPolitique implements Politique{

    int percent1;
    int percent2;
    public SmartDestinationPolitique(int percent1, int percent2) {
        this.percent1 = percent1;
        this.percent2 = percent2;
    }

    public Object getChoice(List<Object> choices) {
        Stack<Agent> bestPile = new Stack<>();
        List<Stack<Agent>> listePiles = (List<Stack<Agent>>) choices.get(0);
        int smallestSize = 10000;
        for (Stack<Agent> pile : listePiles) {
            if (pile.isEmpty() && new Random().nextInt(100) < percent1) {
                return pile;
            }
            if (pile.size() <= smallestSize) {
                smallestSize = pile.size();
                bestPile = pile;
            }
        }
        new Random().nextInt(100);
        if (new Random().nextInt(100)<percent2) {
            return bestPile;
        }
        return listePiles.get(new Random().nextInt(listePiles.size()));
    }
}
