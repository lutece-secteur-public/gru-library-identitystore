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
package fr.paris.lutece.plugins.identitystore.v3.web.service;

import fr.paris.lutece.plugins.identitystore.v3.business.IExternalAttributeSource;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.exporting.IdentityExportRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.exporting.IdentityExportResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistoryGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;

/**
 * IdentityService
 */
public class IdentityService
{
    /** transport provider */
    protected IIdentityTransportProvider _transportProvider;
    protected List<IExternalAttributeSource> _listExternalAttributesSource;

    /**
     * Simple Constructor
     */
    public IdentityService( )
    {
        super( );
    }

    /**
     * Constructor with IIdentityTransportProvider in parameters
     * 
     * @param transportProvider
     *            IIdentityTransportProvider
     */
    public IdentityService( IIdentityTransportProvider transportProvider )
    {
        super( );
        this._transportProvider = transportProvider;
    }

    /**
     * setter of transportProvider parameter
     * 
     * @param transportProvider
     *            IIdentityTransportProvider
     */
    public void setTransportProvider( IIdentityTransportProvider transportProvider )
    {
        this._transportProvider = transportProvider;
    }

    /**
     * setter of additional attribute source parameter
     * 
     * @param listExternalAttributesSource
     */
    public void setExternalAttributesSourceList( List<IExternalAttributeSource> listExternalAttributesSource )
    {
        this._listExternalAttributesSource = listExternalAttributesSource;
    }

    /**
     * get identity matching connectionId for provided application code
     *
     * @param strConnectionId
     *            connection Id
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return identity if found
     * @throws AppException
     *             if inconsitent parmeters provided, or errors occurs...
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse getIdentityByConnectionId( final String strConnectionId, final String strClientCode, final RequestAuthor author )
            throws AppException, IdentityStoreException
    {
        final IdentitySearchRequest request = new IdentitySearchRequest( );
        request.setConnectionId( strConnectionId );
        return searchIdentities( request, strClientCode, author );
    }

    /**
     * get identity matching customerId for provided application code
     *
     * @param strCustomerId
     *            customer Id
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return identity if found
     * @throws AppException
     *             if inconsitent parmeters provided, or errors occurs...
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse getIdentityByCustomerId( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws AppException, IdentityStoreException
    {
        return getIdentity( strCustomerId, strClientCode, author );
    }

    /**
     * get identity matching connectionId and customerId for provided application code
     *
     * @param strCustomerId
     *            customer Id (can be null if strConnectionId is provided)
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return identity if found
     * @throws AppException
     *             if inconsistent parameters provided, or errors occurs...
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse getIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws AppException, IdentityStoreException
    {
        IdentitySearchResponse identitySearchResponse = _transportProvider.getIdentity( strCustomerId, strClientCode, author );

        return identitySearchResponseWithAdditionnalData( identitySearchResponse );
    }

    /**
     * apply changes to an identity
     *
     * @param customerId
     *            the id of the customer
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the updated identity
     * @throws AppException
     *             if error occured while updating identity
     * @throws IdentityStoreException
     */
    public IdentityChangeResponse updateIdentity( final String customerId, final IdentityChangeRequest identityChange, final String strClientCode,
            final RequestAuthor author ) throws AppException, IdentityStoreException
    {
        return _transportProvider.updateIdentity( customerId, identityChange, strClientCode, author );
    }

    /**
     * Creates an identity only if the identity does not already exist. The identity is created from the provided attributes.
     * <p>
     * The order to test if the identity exists: - by using the provided customer id if present - by using the provided connection id if present
     *
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the created identity
     * @throws AppException
     *             if error occured while updating identity
     * @throws IdentityStoreException
     */
    public IdentityChangeResponse createIdentity( final IdentityChangeRequest identityChange, final String strClientCode, final RequestAuthor author )
            throws AppException, IdentityStoreException
    {
        return _transportProvider.createIdentity( identityChange, strClientCode, author );
    }

    /**
     * Deletes an identity from the specified customer_Id
     * 
     * @param strCustomerId
     *            the customer id
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @throws IdentityStoreException
     */
    public IdentityChangeResponse deleteIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.deleteIdentity( strCustomerId, strClientCode, author );
    }

    /**
     * returns a list of identity from combination of attributes
     *
     * @param identitySearchRequest
     *            search request tpo perform
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return identity filled according to application rights for user identified by connection id
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse searchIdentities( final IdentitySearchRequest identitySearchRequest, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.searchIdentities( identitySearchRequest, strClientCode, author );
    }

    /**
     * Gives the identity history (identity+attributes) from a customerID
     *
     * @param strCustomerId
     *            customerID
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the history
     */
    public IdentityHistoryGetResponse getIdentityHistory( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.getIdentityHistory( strCustomerId, strClientCode, author );
    }

    /**
     * Search for identities history according to given request
     *
     * @param request
     *            request
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the history
     */
    public IdentityHistorySearchResponse searchIdentityHistory( final IdentityHistorySearchRequest request, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        return _transportProvider.searchIdentityHistory( request, strClientCode, author );
    }

    /**
     * get identities that have been updated, according to given request.
     * 
     * @param request
     *            the request
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the list of identities
     */
    public UpdatedIdentitySearchResponse getUpdatedIdentities( final UpdatedIdentitySearchRequest request, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        return _transportProvider.getUpdatedIdentities( request, strClientCode, author );
    }

    /**
     * Exports a list of identities according to the provided request and client code.
     *
     * @param request
     *            the export request
     * @param strClientCode
     *            the client code
     * @return IdentityExportResponse
     */
    public IdentityExportResponse exportIdentities( final IdentityExportRequest request, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        return _transportProvider.exportIdentities( request, strClientCode, author );
    }

    /**
     * Complete attribute list with external sources
     * 
     * @param identitySearchResponse
     *            the search response to be completed
     * @return the IdentitySearchResponse
     */
    private IdentitySearchResponse identitySearchResponseWithAdditionnalData( final IdentitySearchResponse identitySearchResponse )
            throws IdentityStoreException
    {
        // none
        if ( _listExternalAttributesSource == null )
        {
            return identitySearchResponse;
        }

        try
        {
            // Check all external sources
            for ( IExternalAttributeSource source : _listExternalAttributesSource )
            {
                // fill each identity
                for ( final IdentityDto identity : identitySearchResponse.getIdentities( ) )
                {
                    List<AttributeDto> listAdditionnalAttributes = source.getAdditionnalAttributes( identity.getCustomerId( ) );

                    identity.getAttributes( ).addAll( listAdditionnalAttributes );
                }
            }
        }
        catch( Exception e )
        {
            throw new IdentityStoreException( e.getMessage( ) );
        }

        return identitySearchResponse;
    }

}
