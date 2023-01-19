import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ReadTask extends RecursiveTask<Void> {
    private final OrdersReader ordersReader;
    private final OrderProductsReader orderProductsReader;
    private final boolean isInitial;
    private final int generateCount;
    public ReadTask(OrdersReader ordersReader, OrderProductsReader orderProductsReader, boolean isInitial, int generateCount) {
        this.ordersReader = ordersReader;
        this.isInitial = isInitial;
        this.generateCount = generateCount;
        this.orderProductsReader = orderProductsReader;
    }

    @Override
    protected Void compute() {
        List<ReadTask> readTasks = null;
        if (isInitial) {
            readTasks = new ArrayList<>();
            for (int i = 0; i < generateCount; ++i) {
                ReadTask readTask = new ReadTask(ordersReader, orderProductsReader, false, 0);
                readTask.fork();
                readTasks.add(readTask);
            }
        }
        List<ProcessTask> processTasks = new ArrayList<>();

        try {
            while (true) {
                OrderEntry entry = ordersReader.readLine();
                    //System.out.println("a ");
                    ProcessTask processTask = new ProcessTask(entry, orderProductsReader);
                    processTask.fork();
                    processTasks.add(processTask);
            }
        } catch (EndOfFileException ignored) {
        } finally {
            for (ProcessTask processTask : processTasks) {
                processTask.join();
            }

            assert readTasks != null;
            if(isInitial) {
                for (ReadTask readTask : readTasks) {
                    readTask.join();
                }
            }

        }
        return null;
    }
}
