package jmr.home.rap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

//import jmr.home.model.Atom;
//import jmr.home.model.IAtomConsumer;

public class UserInterfaceRemote 
					extends UserInterface 
//					implements IAtomConsumer 
					{

	static {
		System.out.println( UserInterfaceRemote.class.toString() + " loaded." );
	}
	

	//	private static final String DATE_PATTERN = "h:mm a";
	private static final String DATE_PATTERN = "h:mm:ss a";

	private final ServerPushSession spsClock;
	private final ServerPushSession spsControls;
	
	private final static StringBuffer strbufLog = new StringBuffer();
	
	private final static List<UserInterfaceRemote> 
									listInstances = new LinkedList<>();


	/*
	 * PERIOD must be more than MAX_LATENCY
	 */
	
//	private static final long PERIOD = 1000L * 60;
	private static final long PERIOD = 1000L * 10;
	
//	private static final long MAX_LATENCY = 1000L * 2;
	private static final long MAX_ALLOWABLE_DELAY = 1000L;

	private long lClockLastUpdate = 0;
	private long lControlsLastUpdate = 0;
	
	
	private final static SimpleDateFormat 
			DATE_FORMAT = new SimpleDateFormat( DATE_PATTERN );

//	private final HttpServletRequest request;
//	private final UISession session;


	public UserInterfaceRemote() {
	    spsClock = new ServerPushSession();
	    spsControls = new ServerPushSession();
	    listInstances.add( this );
	    
	    UserInterfaceRemote.log( "New session: " + this );
	    

//        request = RWT.getRequest();
//        session = RWT.getUISession();
//
//        UserInterfaceRemote.log( "RWT RemoteHost: " + request.getRemoteHost() );
//        UserInterfaceRemote.log( "RWT URI: " + request.getRequestURI() );
//        UserInterfaceRemote.log( "RWT UI ID: " + session.getId() );
	}
	
	
	public static void refreshInstances() {
		final Set<UserInterfaceRemote> setToDelete = new HashSet<>();
		for ( final UserInterfaceRemote uir : listInstances ) {
			if ( null!=uir.display && uir.display.isDisposed() ) {
				setToDelete.add( uir );
			}
		}
		for ( final UserInterfaceRemote uir : setToDelete ) {
			setToDelete.remove( uir );
		}
	}
	
	
	public static boolean processMessage( final String strMessage ) {
		
		System.out.println( "--- jmr.home.rap.UserInterfaceRemote.processMessage(String)" );
		System.out.println( "\t" + strMessage );
		
		refreshInstances();

		for ( final UserInterfaceRemote uir : listInstances ) {
			uir.strPendingMessage = strMessage;
		}
		
		return true;
	}
	
	
	public static void log( final String strMessage ) {
		strbufLog.append( "\n" );
		strbufLog.append( strMessage );
		
		System.out.println( "log> " + strMessage );
		
		String strLog = strbufLog.toString();
		int iPos = strLog.length() - 2048;
		if ( iPos>0 ) {
			iPos = strLog.lastIndexOf( "\n", iPos );
			if ( iPos>0 ) {
				strLog = strLog.substring( iPos );
			}
		}
		strLog.replaceAll( "\\n", Text.DELIMITER );
		
		refreshInstances();
		
		final String strText = strLog;
		for ( final UserInterfaceRemote uir : listInstances ) {
			if ( null!=uir.display ) {
				uir.display.syncExec( new Runnable() {
					@Override
					public void run() {
						System.out.println( "[send] set log text" );
						uir.txtInfo.setText( strText );
					}
				});
			}
		}
	}
	
	
	
//	public void setTime( final String strTime ) {
//		if ( null==display ) return;
//		if ( display.isDisposed() ) return;
//		if ( lblTime.isDisposed() ) return;
//		if ( null==strTime ) return;
//		
//		display.asyncExec( new Runnable() {
//			public void run() {
//				lblTime.setText( strTime );
//			};
//		});
//	}
	
	
	public void buildUI( final Composite parent ) {
		super.buildUI( parent, true );
		
		spsClock.start();
		startTimerTaskTimeThread();
		
		spsControls.start();
		startControlUpdateThread();
		
		this.lblText.setText( "Class: " + UserInterfaceRemote.class.getName() );
	}

	
	
	String strPendingMessage = null;
	
	
//	@Override
//	public void consume( final Atom atom ) {
//		final String strInfo = atom.toString();
//		strPendingMessage = strInfo;
//	}
	

	
	private void startControlUpdateThread() {
		
		final TimerTask task = new TimerTask() {
			@Override
			public void run() {

				// create random message
				if ( 1000 * Math.random() < 10 ) {
					final long lTime = System.currentTimeMillis();
					strPendingMessage = "Timer: " + lTime;
				}
				
				
				final long[] lNow = { System.currentTimeMillis() };
				final long lElapsed = lNow[0] - lControlsLastUpdate;
				if ( lElapsed >= MAX_ALLOWABLE_DELAY ) {
					
					if ( null!=strPendingMessage ) {
	
						display.syncExec( new Runnable() {
							@Override
							public void run() {
	//							System.out.println( "[send] time update" );
								
								lblText.setText( strPendingMessage );
								strPendingMessage = null;
								lNow[0] = System.currentTimeMillis();
							}
						});
						
						lControlsLastUpdate = lNow[0];
					}
				}
			}
		};
		
		final Timer timer = new Timer();
		
		timer.scheduleAtFixedRate( task, 2000, 10 );
	}
	
	
	private void startTimerTaskTimeThread() {
		
		final TimerTask task = new TimerTask() {
			@Override
			public void run() {
				thread_run();
			}
		};
		
		final Timer timer = new Timer();
		
		final double dNow = System.currentTimeMillis();
		long dateFirst = (long)( Math.floor( dNow / PERIOD ) + 1 ) * PERIOD;
		timer.scheduleAtFixedRate( task, new Date( dateFirst ), PERIOD );
	}
	
	
	private void thread_run() {

		final long lNowCheck = System.currentTimeMillis();
//		final long lElapsed = lNowCheck - lLastUpdate + MAX_LATENCY;
		final long lElapsed = lNowCheck - lClockLastUpdate;
		
//		if ( lElapsed >= PERIOD ) {
		if ( lElapsed >= MAX_ALLOWABLE_DELAY ) {
		
			final Long[] lNowUI = { null };
			
			display.syncExec( new Runnable() {
				@Override
				public void run() {
//					System.out.println( "[send] time update" );
					lNowUI[0] = System.currentTimeMillis();
					final Date date = new Date( lNowUI[0]);
					final String strTime = DATE_FORMAT.format( date );
					lblTime.setText( strTime );
				}
			});
			
			lClockLastUpdate = lNowUI[0];
		} else {
			// skip this update
			System.out.println( "(time update skipped)  "
					+ "PERIOD = " + PERIOD + ", lElapsed = " + lElapsed );
		}
	}


}
