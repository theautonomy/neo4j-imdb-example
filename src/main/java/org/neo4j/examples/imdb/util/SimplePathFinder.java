/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.neo4j.examples.imdb.util;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.Predicate;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.impl.traversal.TraversalDescriptionImpl;

import java.util.*;

/**
 * @author mh
 * @since 24.01.11
 */
public class SimplePathFinder implements PathFinder {
    private static final int MAXIMUM_DEPTH = 5;

    @Override
    public List<Node> shortestPath(final Node startNode, final Node endNode, final RelationshipType relType) {
        return findPath(startNode, endNode, relType);
    }

    private List<Node> findPath(final Node startNode, final Node endNode, final RelationshipType relType) {
        final Map<Node, Node> forwardTraversedNodes = new HashMap<Node, Node>();
        final Map<Node, Node> backwardTraversedNodes = new HashMap<Node, Node>();
        final PathReturnEval forwardReturnEvaluator = new PathReturnEval(forwardTraversedNodes, backwardTraversedNodes);
        final PathReturnEval backwardReturnEvaluator = new PathReturnEval(backwardTraversedNodes, forwardTraversedNodes);
        Iterator<Node> forwardIterator = traversePath(startNode, relType, forwardReturnEvaluator);
        Iterator<Node> backwardIterator = traversePath(endNode, relType, backwardReturnEvaluator);

        while (forwardIterator.hasNext() || backwardIterator.hasNext()) {
            if (forwardIterator.hasNext()) {
                forwardIterator.next();
            }
            List<Node> forwardPath = forwardReturnEvaluator.getMatch();
            if (forwardPath != null) {
                Collections.reverse(forwardPath);
                return forwardPath;
            }
            if (backwardIterator.hasNext()) {
                backwardIterator.next();
            }
            List<Node> backwardPath = backwardReturnEvaluator.getMatch();
            if (backwardPath != null) {
                return backwardPath;
            }
        }
        return Collections.emptyList();
    }

    private Iterator<Node> traversePath(Node startNode, RelationshipType relType, PathReturnEval returnEval) {
        TraversalDescription traversalDescription = new TraversalDescriptionImpl()
                .order(Traversal.postorderBreadthFirst())
                .prune(Traversal.pruneAfterDepth(MAXIMUM_DEPTH))
                .filter(returnEval)
                .expand(Traversal.expanderForTypes(relType, Direction.BOTH));
        final Traverser traverser = traversalDescription
                .traverse(startNode);
        return traverser.nodes().iterator();
    }

    private static class PathReturnEval implements Predicate<Path> {
        private final Map<Node, Node> myNodes;
        private final Map<Node, Node> otherNodes;
        private LinkedList<Node> match = null;

        public PathReturnEval(final Map<Node, Node> myNodes, final Map<Node, Node> otherNodes) {
            this.myNodes = myNodes;
            this.otherNodes = otherNodes;
        }

        @Override
        public boolean accept(Path currentPos) {
            Node currentNode = currentPos.endNode();
            Relationship lastRelationship = currentPos.lastRelationship();
            Node prevNode = lastRelationship!=null ? lastRelationship.getOtherNode(currentNode) : null;
            if (!otherNodes.containsKey(currentNode)) {
                myNodes.put(currentNode, prevNode);
            } else {
                match = new LinkedList<Node>();
                match.add(currentNode);
                while (prevNode != null) {
                    match.add(prevNode);
                    prevNode = myNodes.get(prevNode);
                }
                Node otherNode = otherNodes.get(currentNode);
                while (otherNode != null) {
                    match.addFirst(otherNode);
                    otherNode = otherNodes.get(otherNode);
                }
            }
            return true;
        }

        protected List<Node> getMatch() {
            return match;
        }

    }
}