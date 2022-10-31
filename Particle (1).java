import java.util.List;
import java.util.ArrayList;
import java.util.Random;
 
/**
 * Particle class that maintains velocity and position vector by initialising and updating them. 
 * The test function is evaluated in this class and the personal best is stored. 
 * 
 * @authors Souleman Toure, Diyaa Yaqub, and Jigyasa Subedi
 */
public class Particle {
    private int dimensions; 
     
    //the velocity vector for the particle
    private List<Double> velocity; 
    
    //the position vector for the particle
    private List<Double> position; 
    
    //the position at which the best solution was found for this particle
    List<Double> pbestPosition; 
    
    //the function being evaluated
    private String function; 
    
    //the personal best value found by the particle over a number of iterations
    private double pbest; 

    //personal best acceleration coeffecient
    private double phi1 = 2.05; 
    
    // neighborhood best acceleration coefficient
    private double phi2 = 2.05;  
    
    // constriction factor
    private double constrictionFactor = 0.7298;
    
    //a list containing all the neighbors this particle has 
    private List<Particle> neighbors;
    
    private Random rand = new Random(); 
    
    /**
     * Constructor for a particle. 
     * 
     * @param function is the function that will be evaluated by the particle. 
     */
    public Particle(String function) {
        velocity = new ArrayList<Double>(); 
        position = new ArrayList<Double>(); 
        neighbors = new ArrayList<Particle>(); 
        pbestPosition = new ArrayList<Double>(); 
        
        this.dimensions = 30; 
        this.function = function; 
        pbest = Double.MAX_VALUE; 
        
        double max = 0; 
        double min = 0; 
        
        //initialises the particles with random values for the velocity and position vectors within a specified range depending on the function being evaluated. 
        for (int i = 0; i < dimensions; i++) {
             if (function.equals("rok")) {
                 min = 15.0; 
                 max = 30.0; 
                 position.add(Math.floor(Math.random()*(max-min)+1)+min); 
                 
                 //initially sets the personal best position to be the same as current position, but this is updated as position is updated over numerous iterations. 
                 pbestPosition.add(Math.floor(Math.random()*max-min+1)+min); 
                 
                 min = -2.0; 
                 max = 2.0; 
                 velocity.add(Math.floor(Math.random()*(max-min)+1)+min);
                } else if (function.equals("ack")) {
                    min = 16.0; 
                    max = 32.0; 
                    position.add(Math.floor(Math.random()*max-min+1)+min);
                    pbestPosition.add(Math.floor(Math.random()*max-min+1)+min);
                    
                    min = -2.0; 
                    max = 4.0; 
                    velocity.add(Math.floor(Math.random()*max-min+1)+min);
                } else if (function.equals("ras")) {
        
                    min = 2.56; 
                    max = 5.12; 
                    position.add(Math.floor(Math.random()*max-min+1)+min);
                    pbestPosition.add(Math.floor(Math.random()*max-min+1)+min); 
                    
                    min = -2.0; 
                    max = 4.0; 
                    velocity.add(Math.floor(Math.random()*max-min+1)+min);
            }
        }
    }
    
    /**
     * Updates this particle's velocity and position vector according to an equation that considers the neighborhood best, the personal best, and some randomisation. 
     * After updating the velocity and position, the method uses the new position to evaluate the function and updates the pbest value and pbestPosition of the particle accordingly. 
     */
    public void update() {  
        double pBestAttract; 
        double nBestAttract; 
          
        for (int i = 0; i < dimensions; i ++) {

             //compute acceleration based on personal best. 
            pBestAttract = pbestPosition.get(i) - position.get(i);
            pBestAttract *= rand.nextDouble() * phi1; 
    
                 
            //compute acceleration due to neighborhood best
            Particle nbest = findNBest(); //finds particle with the best pbest in the neighborhood
            if (nbest.getPBest() < pbest) { //if the neighbor has a better solution than current particle's position
                nBestAttract = nbest.getPBestPosition(i) - position.get(i);
                nBestAttract *= rand.nextDouble() * phi2; 
            } else {
                nBestAttract = 0; 
                nBestAttract *= rand.nextDouble() * phi2; 
            }
            
               //constrict the new velocity and reset the current velocity
               double curVelocity = velocity.get(i); 
               double newVelocity = (curVelocity + (nBestAttract + pBestAttract)) * constrictionFactor; 
               velocity.set(i, newVelocity); 
            
               //update position
               double curPosition= position.get(i);
               double newPosition = newVelocity + curPosition; 
               position.set(i, newPosition);
                
            }
            
              //find the value for this particle given the new position. 
               double curValue = evalFunction(); 
           
               //update personal best
               if (curValue < pbest) {
                   pbest = curValue; 
                   for (int i  = 0; i < position.size(); i ++) {
                       pbestPosition.set(i, position.get(i));
                   }
               }
            
        }
            
            
            /**
          * This function is used to assign neighbors to each particle. 
          * Depending on the topology, each particle will have a different set of neighbors and this function is called so that each particle 
          * can store its list of neighbors and have it influence the way the velocity and position vector is updated. 
          * 
          * @param neighbor is a Particle that is this particle's neighbor and needs to be added to the list. 
          */
        public void assignN(Particle neighbor) {
        neighbors.add(neighbor);
        }
    
