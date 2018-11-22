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
package fr.paris.lutece.plugins.identitystore.v2.web.rs.service;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.ApplicationRightsDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.IdentityChangeDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.ResponseDto;
import fr.paris.lutece.portal.service.util.AppException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WARNING this is a mock transport for LuteceTestCase purpose
 */
public class MockIdentityTransportRest extends AbstractIdentityTransportRest
{
    private static Logger _logger = Logger.getLogger( MockIdentityTransportRest.class );
    private List<IdentityDto> _listIdentities;
    private final String CONNECTION_ID_PREFIX = "conn_";
    private final String CUSTOMER_ID_PREFIX = "cust_";

    public MockIdentityTransportRest( )
    {
        _logger.error( "MockIdentityTransportRest is used" );
        _listIdentities = new ArrayList<IdentityDto>( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto getIdentity( String strIdConnection, String strCustomerId, String strClientCode ) throws IdentityNotFoundException, AppException
    {
        if ( StringUtils.isEmpty( strIdConnection ) && StringUtils.isEmpty( strCustomerId ) )
        {
            throw new AppException( "params wrong in mock" );
        }
        for ( IdentityDto identityDto : _listIdentities )
        {
            if ( StringUtils.isEmpty( strIdConnection ) || strIdConnection.equals( identityDto.getConnectionId( ) ) )
            {
                if ( StringUtils.isEmpty( strCustomerId ) || strCustomerId.equals( identityDto.getCustomerId( ) ) )
                {
                    return identityDto;
                }
            }
        }

        throw new IdentityNotFoundException( "not found in mock list" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto updateIdentity( IdentityChangeDto identityChange, Map<String, FileItem> mapFileItem ) throws IdentityNotFoundException, AppException
    {
        _logger.debug( "MockIdentityTransportRest.updateIdentity not managed return existing identity if possible" );

        return getIdentity( identityChange.getIdentity( ).getConnectionId( ), identityChange.getIdentity( ).getCustomerId( ), identityChange.getAuthor( )
                .getApplicationCode( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream downloadFileAttribute( String strIdConnection, String strCustomerId, String strAttributeKey, String strClientAppCode )
    {
        _logger.debug( "MockIdentityTransportRest.downloadFileAttribute not managed return null" );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto createIdentity( IdentityChangeDto identityChange ) throws IdentityNotFoundException, AppException
    {
        IdentityDto identity = identityChange.getIdentity( );

        if ( StringUtils.isEmpty( identity.getConnectionId( ) ) )
        {
            identity.setConnectionId( CONNECTION_ID_PREFIX + _listIdentities.size( ) );
        }
        if ( StringUtils.isEmpty( identity.getCustomerId( ) ) )
        {
            identity.setCustomerId( CUSTOMER_ID_PREFIX + _listIdentities.size( ) );
        }
        _listIdentities.add( identity );

        return identity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRightsDto getApplicationRights( String strClientAppCode ) throws AppException
    {
        _logger.debug( "MockIdentityTransportRest.getApplicationRights not managed return empty rights" );
        ApplicationRightsDto appRightsDto = new ApplicationRightsDto( );
        appRightsDto.setAppRights( new ArrayList<>( ) );
        appRightsDto.setApplicationCode( strClientAppCode );

        return appRightsDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addAuthentication( Map<String, String> mapHeadersRequest )
    {
        // no authentication for mock
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto deleteIdentity( String strConnectionId, String strApplicationCode )
    {
        _logger.debug( "MockIdentityTransportRest.deleteIdentity always return ok" );
        ResponseDto response = new ResponseDto( );
        response.setStatus( "OK" );
        response.setMessage( "OK" );

        return response;
    }
}
