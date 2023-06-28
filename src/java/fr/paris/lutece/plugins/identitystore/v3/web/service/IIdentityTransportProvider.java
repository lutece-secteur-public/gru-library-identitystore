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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.util.AppException;

/**
 * Interface for providing identity transport
 */
public interface IIdentityTransportProvider
{
    /**
     * get identity matching connectionId for provided application code
     *
     * @param strCustomerId
     *            customer Id
     * @param strApplicationCode
     *            application code of calling application
     * @return identity if found
     * @throws IdentityNotFoundException
     *             if no identity found for input parameters
     * @throws AppException
     *             if inconsitent parmeters provided, or errors occurs...
     *
     */
    IdentitySearchResponse getIdentity( String strCustomerId, String strApplicationCode ) throws IdentityStoreException;

    /**
     * Creates an identity only if the identity does not already exist. The identity is created from the provided attributes.
     * 
     * The order to test if the identity exists:
     * 
     * - by using the provided customer id if present - by using the provided connection id if present
     * 
     *
     * @param identityChange
     *            change to apply to identity
     * @return the {@link IdentityChangeResponse}
     *
     * @throws AppException
     *             if error occurred while updating identity
     * @throws IdentityNotFoundException
     */
    IdentityChangeResponse createIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException;

    /**
     * Updates an identity.
     * 
     * @param customerId
     *            customer Id
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            application code who requested identities
     * @return the {@link IdentityChangeResponse}
     *
     * @throws AppException
     *             if error occurred while updating identity
     * @throws IdentityNotFoundException
     */
    IdentityChangeResponse updateIdentity( String customerId, IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException;

    /**
     * Deletes an identity from the specified connectionId
     * 
     * @param strConnectionId
     *            the connection id
     * @param strApplicationCode
     *            the application code on the header
     * @return the {@code ResponseDto} object
     * @throws IdentityNotFoundException
     *             if no identity found for input parameters
     * @throws AppException
     *             if inconsistent parameters provided, or errors occurs...
     */
    ResponseDto deleteIdentity( String strConnectionId, String strApplicationCode ) throws IdentityStoreException;

    /**
     * returns a list of identity from combination of attributes
     *
     * @param identitySearchRequest
     *            change to apply to identity
     * @param strClientCode
     *            application code who requested identities
     * @return identity filled according to application rights for user identified by connection id
     */
    IdentitySearchResponse searchIdentities( IdentitySearchRequest identitySearchRequest, String strClientCode ) throws IdentityStoreException;

    /**
     * returns the active service contract for the given application code
     * 
     * @deprecated Please use {@link IServiceContractTransportProvider} for requests regarding service contract.
     *
     * @param strClientCode
     *            application code who requested service contract
     * @return the active service contract for the given application code
     * @throws IdentityStoreException
     */
    @Deprecated
    ServiceContractSearchResponse getServiceContract( String strClientCode ) throws IdentityStoreException;

    /**
     * import an identity to the id store
     *
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            application code who requested identities
     * @return identity filled according to application rights for user identified by connection id
     */
    IdentityChangeResponse importIdentity( IdentityChangeRequest identityChange, String strClientCode ) throws IdentityStoreException;

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
    IdentityMergeResponse mergeIdentities( IdentityMergeRequest identityMerge, String strClientCode ) throws IdentityStoreException;
}
