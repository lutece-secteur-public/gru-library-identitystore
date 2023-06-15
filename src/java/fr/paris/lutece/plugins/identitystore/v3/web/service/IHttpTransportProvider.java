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

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import org.apache.commons.fileupload.FileItem;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IHttpTransportProvider
{
    /**
     * make POST request on given url with params and headers
     * 
     * @param strUrl
     *            url to call
     * @param mapParams
     *            params to post
     * @param mapHeadersRequest
     *            headers of the request
     * @return response body as String
     * @throws IdentityStoreException
     */
    String doPost( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest ) throws IdentityStoreException;

    /**
     * make POST request on given url with params and headers of a JSON object to retrieve another JSON
     * 
     * @param strUrl
     *            url to call
     * @param mapParams
     *            params to post
     * @param mapHeadersRequest
     *            headers of the request
     * @param json
     *            JSON object to post
     * @param responseJsonClass
     *            the class
     * @param <T>
     *            of the response
     * @param mapper
     *            mapper for JSON serialize / deserialize
     * @return T
     */
    <T> T doPostJSON( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json, Class<T> responseJsonClass,
            ObjectMapper mapper ) throws IdentityStoreException;

    /**
     * make POST request on given url with params and headers of a JSON object to retrieve another JSON
     *
     * @param strUrl
     *            url to call
     * @param mapParams
     *            params to post
     * @param mapHeadersRequest
     *            headers of the request
     * @param json
     *            JSON object to post
     * @param responseJsonClass
     *            the class
     * @param <T>
     *            of the response
     * @param mapper
     *            mapper for JSON serialize / deserialize
     * @return T
     */
    <T> T doPutJSON( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json, Class<T> responseJsonClass,
            ObjectMapper mapper ) throws IdentityStoreException;

    /**
     * make POST request on given url with params and headers of a JSON object to retrieve another JSON
     *
     * @param strUrl
     *            url to call
     * @param mapParams
     *            params to post
     * @param mapHeadersRequest
     *            headers of the request
     * @param json
     *            JSON object to post
     * @param responseJsonClass
     *            the class
     * @param <T>
     *            of the response
     * @param mapper
     *            mapper for JSON serialize / deserialize
     * @return response list
     */
    <T> List<T> doPostJSONforList( String strUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Object json, Class<T> responseJsonClass,
            ObjectMapper mapper ) throws IdentityStoreException;

    /**
     * make a Get request on given url with parameters
     * 
     * @param strEndPointUrl
     *            url
     * @param mapParams
     *            param to add to url
     * @param mapHeadersRequest
     *            request header
     * @param responseJsonClass
     *            the class
     * @param <T>
     *            of the response
     * @param mapper
     *            mapper for JSON serialize / deserialize
     * @return response list
     */
    <T> T doGet( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Class<T> responseJsonClass, ObjectMapper mapper )
            throws IdentityStoreException;;

    /**
     * make a Delete request on given url with parameters
     * 
     * @param strEndPointUrl
     *            url
     * @param mapParams
     *            param to add to url
     * @param mapHeadersRequest
     *            request header
     * @param responseJsonClass
     *            the class
     * @param <T>
     *            of the response
     * @param mapper
     *            mapper for JSON serialize / deserialize
     * @return response list
     * @throws IdentityStoreException
     */
    <T> T doDelete( String strEndPointUrl, Map<String, String> mapParams, Map<String, String> mapHeadersRequest, Class<T> responseJsonClass,
            ObjectMapper mapper ) throws IdentityStoreException;
    
	/**
	 * set end point
	 * 
	 * @param strEndPointUrl
	 */
	public void setApiEndPointUrl(String strApiEndPointUrl) ;
	
	/**
	 * get end point
	 * 
	 * @return strEndPointUrl
	 */
	public String getApiEndPointUrl( );
}
