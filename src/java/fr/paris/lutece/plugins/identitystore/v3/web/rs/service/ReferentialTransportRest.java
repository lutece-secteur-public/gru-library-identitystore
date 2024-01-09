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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.referentiel.AttributeSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.referentiel.LevelSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.referentiel.ProcessusSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IReferentialTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

import java.util.HashMap;
import java.util.Map;

public class ReferentialTransportRest extends AbstractTransportRest implements IReferentialTransportProvider
{
    /** URL for identityStore REST service */
    private final String _strIdentityStoreEndPoint;

    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public ReferentialTransportRest( final IHttpTransportProvider httpTransport )
    {
        super( httpTransport );

        _strIdentityStoreEndPoint = httpTransport.getApiEndPointUrl( );
    }

    @Override
    public ProcessusSearchResponse getProcessList( final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.REFERENTIAL_PATH + Constants.REFERENTIAL_PROCESSUS_PATH;
        return _httpTransport.doGet( url, null, mapHeadersRequest, ProcessusSearchResponse.class, _mapper );
    }

    @Override
    public LevelSearchResponse getLevelList( final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.REFERENTIAL_PATH + Constants.REFERENTIAL_LEVEL_PATH;
        return _httpTransport.doGet( url, null, mapHeadersRequest, LevelSearchResponse.class, _mapper );
    }

    @Override
    public AttributeSearchResponse getAttributeKeyList( final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.REFERENTIAL_PATH + Constants.REFERENTIAL_ATTRIBUTE_KEYS_PATH;
        return _httpTransport.doGet( url, null, mapHeadersRequest, AttributeSearchResponse.class, _mapper );
    }
}
