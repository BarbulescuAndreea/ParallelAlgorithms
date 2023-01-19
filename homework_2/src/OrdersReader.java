import java.io.*;

public class OrdersReader {
    private final BufferedReader reader;

    OrdersReader(File ordersFile) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(ordersFile));
    }

     public OrderEntry readLine() throws EndOfFileException {
        try {
            String line;
            synchronized(this) {
                line = reader.readLine();
            }

            if (line == null) {
                throw new EndOfFileException();
            }

            String[] splitResult = line.split(","); // "o_z1educ4,6" => [o_z1educ4, 6]
            return new OrderEntry(splitResult[0], Integer.parseInt(splitResult[1]));
        } catch (IOException e) {
            throw new EndOfFileException();
        }
    }
}
