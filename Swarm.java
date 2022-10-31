import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

/**
 * Swarm class that stores all particles in the swarm. It assigns neighbors to each particle, iterates to update each particle's velocity for 10000 iterations,
 * and outputs the best solution found in the swarm. 
 * 
 * @authors Jigyasa Subedi, Souleman Toure, and Diyaa Yaqub 
 */
public class Swarm {
    
    //list containing all the particles in the swarm
    private List<Particle> allParticles; 
    
    /**
     * Constructor for the swarm. 
     * This creates a number of particles and assigns them to allParticles. Then it assigns neighborhoods to each particle depending on user-specified topology. 
     * Lastly, it calls on the update method for each particle to update its velocity and position for 10000 iterations. 
     * For each 1000 iterations, the best solution found in the swarm is added to a List, which is then outputted (by printing it) once the iterations are finished. 
     * 
     * @param numParticles the total number of particles in the swarm. 
     * @param function is the function that this swarm and its particles will solve. 
     * @param topology is the neighborhood topology. 
     */
    public Swarm(int numParticles, String function, String topology) {
        allParticles = new ArrayList<Particle>(); 
        
        //initialises particles and adds them to list of all particles (that represents the swarm). 
        for (int i = 0; i < numParticles; i ++) {
            Particle particle = new Particle(function); 
            allParticles.add(particle); 
        }
        
        
        //depending on topology, it calls on methods that assign neighbors to each particle
            if (topology.equals("gl")) {
                 for (int i = 0; i < allParticles.size(); i++) {
                     Particle particle = allParticles.get(i); 
                     particle.assignNList(allParticles); 
                 }
            } else if (topology.equals("ri")) {
                ringN(); 
            } else if (topology.equals("vn")) {
                neumannN(); 
            } else if (topology.equals("ra")) {
                randomN(); 
            }
    
    //the list contains the best values found by each neighborhood.
    List<Double> NBestValues = new ArrayList<Double>(); 
    
    //the list contains the best solution from the best values found by each neighborhood
    List<Double> iterations = new ArrayList<Double>(); 
    

    for (int iter = 1; iter <= 10000; iter++) {
        NBestValues.clear(); 
        for (int p = 0; p < allParticles.size(); p++) {
            Particle particle = allParticles.get(p); 
            particle.update(); 
        
                //each 1000 iterations, the best solution obtained from the swarm of particles is added to list iterations
                if (iter % 1000 == 0) {
                    NBestValues.add(particle.findNBestValue()); //adds the best value found from each particles neighborhood to a list. 
                    if (p == allParticles.size() - 1) {
                       //finds the minimum value from all the neighborhood bests and adds it to a list that maintains the solution found at this iteration
                        iterations.add(Collections.min(NBestValues)); 
                    }
                }    
        }         
    }
 
    //outputs the best solution found to functions at every 1000 iterations. 
    System.out.println(iterations); 
    
}

/**
     * Creates a neighborhood for each particle based on ring topology. Each particle has 2 neighbors. 
     * The neighborhoods list is being populated with the particle, and its two neighbors. The method creates sublists that hold each neighborhood. 
     * Each index position in the neighborhoods list should correspond to the particle whose neighbors are stored in that index position. For example,
     * index position 3 contains particle 3 and particle 3's neighbors.
     */
    public void ringN() {
        for (int i = 0; i < allParticles.size(); i++) {
                Particle particle = allParticles.get(i); 
            if (i == 0){ //Ring topology should wrap around. Therefore, the neighbors of the first particle in the list should be the last and the one in the next index position. 
                particle.assignN(allParticles.get(allParticles.size()-1));
                particle.assignN(allParticles.get(i + 1));
            } else if (i == allParticles.size() - 1){ //if particle is last in the swarm, its neighbors should be first element and the one to its left (index-1). 
                particle.assignN(allParticles.get(0));
                particle.assignN(allParticles.get(i-1));
            } else{
                particle.assignN(allParticles.get(i - 1));
                particle.assignN(allParticles.get(i + 1));
            }
            
            particle.assignN(particle); //this current particle is also assigned to the neighborhood. 

        }
    }
    
