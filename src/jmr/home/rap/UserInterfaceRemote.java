package jmr.home.rap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import jmr.home.comm.http.HttpSendEvent;
import jmr.home.comm.http.HttpSendEvent.Type;
import jmr.util.Util;

//import jmr.util.Util;

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
			if ( null!=uir.display && !uir.display.isDisposed() ) {
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
	
	
//	protected final static Map<String,String> mapLines = new HashMap<>();
	
//	private final static String KEY_NAME = "KEY_NAME";
//	private final static String KEY_VALUE= "KEY_VALUE";
	
	final static String[] arrLines = new String[MAX_LINES];
	final static String[] arrValues = new String[MAX_LINES];
	final static String[] arrNewValue = new String[MAX_LINES];
	
	
	public static void updateLines(	final String strMessage ) {
		
		int iUpdatedCount = 0;

//		final Map<String, String> map = Util.extractParameters( strMessage );
		final Map<String, String> map = extractParameters( strMessage );
		
		for ( final Entry<String, String> entry : map.entrySet() ) {
			
			final String strField = entry.getKey();
			String strValue = entry.getValue();
			
			
			if ( strField.contains( "Time" ) ) {
				try {
					long lValue = Long.parseLong( strValue );
					// 86400000 ms in a day
					if ( lValue < 90000000 ) {
						lValue = lValue + Util.getMillisecondsToLastMidnight();
					}
//					final Date dateValue = new Date( lValue );
					strValue = Util.getFormattedDatetime( lValue );
				} catch ( final NumberFormatException e ) {
					// just don't change
				}
			}
			

			Integer iLine = null;
			
			for ( int i=0; ( i<MAX_LINES && null==iLine ); i++ ) {
				if ( null==arrLines[i] ) {
					arrLines[i] = strField;
					arrNewValue[i] = strValue;
					iUpdatedCount++;
					iLine = i;
				} else if ( arrLines[i].equals( strField ) ) {
					if ( arrValues[i].equals( strValue ) ) {
						// value is equal, nothing to do.
						iLine = -1;
					} else {
						arrNewValue[i] = strValue;
						iUpdatedCount++;
						iLine = i;
					}
				}
			}
		}
		
		if ( iUpdatedCount>0 ) {
			for ( UserInterfaceRemote client : listInstances ) {
				client.strPendingMessage = 
	//					"Field updated: " + strField + " = " + strValue;
						"" + iUpdatedCount + " fields updated.";
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
		
//		for ( final Label label : super.arrLabels ) {
		for ( int i=0; i<MAX_LINES; i++ ) {
			final int iIndex = i;
			final Button label = super.arrLabels[i];
//			label.addTouchListener( new TouchListener() {
			label.addSelectionListener( new SelectionAdapter() {
				private static final long serialVersionUID = 1L;
				@Override
//				public void touch( final TouchEvent e ) {
				public void widgetSelected( final SelectionEvent e ) {
					
					label.setBackground( colorCyan );
					
					final String strText = label.getText();
					final String strMessage = "Label " + iIndex + " clicked  "
							+ "(\"" + strText + "\")";
					System.out.println( strMessage );
					
					new Thread() {
						@Override
						public void run() {
							try {
								HttpSendEvent.get().send( Type.CLICK, ""+iIndex, strText );
								Thread.sleep( 100 );
								strPendingMessage = strText;
							} catch ( final InterruptedException e ) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}.start();
				}
			});
		}
		
		super.arrLabels[10].setText( "Garage-Left:  [ Open ]" );
		super.arrLabels[11].setText( "Garage-Middle:  [ Partial ]" );
		super.arrLabels[12].setText( "Garage-Right:  [ Closed ]" );

		super.btnPlayPause.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;
			@Override
			public void widgetSelected( final SelectionEvent e ) {
				System.out.println( "Play/Pause button clicked" );
			}
		});

		super.btnNextTrack.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;
			@Override
			public void widgetSelected( final SelectionEvent e ) {
				System.out.println( "Next-Track button clicked" );
			}
		});
		
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
	
						display.syncExec( runnable );
						
//						lControlsLastUpdate = lNow[0];
						lControlsLastUpdate = System.currentTimeMillis();
					}
				}
			}
		};
		
		final Timer timer = new Timer();
		
		timer.scheduleAtFixedRate( task, 2000, 10 );
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			
			lblText.setText( strPendingMessage );
			
			for ( int i=0; i<MAX_LINES; i++ ) {
				final Button lbl = arrLabels[i];
				if ( null!=arrNewValue[i] && null!=arrLines[i] && null!=lbl ) {
//					if ( !arrLines[i].equals( lbl.getText() ) ) {
//						lbl.setText( arrLines[i] );
//					}
					final String strText = " " + arrLines[i] + " = " + arrNewValue[i];
					lbl.setText( strText );

					arrValues[i] = arrNewValue[i];
					arrNewValue[i] = null;
				}
				lbl.setBackground( colorBtnFace );
			}
			
			strPendingMessage = null;
//			lNow[0] = System.currentTimeMillis();
		}
	};

	
	
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
		if ( ( lElapsed >= MAX_ALLOWABLE_DELAY ) 
						&& ( !display.isDisposed() ) ) {
		
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

	
	// should be API..util. 
	// java.lang.NoClassDefFoundError: jmr/util/Util

	final public static String UTF8 = StandardCharsets.UTF_8.name();
		
	public static Map<String,String> extractParameters( final String query ) {
	    final Map<String, String> map = new LinkedHashMap<String, String>();
	    if ( null!=query && !query.isEmpty() ) {
		    final String[] pairs = query.split( "&" );
		    for ( final String pair : pairs ) {
		        final int idx = pair.indexOf( "=" );
		        try {
					final String strKey = URLDecoder.decode( pair.substring(0, idx), UTF8 );
					final String strValue = URLDecoder.decode( pair.substring(idx + 1), UTF8 );
					map.put( strKey, strValue );
				} catch ( final UnsupportedEncodingException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
	    }
	    return map;
	}
	
	
	
	
	@Override
	protected void finalize() throws Throwable {
		spsClock.stop();
		spsControls.stop();
	}

}
