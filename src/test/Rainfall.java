package test;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Rainfall {
        int arraysize = 0; //size of the plot       
        String[][] AltPlot; //original altitude plot (we get this as input)
        String[][] directionPlot; //direction plot - shows us which way the water is flowing
        int sinkCounter = 0; //a counter for how many sinks we have
        HashMap<Integer, Integer> sinkCounterMap = new HashMap<Integer, Integer>(); //a map of a sink to basin area
        
        //constructor!
        //input: an integer array (containing altitudes) and the plot size
        public Rainfall(String[][] altitudes, int size){
            AltPlot = altitudes;
            arraysize = size;
        }
        
        //method - fills in the direction that the water will flow into the directionPlot
        // directions include: up("u"), down("d"), left("l"), right("r");
        // the directionPlot will be filled accordingly -> if altitude you are at 
        //     in the AltPlot is the smallest of the altitudes of the up, down, left, and 
        //     right altitudes, it will be a sink and will be labeled with an integer. 
        //     If one of the directions is the smallest of the 5 altitudes, the position
        //     will be labeled with a String indicating the direction. 
        public void fillDirectionPlot(){
            for(int i=0; i<arraysize; i++){
                for(int j=0; j<arraysize; j++){
                    int current = Integer.parseInt(AltPlot[i][j]);
                    
                    int min = current;
                    String minDirection = "c";
                    int right;
                    int left;
                    int up;
                    int down;
                    
                    if((j+1)<arraysize){
                        right = Integer.parseInt(AltPlot[i][j+1]);
                        if(right<min){
                            min = right;
                            minDirection = "r";
                        }
                    }
                    if((j-1)>=0){
                        left = Integer.parseInt(AltPlot[i][j-1]);
                        if(left<min){
                            min = left;
                            minDirection = "l";
                        }
                    }
                    if((i-1)>=0){
                        up = Integer.parseInt(AltPlot[i-1][j]);
                        if(up<min){
                            min = up;
                            minDirection = "u";
                        }
                    }
                    if((i+1)<arraysize){
                        down = Integer.parseInt(AltPlot[i+1][j]);
                        if(down<min){
                            min = down;
                            minDirection = "d";
                        }
                    }
                    
                    //check for which is the minimum number of the 5 and update
                    //directionPlot accordingly
                    if(min == current){
                        sinkCounter +=1;
                    
                        directionPlot[i][j] = Integer.toString(sinkCounter);
                    }
                    else{
                        
                        directionPlot[i][j] = minDirection;//indicates which way water flows
                    }
                }
            }
        }
        
    
    //by now, we know how many sinks there are total. We will make a hashmap of it;
    public void sinkCounterMap(){
        for(int i=1; i<(sinkCounter+1); i++){
            sinkCounterMap.put(i,1); //sink is included in basin count;
            
        }
    }
    
    //we will parse the directions to see where the basins are and update the 
    //sinkCounterMap accordingly
    public void ParseDirectionPlot(){
        int tempCounter = 0; //counter for how many sinks we've found so far.
        sinkCounterMap(); //we'll make the map first;
        for(int i=0; i<arraysize; i++){
            for(int j=0; j<arraysize; j++){
                String curr = directionPlot[i][j];
                //if it's a sink, we'll find it's basin area
                //with the convertNeighbors procedure
                if(isInteger(curr)){
                    tempCounter += 1;
                   convertNeighbors(Integer.parseInt(curr), i, j);
                }
                if(tempCounter == sinkCounter) break; 
            }
          if(tempCounter == sinkCounter) break; //we don't want to keep looking for sinks if we've found all of them  
        }
    }
    
    //helper procedure to verify a string is an int
    public boolean isInteger(String x){
        try{
            Integer.parseInt(x);
        
        }
        catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    
    
    //convertNeighbors finds the starting of a sink and looks around the sink
    //for the entire basin area leading towards that number.
    //ex: if a sink - 1 - is surrounded by directions leading to the sink,
    //    that area is all basin and will be added to the area for sink 1.
    public void convertNeighbors(int x, int i, int j){
        if((j+1)<arraysize){
            if(directionPlot[i][j+1].equals("l")){
                int currval = sinkCounterMap.get(x);
                sinkCounterMap.put(x, currval+1);
                convertNeighbors(x, i, (j+1)); //run this recursively
            }
        }
        if((j-1)>=0){
            if(directionPlot[i][j-1].equals("r")){
                int currval = sinkCounterMap.get(x);
                sinkCounterMap.put(x, currval+1);
                convertNeighbors(x, i, (j-1)); //run this recursively
            }
        }
        if((i-1)>=0){
            if(directionPlot[i-1][j].equals("d")){
                int currval = sinkCounterMap.get(x);
                sinkCounterMap.put(x, currval+1);
                convertNeighbors(x, (i-1), j); //run this recursively
            }
        }
        if((i+1)<arraysize){
            if(directionPlot[i+1][j].equals("u")){
                int currval = sinkCounterMap.get(x);
                sinkCounterMap.put(x, currval+1);
                convertNeighbors(x, (i+1), j); //run this recursively
            }
        }
    
    }
    
    //sorting basins by size and printing them out
    public void printBasinSizes(){
        PriorityQueue<Integer> BasinQueue = new PriorityQueue<Integer>(sinkCounter, Collections.reverseOrder());
        String toPrint = "";
        for(int x: sinkCounterMap.values()){
            BasinQueue.add(x);
        
        }
        
        for(int i=0; i<(sinkCounter); i++){
            toPrint = toPrint + BasinQueue.poll() + " ";
        }
        
        System.out.println(toPrint);
    }
    
    
    
    public static void main(String args[] ) throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        
        String[] input;
        int size = Integer.parseInt(bf.readLine());
        String[][] altitudes = new String[size][size];
        for(int count = 0; count<size; count++){
            altitudes[count] = bf.readLine().split(" ");
        
        }
        
        Solution toReturn = new Solution(altitudes, size);
        toReturn.fillDirectionPlot();
        toReturn.ParseDirectionPlot();
        toReturn.printBasinSizes();
    }
}


	
	public static void main(String[] args) {
		int size = 4;
		int[][] alts = new int[4][4];
		alts[0][0] = 0;
		alts[0][1] = 2;
		alts[0][2] = 1;
		alts[0][3] = 3;
		alts[1][0] = 2;
		alts[1][1] = 1;
		alts[1][2] = 0;
		alts[1][3] = 4;
		alts[2][0] = 3;
		alts[2][1] = 3;
		alts[2][2] = 3;
		alts[2][3] = 3;
		alts[3][0] = 5;
		alts[3][1] = 5;
		alts[3][2] = 2;
		alts[3][3] = 1;
		

		Rainfall test = new Rainfall(alts, size);
		test.fillDirectionPlot();
		test.ParseDirectionPlot();
		test.printBasinSizes();
		
	}

}
