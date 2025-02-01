package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

//Complexity: O(n * m * log(n * m))
public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int HEIGHT = 21;
    private static final int WIDTH = 27;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        int[][] distance = new int[WIDTH][HEIGHT];
        for (int[] row : distance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] previous = new Edge[WIDTH][HEIGHT];

        Set<String> occupiedCells = getOccupiedCells(existingUnitList, attackUnit, targetUnit);

        PriorityQueue<EdgeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));

        // initialize start point
        queue.add(new EdgeDistance(attackUnit.getxCoordinate(), attackUnit.getyCoordinate(), 0));

        while (!queue.isEmpty()) {
            EdgeDistance curr = queue.poll();
            if (visited[curr.getX()][curr.getY()]) continue;
            visited[curr.getX()][curr.getY()] = true;
            if (isTargetReached(curr, targetUnit)) break;
            exploreNeighbors(curr, occupiedCells, distance, previous, queue);
        }

        return constructPath(previous, attackUnit, targetUnit);
    }

    private Set<String> getOccupiedCells(List<Unit> existingUnitList, Unit attackUnit, Unit targetUnit) {
        Set<String> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                occupiedCells.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }
        return occupiedCells;
    }

    private boolean isTargetReached(EdgeDistance current, Unit targetUnit) {
        return current.getX() == targetUnit.getxCoordinate()
                && current.getY() == targetUnit.getyCoordinate();
    }

    private void exploreNeighbors(EdgeDistance current, Set<String> occupiedCells,
                                  int[][] distance, Edge[][] previous, PriorityQueue<EdgeDistance> queue) {
        for (int[] dir : DIRECTIONS) {
            int neighbX = current.getX() + dir[0];
            int neighbY = current.getY() + dir[1];
            if (isValid(neighbX, neighbY, occupiedCells)) {
                int newDistance = distance[current.getX()][current.getY()] + 1;
                if (!queue.contains(new EdgeDistance(neighbX, neighbY, newDistance))
                        && newDistance < distance[neighbX][neighbY]) {
                    distance[neighbX][neighbY] = newDistance;
                    previous[neighbX][neighbY] = new Edge(current.getX(), current.getY());
                    queue.add(new EdgeDistance(neighbX, neighbY, newDistance));
                }
            }
        }
    }

    private boolean isValid(int x, int y, Set<String> occupiedCells) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && !occupiedCells.contains(x + "," + y);
    }

    private List<Edge> constructPath(Edge[][] previous, Unit attackUnit, Unit targetUnit) {
        List<Edge> path = new ArrayList<>();
        int pathX = targetUnit.getxCoordinate();
        int pathY = targetUnit.getyCoordinate();

        while (pathX != attackUnit.getxCoordinate() || pathY != attackUnit.getyCoordinate()) {
            path.add(new Edge(pathX, pathY));
            Edge prev = previous[pathX][pathY];
            if (prev == null) return Collections.emptyList();
            pathX = prev.getX();
            pathY = prev.getY();
        }
        path.add(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));
        Collections.reverse(path);
        return path;
    }
}
