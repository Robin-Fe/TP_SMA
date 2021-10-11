import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Environnement environnement = new Environnement(3);
        Agent A = new Agent("A", null);
        Agent B = new Agent("B", A);
        Agent C = new Agent("C", B);
        Agent D = new Agent("D", C);
        List<Agent> agents = Arrays.asList(A, B, C, D);
        while (!(A.goalAchieved && B.goalAchieved && C.goalAchieved && D.goalAchieved)) {
            Agent agentChoisi = agents.get(new Random().nextInt(agents.size()));
            agentChoisi.perception(environnement);
            agentChoisi.action(environnement);
        }
    }
}
