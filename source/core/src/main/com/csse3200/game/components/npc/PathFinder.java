package com.csse3200.game.components.npc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;

public class PathFinder {

    public static List<GridPoint2> findPath(GridPoint2 start, GridPoint2 target) {
        final GameArea map = ServiceLocator.getGameArea();
        final PriorityQueue<Node> open = new PriorityQueue<Node>();
        final Set<Node> closed = new HashSet<>();
        final Node[][] nodeMap = new Node[map.getTerrain().getMapBounds(0).x][map.getTerrain().getMapBounds(0).y];
        Node current;

        for (int x = 0; x < nodeMap.length; x++) {
            for (int y = 0; y < nodeMap[0].length; y++) {
                int heuristic = Math.abs(x - target.x) + Math.abs(y - target.y);
                Node node = new Node(10, heuristic, x, y);

                if (!(map.getAreaEntities().get(new GridPoint2(x, y)) == null)) {
                    if (map.getAreaEntities().get(new GridPoint2(x, y)).getComponent(HitboxComponent.class) != null) {
                        if (map.getAreaEntities().get(new GridPoint2(x, y)).getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.ENEMY_RANGE ||
                                map.getAreaEntities().get(new GridPoint2(x, y)).getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.ENEMY_MELEE) {
                            closed.add(node);
                        }
                    }
                }

                nodeMap[x][y] = node;
            }
        }

        Node startNode = nodeMap[start.x][start.y];
        Node targetNode = nodeMap[target.x][target.y];

        open.add(startNode);

        do {
            current = open.poll();
            closed.add(current);

            if (current.equals(targetNode)) {
                return extractPath(current);
            }

            for (int x = current.gridX - 1; x < current.gridX + 2; x++) {
                for (int y = current.gridY -1; y < current.gridY + 2; y++) {
                    if (map.getTerrain().gridWithinBounds(x, y)){
                        Node neighbor = nodeMap[x][y];

                        if (closed.contains(neighbor)) {
                            continue;
                        }

                        int calculatedCost = neighbor.heuristic + neighbor.moveCost + current.totalCost;

                        if (calculatedCost < neighbor.totalCost || open.contains(neighbor)) {
                            neighbor.totalCost = calculatedCost;
                            neighbor.parent = current;

                            if (!open.contains(neighbor)) {
                                open.add(neighbor);
                            }
                        }
                    }
                }
            }
        } while(!open.isEmpty());

        return List.of(start);
    }

    private static List<GridPoint2> extractPath(Node current) {
        List<GridPoint2> path = new ArrayList<>();
        while (current.parent != null) {
            path.add(current.getPosition());
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static class Node implements Comparable<Node> {
        private int moveCost;
        private int heuristic;
        private int gridX;
        private int gridY;
        private int totalCost;
        private Node parent;

        public Node(int moveCost, int heuristic, int gridX, int gridY) {
            this.moveCost = moveCost;
            this.heuristic = heuristic;
            this.gridX = gridX;
            this.gridY = gridY;
        }

        public int compareTo(Node that) {
            return Integer.compare(this.totalCost, that.totalCost);
        }

        public GridPoint2 getPosition() {
            return new GridPoint2(gridX, gridY);
        }
    }
}
