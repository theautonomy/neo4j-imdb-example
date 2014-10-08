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

public interface Actor
{
    /**
     * Returns this actors imdb-encoded name.
     * @return actor name
     */
    String getName();

    /**
     * Sets the actors name.
     * @param name
     *            name of actor
     */
    void setName( String name );

    /**
     * Returns all movies this actor acted in.
     * @return all movies
     */
    Iterable<Movie> getMovies();

    /**
     * Returns the specific role an actor had in a movie or null if actor didn't
     * have a role in the movie.
     * @param inMovie
     *            the movie to get role for
     * @return the role or null
     */
    Role getRole( Movie inMovie );
}
