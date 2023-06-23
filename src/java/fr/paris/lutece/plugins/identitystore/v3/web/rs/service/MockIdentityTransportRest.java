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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.Identity;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeStatus;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WARNING this is a mock transport for LuteceTestCase purpose
 */
public class MockIdentityTransportRest implements IIdentityTransportProvider
{
    private static Logger _logger = Logger.getLogger( MockIdentityTransportRest.class );
    private List<Identity> _listIdentities;
    private final String CONNECTION_ID_PREFIX = "conn_";
    private final String CUSTOMER_ID_PREFIX = "cust_";

    /** mapper */
    protected static ObjectMapper _mapper;
    static
    {
        _mapper = new ObjectMapper( );
        // _mapper.enable( DeserializationFeature.UNWRAP_ROOT_VALUE );
        _mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        // _mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
    }
    
    public MockIdentityTransportRest( )
    {
        _logger.info( "MockIdentityTransportRest is used" );
        _listIdentities = new ArrayList<Identity>( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentitySearchResponse getIdentity( String strCustomerId, String strClientCode ) throws IdentityNotFoundException, AppException
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
    public IdentityChangeResponse updateIdentity( String customerId, IdentityChangeRequest identityChange, String strClientCode )
            throws IdentityNotFoundException, AppException
    {
        _logger.debug( "MockIdentityTransportRest.updateIdentity not managed return existing identity if possible" );

        return new IdentityChangeResponse( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws AppException
    {
        Identity identity = identityChange.getIdentity( );

        if ( StringUtils.isEmpty( identity.getConnectionId( ) ) )
        {
            identity.setConnectionId( CONNECTION_ID_PREFIX + _listIdentities.size( ) );
        }
        if ( StringUtils.isEmpty( identity.getCustomerId( ) ) )
        {
            identity.setCustomerId( CUSTOMER_ID_PREFIX + _listIdentities.size( ) );
        }
        _listIdentities.add( identity );

        return createIdentity( identityChange, strClientCode );
    }

    @Override
    public IdentityChangeResponse importIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException
    {
        Identity identity = identityChange.getIdentity( );

        if ( StringUtils.isEmpty( identity.getConnectionId( ) ) )
        {
            identity.setConnectionId( CONNECTION_ID_PREFIX + _listIdentities.size( ) );
        }
        if ( StringUtils.isEmpty( identity.getCustomerId( ) ) )
        {
            identity.setCustomerId( CUSTOMER_ID_PREFIX + _listIdentities.size( ) );
        }
        _listIdentities.add( identity );

        return importIdentity( identityChange, strClientCode );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityChangeResponse deleteIdentity( String strConnectionId, String strApplicationCode, IdentityChangeRequest identityChange )
    {
        _logger.debug( "MockIdentityTransportRest.deleteIdentity always return ok" );

        IdentityChangeResponse response = new IdentityChangeResponse( );
        response.setStatus( IdentityChangeStatus.DELETE_SUCCESS );
        response.setMessage( "OK" );

        return response;
    }

	@Override
	public IdentitySearchResponse searchIdentities(IdentitySearchRequest identitySearchRequest, String strClientCode)
			throws IdentityStoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceContractSearchResponse getServiceContract(String strClientCode) throws IdentityStoreException {
		// TODO Auto-generated method stub
		return null;
	}
}
