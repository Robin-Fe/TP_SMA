import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        //ToDo : Mettre une autre classe pour la table
        Agent table = new Agent("Table", null);
        Environnement environnement = new Environnement(3, table);
        Agent A = new Agent("A", table);
        Agent B = new Agent("B", A);
        Agent C = new Agent("C", B);
        Agent D = new Agent("D", C);
        environnement.addAgent(C, 0);
        environnement.addAgent(D, 0);
        environnement.addAgent(A, 0);
        environnement.addAgent(B, 0);
        List<Agent> agents = Arrays.asList(A, B, C, D);

        while (!(A.getGoalAchieved() && B.getGoalAchieved() && C.getGoalAchieved() && D.getGoalAchieved())) {
            System.out.println("\n");
            environnement.printEnvironment();
            //ToDo : Comment on choisi l'agent ?
            int r = new Random().nextInt(agents.size());
            Agent agentChoisi = agents.get(r);
            agentChoisi.perception(environnement);
            agentChoisi.action(environnement);
        }
        System.out.println("\n");
        environnement.printEnvironment();
    }
}
