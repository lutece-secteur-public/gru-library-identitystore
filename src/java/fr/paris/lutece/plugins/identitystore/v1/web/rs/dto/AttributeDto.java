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
package fr.paris.lutece.plugins.identitystore.v1.web.rs.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import fr.paris.lutece.plugins.identitystore.v1.web.rs.dto.CertificateDto;
import java.io.Serializable;

/**
 * Attribute Dto
 *
 */
@JsonRootName( value = DtoFormatConstants.KEY_ATTRIBUTES )
@JsonPropertyOrder( {
        DtoFormatConstants.KEY_ATTRIBUTE_KEY, DtoFormatConstants.KEY_ATTRIBUTE_TYPE, DtoFormatConstants.KEY_ATTRIBUTE_VALUE,
        DtoFormatConstants.KEY_ATTRIBUTE_CERTIFIED, DtoFormatConstants.KEY_ATTRIBUTE_READABLE, DtoFormatConstants.KEY_ATTRIBUTE_WRITABLE,
        DtoFormatConstants.KEY_ATTRIBUTE_CERTIFIED, DtoFormatConstants.KEY_ATTRIBUTE_CERTIFICATE,
} )
public class AttributeDto implements Serializable
{
    /**
    *
    */
    private static final long serialVersionUID = 1L;
    private String _strKey;
    private String _strValue;
    private String _strType;
    private boolean _bCertified;
    private CertificateDto _certificate;

    /**
     * @return the _strName
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_KEY )
    public String getKey( )
    {
        return _strKey;
    }

    /**
     * @param strKey
     *            the strName to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_KEY )
    public void setKey( String strKey )
    {
        this._strKey = strKey;
    }

    /**
     * @return the _strValue
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_VALUE )
    public String getValue( )
    {
        return _strValue;
    }

    /**
     * @param strValue
     *            the strValue to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_VALUE )
    public void setValue( String strValue )
    {
        this._strValue = strValue;
    }

    /**
     * @return the _strType
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_TYPE )
    public String getType( )
    {
        return _strType;
    }

    /**
     * @param strType
     *            the _strType to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_TYPE )
    public void setType( String strType )
    {
        this._strType = strType;
    }

    /**
     * @return the _bCertified
     */
    @JsonIgnore
    public boolean getCertified( )
    {
        return _bCertified;
    }

    /**
     * @param bCertified
     *            the bCertified to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_CERTIFIED )
    public void setCertified( boolean bCertified )
    {
        this._bCertified = bCertified;
    }

    /**
     * @return the _bCertified
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_CERTIFIED )
    public boolean isCertified( )
    {
        return _bCertified;
    }

    /**
     * @return the _certificate
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_CERTIFICATE )
    public CertificateDto getCertificate( )
    {
        return _certificate;
    }

    /**
     * @param certificate
     *            the _certificate to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_CERTIFICATE )
    public void setCertificate( CertificateDto certificate )
    {
        this._certificate = certificate;
    }
}
