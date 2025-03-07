package fr.paris.lutece.plugins.identitystore.v3.web.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.CryptoService;

/**
 * Message service class to get hashcodes of technical error message resource keys
 * 
 */
public class MessageService {

	public static final String HASH_ALGORITHM = "SHA-256";
	public static final int NB_CHARS = 8;
	private static final String PLUGIN_NAME = "identitystore";
	
	/**
	 * get a hashcode of the resource key in 8 chars
	 * 
	 * @param strMsgKey
	 * @return the hashcode
	 */
	public static String getHashcode( String strMsgKey )
	{
		if ( strMsgKey == null ) return null;
		
		String hash = CryptoService.encrypt(strMsgKey, HASH_ALGORITHM);
		
		return (hash != null && hash.length( ) >= NB_CHARS ? hash.substring( 0, NB_CHARS ) : null ) ;
	}
	
	/**
	 * get a map of hashcode and corresponding resource keys
	 * 
	 * @return the map
	 */
	public static Map<String,String> getHashCodeList( )
	{
		Map<String,String> mapKeys = new HashMap<>();
		
		Collections.list( I18nService.getPluginBundleKeys( PLUGIN_NAME ) ).stream( ).forEach( k -> mapKeys.put( getHashcode( k ), k ) );
		
		return mapKeys;
	}
}
