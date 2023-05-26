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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractsSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.duplicate.DuplicateRuleSummarySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.HttpAccessTransport;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IServiceContractTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ServiceContractTransportRest extends AbstractTransportRest implements IServiceContractTransportProvider
{

    /** logger */
    private static Logger _logger = Logger.getLogger( ServiceContractTransportRest.class );

    /** URL for identityStore REST service */
    private String _strIdentityStoreEndPoint;

    /**
     * Simple Constructor
     */
    public ServiceContractTransportRest( )
    {
        super( new HttpAccessTransport( ) );
    }

    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public ServiceContractTransportRest( final IHttpTransportProvider httpTransport )
    {
        super( httpTransport );
    }

    /**
     * setter of identityStoreEndPoint
     *
     * @param strIdentityStoreEndPoint
     *            value to use
     */
    public void setIdentityStoreEndPoint( final String strIdentityStoreEndPoint )
    {
        this._strIdentityStoreEndPoint = strIdentityStoreEndPoint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractsSearchResponse getServiceContractList( final String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Get serivce contract list of " + strClientCode );

        this.checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final ServiceContractsSearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACTS_PATH, mapParams, mapHeadersRequest,
                ServiceContractsSearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractSearchResponse getActiveServiceContract( final String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "Get active serivce contract of " + strClientCode );

        this.checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final ServiceContractSearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACT_PATH, mapParams, mapHeadersRequest,
                ServiceContractSearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractSearchResponse getServiceContract( final String strClientCode, final Integer nServiceContractId ) throws IdentityStoreException
    {
        _logger.debug( "Get serivce contract [id=" + nServiceContractId + "] of " + strClientCode );

        this.checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final ServiceContractSearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACT_PATH + "/" + nServiceContractId, mapParams, mapHeadersRequest,
                ServiceContractSearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractChangeResponse createServiceContract( final ServiceContractDto serviceContract, final String strClientCode )
            throws IdentityStoreException
    {
        _logger.debug( "Create new serivce contract of " + strClientCode );
        _logger.debug( serviceContract );

        this.checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );
        final ServiceContractChangeResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACT_PATH, mapParams, mapHeadersRequest, serviceContract,
                ServiceContractChangeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractChangeResponse updateServiceContract( final ServiceContractDto serviceContract, final Integer nServiceContractId,
            final String strCLientCode ) throws IdentityStoreException
    {
        _logger.debug( "Update serivce contract [id=" + nServiceContractId + "] of " + strCLientCode );
        _logger.debug( serviceContract );

        this.checkClientApplication( strCLientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strCLientCode );

        final Map<String, String> mapParams = new HashMap<>( );
        final ServiceContractChangeResponse response = _httpTransport.doPutJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACT_PATH + "/" + nServiceContractId, mapParams, mapHeadersRequest,
                serviceContract, ServiceContractChangeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractChangeResponse closeServiceContract( ServiceContractDto serviceContract, Integer nServiceContractId, String strCLientCode )
            throws IdentityStoreException
    {
        _logger.debug( "Close serivce contract [id=" + nServiceContractId + "] of " + strCLientCode );
        _logger.debug( serviceContract );

        this.checkClientApplication( strCLientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strCLientCode );

        final Map<String, String> mapParams = new HashMap<>( );
        final ServiceContractChangeResponse response = _httpTransport.doPutJSON(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.SERVICECONTRACT_PATH + "/" + nServiceContractId
                        + Constants.SERVICECONTRACT_END_DATE_PATH,
                mapParams, mapHeadersRequest, serviceContract, ServiceContractChangeResponse.class, _mapper );

        return response;
    }
}
