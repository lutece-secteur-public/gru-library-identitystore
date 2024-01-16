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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.IdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.exporting.IdentityExportRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.exporting.IdentityExportResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistoryGetResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.history.IdentityHistorySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.merge.IdentityMergeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.UpdatedIdentitySearchResponse;
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
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            author of the request
     * @return identity if found
     * @throws IdentityNotFoundException
     *             if no identity found for input parameters
     * @throws AppException
     *             if inconsitent parmeters provided, or errors occurs...
     *
     */
    IdentitySearchResponse getIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author ) throws IdentityStoreException;

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
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the {@link IdentityChangeResponse}
     *
     * @throws AppException
     *             if error occurred while updating identity
     * @throws IdentityNotFoundException
     */
    IdentityChangeResponse createIdentity( final IdentityChangeRequest identityChange, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

    /**
     * Updates an identity.
     * 
     * @param customerId
     *            customer Id
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the {@link IdentityChangeResponse}
     *
     * @throws AppException
     *             if error occurred while updating identity
     * @throws IdentityNotFoundException
     */
    IdentityChangeResponse updateIdentity( final String customerId, final IdentityChangeRequest identityChange, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException;

    /**
     * Deletes an identity from the specified connectionId
     *
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the {@code ResponseDto} object
     * @throws IdentityNotFoundException
     *             if no identity found for input parameters
     * @throws AppException
     *             if inconsistent parameters provided, or errors occurs...
     */
    IdentityChangeResponse deleteIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author ) throws IdentityStoreException;

    /**
     * returns a list of identity from combination of attributes
     *
     * @param identitySearchRequest
     *            change to apply to identity
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return identity filled according to application rights for user identified by connection id
     */
    IdentitySearchResponse searchIdentities( final IdentitySearchRequest identitySearchRequest, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

    /**
     * returns the active service contract for the given application code
     * 
     * @deprecated Please use {@link IServiceContractTransportProvider} for requests regarding service contract.
     *
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return the active service contract for the given application code
     * @throws IdentityStoreException
     */
    @Deprecated
    ServiceContractSearchResponse getServiceContract( final String strClientCode, final RequestAuthor author ) throws IdentityStoreException;

    /**
     * import an identity to the id store
     *
     * @param identityChange
     *            change to apply to identity
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return identity filled according to application rights for user identified by connection id
     */
    IdentityChangeResponse importIdentity( final IdentityChangeRequest identityChange, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

    /**
     * Merge two identities.
     * 
     * @param identityMerge
     *            the request containing the master cuid, the secondary cuid, and a list of attribute to be taken from the secondary identity and put on the
     *            master identity.
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return IdentityMergeResponse
     */
    IdentityMergeResponse mergeIdentities( final IdentityMergeRequest identityMerge, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

    /**
     * Unmerge two identities.
     *
     * @param identityMerge
     *            the request containing the master cuid, the secondary cuid
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return IdentityMergeResponse
     */
    IdentityMergeResponse unMergeIdentities( final IdentityMergeRequest identityMerge, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

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
    IdentityHistoryGetResponse getIdentityHistory( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

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
    IdentityHistorySearchResponse searchIdentityHistory( final IdentityHistorySearchRequest request, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

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
    UpdatedIdentitySearchResponse getUpdatedIdentities( final UpdatedIdentitySearchRequest request, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

    /**
     * Dé-certification d'une identité.<br/>
     * Une identité ne pouvant pas posséder d'attributs non-certifiés, une dé-certification implique la certification de ses attributs avec le processus défini
     * par la property : <code>identitystore.identity.uncertify.processus</code> (par défaut : "dec", qui correspond au niveau le plus faible de certification
     * (auto-déclaratif))
     *
     * @param strCustomerId
     *            the customer ID
     * @param strClientCode
     *            client code of calling application
     * @param author
     *            the author of the request
     * @return IdentityChangeResponse
     */
    IdentityChangeResponse uncertifyIdentity( final String strCustomerId, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

    /**
     * Exports a list of identities according to the provided request and client code.
     *
     * @param request
     *            the export request
     * @param strClientCode
     *            the client code
     * @return IdentityExportResponse
     */
    IdentityExportResponse exportIdentities( final IdentityExportRequest request, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException;

    default void checkCommonHeaders( final String strClientCode, final RequestAuthor author ) throws IdentityStoreException
    {
        IdentityRequestValidator.instance( ).checkAuthor( author );
        IdentityRequestValidator.instance( ).checkClientCode( strClientCode );
    }
}
