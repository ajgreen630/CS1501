/*
***************************************************************************************************
* Compilation: javac AirlineGraph.java
* Execution: java AirlineGraph filename.txt
* Dependencies: BreadFirstPaths.java DepthFirstSearch.java DijkstraDistSP.java DijkstraPrice.java
*               EagerPrimMST.java Edge.java IndexMinPQ.java Queue.java StdIn.java StdOut.java
*               UF.java WeightedGraph.java
* 
* Main program for simulating an airline database that the user can interact with.
***************************************************************************************************
*/
import java.io.*;
import java.util.*;
public class AirlineGraph {
    /*
    *    Main entry point for the program. 
    */
    public static void main(String[] args) throws IOException {
        /* Load a file (whose name was input by the user) a
           list of all of the service routes that your airline
           runs. */
        Scanner fileScan = new Scanner(new FileInputStream(args[0]));   // For reading the file.
        Scanner inScan = new Scanner(System.in);    // For user input.

        int V = Integer.parseInt(fileScan.nextLine());     // Get the number of vertices for the graph.
        WeightedGraph ALGraph = new WeightedGraph(V);   // Create Graph of V vertices.
        ArrayList<String> cities = new ArrayList<String>(V);    // Create corresponding array list of city names.

        /* Read through V lines to fill the list of cities: */
        for(int i = 0; i < V; i++) {
            String name = fileScan.nextLine();
            cities.add(name);
        }
        ArrayList<String[]> table = new ArrayList<String[]>();
        /* Read through the rest of the file to create the
           edges for the graph: */
        while(fileScan.hasNext()) {
            String route = fileScan.nextLine();     // Get the route.
            String [] data = route.split(" ");      // Split the data up by whitespace.
            
            /* Parse the data into the proper arguments for Edge: */
            int v = Integer.parseInt(data[0]) - 1;      // vertex v
            int w = Integer.parseInt(data[1]) - 1;      // vertex w
            int distance = Integer.parseInt(data[2]);   // distance of route
            double price = Double.parseDouble(data[3]); // price for trip

            Edge e = new Edge(v, w, distance, price);   // Create an Edge out of the extracted data.
            ALGraph.addEdge(e);     // Add the edge to the Graph.
        }

        Iterable<Edge> myEdgesItr = ALGraph.edges();
        ArrayList<Edge> myEdges = new ArrayList<Edge>();
        for(Edge e : myEdgesItr) {
            int v = e.either();
            int w = e.other(v);
            double distance = e.weight();
            double price = e.price();

            table.add(new String[] {"| " + cities.get(v) + " to " + cities.get(w), "| " + String.format("%.0f", distance), "| " + String.format("$%.2f", price) + " |"});  // Add the route to the table.
        }

        System.out.println("\nWelcome to Algorithm Airlines!");
        System.out.println("You are working with the file " + args[0] + ".\n");
        int option = -1;
        while (option != 7) {
            printMainMenu();
            do
            {   
                try {
                    System.out.print("\nWhat would you like to do? ");
                    option = inScan.nextInt();
                    if(option <= 0 && option > 7) {
                        System.out.println("Invalid entry! ");
                        inScan.nextLine();
                        System.out.println();
                        printMainMenu();
                        System.out.print("\nWhat would you like to do? ");
                        option = inScan.nextInt();
                    }
                }
                catch (InputMismatchException e) {
                    System.out.println("Invalid entry! ");
                    inScan.nextLine();
                    System.out.println();
                    printMainMenu();
                    System.out.print("\nWhat would you like to do? ");
                    option = inScan.nextInt();
                }
            } while (option <= 0 || option > 7);

            switch (option) {
                case 1:     // DONE!
                    printTable(table);
                    break;
                case 2: // DONE!
                    EagerPrimMST mst = new EagerPrimMST(ALGraph);
                    System.out.println("\nMinimum Distance Spanning Tree (Indexed from 0):");
                    for(Edge e : mst.edges()) {
                        System.out.println(e.toString());
                    }
                    System.out.println("\nTotal Weight by Distance: " + mst.weight() + "\n");
                    break;
                case 3: // DONE!
                    String subopt;
                    char suboptChar;
                    String sourceStr;
                    String destinationStr;
                    int source;
                    int destination;

                    printSubMenu();
                    inScan.nextLine();
                    subopt = inScan.nextLine().trim();

                    while(!subopt.equals("a") && !subopt.equals("b") && !subopt.equals("c")) {
                        System.out.println("\n      Invalid entry!  Please enter a valid choice.");
                        printSubMenu();
                        subopt = inScan.nextLine().trim();
                    }
                    suboptChar = subopt.charAt(0);
                    System.out.print("      Enter source name: ");
                    sourceStr = inScan.nextLine().trim();
                    while(!cities.contains(sourceStr)) {
                        System.out.println("      " + sourceStr + " is not in the system!");
                        System.out.print("\n      Enter source name: ");
                        sourceStr = inScan.nextLine().trim();
                    }
                    System.out.print("      Enter destination name: ");
                    destinationStr = inScan.nextLine().trim();
                    while(!cities.contains(destinationStr)) {
                        System.out.println("      " + destinationStr + " is not in the system!");
                        System.out.print("\n      Enter destination name: ");
                        destinationStr = inScan.nextLine().trim();
                    }
                    source = cities.indexOf(sourceStr);
                    destination = cities.indexOf(destinationStr);
                    
                    switch (suboptChar) {
                        case 'a':   // Shortest path based on total miles (works bidirectionally) from source to destination. // DONE!
                            DijkstraDistSP sp = new DijkstraDistSP(ALGraph, source);
                            Iterable<Edge> distPathItr = sp.pathTo(destination);
                            Stack<Edge> distPath = new Stack<Edge>();
                            if(!sp.hasPathTo(destination)) {
                                System.out.println("     No such path!");
                                break;
                            }
                            for(Edge e : distPathItr) {
                                distPath.push(e);
                            }
                            System.out.println("\n      Shortest distance path from " + cities.get(source) + " to " + cities.get(destination) + ":");
                            StringBuilder sb = new StringBuilder();
                            sb.append("      ");
                            int eDistance = 0;
                            int either = source;
                            while(!distPath.isEmpty()) {
                                Edge place = distPath.pop();
                                int other = place.other(either);
                                sb.append(cities.get(either));
                                sb.append(" ");
                                sb.append(place.weight());
                                sb.deleteCharAt(sb.length()-1);
                                sb.deleteCharAt(sb.length()-1);
                                sb.append(" ");
                                eDistance += place.weight();
                                either = other;
                            }
                            sb.deleteCharAt(sb.length()-1);
                            sb.append(" " + destinationStr + ": Total = " + eDistance + "\n");
                            System.out.println(sb);
                            break;

                        case 'b':   // Shortest path based on price from source to destination. // DONE!
                            DijkstraPriceSP pricesp = new DijkstraPriceSP(ALGraph, source);
                            Iterable<Edge> pricePathItr = pricesp.pathTo(destination);
                            Stack<Edge> pricePath = new Stack<Edge>();
                            if(!pricesp.hasPathTo(destination)) {
                                System.out.println("     No such path!");
                                break;
                            }
                            for(Edge e : pricePathItr) {
                                pricePath.push(e);
                            }
                            System.out.println("\n      Shortest price path from " + cities.get(source) + " to " + cities.get(destination) + ":");
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("      ");
                            int ePrice = 0;
                            int either2 = source;
                            while(!pricePath.isEmpty()) {
                                Edge place = pricePath.pop();
                                int other2 = place.other(either2);
                                System.out.println(other2);
                                sb2.append(cities.get(either2));
                                sb2.append(" ");
                                sb2.append(place.price());
                                sb2.deleteCharAt(sb2.length()-1);
                                sb2.deleteCharAt(sb2.length()-1);
                                sb2.append(" ");
                                ePrice += place.price();
                                either2 = other2;
                            }
                            sb2.deleteCharAt(sb2.length()-1);
                            sb2.append(": Total = " + ePrice + "\n");
                            System.out.println(sb2);
                            break;
                        case 'c':   // Shortest number of hops from source to destination.  DONE!
                            BreadthFirstPaths bfs = new BreadthFirstPaths(ALGraph, source);
                            Iterable<Integer> pathItr = bfs.pathTo(destination);
                            Stack<Integer> path = new Stack<Integer>();
                            for(Integer item : pathItr) {
                                path.push(item);
                            }
                            System.out.println("\n      Shortest hops from " + cities.get(source) + " to " + cities.get(destination) + ":");
                            System.out.print("      ");
                            int hops = 0;
                            while(!path.isEmpty()) {
                                int place = path.pop();
                                if(path.size() == 0) {
                                    System.out.print(cities.get(place) + ": Total = " + hops + "\n");
                                }
                                else {
                                    System.out.print(cities.get(place) + " ");
                                    hops++;
                                }
                            }
                            System.out.println();
                            break;
                    }
                    break;
                case 4: // Given a dollar amount, print out all trips whose cost is less than or equal to
                        // that amount.     DONE!
                    System.out.print("Enter a dollar amount : $");  // Get the dollar amount.
                    double dollar = inScan.nextDouble();
                    ArrayList<String[]> list = new ArrayList<String[]>();
                    DepthFirstSearch search = new DepthFirstSearch(ALGraph, 0);     // Perform a depth-first search for all trips costing less than or equal to the dollar amount.
                    for (int v = 0; v < ALGraph.V(); v++) {
                        for (Edge e : ALGraph.adj(v)) {
                            if(e.price() <= dollar && search.marked(v) && !e.marked()) {    // Added a marked method in Edge for pruning mentioned in the assignment sheet to reduce possible exponential run-time.
                                list.add(new String[] {"| " + cities.get(e.either()) + " to " + cities.get(e.other(e.either())), " | " + String.format("$%.2f", e.price()) + " |"});
                                e.setMark(true);
                            }
                        }
                    }
                    for (int v = 0; v < ALGraph.V(); v++) {     // Reset all of the marked-tags for the edges so DFS does not affect future cases.
                        for (Edge e : ALGraph.adj(v)) {
                            e.setMark(false);
                        }
                    }
                    if(list.isEmpty()) {    // If the user enters a dollar amount in which no trips are less than or equal to in cost.
                        System.out.println("\nThere aren't any trips under " + String.format("$%.2f", dollar) + "!\n");
                        break;
                    }
                    // Print out the resulting table of trips and their costs.
                    System.out.println("\nTrips less than or equal to " + String.format("$%.2f", dollar) + ":");
                    System.out.println(" ---------------------------------------------- ");
                    System.out.format("%-36s%-15s\n", "| Trip                           ", "| Price   |");
                    System.out.println(" ---------------------------------------------- ");
                    for(String[] row : list) {
                        System.out.format("%-35s%-15s\n", (Object[]) row);
                    }
                    System.out.println(" ----------------------------------------------\n");
                    break;
                case 5: // Add a new route to the schedule. Assume that both cities aleady exist.   DONE!
                    inScan.nextLine();  // Clear buffer. 
                    String sourceStr2;
                    String destinationStr2;
                    double distance2;
                    double price2;
                    int source2;
                    int destination2;

                    System.out.print("Enter source name: ");    // Get the source.
                    sourceStr2 = inScan.nextLine().trim();
                    while(!cities.contains(sourceStr2)) {   // Source must exist within the system.
                        System.out.println(sourceStr2 + " is not in the system!");
                        System.out.print("Enter source name: ");
                        sourceStr2 = inScan.nextLine().trim();
                    }
                    System.out.print("Enter destination name: ");   // Get the destination.
                    destinationStr2 = inScan.nextLine().trim();
                    while(!cities.contains(destinationStr2)) {  // Destination must exist within the system.
                        System.out.println(destinationStr2 + " is not in the system!");
                        System.out.print("Enter destination name: ");
                        destinationStr2 = inScan.nextLine().trim();
                    }
                    System.out.print("Enter distance: ");   // Get the distance.
                    distance2 = inScan.nextDouble();
                    while(distance2 < 0) {  // Distance must be nonnegative.
                        System.out.println("Distance must be nonnegative!");
                        System.out.print("\nEnter distance: ");
                        distance2 = inScan.nextDouble();
                    }
                    System.out.print("Enter price: $"); // Get the price.
                    price2 = inScan.nextDouble();
                    while(price2 < 0) {     // Price must be nonnegative.
                        System.out.println("Price must be nonnegative!");
                        System.out.print("\nEnter price: $");
                        price2 = inScan.nextDouble();
                    }
                    source2 = cities.indexOf(sourceStr2);   // Get the vertex number of the source.
                    destination2 = cities.indexOf(destinationStr2); // Get the vertex number of the destination.

                    // I don't know why I re-declared everything but it's staying because I'm not risking a typo-causing error that I won't be able to find later.
                    int v = source2;      // vertex v
                    int w = destination2;      // vertex w
                    double distance = distance2;   // distance of route
                    double price = price2; // price for trip

                    if(ALGraph.hasEdge(v, w)) {     // If the graph already contains this edge.
                        inScan.nextLine();
                        System.out.println("This route is already in the system!");
                        System.out.print("Would you like to update the distance and price with the data you entered? (Y or N): ");    // Give the option to update the distance and price of the existing route.
                        String yOrN = inScan.nextLine().trim();
                        while(!yOrN.equals("Y") && !yOrN.equals("y") && !yOrN.equals("N") && !yOrN.equals("n")) {
                            System.out.print("Invalid entry! Re-enter your choice (Y or N): ");
                            yOrN = inScan.nextLine().trim();
                        }
                        if(yOrN.equals("Y") || yOrN.equals("y")) {
                            Edge e = new Edge(v, w, distance, price);
                            ALGraph.removeEdge(v, w);
                            ALGraph.removeEdge(w, v);
                            ALGraph.addEdge(e);
                            table.clear();
                            myEdgesItr = ALGraph.edges();
                            myEdges = new ArrayList<Edge>();
                            for(Edge myE : myEdgesItr) {
                                v = myE.either();
                                w = myE.other(v);
                                distance = myE.weight();
                                price = myE.price();
                                table.add(new String[] {"| " + cities.get(v) + " to " + cities.get(w), "| " + String.format("%.0f", distance), "| " + String.format("$%.2f", price) + " |"});  // Add the route to the table.
                            }
                            System.out.println("\nUpdated the distance and price for the route " + sourceStr2 + " to " + destinationStr2 + ".");
                            System.out.println("If you entered the route in reverse order of the route in the table, the route has been updated to reflect the order of "
                                                + "\nthe source and destination you entered.");
                            System.out.println();
                            break;
                        }
                        else {
                            System.out.println("Existing route will remain unchanged.");
                            System.out.println();
                            break;
                        }
                    }
                    else {  // Otherwise, assume this is a new route.
                        Edge e = new Edge(v, w, distance, price);   // Create an Edge out of the extracted data.
                        ALGraph.addEdge(e);     // Add the edge to the Graph.
                        table.clear();
                        myEdgesItr = ALGraph.edges();
                        myEdges = new ArrayList<Edge>();
                        for(Edge myE : myEdgesItr) {
                            v = myE.either();
                            w = myE.other(v);
                            distance = myE.weight();
                            price = myE.price();
                            table.add(new String[] {"| " + cities.get(v) + " to " + cities.get(w), "| " + String.format("%.0f", distance), "| " + String.format("$%.2f", price) + " |"});  // Add the route to the table.
                        }
                        System.out.println();
                        break;
                    }
                case 6:
                    inScan.nextLine();  // Clear the buffer.
                    System.out.println("NOTE: Routes are bidirectional. The order of the source and destination you enter does not matter.");
                    System.out.print("Enter source name: ");    // Get the source.
                    sourceStr2 = inScan.nextLine().trim();
                    while(!cities.contains(sourceStr2)) {   // Source must exist within the system.
                        System.out.println(sourceStr2 + " is not in the system!");
                        System.out.print("Enter source name: ");
                        sourceStr2 = inScan.nextLine().trim();
                    }
                    System.out.print("Enter destination name: ");   // Get the destination.
                    destinationStr2 = inScan.nextLine().trim();
                    while(!cities.contains(destinationStr2)) {  // Destination must exist within the system.
                        System.out.println(destinationStr2 + " is not in the system!");
                        System.out.print("Enter destination name: ");
                        destinationStr2 = inScan.nextLine().trim();
                    }
                    source2 = cities.indexOf(sourceStr2);   // Get the vertex number of the source.
                    destination2 = cities.indexOf(destinationStr2); // Get the vertex number of the destination.
                    if(!ALGraph.hasEdge(source2, destination2) || !ALGraph.hasEdge(destination2, source2)) {
                        System.out.println("This route does not exist!");
                        System.out.println();
                        break;
                    }
                    else {
                        ALGraph.removeEdge(source2, destination2);
                        ALGraph.removeEdge(destination2, source2);
                        table.clear();
                        myEdgesItr = ALGraph.edges();
                        myEdges = new ArrayList<Edge>();
                        for(Edge e : myEdgesItr) {
                            v = e.either();
                            w = e.other(v);
                            distance = e.weight();
                            price = e.price();
                            table.add(new String[] {"| " + cities.get(v) + " to " + cities.get(w), "| " + String.format("%.0f", distance), "| " + String.format("$%.2f", price) + " |"});  // Add the route to the table.
                        }
                        System.out.println("Removed the route " + sourceStr2 + " to " + destinationStr2 + " from the system.");
                        System.out.println();
                        break; 
                    }
                case 7:
                    StringBuilder newData = new StringBuilder();
                    newData.append(ALGraph.V() + "\n");
                    for(int i = 0; i < cities.size(); i++){
                        newData.append(cities.get(i) + "\n");
                    }
                    myEdgesItr = ALGraph.edges();
                    myEdges = new ArrayList<Edge>();
                    for(Edge e : myEdgesItr) {
                        v = e.either();
                        w = e.other(v);
                        v++;
                        w++;
                        distance = e.weight();
                        price = e.price();
                        newData.append(v + " " + w + " " + String.format("%.0f", distance) + " " + String.format("%.2f", price) + "\n");
                    }
                    newData.deleteCharAt(newData.length()-1);
                    System.out.println("Here is the updated data to be written to the file " + args[0] + " (edges are indexed from 1 to match file format):");
                    System.out.println(newData);
                    boolean appendFile = false;
                    File myFile = new File(args[0]);
                    try {
                    FileWriter myWriter = new FileWriter(myFile, appendFile);
                    myWriter.write(newData.toString());
                    myWriter.close();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\nNew data has overwritten the data in " + args[0] + ". Exiting program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nInvalid menu option. Please choose again.\n");
            }
        }
        System.out.println("\nExiting program. Goodbye!");
    }

    /**
    * Prints the main menu for the user options in the console.
    */
    public static void printMainMenu() {
        System.out.println(" 1)  Show the entire list of direct bidirectional routes, distances and prices.");
        System.out.println(" 2)  Display a minimum spanning tree for the services routes based on distances.");
        System.out.println(" 3)  Search for a type of shortest path.");
        System.out.println(" 4)  Display all trips whose cost is less than or equal to a given dollar amount.");
        System.out.println(" 5)  Add a new route to the schedule.");
        System.out.println(" 6)  Remove a route from the schedule.");
        System.out.println(" 7)  Quit the program.");
    }

    /**
    * Prints a table of routes and their distances and prices in a well-formatted manner.
    * 
    * @param table the table of String arrays containing the information of each edge in the graph.
    */
    public static void printTable(ArrayList<String[]> table) {
        System.out.println();
        System.out.println(" ----------------------------------------------------------- ");
        System.out.println("|       Table of Direct Routes, Distances, and Prices       |");
        System.out.println("|-----------------------------------------------------------|");
        System.out.format("%-35s%-15s%-17s\n", "| Route", "| Distance", "| Price   |");
        System.out.println("|-----------------------------------------------------------|");
        for(String[] row : table) {
            System.out.format("%-35s%-15s%-15s\n", (Object[]) row);
        }
        System.out.println(" ----------------------------------------------------------- \n");
    }
    /**
    * Prints the sub-menu of actions for option 3) in the main menu for the user to choose from.
    */
    public static void printSubMenu() {
        System.out.println("\n      Which type of search would you like to perform?");
        System.out.println("      a) Shortest path based on total miles from source to destination.");
        System.out.println("      b) Shortest path based on price from source to destination.");
        System.out.println("      c) Shortest path based on number of hops from source to destination.");
        System.out.print("      Enter your choice (a, b, or c): ");
    }
}