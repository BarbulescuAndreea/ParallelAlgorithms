import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class ProcessTask extends RecursiveTask<Void> {
    private final OrderEntry orderEntry;
    private final OrderProductsReader orderProductsReader;
    public List<ProcessProductsWriter> listwithThreads = new ArrayList<>();

    private static final File file = new File("orders_out.txt");
    public static Writer output;

    public ProcessTask(OrderEntry orderEntry, OrderProductsReader orderProductsReader) {
        this.orderEntry = orderEntry;
        this.orderProductsReader = orderProductsReader;

    }
    Map<String, String> IDsMap = new HashMap<>();
    @Override
    protected Void compute() {
        if(orderEntry.getProductCount() == 0){
            return null;
        }
        try {
            if(output == null){
                output = new BufferedWriter(new FileWriter(file)); //open in append mode
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int prevOffset = -1;
        List<ProductsAndOrderID> products = new ArrayList<>();
        for (int i = 0; i < orderEntry.getProductCount(); ++i) { // pentru numarul de produse dintr o comanda
            // cauti urmatorul produs pt order_id
            OrderProductsReader.ProductWithOffset pwo = null;
            try {
                pwo = orderProductsReader.productForOrderID(orderEntry.getOrderID(), prevOffset);
                IDsMap.put(orderEntry.getOrderID(), pwo.getProductID());

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            prevOffset = pwo.getOffset();
            String productID = pwo.getProductID();

            products.add(new ProductsAndOrderID(orderEntry.getOrderID(), pwo.getProductID())); // adaug produsele dintr-o comanda in lista

        }
        // paralelizare la scriere de "shipped" pt fiecare produs in fis de iesire
        for (ProductsAndOrderID product : products) {
            ProcessProductsWriter procWriteThread= new ProcessProductsWriter(product);
            listwithThreads.add(procWriteThread);
            procWriteThread.fork();
        }
        for(ProcessProductsWriter product : listwithThreads){
            product.join();
        }
        try {
            synchronized (this) {
                output.write(orderEntry.getOrderID() + "," + orderEntry.getProductCount() + ",shipped\n");
                //System.out.println("b ");
               // output.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // la fnal processtask curent scrie shipped pe order mare
        return null;
    }
}
