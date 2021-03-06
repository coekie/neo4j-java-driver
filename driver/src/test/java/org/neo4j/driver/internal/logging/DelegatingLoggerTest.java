/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.driver.internal.logging;

import org.junit.Test;

import org.neo4j.driver.v1.Logger;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DelegatingLoggerTest
{
    private static final String PREFIX = "Output";
    private static final String MESSAGE = "Hello World!";
    private static final Exception ERROR = new Exception();

    @Test
    public void shouldThrowWhenDelegateIsNull()
    {
        try
        {
            new DelegatingLogger( null );
            fail( "Exception expected" );
        }
        catch ( Exception e )
        {
            assertThat( e, instanceOf( NullPointerException.class ) );
        }
    }

    @Test
    public void shouldAllowNullPrefix()
    {
        assertNotNull( new DelegatingLogger( newLoggerMock(), null ) );
    }

    @Test
    public void shouldDelegateIsDebugEnabled()
    {
        Logger delegate = newLoggerMock( true, false );

        DelegatingLogger logger = new DelegatingLogger( delegate );

        assertTrue( logger.isDebugEnabled() );
        verify( delegate ).isDebugEnabled();
    }

    @Test
    public void shouldDelegateIsTraceEnabled()
    {
        Logger delegate = newLoggerMock( false, true );

        DelegatingLogger logger = new DelegatingLogger( delegate );

        assertTrue( logger.isTraceEnabled() );
        verify( delegate ).isTraceEnabled();
    }

    @Test
    public void shouldNotDelegateDebugLogWhenDebugDisabled()
    {
        Logger delegate = newLoggerMock();

        DelegatingLogger logger = new DelegatingLogger( delegate );
        logger.debug( MESSAGE );

        verify( delegate, never() ).debug( anyString(), anyVararg() );
    }

    @Test
    public void shouldNotDelegateTraceLogWhenTraceDisabled()
    {
        Logger delegate = newLoggerMock();

        DelegatingLogger logger = new DelegatingLogger( delegate );
        logger.trace( MESSAGE );

        verify( delegate, never() ).trace( anyString(), anyVararg() );
    }

    @Test
    public void shouldDelegateErrorMessageWhenNoPrefix()
    {
        Logger delegate = newLoggerMock();
        DelegatingLogger logger = new DelegatingLogger( delegate );

        logger.error( MESSAGE, ERROR );

        verify( delegate ).error( MESSAGE, ERROR );
    }

    @Test
    public void shouldDelegateInfoMessageWhenNoPrefix()
    {
        Logger delegate = newLoggerMock();
        DelegatingLogger logger = new DelegatingLogger( delegate );

        logger.info( MESSAGE );

        verify( delegate ).info( MESSAGE );
    }

    @Test
    public void shouldDelegateWarnMessageWhenNoPrefix()
    {
        Logger delegate = newLoggerMock();
        DelegatingLogger logger = new DelegatingLogger( delegate );

        logger.warn( MESSAGE );

        verify( delegate ).warn( MESSAGE );
    }

    @Test
    public void shouldDelegateDebugMessageWhenNoPrefix()
    {
        Logger delegate = newLoggerMock( true, false );
        DelegatingLogger logger = new DelegatingLogger( delegate );

        logger.debug( MESSAGE );

        verify( delegate ).debug( MESSAGE );
    }

    @Test
    public void shouldDelegateTraceMessageWhenNoPrefix()
    {
        Logger delegate = newLoggerMock( false, true );
        DelegatingLogger logger = new DelegatingLogger( delegate );

        logger.trace( MESSAGE );

        verify( delegate ).trace( MESSAGE );
    }

    @Test
    public void shouldDelegateErrorMessageWithPrefix()
    {
        Logger delegate = newLoggerMock();
        DelegatingLogger logger = new DelegatingLogger( delegate, PREFIX );

        logger.error( MESSAGE, ERROR );

        verify( delegate ).error( "[Output] Hello World!", ERROR );
    }

    @Test
    public void shouldDelegateInfoMessageWithPrefix()
    {
        Logger delegate = newLoggerMock();
        DelegatingLogger logger = new DelegatingLogger( delegate, PREFIX );

        logger.info( MESSAGE );

        verify( delegate ).info( "[Output] Hello World!" );
    }

    @Test
    public void shouldDelegateWarnMessageWithPrefix()
    {
        Logger delegate = newLoggerMock();
        DelegatingLogger logger = new DelegatingLogger( delegate, PREFIX );

        logger.warn( MESSAGE );

        verify( delegate ).warn( "[Output] Hello World!" );
    }

    @Test
    public void shouldDelegateDebugMessageWithPrefix()
    {
        Logger delegate = newLoggerMock( true, false );
        DelegatingLogger logger = new DelegatingLogger( delegate, PREFIX );

        logger.debug( MESSAGE );

        verify( delegate ).debug( "[Output] Hello World!" );
    }

    @Test
    public void shouldDelegateTraceMessageWithPrefix()
    {
        Logger delegate = newLoggerMock( false, true );
        DelegatingLogger logger = new DelegatingLogger( delegate, PREFIX );

        logger.trace( MESSAGE );

        verify( delegate ).trace( "[Output] Hello World!" );
    }

    private static Logger newLoggerMock()
    {
        return newLoggerMock( false, false );
    }

    private static Logger newLoggerMock( boolean debugEnabled, boolean traceEnabled )
    {
        Logger logger = mock( Logger.class );
        when( logger.isDebugEnabled() ).thenReturn( debugEnabled );
        when( logger.isTraceEnabled() ).thenReturn( traceEnabled );
        return logger;
    }
}
