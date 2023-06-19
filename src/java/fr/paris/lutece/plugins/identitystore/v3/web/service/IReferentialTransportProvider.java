package fr.paris.lutece.plugins.identitystore.v3.web.service;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.referentiel.LevelSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.referentiel.ProcessusSearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

public interface IReferentialTransportProvider {

	/**
	 * get process list
	 * 
	 * @return the list
	 * @throws IdentityStoreException
	 */
	ProcessusSearchResponse getProcessList() throws IdentityStoreException;

	/**
	 * get level list
	 * 
	 * @return the list
	 * @throws IdentityStoreException
	 */
	LevelSearchResponse getLevelList() throws IdentityStoreException;

}
