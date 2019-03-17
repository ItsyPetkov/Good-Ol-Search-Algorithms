import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Practical1 {
	
	private static List<String> path = new ArrayList<String>(Arrays.asList("MI","MII"));
	
    public static List<String> nextStates(String s) {
    	
        List<String> result = new ArrayList<String>();
        
        if (s.endsWith("I")) {
            String copy = s;
            copy +=("U");
            result.add(copy);
        }
        
        if (s.startsWith("M")) {
            String copy = s;
            copy += s.substring(1);
            result.add(copy);
        }
        
        if (s.contains("III")) {
            @SuppressWarnings("unused")
			int index = s.indexOf("III", 0);
            for (int i = 0; i < s.length()-2; i++) {
                String start = s.substring(0, i);
                String end = s.substring(i);
                if (end.contains("III")) {
                    end = end.replaceFirst("III", "U");
                    String copy = s;
                    copy = start + end;
                    if (!result.contains(copy)) {
                        result.add(copy);
                    }
                }
            }
        }
        
        if (s.contains("UU")) {
            @SuppressWarnings("unused")
			int index = s.indexOf("UU", 0);
            for (int i = 0; i < s.length()-2; i++) {
                String start = s.substring(0, i);
                String end = s.substring(i);
                if (end.contains("UU")) {
                    end = end.replaceFirst("UU", "");
                    String copy = s;
                    copy = start + end;
                    if (!result.contains(copy)) {
                        result.add(copy);
                    }
                }
            }
        }
        
        return result;
    }
    
    public static List<List<String>> extendPath(List<String> path){
    	List<List<String>> extendedPath = new ArrayList<List<String>>();
    	String last = path.get(path.size() - 1);
    	List<String> elements = new ArrayList<String>();
    	
    	elements = nextStates(last);
    	for(int i = 0 ; i < elements.size(); i++) {
    		List<String> newPath = new ArrayList<String>(path);
    		newPath.add(elements.get(i));
    		extendedPath.add(newPath);
    	}
    	
    	return extendedPath;
    }
    
    public static List<String> breadthFirstSearch(String goalString){
    	int extendedPathCounter = 0;
    	Queue<List<String>> agenda = new LinkedList<>();
    	List<String> initialSettings = new ArrayList<String>(Arrays.asList("MI"));
    	List<String> empty = new LinkedList<String>();
    	agenda.add(initialSettings);
    	
    	while(!agenda.isEmpty() || extendedPathCounter < 15) {
    		List<String> currentPath = agenda.poll();
    		if(currentPath.get(currentPath.size() - 1).equals(goalString)) {
    			System.out.println("Size of current path " + currentPath.size());
    			System.out.println("Number of times extendedPath got called " + extendedPathCounter);
    			System.out.println("Size of the agenda " + agenda.size());
    			return currentPath;
    		}
    		else {
    			for(List<String> newPath: extendPath(currentPath)) {
    				agenda.add(newPath);
    			}
    			extendedPathCounter++;
    		}
    	}
    	return empty;
    }
    
    public static List<String> depthLimitedDFS(String goalString, int limit) {
    	int extendedPathCounter = 0;
    	Deque<List<String>> agenda = new LinkedList<List<String>>();
    	List<String> initialSettings = new ArrayList<String>(Arrays.asList("MI"));
    	List<String> empty = new LinkedList<String>();
    	agenda.add(initialSettings);
    	
    	while(!agenda.isEmpty()) {
    		List<String> currentPath = agenda.pop();
    		if(currentPath.get(currentPath.size() - 1).equals(goalString)) {
    			System.out.println("Size of current path " + currentPath.size());
    			System.out.println("Number of times extendPath got called " + extendedPathCounter);
    			System.out.println("Size of the agenda " + agenda.size());
    			return currentPath;
    		}else {
    			if(extendedPathCounter < limit) {
    				agenda.addAll(extendPath(currentPath));
    				extendedPathCounter++;
    			}
    		}
    	}
    	return empty;
    }
    
    public static List<String> iterativeDeepening(String goalString){
    	int counter = 2;
    	boolean running = false;
    	List<String> solution = new LinkedList<String>();
    	for(counter = 2; !running; counter++) {
    		solution.addAll(depthLimitedDFS(goalString, counter));
    		if(!solution.isEmpty()) {
    			running = true;
    		}
    	}
    	return solution;
    }
    
    public static List<String> breadthFirstSearchWithHashMap(String sourceString ,String goalString){
    	
    	int extendedPathCounter = 0;
    	Map<String, String> map = new HashMap<String, String>();
    	List<String> agenda = new ArrayList<String>();
    	List<String> currentPath = new ArrayList<String>();
    	List<String> empty = new LinkedList<String>();
    	String initialString = "MI";
    	agenda.add(initialString);
    	map.put("MI", "null");
    	
    	while(!agenda.isEmpty()) {
    		String currentString = agenda.get(0);
    		agenda.remove(0);
    		if(currentString.equals(goalString)) {
    			currentPath.add(currentString);
    			while(!map.get(currentPath.get(0)).equals("null")) {
    				currentPath.add(0, map.get(currentPath.get(0)));
    			}
    			System.out.println("Size of current path " + currentPath.size());
    			System.out.println("Number of times extendPath got called " + extendedPathCounter);
    			System.out.println("Size of the agenda " + agenda.size());
    			return currentPath;
    		}
    		
    		extendedPathCounter++;
    		List<String> next = nextStates(currentString);
    		
    		for(int i = 0; i < next.size(); i++) {
    			map.putIfAbsent(next.get(i), currentString);
    			if(!agenda.contains(next.get(i))) {
    				agenda.add(next.get(i));
    			}
    		}
    	}
    	return empty;
    }
    
    public static int estimateSteps(String currentString, String goalString) {
    	if(currentString != goalString) {
    		return 1;
    	}
    	return 0;
    }
    
    public static List<String> bestFirstSearch(String goalString){
    	int extendedPathCounter = 0;
    	List<List<String>> agenda = new LinkedList<>();
    	List<String> initialSettings = new ArrayList<String>(Arrays.asList("MI"));
    	List<String> empty = new LinkedList<String>();
    	agenda.add(initialSettings);
    	
    	while(!agenda.isEmpty() || extendedPathCounter < 15) {
    		
    		List<Integer> agendaIndex = new ArrayList<Integer>();
    		
    		for(List<String> currentList: agenda) {
    			int estimate = 0;
    			for(String currentString: currentList) {
    				estimate += estimateSteps(currentString, goalString);
    			}
    			agendaIndex.add(estimate);
    		}
    		
    		int index = Collections.min(agendaIndex);
    		
    		List<String> currentPath = agenda.remove(index - 1);
    		if(currentPath.get(currentPath.size() - 1).equals(goalString)) {
    			System.out.println("Size of current path " + currentPath.size());
    			System.out.println("Number of times extendedPath got called " + extendedPathCounter);
    			System.out.println("Size of the agenda " + agenda.size());
    			return currentPath;
    		}
    		else {
    			for(List<String> newPath: extendPath(currentPath)) {
    				agenda.add(newPath);
    			}
    			extendedPathCounter++;
    		}
    	}
    	return empty;
    }
    
    public static int pathSoFar(String initial, String current) {
    	int extendedPathCounter = 0;
    	Queue<List<String>> agenda = new LinkedList<>();
    	List<String> initialSettings = new ArrayList<String>(Arrays.asList("MI"));
    	List<String> empty = new LinkedList<String>();
    	agenda.add(initialSettings);
    	
    	while(!agenda.isEmpty() || extendedPathCounter < 15) {
    		List<String> currentPath = agenda.poll();
    		if(currentPath.get(currentPath.size() - 1).equals(current)) {
    			return currentPath.size() - 1;
    		}
    		else {
    			for(List<String> newPath: extendPath(currentPath)) {
    				agenda.add(newPath);
    			}
    			extendedPathCounter++;
    		}
    	}
    	return empty.size();
    }
    
    public static List<String> aStarSearch(String goalString){
    	int extendedPathCounter = 0;
    	List<List<String>> agenda = new LinkedList<>();
    	List<String> initialSettings = new ArrayList<String>(Arrays.asList("MI"));
    	List<String> empty = new LinkedList<String>();
    	agenda.add(initialSettings);
    	
    	while(!agenda.isEmpty() || extendedPathCounter < 15) {
    		
    		List<Integer> agendaIndex = new ArrayList<Integer>();
    		
    		for(List<String> currentList: agenda) {
    			int estimate = 0;
    			for(String currentString: currentList) {
    				estimate = pathSoFar("MI", currentString) + estimateSteps(currentString, goalString);
    			}
    			agendaIndex.add(estimate);
    		}
    		
    		int index = Collections.min(agendaIndex);
    		
    		List<String> currentPath = agenda.remove(index - 1);
    		if(currentPath.get(currentPath.size() - 1).equals(goalString)) {
    			System.out.println("Size of current path " + currentPath.size());
    			System.out.println("Number of times extendedPath got called " + extendedPathCounter);
    			System.out.println("Size of the agenda " + agenda.size());
    			return currentPath;
    		}
    		else {
    			for(List<String> newPath: extendPath(currentPath)) {
    				agenda.add(newPath);
    			}
    			extendedPathCounter++;
    		}
    	}
    	return empty;
    }

    public static void main(String[] args){
    	System.out.println("---------------------Practical1--------------------------------");
        System.out.println(nextStates("MI"));
        System.out.println(nextStates("MIU"));
        System.out.println(nextStates("MUI"));
        System.out.println(nextStates("MIIII"));
        System.out.println(nextStates("MUUII"));
        System.out.println(nextStates("MUUUI"));
        System.out.println(nextStates("MIIIIIII"));
        System.out.println("----------------------Practical2-------------------------------");
        System.out.println(extendPath(path));
        System.out.println("--------------------------BFS----------------------------------");
        System.out.println(breadthFirstSearch("MIIIIU"));
        System.out.println("-----------------------DFSLimited------------------------------");
        System.out.println(depthLimitedDFS("MIUIUIUIU", 100));
        System.out.println("---------------------IterativeDeepening------------------------");
        System.out.println(iterativeDeepening("MIUIUIUIU"));
        System.out.println("----------------------Practical3-------------------------------");
        System.out.println(breadthFirstSearchWithHashMap("MI", "MIIIIU"));
        System.out.println("----------------------BestFirstSearch--------------------------");
        System.out.println(bestFirstSearch("MIIIIIIIIIIIIIIII"));
        System.out.println("----------------------A*SearchAlgorithm------------------------");
        System.out.println(aStarSearch("MIIIIIIIIIIIIIIII"));
    }

}
