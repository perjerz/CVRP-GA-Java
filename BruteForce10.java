import java.util.*;

class BruteForce10{
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
private static ArrayList<Integer> bestRoute;
private static ArrayList<ArrayList<Integer>> best;
private static int min = 10000;

    public static void bruteForceFindBestRoute
        (ArrayList<Integer> r,
         ArrayList<Integer> citiesNotInRoute)
    {
        if(!citiesNotInRoute.isEmpty())
        {
            for(int i = 0; i<citiesNotInRoute.size(); i++)
            {
                Integer justRemoved =
                    (Integer) citiesNotInRoute.remove(0);
                ArrayList<Integer> newRoute =
                    (ArrayList<Integer>) r.clone();
                newRoute.add(justRemoved);

                bruteForceFindBestRoute(newRoute, citiesNotInRoute);
                citiesNotInRoute.add(justRemoved);
            }
        }
        else //if(citiesNotInRoute.isEmpty())
        {
            if(isBestRoute(r))
                bestRoute = r;
        }

    }

    private static boolean isBestRoute(ArrayList<Integer> r) {
        //System.out.println(r.toString());
        int CITY = 10;
        //int min=10000;   
        int i=0,j=0;//
        int nextCity=0,currentCity=0;
        int maxCapacity = 200;
        int vehiclecapacity = maxCapacity;
        int index=0;
        int startPos=0,endPos;//startposition to swap and end
        int tempSwap=0;   
        vehiclecapacity = maxCapacity;
        currentCity = 0;
        int cost=0;
        int count=0;
        currentCity = 0;
        cost=0;
        j=0;
        vehiclecapacity = maxCapacity;
        for(Integer node : r)
        {
            nextCity =  node;
            if(vehiclecapacity - demand[nextCity] < 0)
            {
                vehiclecapacity = maxCapacity;
                cost+=distanceData[currentCity][0];
                /*if(routea.equals(Arrays.asList(4, 7, 2, 9, 5, 10, 3, 8, 1, 6)))
                System.out.println(currentCity+"->"+0+" +Distance*="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity+"cost="+cost);
                */
                currentCity = 0;
                vehiclecapacity -= demand[nextCity];
                cost+=distanceData[currentCity][nextCity];
                /*if(routea.equals(Arrays.asList(4, 7, 2, 9, 5, 10, 3, 8, 1, 6)))
                System.out.println(currentCity+"->"+nextCity+" +Distance**="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity+"cost="+cost);
                */
                currentCity = nextCity;
            }               
            else// > 0 remain
            {
                vehiclecapacity -= demand[nextCity];
                cost+=distanceData[currentCity][nextCity];
                /*if(routea.equals(Arrays.asList(4, 7, 2, 9, 5, 10, 3, 8, 1, 6)))
                System.out.println(currentCity+"->"+nextCity+" +Distance***="+distanceData[currentCity][nextCity]+"demand "+demand[nextCity]+"vehiclecapacity "+vehiclecapacity+"cost="+cost);
                */
                currentCity = nextCity;
            }
            if(vehiclecapacity == 0 && j != CITY-1)//
            {
                cost+=distanceData[currentCity][0];
                vehiclecapacity = maxCapacity;
                /*if(routea.equals(Arrays.asList(4, 7, 2, 9, 5, 10, 3, 8, 1, 6)))
                System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity+"cost="+cost);
                */
                currentCity = 0;
            }
            j++;
        }
        cost+=distanceData[currentCity][0];//cost go back to node 0
       /* if(routea.equals(Arrays.asList(4, 7, 2, 9, 5, 10, 3, 8, 1, 6)))
        System.out.println(currentCity+"->"+0+" +Distance***="+distanceData[currentCity][0]+"demand "+demand[0]+"vehiclecapacity "+vehiclecapacity+"cost="+cost);
        */
        //System.out.println("Cost "+cost+" Min "+min);
        if(cost<min)
        {
            min = cost;
            best.clear();
            best.add(r);
        }
        else if(cost == min)
        {
            best.add(r);
        }
        /*if(routea.equals(Arrays.asList(4, 7, 2, 9, 5, 10, 3, 8, 1, 6)))
        {
            System.out.println("4, 7, 2, 9, 5, 10, 3, 8, 1, 6 "+cost);
        }*/
        
        
        return false;
    }

    public static void main(String[] args) {
        best = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> lst = new ArrayList<Integer>();
        for (int i = 1; i < 11; ++i)
            lst.add(i);
        ArrayList<Integer> route = new ArrayList<Integer>();
        double startTime   = System.currentTimeMillis();
        bruteForceFindBestRoute(route, lst);
        double endTime   = System.currentTimeMillis();
        double totalTime = endTime - startTime;
        System.out.println("Excution Time : " +(totalTime/1000));
        // Java statements
        //System.out.println("Time="+(double)(System.nanoTime() - start)/1000000000.0);
        System.out.println("Min cost="+min);
        System.out.println("All Routes =");
        int CITY = 10;
        //int min=10000;   
        int i=0,j=0;//
        int nextCity=0,currentCity=0;
        int maxCapacity = 200;
        int vehiclecapacity = maxCapacity;
        int index=0;
        int startPos=0,endPos;//startposition to swap and end
        int tempSwap=0;   
        vehiclecapacity = maxCapacity;
        currentCity = 0;
        int cost=0;
        int count=0;
        currentCity = 0;
        cost=0;
        j=0;
        for(ArrayList<Integer> routeb : best)
        {
            System.out.println(routeb);
            vehiclecapacity = maxCapacity;
            currentCity = 0;
            cost=0;
            int round = 1;
            for(j=0;j< routeb.size();j++)
            {   
                nextCity =  routeb.get(j);
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
            
        }
    }
}