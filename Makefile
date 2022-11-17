CXXFLAGS = -std=c++11 -Wall 
LIBS = -lm -lpthread

build: main.o SynchronizedQueue.o MappingArgumentWrapper.o ReducerArgumentWrapper.o
	g++ $(CXXFLAGS) main.o SynchronizedQueue.o MappingArgumentWrapper.o ReducerArgumentWrapper.o $(LIBS) -o tema1

main.o: main.cpp
	g++ $(CXXFLAGS) -c main.cpp

SynchronizedQueue.o:
	g++ $(CXXFLAGS) -c SynchronizedQueue.cpp

MappingArgumentWrapper.o:
	g++ $(CXXFLAGS) -c MappingArgumentWrapper.cpp

ReducerArgumentWrapper.o:
	g++ $(CXXFLAGS) -c ReducerArgumentWrapper.cpp

clean:
	rm *.o tema1
