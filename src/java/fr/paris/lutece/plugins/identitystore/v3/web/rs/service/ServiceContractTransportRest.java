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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.IdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractsSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.business.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IServiceContractTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

import java.util.HashMap;
import java.util.Map;

public class ServiceContractTransportRest extends AbstractTransportRest implements IServiceContractTransportProvider
{

    /** URL for identityStore REST service */
    private final String _strIdentityStoreEndPoint;
    private final String _strIdentityPath;

    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public ServiceContractTransportRest( final IHttpTransportProvider httpTransport )
    {
        super( httpTransport );
        _strIdentityStoreEndPoint = httpTransport.getApiEndPointUrl( );
        _strIdentityPath = Constants.CONSTANT_DEFAULT_IDENTITY_PATH;
    }
    
    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public ServiceContractTransportRest( final IHttpTransportProvider httpTransport, String strIdentityPath )
    {
        super( httpTransport );
        _strIdentityStoreEndPoint = httpTransport.getApiEndPointUrl( );
        _strIdentityPath = strIdentityPath;
    }


    @Override
    public ServiceContractsSearchResponse getServiceContractList( final String strTargetClientCode, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + _strIdentityPath + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACTS_PATH + "/list/" + strTargetClientCode;
        return _httpTransport.doGet( url, null, mapHeadersRequest, ServiceContractsSearchResponse.class, _mapper );
    }

    @Override
    public ServiceContractsSearchResponse getAllServiceContractList( final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + _strIdentityPath + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACTS_PATH;
        return _httpTransport.doGet( url, null, mapHeadersRequest, ServiceContractsSearchResponse.class, _mapper );
    }

    @Override
    public ServiceContractSearchResponse getActiveServiceContract( final String strTargetClientCode, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + _strIdentityPath + Constants.VERSION_PATH_V3 + Constants.ACTIVE_SERVICE_CONTRACT_PATH + "/" + strTargetClientCode;
        return _httpTransport.doGet( url, null, mapHeadersRequest, ServiceContractSearchResponse.class, _mapper );
    }

    @Override
    public ServiceContractSearchResponse getServiceContract( final Integer nServiceContractId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + _strIdentityPath + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACTS_PATH + "/" + nServiceContractId;
        return _httpTransport.doGet( url, null, mapHeadersRequest, ServiceContractSearchResponse.class, _mapper );
    }

    @Override
    public ServiceContractChangeResponse createServiceContract( final ServiceContractDto serviceContract, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkServiceContract( serviceContract );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + _strIdentityPath + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACTS_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, serviceContract, ServiceContractChangeResponse.class, _mapper );
    }

    @Override
    public ServiceContractChangeResponse updateServiceContract( final ServiceContractDto serviceContract, final Integer nServiceContractId,
            final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkServiceContract( serviceContract );
        IdentityRequestValidator.instance( ).checkContractId( nServiceContractId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final Map<String, String> mapParams = new HashMap<>( );

        final String url = _strIdentityStoreEndPoint + _strIdentityPath + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACTS_PATH + "/" + nServiceContractId;
        return _httpTransport.doPutJSON( url, mapParams, mapHeadersRequest, serviceContract, ServiceContractChangeResponse.class, _mapper );
    }

    @Override
    public ServiceContractChangeResponse closeServiceContract( final ServiceContractDto serviceContract, final Integer nServiceContractId,
            final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        IdentityRequestValidator.instance( ).checkServiceContract( serviceContract );
        IdentityRequestValidator.instance( ).checkContractId( nServiceContractId );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final Map<String, String> mapParams = new HashMap<>( );

        final String url = _strIdentityStoreEndPoint + _strIdentityPath + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACTS_PATH + "/" + nServiceContractId
                + Constants.SERVICECONTRACT_END_DATE_PATH;
        return _httpTransport.doPutJSON( url, mapParams, mapHeadersRequest, serviceContract, ServiceContractChangeResponse.class, _mapper );
    }
}
