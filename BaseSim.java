import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;


public class BaseSim {
    public static List<Game> main(String fileName) throws IOException {
        List<Game> games = readEventsFromCSV(fileName);
        return games;
    }
    private static List<Game> readEventsFromCSV(String fileName) throws IOException {
        List<Game> games = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] attributes = line.split(",");
            Game game = createGame(attributes);
            games.add(game);
        }
        return games;
    }
    private static Game createGame(String[] metadata) {
        String hTeam = metadata[0];
        String aTeam = metadata[2];
        if (metadata[1].equals("")){
            return new Game(hTeam, aTeam);
        }
        int hScore = Integer.parseInt(metadata[1]);
        int aScore = Integer.parseInt(metadata[3]);
        return new Game(hTeam, hScore, aTeam, aScore);
    }
}
