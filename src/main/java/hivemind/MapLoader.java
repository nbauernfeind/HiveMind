package hivemind;

import java.io.*;

public class MapLoader implements Serializable {
    public static TileType[][] loadMap(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            int rows = -1, cols = -1;
            TileType[][] array = null;

            int r = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("m ")) {
                    line = line.substring(2);
                    char[] chars = line.toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        char c = chars[i];
                        if (c == '%') {
                            array[r][i] = TileType.WATER;
                        } else {
                            array[r][i] = TileType.GROUND;
                        }
                    }
                    r++;
                } else if (line.startsWith("rows ")) {
                    rows = Integer.parseInt(line.substring(5));
                } else if (line.startsWith("cols ")) {
                    cols = Integer.parseInt(line.substring(5));
                } else if (line.startsWith("players ")) {
                    if (rows > 0 && cols > 0) {
                        array = new TileType[rows][cols];
                    } else {
                        System.out.println("Invalid map file");
                        break;
                    }
                }
            }
            return array;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}