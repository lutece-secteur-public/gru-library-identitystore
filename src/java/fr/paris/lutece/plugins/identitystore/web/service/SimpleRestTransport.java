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
package fr.paris.lutece.plugins.identitystore.web.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.identitystore.web.rs.service.Constants;


/**
 * RestTransport class, direct access to RestService (no use of HttpAccess lib)
 */
public class SimpleRestTransport implements IHttpTransportProvider
{
    private static Logger _logger = Logger.getLogger( SimpleRestTransport.class );

    /**
     * {@inheritDoc}
     */
    @Override
    public String doPost( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest )
    {
        Client client = Client.create(  );
        WebResource webResource = client.resource( strUrl );

        if ( mapParams != null )
        {
            for ( String strParamKey : mapParams.keySet(  ) )
            {
                webResource = webResource.queryParam( strParamKey, mapParams.get( strParamKey ) );
            }
        }

        WebResource.Builder builder = webResource.type( Constants.CONTENT_FORMAT_TOKEN )
                                                 .accept( MediaType.APPLICATION_JSON_TYPE );

        if ( mapHeadersRequest != null )
        {
            for ( String strHeaderKey : mapHeadersRequest.keySet(  ) )
            {
                builder = builder.header( strHeaderKey, mapHeadersRequest.get( strHeaderKey ) );
            }
        }

        ClientResponse response = builder.post( ClientResponse.class );

        if ( response.getStatus(  ) != Status.OK.getStatusCode(  ) )
        {
            String strError = "LibraryIdentityStore - Error SimpleRestTransport.doPost, status code return " +
                response.getStatus(  );
            _logger.error( strError );
            throw new IdentityStoreException( strError );
        }
        else
        {
            return response.getEntity( String.class );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T doPostJSON( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest,
        Object json, Class<T> responseJsonClass, ObjectMapper mapper )
    {
        T oResponse = null;
        Client client = Client.create(  );
        WebResource webResource = client.resource( strUrl );

        if ( mapParams != null )
        {
            for ( String strParamKey : mapParams.keySet(  ) )
            {
                webResource = webResource.queryParam( strParamKey, mapParams.get( strParamKey ) );
            }
        }

        WebResource.Builder builder = webResource.type( Constants.CONTENT_FORMAT ).accept( MediaType.APPLICATION_JSON );

        if ( mapHeadersRequest != null )
        {
            for ( String strHeaderKey : mapHeadersRequest.keySet(  ) )
            {
                builder = builder.header( strHeaderKey, mapHeadersRequest.get( strHeaderKey ) );
            }
        }

        try
        {
            ClientResponse response = builder.post( ClientResponse.class, mapper.writeValueAsString( json ) );

            if ( ( response.getStatus(  ) != Status.OK.getStatusCode(  ) ) &&
                    ( response.getStatus(  ) != Status.CREATED.getStatusCode(  ) ) )
            {
                String strError = "LibraryIdentityStore - Error SimpleRestTransport.doPostJSON, status code return " +
                    response.getStatus(  );
                _logger.error( strError );
                throw new IdentityStoreException( strError );
            }
            else
            {
                oResponse = mapper.readValue( response.getEntity( String.class ), responseJsonClass );
            }
        }
        catch ( UniformInterfaceException e )
        {
            handleException( e );
        }
        catch ( ClientHandlerException e )
        {
            handleException( e );
        }
        catch ( IOException e )
        {
            handleException( e );
        }

        return oResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T doGet( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest,
        Class<T> responseJsonClass, ObjectMapper mapper )
    {
        T oResponse = null;
        Client client = Client.create(  );
        WebResource webResource = client.resource( strUrl );

        if ( mapParams != null )
        {
            for ( String strParamKey : mapParams.keySet(  ) )
            {
                webResource = webResource.queryParam( strParamKey, mapParams.get( strParamKey ) );
            }
        }

        WebResource.Builder builder = webResource.type( Constants.CONTENT_FORMAT ).accept( MediaType.APPLICATION_JSON );

        if ( mapHeadersRequest != null )
        {
            for ( String strHeaderKey : mapHeadersRequest.keySet(  ) )
            {
                builder = builder.header( strHeaderKey, mapHeadersRequest.get( strHeaderKey ) );
            }
        }

        try
        {
            ClientResponse response = builder.get( ClientResponse.class );

            if ( response.getStatus(  ) == Status.NOT_FOUND.getStatusCode(  ) )
            {
            	throw new IdentityNotFoundException(  );
            }
            else if ( response.getStatus(  ) != Status.OK.getStatusCode(  ) )
            {
                String strError = "LibraryIdentityStore - Error SimpleRestTransport.doGet, status code return " +
                    response.getStatus(  );
                _logger.error( strError );
                throw new IdentityStoreException( strError );
            }
            else
            {
                oResponse = mapper.readValue( response.getEntity( String.class ), responseJsonClass );
            }
        }
        catch ( UniformInterfaceException e )
        {
            handleException( e );
        }
        catch ( ClientHandlerException e )
        {
            handleException( e );
        }
        catch ( IOException e )
        {
            handleException( e );
        }
        return oResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T doPostMultiPart( String strEndPointUrl, Map<String, String> mapParams,
        Map<String, String> mapHeadersRequest, Map<String, FileItem> mapFiles, Class<T> responseJsonClass,
        ObjectMapper mapper )
    {
        T oResponse = null;
        Client client = Client.create(  );
        WebResource webResource = client.resource( strEndPointUrl );

        FormDataMultiPart formParams = new FormDataMultiPart(  );

        WebResource.Builder builder = webResource.type( MediaType.MULTIPART_FORM_DATA );

        if ( mapHeadersRequest != null )
        {
            for ( String strHeaderKey : mapHeadersRequest.keySet(  ) )
            {
                builder = builder.header( strHeaderKey, mapHeadersRequest.get( strHeaderKey ) );
            }
        }

        if ( mapParams != null )
        {
            for ( String strParamKey : mapParams.keySet(  ) )
            {
                formParams.field( strParamKey, mapParams.get( strParamKey ) );
            }
        }

        if ( mapFiles != null )
        {
            for ( String strKey : mapFiles.keySet(  ) )
            {
                FileItem fileItem = mapFiles.get( strKey );
                File file = new File( fileItem.getName(  ) );
                
                //TODO test it 
                FileDataBodyPart filePart = new FileDataBodyPart( strKey, file );
                formParams.field( strKey, fileItem.getName(  ) );
                formParams.bodyPart( filePart );

            }
        }

        ClientResponse response = builder.post( ClientResponse.class, formParams );

        if ( response.getStatus(  ) == Status.NOT_FOUND.getStatusCode(  )  )
        {
        	throw new IdentityNotFoundException(  );
        }
        else if ( response.getStatus(  ) != Status.OK.getStatusCode(  ) )
        {
            throw new IdentityStoreException( Constants.ERROR_MESSAGE + response.getStatus(  ) );
        }

        try
        {
            oResponse = mapper.readValue( response.getEntity( String.class ), responseJsonClass );
        }
        catch ( UniformInterfaceException e )
        {
        	handleException( e );
        }
        catch ( ClientHandlerException e )
        {
            handleException( e );
        }
        catch ( IOException e )
        {
            handleException( e );
        }

        return oResponse;
    }

    /**
     * add error log and throw IdentityStoreException
     * @param e root exception
     * @throws IdentityStoreException identityStore exception
     */
    private void handleException( Exception e ) throws IdentityStoreException
    {
        String strError = "LibraryIdentityStore - Error SimpleRestTransport : ";
        _logger.error( strError + e.getMessage(  ), e );
        throw new IdentityStoreException( strError, e );
    }
}
