/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author arash
 */
public class Main {
    
    static public int[] a = { 1};
    static public int[] getA (){ return a; }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	// First line: # of rows
    	// Second line: # of columns
        // Walls -> #
    	// Character -> $
    	// Box -> o
    	// Target -> _
    	
    	if (args.length != 1) {
    		System.out.println("Error: missing map filename or more arguments than expected!");
    		System.exit(1);
    	}
    	
    	BufferedReader br = null;
    	try {
    		br = new BufferedReader(new FileReader(new File(args[0])));
    	}
    	catch (Throwable e) {
    		System.out.println("Error opening/reading the file: " + e.getMessage());
    		System.exit(1);
    	}
    	
    	Map.SquareType[][] map_array = null;
    	Point agent = null;
    	List<Point> boxes_list = null;
    	int targets = 0;
    	try {
        	int rows = Integer.parseInt(br.readLine());
        	int cols = Integer.parseInt(br.readLine());
        	
        	if (rows < 3 || cols < 3) {
        		System.out.println("Error: Number of columns or rows too little");
	    		System.exit(1);
        	}
        	
        	map_array = new Map.SquareType[rows][];
    		
	    	for (int i = 0; i < rows; i++) {
	    		map_array[i] = new Map.SquareType[cols];
	        	
        		String line = br.readLine();
        		for (int j = 0; j < line.length(); j++) {
        			switch (line.charAt(j)) {
        				case ' ':
        					map_array[i][j] = Map.SquareType.Empty;
                                        break;
        				case '#':
        					map_array[i][j] = Map.SquareType.Wall;
                                        break;
        				case '$':
        					if (agent != null) {
        						System.out.println("Error: Agent appears more than once in the map");
        			    		System.exit(1);
        					}
        					agent = new Point(i, j);
        					map_array[i][j] = Map.SquareType.Empty;
            				break;
        				case '_':
        					map_array[i][j] = Map.SquareType.Target;
        					++targets;
            				break;
        				case 'o':
        					if (boxes_list == null) {
        						boxes_list = new LinkedList<Point>();
        					}
        					boxes_list.add(new Point(i, j));
        					map_array[i][j] = Map.SquareType.Empty;
            				break;
        			}
        		}
        		for (int j = line.length(); j < cols; ++j) {
        			map_array[i][j] = Map.SquareType.Empty;
        		}
	    	}
    	}
    	catch (Throwable e) {
    		System.out.println("Error reading file");
    		System.exit(1);
    	}
    	
    	if (agent == null || boxes_list == null) {
    		System.out.println("Error: No agent or nor boxes where found");
    		System.exit(1);
    	}
    	if (targets != boxes_list.size()) {
    		System.out.println("Error: Number of boxes and number of targets don't match");
    		System.exit(1);
    	}
    	
    	Point[] boxes = new Point[boxes_list.size()];
    	for (int i = 0; i < boxes_list.size(); ++i) {
    		boxes[i] = boxes_list.get(i);
    	}
    	
    	Map map = new Map(map_array);
    	State state = new State(agent, boxes);
    	
    	// OUTPUT OF THE READ MAP
    	print_status(map, state);
        
        // Initialize game
        Sokoban game = new Sokoban(map, state);
        game.outputSolution();
        
    }
    
    private static void print_status(Map map, State state) {
    	Map.SquareType[][] map_array = map.map;
    	Point agent = state.getAgent();
    	Point[] boxes = state.getBoxes();
    	
    	for (int i = 0; i < map_array.length; ++i) {
    		for (int j = 0; j < map_array[i].length; ++j) {
    			switch (map_array[i][j]) {
	    			case Wall:
	    				System.out.print("#");
	    				break;
	    			case Empty:
	    				if (agent.x == i && agent.y == j) {
	    					System.out.print("$");
	    				}
	    				else {
	    					int k;
	    					for (k = 0; k < boxes.length; ++k) {
	    						if (boxes[k].x == i && boxes[k].y == j) {
	    							System.out.print("o");
	    							break;
	    						}
	    					}
	    					if (k == boxes.length) System.out.print(" ");
	    				}
	    				break;
	    			case Target:
	    				if (agent.x == i && agent.y == j) {
	    					System.out.print("$");
	    				}
	    				else {
	    					int k;
	    					for (k = 0; k < boxes.length; ++k) {
	    						if (boxes[k].x == i && boxes[k].y == j) {
	    							System.out.print("o");
	    							continue;
	    						}
	    					}
	    					if (k == boxes.length) System.out.print("_");
	    				}
	    				break;
    			}
    		}
    		System.out.println("");
    	}
    }
}
