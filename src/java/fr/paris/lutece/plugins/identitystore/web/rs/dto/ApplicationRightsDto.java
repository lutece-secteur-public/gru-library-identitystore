/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * ApplicationRightsDto
 */
@JsonRootName( value = DtoFormatConstants.KEY_APPLICATION_RIGHTS )
@JsonPropertyOrder( {
        DtoFormatConstants.KEY_APPLICATION_CODE, DtoFormatConstants.KEY_ATTRIBUTE_RIGHTS
} )
public class ApplicationRightsDto implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String _strApplicationCode;
    private List<AppRightDto> _listAppRights;

    /**
     * @return the strApplicationCode
     */
    @JsonProperty( DtoFormatConstants.KEY_APPLICATION_CODE )
    public String getApplicationCode( )
    {
        return _strApplicationCode;
    }

    /**
     * @param strApplicationCode
     *            the strApplicationCode to set
     */
    @JsonProperty( DtoFormatConstants.KEY_APPLICATION_CODE )
    public void setApplicationCode( String strApplicationCode )
    {
        this._strApplicationCode = strApplicationCode;
    }

    /**
     * @return the listAppRights
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_RIGHTS )
    public List<AppRightDto> getAppRights( )
    {
        return _listAppRights;
    }

    /**
     * @param appRight
     *            the AppRightDto to add
     */
    public void addAppRight( AppRightDto appRight )
    {
        if ( this._listAppRights == null )
        {
            this._listAppRights = new ArrayList<>( );
        }
        this._listAppRights.add( appRight );
    }

    /**
     * @param _listAppRights
     *            the _listAppRights to set
     */
    @JsonProperty( DtoFormatConstants.KEY_ATTRIBUTE_RIGHTS )
    public void setAppRights( List<AppRightDto> listAppRights )
    {
        this._listAppRights = listAppRights;
    }
}
