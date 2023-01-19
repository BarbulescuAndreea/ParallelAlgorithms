import java.io.*;
import java.util.concurrent.RecursiveTask;

public class ProcessProductsWriter extends RecursiveTask<Void> {

    ProductsAndOrderID product;
    private static final File file = new File("order_products_out.txt");
    public static Writer output;

    static {
        try {
            if(output == null){
                output = new BufferedWriter(new FileWriter(file));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ProcessProductsWriter(ProductsAndOrderID product) {
        this.product = product;
    }

    public Void compute() {
        try {
            synchronized(this) { // pun lock pe scriere
                output.write(product.orderID + "," + product.productID + ",shipped" + "\n");
            }
        } catch (Exception ignored) {

        }
        return null;
    }
}
