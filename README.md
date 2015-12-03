# CVRP-GA-Java
Genetic Algorithm Capacitated Vehicle Routing 10! Problem Size

need command line or terminal to run
to compile all of the source code 

javac *.java

to run the brute force for ten cities just put
java BruteForce10

to run the brute force for eleven cities just put(It takes 8 seconds)
java BruteForce11

to run the brute force for eleven cities just put(It takes 90 seconds)
java BruteForce11

to run CVRP 10 cities

java CVRP10 <popNum> <generation> <crossRate> <mutationRate> <vehiclecapacity>
Ex. java CVRP 100 100 80 50 200
crossRate <0-100> mutationRate <0-100> vehicle <100,200,1000>

to run CVRP 11 cities
java CVRP11 <popNum> <generation> <crossRate> <mutationRate> <vehiclecapacity>
Ex. java CVRP 100 100 80 50 200
crossRate <0-100> mutationRate <0-100> vehicle <100,200,1000>

java CVRP12 <popNum> <generation> <crossRate> <mutationRate> <vehiclecapacity>
Ex. java CVRP 100 100 80 50 200
crossRate <0-100> mutationRate <0-100> vehicle <100,200,1000>
