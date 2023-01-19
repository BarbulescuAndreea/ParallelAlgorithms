#ifndef ReducerArgumentWrapper_hpp
#define ReducerArgumentWrapper_hpp
#include <queue>
#include <vector>
#include <pthread.h>

struct ReducerArgumentWrapper {
    // coada de liste partiale
public:
    std::queue<std::vector<int> > reducerQueue;
    pthread_mutex_t mutex;
    int power;
    
    ReducerArgumentWrapper(int);
    ~ReducerArgumentWrapper();
};
/*
    ex: 
 Th1 => Coada de liste partiale
     => mutex care blocheaza threadul la inceput si ii da drumul ca mapperele sunt gata
 */

#endif
