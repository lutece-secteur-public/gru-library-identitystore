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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * IdentityRestClientService
 */
public final class HttpApiManagerAccessTransport extends HttpAccessTransport
{
    /** The Constant PARAMS_ACCES_TOKEN. */
    private static final String PARAMS_ACCES_TOKEN = "access_token";
    private static final String PARAMS_VALIDITY = "expires_in";
    private static final String TYPE_AUTHENTIFICATION_BASIC = "Basic";
    private static final String TYPE_AUTHENTIFICATION_BEARER = "Bearer";

    /** The Constant PARAMS_GRANT_TYPE. */
    private static final String PARAMS_GRANT_TYPE = "grant_type";

    /** The Constant PARAMS_GRANT_TYPE_VALUE. */
    private static final String PARAMS_GRANT_TYPE_VALUE = "client_credentials";

    private static final ObjectMapper _objectMapper = new ObjectMapper( );

    /** URL for REST service apiManager */
    private String _strAccessManagerEndPointUrl;
    private String _strAccessManagerCredentials;
    
    // instance variables
    private String _strToken;
    private LocalDateTime _dateExpiration;

    /**
     * setter of apiManagerEndPoint
     * 
     * @param strApiManagerEndPoint
     *            value to use
     */
    public void setAccessManagerEndPointUrl( String strApiManagerEndPoint )
    {
        this._strAccessManagerEndPointUrl = strApiManagerEndPoint;
    }

    /**
     * Sets the API Manager credentials
     * 
     * @param strCredentials
     *            the API Manager credentials
     */
    public void setAccessManagerCredentials( String strCredentials )
    {
        this._strAccessManagerCredentials = strCredentials;
    }

    /**
     * Gets the security token from API Manager
     * refresh token if expired
     * 
     * @return the token
     * @throws IdentityStoreException
     */
    private String getToken( ) throws IdentityStoreException
    {
	if ( StringUtils.isEmpty(_strToken ) || _dateExpiration == null
	        || LocalDateTime.now( ).isAfter( ( _dateExpiration.minusMinutes( 1 ) ) ) )
	{
	    refreshToken( );
	}
	
	return _strToken;
    }
    
    /**
     * Refresh the bearer token
     * @return 
     *  
     * @throws IdentityStoreException
     */
    private void refreshToken( ) throws IdentityStoreException
    {
        String strToken = StringUtils.EMPTY;

        final Map<String, String> mapHeadersRequest = new HashMap<String, String>( );
        final Map<String, String> mapParams = new HashMap<String, String>( );

        mapParams.put( PARAMS_GRANT_TYPE, PARAMS_GRANT_TYPE_VALUE );

        mapHeadersRequest.put( Constants.ACCEPT, Constants.APPLICATION_JSON );
        mapHeadersRequest.put( Constants.CONTENT_TYPE, Constants.APPLICATION_FORM_URLENCODED );
        mapHeadersRequest.put( Constants.AUTHORIZATION, TYPE_AUTHENTIFICATION_BASIC + " " + _strAccessManagerCredentials );

        final Map<String, String> mapHeadersResponse = new HashMap<>( );
        String strOutput = StringUtils.EMPTY;

        try
        {
            strOutput = this._httpClient.doPost( _strAccessManagerEndPointUrl, mapParams, null, null, mapHeadersRequest, mapHeadersResponse );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        try
        {
            JsonNode jsonNode = _objectMapper.readTree( strOutput );
            
            if ( ( jsonNode != null ) && jsonNode.has( PARAMS_ACCES_TOKEN ) )
            {
                JsonNode jsonTokenNode = jsonNode.get( PARAMS_ACCES_TOKEN );
                _strToken = jsonTokenNode.textValue( );
                
                JsonNode jsonValidityNode = jsonNode.get( PARAMS_VALIDITY );
                int nbSecondsValidityTime = jsonValidityNode.asInt( 0 );
                _dateExpiration = LocalDateTime.now( ).plusSeconds( nbSecondsValidityTime );
            }
        }
        catch( JsonProcessingException e )
        {
            handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    protected void addAuthentication( Map<String, String> mapHeadersRequest ) throws IdentityStoreException
    {
        String strToken = getToken( );

        if ( StringUtils.isNotBlank( strToken ) )
        {
            mapHeadersRequest.put( Constants.AUTHORIZATION, TYPE_AUTHENTIFICATION_BEARER + " " + strToken );
        }
    }
}
