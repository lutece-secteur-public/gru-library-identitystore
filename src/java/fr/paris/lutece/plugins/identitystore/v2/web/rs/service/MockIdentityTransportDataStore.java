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
package fr.paris.lutece.plugins.identitystore.v2.web.rs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.ApplicationRightsDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.IdentityChangeDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v2.web.rs.dto.ResponseDto;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Map;

/**
 * WARNING this is a mock transport for LuteceTestCase purpose
 */
public class MockIdentityTransportDataStore extends AbstractIdentityTransportRest
{
    private static final String KEY_DATASTORE_MOCK_IDENTITY_PREFIX = "identitystore.mock.identity.data.";
    private static final String KEY_DATASTORE_MOCK_APPLICATION_RIGHTS_PREFIX = "identitystore.mock.applicationRights.data.";

    private static ObjectMapper _mapper = new ObjectMapper( );

    public MockIdentityTransportDataStore( )
    {
        AppLogService.error( "MockIdentityTransportDatastore is used" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto getIdentity( String strIdConnection, String strCustomerId, String strClientCode ) throws AppException, IdentityStoreException
    {
        if ( StringUtils.isEmpty( strIdConnection ) && StringUtils.isEmpty( strCustomerId ) )
        {
            throw new AppException( "params wrong in ds mock" );
        }

        String strId = StringUtils.isEmpty( strIdConnection ) ? strCustomerId : strIdConnection;

        return getMockIdentityFromDatastore( strId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto updateIdentity( IdentityChangeDto identityChange, Map<String, FileItem> mapFileItem ) throws AppException
    {
        AppLogService.debug( "MockIdentityTransportDatastore.updateIdentity not managed return existing identity if possible" );

        return getMockIdentityFromDatastore( identityChange.getIdentity( ).getConnectionId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream downloadFileAttribute( String strIdConnection, String strCustomerId, String strAttributeKey, String strClientAppCode )
    {
        AppLogService.debug( "MockIdentityTransportDataStore.downloadFileAttribute not managed return null" );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityDto createIdentity( IdentityChangeDto identityChange ) throws AppException
    {
        AppLogService.debug( "MockIdentityTransportDataStore.createIdentity not managed " );

        return getMockIdentityFromDatastore( identityChange.getIdentity( ).getConnectionId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRightsDto getApplicationRights( String strClientAppCode ) throws AppException
    {
        AppLogService.debug( "MockIdentityTransportRest.getApplicationRights not managed return rights if exists in DS" );

        return getMockApplicationRightsFromDatastore( strClientAppCode );
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
        AppLogService.debug( "MockIdentityTransportDatastore.deleteIdentity always return ok" );

        ResponseDto response = new ResponseDto( );
        response.setStatus( "OK" );
        response.setMessage( "OK" );

        return response;
    }

    /**
     * get the mock json identity from DataStore
     * 
     * @param strId
     * @return the identity Dto
     */
    private IdentityDto getMockIdentityFromDatastore( String strId )
    {
        String strDsData = DatastoreService.getDataValue( KEY_DATASTORE_MOCK_IDENTITY_PREFIX + strId, "{}" );

        try
        {
            return _mapper.readValue( strDsData, IdentityDto.class );
        }
        catch( Exception e )
        {
            AppLogService.error( "MockIdentityFromDatastore : Error while mapping DS data to IdentityDto", e );

            return null;
        }
    }

    /**
     * get the mock json rights from DataStore
     * 
     * @param strId
     * @return the identity Dto
     */
    private ApplicationRightsDto getMockApplicationRightsFromDatastore( String strId )
    {
        String strDsData = DatastoreService.getDataValue( KEY_DATASTORE_MOCK_APPLICATION_RIGHTS_PREFIX + strId, "{}" );

        try
        {
            return _mapper.readValue( strDsData, ApplicationRightsDto.class );
        }
        catch( Exception e )
        {
            AppLogService.error( "MockIdentityFromDatastore : Error while mapping DS data to IdentityDto", e );

            return null;
        }
    }
}