    /**
     * Assigns neighborhoods to each particle in the swarm using Von Neumann topology. 
     * Particles are imagined to be in a gird that wraps aorund in both directions, and the 
     * neighbors of each particle are the particles above, below, and to the left and right of it. 
     */
    public void neumannN() {
        
        Particle[][] topology; 
        int row = 0; 
        int col = 0; 
    
        //determines dimensions for von neumann array (matrix/grid size) depending on number of particles in the swarm. 
        if (allParticles.size() == 16) {
            row = 4; 
            col = 4; 
        } else if (allParticles.size() == 30) {
            row = 5; 
            col = 6; 
        } else if (allParticles.size() == 49) {
            row = 7; 
            col = 7; 
        }
        
        //assigns each particle to a cell in the matrix/grid. 
        topology = new Particle[row][col]; 
        int count = 0; 
        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j++) {
                topology[i][j] = allParticles.get(count); 
                count+=1; 
            }
        }
        
        
        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j++) {
                //the following conditions consider if going up, down, right, or left would reach the end of the matrix and wraps around to assign neighbors accordingly.
                if (i == 0 && j == 0) { //if both row and column are 0
                    topology[i][j].assignN(topology[row-1][j]); //top
                    topology[i][j].assignN(topology[i][col-1]); //left
        
                    topology[i][j].assignN(topology[i+1][j]); //bottom
                    topology[i][j].assignN(topology[i][j+1]); //right
                } else if (i == 0 && j == col-1) {
                    topology[i][j].assignN(topology[row-1][j]);//top
                   topology[i][j].assignN(topology[i][0]); //right
                    
                   topology[i][j].assignN(topology[i][j-1]); //left
                    topology[i][j].assignN((topology[i+1][j]));//bottom
                } else if (i == row-1 && j == 0) {
                    topology[i][j].assignN(topology[i][col-1]);//left 
                    topology[i][j].assignN(topology[0][j]); //bottom
                    
                    topology[i][j].assignN(topology[i-1][j]);//top
                   topology[i][j].assignN(topology[i][j+1]);//right
                } else if (i == row-1 && j == col-1) {
                    topology[i][j].assignN(topology[0][j]); //bottom
                    topology[i][j].assignN(topology[i][0]);//right
                    
                    topology[i][j].assignN(topology[i][j-1]);//left
                    topology[i][j].assignN(topology[i-1][j]);//top
                } else if (i == 0) {
                    topology[i][j].assignN(topology[row-1][i]); //top
                    
                   topology[i][j].assignN(topology[i][j-1]);//left
                   topology[i][j].assignN(topology[i][j+1]);//right
                   topology[i][j].assignN(topology[i+1][j]); //bottom
                } else if (i == row-1) {
                    topology[i][j].assignN(topology[0][j]);//bottom 
                    
                    topology[i][j].assignN(topology[i][j-1]);//left
                    topology[i][j].assignN(topology[i][j+1]);//right
                   topology[i][j].assignN(topology[i-1][j]);//top
                } else if (j == 0) {
                    topology[i][j].assignN(topology[i][col-1]);//left
                    
                    topology[i][j].assignN(topology[i][j+1]);//right
                   topology[i][j].assignN(topology[i-1][j]);//top
                   topology[i][j].assignN(topology[i+1][j]); //bottom
                } else if (j == col-1) {
                    topology[i][j].assignN(topology[i][0]); //right                    
                    
                    topology[i][j].assignN(topology[i-1][j]); //top
                    topology[i][j].assignN(topology[i][j-1]); //left
                    topology[i][j].assignN(topology[i+1][j]); //bottom
                } else {
                    topology[i][j].assignN(topology[i+1][j]);
                    topology[i][j].assignN(topology[i-1][j]); 
                    topology[i][j].assignN(topology[i][j-1]); 
                    topology[i][j].assignN(topology[i][j+1]); 
                }
                
                topology[i][j].assignN(topology[i][j]); 

            }
                       
            }
        }
        
        
        /**
         * Method creates random neighborhoods of size 5 for each particle by choosing particles from the swarm randomly and without repetition. 
         * For each particle, a particle's neighborhood is recreated in the same way with a probability of 0.2. 
         */
        public void randomN() {
        Random rand = new Random(); 
        
        for (int i = 0; i < allParticles.size(); i++) {
            Particle particle = allParticles.get(i); 
            particle.assignN(particle); //adds current particle to the neighborhood. 
            int count = 1; 
        
            while (count < 5) { //creates neighborhood of size 5 (including current particle). 
                int num = rand.nextInt(allParticles.size()-1); 
                double probability = rand.nextDouble(); 
                
                //if the particle selected isn't already part of this particle's neighborhood, or if the probability of it being included is less than 0.2, 
                //the particle selected should be included in the neighborhood. 
                if (particle.nContains(allParticles.get(num)) || (probability < 0.2)) { 
                    num = rand.nextInt(allParticles.size()-1); 
                } 
                particle.assignN(allParticles.get(num)); 
                count++; 
            }    
        }
    } 
    }
