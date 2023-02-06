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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchStatusType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.Identity;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.*;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.service.MockIdentityTransportRest;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityService;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.util.httpaccess.HttpAccessService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * test of NotificationService
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = {
        "classpath:test-identitystore-v3.xml"
} )
public class IdentityServiceTest
{
    private static Logger _logger = Logger.getLogger( IdentityServiceTest.class );
    @Resource( name = "testIdentityService.api.httpAccess.v3" )
    private IdentityService _identityServiceApiHttpAccess;
    @Resource( name = "testIdentityService.rest.httpAccess.v3" )
    private IdentityService _identityServiceRestHttpAccess;
    private Identity _identity;

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

        _identity = mapper.readValue( getClass( ).getResourceAsStream( "/identity-v3.json" ), Identity.class );
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
        _identityServiceRestHttpAccess.getIdentity( "0", "0" );
    }

    /**
     * test IdentityService through mock and no spring beans
     */
    @Test
    public void testServiceThroughMockNoSpring( ) throws IdentityStoreException
    {
        MockIdentityTransportRest mockTransport = new MockIdentityTransportRest( );
        mockTransport.setIdentityStoreEndPoint( "unused" );

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
        RequestAuthor author = new RequestAuthor( );
        author.setType( AuthorType.admin );
        author.setName( "TEST" );

        IdentityChangeRequest identityChangeRequest = new IdentityChangeRequest( );

        identityChangeRequest.setOrigin( author );
        identityChangeRequest.setIdentity( _identity );

        // test getIdentity
        try
        {
            // test getIdentities
            final IdentitySearchRequest identitySearchRequest = new IdentitySearchRequest( );
            SearchDto search = new SearchDto( );
            SearchAttributeDto email = new SearchAttributeDto( );
            search.setAttributes( new ArrayList<>( ) );
            search.getAttributes( ).add( email );
            email.setKey( "login" );
            email.setValue( "toto@gmail.com" );
            email.setStrict( true );
            identitySearchRequest.setSearch( search );
            IdentitySearchResponse searchResponse = identityService.searchIdentities( identitySearchRequest, author.getName( ) );

            IdentitySearchResponse identityByCustomerId = identityService.getIdentityByCustomerId( _identity.getCustomerId( ), author.getName( ) );
            IdentitySearchResponse identityByConnectionId = identityService.getIdentityByConnectionId( _identity.getConnectionId( ), author.getName( ) );

            // test updateIdentity
            IdentityChangeResponse identityChangeResponse = identityService.updateIdentity( identityChangeRequest.getIdentity().getCustomerId(), identityChangeRequest, author.getName( ) );

            // test createIdentity
            IdentityChangeResponse identity = identityService.createIdentity( identityChangeRequest, author.getName( ) );
            // identityService.deleteIdentity( _identity.getConnectionId( ), author.getName( ) );

            // Service contract
            final ServiceContractSearchResponse response = identityService.getServiceContract( "TEST" );
            if ( !ServiceContractSearchStatusType.NOT_FOUND.equals( response.getStatus( ) ) )
            {
                final ServiceContractDto serviceContract = response.getServiceContract( );
                serviceContract.getAttributeDefinitions( ).forEach( attributeDefinitionDto -> {

                } );
            }
        }
        catch( IdentityStoreException e )
        {
            System.out.println( "Une erreur est survenue lors des test: " + e.getMessage( ) );
        }
    }
}
