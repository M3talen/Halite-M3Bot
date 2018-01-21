package GP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GPTreeLoader {

    public String loadGPTree(String path) {

        StringBuilder sb = new StringBuilder();
        try
        {
            FileReader reader = new FileReader(path);
            BufferedReader buff = new BufferedReader(reader);
            while(true) {
                String inputText = buff.readLine();
                if(inputText == null)
                    break;
                if(inputText.startsWith("#")) continue;
                sb.append(' ').append(inputText);
            }
        }
        catch (IOException ex)
        {
            System.out.println("Can't read file.");
        }
        return sb.toString();

    }
}
