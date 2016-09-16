/*
 * Copyright (c) 2002-2016, Mairie de Paris
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
package fr.paris.lutece.plugins.identitystore.web.rs.service;

import fr.paris.lutece.plugins.identitystore.web.service.IIdentityTransportProvider;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 * IdentityRestClientService
 */
public final class IdentityTransportApiManagerRest extends AbstractIdentityTransportRest
    implements IIdentityTransportProvider
{
    /** The Constant PARAMS_ACCES_TOKEN. */
    public static final String PARAMS_ACCES_TOKEN = "access_token";
    public static final String AUTHORIZATION = "Authorization";
    public static final String TYPE_AUTHENTIFICATION = "Bearer";

    /** The Constant PARAMS_GRANT_TYPE. */
    public static final String PARAMS_GRANT_TYPE = "grant_type";

    /** The Constant PARAMS_GRANT_TYPE_VALUE. */
    public static final String PARAMS_GRANT_TYPE_VALUE = "client_credentials";
    private static Logger _logger = Logger.getLogger( IdentityTransportApiManagerRest.class );

    /** URL for REST service apiManager */
    private String _strApiManagerEndPoint;

    /**
         * Simple Constructor
         */
    public IdentityTransportApiManagerRest(  )
    {
        super(  );
        this.setHttpTransport( new fr.paris.lutece.plugins.identitystore.web.service.HttpAccessTransport(  ) );
    }

    /**
         * setter of apiManagerEndPoint
         * @param strApiManagerEndPoint value to use
         */
    public void setApiManagerEndPoint( String strApiManagerEndPoint )
    {
        this._strApiManagerEndPoint = strApiManagerEndPoint;
    }

    /**
     * Gets the security token according to the given credentials
     *
     * @param strAuthenticationKey the key of properties credentials to use
     * @return the token
     */
    private String getToken( String strAuthenticationKey )
    {
        String strToken = "";

        _logger.debug( "LibraryIdentityStore - IdentityTransportApiManagerRest.getToken with URL_TOKEN property [" +
            _strApiManagerEndPoint + "]" );

        Map<String, String> mapHeadersRequest = new HashMap<String, String>(  );
        Map<String, String> mapParams = new HashMap<String, String>(  );

        mapParams.put( PARAMS_GRANT_TYPE, PARAMS_GRANT_TYPE_VALUE );

        mapHeadersRequest.put( AUTHORIZATION, TYPE_AUTHENTIFICATION + " " + strAuthenticationKey );

        String strOutput = getHttpTransport(  ).doPost( _strApiManagerEndPoint, mapParams, mapHeadersRequest );

        JSONObject strResponseApiManagerJsonObject = null;

        if ( JSONUtils.mayBeJSON( strOutput ) )
        {
            strResponseApiManagerJsonObject = (JSONObject) JSONSerializer.toJSON( strOutput );

            if ( ( strResponseApiManagerJsonObject != null ) &&
                    strResponseApiManagerJsonObject.has( PARAMS_ACCES_TOKEN ) )
            {
                strToken = (String) strResponseApiManagerJsonObject.get( PARAMS_ACCES_TOKEN );
            }
        }
        else
        {
            _logger.debug( "LibraryIdentityStore - IdentityTransportApiManagerRest.getToken invalid response [" +
                strOutput + "]" );
        }

        return strToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addAuthentication( Map<String, String> mapHeadersRequest, String strAuthenticationKey )
    {
        String strToken = getToken( strAuthenticationKey );

        if ( StringUtils.isNotBlank( strToken ) )
        {
            mapHeadersRequest.put( AUTHORIZATION, TYPE_AUTHENTIFICATION + " " + strToken );
        }
    }
}
