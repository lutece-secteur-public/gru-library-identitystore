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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.duplicate.DuplicateRuleSummarySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IIdentityQualityTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * IdentityQualityRestClientService
 */
public class IdentityQualityTransportRest extends AbstractTransportRest implements IIdentityQualityTransportProvider
{

    /**
     * Logger
     */
    private static Logger _logger = Logger.getLogger( IdentityQualityTransportRest.class );

    /** URL for identityStore Quality REST service */
    private String _strIdentityStoreQualityEndPoint;
    
    /**
     * Simple Constructor
     */
    public IdentityQualityTransportRest( )
    {
        super( new HttpAccessTransport( ) );
    }

    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public IdentityQualityTransportRest( final IHttpTransportProvider httpTransport )
    {
        super( httpTransport );
        
        _strIdentityStoreQualityEndPoint = httpTransport.getApiEndPointUrl( );
    }

    @Override
    public DuplicateRuleSummarySearchResponse getAllDuplicateRules( final String strApplicationCode ) throws IdentityStoreException
    {
        _logger.debug( "Get duplicate rules of " + strApplicationCode );

        this.checkClientCode( strApplicationCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strApplicationCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final DuplicateRuleSummarySearchResponse response = _httpTransport.doGet(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.RULES_PATH, mapParams,
                mapHeadersRequest, DuplicateRuleSummarySearchResponse.class, _mapper );

        return response;
    }
    
    /**
     * check whether the parameters related to the identity are valid or not
     *
     * @param strClientCode
     *            the strClientCode
     * @throws AppException
     *             if the parameters are not valid
     */
    public void checkClientCode( String strClientCode ) throws IdentityStoreException
    {
        if ( StringUtils.isBlank( strClientCode ) )
        {
            throw new IdentityStoreException( "Client code is mandatory." );
        }
    }
}
