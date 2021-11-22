


public class Game {
    private String hTeam;
    private String aTeam;
    private double hScore;
    private double aScore;

    public Game(String hTeam, int hScore, String aTeam, int aScore) {
        this.hTeam = hTeam;
        this.hScore = hScore;
        this.aTeam = aTeam;
        this.aScore = aScore;
    }
    public Game(String hTeam, String aTeam) {
        this.hTeam = hTeam;
        this.aTeam = aTeam;
        this.hScore = -1;
        this.aScore = -1;
    }
    public String gethTeam() {return hTeam;}
    public String getaTeam() {return aTeam;}
    public double gethScore() {return hScore;}
    public double getaScore() {return aScore;}
    public void sethTeam(String hTeam) {this.hTeam = hTeam;}
    public void setaTeam(String aTeam) {this.aTeam = aTeam;}
    public void sethScore(double hScore) {this.hScore = hScore;}
    public void setaScore(double aScore) {this.aScore = aScore;}
    public String toString() {
        if (this.gethScore()==-1) {
            return "Home: " + this.hTeam + " -, " + this.aTeam + " -";
        }
        return "Home: " + this.hTeam + " " + this.hScore + ", " + this.aTeam + " " + this.aScore;
    }

    public void reset() {
        this.hTeam = null;
        this.aTeam = null;
        this.hScore = -1;
        this.aScore = -1;
    }
}
