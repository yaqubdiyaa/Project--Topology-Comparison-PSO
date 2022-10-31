# Title 

* A Comparison of Neighborhood Topologies in Particle Swarm Optimization

## About 

* A Particle Swarm Optimization algorithm to assess the role that different neighborhood topologies played in finding the optimal solution, as tested using three optimization functions. The program contains three classes. Main.java reads in the user provided arguments and creates the necessary Swarm object to run the program. The Swarm.java creates and initialises the particles in the swarm. It also contains other functionalities of the algorithm such as creating neighborhoods, finding neighborhood best solution and printing the best value at every 1000 iterations. The Particle.java represents one particle in the swarm and contains attributes like position, velocity, neighbors as well as methods to find personal best solution, update velocity and to run the evaluation functions. 

## Installation

* Download the following files:
1) Main.java
2) Particle.java
3) Swarm.java

## Usage

You can run the program on command line using the following structure:

* Switch into the directory that has the programs and the input files
* Type javac *.java to compile any changes to the code
* Type: java Main {function} {topology} {swarm size}
<br> Example: java Main ack ri 49 </br>

## Authors

* *Souleman Toure*
* *Jigyasa Subedi*
* *Diyaa Yaqub*
