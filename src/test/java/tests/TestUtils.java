package tests;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TestUtils {
    public static final String startingLine = "In order to get the list of commands, type \"help\"\r\n\n";

    public static String outputStreamToReadable(ByteArrayOutputStream stream, int inputLength){
        byte[] outputMinusInput = Arrays.copyOfRange(stream.toByteArray(), inputLength + startingLine.length(), stream.size());
        String output = new String(outputMinusInput, StandardCharsets.UTF_8);
        return output.replace("\r\n", "\n").replace("\r", "").replace("\u001B[?2004h", "").replace("\u001B[?2004l", "");
    }
}
