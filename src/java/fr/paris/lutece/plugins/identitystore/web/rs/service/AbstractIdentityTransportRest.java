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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityChangeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.service.IIdentityTransportProvider;
import fr.paris.lutece.portal.service.util.AppException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
abstract class AbstractIdentityTransportRest implements IIdentityTransportProvider
{
    private static ObjectMapper _mapper;
    private static Logger _logger = Logger.getLogger( AbstractIdentityTransportRest.class );

    static
    {
        _mapper = new ObjectMapper(  );
        _mapper.enable( DeserializationFeature.UNWRAP_ROOT_VALUE );
        _mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
    }

    /** HTTP transport provider */
    private IHttpTransportProvider _httpTransport;

    /** URL for identityStore REST service */
    private String _strIdentityStoreEndPoint;

    /**
     * setter of identityStoreEndPoint
     * @param strIdentityStoreEndPoint value to use
     */
    public void setIdentityStoreEndPoint( String strIdentityStoreEndPoint )
    {
        this._strIdentityStoreEndPoint = strIdentityStoreEndPoint;
    }

    /**
     * setter of httpTransport
     * @param httpTransport IHttpTransportProvider to use
     */
    public void setHttpTransport( IHttpTransportProvider httpTransport )
    {
        this._httpTransport = httpTransport;
    }

    /**
         * @return the httpTransport
         */
    protected IHttpTransportProvider getHttpTransport(  )
    {
        return _httpTransport;
    }

