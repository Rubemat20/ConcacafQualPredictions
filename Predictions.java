import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Predictions {
    public static List<Game> games;

    static {
        try {
            games = BaseSim.main("ConcacafQualifiers.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Team> teams;
    public static void main(String[] args) {
        setUp();
        Scanner scan = new Scanner(System.in);
        String input = "";
        while (!input.equals("quit")) {
            System.out.println("Enter prompt");
            //System.out.println(teams.get(0).getName());
            //System.out.println(teams.get(0).getWins());
            input = scan.nextLine();
            if (input.equalsIgnoreCase("mc")) {
                fullMonteCarlo(10000);
            }
            if (input.equalsIgnoreCase("standings")) {
                standings();
            }
            if (input.equalsIgnoreCase("help")) {
                System.out.println("standings, p1, predict + 'x', past/future/home + 'team'");
            }
            if (input.equalsIgnoreCase("p1")) { nextOpenGame();}
            if (input.indexOf(" ") != -1) {
                String command = input.substring(0,input.indexOf(" "));
                String teamInput = input.substring(input.indexOf(" ")+1);
                Team chosen = null;
                chosen = getTeam(teamInput);
                if (command.equalsIgnoreCase("predict")) {
                    if (chosen==null) {
                        if (teamInput.length()==1) {
                            int weeks = Integer.parseInt(teamInput);
                            predictWeeks(weeks);
                        }
                        else if (teamInput.equalsIgnoreCase("all")) {
                            /*for (Team team: teams) { //Biases beginning teams bc points go up earlier
                                predictTeam(team);
                            }*/
                            int i = 0;
                            while (i==0) {
                                i =nextOpenGame();
                            }
                            standings();
                        }
                    }
                    else { predictTeam(chosen);}
                }
                if (command.equalsIgnoreCase("past")) {
                    gameListPast(chosen);
                }
                if (command.equalsIgnoreCase("stats")) {
                    stats(chosen);
                }
                if (command.equalsIgnoreCase("future")) {
                    gameListFut(chosen);
                }
                if (command.equalsIgnoreCase("home")) {
                    gameListH(chosen);
                }
            }
        }
    }

    private static void setUp() {
        try {
            games = BaseSim.main("ConcacafQualifiers.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(
                "United States", "Mexico", "Canada", "El Salvador",
                "Costa Rica", "Honduras", "Jamaica", "Panama"));
        ArrayList<String> abbs = new ArrayList<String>(Arrays.asList(
                "USA", "MEX", "CAN", "SLV", "CRC", "HON", "JAM", "PAN"));
        ArrayList<Game> tGames = new ArrayList<Game>();
        teams = new ArrayList<Team>();
        games.get(0).sethTeam("CAN");
        for (int a = 0; a < 8; a++) {
            tGames.clear();
            String abb = abbs.get(a);
            for (Game game : games) {
                if ((game.getaTeam().equals(abb)) || (game.gethTeam().equals(abb))) {
                    //System.out.println(game.getaTeam()+game.gethTeam());
                    tGames.add(game);
                    //System.out.println(game.gethTeam());
                }
            }
            //System.out.println(names.get(a));
            teams.add(new Team(abbs.get(a), names.get(a), tGames));
            //gameList(teams.get(a));
        }
    }

    private static void fullMonteCarlo(int i) {
        int teamRankings[][] = new int[8][9];
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(
                "United States", "Mexico", "Canada", "El Salvador",
                "Costa Rica", "Honduras", "Jamaica", "Panama"));
        //Team iterationRanks[] = new Team[8];
        for (int j = 0; j<i; j++) {
            resetAll();
            setUp();
            int a = 0;
            while (a==0) {
                a =nextOpenGame();
            }
            ArrayList<Team> ptOrder = standings();
            int place = 1;
            for (Team team : ptOrder) {
                //System.out.println(team.getName());
                teamRankings[rankGetName(team.getName())][place] +=1;
                place ++;
            }
        }
        for (int t = 0; t<8; t++) {
            System.out.println(names.get(t));
            for (int p = 1; p<9; p++) {
                System.out.print(" " + p + ": " + teamRankings[t][p]);
            }
            System.out.println();
        }
        percents(names, teamRankings, i);
    }

    private static void percents(ArrayList<String> names, int[][] teamRankings, int i) {
        for (int t = 0; t<8; t++) {
            System.out.println(names.get(t));
            System.out.println("1\t" + "2\t" + "3\t" + "4\t" + "5\t" + "6\t"+"7\t" + "8\t");
            for (int p = 1; p<9; p++) {
                double chance = (double)((int) (1000 * (double)teamRankings[t][p]/(double)i))/10.0;
                System.out.print(chance + "%\t");
            }
            System.out.println();
        }
    }

    private static int rankGetName(String name) {
        switch (name) {
            case "United States":
                return 0;
            case "Mexico":
                return 1;
            case "Canada":
                return 2;
            case "El Salvador":
                return 3;
            case "Costa Rica":
                return 4;
            case "Honduras":
                return 5;
            case "Jamaica":
                return 6;
            case "Panama":
                return 7;
        }
        return 0;
    }

    private static void resetAll() {
        for (Game game : games) {
            game.reset();
        }
        for (Team team : teams) {
            team.reset();
        }
    }

    private static void stats(Team chosen) {
        chosen.updateTeam();
        System.out.println("Goals For: " + chosen.getGF());
        System.out.println("Goals Against: " + chosen.getGA());
        System.out.println("Wins " + chosen.getWins());
        System.out.println("Ties: " + chosen.getTies());
        System.out.println("Losses: " + chosen.getLosses());
        System.out.println("Abb " + chosen.toString());
        for (Game game : chosen.gameList) {
            System.out.println(game.toString());
        }
    }

    private static int nextOpenGame() {
        int a = 0;
        Game curr = games.get(a);
        while (curr.gethScore()!= -1) {
            if (a>= games.size()) {
                System.out.println("No games remaining.");
                return 1;
            }
            curr = games.get(a);
            a++;
        }
        predictGame(curr);
        return 0;
    }

    private static void predictGame(Game curr) {
       /* int a = 0;
        Game curr = games.get(a);
        while (curr.gethScore()!= -1) {
            //System.out.println("?");
            curr = games.get(a);
            a++;
        }*/
        Team hTeam = getTeam(curr.gethTeam());
        Team aTeam = getTeam(curr.getaTeam());
        //int adv = 0;
        double hWinPer  = .503;
        double hTiePer = .246;
        /*if (hTeam.getPoints() > aTeam.getPoints()) {
            hWinPer += .1;
            //hTiePer -= .03;
            //adv = 1; //Let's say adv gives +10% wins, -3% ties, -7% losses
        }
        else if (hTeam.getPoints() < aTeam.getPoints()) {
            hWinPer -= .1;
            //hTiePer -= .03;
        }*/
        gameSimul(curr);
        int hWinS = 0;
        int hTieS = 0;
        if (curr.gethScore()>curr.getaScore()) {
            hWinS = 1;
        }
        else if (curr.gethScore()==curr.getaScore()) {
            hTieS = 1;
        }
        hWinPer = hWinPer/2 + hWinS/2;
        hTiePer = hTiePer/2 + hTieS/2;
        //hWinPer += (curr.gethScore() - curr.getaScore()) * .075;//Subjective? Maybe try calibrating your constants based on past results
        //hTiePer += (1 - (curr.gethScore()-curr.getaScore()))*.01;//This hurts/helps home team more
        double hPoints = hWinPer*3 + hTiePer*1;
        double aPoints = (1-hWinPer-hTiePer)*3 + hTiePer*1;
        hTeam.addWins(hWinPer);
        hTeam.addTies(hTiePer);
        hTeam.addLosses((1-hWinPer-hTiePer));
        hTeam.addPoints(hPoints);
        aTeam.addLosses(hWinPer);
        aTeam.addTies(hTiePer);
        aTeam.addWins((1-hWinPer-hTiePer));
        aTeam.addPoints(aPoints);
        //System.out.println(hPoints + " " + aPoints);
        //System.out.println((int)(hWinPer*100) + "% " + (int)(hTiePer*100) + "% " + (int)((1-hWinPer-hTiePer)*100));
    }

    private static void gameSimul(Game curr) {
        Team hTeam = getTeam(curr.gethTeam()); //Home Team
        Team aTeam = getTeam(curr.getaTeam()); //Away Team
        hTeam.updateTeam();
        aTeam.updateTeam();
        double hGF = hTeam.getGF(); //Home Goals For
        //System.out.println("Goals For " + hGF + hTeam.getAbb());
        double hGA = hTeam.getGA(); //Home Goals Against
        double aGF = aTeam.getGF(); //Away Goals For
        double aGA = aTeam.getGA(); //Away Goals Against
        double hGP = hTeam.getGamesPlayed();
        double aGP = aTeam.getGamesPlayed();
        //double hAvgGD = (hGF-hGA)/(double)hTeam.getGamesPlayed();
        //double aAvgGD = (aGF-aGA)/(double)aTeam.getGamesPlayed();
        //Below are the predicted scores of the game, next use Poisson Distribution to
        // generate distribution and chance each side will win
        //What factors can affect a team's predicted goals per game? GF, GA, oppGF, oppGA, home field +- 1?
            //Don't use random +- if you plan on using Monte Carlo.
        //Run "hot" Monte Carlo simulations to create a probabilistic outcome
        double hScore = ((hGF/hGP)*2 + aGA/aGP)/3;// + hAvgGD; //This doesn't work, bc you shouldn't subtract GA
        double aScore = ((aGF/aGP)*2 + hGA/hGP)/3;// + aAvgGD; //Good is just +GD, it works ok
        //double hPoisson[] = poisson(hScore);
        //if (hScore<0) { hScore=0;}
        //if (aScore<0) { aScore=0;}
        //if (hAvgGD>=aAvgGD) {
        curr.sethScore(hScore);
        curr.setaScore(aScore); //Use Euros data in SQL or R to analyze how hGF and aGA relate to goals scored per game
        monteCarlo(curr);
        //System.out.println(curr.toString());
        //}
    }

    private static double[] poisson(double A) {
        //double n = 0;
        double pMatrix[] = new double[6];
        for (int n = 0; n<5; n++) {
            double numerator = Math.pow(A, n);
            double denominator = 1;
            for (int a = n; a>0; a--) {
                denominator *= a;
            }
            double eVal = Math.pow(Math.exp(1), (-A));
            pMatrix[n] = numerator * eVal /denominator;
            //System.out.print(pMatrix[n]);
            //System.out.println(n + " goals: " + pMatrix[n]);
        }
        //System.out.println();
        pMatrix[5] = 1-(pMatrix[4]+pMatrix[3]+pMatrix[2]+pMatrix[1]+pMatrix[0]);
        return pMatrix;
    }

    private static void monteCarlo(Game game) {
        double hPoisson[] = poisson(game.gethScore());
        double aPoisson[] = poisson(game.getaScore());
        double scoreChances[][] = new double[6][6];
        for (int h = 0; h<6; h++) {
            //System.out.println("Goals: " + hPoisson[h]);
            for (int a = 0; a<6; a++) {
                scoreChances[h][a] = hPoisson[h]*aPoisson[a];
                //.print((int)(100*scoreChances[h][a])+ "%\t");
            }
           // System.out.println();//Montecarlo then needs to choose a score at weighted random
            //So close!
        }
        double rand = Math.random();
        //System.out.println(rand);
        int i = 0;
        double perCurr = hPoisson[0];
        while (rand>perCurr) {
            i++;
            perCurr += hPoisson[i]; //This won't work properly//Now it will
        }
        game.sethScore(i);
        //System.out.println(game.gethTeam()+ i);

        rand = Math.random();
        //System.out.println(rand);
        i = 0;
        perCurr = aPoisson[0];
        while (rand>perCurr) {
            i++;
            perCurr += aPoisson[i]; //This won't work properly//Now it will
        }
        game.setaScore(i);
        //System.out.println(game.getaTeam()+i);
    }

    private static Team getTeam(String gethTeam) {
        for (Team team : teams) {
            if ((team.getName().equalsIgnoreCase(gethTeam))||
                    (team.getAbb().equalsIgnoreCase(gethTeam))) {
                return team;
            }
        }
        return null;
    }


    private static void predictTeam(Team chosen) { //40% of games end in ties (Where'd this come from???)
        //double predictedPoints =0;
        for (Game game : chosen.gameList) {
            if (game.gethScore()==-1) {
                predictGame(game);
            }
        }
        /*for (Game game : chosen.gameList) {
            if (game.gethTeam().equals(chosen.getAbb())) {
                predictedPoints += .503*3 + .246*1;
            }
            else {
                predictedPoints += .251*3 + .246*1;
            }
        }*/
        //System.out.println("Predicted total points: " + chosen.getName() + ((double)chosen.getPoints()));
    } //won 50.3 percent of home games, while losing just 25.1 percent

    private static void predictWeeks(int weeks) {
            for (int a = 0; a<4*weeks;a++) {
                nextOpenGame();
                if (a%4==0) { //Need to implement a week counter
                    //System.out.println("Weeks elapsed: " +(a+1));
                    standings();
                }
            }
    }

    public static ArrayList<Team> standings() {
        ArrayList<Team> ptOrder = new ArrayList<Team>();
        for (Team team : teams) {
            if (ptOrder.size()==0) {
                ptOrder.add(team);
            }
            else if (ptOrder.get(0).getPoints()>team.getPoints()) {
                int a = 0;
                while ((a<ptOrder.size())&&(ptOrder.get(a).getPoints()>team.getPoints())) {
                    a++;
                }
                ptOrder.add(a, team);
            }
            else {
                ptOrder.add(0, team);
            }
        }
        for (Team team : ptOrder) {
            int pts = (int)team.getPoints();
            //System.out.println(team.getName() + ": " + pts);
        }
        return ptOrder;
    }
    private static void gameListPast(Team team) {
        for (Game game : team.gameList ) {
            if (game.gethScore()!=-1) {
                System.out.println(game.toString());
            }
        }
    }
    private static void gameListFut(Team team) {
        for (Game game : team.gameList ) {
            if (game.gethScore()==-1) {
                System.out.println(game.toString());
            }
        }
    }
    private static void gameListH(Team team) {
        for (Game game : team.gameList ) {
            if (game.gethTeam().equals(team.getAbb())) {
                System.out.println(game.toString());
            }
        }
    }
}
