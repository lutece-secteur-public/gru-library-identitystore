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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Date;

/**
 * Attribute Status Dto
 *
 */
@JsonRootName( value = DtoFormatConstants.KEY_ATTRIBUTE_STATUS )
@JsonPropertyOrder( {
        DtoFormatConstants.KEY_ATTRIBUTE_STATUS_CODE, DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_VALUE, DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_CERTIFIER, DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_CERTIFICATE_EXP_DATE
} )
public class AttributeStatusDto implements Serializable
{
	public static final String OK_CODE = "200";
	public static final String INFO_DEFAULT_CODE = "300";
	public static final String INFO_NO_CHANGE_REQUEST_CODE = "301";
	public static final String INFO_DELETE_NOT_ALLOW_CODE = "302";
	public static final String INFO_VALUE_CERTIFIED_CODE = "303";
	public static final String INFO_LONGER_CERTIFIER_CODE = "304";
	public static final String ERROR_DEFAULT_CODE = "400";
	
    /**
    *
    */
    private static final long serialVersionUID = 1L;
    private String _strStatusCode;
    private String _strNewValue;
    private String _strNewCertifier;
    private Date _dateNewCertificateExpirationDate;

    /**
     * @return the _strStatusCode
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_CODE )
    public String getStatusCode( )
    {
        return _strStatusCode;
    }

    /**
     * @param strStatusCode
     *            the strStatusCode to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_CODE )
    public void setStatusCode( String strStatusCode )
    {
        this._strStatusCode = strStatusCode;
    }

    /**
     * @return the _strNewValue
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_VALUE )
    @JsonInclude( Include.NON_NULL )
    public String getNewValue( )
    {
        return _strNewValue;
    }

    /**
     * @param strNewValue
     *            the strNewValue to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_VALUE )
    public void setNewValue( String strNewValue )
    {
        this._strNewValue = strNewValue;
    }

    /**
     * @return the _strNewCertifier
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_CERTIFIER )
    @JsonInclude( Include.NON_NULL )
    public String getNewCertifier( )
    {
        return _strNewCertifier;
    }

    /**
     * @param strNewCertifier
     *            the _strNewCertifier to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_CERTIFIER )
    public void setNewCertifier( String strNewCertifier )
    {
        this._strNewCertifier = strNewCertifier;
    }

    /**
     * @return the _dateCertificateExpirationDate
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_CERTIFICATE_EXP_DATE )
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z" )
    public Date getNewCertificateExpirationDate( )
    {
        return _dateNewCertificateExpirationDate;
    }

    /**
     * @param dateCertificateExpirationDate
     *            the _dateCertificateExpirationDate to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_STATUS_NEW_CERTIFICATE_EXP_DATE )
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z" )
    public void setNewCertificateExpirationDate( Date dateNewCertificateExpirationDate )
    {
        this._dateNewCertificateExpirationDate = dateNewCertificateExpirationDate;
    }
}
