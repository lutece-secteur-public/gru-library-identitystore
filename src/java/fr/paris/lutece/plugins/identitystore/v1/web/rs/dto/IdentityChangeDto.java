/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.plugins.identitystore.v1.web.rs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * IdentityChangeDto
 *
 */
@JsonRootName( value = DtoFormatConstants.KEY_IDENTITY_CHANGE )
@JsonPropertyOrder( {
        DtoFormatConstants.KEY_IDENTITY_CHANGE_IDENTITY, DtoFormatConstants.KEY_IDENTITY_CHANGE_AUTHOR
} )
public class IdentityChangeDto
{
    private IdentityDto _identity;
    private AuthorDto _author;

    /**
     * @return the _identity
     */
    @JsonProperty( value = DtoFormatConstants.KEY_IDENTITY_CHANGE_IDENTITY )
    public IdentityDto getIdentity( )
    {
        return _identity;
    }

    /**
     * @param identity
     *            the _identity to set
     */
    @JsonProperty( value = DtoFormatConstants.KEY_IDENTITY_CHANGE_IDENTITY )
    public void setIdentity( IdentityDto identity )
    {
        this._identity = identity;
    }

    /**
     * @return the _author
     */
    @JsonProperty( value = DtoFormatConstants.KEY_IDENTITY_CHANGE_AUTHOR )
    public AuthorDto getAuthor( )
    {
        return _author;
    }

    /**
     * @param author
     *            the _author to set
     */
    @JsonProperty( value = DtoFormatConstants.KEY_IDENTITY_CHANGE_AUTHOR )
    public void setAuthor( AuthorDto author )
    {
        this._author = author;
    }
}
