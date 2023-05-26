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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractsSearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

public interface IServiceContractTransportProvider
{

    /**
     * Get all service contract associated to the given client code.
     * 
     * @param strClientCode
     *            the client code.
     * @return ServiceContractsSearchResponse
     */
    ServiceContractsSearchResponse getServiceContractList( final String strClientCode ) throws IdentityStoreException;

    /**
     * Get the active service contract associated to the given client code.
     * 
     * @param strClientCode
     *            the client code.
     * @return ServiceContractSearchResponse
     */
    ServiceContractSearchResponse getActiveServiceContract( final String strClientCode ) throws IdentityStoreException;

    /**
     * Get the service contract associated to the given ID and application client code.
     * 
     * @param strClientCode
     *            the client code.
     * @param nServiceContractId
     *            the ID.
     * @return ServiceContractSearchResponse
     */
    ServiceContractSearchResponse getServiceContract( final String strClientCode, final Integer nServiceContractId ) throws IdentityStoreException;

    /**
     * Create a new Service Contract assoated with the given client code.<br/>
     * The service contract is created from the provided {@link ServiceContractDto}.
     * 
     * @param serviceContract
     *            the service contract to create.
     * @param strClientCode
     *            the client code.
     * @return ServiceContractChangeResponse
     */
    ServiceContractChangeResponse createServiceContract( final ServiceContractDto serviceContract, final String strClientCode ) throws IdentityStoreException;

    /**
     * Updates a service contract.<br/>
     * The service contract is updated from the provided {@link ServiceContractDto}.<br/>
     * 
     * @param serviceContract
     *            the service contract to update
     * @param nServiceContractId
     *            the service contract ID
     * @param strCLientCode
     *            the client code
     * @return ServiceContractChangeResponse
     */
    ServiceContractChangeResponse updateServiceContract( final ServiceContractDto serviceContract, final Integer nServiceContractId,
            final String strCLientCode ) throws IdentityStoreException;

    /**
     * Closes a service contract by specifying an end date.<br/>
     * The service contract is updated from the provided {@link ServiceContractDto}.<br/>
     * 
     * @param serviceContract
     *            the service contract to close
     * @param nServiceContractId
     *            the service contract ID
     * @param strCLientCode
     *            the client code
     * @return ServiceContractChangeResponse
     */
    ServiceContractChangeResponse closeServiceContract( final ServiceContractDto serviceContract, final Integer nServiceContractId, final String strCLientCode )
            throws IdentityStoreException;
}
