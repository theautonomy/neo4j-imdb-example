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
package org.neo4j.examples.imdb.parser;

import java.util.List;

import org.neo4j.examples.imdb.domain.Actor;
import org.neo4j.examples.imdb.domain.ImdbService;
import org.neo4j.examples.imdb.domain.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class ImdbReaderImpl implements ImdbReader
{
    @Autowired
    private ImdbService imdbService;

    @Override
    @Transactional
    public void newActors( final List<ActorData> actorList )
    {
        for ( ActorData actorData : actorList )
        {
            newActor( actorData.getName(), actorData.getMovieRoles() );
        }
    }

    @Override
    @Transactional
    public void newMovies( final List<MovieData> movieList )
    {
        for ( MovieData movieData : movieList )
        {
            newMovie( movieData.getTitle(), movieData.getYear() );
        }
    }

    private void newMovie( final String title, final int year )
    {
        imdbService.createMovie( title, year );
    }

    private void newActor( final String name, final RoleData[] movieRoles )
    {
        final Actor actor = imdbService.createActor( name );
        for ( RoleData movieRole : movieRoles )
        {
            final Movie movie = imdbService
                .getExactMovie( movieRole.getTitle() );
            if ( movie != null )
            {
                imdbService.createRole( actor, movie, movieRole.getRole() );
            }
        }
    }
}
