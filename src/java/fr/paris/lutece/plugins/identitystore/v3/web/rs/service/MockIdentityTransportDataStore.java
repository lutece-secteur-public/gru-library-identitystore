/*
 * Copyright (c) 2002-2024, City of Paris
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistoryGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import org.apache.commons.lang3.StringUtils;

/**
 * WARNING this is a mock transport for LuteceTestCase purpose
 */
public class MockIdentityTransportDataStore implements IIdentityTransportProvider
{
    private static final String KEY_DATASTORE_MOCK_IDENTITY_CHANGE_PREFIX = "identitystore.mock.identity.change.data.";

    /** mapper */
    protected static ObjectMapper _mapper;
    static
    {
        _mapper = new ObjectMapper( );
        // _mapper.enable( DeserializationFeature.UNWRAP_ROOT_VALUE );
        _mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        // _mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
    }

    public MockIdentityTransportDataStore( )
    {

        AppLogService.error( "MockIdentityTransportDatastore is used" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentitySearchResponse getIdentity( String strCustomerId, String strClientCode, RequestAuthor author ) throws AppException, IdentityStoreException
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
    public IdentityChangeResponse updateIdentity( String customerId, IdentityChangeRequest identityChange, String strClientCode, RequestAuthor author )
            throws IdentityNotFoundException, AppException
    {
        AppLogService.debug( "MockIdentityTransportDatastore.updateIdentity not managed return existing identity if possible" );

        return getMockIdentityChangeFromDatastore( customerId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, String strClientCode, RequestAuthor author ) throws AppException
    {
        AppLogService.debug( "MockIdentityTransportDataStore.createIdentity not managed " );

        return getMockIdentityChangeFromDatastore( identityChange.getIdentity( ).getConnectionId( ) );
    }

    @Override
    public IdentityChangeResponse importIdentity( IdentityChangeRequest identityChange, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportDataStore.importIdentity not managed " );

        return getMockIdentityChangeFromDatastore( identityChange.getIdentity( ).getConnectionId( ) );
    }

    @Override
    public IdentityMergeResponse mergeIdentities( IdentityMergeRequest identityMerge, String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        return null;
    }

    @Override
    public IdentityMergeResponse unMergeIdentities( IdentityMergeRequest identityMerge, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        return null;
    }

    @Override
    public IdentityHistoryGetResponse getIdentityHistory( final String strCustomerId, final String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        return null;
    }

    @Override
    public IdentityHistorySearchResponse searchIdentityHistory( IdentityHistorySearchRequest request, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        return null;
    }

    @Override
    public UpdatedIdentitySearchResponse getUpdatedIdentities( UpdatedIdentitySearchRequest request, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        return null;
    }

    @Override
    public IdentityChangeResponse uncertifyIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse deleteIdentity( String strCustomerId, String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportDatastore.deleteIdentity always return ok" );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

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
            AppLogService.error( "MockIdentityFromDatastore : Error while mapping DS data to IdentityDto", e );

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
            AppLogService.error( "MockIdentityFromDatastore : Error while mapping DS data to IdentityDto", e );

            return null;
        }
    }

    @Override
    public IdentitySearchResponse searchIdentities( IdentitySearchRequest identitySearchRequest, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServiceContractSearchResponse getServiceContract( String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
