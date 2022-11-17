#ifndef Queue_hpp
#define Queue_hpp

#include <iostream>
#include <stdio.h>
#include <queue>
#include <pthread.h>

template <typename T>
class SynchronizedQueue {
private:
    std::queue<T*> queue;
    pthread_mutex_t mutex;
public:
    SynchronizedQueue() {
        pthread_mutex_init(&mutex, NULL);
    }
    
    ~SynchronizedQueue() {
        pthread_mutex_destroy(&mutex);
    }
    
    T *pop() {
        pthread_mutex_lock(&mutex);
        
        T* result = queue.empty() ? nullptr : queue.front();
        if (! queue.empty()) {
            queue.pop();
        }
        
        pthread_mutex_unlock(&mutex);
        
        return result;
    };
    
    void push(T *elem) {
        pthread_mutex_lock(&mutex);
        queue.push(elem);
        pthread_mutex_unlock(&mutex);
    };
    
    bool isEmpty() {
        return queue.empty();
    };
};

#endif
