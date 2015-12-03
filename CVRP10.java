import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
public class CVRP extends JPanel {
	 private static final int MAX_SCORE = 9;
	 private static final int PREF_W = 800;
   private static final int PREF_H = 600;
   private static final int BORDER_GAP = 1;
   private static final Color GRAPH_COLOR = Color.black;
   private static final Color GRAPH_POINT_COLOR = new Color(0, 250, 0, 250);
   private static final Stroke GRAPH_STROKE = new BasicStroke(4f);
   private static final int GRAPH_POINT_WIDTH = 5;
   private static final int Y_HATCH_CNT = 1;
   private static List<Float> scores;
	public static int[][] distanceData= new int[][] {
            {0,10,28,23,65,16,16,42,16,21,85,23,35,70,23},//0
			{10,0,76,72,150,52,52,104,42,62,190,60,90,110,65},//1
			{28,76,0,106,74,48,83,28,88,58,186,106,86,146,106},//2
			{23,72,106,0,180,82,82,134,18,82,220,18,120,110,10},//3
			{65,150,74,180,0,122,157,56,162,132,172,180,120,215,180},//4
			{16,52,48,82,122,0,59,76,64,34,162,82,62,117,82},//5
			{16,52,83,82,157,59,0,111,64,69,197,82,97,74,82},//6
			{42,104,28,134,56,76,111,0,116,106,126,134,114,283,134},//7
			{16,42,88,18,162,64,64,116,0,74,202,18,102,112,18},//8
			{21,62,58,92,132,34,69,106,74,0,172,92,72,127,92},//9
			{85,190,186,220,172,162,197,126,202,172,0,220,110,255,220},//10
			{23,60,106,18,180,82,82,134,18,92,220,0,120,130,10},//11
			{35,90,86,120,120,97,97,114,102,72,120,120,0,155,120},//12
			{70,110,146,110,215,74,74,283,112,127,130,130,155,0,120},//13
			{23,65,106,10,180,82,82,134,18,92,220,10,120,120,0}//14
		};
     public static int[] demand = new int[] 
     		{ 0, 
			//use data from set 4
            50, // node 1
			100, // node 2
			150, // node 3
			0, // node 4
			20, // node 5
			0, // node 6
			80, // node 7
			20, // node 8
			0, // node 9
			100, // node 10
			0, // node 11
			10, // node 12
			50, // node 13
			120 // node 14
    };
	private ArrayList<ArrayList<Integer>> population;
	private static final int CITY = 11;
	//private ArrayList<ArrayList<Integer>> bestRoutes;
	//private double bestCost = 99999;
	public static Map<Integer,ArrayList<Integer>> putFirstEntries(int max, Map<Integer,ArrayList<Integer>> source) {
  		int count = 0;
  		TreeMap<Integer,ArrayList<Integer>> target = new TreeMap<Integer,ArrayList<Integer>>();
  		for (Map.Entry<Integer,ArrayList<Integer>> entry:source.entrySet()) 
  		{
   			if (count >= max) break;
     		target.put(entry.getKey(), entry.getValue());
     		count++;
  		}
  		return target;
	}

