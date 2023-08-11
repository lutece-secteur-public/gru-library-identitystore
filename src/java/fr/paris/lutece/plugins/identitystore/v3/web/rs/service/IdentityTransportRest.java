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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.IdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistory;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class IdentityTransportRest extends AbstractTransportRest implements IIdentityTransportProvider
{

    /** logger */
    private static Logger _logger = Logger.getLogger( IdentityTransportRest.class );

    /** URL for identityStore REST service */
    private String _strIdentityStoreEndPoint;

    /**
     * contructor
     * 
     * @param transportProvider
     */
    protected IdentityTransportRest( IHttpTransportProvider transportProvider )
    {
        super( transportProvider );

        _strIdentityStoreEndPoint = transportProvider.getApiEndPointUrl( );
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public IdentitySearchResponse getIdentity( String strCustomerId, String strClientCode ) throws AppException, IdentityStoreException
    {
        _logger.debug( "Get identity attributes of " + strCustomerId );

        IdentityRequestValidator.instance( ).checkCustomerId( strCustomerId );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );

        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentitySearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + "/" + strCustomerId, mapParams, mapHeadersRequest,
                IdentitySearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public IdentityChangeResponse updateIdentity( String strCustomerId, IdentityChangeRequest identityChange, String strClientCode )
            throws IdentityStoreException
    {
        _logger.debug( "Update identity attributes" );
        IdentityRequestValidator.instance( ).checkIdentityChange( identityChange, true );
        IdentityRequestValidator.instance( ).checkIdentityForUpdate( identityChange.getIdentity( ).getConnectionId( ), strCustomerId );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityChangeResponse response = _httpTransport.doPutJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + "/" + strCustomerId, mapParams, mapHeadersRequest,
                identityChange, IdentityChangeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Create identity" );
        IdentityRequestValidator.instance( ).checkIdentityChange( identityChange, false );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityChangeResponse response = _httpTransport.doPostJSON( _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH,
                mapParams, mapHeadersRequest, identityChange, IdentityChangeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse deleteIdentity( String strIdConnection, String strClientCode, IdentityChangeRequest identityChange )
            throws IdentityStoreException
    {
        _logger.debug( "Delete identity" );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );
        IdentityRequestValidator.instance( ).checkOrigin( identityChange.getOrigin( ) );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityChangeResponse response = _httpTransport.doDeleteJSON( _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH,
                mapParams, mapHeadersRequest, identityChange, IdentityChangeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IdentityStoreException
     */
    @Override
    public IdentitySearchResponse searchIdentities( IdentitySearchRequest identitySearchRequest, String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Search identities" );

        IdentityRequestValidator.instance( ).checkIdentitySearch( identitySearchRequest );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final IdentitySearchResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.SEARCH_IDENTITIES_PATH, null, mapHeadersRequest,
                identitySearchRequest, IdentitySearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Please use {@link ServiceContractTransportRest} for requests regarding service contract.
     * @throws IdentityStoreException
     */
    @Override
    @Deprecated
    public ServiceContractSearchResponse getServiceContract( String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Get active service contract" );

        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final ServiceContractSearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACT_PATH, null, mapHeadersRequest,
                ServiceContractSearchResponse.class, _mapper );

        return response;
    }

    @Override
    public IdentityChangeResponse importIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Import identity" );
        IdentityRequestValidator.instance( ).checkIdentityChange( identityChange, false );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityChangeResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.IMPORT_IDENTITY_PATH, mapParams, mapHeadersRequest,
                identityChange, IdentityChangeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityMergeResponse mergeIdentities( final IdentityMergeRequest identityMerge, final String strClientCode ) throws IdentityStoreException
    {

        IdentityRequestValidator.instance( ).checkMergeRequest( identityMerge );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );
        _logger.debug( "merge identities [master cuid= " + identityMerge.getPrimaryCuid( ) + "][secondary cuid = " + identityMerge.getSecondaryCuid( ) + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityMergeResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.MERGE_IDENTITIES_PATH, mapParams, mapHeadersRequest,
                identityMerge, IdentityMergeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityMergeResponse unMergeIdentities( final IdentityMergeRequest identityMerge, final String strClientCode ) throws IdentityStoreException
    {

        IdentityRequestValidator.instance( ).checkCancelMergeRequest( identityMerge );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );
        _logger.debug(
                "unmerge identities [master cuid= " + identityMerge.getPrimaryCuid( ) + "][secondary cuid = " + identityMerge.getSecondaryCuid( ) + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityMergeResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.CANCEL_MERGE_IDENTITIES_PATH, mapParams,
                mapHeadersRequest, identityMerge, IdentityMergeResponse.class, _mapper );

        return response;
    }

    @Override
    public IdentityHistory getIdentityHistory( final String strCustomerId, final String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Get identity history [cuid=" + strCustomerId + "]" );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );
        IdentityRequestValidator.instance( ).checkCustomerId( strCustomerId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final IdentityHistory response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.HISTORY_PATH + "/" + strCustomerId, null, mapHeadersRequest,
                IdentityHistory.class, _mapper );

        return response;
    }

    @Override
    public UpdatedIdentitySearchResponse getUpdatedIdentities( String strDays, String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Get updated identities since " + strDays + " days ago" );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final UpdatedIdentitySearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.UPDATED_IDENTITIES_PATH + "?" + Constants.PARAM_DAYS + "=" + strDays, null,
                mapHeadersRequest, UpdatedIdentitySearchResponse.class, _mapper );

        return response;
    }

    @Override
    public IdentityChangeResponse uncertifyIdentity( final String strCustomerId, final String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Uncertify identity [cuid=" + strCustomerId + "]" );
        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );
        IdentityRequestValidator.instance( ).checkCustomerId( strCustomerId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final IdentityChangeResponse response = _httpTransport.doPutJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.UNCERTIFY_ATTRIBUTES_PATH + "/" + strCustomerId,
                null, mapHeadersRequest, null, IdentityChangeResponse.class, _mapper );

        return response;
    }

}
