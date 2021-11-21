import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPolitique implements Politique {
    public Object getChoice(List<Object> choices) {
        return ((ArrayList) choices.get(0)).get(new Random().nextInt(((ArrayList)choices.get(0)).size()));
    }
}
