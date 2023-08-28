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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.error.ErrorResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.CustomResponseStatusValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.InvalidResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.log4j.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IHttpTransportProvider which use library-httpaccess
 */
public class HttpAccessTransport implements IHttpTransportProvider
{
    private static Logger _logger = Logger.getLogger( HttpAccessTransport.class );

    protected HttpAccess _httpClient;
    protected String _strEndPoint;

    public HttpAccessTransport( )
    {
        this._httpClient = new HttpAccess( CustomResponseStatusValidator.getInstance( ) );
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public String doPost( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest ) throws IdentityStoreException
    {
        final Map<String, String> mapHeadersResponse = new HashMap<>( );

        String strOutput = StringUtils.EMPTY;

        try
        {
            addAuthentication( mapHeadersRequest );

            strOutput = this._httpClient.doPost( strUrl, mapParams, null, null, mapHeadersRequest, mapHeadersResponse );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return strOutput;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IdentityStoreException
     */
    @Override
    public <T extends ResponseDto> T doPostJSON( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json,
            Class<T> responseJsonClass, ObjectMapper mapper ) throws IdentityStoreException
    {
        final Map<String, String> mapHeadersResponse = new HashMap<>( );
        mapHeadersRequest.put( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON );
        mapHeadersRequest.put( HttpHeaders.CONTENT_TYPE, Constants.CONTENT_FORMAT_CHARSET );

        T oResponse = null;

        try
        {
            addAuthentication( mapHeadersRequest );

            String strJSON = mapper.writeValueAsString( json );
            String strResponseJSON = this._httpClient.doPostJSON( strUrl, strJSON, mapHeadersRequest, mapHeadersResponse );
            oResponse = mapJson( mapper, strResponseJSON, responseJsonClass );
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
    public <T extends ResponseDto> T doPutJSON( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json,
            Class<T> responseJsonClass, ObjectMapper mapper ) throws IdentityStoreException
    {
        final Map<String, String> mapHeadersResponse = new HashMap<>( );
        mapHeadersRequest.put( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON );
        mapHeadersRequest.put( HttpHeaders.CONTENT_TYPE, Constants.CONTENT_FORMAT_CHARSET );

        T oResponse = null;

        try
        {
            addAuthentication( mapHeadersRequest );

            String strJSON = mapper.writeValueAsString( json );
            String strResponseJSON = this._httpClient.doPutJSON( strUrl, strJSON, mapHeadersRequest, mapHeadersResponse );
            oResponse = mapJson( mapper, strResponseJSON, responseJsonClass );
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
    public <T extends ResponseDto> List<T> doPostJSONforList( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json,
            Class<T> responseJsonClass, ObjectMapper mapper ) throws IdentityStoreException
    {
        final Map<String, String> mapHeadersResponse = new HashMap<>( );
        mapHeadersRequest.put( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON );
        mapHeadersRequest.put( HttpHeaders.CONTENT_TYPE, Constants.CONTENT_FORMAT_CHARSET );

        List<T> oResponse = null;

        try
        {
            addAuthentication( mapHeadersRequest );

            String strJSON = mapper.writeValueAsString( json );
            String strResponseJSON = this._httpClient.doPostJSON( strUrl, strJSON, mapHeadersRequest, mapHeadersResponse );
            oResponse = mapJsonList( mapper, strResponseJSON, responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    @Override
    public <T extends ResponseDto> T doGet( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest,
            Class<T> responseJsonClass, ObjectMapper mapper ) throws IdentityStoreException
    {
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

            addAuthentication( mapHeadersRequest );

            String strResponseJSON = this._httpClient.doGet( uriBuilder.toString( ), null, null, mapHeadersRequest );

            oResponse = mapJson( mapper, strResponseJSON, responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    @Override
    public <T extends ResponseDto> T doDelete( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest,
            Class<T> responseJsonClass, ObjectMapper mapper ) throws IdentityStoreException
    {
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

            addAuthentication( mapHeadersRequest );

            String strResponseJSON = this._httpClient.doDelete( uriBuilder.toString( ), null, null, mapHeadersRequest, null );

            oResponse = mapJson( mapper, strResponseJSON, responseJsonClass );
        }
        catch( Exception e )
        {
            handleException( e );
        }

        return oResponse;
    }

    @Override
    public <T extends ResponseDto> T doDeleteJSON( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json,
            Class<T> responseJsonClass, ObjectMapper mapper ) throws IdentityStoreException
    {
        final Map<String, String> mapHeadersResponse = new HashMap<>( );
        mapHeadersRequest.put( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON );
        mapHeadersRequest.put( HttpHeaders.CONTENT_TYPE, Constants.CONTENT_FORMAT_CHARSET );

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

            addAuthentication( mapHeadersRequest );
            String strJSON = mapper.writeValueAsString( json );

            String strResponseJSON = this._httpClient.doDeleteJSON( uriBuilder.toString( ), strJSON, null, null, mapHeadersRequest, null );

            oResponse = mapJson( mapper, strResponseJSON, responseJsonClass );
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
    protected void handleException( Exception e ) throws IdentityStoreException
    {
        String strError = "LibraryIdentityStore - Error HttpAccessTransport :";
        _logger.error( strError + e.getMessage( ), e );

        if ( e instanceof InvalidResponseStatus && HttpStatus.SC_NOT_FOUND == ( (InvalidResponseStatus) e ).getResponseStatus( )
                || e instanceof IdentityNotFoundException )
        {
            // throw new IdentityNotFoundException( strError, e );
            throw new IdentityStoreException( strError, e );
        }
        else
        {
            throw new IdentityStoreException( strError, e );
        }
    }

    /**
     * add specific authentication to request
     * 
     * @param mapHeadersRequest
     *            map of headers to add
     * @throws IdentityStoreException
     */
    protected void addAuthentication( Map<String, String> mapHeadersRequest ) throws IdentityStoreException
    {
        // default : no authentication
    }

    /**
     * set end point
     * 
     * @param strApiEndPointUrl
     */
    public void setApiEndPointUrl( String strApiEndPointUrl )
    {

        _strEndPoint = strApiEndPointUrl;
    }

    /**
     * get end point
     * 
     * @return strEndPointUrl
     */
    public String getApiEndPointUrl( )
    {

        return _strEndPoint;
    }

    /**
     * Converts json String response to the desired {@link ResponseDto} subclass instance.
     * 
     * @param mapper
     *            the mapper
     * @param jsonStr
     *            the json string value
     * @param responseClass
     *            the desired {@link ResponseDto} subclass
     * @return the desired {@link ResponseDto} subclass instance
     */
    private <T extends ResponseDto> T mapJson( final ObjectMapper mapper, final String jsonStr, final Class<T> responseClass )
            throws JsonProcessingException, InstantiationException, IllegalAccessException
    {
        T response = null;
        try
        {
            response = mapper.readValue( jsonStr, responseClass );
        }
        catch( final Exception e )
        {
            // If mapper didn't manage to map the json with the desired class, we try to map it as an error response
            final ErrorResponse er = mapper.readValue( jsonStr, ErrorResponse.class );
            if ( er != null )
            {
                // If it is an error response, we need to convert it to the desired response class
                response = responseClass.newInstance( );
                response.setStatus( er.getStatus( ) );
                response.setMessage( er.getMessage( ) );
                response.setI18nMessageKey( er.getI18nMessageKey( ) );
            }
        }
        return response;
    }

    /**
     * Converts json String response to a {@link List} of the desired {@link ResponseDto} subclass instances.
     * 
     * @param mapper
     *            the mapper
     * @param jsonStr
     *            the json string value
     * @param responseClass
     *            the desired {@link ResponseDto} subclass
     * @return a {@link List} of the desired {@link ResponseDto} subclass instances
     */
    private <T extends ResponseDto> List<T> mapJsonList( final ObjectMapper mapper, final String jsonStr, final Class<T> responseClass )
            throws JsonProcessingException, InstantiationException, IllegalAccessException
    {
        List<T> responseList = new ArrayList<>( );
        JavaType responseListClassType = mapper.getTypeFactory( ).constructCollectionType( List.class, responseClass );
        try
        {
            responseList = mapper.readValue( jsonStr, responseListClassType );
        }
        catch( final Exception e1 )
        {
            try
            {
                // If mapper didn't manage to map the json with the desired class, we try to map it as a list of error response
                responseListClassType = mapper.getTypeFactory( ).constructCollectionType( List.class, ErrorResponse.class );
                final List<ErrorResponse> errorResponseList = mapper.readValue( jsonStr, responseListClassType );
                if ( errorResponseList != null && !errorResponseList.isEmpty( ) )
                {
                    // If it is an error responseList, we need to convert it to the desired responseList class
                    for ( final ErrorResponse er : errorResponseList )
                    {
                        final T response = responseClass.newInstance( );
                        response.setStatus( er.getStatus( ) );
                        response.setMessage( er.getMessage( ) );
                        response.setI18nMessageKey( er.getI18nMessageKey( ) );
                        responseList.add( response );
                    }
                }
            }
            catch( final Exception e2 )
            {
                // If mapper didn't manage to map the json with a list of error response, we try to map it as single error response
                final ErrorResponse er = mapper.readValue( jsonStr, ErrorResponse.class );
                if ( er != null )
                {
                    // If it is an error responseList, we need to convert it to the desired responseList class
                    final T response = responseClass.newInstance( );
                    response.setStatus( er.getStatus( ) );
                    response.setMessage( er.getMessage( ) );
                    response.setI18nMessageKey( er.getI18nMessageKey( ) );
                    responseList.add( response );
                }
            }
        }
        return responseList;
    }

}
