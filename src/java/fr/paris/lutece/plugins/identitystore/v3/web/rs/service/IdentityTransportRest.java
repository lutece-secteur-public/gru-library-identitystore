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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class IdentityTransportRest extends AbstractTransportRest implements IIdentityTransportProvider
{

    /** logger */
    private static final Logger _logger = Logger.getLogger( IdentityTransportRest.class );

    /** URL for identityStore REST service */
    private final String _strIdentityStoreEndPoint;

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
    public IdentitySearchResponse getIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws AppException, IdentityStoreException
    {
        _logger.debug( "Get identity attributes of " + strCustomerId );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkCustomerId( strCustomerId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + "/" + strCustomerId;
        return _httpTransport.doGet( url, null, mapHeadersRequest, IdentitySearchResponse.class, _mapper );
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public IdentityChangeResponse updateIdentity( final String strCustomerId, final IdentityChangeRequest identityChange, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        _logger.debug( "Update identity attributes" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkIdentityChange( identityChange, true );
        IdentityRequestValidator.instance( ).checkIdentityForUpdate( identityChange.getIdentity( ).getConnectionId( ), strCustomerId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + "/" + strCustomerId;
        return _httpTransport.doPutJSON( url, null, mapHeadersRequest, identityChange, IdentityChangeResponse.class, _mapper );
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        _logger.debug( "Create identity" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkIdentityChange( identityChange, false );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, identityChange, IdentityChangeResponse.class, _mapper );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse deleteIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        _logger.debug( "Delete identity" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkCustomerId( strCustomerId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + "/" + strCustomerId;
        return _httpTransport.doDeleteJSON( url, null, mapHeadersRequest, null, IdentityChangeResponse.class, _mapper );
    }

    /**
     * {@inheritDoc}
     *
     * @throws IdentityStoreException
     */
    @Override
    public IdentitySearchResponse searchIdentities( final IdentitySearchRequest identitySearchRequest, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        _logger.debug( "Search identities" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkIdentitySearch( identitySearchRequest );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.SEARCH_IDENTITIES_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, identitySearchRequest, IdentitySearchResponse.class, _mapper );
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Please use {@link ServiceContractTransportRest} for requests regarding service contract.
     * @throws IdentityStoreException
     */
    @Override
    @Deprecated
    public ServiceContractSearchResponse getServiceContract( final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        _logger.debug( "Get active service contract" );
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACT_PATH;
        return _httpTransport.doGet( url, null, mapHeadersRequest, ServiceContractSearchResponse.class, _mapper );
    }

    @Override
    public IdentityChangeResponse importIdentity( final IdentityChangeRequest identityChange, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        _logger.debug( "Import identity" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkIdentityChange( identityChange, false );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.IMPORT_IDENTITY_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, identityChange, IdentityChangeResponse.class, _mapper );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityMergeResponse mergeIdentities( final IdentityMergeRequest identityMerge, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {

        _logger.debug( "merge identities [master cuid= " + identityMerge.getPrimaryCuid( ) + "][secondary cuid = " + identityMerge.getSecondaryCuid( ) + "]" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkMergeRequest( identityMerge );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.MERGE_IDENTITIES_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, identityMerge, IdentityMergeResponse.class, _mapper );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityMergeResponse unMergeIdentities( final IdentityMergeRequest identityMerge, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {

        _logger.debug(
                "unmerge identities [master cuid= " + identityMerge.getPrimaryCuid( ) + "][secondary cuid = " + identityMerge.getSecondaryCuid( ) + "]" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkCancelMergeRequest( identityMerge );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.CANCEL_MERGE_IDENTITIES_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, identityMerge, IdentityMergeResponse.class, _mapper );
    }

    @Override
    public IdentityHistoryGetResponse getIdentityHistory( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        _logger.debug( "Get identity history [cuid=" + strCustomerId + "]" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkCustomerId( strCustomerId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.HISTORY_PATH + "/" + strCustomerId;
        return _httpTransport.doGet( url, null, mapHeadersRequest, IdentityHistoryGetResponse.class, _mapper );
    }

    @Override
    public IdentityHistorySearchResponse searchIdentityHistory( final IdentityHistorySearchRequest request, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        _logger.debug( "Search identity history with request [cuid=" + request.getCustomerId( ) + "][client_code=" + request.getClientCode( ) + "][author_name="
                + request.getAuthorName( ) + "][change_type=" + ( request.getIdentityChangeType( ) != null ? request.getIdentityChangeType( ).name( ) : "" )
                + "][nb_days_from=" + request.getNbDaysFrom( ) + "][metadata="
                + request.getMetadata( ).entrySet( ).stream( ).map( entry -> entry.getKey( ) + " : " + entry.getValue( ) ).collect( Collectors.joining( ", " ) )
                + "]" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkIdentityHistory( request );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.HISTORY_PATH + Constants.SEARCH_HISTORY_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, request, IdentityHistorySearchResponse.class, _mapper );
    }

    @Override
    public UpdatedIdentitySearchResponse getUpdatedIdentities( final String strDays, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        _logger.debug( "Get updated identities since " + strDays + " days ago" );
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final HashMap<String, String> mapParams = new HashMap<>( );
        mapParams.put( Constants.PARAM_DAYS, strDays );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.UPDATED_IDENTITIES_PATH;
        return _httpTransport.doGet( url, mapParams, mapHeadersRequest, UpdatedIdentitySearchResponse.class, _mapper );
    }

    @Override
    public IdentityChangeResponse uncertifyIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        _logger.debug( "Uncertify identity [cuid=" + strCustomerId + "]" );
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkCustomerId( strCustomerId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.UNCERTIFY_ATTRIBUTES_PATH + "/"
                + strCustomerId;
        return _httpTransport.doPutJSON( url, null, mapHeadersRequest, null, IdentityChangeResponse.class, _mapper );
    }

}
