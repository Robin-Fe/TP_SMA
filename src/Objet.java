public class Objet {
    private final String name;
    private final Objet goal;
    private Boolean goalAchieved;
    private Boolean isPushed;
    private Boolean isFree;

    public Objet(String name, Objet goal) {
        this.name = name;
        this.goal = goal;
        this.goalAchieved = false;
        this.isPushed = false;
    }

    public Objet getGoal() {
        return goal;
    }

    public Boolean getGoalAchieved() {
        return goalAchieved;
    }

    public Boolean getPushed() {
        return isPushed;
    }

    public Boolean getFree() {
        return isFree;
    }

    public String getName() {
        return name;
    }

    public void setGoalAchieved(Boolean goalAchieved) {
        this.goalAchieved = goalAchieved;
    }

    public void setPushed(Boolean pushed) {
        isPushed = pushed;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

}