        /**
         * This function is used only for global topology. It takes in the full list of 
         * particles and assings them to the neighborhood since the neighborhood for the global topology is just all the other particles in the swarm. 
         * 
         * @param neighbors is the full list of particles in the swarm. 
         */
        public void assignNList(List<Particle> neighbors) {
            for (int i = 0; i < neighbors.size(); i ++) {
                this.neighbors.add(neighbors.get(i)); 
            }
        }
            
            
            
            
        /**
             * Method that returns the best solution found within the particle's neighborhood. 
             * 
             * @return the neighborhood best value. 
             */
            public double findNBestValue() {
                return findNBest().getPBest(); 
            }
            
            
            /**
             * Helper method that iterates through the list of neighbors to find the neighbor with the best personal best solution. 
             * 
             * @return the particle that is the neighborhood best (has the best personal best solution within the neighborhood). 
             */
            public Particle findNBest() {
                
                double nBest = Double.MAX_VALUE; 
                Particle nBestParticle = neighbors.get(0); 
                for (int i = 0; i < neighbors.size(); i ++) {
                    if (neighbors.get(i).getPBest() < nBest) {
                        nBestParticle = neighbors.get(i); 
                        nBest = neighbors.get(i).getPBest(); 
                    }
                }
                
                return nBestParticle; 

            }
             
            /**
             * Getter method for the particle's personal best. 
             * @return the value of the personal best solution. 
             */
            public double getPBest() {
                return pbest; 
            }
            
            /**
             * Getter method for the position at which the particle found its personal best solution. 
             * 
             * @param dimension is the dimension for which the v
             * @return double the value in the position vector at specified dimension. 
             */
            public double getPBestPosition(int dimension) {
                return pbestPosition.get(dimension); 
            }
            
            
             /**
             * This is a contians method. Given a particle, it checks if the particle already exists within the neighborhood list. 
             * 
             * @param particle is the particle being checked for in the neighborhood list. 
             * @return whether or not the particle in the parameter is already a neighbor. 
             */
            public boolean nContains(Particle particle) {
                return neighbors.contains(particle); 
            }
            
            /**
             * Calls on the the function that needs to be evaluated based on user-specified input. 
             *
             */
               public double evalFunction() {
                double retValue = 0.0;
             
                if (function.equals("rok")) {
                  retValue = evalRosenbrock(); 
                }  
                else if (function.equals("ras")){
                  retValue = evalRastrigin(); 
                }  
                else if (function.equals("ack")) {
                  retValue = evalAckley();
                }
            
                return retValue;
            }

            
            
            /**
     * Evaluates the Rosenbrock function in the 30 dimensional space. 
     * 
     * @return the value of the solution found by this particle
     */
    public double evalRosenbrock () {
          
          double retVal = 0; 
           
        for(int i= 0 ; i < (dimensions - 1) ; i++) {
            double xi = position.get(i);
            double xiPlusOne = position.get(i+1);
           
            retVal += 100.0 * Math.pow(xiPlusOne - (xi*xi), 2.0) + Math.pow(xi-1.0, 2.0);
        }
          return retVal; 

        }

        /**
         * Evaluates the Rastrigin function in the 30 dimensional space. 
         * 
         * @return the value of the solution found by this particle
         */
    public double evalRastrigin () {
           double retVal = 0;

            for(int i = 0 ; i < dimensions; i++) {
                double xi = position.get(i);
                retVal += xi*xi - 10.0*Math.cos(2.0*Math.PI*xi) + 10.0;
            }

           return retVal;  
        }

        
        /**
         * Evaluates the Ackley function in the 30 dimensional space. 
         * 
         * @return the value of the solution found by this particle. 
         */
     public double evalAckley () {

           double firstSum = 0.0;
           double secondSum = 0.0;
          
           for(int i = 0 ; i < dimensions ; i++) {
                  double xi = position.get(i);
                  firstSum += xi * xi; 
                  secondSum += Math.cos(2.0*Math.PI*xi);
               }
           return -20.0 * Math.exp(-0.2 * Math.sqrt(firstSum/dimensions)) - 
                  Math.exp(secondSum/dimensions) + 20.0 + Math.E;   

        }
    }