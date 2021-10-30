public class Main {

    public static void main(String[] args) {

        //POLITIQUES
        Politique randomPolitique = new RandomPolitique();
        Politique smartAgentPickingPolitique = new SmartAgentPickingPolitique();
        Politique smartDestinationPolitique = new SmartDestinationPolitique(65, 50);

        //SCORES
        int score1 = 0;
        int score2 = 0;
        int score3 = 0;
        int score4 = 0;
        int nbSimulations = 60;

        //PARAMETRES
        int percentMin1 = 69;
        int percentMax1 = 70;
        int percentMin2 = 50;
        int percentMax2 = 53;

        for (int j = 0; j < 30; j++) {
            score1 = 0;
            score2 = 0;
            score3 = 0;
            score4 = 0;
            for (int i = 0; i < nbSimulations; i++) {
                Simulation simulation1 = new Simulation(false, false, 15, randomPolitique, randomPolitique, new SmartDestinationPolitique(percentMin1, percentMin2));
                Simulation simulation2 = new Simulation(false, false, 15, randomPolitique, randomPolitique, new SmartDestinationPolitique(percentMax1, percentMin2));
                Simulation simulation3 = new Simulation(false, false, 15, randomPolitique, randomPolitique, new SmartDestinationPolitique(percentMin1, percentMax2));
                Simulation simulation4 = new Simulation(false, false, 15, randomPolitique, randomPolitique, new SmartDestinationPolitique(percentMax1, percentMax2));
                score1 += simulation1.runSimulation();
                score2 += simulation2.runSimulation();
                score3 += simulation3.runSimulation();
                score4 += simulation4.runSimulation();
            }
            //System.out.println("Pour " + nbSimulations + " simulations, score total : " + score);
            if (score1 > score2 && score1 > score3 && score1 > score4) {
                percentMax1 -= 3;
                percentMax2 -= 3;
            }
            if (score2 > score1 && score2 > score3 && score2 > score4) {
                percentMin1 += 3;
                percentMax2 -= 3;
            }
            if (score3 > score1 && score3 > score2 && score3 > score4) {
                percentMax1 -= 3;
                percentMin2 += 3;
            }
            if (score4 > score1 && score4 > score2 && score4 > score3) {
                percentMin1 += 3;
                percentMin2 += 3;
            }
            if (percentMin1>=percentMax1) {
                percentMin1 -= 2;
                percentMax1 += 2;
            }
            if (percentMin2>=percentMax2) {
                percentMin2 -= 2;
                percentMax2 += 2;
            }
            System.out.println("percentMin1 " + percentMin1 + "   percentMax1 " + percentMax1 + "   percentMin2 " + percentMin2 + "   percentMax2 " + percentMax2);
            System.out.println("score1 " + score1 + "   score2 " + score2 + "   score3 " + score3 + "   score4 " + score4);
        }
    }
}
