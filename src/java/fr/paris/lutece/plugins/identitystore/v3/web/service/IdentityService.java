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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;

/**
 * IdentityService
 */
public class IdentityService
{
    /** transport provider */
    private IIdentityTransportProvider _transportProvider;

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
    public IdentitySearchResponse getIdentityByConnectionId( String strConnectionId, String strApplicationCode ) throws AppException, IdentityStoreException
    {
        final IdentitySearchRequest request = new IdentitySearchRequest( );
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
    public IdentitySearchResponse getIdentityByCustomerId( String strCustomerId, String strApplicationCode ) throws AppException, IdentityStoreException
    {
        return getIdentity( strCustomerId, strApplicationCode );
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
     *             if inconsitent parmeters provided, or errors occurs...
     * @throws IdentityStoreException
     */
    public IdentitySearchResponse getIdentity( String strCustomerId, String strApplicationCode ) throws AppException, IdentityStoreException
    {
        return _transportProvider.getIdentity( strCustomerId, strApplicationCode );
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
     * Deletes an identity from the specified connectionId
     * 
     * @param strConnectionId
     *            the connection id
     * @param strApplicationCode
     *            the application code
     * @throws IdentityStoreException
     */
    public void deleteIdentity( String strConnectionId, String strApplicationCode ) throws IdentityStoreException
    {
        _transportProvider.deleteIdentity( strConnectionId, strApplicationCode );
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
     * returns the active service contract for the given application code
     *
     * @param strClientCode
     *            application code who requested service contract
     * @return the active service contract for the given application code
     * @throws IdentityStoreException
     */
    public ServiceContractSearchResponse getServiceContract( String strClientCode ) throws IdentityStoreException
    {
        return _transportProvider.getServiceContract( strClientCode );
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

}
