package jmr.home.comm.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import jmr.home.api.comm.rmi.ISimpleMessageConsumer;
import jmr.home.rap.UserInterfaceRemote;

public class RMIMessageConsumer 
			extends UnicastRemoteObject 
			implements ISimpleMessageConsumer {

	static {
		System.out.println( RMIMessageConsumer.class.toString() + " loaded." );
	}
//	
//	public static final String PATH_RMI_CLASSES = 
////			"C:\\Development\\workspaces\\20160807_Eclipse_4.6\\Star_API\\bin\\";
//			"C:\\Development\\workspaces\\20160807_Eclipse_4.6\\Star_API\\";
//	
	private static final long serialVersionUID = 1L;

	public static void initialize() {
//		System.setProperty( "java.rmi.server.codebase", PATH_RMI_CLASSES );
		if ( null==System.getSecurityManager() ) {
			System.setSecurityManager( new RMISecurityManager() );
		}
	}
	
	public RMIMessageConsumer() throws RemoteException {
		super();

		RMIMessageConsumer.initialize();
		
		ISimpleMessageConsumer.TEST.toString();
		
		try {
//			Naming.bind( ISimpleMessageConsumer.class.getName(), this );
			Naming.rebind( "ISimpleMessageConsumer", this );
			
		} catch ( final MalformedURLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Override
	public boolean sendMessage(	final Type type, 
								final String strText ) {
		switch ( type ) {
			case CONTROL: {
				break;
			}
			case MESSAGE: {
				UserInterfaceRemote.processMessage( strText );
				break;
			}
		}
		return true;
	}

	@Override
	public String sendSignal( final Signal signal ) {
		// TODO Auto-generated method stub
		return null;
	}

}
