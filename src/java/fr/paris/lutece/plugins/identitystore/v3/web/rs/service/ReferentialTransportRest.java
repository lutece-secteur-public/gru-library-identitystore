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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractsSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.referentiel.LevelSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.referentiel.ProcessusSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IReferentialTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IServiceContractTransportProvider;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;

public class ReferentialTransportRest extends AbstractTransportRest implements IReferentialTransportProvider
{

    /** logger */
    private static Logger _logger = Logger.getLogger( ReferentialTransportRest.class );

    /** URL for identityStore REST service */
    private String _strIdentityStoreEndPoint;

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
	public ProcessusSearchResponse getProcessList() throws IdentityStoreException {
		_logger.debug( "Get process list");

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        final Map<String, String> mapParams = new HashMap<>( );

        final ProcessusSearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.REFERENTIAL_PATH + Constants.REFERENTIAL_PROCESSUS_PATH, 
                mapParams, mapHeadersRequest,
                ProcessusSearchResponse.class, _mapper );

        return response;
	}

	@Override
	public LevelSearchResponse getLevelList() throws IdentityStoreException {
		_logger.debug( "Get level list");

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        final Map<String, String> mapParams = new HashMap<>( );

        final LevelSearchResponse response = _httpTransport.doGet(
                _strIdentityStoreEndPoint + Constants.VERSION_PATH_V3 + Constants.REFERENTIAL_PATH + Constants.REFERENTIAL_LEVEL_PATH, 
                mapParams, mapHeadersRequest,
                LevelSearchResponse.class, _mapper );

        return response;
	}
}
