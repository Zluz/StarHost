/*******************************************************************************
 * Copyright (c) 2007, 2015 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package jmr.home.app.uirap;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class UpdatingControls /*extends ExampleTab*/ {

  private ServerPushSession serverPush;
  private Text txt;

  public UpdatingControls() {
    serverPush = new ServerPushSession();
  }

  public void createExampleControls( final Composite parent ) {
    
    parent.setLayout( new GridLayout() );
    
    final Label lbl = new Label( parent, SWT.NONE );
    lbl.setText( "Time" );
    txt = new Text( parent, SWT.NONE );
    txt.setText( "000000000000000000" );
    

    final Button button = new Button( parent, SWT.PUSH );
    button.setText( "Start Background Process" );
    button.addSelectionListener( new SelectionAdapter() {
		private static final long serialVersionUID = 1L;
	@Override
      public void widgetSelected( final SelectionEvent evt ) {
        button.setEnabled( false );
        // activate server push mechanism
        serverPush.start();
        // create and start background thread that updates the progress bar
        Thread thread = new Thread( createRunnable( txt, button ) );
        thread.setDaemon( true );
        thread.start();
      }
    } );
    parent.layout();
  }

  private Runnable createRunnable( final Text txt, final Button button ) {
    final Display display = txt.getDisplay();
    Runnable result = new Runnable() {
      @Override
      public void run() {
        for(;;) {
        	
          try {
            // simulate some work
        	  final long lInterval = (long)(200 + 2000 * Math.random());
        	  Thread.sleep( lInterval );
          } catch( final Throwable shouldNotHappen ) {
            shouldNotHappen.printStackTrace();
          }
          
          if( !display.isDisposed() ) {
            // perform process bar update
            display.asyncExec( new Runnable() {

              @Override
              public void run() {
                if( !txt.isDisposed() ) {
                	
                	txt.setText( "" + System.currentTimeMillis() );
                }
              }
            } );
          }
        }
      }
    };
    return result;
  }
}
