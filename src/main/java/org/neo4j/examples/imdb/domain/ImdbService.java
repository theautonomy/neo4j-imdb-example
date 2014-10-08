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
package org.neo4j.examples.imdb.domain;

import java.util.List;

public interface ImdbService
{
    /**
     * Store a new actor in the graph and add the name to the index.
     * @param name
     * @return the new actor
     */
    Actor createActor( String name );

    /**
     * Store a new movie and add the title to the index.
     * @param title
     *            title of the movie
     * @param year
     *            year of release
     * @return the new movie
     */
    Movie createMovie( String title, int year );

    /**
     * Store a new role in the graph.
     * @param actor
     *            the actor
     * @param movie
     *            the movie
     * @param rolename
     *            name of the role
     * @return the new role
     */
    Role createRole( Actor actor, Movie movie, String roleName );

    /**
     * Returns the actor with the given <code>name</code> or <code>null</code>
     * if not found.
     * @param name
     *            name of actor
     * @return actor or <code>null</code> if not found
     */
    Actor getActor( String name );

    /**
     * Return the movie with given <code>title</code> or <code>null</code> if
     * not found.
     * @param title
     *            movie title
     * @return movie or <code>null</code> if not found
     */
    Movie getMovie( String title );
    
    Movie getExactMovie( String title );

    /**
     * Returns a list with first element {@link Actor} followed by {@link Movie}
     * ending with an {@link Actor}. The list is one of the shortest paths
     * between the <code>actor</code> and actor Kevin Bacon.
     * @param actor
     *            name of actor to find shortest path to Kevin Bacon
     * @return one of the shortest paths to Kevin Bacon
     */
    List<?> getBaconPath( Actor actor );
    
    /**
     * Add a relationship from some node to the reference node.
     * Will make it easy and fast to retrieve this node.
     */
    void setupReferenceRelationship();
}
