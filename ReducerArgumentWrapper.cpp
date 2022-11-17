#include "ReducerArgumentWrapper.hpp"

ReducerArgumentWrapper::ReducerArgumentWrapper(int power): power(power) {
    pthread_mutex_init(&mutex, NULL);
}

ReducerArgumentWrapper::~ReducerArgumentWrapper() {
    pthread_mutex_destroy(&mutex);
}
