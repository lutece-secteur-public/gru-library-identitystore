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
package fr.paris.lutece.plugins.identitystore.v1.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.v1.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppLogService;
import org.apache.commons.fileupload.FileItem;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Map;

/**
 * RestTransport class, direct access to RestService (no use of HttpAccess lib)
 */
public class SimpleRestTransport implements IHttpTransportProvider
{
    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public String doPost( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest ) throws IdentityStoreException
    {
        String strResponse = null;

        try
        {
            Invocation.Builder invocationBuilder = getInvocationBuilder( strUrl, null, mapParams, mapHeadersRequest, MediaType.APPLICATION_JSON );
            Response response = invocationBuilder.post( Entity.entity( "{}", MediaType.APPLICATION_JSON ) );

            if ( response.getStatus( ) != Response.Status.OK.getStatusCode( ) )
            {
                String strError = "LibraryIdentityStore - Error SimpleRestTransport.doPost, status code return " + response.getStatus( );
                AppLogService.error( strError );
                throw new IdentityStoreException( strError );
            }
            else
            {
                strResponse = response.readEntity( String.class );
            }
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return strResponse;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public <T> T doPostJSON( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json, Class<T> responseJsonClass,
            ObjectMapper mapper ) throws IdentityStoreException
    {
        T oResponse = null;

        try
        {
            Invocation.Builder invocationBuilder = getInvocationBuilder( strUrl, null, mapParams, mapHeadersRequest, MediaType.APPLICATION_JSON );

            Response response = invocationBuilder.post( Entity.entity( json, MediaType.APPLICATION_JSON ) );

            if ( ( response.getStatus( ) != Response.Status.OK.getStatusCode( ) ) && ( response.getStatus( ) != Response.Status.CREATED.getStatusCode( ) ) )
            {
                String strError = "LibraryIdentityStore - Error SimpleRestTransport.doPostJSON, status code return " + response.getStatus( );
                AppLogService.error( strError );
                throw new IdentityStoreException( strError );
            }
            else
            {
                oResponse = response.readEntity( responseJsonClass );
            }
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public <T> T doGet( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Class<T> responseJsonClass, ObjectMapper mapper )
            throws IdentityStoreException
    {
        T oResponse = null;
        try
        {
            Invocation.Builder invocationBuilder = getInvocationBuilder( strUrl, null, mapParams, mapHeadersRequest, MediaType.APPLICATION_JSON );

            Response response = invocationBuilder.get( );

            if ( response.getStatus( ) == Response.Status.NOT_FOUND.getStatusCode( ) )
            {
                throw new IdentityNotFoundException( "LibraryIdentityStore - Error SimpleRestTransport - Identity not found." );
            }
            else
                if ( response.getStatus( ) != Response.Status.OK.getStatusCode( ) )
                {
                    String strError = "LibraryIdentityStore - Error SimpleRestTransport.doGet, status code return " + response.getStatus( );
                    AppLogService.error( strError );
                    throw new IdentityStoreException( strError );
                }
                else
                {
                    oResponse = response.readEntity( responseJsonClass );
                }
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public <T> T doPostMultiPart( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Map<String, FileItem> mapFiles,
            Class<T> responseJsonClass, ObjectMapper mapper ) throws IdentityStoreException
    {
        T oResponse = null;

        try
        {
            FormDataMultiPart formParams = new FormDataMultiPart( );

            Invocation.Builder invocationBuilder = getInvocationBuilder( strEndPointUrl, null, mapParams, mapHeadersRequest, MediaType.MULTIPART_FORM_DATA );

            if ( mapFiles != null )
            {
                for ( String strKey : mapFiles.keySet( ) )
                {
                    FileItem fileItem = mapFiles.get( strKey );
                    File file = new File( fileItem.getName( ) );

                    // TODO test it
                    FileDataBodyPart filePart = new FileDataBodyPart( strKey, file );
                    formParams.field( strKey, fileItem.getName( ) );
                    formParams.bodyPart( filePart );
                }
            }

            Response response = invocationBuilder.post( Entity.entity( formParams, formParams.getMediaType( ) ) );

            if ( response.getStatus( ) == Response.Status.NOT_FOUND.getStatusCode( ) )
            {
                throw new IdentityNotFoundException( "LibraryIdentityStore - Error SimpleRestTransport - Identity not found." );
            }
            else
                if ( response.getStatus( ) != Response.Status.OK.getStatusCode( ) )
                {
                    throw new IdentityStoreException( Constants.ERROR_MESSAGE + response.getStatus( ) );
                }

            oResponse = response.readEntity( responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public <T> T doDelete( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Class<T> responseJsonClass,
            ObjectMapper mapper ) throws IdentityStoreException
    {
        T oResponse = null;

        try
        {
            Invocation.Builder invocationBuilder = getInvocationBuilder( strUrl, null, mapParams, mapHeadersRequest, MediaType.APPLICATION_JSON );

            Response response = invocationBuilder.delete( );

            if ( response.getStatus( ) == Response.Status.NOT_FOUND.getStatusCode( ) )
            {
                throw new IdentityNotFoundException( "LibraryIdentityStore - Error SimpleRestTransport - Identity not found." );
            }
            else
                if ( response.getStatus( ) != Response.Status.OK.getStatusCode( ) )
                {
                    String strError = "LibraryIdentityStore - Error SimpleRestTransport.doDelete, status code return " + response.getStatus( );
                    AppLogService.error( strError );
                    throw new IdentityStoreException( strError );
                }
                else
                {
                    oResponse = response.readEntity( responseJsonClass );
                }
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    /**
     * add error log and throw IdentityStoreException
     * 
     * @param e
     *            root exception
     * @throws IdentityStoreException
     *             identityStore exception
     */
    private void handleException( Exception e ) throws IdentityStoreException
    {
        String strError = "LibraryIdentityStore - Error SimpleRestTransport : ";
        AppLogService.error( strError + e.getMessage( ), e );
        throw new IdentityStoreException( strError, e );
    }

    /**
     * Get invocation builder from config
     * 
     * @param strUrl
     * @param strPath
     * @param mapParams
     * @param mapHeadersRequest
     * @return the invocation builder
     */
    private Invocation.Builder getInvocationBuilder( String strUrl, String strPath, Map<String, String> mapParams, Map<String, String> mapHeadersRequest,
            String strRequestMediaType )
    {
        Client client = ClientBuilder.newClient( new ClientConfig( ) );
        WebTarget webTarget = client.target( strUrl );

        if ( strPath != null )
        {
            webTarget = webTarget.path( strPath );
        }

        if ( mapParams != null )
        {
            for ( String strParamKey : mapParams.keySet( ) )
            {
                webTarget = webTarget.queryParam( strParamKey, mapParams.get( strParamKey ) );
            }
        }

        Invocation.Builder invocationBuilder = webTarget.request( strRequestMediaType ).accept( MediaType.APPLICATION_JSON );
        if ( mapHeadersRequest != null )
        {
            for ( String strHeaderKey : mapHeadersRequest.keySet( ) )
            {
                invocationBuilder.header( strHeaderKey, mapHeadersRequest.get( strHeaderKey ) );
            }
        }

        return invocationBuilder;
    }
}
