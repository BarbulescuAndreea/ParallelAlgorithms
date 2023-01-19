import java.io.*;
public class OrderProductsReader {
    static class ProductWithOffset {
        private final String productID;
        private final int offset;

        public ProductWithOffset(String productID, int offset) {
            this.productID = productID;
            this.offset = offset;
        }

        public String getProductID() {
            return productID;
        }

        public int getOffset() {
            return offset;
        }
    }

    private final File productsFile;

    public OrderProductsReader(File productsFile) {
        this.productsFile = productsFile;
    }

    /**
     *
     * @throws IOException
     * Am un numar de comanda, citesc liniile si vad pe a cata linie am gasit un produs din comanda, cand il gasesc,
     * ii returnez id-ul cu tot cu offset(numarul liniei la care l am gasit).
     */
    public ProductWithOffset productForOrderID(String orderID, long offset) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(productsFile));
        // sunt in fisierul de produse, cat timp mai am linii verific daca i este mai mic decat ofset, atunci trebuie
        // sa continui, daca este egal cu offset inseamna ca verific daca produsul de pe linia aia are id-ul comenzii
        // curente, daca da, atunci il intorc impreuna cu i care reprezinta noul offset.
        String line = reader.readLine();
        int i = 0;
        while (line != null) {
            if (i <= offset) {
                i++;
                line = reader.readLine();
                continue; // am verificat deja cele dinainte de offset, pornesc cu verificarea de la offset
            }
            String[] components = line.split(",");
            if (components[0].equals(orderID)) { // cand gasesc un produs care are id ul comenzii curente, il intorc
                return new ProductWithOffset(components[1], i);
            }
            i++;
            line = reader.readLine();
        }
        return null;
    }
}
