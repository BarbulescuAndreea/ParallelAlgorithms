For the first homework:

Way of thinking: I use a synchronized queue in which I initially put the paths to the files to be processed by the Mappers, from which I keep removing and giving the Mappers one file at a time, then as they perform the operations and create the partial lists, they are added back into a the queue from where they are taken by Reductions that unify the perfect powers for each exponent, count them and at the end write the final result in the output files.

Functions:

"computePowerList" - receives the name of the file containing the numbers to be checked, I check if a number in the file is perfect power, checking if there is a list containing all the numbers for a certain power. if there is, I add it in powerMap to the corresponding power. ^2 -> [....] ^3 -> [...] ... ^R+1 -> [...] Thus, I will have the partial lists formed for each number read from the file.

"mappingFunc" - I process the argument, I take from the synchronized queue, I take a path to the files to be processed, I create the partial lists of perfect powers for each data file, and then I put them in the result queue where they will be taken by the Reductions to be processed and modified accordingly.

"reduceFunc" - I put a lock so that the reducer threads do not start before the mapper threads produce the results, otherwise the reducer threads would have nothing to process and would finish instantly. I write in the out file the number of perfect powers with a unique exponent.

"SynchronizedQueue" - I used a queue already implemented from c++, but I made a synchronized one using mutex. It is synchronized because there could be a situation where 2 threads want to extract the same file to be processed or push at the same time, which would lead to overlaps and possible problems.

"MappingArgumentWrapper" - the structure created by me, because I needed a single argument to access the number of Reductions, the synchronized queue, the map of (power, the list of corresponding perfect powers), and the final queue of perfect powers results.

The method for calculating the perfect powers: for optimization, I calculated from the beginning all the perfect powers for each power up to the number of reductions, because this calculation is performed only once at the beginning, but not every time, then for each number in each file test I just checked if it is in the set of perfect powers. I also tried another method using Erastosthene's test, but it turned out not to be as efficient due to the duration of the tests, so I stayed with the first method described.

For the second homework:

Implementation idea:
We started from the Fork-Join Pool model. I create a ForkJoinPool that has the maximum size of threads equal to the number of threads
given on call(P). I create (invoke) a Read task (thread), which in turn (in the ReadTask class) will create several threads for me, a few more Read for
reading from the orders.txt file, and other process tasks that will see the order and process it - each process task that sees the products
from an order, it creates new sub-threads of Process that deal with writing in parallel in the out file "orderID, productID, shipped",
then when an order has all the products written with shipped by the new process threads, the big process thread that subcreated them for
corresponding order, he is the one who will write in the file "orders_out.txt" shipped for the whole order (as it is mentioned that it must happen
in the last mentioned point of the theme in the "WorkFlow" section.

To follow OOP principles, I have several classes, each one dealing with a specific thing:

FolderReader - the class that takes my folders with the input files, and takes the input files from the folder into 2 variables.

OrdersReader - the class that reads my input file "orders.txt" line by line, and gives me an input line in [orderID, nrProduse],
because at first it is received as a String.

OrderEntry - the class used only to keep the pair of orderID, nrProducts from the respective order.

OrderProductsReader - the class that reads my orders_products.txt file and searches for a product for a specific order, then when searching
                      the next product in the order starts from the position where the previous product was found.

ProcessProductsWriter - the class that extends recursiveTask, and which contains the Compute method that will be called by the sub-threads
                         created by process threads (when called with fork) to write to the file
                         of output "orderID, productID, shipped".
ProcessTask - the class that contains the Compute method called by the initial process threads that were created by the first Read thread.
              In it I look for all the products from a specific order, then I give each product in part of an order in turn
              another sub-thread that will call the compute function from ProcessProductsWriter explained above to write to the file
              of out. At the end, the current ProcessTask (thread) writes "shipped" for the entire order in the "orders_out.txt" file.

ProductsAndOrderID - the helper class that includes my orderID, productID fields.

ReadTask - the class used to create all Read and Process threads, starting from a single Read thread given in Main (Tema2)
             in invoke => used to fork and join all these threads.

Topic 2 - main class, necessary initializations.
