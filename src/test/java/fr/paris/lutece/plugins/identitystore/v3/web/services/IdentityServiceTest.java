/*
 * Copyright (c) 2002-2023, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identitystore.v3.web.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AuthorType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.SearchAttribute;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.SearchDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.service.MockIdentityTransportRest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityService;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.util.httpaccess.HttpAccessService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

/**
 * test of NotificationService
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = {
        "classpath:test-identitystore-v3.xml"
} )
public class IdentityServiceTest
{
    private static final String CLIENT_CODE = "TEST";
    private static Logger _logger = Logger.getLogger( IdentityServiceTest.class );
    @Resource( name = "testIdentityService.api.httpAccess.v3" )
    private IdentityService _identityServiceApiHttpAccess;
    @Resource( name = "testIdentityService.rest.httpAccess.v3" )
    private IdentityService _identityServiceRestHttpAccess;
    private IdentityDto _identity;

    /**
     * Constructor, init the notification JSON
     *
     * @throws JsonParseException
     *             exception
     * @throws JsonMappingException
     *             exception
     * @throws IOException
     *             exception
     */
    public IdentityServiceTest( ) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper( );
        // mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );

        _identity = mapper.readValue( getClass( ).getResourceAsStream( "/identity-v3.json" ), IdentityDto.class );
        // Init HttpAccess singleton through NPE exception due of lack of properties access
        try
        {
            HttpAccessService.getInstance( );
        }
        catch( Exception e )
        {
            // do nothing
        }
    }

    /**
     * test IdentityService through ApiManager Transport and HttpAccess Provider
     */
    @Test
    public void testServiceThroughApiManagerHttpAccess( ) throws IdentityStoreException
    {
        callServiceMethod( _identityServiceApiHttpAccess, "_identityServiceApiHttpAccess" );
    }

    /**
     * test IdentityService through Rest Transport and HttpAccess Provider
     */
    @Test
    public void testServiceThroughRestHttpAccess( ) throws IdentityStoreException
    {
        callServiceMethod( _identityServiceRestHttpAccess, "_identityServiceApiHttpAccess" );
    }

    @Test
    public void testGetIdentity( ) throws IdentityStoreException
    {
        _identityServiceRestHttpAccess.getIdentity( "0", "0", getRequestAuthor( ) );
    }

    @Test
    public void testSearchIdentityHistory( ) throws IdentityStoreException
    {
        // Search all modifications triggered by rule RG_GEN_SuspectDoublon_08 within 15 last days
        final IdentityHistorySearchRequest requestWithMetadata = new IdentityHistorySearchRequest( );
        requestWithMetadata.getMetadata( ).put( Constants.METADATA_DUPLICATE_RULE_CODE, "RG_GEN_SuspectDoublon_08" );
        requestWithMetadata.setNbDaysFrom( 15 );
        final IdentityHistorySearchResponse responseWithMetadata = _identityServiceRestHttpAccess.searchIdentityHistory( requestWithMetadata, CLIENT_CODE,
                getRequestAuthor( ) );

        // Search all modifications on Identity cuid_fake
        final IdentityHistorySearchRequest requestByCuid = new IdentityHistorySearchRequest( );
        requestByCuid.setCustomerId( "cuid_fake" );
        final IdentityHistorySearchResponse responseWByCuid = _identityServiceRestHttpAccess.searchIdentityHistory( requestByCuid, CLIENT_CODE,
                getRequestAuthor( ) );

        // Search all modifications performed by author Imaginary
        final IdentityHistorySearchRequest requestByAuthor = new IdentityHistorySearchRequest( );
        requestByCuid.setAuthorName( "Imaginary" );
        final IdentityHistorySearchResponse responseWByAuthor = _identityServiceRestHttpAccess.searchIdentityHistory( requestByAuthor, CLIENT_CODE,
                getRequestAuthor( ) );
    }

    /**
     * test IdentityService through mock and no spring beans
     */
    @Test
    public void testServiceThroughMockNoSpring( ) throws IdentityStoreException
    {
        MockIdentityTransportRest mockTransport = new MockIdentityTransportRest( );

        // default mockTransport.httpTransport set with simpleRest
        IdentityService identityServiceNoSpring = new IdentityService( mockTransport );

        callServiceMethod( identityServiceNoSpring, "identityServiceNoSpring" );
    }

    /**
     * full test of service methods
     *
     * @param identityService
     *            the service to test
     * @param messagePrefix
     *            string put at the beginning of messages
     */
    private void callServiceMethod( IdentityService identityService, String messagePrefix ) throws IdentityStoreException
    {
        final RequestAuthor author = getRequestAuthor( );

        IdentityChangeRequest identityChangeRequest = new IdentityChangeRequest( );

        identityChangeRequest.setIdentity( _identity );

        // test getIdentity
        try
        {
            // test getIdentities
            final IdentitySearchRequest identitySearchRequest = new IdentitySearchRequest( );
            SearchDto search = new SearchDto( );
            SearchAttribute email = new SearchAttribute( );
            search.setAttributes( new ArrayList<>( ) );
            search.getAttributes( ).add( email );
            email.setKey( "login" );
            email.setValue( "toto@gmail.com" );
            identitySearchRequest.setSearch( search );
            IdentitySearchResponse searchResponse = identityService.searchIdentities( identitySearchRequest, CLIENT_CODE, author );

            IdentitySearchResponse identityByCustomerId = identityService.getIdentityByCustomerId( _identity.getCustomerId( ), CLIENT_CODE, author );
            IdentitySearchResponse identityByConnectionId = identityService.getIdentityByConnectionId( _identity.getConnectionId( ), CLIENT_CODE,
                    getRequestAuthor( ) );

            // test updateIdentity
            IdentityChangeResponse identityChangeResponse = identityService.updateIdentity( identityChangeRequest.getIdentity( ).getCustomerId( ),
                    identityChangeRequest, CLIENT_CODE, author );

            // test createIdentity
            IdentityChangeResponse identity = identityService.createIdentity( identityChangeRequest, CLIENT_CODE, author );
            // identityService.deleteIdentity( _identity.getConnectionId( ), CLIENT_CODE );

        }
        catch( IdentityStoreException e )
        {
            System.out.println( "Une erreur est survenue lors des test: " + e.getMessage( ) );
        }
    }

    private static RequestAuthor getRequestAuthor( )
    {
        RequestAuthor author = new RequestAuthor( );
        author.setType( AuthorType.admin );
        author.setName( CLIENT_CODE );
        return author;
    }
}
