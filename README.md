For the first homework:

Way of thinking: I use a synchronized queue in which I initially put the paths to the files to be processed by the Mappers, from which I keep removing and giving the Mappers one file at a time, then as they perform the operations and create the partial lists, they are added back into a the queue from where they are taken by Reductions that unify the perfect powers for each exponent, count them and at the end write the final result in the output files.

Functions:

"computePowerList" - receives the name of the file containing the numbers to be checked, I check if a number in the file is perfect power, checking if there is a list containing all the numbers for a certain power. if there is, I add it in powerMap to the corresponding power. ^2 -> [....] ^3 -> [...] ... ^R+1 -> [...] Thus, I will have the partial lists formed for each number read from the file.

"mappingFunc" - I process the argument, I take from the synchronized queue, I take a path to the files to be processed, I create the partial lists of perfect powers for each data file, and then I put them in the result queue where they will be taken by the Reductions to be processed and modified accordingly.

"reduceFunc" - I put a lock so that the reducer threads do not start before the mapper threads produce the results, otherwise the reducer threads would have nothing to process and would finish instantly. I write in the out file the number of perfect powers with a unique exponent.

"SynchronizedQueue" - I used a queue already implemented from c++, but I made a synchronized one using mutex. It is synchronized because there could be a situation where 2 threads want to extract the same file to be processed or push at the same time, which would lead to overlaps and possible problems.

"MappingArgumentWrapper" - the structure created by me, because I needed a single argument to access the number of Reductions, the synchronized queue, the map of (power, the list of corresponding perfect powers), and the final queue of perfect powers results.

The method for calculating the perfect powers: for optimization, I calculated from the beginning all the perfect powers for each power up to the number of reductions, because this calculation is performed only once at the beginning, but not every time, then for each number in each file test I just checked if it is in the set of perfect powers. I also tried another method using Erastosthene's test, but it turned out not to be as efficient due to the duration of the tests, so I stayed with the first method described.
