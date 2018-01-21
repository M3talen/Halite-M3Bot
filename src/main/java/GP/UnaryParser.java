package GP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnaryParser {

    public static String Parse(String pattern, String data, UnaryOperation operation) {
        Pattern tPattern = Pattern.compile(pattern);
        Matcher Matcher = tPattern.matcher(data);
        while (Matcher.find()) {
            double var1 = Double.parseDouble(Matcher.group(1));
            // System.out.println("group 1: " + Matcher.group(1));
            data = data.replaceFirst(pattern, operation.run(var1) + "");
           // System.out.println("data = " + data);
        }
        return data;
    }
}
