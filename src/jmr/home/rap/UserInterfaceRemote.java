package jmr.home.rap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Composite;

public class UserInterfaceRemote extends UserInterface {

//	private static final String DATE_PATTERN = "h:mm a";
	private static final String DATE_PATTERN = "h:mm:ss a";

	private final ServerPushSession sps;
	
//	private final static long PERIOD = 1000 * 60;
	private final static long PERIOD = 1000 * 10;

	private final static SimpleDateFormat 
			DATE_FORMAT = new SimpleDateFormat( DATE_PATTERN );


	public UserInterfaceRemote() {
	    sps = new ServerPushSession();
	}
	
	public void setTime( final String strTime ) {
		if ( null==display ) return;
		if ( display.isDisposed() ) return;
		if ( lblTime.isDisposed() ) return;
		if ( null==strTime ) return;
		
		display.asyncExec( new Runnable() {
			public void run() {
				lblTime.setText( strTime );
			};
		});
	}
	
	
	public void buildUI( final Composite parent ) {
		super.buildUI( parent, true );
		sps.start();
		
//		Thread threadUpdateTime = createUpdateTimeThread();
//		threadUpdateTime.start();
		startTimerTaskTimeThread();
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

		//TODO compare current time to last-fired. skip if within PERIOD.
		
		final String strTime = DATE_FORMAT.format( new Date() );
		
		display.syncExec( new Runnable() {
			@Override
			public void run() {
				lblTime.setText( strTime );
			}
		});
		//TODO record last-fired here.
	}
	
	private Thread createUpdateTimeThread() {
		final Thread thread = new Thread( new Runnable() {
			@Override
			public void run() {
				thread_run();
				try {
					
					while ( !display.isDisposed() ) {
						
						thread_run();
						
						Thread.sleep( PERIOD );
					}
					
				} catch ( final InterruptedException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.setDaemon( true );
		return thread;
	}
	
	
	
	
	
	
}
