package scheadpp.core.Common.Helpers;

import scheadpp.core.SchedCoreApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenFile {

    private static List<String> tokens = new ArrayList<>();

    private static String tokenFileName = "/data/tokens.txt";

    @Value("${schedapp.tokens.serviceKey}")
    private String serviceToken;

    static {
        try {

            if(!Files.exists(Path.of(tokenFileName))) {
                Files.createFile(Path.of(tokenFileName));
            }

            FileReader fr = new FileReader(tokenFileName);
            Scanner scan  = new Scanner(fr);

            while (scan.hasNextLine()) {
                tokens.add(scan.nextLine());
            }

        } catch (IOException e) {
            SchedCoreApplication.getLogger().warn("failed to read or write tokens file; working in memory-only mode");
        }
    }

    public String getToken() {
        if(tokens.isEmpty()) return serviceToken;
        Integer index = new Random().nextInt(tokens.size());
        return tokens.get(index);
    }

    public String invalidateToken(String token) {
        tokens.removeIf( x-> x.equals(token));

        String collectedTokens = tokens.stream().collect(Collectors.joining("\n"));

        try {
            FileWriter fw = new FileWriter(tokenFileName, false);
            fw.write(collectedTokens);
        } catch (IOException e) {
            SchedCoreApplication.getLogger().warn("failed to write tokens file");
        }

        return getToken();
    }

    public void addToken(String token) {
        tokens.add(token);
        try {
            FileWriter fw = new FileWriter(tokenFileName, true);
            fw.write(token+"\n");
        } catch (IOException e) {
            SchedCoreApplication.getLogger().warn("failed to write tokens file; working in memory-only mode");
        }
    }
}