    /**
    * add specific authentication to request
    * @param mapHeadersRequest map of headers to add
    * @param strAuthenticationKey authentication key
    */
    protected abstract void addAuthentication( Map<String, String> mapHeadersRequest, String strAuthenticationKey );

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto getIdentity( String strIdConnection, int nCustomerId, String strClientCode,
        String strAuthenticationKey ) throws IdentityNotFoundException, AppException
    {
        _logger.debug( "Get identity attributes of " + strIdConnection );

        checkFetchParameters( strIdConnection, nCustomerId, strClientCode, strAuthenticationKey );

        Map<String, String> mapHeadersRequest = new HashMap<String, String>(  );
        addAuthentication( mapHeadersRequest, strAuthenticationKey );

        Map<String, String> mapParams = new HashMap<String, String>(  );
        mapParams.put( Constants.PARAM_ID_CONNECTION, strIdConnection );
        mapParams.put( Constants.PARAM_ID_CUSTOMER, String.valueOf( nCustomerId ) );
        mapParams.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        IdentityDto identityDto = _httpTransport.doGet( _strIdentityStoreEndPoint + Constants.IDENTITY_PATH, mapParams,
                mapHeadersRequest, IdentityDto.class, _mapper );

        return identityDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto updateIdentity( IdentityChangeDto identityChange, String strAuthenticationKey,
        Map<String, FileItem> mapFileItem ) throws IdentityNotFoundException, AppException
    {
        _logger.debug( "Update identity attributes" );
        checkUpdateParameters( identityChange, strAuthenticationKey );

        Map<String, String> mapHeadersRequest = new HashMap<String, String>(  );
        addAuthentication( mapHeadersRequest, strAuthenticationKey );

        Map<String, String> mapParams = new HashMap<String, String>(  );

        String strJsonReq;

        try
        {
            strJsonReq = _mapper.writeValueAsString( identityChange );
            mapParams.put( Constants.PARAM_IDENTITY_CHANGE, strJsonReq );
        }
        catch ( JsonProcessingException e )
        {
            String strError = "AbstractIdentityTransportRest - Error serializing IdentityChangeDto : ";
            _logger.error( strError + e.getMessage(  ), e );
            throw new IdentityStoreException( strError, e );
        }

        IdentityDto identityDto = _httpTransport.doPostMultiPart( _strIdentityStoreEndPoint + Constants.IDENTITY_PATH + Constants.UPDATE_IDENTITY_PATH,
                mapParams, mapHeadersRequest, mapFileItem, IdentityDto.class, _mapper );

        return identityDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream downloadFileAttribute( String strIdConnection, int nCustomerId, String strAttributeKey,
        String strClientAppCode, String strAuthenticationKey )
    {
        checkDownloadFileAttributeParams( strIdConnection, nCustomerId, strAttributeKey, strClientAppCode,
            strAuthenticationKey );

        //            
        //            Map<String, String> mapHeadersRequest = new HashMap<String, String>(  );
        //            addAuthentication( mapHeadersRequest, strAuthenticationKey );
        //
        //            Map<String, String> mapParams = new HashMap<String, String>(  );
        //            mapParams.put( Constants.PARAM_ID_CONNECTION, strIdConnection );
        //            mapParams.put( Constants.PARAM_ID_CUSTOMER, String.valueOf( nCustomerId ) );
        //            mapParams.put( Constants.PARAM_ATTRIBUTE_KEY, strAttributeKey );
        //            mapParams.put( Constants.PARAM_CLIENT_CODE, strClientAppCode );
        //
        //            _httpTransport.doGet( _strIdentityStoreEndPoint + Constants.IDENTITY_PATH, mapParams,
        //                    mapHeadersRequest, IdentityDto.class, _mapper );
        //            
        //            Client client = Client.create(  );
        //            WebResource webResource = client.resource( AppPropertiesService.getProperty( 
        //                        Constants.URL_IDENTITYSTORE_ENDPOINT ) + Constants.IDENTITY_PATH )
        //                                            .queryParam( Constants.PARAM_ID_CONNECTION,
        //                    ( strIdConnection != null ) ? strIdConnection : StringUtils.EMPTY )
        //                                            .queryParam( Constants.PARAM_ID_CUSTOMER, String.valueOf( nCustomerId ) )
        //                                            .queryParam( Constants.PARAM_CLIENT_CODE, strClientAppCode );
        //    
        //            WebResource.Builder builder = webResource.header( Constants.PARAM_CLIENT_CONTROL_KEY, strAuthenticationKey )
        //                                                     .accept( MediaType.WILDCARD_TYPE );
        //          
        //            ClientResponse response = builder.get( ClientResponse.class );
        //    
        //            if ( response.getStatus(  ) != Status.OK.getStatusCode(  ) )
        //            {
        //                throw new AppException( Constants.ERROR_MESSAGE + response.getStatus(  ) );
        //            }
        //    
        InputStream in = null; // response.getEntityInputStream(  );

        return in;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto createIdentity( IdentityChangeDto identityChange, String strAuthenticationKey )
        throws IdentityNotFoundException, AppException
    {
        _logger.debug( "Create identity" );
        checkCreateParameters( identityChange, strAuthenticationKey );

        Map<String, String> mapHeadersRequest = new HashMap<String, String>(  );
        addAuthentication( mapHeadersRequest, strAuthenticationKey );

        Map<String, String> mapParams = new HashMap<String, String>(  );

        String strJsonReq;

        try
        {
            strJsonReq = _mapper.writeValueAsString( identityChange );
            mapParams.put( Constants.PARAM_IDENTITY_CHANGE, strJsonReq );
        }
        catch ( JsonProcessingException e )
        {
            String strError = "AbstractIdentityTransportRest - Error serializing IdentityChangeDto : ";
            _logger.error( strError + e.getMessage(  ), e );
            throw new IdentityStoreException( strError, e );
        }

        IdentityDto identityDto = _httpTransport.doPostMultiPart( _strIdentityStoreEndPoint + Constants.IDENTITY_PATH + Constants.CREATE_IDENTITY_PATH ,
                mapParams, mapHeadersRequest, null, IdentityDto.class, _mapper );

        return identityDto;
    }

    /**
     * check input parameters to get an identity
     * @param strIdConnection connection id
     * @param nCustomerId customer id
     * @param strClientCode client code
     * @param strAuthenticationKey hash code
     * @throws AppException if the parameters are not valid
     */
    private void checkFetchParameters( String strIdConnection, int nCustomerId, String strClientCode,
        String strAuthenticationKey ) throws AppException
    {
        checkIdentity( strIdConnection, nCustomerId );
        checkClientApplication( strClientCode, strAuthenticationKey );
    }

    /**
     * check input parameters to create an identity
     * @param identityChange identity change, ensure that author and identity are filled
     * @param strAuthenticationKey hashcode
     * @throws  AppException if the parameters are not valid
     */
    private void checkCreateParameters( IdentityChangeDto identityChange, String strAuthenticationKey )
        throws AppException
    {
        checkIdentityChange( identityChange );
        checkClientApplication( identityChange.getAuthor(  ).getApplicationCode(  ), strAuthenticationKey );
    }

    /**
     * check input parameters to update an identity
     * @param identityChange identity change, ensure that author and identity are filled
     * @param strAuthenticationKey hashcode
     * @throws  AppException if the parameters are not valid
     */
    private void checkUpdateParameters( IdentityChangeDto identityChange, String strAuthenticationKey )
        throws AppException
    {
        checkIdentityChange( identityChange );
        checkIdentity( identityChange.getIdentity(  ).getConnectionId(  ),
            identityChange.getIdentity(  ).getCustomerId(  ) );
        checkClientApplication( identityChange.getAuthor(  ).getApplicationCode(  ), strAuthenticationKey );
    }

    /**
     * check that parameters are valid, otherwise throws an AppException
     * @param strIdConnection connection id
     * @param nCustomerId customer id
     * @param strAttributeKey attribute Key
     * @param strClientCode client code
     * @param strAuthenticationKey hash code
     * @throws AppException if the parameters are not valid
     */
    private void checkDownloadFileAttributeParams( String strIdConnection, int nCustomerId, String strAttributeKey,
        String strClientCode, String strAuthenticationKey )
        throws AppException
    {
        if ( StringUtils.isEmpty( strAttributeKey ) )
        {
            throw new AppException( "missing parameters : attribute key must be provided" );
        }

        checkIdentity( strIdConnection, nCustomerId );
        checkClientApplication( strClientCode, strAuthenticationKey );
    }

    /**
     * check whether the parameters related to the identity are valid or not
     * @param strIdConnection the connection id
     * @param nCustomerId the customer id
     * @throws AppException if the parameters are not valid
     */
    private void checkIdentity( String strIdConnection, int nCustomerId )
        throws AppException
    {
        if ( StringUtils.isEmpty( strIdConnection ) && ( nCustomerId == 0 ) )
        {
            throw new AppException( "missing parameters : connection Id or customer Id must be provided" );
        }
    }

    /**
     * check whether the parameters related to the application are valid or not
     * @param strClientCode the client code
     * @param strAuthenticationKey the authentication key
     * @throws AppException if the parameters are not valid
     */
    private void checkClientApplication( String strClientCode, String strAuthenticationKey )
        throws AppException
    {
        if ( StringUtils.isEmpty( strClientCode ) )
        {
            throw new AppException( "missing parameters : client Application Code is mandatory" );
        }

        // FIXME : Uncomment this part when application code will be OK
        //        if ( StringUtils.isEmpty( strAuthenticationKey ) )
        //        {
        //            throw new AppException( "missing parameters : client Application strAuthenticationKey is mandatory" );
        //        }
    }

    /**
     * check whether the parameters related to the identity change are valid or not
     * @param identityChange the identity change
     * @throws AppException if the parameters are not valid
     */
    private void checkIdentityChange( IdentityChangeDto identityChange )
        throws AppException
    {
        if ( ( identityChange == null ) || ( identityChange.getAuthor(  ) == null ) ||
                ( identityChange.getIdentity(  ) == null ) )
        {
            throw new AppException( 
                "missing parameters : provided identityChange object is invalid, check author and identity are filled" );
        }
    }
}
