#ifndef MappingArgumentWrapper_hpp
#define MappingArgumentWrapper_hpp
#include "SynchronizedQueue.hpp"
#include <string>
#include <map>
#include <set>

#include <stdio.h>
struct MappingArgumentWrapper {
    SynchronizedQueue<std::string> queue;
    SynchronizedQueue<std::map<int, std::vector<int>*> > powerMapResultsQueue;
    std::map<int, std::set<int>> *allPerfectPowers;
    int R;
    MappingArgumentWrapper(int, std::map<int, std::set<int>> *);
    ~MappingArgumentWrapper();
};

#endif
