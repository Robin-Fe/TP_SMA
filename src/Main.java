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
        environnement.addAgent(A, 0);
        environnement.addAgent(D, 0);
        environnement.addAgent(B, 0);
        environnement.addAgent(C, 0);
        List<Agent> agents = Arrays.asList(A, B, C, D);
        while (!(A.getGoalAchieved() && B.getGoalAchieved() && C.getGoalAchieved() && D.getGoalAchieved())) {
            System.out.println("NOUVEAU TOUR \n");
            environnement.printEnvironment();
            // ToDo : choose agent
            int r = new Random().nextInt(agents.size());
            Agent agentChoisi = agents.get(r);
            agentChoisi.perception(environnement);
            agentChoisi.action(environnement);
        }
    }
}
