#include "MappingArgumentWrapper.hpp"
#include <vector>

MappingArgumentWrapper::~MappingArgumentWrapper() {
    while (auto mapEntry = this->powerMapResultsQueue.pop()) {
        for (auto const& pairs : *mapEntry) {
            delete pairs.second;
        }
    }
}

MappingArgumentWrapper::MappingArgumentWrapper(int R, std::map<int, std::set<int>> * allPerfectPowers): allPerfectPowers(allPerfectPowers), R(R) {}
