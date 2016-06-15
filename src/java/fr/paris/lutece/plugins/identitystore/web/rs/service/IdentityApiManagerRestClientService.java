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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.paris.lutece.plugins.identitystore.web.service.IIdentityProvider;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;


/**
 * IdentityRestClientService
 */
public final class IdentityApiManagerRestClientService extends AbstractIdentityRestClientService
    implements IIdentityProvider
{
    /**
     *
     * @param strKeyPropertiesCredentials hash code
     * @return token
     */
    private String getToken( String strKeyPropertiesCredentials )
    {
        String strToken = null;
        AppLogService.info( 
            "\n*************************************************************** TOKEN REQUEST **********************************************************************\n" );
        AppLogService.info( "\n PROPERTIES CREDENTIALS   : " + strKeyPropertiesCredentials + "\n" );
        AppLogService.info( "\n TOKEN URL " + Constants.URL_TOKEN + "  : " +
            AppPropertiesService.getProperty( Constants.URL_TOKEN ) + "\n" );

        Client client = Client.create(  );

        WebResource webResource = client.resource( AppPropertiesService.getProperty( Constants.URL_TOKEN ) );

        javax.ws.rs.core.MultivaluedMap<String, String> params = new com.sun.jersey.core.util.MultivaluedMapImpl(  );
        params.add( Constants.PARAMS_GRANT_TYPE, Constants.PARAMS_GRANT_TYPE_VALUE );

        ClientResponse response = webResource.type( Constants.CONTENT_FORMAT_TOKEN )
                                             .header( HttpHeaders.AUTHORIZATION,
                Constants.TYPE_AUTHENTIFICATION + " " +
                AppPropertiesService.getProperty( strKeyPropertiesCredentials ) ).accept( MediaType.APPLICATION_JSON )
                                             .post( ClientResponse.class, params );

        String output = response.getEntity( String.class );

        JSONObject strResponseApiManagerJsonObject = null;

        if ( JSONUtils.mayBeJSON( output ) )
        {
            strResponseApiManagerJsonObject = (JSONObject) JSONSerializer.toJSON( output );

            if ( ( strResponseApiManagerJsonObject != null ) &&
                    strResponseApiManagerJsonObject.has( Constants.PARAMS_ACCES_TOKEN ) )
            {
                AppLogService.info( "\n TOKEN JSON RESPONSE \n\n\n\n" + strResponseApiManagerJsonObject.toString( 2 ) +
                    "\n" );
                strToken = (String) strResponseApiManagerJsonObject.get( Constants.PARAMS_ACCES_TOKEN );
                AppLogService.info( "\n TOKEN \n\n\n\n" + strToken + "\n" );
            }
        }

        AppLogService.info( 
            "\n*************************************************************** END TOKEN REQUEST **********************************************************************\n" );

        return strToken;
    }

    @Override
    protected void addAuthentication( Builder builder, String strAuthenticationKey )
    {
        String strToken = getToken( strAuthenticationKey );

        if ( StringUtils.isNotBlank( strToken ) )
        {
            builder.header( HttpHeaders.AUTHORIZATION, Constants.TYPE_AUTHENTIFICATION + " " + strToken );
        }
    }
}
