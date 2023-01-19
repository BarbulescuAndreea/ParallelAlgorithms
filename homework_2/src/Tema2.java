import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class Tema2 {
    //public static int noFolder;
    //  0           1
    //, numeFolder, P
    public static void main(String[] args) throws IOException, InterruptedException {
        // ok
        File input = new File(args[0]); // numele folder-ului de intrare
        // ok
        // input/input_1
        //noFolder = Integer.parseInt(args[0].split("_")[1]);
        int noOfThreads = Integer.parseInt(args[1]); // P-ul
        FolderReader folderReader = new FolderReader();
        FolderReader.Reading files = folderReader.getOrders(input); // am in files cele 2 fisiere de intrare


        OrdersReader ordersReader = new OrdersReader(files.getOrdersFile());
        OrderProductsReader orderProductsReader = new OrderProductsReader(files.getProductsFile());

        double readPctg = 0.3;
        int readThreadsCount = (int) (noOfThreads * readPctg); // x% sunt de citire
        //int processThreadsCount = noOfThreads - readThreadsCount; // restul de procesare

        ForkJoinPool fjp = new ForkJoinPool(noOfThreads); // maximul de thread-uri care intra in pool = noOfThreads = p
        fjp.invoke(new ReadTask(ordersReader, orderProductsReader, true, readThreadsCount - 1)); //asteapta dupa toate
        fjp.shutdown(); // opreste pe toate
        ProcessProductsWriter.output.close();
        ProcessTask.output.close();
    }
}