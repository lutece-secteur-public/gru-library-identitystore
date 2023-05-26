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

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.contract.ServiceContractsSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.duplicate.DuplicateRuleSummarySearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

/**
 * Service regarding identity quality.
 */
public class IdentityQualityService
{

    /** transport provider */
    private IIdentityQualityTransportProvider _transportProvider;

    /**
     * Simple Constructor
     */
    public IdentityQualityService( )
    {
        super( );
    }

    /**
     * Constructor with IIdentityTransportProvider in parameters
     *
     * @param transportProvider
     *            IIdentityQualityTransportProvider
     */
    public IdentityQualityService( final IIdentityQualityTransportProvider transportProvider )
    {
        super( );
        this._transportProvider = transportProvider;
    }

    /**
     * setter of transportProvider parameter
     *
     * @param transportProvider
     *            IIdentityQualityTransportProvider
     */
    public void setTransportProvider( final IIdentityQualityTransportProvider transportProvider )
    {
        this._transportProvider = transportProvider;
    }

    /**
     * Get full list of duplicate rules.
     * 
     * @param strApplicationCode
     *            - the application code
     * @return DuplicateRuleSummarySearchResponse containing a list of <code>DuplicateRuleSummaryDto</code>.
     * @throws IdentityStoreException
     */
    public DuplicateRuleSummarySearchResponse getAllDuplicateRules( final String strApplicationCode ) throws IdentityStoreException
    {
        return this._transportProvider.getAllDuplicateRules( strApplicationCode );
    }

}
