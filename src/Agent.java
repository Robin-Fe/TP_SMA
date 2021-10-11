public class Agent {
    public String name;
    public Agent goal;
    public Boolean goalAchieved;
    public Agent underAgent;
    public Boolean isPushed;
    public Boolean isFree;

    public Agent(String name, Agent goal){
        this.name = name;
        this.goal = goal;
        this.goalAchieved = false;
    }

    public void perception(Environnement environment){
        this.underAgent = environment.getNextAgent(this);
        this.isFree = environment.getIsFree(this);
        this.isPushed = environment.getIsPushed(this);
        this.goalAchieved = underAgent == goal || goal == null;
    }

    public void action(Environnement environment){
        if (!isFree && isPushed){
            environment.pousser(this);
        }
        else if (isFree && !goalAchieved || isPushed && isFree){
            // ToDo : choose index on the table
            int index = 0;
            environment.seDeplacer(this, index);
            this.isPushed = false;
        }
        else if (!isFree && ! goalAchieved){
            environment.pousser(this);
        }
    }

}
