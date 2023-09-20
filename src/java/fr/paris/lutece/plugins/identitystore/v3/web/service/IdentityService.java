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
package fr.paris.lutece.plugins.identitystore.v3.web.service;

import java.util.List;

import fr.paris.lutece.plugins.identitystore.v2.business.IExternalAttributeSource;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistoryGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;

/**
 * IdentityService
 */
public class IdentityService
{
    /** transport provider */
    private IIdentityTransportProvider _transportProvider;
    private List<IExternalAttributeSource> _listExternalAttributesSource;

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
     * @param strApplicationCode
     *            application code of calling application
     * @return identity if found
     * @throws AppException
     *             if inconsitent parmeters provided, or errors occurs...
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse getIdentityByConnectionId( String strConnectionId, String strApplicationCode, RequestAuthor author )
            throws AppException, IdentityStoreException
    {
        final IdentitySearchRequest request = new IdentitySearchRequest( );
        request.setOrigin( author );
        request.setConnectionId( strConnectionId );
        return searchIdentities( request, strApplicationCode );
    }

    /**
     * get identity matching customerId for provided application code
     *
     * @param strCustomerId
     *            customer Id
     * @param strApplicationCode
     *            application code of calling application
     * @return identity if found
     * @throws AppException
     *             if inconsitent parmeters provided, or errors occurs...
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse getIdentityByCustomerId( String strCustomerId, String strApplicationCode, RequestAuthor origin )
            throws AppException, IdentityStoreException
    {
        return getIdentity( strCustomerId, strApplicationCode, origin );
    }

    /**
     * get identity matching connectionId and customerId for provided application code
     *
     * @param strCustomerId
     *            customer Id (can be null if strConnectionId is provided)
     * @param strApplicationCode
     *            application code of calling application
     * @return identity if found
     * @throws AppException
     *             if inconsistent parameters provided, or errors occurs...
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse getIdentity( String strCustomerId, String strApplicationCode, RequestAuthor origin )
            throws AppException, IdentityStoreException
    {
        IdentitySearchResponse identitySearchResponse = _transportProvider.getIdentity( strCustomerId, strApplicationCode, origin );

        return identitySearchResponseWithAdditionnalData( identitySearchResponse );
    }

    /**
     * apply changes to an identity
     *
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            application code of calling application
     * @return the updated identity
     * @throws AppException
     *             if error occured while updating identity
     * @throws IdentityStoreException
     */
    public IdentityChangeResponse updateIdentity( String customerId, IdentityChangeRequest identityChange, String strClientCode )
            throws AppException, IdentityStoreException
    {
        return _transportProvider.updateIdentity( customerId, identityChange, strClientCode );
    }

    /**
     * Creates an identity only if the identity does not already exist. The identity is created from the provided attributes.
     * <p>
     * The order to test if the identity exists: - by using the provided customer id if present - by using the provided connection id if present
     *
     * @param identityChange
     *            change to apply to identity
     * @return the created identity
     * @throws AppException
     *             if error occured while updating identity
     * @throws IdentityStoreException
     */
    public IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws AppException, IdentityStoreException
    {
        return _transportProvider.createIdentity( identityChange, strClientCode );
    }

    /**
     * Deletes an identity from the specified customer_Id
     * 
     * @param strCustomerId
     *            the customer id
     * @param strApplicationCode
     *            the application code
     * @param identityChange
     *            : an identiyChange object to provide the RequestAuthor values
     * @throws IdentityStoreException
     */
    public void deleteIdentity( String strCustomerId, String strApplicationCode, IdentityChangeRequest identityChange ) throws IdentityStoreException
    {
        _transportProvider.deleteIdentity( strCustomerId, strApplicationCode, identityChange );
    }

    /**
     * returns a list of identity from combination of attributes
     *
     * @param identitySearchRequest
     *            search request tpo perform
     * @param strClientCode
     *            application code who requested identities
     * @return identity filled according to application rights for user identified by connection id
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse searchIdentities( IdentitySearchRequest identitySearchRequest, String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.searchIdentities( identitySearchRequest, strClientCode );
    }

    /**
     * import an identity
     *
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            application code who requested identities
     * @return identity filled according to application rights for user identified by connection id
     * @throws IdentityStoreException
     */
    public IdentityChangeResponse importIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.importIdentity( identityChange, strClientCode );
    }

    /**
     * Merge two identities.
     * 
     * @param identityMerge
     *            the request containing the master cuid, the secondary cuid, and a list of attribute to be taken from the secondary identity and put on the
     *            master identity.
     * @param strClientCode
     *            the client code
     * @return IdentityMergeResponse
     */
    public IdentityMergeResponse mergeIdentities( IdentityMergeRequest identityMerge, String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.mergeIdentities( identityMerge, strClientCode );
    }

    /**
     * Unmerge two identities.
     *
     * @param identityMerge
     *            the request containing the master cuid, the secondary cuid
     * @param strClientCode
     *            the client code
     * @return IdentityMergeResponse
     */
    public IdentityMergeResponse unMergeIdentities( IdentityMergeRequest identityMerge, String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.unMergeIdentities( identityMerge, strClientCode );
    }

    /**
     * Gives the identity history (identity+attributes) from a customerID
     *
     * @param strCustomerId
     *            customerID
     * @param strClientCode
     *            client code
     * @return the history
     */
    public IdentityHistoryGetResponse getIdentityHistory( String strCustomerId, String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.getIdentityHistory( strCustomerId, strClientCode );
    }

    /**
     * Search for identities history according to given request
     *
     * @param request
     *            request
     * @param strClientCode
     *            client code
     * @return the history
     */
    public IdentityHistorySearchResponse searchIdentityHistory( final IdentityHistorySearchRequest request, String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.searchIdentityHistory( request, strClientCode );
    }

    /**
     * get identities that have been updated during the previous `days`.
     * 
     * @param strDays
     *            max number of days since the last update
     * @return the list of identities
     */
    public UpdatedIdentitySearchResponse getUpdatedIdentities( String strDays, String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.getUpdatedIdentities( strDays, strClientCode );
    }

    /**
     * Dé-certification d'une identité.<br/>
     * Une identité ne pouvant pas posséder d'attributs non-certifiés, une dé-certification implique la certification de ses attributs avec le processus défini
     * par la property : <code>identitystore.identity.uncertify.processus</code> (par défaut : "dec", qui correspond au niveau le plus faible de certification
     * (auto-déclaratif))
     *
     * @param strCustomerId
     *            the customer ID
     * @param strClientCode
     *            the client code
     * @param origin
     *            the origin
     * @return IdentityChangeResponse
     */
    IdentityChangeResponse uncertifyIdentity( String strCustomerId, String strClientCode, RequestAuthor origin ) throws IdentityStoreException
    {
        return _transportProvider.uncertifyIdentity( strCustomerId, strClientCode, origin );
    }

    /**
     * Complete attribute list with external sources
     * 
     * @param identitySearchResponse
     * @return the IdentitySearchResponse
     */
    private IdentitySearchResponse identitySearchResponseWithAdditionnalData( IdentitySearchResponse identitySearchResponse ) throws IdentityStoreException
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
