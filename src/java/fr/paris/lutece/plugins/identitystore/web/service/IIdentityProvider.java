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
package fr.paris.lutece.plugins.identitystore.web.service;

import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityChangeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.portal.service.util.AppException;

import java.io.File;
import java.io.InputStream;

import java.util.List;


/**
 * IIdentityService
 */
public interface IIdentityProvider
{
    /**
     * get identity matching connectionId and customerId for provided application
     * code
     *
     * @param strConnectionId
     *          connection Id (can be null if strCustomerId is provided)
     * @param nCustomerId
     *          customer Id (can be null if strconnection Id is provided)
     * @param strApplicationCode
     *          application code of calling application
     * @param strAuthenticationKey client application hash code
     * @return identity if found
     * @throws IdentityNotFoundException
     *           if no identity found for input parameters
     * @throws AppException
     *           if inconsitent parmeters provided, or errors occurs...
     *
     */
    IdentityDto getIdentity( String strConnectionId, int nCustomerId, String strApplicationCode,
        String strAuthenticationKey ) throws IdentityNotFoundException, AppException;

    /**
     * apply changes to an identity
     *
     * @param identityChange
     *          change to apply to identity
     * @param strAuthenticationKey client application hash code
     * @param listFiles file list to upload
     * @return the updated identity
     *
     * @throws AppException
     *           if error occured while updating identity
     * @throws IdentityNotFoundException
     *           if no identity found for input parameters
     */
    IdentityDto updateIdentity( IdentityChangeDto identityChange, String strAuthenticationKey, List<File> listFiles )
        throws IdentityNotFoundException, AppException;

    /**
     * create identity
     *
     * @param identityChange
     *          change to apply to identity
     * @param strAuthenticationKey client application hash code
     * @return the created identity
     *
     * @throws AppException
     *           if error occured while updating identity
     */
    IdentityDto createIdentity( IdentityChangeDto identityChange, String strAuthenticationKey )
        throws AppException;

    /**
     *
     * @param strConnectionId
     *          connection Id (can be null if strCustomerId is provided)
     * @param nCustomerId
     *          customer Id (can be null if strconnection Id is provided)
     * @param strAttributeKey attribute Key (must match a an attribute of type file)
     * @param strClientAppCode
     *          application code of calling application
     * @param strAuthenticationKey client application hash code
     * @return inputstream of attribute file
     * @throws AppException
     *           if error occured while retrieving file attribute
     * @throws IdentityNotFoundException
     *           if no identity found for input parameters
     */
    InputStream downloadFileAttribute( String strConnectionId, int nCustomerId, String strAttributeKey,
        String strClientAppCode, String strAuthenticationKey );
}