	public static void main(String[] args) {
		if(args.length != 5)
		{
			System.out.println("java CVRP <popNum> <generation> <crossRate> <mutationRate> <vehiclecapacity>");
			System.out.println("Ex. java CVRP 100 100 80 50 200");
			System.out.println("crossRate <0-100> mutationRate <0-100> vehicle <100,200,1000>");
			return ;
		}
        int popNum= Integer.parseInt(args[0]);
        int generation = Integer.parseInt(args[1]);
        int crossRate = Integer.parseInt(args[2]);
        int mutationRate = Integer.parseInt(args[3]);
        int maxCapacity = Integer.parseInt(args[4]);
        /*int crossRate = 80;
        int popNum = 100;
        int generation = 20;
        int mutationRate = 10;*/
        Random rand = new Random();
        Map<Integer,ArrayList<Integer>> currentGen = new TreeMap<Integer,ArrayList<Integer>>();
        Map<Integer,ArrayList<Integer>> nextGen = new TreeMap<Integer,ArrayList<Integer>>();
        ArrayList<Integer> costs = new ArrayList<Integer> ();
        scores = new ArrayList<Float> ();
        ArrayList<ArrayList<Integer>> routes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> nodeOnetoTen = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11));
        ArrayList<Integer> parent1 =  new ArrayList<Integer> ();
        ArrayList<Integer> parent2 =  new ArrayList<Integer> ();
        ArrayList<Integer> child1 =  new ArrayList<Integer> ();
        ArrayList<Integer> child2 =  new ArrayList<Integer> ();
        ArrayList<Integer> keys;//cost to access route random select to crossover 
        Map<Integer,Integer> keytoValue = new HashMap<Integer,Integer>();//check duplicate in child chromosome 
        Map<Integer,Integer> valuetoKey = new HashMap<Integer,Integer>();//check duplicate in child chromosome 
        int i=0,j=0;//
        int nextCity=0,currentCity=0;
        int vehiclecapacity = maxCapacity;
        int index=0;
        int startPos=0,endPos;//startposition to swap and end
        int tempSwap=0;
        for(i=0;i<popNum;i++)//initilize costs each node
        {
        	costs.add(i,0);
        }
        for(i=0;i<popNum;i++)//initilize costs each node
        {
        	routes.add(i,new ArrayList<Integer>());
        }
        //for(i=0;i<popNum;i++)//random pick node with costs matching constraint(capacity)
        double startTime = System.currentTimeMillis();
	    while(currentGen.size()<popNum)
	    {
	    	vehiclecapacity = maxCapacity;
	    	currentCity = 0;
	    	//System.out.println("popNum:"+i);
	    	j=0;
	    	//for(j=0;j<CITY+1;j++)
	    	routes.add(new ArrayList<Integer>());
	    	costs.add(new Integer(0));
	    	while(routes.get(i).size()!=CITY)
	    	{
	    	//	System.out.println("Currentvehiclecapacity="+vehiclecapacity+"cost="+costs.get(i));
	        	index = rand.nextInt(nodeOnetoTen.size());
	        	nextCity = nodeOnetoTen.get(index);
	        	//System.out.println(nodeOnetoTen.size()+" "+index+" "+nextCity+" "+j);
	        	nodeOnetoTen.remove(index);

	        	/*while(alreadyNode.contains(nextCity))
	        	{
	        		nextCity = rand.nextInt(10) +  1;
	        	}
	        	alreadyNode.add(nextCity);*/
	        	if(vehiclecapacity - demand[nextCity] < 0)
	        	{
	        		vehiclecapacity = maxCapacity;
	        		costs.set(i,costs.get(i)+distanceData[currentCity][0]);
	        		//System.out.println(currentCity+"->"+0+" +Distance*="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
	        		currentCity = 0;
	        		vehiclecapacity -= demand[nextCity];
	        		costs.set(i,costs.get(i)+distanceData[currentCity][nextCity]);
					//System.out.println(currentCity+"->"+nextCity+" +Distance**="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
					routes.get(i).add(j,nextCity);//
					currentCity = nextCity;
	        		//costs.set(i,costs.get(i))+distanceData[currentCity][nextCity]));
					//if(i != CITY)
	        	}	        	
	        	else// > 0 remain
	        	{
	        		vehiclecapacity -= demand[nextCity];
	        		costs.set(i,costs.get(i)+distanceData[currentCity][nextCity]);
	        		//System.out.println(currentCity+"->"+nextCity+" +Distance***="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
					routes.get(i).add(j,nextCity);//
					currentCity = nextCity;
	        	}
	        	if(vehiclecapacity == 0 && j != CITY-1)//
	        	{
	        		costs.set(i,costs.get(i)+distanceData[currentCity][0]);
	        		vehiclecapacity = maxCapacity;
	        		//System.out.println(currentCity+"->"+nextCity+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
	        		currentCity = 0;
	        	}

	        	j++;	
	        }
	        costs.set(i,costs.get(i)+distanceData[currentCity][0]);//cost go back to node 0
	        currentGen.put(costs.get(i),routes.get(i));
	        //System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
	        nodeOnetoTen = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11));
	        i++; 
        }
        /*Collections.sort(routes,new Comparators<Integer>(){
        	@Override
        	public ArrayList<Integer> compare(ArrayList<Integer> , ArrayList<Integer> route2)
        	{
        		return route1.compareTo(route2)
        	}
        });*/
		/*for (Integer gene: currentGen.keySet())
		{
            String key =gene.toString();
            String value = currentGen.get(gene).toString();  
            System.out.println(key + " " + value);  
		} */
		/*for(i=0;i<popNum;i++)
	    {
	    	System.out.println(i+"."+" Route= "+routes.get(i)+" Cost="+costs.get(i));	  
	    }*/
	    /*//Collections.sort(routes);
        //Collections.sort(costs);       
        for(i=0;i<popNum;i++)
	    {
	    	System.out.println(i+"."+" Route= "+routes.get(i)+" Cost="+costs.get(i));	  
	    }*/
	    //nextGen = currentGen.headMap(100-(popNum*crossRate)/2);
	    /*for(i=0;i<100-(popNum*crossRate)/2;i++)
	    {
	    	nextGen.put
	    }*/
	    //nextGen = putFirstEntries(/2, currentGen);
	    int gen;
	    for(gen=1;gen<=generation;gen++)
       	{
		    i=0;
		    //System.out.println("Generation : "+gen);
		    for (Integer gene: currentGen.keySet())//print nextGen
			{
				if(i>=1)
					break;
	            String key =gene.toString();
	            String value = currentGen.get(gene).toString();  
	            //System.out.println("1st ::"+ "\tRoute # " + value +" Cost : "+key );
	            scores.add((float)gene/100);
	            i++;
	        }
	        //System.out.println(" ");
		    i=0;
		    for (Map.Entry<Integer, ArrayList<Integer>> entry : currentGen.entrySet())//Bring top ten to nextGen
		    {
		    	if(i >= popNum*((1.0-(float)(crossRate)/100))/2) break;//(1-crossRate)/2 example crossRate = 80 10%
		 		//System.out.println(i+" Key: " + entry.getKey() + ". Value: " + entry.getValue());
		 		nextGen.put(entry.getKey(),entry.getValue());
		 		i++;
			}
			i=0;
			/*for (Integer gene: nextGen.keySet())//print nextGen
				{
		            String key =gene.toString();
		            String value = nextGen.get(gene).toString();  
		            System.out.println((i+1)+" "+key + " " + value);
		            i++;  
				}*/
		    routes.clear();
		    costs.clear();
			while(nextGen.size()<crossRate-1)
			{
				//PMX Crossover
				keys = new ArrayList<Integer>(currentGen.keySet());
				index = keys.get(rand.nextInt(keys.size()));
				parent1 = currentGen.get(index);
				index = keys.get(rand.nextInt(keys.size()));
				parent2 = currentGen.get(index);
				//parent1=routes.get(rand.nextInt(costs.size()));
				//parent2=routes.get(rand.nextInt(costs.size()));
				//System.out.println("Parent1="+parent1);
				//System.out.println("Parent2="+parent2);
				child1 = new ArrayList<Integer> (parent1);
				child2 = new ArrayList<Integer> (parent2);
				startPos = rand.nextInt(parent1.size());
				endPos = startPos+rand.nextInt(parent1.size()-startPos);
				//System.out.println("startPos="+startPos+" EndPos="+endPos);
				//PMX Crossover
				//System.out.println("Child1="+child1);
				//System.out.println("Child2="+child2);
				for (j = startPos; j <= endPos; j++)
				{
					int gene1 = child1.get(j);
					int gene2 = child2.get(j);
					int tmp1 = gene2;
					int tmp2 = gene1;

					child1.set(child1.indexOf(gene2), gene1);
					child1.set(j, new Integer(tmp1));
					child2.set(child2.indexOf(gene1), gene2);
					child2.set(j, new Integer(tmp2));
				}
				//System.out.println("Before\nChild1="+child1);
				//System.out.println("Child2="+child2);
				int prob,tmp,index1,index2;
				//Muatation some swap
				prob = rand.nextInt(100);
				if (prob <= mutationRate) 
				{
					//System.out.println("Shuffle");
					Collections.shuffle(child1);
					/*index1 = rand.nextInt(child1.size());
					index2 = rand.nextInt(child1.size());
					tmp = child1.get(index2);
					//System.out.println("Mutate at"+index1+" "+index2);
					System.out.println("Child1index1"+child1.get(index1)+"Child1index1"+child1.get(index2)+"Size"+child1.size());
					child1.set(index2,child1.get(index1));
					child1.set(index1,tmp);
					System.out.println("Child1index1"+child1.get(index1)+"Child1index1"+child1.get(index2)+"Size"+child1.size());
					*/
				}
				prob = rand.nextInt(100);
				if (prob <= mutationRate) 
				{
					//System.out.println("Shuffle");
					Collections.shuffle(child2);
					/*
					index1 = rand.nextInt(child2.size());
					index2 = rand.nextInt(child2.size());
					System.out.println("Child2index2"+child2.get(index1)+"Child2index2"+child2.get(index2));
					//System.out.println("Mutate at"+index1+" "+index2);
					tmp = child2.get(index2);
					child1.set(index2,child2.get(index1));
					child1.set(index1,tmp);
					System.out.println("Child2index2"+child2.get(index1)+"Child2index1"+child2.get(index2));
					*/
				}
				//System.out.println("After Mutation \nChild1="+child1);
				//System.out.println("Child2="+child2);
				j=0;
		    	//for(j=0;j<CITY+1;j++)
		    	vehiclecapacity = maxCapacity;
		    	currentCity = 0;
		    	
		    	int cost=0;
		    	for(j=0;j<child1.size();j++)
		    	{
		    		nextCity = child1.get(j);
		        	if(vehiclecapacity - demand[nextCity] < 0)
		        	{
		        		vehiclecapacity = maxCapacity;
		        		cost+=distanceData[currentCity][0];
		        		//System.out.println(currentCity+"->"+0+" +Distance*="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        		currentCity = 0;
		        		vehiclecapacity -= demand[nextCity];
		        		cost+=distanceData[currentCity][nextCity];
						//System.out.println(currentCity+"->"+nextCity+" +Distance**="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
						
						currentCity = nextCity;
		        		
		        	}	        	
		        	else// > 0 remain
		        	{
		        		vehiclecapacity -= demand[nextCity];
		        		cost+=distanceData[currentCity][nextCity];
		        		//System.out.println(currentCity+"->"+nextCity+" +Distance***="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
						currentCity = nextCity;
		        	}
		        	if(vehiclecapacity == 0 && j != CITY-1)//
		        	{
		        		cost+=distanceData[currentCity][0];
		        		vehiclecapacity = maxCapacity;
		        		//System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        		currentCity = 0;
		        	}
		        }
		        //System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        cost+=distanceData[currentCity][0];//cost go back to node 0
		        //System.out.println("Cost="+cost);
		        nextGen.put(cost,child1);
		        vehiclecapacity = maxCapacity;
		    	currentCity = 0;
		    	cost=0;
		    	for(j=0;j<child2.size();j++)
		    	{
		    		nextCity = child2.get(j);
		        	if(vehiclecapacity - demand[nextCity] < 0)
		        	{
		        		vehiclecapacity = maxCapacity;
		        		cost+=distanceData[currentCity][0];
		        		//System.out.println(currentCity+"->"+0+" +Distance*="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        		currentCity = 0;
		        		vehiclecapacity -= demand[nextCity];
		        		cost+=distanceData[currentCity][nextCity];
						//System.out.println(currentCity+"->"+nextCity+" +Distance**="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
						
						currentCity = nextCity;
		        		
		        	}	        	
		        	else// > 0 remain
		        	{
		        		vehiclecapacity -= demand[nextCity];
		        		cost+=distanceData[currentCity][nextCity];
		        		//System.out.println(currentCity+"->"+nextCity+" +Distance***="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
						currentCity = nextCity;
		        	}
		        	if(vehiclecapacity == 0 && j != CITY-1)//
		        	{
		        		cost+=distanceData[currentCity][0];
		        		vehiclecapacity = maxCapacity;
		        		//System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        		currentCity = 0;
		        	}
		        }
		        //System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        cost+=distanceData[currentCity][0];//cost go back to node 0
		        nextGen.put(cost,child2);
		        //System.out.println("Cost="+cost);
				/*System.out.println("startPos="+startPos+" EndPos="+endPos);
				j=0;
				for(i=0;i<parent1.size();i++)
				{
					if(i>= startPos && i<= endPos)
					{
						
						keytoValue.put(parent2.get(i),parent1.get(i));
						valuetoKey.put(parent1.get(i),parent2.get(i));
						//System.out.println("p1:"+parent1.get(i)+"p2:"+parent2.get(i));
						child1.set(i,parent2.get(i));
						//System.out.println("p1:"+parent1.get(i)+"p2:"+parent2.get(i));
						child2.set(i,parent1.get(i));
					}
				}
				System.out.println("keytoValue="+keytoValue);
				System.out.println("valuetoKey="+valuetoKey);
				for(i=0;i<parent1.size();i++)
				{
					if(keytoValue.containsKey(child1.get(i)) && (i< startPos || i> endPos))
					{
						child1.set(i,keytoValue.get(child1.get(i)));	
					}
				}
				for(i=0;i<parent2.size();i++)
				{
					if(valuetoKey.containsKey(child2.get(i)) && (i< startPos || i> endPos))
					{
						child2.set(i,valuetoKey.get(child2.get(i)));	
					}
				}*/
				


			    /*for (Integer gene: nextGen.keySet())//print nextGen
				{
		            String key =gene.toString();
		            String value = nextGen.get(gene).toString();  
		            System.out.println(key + " " + value);  
				}*/
			    /*ArrayList<ArrayList<Integer>> topTen = new ArrayList<ArrayList<Integer>>(currentGen.values());
			    for(ArrayList<Integer> route: topTen)
			    { 
			    	System.out.println(route);
			    }*/
		   	}
		   	i=0;
	   		//for(i=0;i<popNum-nextGen.size();i++)//random pick node with costs matching constraint(capacity)
		    while(nextGen.size()<popNum)
		    {
		    	vehiclecapacity = maxCapacity;
		    	currentCity = 0;
		    	//System.out.println("popNum:"+i);
		    	j=0;
		    	//for(j=0;j<CITY+1;j++)
		    	routes.add(new ArrayList<Integer>());
		    	while(routes.get(i).size()!=CITY)
		    	{
		    		costs.add(new Integer(0));
		    	//	System.out.println("Currentvehiclecapacity="+vehiclecapacity+"cost="+costs.get(i));
		        	index = rand.nextInt(nodeOnetoTen.size());
		        	nextCity = nodeOnetoTen.get(index);
		        	//System.out.println(nodeOnetoTen.size()+" "+index+" "+nextCity+" "+j);
		        	nodeOnetoTen.remove(index);

		        	/*while(alreadyNode.contains(nextCity))
		        	{
		        		nextCity = rand.nextInt(10) +  1;
		        	}
		        	alreadyNode.add(nextCity);*/
		        	if(vehiclecapacity - demand[nextCity] < 0)
		        	{
		        		vehiclecapacity = maxCapacity;
		        		costs.set(i,costs.get(i)+distanceData[currentCity][0]);
		        		//System.out.println(currentCity+"->"+0+" +Distance*="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        		currentCity = 0;
		        		vehiclecapacity -= demand[nextCity];
		        		costs.set(i,costs.get(i)+distanceData[currentCity][nextCity]);
						//System.out.println(currentCity+"->"+nextCity+" +Distance**="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
						routes.get(i).add(j,nextCity);//
						currentCity = nextCity;
		        		//costs.set(i,costs.get(i))+distanceData[currentCity][nextCity]));
						//if(i != CITY)
		        	}	        	
		        	else// > 0 remain
		        	{
		        		vehiclecapacity -= demand[nextCity];
		        		costs.set(i,costs.get(i)+distanceData[currentCity][nextCity]);
		        		//System.out.println(currentCity+"->"+nextCity+" +Distance***="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
						routes.get(i).add(j,nextCity);//
						currentCity = nextCity;
		        	}
		        	if(vehiclecapacity == 0 && j != CITY-1)//
		        	{
		        		costs.set(i,costs.get(i)+distanceData[currentCity][0]);
		        		vehiclecapacity = maxCapacity;
		        		//System.out.println(currentCity+"->"+nextCity+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity);
		        		currentCity = 0;
		        	}

		        	j++;	
		        }
		        costs.set(i,costs.get(i)+distanceData[currentCity][0]);//cost go back to node 0
		        nextGen.put(costs.get(i),routes.get(i));
		        //System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity);
		        nodeOnetoTen = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11));
		        i++; 
		    }
        	currentGen  = new TreeMap<Integer,ArrayList<Integer>>(nextGen);
        	nextGen.clear();
        	routes.clear();
        	costs.clear();
			i=0;
		}
		double endTime   = System.currentTimeMillis();
		for (Integer gene: currentGen.keySet())//print nextGen
		{
			if(i>=1)
				break;
            String key =gene.toString();
            String value = currentGen.get(gene).toString();  
	        System.out.println("=== Best Route Information ===");    
	        System.out.println(" ");
	        vehiclecapacity = maxCapacity;
	    	currentCity = 0;
	    	int cost=0;
	    	int round = 1;
	    	for(j=0;j< currentGen.get(gene).size();j++)
	    	{	
	    		nextCity =  currentGen.get(gene).get(j);
	        	if(vehiclecapacity - demand[nextCity] < 0)
	        	{
	        		vehiclecapacity = maxCapacity;
	        		cost+=distanceData[currentCity][0];
	        		System.out.println(currentCity+" -> "+0+"\t+ Distance *"+ "\t= "+distanceData[currentCity][0]+" \tDemand : "+demand[0]+" \tVehiclecapacity : "+vehiclecapacity+" \tCost = "+cost);
	        		currentCity = 0;
	        		vehiclecapacity -= demand[nextCity];
	        		cost+=distanceData[currentCity][nextCity];
					System.out.println(currentCity+" -> "+nextCity+"\t+ Distance **"+ "\t= "+distanceData[currentCity][nextCity]+" \tDemand : "+demand[nextCity]+" \tVehiclecapacity : "+vehiclecapacity+" \tCost = "+cost);
					
					currentCity = nextCity;
	        		
	        	}	        	
	        	else// > 0 remain
	        	{
	        		vehiclecapacity -= demand[nextCity];
	        		cost+=distanceData[currentCity][nextCity];
	        		System.out.println(currentCity+" -> "+nextCity+"\t+ Distance ***"+ "\t= "+distanceData[currentCity][nextCity]+" \tDemand : "+demand[nextCity]+" \tVehiclecapacity : "+vehiclecapacity+" \tCost = "+cost);
					currentCity = nextCity;
	        	}
	        	if(vehiclecapacity == 0 && j != CITY-1)//
	        	{
	        		cost+=distanceData[currentCity][0];
	        		vehiclecapacity = maxCapacity;
	        		System.out.println(currentCity+" -> "+0+"\t+ Distance ***"+ "\t= "+distanceData[currentCity][0]+" \tDemand : "+demand[0]+" \tVehiclecapacity : "+vehiclecapacity+" \tCost = "+cost);
	        		currentCity = 0;
	        	}
	        	round++;
	        }
	        cost+=distanceData[currentCity][0];//cost go back to node 0
	        System.out.println(currentCity+" -> "+0+"\t+ Distance ***"+ "\t= "+distanceData[currentCity][0]+" \tDemand : "+demand[0]+" \tVehiclecapacity : "+vehiclecapacity+" \tCost = "+cost);
	        //System.out.println("MinCost="+cost);
	        System.out.println(" ");
	       	System.out.println("=== Summary ===");
	       	System.out.println(" ");
	        System.out.println("Population : "+popNum+"\tGeneration : "+generation+"\n"+"CrossRate : "+crossRate+"\t\tMutatationRate : "+mutationRate+"\tMaxCapacity :"+maxCapacity);
	        System.out.println("MinCost : "+key +"\n"+ "Route : " + value);
	        double totalTime = endTime - startTime;
			System.out.println("Excution Time : " +(totalTime/1000));
	        System.out.println(" ");
	        i++;
	     }
	     //System.out.println(scores);
	     SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      }); 
	}
	 public CVRP(List<Float> best) {
      this.scores= best;
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scores.size() - 1);
      double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

      List<Point> graphPoints = new ArrayList<Point>();
      for (int i = 0; i < scores.size(); i++) {
         int x1 = (int) (i * xScale + BORDER_GAP);
         int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
         graphPoints.add(new Point(x1, y1));
      }

      // create x and y axes 
      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

      // create hatch marks for y axis. 
      for (int i = 0; i < Y_HATCH_CNT; i++) {
         int x0 = BORDER_GAP;
         int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
         int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
         int y1 = y0;
         g2.drawLine(x0, y0, x1, y1);
      }

      // and for x axis
      for (int i = 0; i < scores.size() - 1; i++) {
         int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
         int x1 = x0;
         int y0 = getHeight() - BORDER_GAP;
         int y1 = y0 - GRAPH_POINT_WIDTH;
         g2.drawLine(x0, y0, x1, y1);
      }

      Stroke oldStroke = g2.getStroke();
      g2.setColor(GRAPH_COLOR);
      g2.setStroke(GRAPH_STROKE);
      for (int i = 0; i < graphPoints.size() - 1; i++) {
         int x1 = graphPoints.get(i).x;
         int y1 = graphPoints.get(i).y;
         int x2 = graphPoints.get(i + 1).x;
         int y2 = graphPoints.get(i + 1).y;
         g2.drawLine(x1, y1, x2, y2);         
      }

      g2.setStroke(oldStroke);      
      g2.setColor(GRAPH_POINT_COLOR);
      for (int i = 0; i < graphPoints.size(); i++) {
         int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
         int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
         int ovalW = GRAPH_POINT_WIDTH;
         int ovalH = GRAPH_POINT_WIDTH;
         g2.fillOval(x, y, ovalW, ovalH);
      }
   }

	@Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }

   private static void createAndShowGui() {
      CVRP mainPanel = new CVRP(scores);

      JFrame frame = new JFrame("CVRP");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }
}