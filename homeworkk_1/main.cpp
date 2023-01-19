#include <iostream>
#include <vector>
#include <fstream>
#include <map>
#include <set>
#include <algorithm>

#include <math.h>
#include <pthread.h>

#include "SynchronizedQueue.hpp"
#include "MappingArgumentWrapper.hpp"
#include "ReducerArgumentWrapper.hpp"

#define MAX_INT     2147483647


std::map<int, std::set<int>> *computeAllPerfectPowers(int maxPower) {
    /*
    {
     ^2 => {4, 9, 16...}
     ^3 => {8, 27, ...}
     }
    */
    std::map<int, std::set<int>> *powersMap = new std::map<int, std::set<int>>;
    
    for (int currentPower = 2; currentPower <= maxPower; ++currentPower) {
        std::set<int> allPerfectPowers;
        for (int i = 1; pow(i, currentPower) < MAX_INT; ++i) {
            allPerfectPowers.insert(pow(i, currentPower));
        }
        
        powersMap->insert({ currentPower, allPerfectPowers });
    }
    return powersMap;
}


void computePowerList(std::string path, MappingArgumentWrapper *mappingArgumentWrapper,std::map<int, std::vector<int>*> *powerMap) {
    int R = mappingArgumentWrapper->R;
    auto allPerfectPowers = mappingArgumentWrapper->allPerfectPowers;
    std::ifstream file(path);
    
    int numberCount;
    file >> numberCount; // pun in numberCount prima linie din fisier ce reprez numarul de numere
    
    for (int i = 0; i < numberCount; ++i) { // pt fiecare nr pana la numarul de numere existente in fisier
        int number;
        file >> number;
        
        for (int power = 2; power <= R + 1; ++power) {
            std::set<int>& setForPower = (*allPerfectPowers)[power]; // & (referinte) ca sa nu faca copiere
            const bool isInSet = setForPower.find(number) != setForPower.end();
            // verific daca numarul curent citit din fisier exista in setul corespunzator
            // puterii respective, ex daca 25 exista in setul corespunzator puterii 2(5^2)
            if (! isInSet) {
                continue;
            }
            
            if (powerMap->count(power) == 0)  { // nu contine power ca si cheie
                std::vector<int> *newPowerList = new std::vector<int>;
                powerMap->insert({ power, newPowerList }); // creez lista respectiva asociata acelei puteri
            }
            
            (*powerMap)[power]->push_back(number); // adaug numarul in lista puterii corespunzatoare
        }
    }
}


// argument = adresa catre coada mea
void *mappingFunc(void *argument) {
    MappingArgumentWrapper *mappingArgumentWrapper = (MappingArgumentWrapper *) argument;
    
    
    std::map<int, std::vector<int>*> *powerMap = new std::map<int, std::vector<int>*>; // pt toate fisierele
    while (std::string *fileToProcess = mappingArgumentWrapper->queue.pop()) {
        computePowerList(*fileToProcess, mappingArgumentWrapper, powerMap);
        delete fileToProcess;
    }
    mappingArgumentWrapper->powerMapResultsQueue.push(powerMap);
    
    return nullptr;
}


void *reduceFunc(void *argument) {
    ReducerArgumentWrapper *reducerArgumentWrapper = (ReducerArgumentWrapper *) argument;
    pthread_mutex_lock(&reducerArgumentWrapper->mutex); // aici se blocheaza pana are datele
    
    std::set<int> numbersSet; // am folosit Set pentru a mi pastra doar valorile unice, nu si duplicate
    while (!reducerArgumentWrapper->reducerQueue.empty()) {
        std::vector<int> partialList = reducerArgumentWrapper->reducerQueue.front();
        reducerArgumentWrapper->reducerQueue.pop();
        
        std::copy(
          partialList.begin(),
          partialList.end(),
          std::inserter(numbersSet, numbersSet.end()
        )); //copiaza in numbersSet de la inceput pana la final numerele care sunt puteri perfecte
    }
    
    int size = (int) numbersSet.size(); // le numar si le scriu in fisierul de iesire
    
    std::string fileName = "out" + std::to_string(reducerArgumentWrapper->power) + ".txt";
    
    std::ofstream outFile(fileName);
    outFile << size;
    
    return nullptr;
}


int main(int argc, const char * argv[]) {
    int M = atoi(argv[1]);
    int R = atoi(argv[2]);
    const char *inFile = argv[3];
    
    auto allPerfectPowers = computeAllPerfectPowers(R + 1);
    MappingArgumentWrapper mappingArgumentWrapper(R, allPerfectPowers);
    
    
    std::ifstream file(inFile);
    int numberOfFiles;
    file >> numberOfFiles;
    for (int i = 0; i < numberOfFiles; ++i) {
        std::string *path = new std::string;
        file >> *path;
        mappingArgumentWrapper.queue.push(path);
    }
    
    
    // putere => { mutex, coada }
    std::map<int, ReducerArgumentWrapper* > reducerArgumentWrappers;
    for (int power = 2; power <= R + 1; ++power) {
        ReducerArgumentWrapper* argumentWrapper = new ReducerArgumentWrapper(power);
        reducerArgumentWrappers[power] = argumentWrapper;
        
        
        pthread_mutex_lock(&argumentWrapper->mutex);
    }
    
    // Creeaza vectorii de threaduri
    std::vector<pthread_t *> mappingThreads;
    std::vector<pthread_t *> reduceThreads;
    
    for (int i = 0; i < M; ++i) {
        mappingThreads.push_back(new pthread_t);
    }
    
    for (int i = 0; i < R; ++i) {
        reduceThreads.push_back(new pthread_t);
    }
    
    for (pthread_t *thread: mappingThreads) {
        pthread_create(thread, nullptr, mappingFunc, &mappingArgumentWrapper);
    }
    
    int reduceThreadsSize = (int) reduceThreads.size();
    for (int i = 0; i < reduceThreadsSize; ++i) {
        pthread_create(reduceThreads[i], nullptr, reduceFunc, reducerArgumentWrappers[i + 2]);
    }
    
    
    // Join mapping threads
    for (pthread_t *thread : mappingThreads) {
        pthread_join(*thread, nullptr);
    }

    // pasam listele catre reducer
    // reducer le combina
    while (auto powerMap = mappingArgumentWrapper.powerMapResultsQueue.pop()) {
        for (int power = 2; power <= R + 1; ++power) {
            if (powerMap->count(power)) {
                reducerArgumentWrappers[power]->reducerQueue.push(*(*powerMap)[power]);
            }
        }
        
        delete powerMap;
    }

    // Am pus datele, toate reducerele au datele bagate, putem sa le dam drumul
    for (int power = 2; power <= R + 1; ++power) {
        pthread_mutex_unlock(&reducerArgumentWrappers[power]->mutex);
    }
    
    
    for (const pthread_t *thread : reduceThreads) {
        pthread_join(*thread, nullptr);
    }
    
    // cleanup
    for (const auto& thread : mappingThreads) {
        delete thread;
    }
    
    for (const auto& thread : reduceThreads) {
        delete thread;
    }
    
    for (const auto& entry : reducerArgumentWrappers) {
        delete entry.second;
    }
    
    return 0;
}
