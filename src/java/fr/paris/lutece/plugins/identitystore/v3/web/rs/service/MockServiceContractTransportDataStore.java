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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractsSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.business.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IServiceContractTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;

import fr.paris.lutece.portal.service.util.AppLogService;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class MockServiceContractTransportDataStore implements IServiceContractTransportProvider
{
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
        AppLogService.info( "MockServiceContractTransportDataStore is used" );
    }

    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public MockServiceContractTransportDataStore( final IHttpTransportProvider httpTransport )
    {
        AppLogService.info( "MockServiceContractTransportDataStore is used" );
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

    @Override
    public ServiceContractsSearchResponse getServiceContractList( String strTargetClientCode, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "[MOCK] Get service contract list of " + strTargetClientCode );

        return getMockServiceContractListFromDatastore( strTargetClientCode );
    }

    @Override
    public ServiceContractsSearchResponse getAllServiceContractList( String strClientCode, RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "[MOCK] Get all service contracts list" );

        return getMockServiceContractListFromDatastore( "all" );
    }

    @Override
    public ServiceContractsSearchResponse searchServiceContractList(final boolean bLoadDetails, final Date minEndDate, final String strClientCode,
                                                                    final RequestAuthor author) throws IdentityStoreException {
        AppLogService.debug( "[MOCK] search service contracts list" );

        return getMockServiceContractListFromDatastore( "all" );
    }

    @Override
    public ServiceContractSearchResponse getActiveServiceContract( String strTargetClientCode, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "[MOCK] Get active service contract of " + strTargetClientCode );

        return getMockServiceContractFromDatastore( strTargetClientCode );
    }

    @Override
    public ServiceContractSearchResponse getServiceContract( Integer nServiceContractId, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "[MOCK] Get service contract [id=" + nServiceContractId + "]" );

        return getMockServiceContractFromDatastore( strClientCode );
    }

    @Override
    public ServiceContractChangeResponse createServiceContract( ServiceContractDto serviceContract, String strClientCode, RequestAuthor author )
            throws IdentityStoreException
    {
        AppLogService.debug( "[MOCK] Create new service contract of " + serviceContract.getClientCode( ) );

        return null;
    }

    @Override
    public ServiceContractChangeResponse updateServiceContract( ServiceContractDto serviceContract, Integer nServiceContractId, String strClientCode,
            RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "[MOCK] Update service contract [id=" + nServiceContractId + "] of " + serviceContract.getClientCode( ) );

        return null;
    }

    @Override
    public ServiceContractChangeResponse closeServiceContract( ServiceContractDto serviceContract, Integer nServiceContractId, String strClientCode,
            RequestAuthor author ) throws IdentityStoreException
    {
        AppLogService.debug( "[MOCK] Close service contract [id=" + nServiceContractId + "] of " + serviceContract.getClientCode( ) );

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
            AppLogService.error( "MockServiceContractFromDatastore : Error while mapping DS data to ServiceContractsSearchResponse", e );

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
            AppLogService.error( "MockServiceContractFromDatastore : Error while mapping DS data to ServiceContractsSearchResponse", e );

            return null;
        }
    }

}
