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
package fr.paris.lutece.plugins.identitystore.v2.web.rs.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Attribute Dto
 *
 */
@JsonRootName( value = DtoFormatConstants.KEY_ATTRIBUTE_RIGHTS )
@JsonPropertyOrder( {
        DtoFormatConstants.KEY_ATTRIBUTE_KEY, DtoFormatConstants.KEY_ATTRIBUTE_READABLE, DtoFormatConstants.KEY_ATTRIBUTE_WRITABLE,
        DtoFormatConstants.KEY_ATTRIBUTE_CERTIFIERS
} )
public class AppRightDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String _strAttributeKey;
    private boolean _bReadable;
    private boolean _bWritable;
    private List<String> _listCertifiers;

    /**
     * @return the strAttributeKey
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_KEY )
    public String getAttributeKey( )
    {
        return _strAttributeKey;
    }

    /**
     * @param strAttributeKey
     *            the strAttributeKey to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_KEY )
    public void setAttributeKey( String strAttributeKey )
    {
        this._strAttributeKey = strAttributeKey;
    }

    /**
     * @return the bReadable
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_READABLE )
    public boolean isReadable( )
    {
        return _bReadable;
    }

    /**
     * @param bReadable
     *            the bReadable to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_READABLE )
    public void setReadable( boolean bReadable )
    {
        this._bReadable = bReadable;
    }

    /**
     * @return the bWritable
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_WRITABLE )
    public boolean isWritable( )
    {
        return _bWritable;
    }

    /**
     * @param bWritable
     *            the bWritable to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_WRITABLE )
    public void setWritable( boolean bWritable )
    {
        this._bWritable = bWritable;
    }

    /**
     * @return the listCertifier
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_CERTIFIERS )
    public List<String> getCertifiers( )
    {
        return _listCertifiers;
    }

    /**
     * @param strCertifierCode
     *            the certifierCode to add
     */
    public void addCertifier( String strCertifierCode )
    {
        if ( this._listCertifiers == null )
        {
            this._listCertifiers = new ArrayList<>( );
        }
        this._listCertifiers.add( strCertifierCode );
    }

    /**
     * @param listCertifier
     *            the listCertifier to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_CERTIFIERS )
    public void setCertifiers( List<String> listCertifiers )
    {
        this._listCertifiers = listCertifiers;
    }
}
