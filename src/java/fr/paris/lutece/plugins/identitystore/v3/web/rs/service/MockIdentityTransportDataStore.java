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
package fr.paris.lutece.plugins.identitystore.v3.web.rs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * WARNING this is a mock transport for LuteceTestCase purpose
 */
public class MockIdentityTransportDataStore extends AbstractIdentityTransportRest
{
    private static Logger _logger = Logger.getLogger( MockIdentityTransportDataStore.class );

    private static final String KEY_DATASTORE_MOCK_IDENTITY_CHANGE_PREFIX = "identitystore.mock.identity.change.data.";

    public MockIdentityTransportDataStore( )
    {
        super( null );
        _logger.error( "MockIdentityTransportDatastore is used" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentitySearchResponse getIdentity( String strCustomerId, String strClientCode ) throws AppException, IdentityStoreException
    {
        if ( StringUtils.isEmpty( strCustomerId ) )
        {
            throw new AppException( "params wrong in ds mock" );
        }

        return getMockIdentitySearchFromDatastore( strCustomerId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse updateIdentity( String customerId, IdentityChangeRequest identityChange, String strClientCode )
            throws IdentityNotFoundException, AppException
    {
        _logger.debug( "MockIdentityTransportDatastore.updateIdentity not managed return existing identity if possible" );

        return getMockIdentityChangeFromDatastore( customerId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws AppException
    {
        _logger.debug( "MockIdentityTransportDataStore.createIdentity not managed " );

        return getMockIdentityChangeFromDatastore( identityChange.getIdentity( ).getConnectionId( ) );
    }

    @Override
    public IdentityChangeResponse importIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "MockIdentityTransportDataStore.createIdentity not managed " );

        return getMockIdentityChangeFromDatastore( identityChange.getIdentity( ).getConnectionId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addAuthentication( Map<String, String> mapHeadersRequest )
    {
        // no authentication for mock
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto deleteIdentity( String strConnectionId, String strApplicationCode )
    {
        _logger.debug( "MockIdentityTransportDatastore.deleteIdentity always return ok" );

        ResponseDto response = new ResponseDto( );
        response.setStatus( "OK" );
        response.setMessage( "OK" );

        return response;
    }

    /**
     * get the mock json identity change from DataStore
     * 
     * @param strId
     * @return the identity Dto
     */
    private IdentityChangeResponse getMockIdentityChangeFromDatastore( String strId )
    {
        String strDsData = DatastoreService.getDataValue( KEY_DATASTORE_MOCK_IDENTITY_CHANGE_PREFIX + strId, "{}" );

        try
        {
            return _mapper.readValue( strDsData, IdentityChangeResponse.class );
        }
        catch( Exception e )
        {
            _logger.error( "MockIdentityFromDatastore : Error while mapping DS data to IdentityDto", e );

            return null;
        }
    }

    /**
     * get the mock json identity from DataStore
     *
     * @param strId
     * @return the identity Dto
     */
    private IdentitySearchResponse getMockIdentitySearchFromDatastore( String strId )
    {
        String strDsData = DatastoreService.getDataValue( KEY_DATASTORE_MOCK_IDENTITY_CHANGE_PREFIX + strId, "{}" );

        try
        {
            return _mapper.readValue( strDsData, IdentitySearchResponse.class );
        }
        catch( Exception e )
        {
            _logger.error( "MockIdentityFromDatastore : Error while mapping DS data to IdentityDto", e );

            return null;
        }
    }
}
