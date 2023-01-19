import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class FolderReader {
    static class Reading {
        private final File ordersFile;
        private final File productsFile;

        public Reading(File ordersFile, File productsFile) {
            this.ordersFile = ordersFile;
            this.productsFile = productsFile;
        }

        public File getOrdersFile() {
            return ordersFile;
        }

        public File getProductsFile() {
            return productsFile;
        }
    }

    private final ArrayList<String> fileNames = new ArrayList<>();

    private long countLinesForFile(final File file) {
        try {
            return Files.lines(Paths.get(file.getAbsolutePath())).count();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return 0;
        }
    }

    /**
     *
     * @return lista formata din fisierele orders.txt si order_products.txt
     */
    public Reading getOrders(final File folder){
        for(File fileEntry : folder.listFiles()){
            fileNames.add(fileEntry.getAbsolutePath());
        }

        File productsFile = new File(fileNames.get(0));
        File ordersFile = new File(fileNames.get(1));

        long ordersFileSize = countLinesForFile(ordersFile);
        return new Reading(ordersFile, productsFile);
    }
}
