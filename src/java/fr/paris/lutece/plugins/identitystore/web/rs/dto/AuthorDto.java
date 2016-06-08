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
package fr.paris.lutece.plugins.identitystore.web.rs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;


/**
 * AuthorDto
 *
 */
@JsonRootName( DtoFormatConstants.KEY_AUTHOR )
@JsonPropertyOrder( {DtoFormatConstants.KEY_AUTHOR_USERNAME,
    DtoFormatConstants.KEY_AUTHOR_EMAIL,
    DtoFormatConstants.KEY_AUTHOR_TYPE,
    DtoFormatConstants.KEY_AUTHOR_APPLICATION_NAME
} )
public class AuthorDto
{
    private String _strUserName;
    private String _strEmail;
    private int _nType;
    private String _strApplicationName;
    private String _strApplicationCode;

    /**
     * @return the _strUserName
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_USERNAME )
    public String getUserName(  )
    {
        return _strUserName;
    }

    /**
     * @param strUserName
     *          the _strUserName to set
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_USERNAME )
    public void setUserName( String strUserName )
    {
        this._strUserName = strUserName;
    }

    /**
     * @return the _strEmail
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_EMAIL )
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * @param strEmail
     *          the _strEmail to set
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_EMAIL )
    public void setEmail( String strEmail )
    {
        this._strEmail = strEmail;
    }

    /**
     * @return the _type
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_TYPE )
    public int getType(  )
    {
        return _nType;
    }

    /**
     * @param nType
     *          the _nType to set
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_TYPE )
    public void setType( int nType )
    {
        this._nType = nType;
    }

    /**
     * @return the _strApplicationName
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_APPLICATION_NAME )
    public String getApplicationName(  )
    {
        return _strApplicationName;
    }

    /**
     * @param strApplicationName
     *          the _strApplicationName to set
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_APPLICATION_NAME )
    public void setApplicationName( String strApplicationName )
    {
        this._strApplicationName = strApplicationName;
    }

    /**
     * @return the _strApplicationCode
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_APPLICATION_CODE )
    public String getApplicationCode(  )
    {
        return _strApplicationCode;
    }

    /**
     * @param strApplicationCode
     *          the _strApplicationName to set
     */
    @JsonProperty( DtoFormatConstants.KEY_AUTHOR_APPLICATION_CODE )
    public void setApplicationCode( String strApplicationCode )
    {
        this._strApplicationCode = strApplicationCode;
    }
}
