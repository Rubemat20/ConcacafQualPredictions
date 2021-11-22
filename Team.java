import java.util.ArrayList;

public class Team {
    private String abb;
    private String name;
    private double wins;
    private double ties;
    private double losses;
    private double points;
    private double gf;
    private double ga;
    private int gamesPlayed;
    public ArrayList<Game> gameList;

    public Team(String abb, String name, ArrayList<Game> gameList) {
        this.abb = abb;
        this.name = name;
        this.gameList = new ArrayList<Game>();
        this.gf = 0;
        this.ga = 0;
        for (Game game : gameList) {
            //System.out.println(game.gethTeam());
            this.gameList.add(game);
        }
        //this.gameList = gameList;
        wins = 0;
        ties = 0;
        losses = 0;
        points = 0;
        gamesPlayed = 0;
        updateTeam();
        this.wins = wins;
        this.ties = ties;
        this.losses = losses;
        this.points = wins*3 + ties;
        this.gamesPlayed = gamesPlayed;
        this.ga = ga;
        this.gf = gf;
        //System.out.println(this.name+" " +this.points);
    }
    public String getName() {return name;}
    public String getAbb() {return abb;}
    public double getWins() {return wins;}
    public double getTies() {return ties;}
    public double getLosses() {return losses;}
    public double getPoints() {return points;}
    public double getGF() {return gf;}
    public double getGA() {return ga;}
    public void addWins(double wins) {this.wins += wins;}
    public void addTies(double ties) {this.ties += ties;}
    public void addLosses(double losses) {this.losses += losses;}
    public void addPoints(double points) {this.points += points;}
    public int getGamesPlayed() {return gamesPlayed;}
    public void updateTeam() {
        wins = 0;
        ties = 0;
        losses = 0;
        points = 0;
        gamesPlayed = 0;
        gf = 0;
        ga = 0;
        for (Game game : gameList) {
            if (game.gethScore()==-1) {}
            else if (game.gethTeam().equals(abb)) {
                gamesPlayed++;
                gf += game.gethScore();
                ga += game.getaScore();
                if (game.gethScore()>game.getaScore()) {
                    wins++;
                }
                else if (game.gethScore()==game.getaScore()) {
                    ties++;
                }
                else { losses++;}
            }
            else {
                gamesPlayed ++;
                gf += game.getaScore();
                ga += game.gethScore();
                if (game.gethScore()>game.getaScore()) {
                    losses++;
                }
                else if (game.gethScore()==game.getaScore()) {
                    ties++;
                }
                else { wins++;}
            }
        }
        this.wins = wins;
        this.ties = ties;
        this.losses = losses;
        this.points = wins*3 + ties;
        this.gamesPlayed = gamesPlayed;
        this.ga = ga;
        this.gf = gf;
    }
    public String toString() {
        return "Team " + abb + " " + wins + "-" + ties + "-" + losses;
    }

    public void reset() {
        this.wins = 0;
        this.ties = 0;
        this.losses = 0;
        this.points = 0;
        this.gamesPlayed = 0;
        this.ga = 0;
        this.gf = 0;
        this.gameList.clear();
    }
}

