Modalitatea de gandire:
Folosesc o coada sincronizata in care pun initial caile catre fisierele de procesat de catre Mapperi,
din care tot scot si dau Mapperilor cate un fisier, apoi pe masura ce acestia efectueaza operatiile si creeaza listele
partiale, acestea se adauga inapoi intr o coada de unde sunt luate de Reduceri ce unifica puterile perfecte pentru fiecare
exponent, le numara iar la final scriu rezultatul final in fisierele de output.

Functii:

"computePowerList" - primeste numele fisierului in care se afla numerele ce trebuiesc verificate,
verific daca un numar din fisier este putere perfecta, verificand daca exista
in lista ce tine toate numerele pentru o anumita putere. daca exista, il adaug in powerMap
la puterea corespunzatoare.
^2 -> [....]
^3 -> [...]
... 
^R+1 -> [...]
Astfel, o sa am listele partiale formate pentru fiecare numar citit din fisier.

"mappingFunc" - procesez argumentul, iau din coada sincronizata iau cate o cale catre fisierele de procesat,
creez listele partiale de puteri perfecte pt fiecare fisier de date, si apoi 
le pun in coada de rezultat de unde vor fi luate de Reduceri pentru a fi procesate si modificate
corespunzator.

"reduceFunc" - pun lock pentru ca thread-urile de reducer sa nu inceapa
inainte ca threadurile de mapper sa produca rezultatele, altfel threadurile de reducer
nu ar avea ce sa proceseze si ar termina instant. Scriu in fisierul de out numarul de puteri perfecte
cu exponent unic.

"SynchronizedQueue" - am folosit o coada implementata deja din c++, insa 
am facut o sincronizata folosind mutex. Este sincronizata deoarece 
ar putea exista situatia ca 2 thread-uri sa vrea sa scoata  acelasi fisier de procesat
sau sa dea push in acelasi timp, lucru care ar duce la suprapuneri si eventuale probleme.

"MappingArgumentWrapper" - structura creata de mine, deoarece aveam nevoie printr-un singur argument 
sa accesez numarul de Reduceri, coada sincronizata, map-ul de (putere, lista de puteri perfecte corespunzatoare),
si coada finala de rezultate puteri perfecte.

Metoda pentru calcularea puterilor perfecte: pentru optimizare, am calculat de la inceput
toate puterile perfecte pentru fiecare putere pana la numarul de reduceri, fiindca acest calcul se 
efectueaza o singura data la inceput, ci nu de fiecare data, apoi pentru fiecare numar din fiecare fisier 
de test doar am verificat daca este in setul de puteri perfecte. Am incercat si o alta metoda folosind 
ciurul lui Erastostene si cmmdc insa s a dovedit a nu fi la fel de eficienta dpdpv a duratei executiei testelor,
asa ca am ramas la prima metoda descrisa.