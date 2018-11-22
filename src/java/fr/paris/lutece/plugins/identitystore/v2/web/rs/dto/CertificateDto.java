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
package fr.paris.lutece.plugins.identitystore.v2.web.rs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Date;

/**
 * CertifierDto
 */
@JsonRootName( DtoFormatConstants.KEY_CERTIFICATE )
@JsonPropertyOrder( {
        DtoFormatConstants.KEY_CERTIFICATE_CERTIFIER_CODE, DtoFormatConstants.KEY_CERTIFICATE_CERTIFIER_NAME, DtoFormatConstants.KEY_CERTIFICATE_LEVEL,
        DtoFormatConstants.KEY_CERTIFICATE_EXPIRATION_DATE
} )
public class CertificateDto
{
    private String _strCertifierCode;
    private String _strCertifierName;
    private int _nCertifierLevel;
    private Date _dateCertificateExpirationDate;

    /**
     * @return the _strCertifierCode
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_CERTIFIER_CODE )
    public String getCertifierCode( )
    {
        return _strCertifierCode;
    }

    /**
     * @param strCertifierCode
     *            the _strCertifierCode to set
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_CERTIFIER_CODE )
    public void setCertifierCode( String strCertifierCode )
    {
        this._strCertifierCode = strCertifierCode;
    }

    /**
     * @return the _strCertifierName
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_CERTIFIER_NAME )
    public String getCertifierName( )
    {
        return _strCertifierName;
    }

    /**
     * @param strCertifierName
     *            the _strCertifierName to set
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_CERTIFIER_NAME )
    public void setCertifierName( String strCertifierName )
    {
        this._strCertifierName = strCertifierName;
    }

    /**
     * @return the _nCertifierLevel
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_LEVEL )
    public int getCertifierLevel( )
    {
        return _nCertifierLevel;
    }

    /**
     * @param nCertifierLevel
     *            the _nCertifierLevel to set
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_LEVEL )
    public void setCertifierLevel( int nCertifierLevel )
    {
        this._nCertifierLevel = nCertifierLevel;
    }

    /**
     * @return the _dateCertificateExpirationDate
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_EXPIRATION_DATE )
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z" )
    public Date getCertificateExpirationDate( )
    {
        return _dateCertificateExpirationDate;
    }

    /**
     * @param dateCertificateExpirationDate
     *            the _dateCertificateExpirationDate to set
     */
    @JsonProperty( DtoFormatConstants.KEY_CERTIFICATE_EXPIRATION_DATE )
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z" )
    public void setCertificateExpirationDate( Date dateCertificateExpirationDate )
    {
        this._dateCertificateExpirationDate = dateCertificateExpirationDate;
    }
}
