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
package fr.paris.lutece.plugins.identitystore.v2.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.service.Constants;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.httpaccess.HttpAccessStatus;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import org.apache.http.client.utils.URIBuilder;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * IHttpTransportProvider which use library-httpaccess
 */
public class HttpAccessTransport implements IHttpTransportProvider
{
    private static Logger _logger = Logger.getLogger( HttpAccessTransport.class );

    /**
     * {@inheritDoc}
     */
    @Override
    public String doPost( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest )
    {
        HttpAccess clientHttp = new HttpAccess( );
        Map<String, String> mapHeadersResponse = new HashMap<String, String>( );

        String strOutput = StringUtils.EMPTY;

        try
        {
            strOutput = clientHttp.doPost( strUrl, mapParams, null, null, mapHeadersRequest, mapHeadersResponse );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return strOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T doPostJSON( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json, Class<T> responseJsonClass,
            ObjectMapper mapper )
    {
        HttpAccess clientHttp = new HttpAccess( );
        Map<String, String> mapHeadersResponse = new HashMap<String, String>( );
        mapHeadersRequest.put( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON );
        mapHeadersRequest.put( HttpHeaders.CONTENT_TYPE, Constants.CONTENT_FORMAT_CHARSET );

        T oResponse = null;

        try
        {
            String strJSON = mapper.writeValueAsString( json );
            String strResponseJSON = clientHttp.doPostJSON( strUrl, strJSON, mapHeadersRequest, mapHeadersResponse );
            oResponse = mapper.readValue( strResponseJSON, responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    @Override
    public <T> T doGet( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Class<T> responseJsonClass,
            ObjectMapper mapper )
    {
        HttpAccess clientHttp = new HttpAccess( );
        T oResponse = null;

        try
        {
            URIBuilder uriBuilder = new URIBuilder( strEndPointUrl );

            if ( ( mapParams != null ) && !mapParams.isEmpty( ) )
            {
                for ( String strParamKey : mapParams.keySet( ) )
                {
                    uriBuilder.addParameter( strParamKey, mapParams.get( strParamKey ) );
                }
            }

            String strResponseJSON = clientHttp.doGet( uriBuilder.toString( ), null, null, mapHeadersRequest );

            oResponse = mapper.readValue( strResponseJSON, responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    @Override
    public <T> T doPostMultiPart( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Map<String, FileItem> mapFiles,
            Class<T> responseJsonClass, ObjectMapper mapper )
    {
        HttpAccess clientHttp = new HttpAccess( );
        T oResponse = null;

        try
        {
            Map<String, List<String>> params = new HashMap<String, List<String>>( );

            if ( mapParams != null )
            {
                for ( String strParamKey : mapParams.keySet( ) )
                {
                    // HttpAccess allow to post for a given param a list of value
                    List<String> listParam = new ArrayList<String>( );
                    listParam.add( mapParams.get( strParamKey ) );
                    params.put( strParamKey, listParam );
                }
            }

            String strResponseJSON = clientHttp.doPostMultiPart( strEndPointUrl, params, mapFiles, null, null, mapHeadersRequest );

            oResponse = mapper.readValue( strResponseJSON, responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    @Override
    public <T> T doDelete( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Class<T> responseJsonClass,
            ObjectMapper mapper )
    {
        HttpAccess clientHttp = new HttpAccess( );
        T oResponse = null;

        try
        {
            URIBuilder uriBuilder = new URIBuilder( strEndPointUrl );

            if ( ( mapParams != null ) && !mapParams.isEmpty( ) )
            {
                for ( String strParamKey : mapParams.keySet( ) )
                {
                    uriBuilder.addParameter( strParamKey, mapParams.get( strParamKey ) );
                }
            }

            String strResponseJSON = clientHttp.doDelete( uriBuilder.toString( ), null, null, mapHeadersRequest, null );

            oResponse = mapper.readValue( strResponseJSON, responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    /**
     * add error log and throw correct Exception depending on the specified Exception
     * 
     * @param e
     *            root exception
     * @throws IdentityNotFoundException
     *             if the specified Exception is an HttpAccessException with HTTP code 404
     * @throws IdentityStoreException
     *             otherwise
     */
    private void handleException( Exception e ) throws IdentityNotFoundException, IdentityStoreException
    {
        String strError = "LibraryIdentityStore - Error HttpAccessTransport :";
        _logger.error( strError + e.getMessage( ), e );

        if ( e instanceof HttpAccessException && HttpAccessStatus.NOT_FOUND.equals( ( (HttpAccessException) e ).getResponseCode( ) ) )
        {
            throw new IdentityNotFoundException( strError, e );
        }
        else
        {
            throw new IdentityStoreException( strError, e );
        }
    }
}
