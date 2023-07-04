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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        this.checkCustomerId( strCustomerId );
        this.checkClientCode( strClientCode );

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
        checkIdentityChange( identityChange );
        checkIdentityForUpdate( identityChange.getIdentity( ).getConnectionId( ), strCustomerId );
        checkClientCode( strClientCode );

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
        checkIdentityChange( identityChange );
        checkClientCode( strClientCode );

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
        checkAuthor( identityChange );
        checkClientCode( strClientCode );

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

        checkIdentitySearch( identitySearchRequest );
        checkClientCode( strClientCode );

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

        checkClientCode( strClientCode );

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
        checkIdentityChange( identityChange );
        checkClientCode( strClientCode );

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

        this.checkMergeRequest( identityMerge );
        _logger.debug( "merge identities [master cuid= " + identityMerge.getPrimaryCuid( ) + "][secondary cuid = " + identityMerge.getSecondaryCuid( ) + "]" );
        this.checkClientCode( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityMergeResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.MERGE_IDENTITIES_PATH, mapParams, mapHeadersRequest,
                identityMerge, IdentityMergeResponse.class, _mapper );

        return response;
    }

    /**
     * check whether the parameters related to the identity are valid or not
     *
     * @param strCustomerId
     *            the customer id
     * @throws AppException
     *             if the parameters are not valid
     */
    public void checkCustomerId( String strCustomerId ) throws IdentityStoreException
    {
        if ( StringUtils.isBlank( strCustomerId ) )
        {
            throw new IdentityStoreException( fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants.PARAM_ID_CONNECTION + "is missing." );
        }
    }

    /**
     * check whether the parameters related to the identity are valid or not
     *
     * @param strClientCode
     *            the strClientCode
     * @throws AppException
     *             if the parameters are not valid
     */
    public void checkClientCode( String strClientCode ) throws IdentityStoreException
    {
        if ( StringUtils.isBlank( strClientCode ) )
        {
            throw new IdentityStoreException( "Client code is mandatory." );
        }
    }

    /**
     * check whether the parameters related to the identity update are valid or not
     *
     * @param strConnectionId
     *            the connection id
     * @param strCustomerId
     *            the customer id
     * @throws AppException
     *             if the parameters are not valid
     */
    public void checkIdentityForUpdate( String strConnectionId, String strCustomerId ) throws IdentityStoreException
    {
        if ( StringUtils.isNotEmpty( strConnectionId ) && StringUtils.isBlank( strCustomerId ) )
        {
            throw new IdentityStoreException( "You cannot update an identity providing its connection_id. You must specify "
                    + fr.paris.lutece.plugins.identitystore.v2.web.rs.util.Constants.PARAM_ID_CUSTOMER );
        }

        if ( StringUtils.isBlank( strCustomerId ) )
        {
            throw new IdentityStoreException( fr.paris.lutece.plugins.identitystore.v2.web.rs.util.Constants.PARAM_ID_CUSTOMER + " is missing" );
        }
    }

    /**
     * check whether the parameters related to the identity are valid or not
     *
     * @param identityChange
     * @throws AppException
     */
    public void checkIdentityChange( IdentityChangeRequest identityChange ) throws IdentityStoreException
    {
        if ( identityChange == null || identityChange.getIdentity( ) == null || CollectionUtils.isEmpty( identityChange.getIdentity( ).getAttributes( ) ) )
        {
            throw new IdentityStoreException( "Provided Identity Change request is null or empty" );
        }

        this.checkAuthor( identityChange );

        if ( identityChange.getIdentity( ).getAttributes( ).stream( ).anyMatch( a -> !a.isCertified( ) ) )
        {
            throw new IdentityStoreException( "Provided attributes shall be certified" );
        }
    }

    public void checkIdentitySearch( IdentitySearchRequest identitySearch ) throws IdentityStoreException
    {
        if ( StringUtils.isNotEmpty( identitySearch.getConnectionId( ) ) && identitySearch.getSearch( ) != null )
        {
            throw new IdentityStoreException( "You cannot provide a connection_id and a Search at the same time." );
        }
        if ( StringUtils.isEmpty( identitySearch.getConnectionId( ) ) && ( identitySearch.getSearch( ) == null
                || identitySearch.getSearch( ).getAttributes( ) == null || identitySearch.getSearch( ).getAttributes( ).isEmpty( ) ) )
        {
            throw new IdentityStoreException( "Provided Identity Search request is null or empty" );
        }
    }

    /**
     * Check whether the parameters related to the identity merge request are valid or not
     *
     * @param identityMergeRequest
     *            the identity merge request
     * @throws AppException
     *             if the parameters are not valid
     */
    public void checkMergeRequest( IdentityMergeRequest identityMergeRequest ) throws IdentityStoreException
    {
        this.checkAuthor( identityMergeRequest );
        if ( identityMergeRequest == null )
        {
            throw new IdentityStoreException( "Provided Identity Merge request is null" );
        }

        if ( identityMergeRequest.getPrimaryCuid( ) == null )
        {
            throw new IdentityStoreException( "An Identity merge request must provide the CUID of the primary Identity" );
        }

        if ( identityMergeRequest.getSecondaryCuid( ) == null )
        {
            throw new IdentityStoreException( "An Identity merge request must provide the CUID of the secondary Identity" );
        }

        if ( identityMergeRequest.getIdentity( ) != null && Collections.isEmpty( identityMergeRequest.getIdentity( ).getAttributes( ) ) )
        {
            throw new IdentityStoreException( "An Identity merge request that provides an Identity must provide at least one Attribute" );
        }
    }

    /**
     * check whether the parameters related to the identity are valid or not
     *
     * @param identityChange
     * @throws AppException
     */
    public void checkAuthor( ChangeRequest identityChange ) throws IdentityStoreException
    {
        if ( identityChange.getOrigin( ) == null )
        {
            throw new IdentityStoreException( "Provided Author is null" );
        }

        if ( StringUtils.isEmpty( identityChange.getOrigin( ).getName( ) ) || identityChange.getOrigin( ).getType( ) == null )
        {
            throw new IdentityStoreException( "Author and author type fields shall be set" );
        }
    }

}
