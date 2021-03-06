/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author arash
 */
public class Sokoban {

    private boolean isFinal(State s0) {
        for (Point box : s0.getBoxes()) {
            if(readMap(box) != Map.SquareType.Target){
                return false;
            }
        }
        return true;
    }

    public Map gameMap;

    private Map.SquareType readMap(Point p) {
        return gameMap.map[p.y][p.x];
    }
    private State initState;

    public List<Move> solve() {
        State s = bfs();
        List<Move> moves = new ArrayList<Move>();
        while(!s.equals(initState)){
            Move m = moveMade.get(s);
            moves.add(m);
            Main.print_status(gameMap, s);            
            System.out.println(m);
            s = cameFrom.get(s);
        }
        
        Main.print_status(gameMap, s);            
        System.out.println("Took " + moves.size() + " steps in total.");
        
        return moves;
    }
    
    HashMap<State, Move> moveMade;
    HashMap<State, State> cameFrom ;

    private State bfs() {
        // this is not memoized!
        moveMade = new HashMap<State, Move>();
        cameFrom = new HashMap<State, State>();
        Queue<State> q = new LinkedList<State>();
        q.add(initState);
        while (!q.isEmpty()) {
            State s0 = q.remove();
            //Main.print_status(gameMap, s0);
            if(isFinal(s0)){
                System.out.println("Final move, searched states: " + cameFrom.size());
                return s0;
            }
            for (Move m : Move.directions()) {
                State s = applyMove(s0, m);
                if (isLegit(s) && !moveMade.containsKey(s)) {
                    moveMade.put(s, m);
                    cameFrom.put(s, s0);
                    q.add(s);
                   // System.out.println("added to queue");
                }
            }
        }
        System.err.println("NOOOS");
        return null;
    }

    private boolean isLegit(State s0) {
        //State s = applyMove(s0, m); //Is this right?? Do we need to apply move again???
        Set<Point> all = new HashSet<Point>(s0.getBoxes());
        all.add(s0.getAgent());
        
        if(all.size() < initState.getBoxes().size() + 1){
            return false;
        }
        for (Point p : all) {
            //System.out.println("Move: " + p);
            /*if(p.y >= getMap().length || p.x >= getMap()[0].length || p.y < 0 || p.x < 0){
               // System.out.println("Move was not legit!");
                return false;
            }*/
            if (readMap(p).equals(Map.SquareType.Wall)) {
                //System.out.println("WALL! move not legit");
                return false;
            }
        }
        
        // Now also call "dead-end" states as not legit
        for (Point point : s0.getBoxes()) {
            if(gameMap.deadEndForBoxes(point)){
                return false;
            }
        }
        
        return true;
    }

    private State applyMove(State s, Move m) {
        Point agent = s.getAgent();
        Point nextLocation = m.movePoint(agent);
        Set<Point> newBoxes = new HashSet<Point>(s.getBoxes());
        
        if(newBoxes.contains(nextLocation)){
            newBoxes.remove(nextLocation);
            newBoxes.add(m.movePoint(nextLocation));
        }
        return new State(nextLocation, newBoxes);
    }

    
    public Sokoban(Map m, State s){
        gameMap = m;
        initState = s;
    }
    
    public void outputSolution(){
    }
    


}
 