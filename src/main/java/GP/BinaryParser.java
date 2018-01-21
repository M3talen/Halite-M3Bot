package GP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinaryParser {

    public static String Parse(String pattern, String data, BinaryOperation operation) {
        Pattern tPattern = Pattern.compile(pattern);
        Matcher Matcher = tPattern.matcher(data);
        while (Matcher.find()) {
            double var1 = Double.parseDouble(Matcher.group(1));
            double var2 = Double.parseDouble(Matcher.group(2));
            // System.out.println("group 1: " + Matcher.group(1));
            // System.out.println("group 2: " + Matcher.group(2));
            data = data.replaceFirst(pattern, operation.run(var1, var2) + "");
             //System.out.println("data = " + data);
        }
        return data;
    }
}
