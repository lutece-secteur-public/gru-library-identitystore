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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.UncertifyIdentityRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.exporting.IdentityExportRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.exporting.IdentityExportResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistory;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistoryGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskCreateRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskCreateResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskGetStatusResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskListGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskUpdateStatusRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskUpdateStatusResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * WARNING this is mock transport for LuteceTestCase purpose
 */
public class MockIdentityTransportRest implements IIdentityTransportProvider
{
    private final List<IdentityDto> _listIdentities;
    private final String CONNECTION_ID_PREFIX = "conn_";
    private final String CUSTOMER_ID_PREFIX = "cust_";

    /** mapper */
    protected static ObjectMapper _mapper;
    static
    {
        _mapper = new ObjectMapper( );
        _mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
    }

    public MockIdentityTransportRest( )
    {
        AppLogService.info( "MockIdentityTransportRest is used" );
        _listIdentities = new ArrayList<IdentityDto>( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentitySearchResponse getIdentity( String strCustomerId, String strClientCode, RequestAuthor author ) throws IdentityNotFoundException, AppException
    {
        if ( StringUtils.isEmpty( strCustomerId ) )
        {
            throw new AppException( "params wrong in mock" );
        }

        throw new IdentityNotFoundException( "not found in mock list" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse updateIdentity( String customerId, IdentityChangeRequest identityChange, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.updateIdentity not managed return existing identity if possible" );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );
        response.setCustomerId( customerId );
        response.setConnectionId( identityChange.getIdentity( ) != null ? identityChange.getIdentity( ).getCustomerId( ) : null );
        response.setLastUpdateDate( new Timestamp( new Date( ).getTime( ) ) );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.createIdentity always return ok" );

        IdentityDto identity = identityChange.getIdentity( );

        if ( StringUtils.isEmpty( identity.getConnectionId( ) ) )
        {
            identity.setConnectionId( CONNECTION_ID_PREFIX + _listIdentities.size( ) );
        }
        if ( StringUtils.isEmpty( identity.getCustomerId( ) ) )
        {
            identity.setCustomerId( CUSTOMER_ID_PREFIX + _listIdentities.size( ) );
        }
        _listIdentities.add( identity );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );
        response.setCustomerId( identity.getCustomerId( ) );
        response.setConnectionId( identity.getConnectionId( ) );
        response.setCreationDate( new Timestamp( new Date( ).getTime( ) ) );

        return response;
    }

    @Override
    public IdentityChangeResponse importIdentity( IdentityChangeRequest identityChange, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.importIdentity always return ok" );

        IdentityDto identity = identityChange.getIdentity( );

        if ( StringUtils.isEmpty( identity.getConnectionId( ) ) )
        {
            identity.setConnectionId( CONNECTION_ID_PREFIX + _listIdentities.size( ) );
        }
        if ( StringUtils.isEmpty( identity.getCustomerId( ) ) )
        {
            identity.setCustomerId( CUSTOMER_ID_PREFIX + _listIdentities.size( ) );
        }
        _listIdentities.add( identity );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );
        response.setCustomerId( identity.getCustomerId( ) );
        response.setConnectionId( identity.getConnectionId( ) );
        response.setCreationDate( new Timestamp( new Date( ).getTime( ) ) );

        return response;
    }

    @Override
    public IdentityMergeResponse mergeIdentities( IdentityMergeRequest identityMerge, String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.mergeIdentities always return ok" );

        IdentityMergeResponse response = new IdentityMergeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentityMergeResponse unMergeIdentities( IdentityMergeRequest identityMerge, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.unMergeIdentities always return ok" );

        IdentityMergeResponse response = new IdentityMergeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentityHistoryGetResponse getIdentityHistory( String strCustomerId, String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.getIdentityHistory always return empty history" );

        final IdentityHistoryGetResponse response = new IdentityHistoryGetResponse( );
        final IdentityHistory history = new IdentityHistory( );
        history.setCustomerId( strCustomerId );
        response.setHistory( history );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentityHistorySearchResponse searchIdentityHistory( IdentityHistorySearchRequest request, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.getIdentityHistory always return empty history" );

        final IdentityHistorySearchResponse history = new IdentityHistorySearchResponse( );
        history.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return history;
    }

    @Override
    public IdentityChangeResponse deleteIdentity( String strCustomerId, String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.deleteIdentity always return ok" );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentitySearchResponse searchIdentities( IdentitySearchRequest identitySearchRequest, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.searchIdentities always return ok" );

        IdentitySearchResponse response = new IdentitySearchResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public ServiceContractSearchResponse getServiceContract( String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.getServiceContract always return ok" );

        ServiceContractSearchResponse response = new ServiceContractSearchResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public UpdatedIdentitySearchResponse getUpdatedIdentities( UpdatedIdentitySearchRequest request, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.getUpdatedIdentities always return ok" );

        UpdatedIdentitySearchResponse response = new UpdatedIdentitySearchResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentityChangeResponse uncertifyIdentity( String strCustomerId, String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.uncertifyIdentity always return ok" );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentityChangeResponse uncertifyIdentity(UncertifyIdentityRequest request, String strCustomerId, String strClientCode, RequestAuthor author) throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.uncertifyIdentity always return ok" );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( ResponseStatusFactory.success( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentityExportResponse exportIdentities( final IdentityExportRequest request, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "MockIdentityTransportRest.exportIdentities always return ok" );

        final IdentityExportResponse response = new IdentityExportResponse( );
        response.setStatus( ResponseStatusFactory.ok( ).setMessage( "OK" ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );

        return response;
    }

    @Override
    public IdentityTaskCreateResponse createIdentityTask(IdentityTaskCreateRequest request, String strClientCode, RequestAuthor author) throws IdentityStoreException {
        return null;
    }

    @Override
    public IdentityTaskUpdateStatusResponse updateIdentityTaskStatus(String taskCode, IdentityTaskUpdateStatusRequest request, String strClientCode, RequestAuthor author) throws IdentityStoreException {
        return null;
    }

    @Override
    public IdentityTaskGetStatusResponse getIdentityTaskStatus(String taskCode, String strClientCode, RequestAuthor author) throws IdentityStoreException {
        return null;
    }

    @Override
    public IdentityTaskGetResponse getIdentityTaskList(String taskCode, String strClientCode, RequestAuthor author) throws IdentityStoreException {
        return null;
    }

    @Override
    public IdentityTaskListGetResponse getIdentityTaskList(String resourceId, String resourceType, String strClientCode, RequestAuthor author) throws IdentityStoreException {
        return null;
    }

    @Override
    public IdentityTaskListGetResponse getIdentityTaskListBySecondaryCuid(String secondaryCuid, String strClientCode, RequestAuthor author) throws IdentityStoreException
    {
        return null;
    }

    @Override
    public IdentityTaskSearchResponse searchIdentityTasks(IdentityTaskSearchRequest request, String strClientCode, RequestAuthor author) throws IdentityStoreException {
        return null;
    }
}
