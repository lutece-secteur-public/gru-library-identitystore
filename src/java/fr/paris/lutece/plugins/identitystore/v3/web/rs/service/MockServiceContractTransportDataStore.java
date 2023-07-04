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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractsSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IServiceContractTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockServiceContractTransportDataStore implements IServiceContractTransportProvider
{

    /** logger */
    private static Logger _logger = Logger.getLogger( MockServiceContractTransportDataStore.class );

    private static final String KEY_DATASTORE_MOCK_SERVICE_CONTRACT_PREFIX = "identitystore.mock.servicecontract.";

    /** mapper */
    protected static ObjectMapper _mapper;
    static
    {
        _mapper = new ObjectMapper( );
        // _mapper.enable( DeserializationFeature.UNWRAP_ROOT_VALUE );
        _mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        // _mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
    }

    /**
     * Simple Constructor
     */
    public MockServiceContractTransportDataStore( )
    {
        _logger.info( "MockServiceContractTransportDataStore is used" );
    }

    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public MockServiceContractTransportDataStore( final IHttpTransportProvider httpTransport )
    {
        _logger.info( "MockServiceContractTransportDataStore is used" );
    }

    /**
     * setter of identityStoreEndPoint
     *
     * @param strIdentityStoreEndPoint
     *            value to use
     */
    public void setIdentityStoreEndPoint( final String strIdentityStoreEndPoint )
    {
        // do nothing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractsSearchResponse getServiceContractList( final String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "[MOCK] Get service contract list of " + strClientCode );

        return getMockServiceContractListFromDatastore( strClientCode );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractSearchResponse getActiveServiceContract( final String strClientCode ) throws IdentityStoreException
    {
        _logger.debug( "[MOCK] Get active service contract of " + strClientCode );

        return getMockServiceContractFromDatastore( strClientCode );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractSearchResponse getServiceContract( final String strClientCode, final Integer nServiceContractId ) throws IdentityStoreException
    {
        _logger.debug( "[MOCK] Get service contract [id=" + nServiceContractId + "] of " + strClientCode );

        return getMockServiceContractFromDatastore( strClientCode );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractChangeResponse createServiceContract( final ServiceContractDto serviceContract, final String strClientCode )
            throws IdentityStoreException
    {
        _logger.debug( "[MOCK] Create new service contract of " + strClientCode );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractChangeResponse updateServiceContract( final ServiceContractDto serviceContract, final Integer nServiceContractId,
            final String strCLientCode ) throws IdentityStoreException
    {
        _logger.debug( "[MOCK] Update service contract [id=" + nServiceContractId + "] of " + strCLientCode );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceContractChangeResponse closeServiceContract( ServiceContractDto serviceContract, Integer nServiceContractId, String strCLientCode )
            throws IdentityStoreException
    {
        _logger.debug( "[MOCK] Close service contract [id=" + nServiceContractId + "] of " + strCLientCode );

        return null;
    }

    /**
     * get list of service contract from datastore
     * 
     * @param strClientCode
     * @return the response
     */
    private ServiceContractSearchResponse getMockServiceContractFromDatastore( String strClientCode )
    {

        String strDsData = DatastoreService.getDataValue( KEY_DATASTORE_MOCK_SERVICE_CONTRACT_PREFIX + strClientCode, "{}" );

        try
        {
            return _mapper.readValue( strDsData, ServiceContractSearchResponse.class );
        }
        catch( Exception e )
        {
            _logger.error( "MockServiceContractFromDatastore : Error while mapping DS data to ServiceContractsSearchResponse", e );

            return null;
        }
    }

    /**
     * get service contract from datastore
     * 
     * @param strClientCode
     * @return the response
     */
    private ServiceContractsSearchResponse getMockServiceContractListFromDatastore( String strClientCode )
    {

        String strDsData = DatastoreService.getDataValue( KEY_DATASTORE_MOCK_SERVICE_CONTRACT_PREFIX + strClientCode + ".list", "{}" );

        try
        {
            return _mapper.readValue( strDsData, ServiceContractsSearchResponse.class );
        }
        catch( Exception e )
        {
            _logger.error( "MockServiceContractFromDatastore : Error while mapping DS data to ServiceContractsSearchResponse", e );

            return null;
        }
    }

}
