package fr.paris.lutece.plugins.identitystore.web.exception;

import fr.paris.lutece.portal.service.util.AppException;

public class IdentityDeletedException extends AppException {
	
	 private static final long serialVersionUID = 1L;

	    /**
	     * Constructor 1
	     *
	     * @param strMessage
	     *            The error message
	     */
	    public IdentityDeletedException( String strMessage )
	    {
	        super( strMessage );
	    }

	    /**
	     * Constructor 2
	     *
	     * @param strMessage
	     *            The error message
	     * @param e
	     *            The initial exception
	     */
	    public IdentityDeletedException( String strMessage, Exception e )
	    {
	        super( strMessage, e );
	    }

	    /**
	     * Constructor 3
	     */
	    public IdentityDeletedException( )
	    {
	        super( );
	    }

}
