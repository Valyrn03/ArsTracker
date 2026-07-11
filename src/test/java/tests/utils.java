package tests;

import application.models.Campaign;
import application.models.Covenant;
import application.models.CovenantFeature;
import application.models.enums.Art;
import application.models.enums.Tribunal;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class utils {
    public static final String startingLine = "In order to get the list of commands, type \"help\"\r\n\n";

    public static String outputStreamToReadable(ByteArrayOutputStream stream, int inputLength){
        byte[] outputMinusInput = Arrays.copyOfRange(stream.toByteArray(), inputLength + startingLine.length(), stream.size());
        String output = new String(outputMinusInput, StandardCharsets.UTF_8);
        return output.replace("\r\n", "\n").replace("\r", "").replace("\u001B[?2004h", "").replace("\u001B[?2004l", "");
    }

    /*
    Method to make a random campaign for the sake of testing
     */
    public static Campaign generateCampaign(){
        UUID id = UUID.randomUUID();
        Random random = new Random();
        return Campaign.createCampaign(id.toString(), random.nextInt(10000));
    }

    public static Covenant generateCovenant(){
        Random random = new Random();
        Map<Art, Integer> vis = new HashMap<>();
        for (Art art : Art.values()){
            vis.put(art, random.nextInt(0, 100));
        }
        return Covenant.buildCovenant(UUID.randomUUID().toString(),
                String.valueOf(Tribunal.values()[random.nextInt(Tribunal.values().length)]),
                random.nextInt(0, 10000),
                vis);
    }

    public static CovenantFeature generateCovenantFeature(){
        Random random = new Random();

        CovenantFeature feature = new CovenantFeature(random.nextInt());
        feature.setName(UUID.randomUUID().toString());

        if(random.nextBoolean()){
            feature.setType(CovenantFeature.FeatureType.HOOK);
        }else{
            feature.setType(CovenantFeature.FeatureType.BOON);
        }

        feature.setMajor(random.nextBoolean());
        feature.setDescription(feature.getName());

        return feature;
    }
}
