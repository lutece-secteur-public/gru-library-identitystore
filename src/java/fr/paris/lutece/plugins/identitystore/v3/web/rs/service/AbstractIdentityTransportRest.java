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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
abstract class AbstractIdentityTransportRest extends AbstractTransportRest implements IIdentityTransportProvider
{
    /** logger */
    private static Logger _logger = Logger.getLogger( AbstractIdentityTransportRest.class );

    /** URL for identityStore REST service */
    private String _strIdentityStoreEndPoint;

    protected AbstractIdentityTransportRest( final IHttpTransportProvider transportProvider )
    {
        super( transportProvider );
    }

    /**
     * setter of identityStoreEndPoint
     * 
     * @param strIdentityStoreEndPoint
     *            value to use
     */
    public void setIdentityStoreEndPoint( String strIdentityStoreEndPoint )
    {
        this._strIdentityStoreEndPoint = strIdentityStoreEndPoint;
    }

    /**
     * add specific authentication to request
     * 
     * @param mapHeadersRequest
     *            map of headers to add
     * @throws IdentityStoreException
     */
    protected abstract void addAuthentication( Map<String, String> mapHeadersRequest ) throws IdentityStoreException;

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
        this.checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        addAuthentication( mapHeadersRequest );
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
        checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        addAuthentication( mapHeadersRequest );
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
        checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        addAuthentication( mapHeadersRequest );
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
    public ResponseDto deleteIdentity( String strIdConnection, String strClientCode ) throws IdentityStoreException
    {
        throw new IdentityStoreException( "Method not available" );
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
        checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        addAuthentication( mapHeadersRequest );
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

        checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        addAuthentication( mapHeadersRequest );
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
        checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        addAuthentication( mapHeadersRequest );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final IdentityChangeResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.IDENTITY_PATH + Constants.IMPORT_IDENTITY_PATH, mapParams, mapHeadersRequest,
                identityChange, IdentityChangeResponse.class, _mapper );

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
            throw new IdentityStoreException( fr.paris.lutece.plugins.identitystore.v2.web.rs.util.Constants.PARAM_ID_CONNECTION + "is missing." );
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

        if ( identityChange.getOrigin( ) == null )
        {
            throw new IdentityStoreException( "Provided Author is null" );
        }

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
        if ( identityMergeRequest == null || identityMergeRequest.getIdentities( ) == null
                || StringUtils.isEmpty( identityMergeRequest.getIdentities( ).getPrimaryCuid( ) )
                || StringUtils.isEmpty( identityMergeRequest.getIdentities( ).getSecondaryCuid( ) ) )
        {
            throw new IdentityStoreException( "Provided Identity Merge request is null or empty" );
        }
    }
}
