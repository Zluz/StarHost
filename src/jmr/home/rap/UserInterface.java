package jmr.home.rap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UserInterface {

	
	protected Display display;
	protected Label lblTime;


	
	/*
	 * Need a better image scaler.
	 * Maybe:
	 *	 http://stackoverflow.com/questions/4752748/swt-how-to-do-high-quality-image-resize
	 */
	public static Image ImageScale(	final Image image, 
									final int width, 
									final int height ) {

	    ImageData data = image.getImageData();

	    // Some logic to keep the aspect ratio
	    float img_height = data.height;
	    float img_width = data.width;
	    float container_height = height;
	    float container_width = width;

	    float dest_height_f = container_height;
	    float factor = img_height / dest_height_f;

	    int dest_width = (int) Math.floor(img_width / factor );
	    int dest_height = (int) dest_height_f;

	    if(dest_width > container_width) {
	        dest_width = (int) container_width;
	        factor = img_width / dest_width;
	        dest_height = (int) Math.floor(img_height / factor);

	    }

	    // Image resize
	    data = data.scaledTo(dest_width, dest_height);
	    Image scaled = new Image(Display.getDefault(), data);
	    image.dispose();
	    return scaled;
	}
	
	
	public static GridData gd(	final int iWidth,
								final int iHeight ) {
		final GridData gd = new GridData();
		gd.widthHint = iWidth;
		gd.heightHint = iHeight;
		return gd;
	}
	
	public static GridData gdFill() {
        final GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
        return gd;
	}
	

	public static GridLayout gl() {
        final GridLayout gl = new GridLayout( 1, true );
        gl.horizontalSpacing = 0;
        gl.marginWidth = 0;
        gl.verticalSpacing = 0;
        gl.marginHeight = 0;
        return gl;
	}
	
	/*
	 * doors:
	 * 	garage, front, deck, basement_walkout, sunroom, basement_steps (attic?)
	 * 		STATE[ closed ] 
	 * garage (x3)
	 * 	STATE[ full-up, full-down ] ACTION[ activate ]
	 * 
	 */
	

	public void buildUI(	final Composite parent,
							final boolean bRAP ) {

		this.display = parent.getDisplay();
		parent.setLayout( gl() );
		parent.setLayoutData( gdFill() );

//		final CTabFolder folder = new CTabFolder( parent, SWT.RIGHT );
//		folder.setLayoutData( gdFill() );
//		
//		final CTabItem tab1 = new CTabItem( folder, SWT.NONE );
//		tab1.setText( "Main" );
//		final CTabItem tab2 = new CTabItem( folder, SWT.NONE );
//		tab2.setText( "Monitor" );
//		
//		final Composite compMain = new Composite( folder, SWT.BORDER );
//		tab1.setControl( compMain );
		
		final Composite compMain = parent;
//		final Composite compMain = new Composite( parent, SWT.NONE );
		compMain.setLayoutData( gdFill() );

		compMain.setLayout( gl() );

		buildUI_MainPage( compMain, bRAP );
	}

	
	
	public void buildUI_MainPage(	final Composite parent,
									final boolean bRAP ) {
		if ( null==parent ) return;
		if ( parent.isDisposed() ) return;
		
		
        final GridLayout glParent = gl();
        glParent.numColumns = 7;
		parent.setLayout( glParent );
		
		final GridLayout glSingle = gl();
		final GridLayout glButton = new GridLayout( 1, true );

//        final int iParentWidth = parent.getClientArea().width;
        final int iParentWidth = parent.getBounds().width;
//        final int iParentHeight = parent.getClientArea().height;
        
        final Color colorBack = display.getSystemColor( SWT.COLOR_BLACK );
        final Color colorBorder = display.getSystemColor( SWT.COLOR_DARK_GRAY );
        final Color colorButton = display.getSystemColor( SWT.COLOR_DARK_BLUE );
        final Color colorFore = display.getSystemColor( SWT.COLOR_GRAY );
        
        final FontData fd = parent.getFont().getFontData()[0];
        final int iOrigHeight = fd.getHeight();
        
        final int iHugeSize = bRAP ? 60 : 30;
        
        fd.setHeight( iOrigHeight + iHugeSize );
        final Font fontHuge = new Font( display, fd );
        
        fd.setHeight( iOrigHeight + 5 );
        final Font fontBig = new Font( display, fd );
        
		parent.setBackground( colorBorder );

        final Composite compLeft = new Composite( parent, SWT.NONE );
        final GridData gdLeft = gdFill();
        gdLeft.horizontalSpan = 3;
        compLeft.setLayout( glSingle );
        compLeft.setLayoutData( gdLeft );
        compLeft.setBackground( colorBack );
        
        
        final Composite compLeftTop = new Composite( compLeft, SWT.NONE );
        compLeftTop.setLayoutData( gdFill() );
        compLeftTop.setLayout( glSingle );
        compLeftTop.setBackground( colorBack );
        
//        SimpleDateFormat sdf = 
////        		new SimpleDateFormat ("E yyyy-MM-dd 'at' hh:mm:ss a zzz");
////        				would produce "Sun 2004.07.18 at 04:14:09 PM PDT"
//        		new SimpleDateFormat ("hh:mm a");
//		final String strTime = sdf.format( new Date() );
		final String strTime = "00:00 xx";

        lblTime = new Label( compLeftTop, SWT.PUSH );
//        lblTime.setText( "12:34 PM" );
        lblTime.setText( strTime );
        lblTime.setLayoutData( gdFill() );
        lblTime.setFont( fontHuge );
        lblTime.setAlignment( SWT.CENTER );
        lblTime.setBackground( colorBack );
        lblTime.setForeground( colorFore );
        
        final Composite compLeftMiddle = new Composite( compLeft, SWT.NONE );
        final GridData gdLeftMiddle = gdFill();
        gdLeftMiddle.grabExcessVerticalSpace = true;
        compLeftMiddle.setLayoutData( gdLeftMiddle );
        compLeftMiddle.setLayout( glSingle );
        compLeftMiddle.setBackground( colorBack );

        final GridLayout glButtons = gl();
        glButtons.makeColumnsEqualWidth = true;
        glButtons.numColumns = 5;
        final Composite compLeftBottom = new Composite( compLeft, SWT.NONE );
        compLeftBottom.setLayoutData( gdFill() );
        compLeftBottom.setLayout( glButtons );
        compLeftBottom.setBackground( colorBack );
        
        final Composite compLBL = new Composite( compLeftBottom, SWT.NONE );
        final GridData gdLBL = gdFill();
        gdLBL.horizontalSpan = 2;
		compLBL.setLayoutData( gdLBL );
        compLBL.setLayout( glButton );
        compLBL.setBackground( colorButton );
        final Button btnPlayPause = new Button( compLBL, SWT.PUSH );
        btnPlayPause.setText( "Play/Pause" );
        btnPlayPause.setLayoutData( gdFill() );
        btnPlayPause.setFont( fontBig );

        final Composite compLBR = new Composite( compLeftBottom, SWT.NONE );
        final GridData gdLBR = gdFill();
        gdLBR.horizontalSpan = 3;
        compLBR.setLayoutData( gdLBR );
        compLBR.setLayout( glButton );
        compLBR.setBackground( colorButton );
        final Button btnNextTrack = new Button( compLBR, SWT.PUSH );
        btnNextTrack.setText( "Next Track" );
        btnNextTrack.setLayoutData( gdFill() );
        btnNextTrack.setFont( fontBig );


        final Composite compCenter = new Composite( parent, SWT.NONE );
        compCenter.setLayout( gl() );
        final GridData gdCenter = gdFill();
        gdCenter.grabExcessVerticalSpace = true;
        gdCenter.horizontalSpan = 2;
        compCenter.setLayoutData( gdCenter );
//        compCenter.setBackground( colorBack );
        
        final Composite compRight = new Composite( parent, SWT.NONE );
        compRight.setLayout( gl() );
        final GridData gdRight = gdFill();
        gdRight.grabExcessVerticalSpace = true;
        gdRight.horizontalSpan = 2;
        compRight.setLayoutData( gdRight );
        compRight.setBackground( colorBack );
        
        final String strImageFile = "C:\\Development\\workspaces\\20160807_Eclipse_4.6\\20160820 - Star\\files\\Spotify__Album.bmp";
        final Image imgAlbum = new Image( display, strImageFile );
//        final int iWidth = imgAlbum.getImageData().width;
//        final int iHeight = imgAlbum.getImageData().height;

        final int iCalcWidth = ( iParentWidth * 3 / 7 ) - 6;

//        final GridData gdImage = new GridData();
        final Canvas canvas = new Canvas( compLeftMiddle, SWT.NONE );
        canvas.setLayoutData( gd( iCalcWidth, iCalcWidth ) );

		gdLeft.widthHint = iCalcWidth;
        gdLeftMiddle.heightHint = iCalcWidth;
        gdLeftMiddle.widthHint = iCalcWidth;
        
//        gdImage.widthHint = iCalcWidth;
//        gdImage.heightHint = iCalcWidth;
        
//        canvas.setBackgroundImage( imgAlbum );
//        final int iCalcWidth = canvas.getSize().x;
//		gdImage.widthHint = iCalcWidth;
//        final Image imgCanvas = new Image( display, iCalcWidth, iParentHeight - 20 );

        final Image imgScaled = ImageScale( imgAlbum, iCalcWidth, iCalcWidth );
        canvas.setBackgroundImage( imgScaled );
        canvas.setLayoutData( gd( iCalcWidth, iCalcWidth ) );



        Button checkbox = new Button(compCenter, SWT.CHECK);
        checkbox.setText("Hello");
        final Button btn1 = new Button(compCenter, SWT.PUSH);
        btn1.setText( "Button 01" );
        final Button btn2 = new Button(compCenter, SWT.PUSH);
        btn2.setText( "Button 02" );
        final Button btn3 = new Button(compCenter, SWT.PUSH);
        btn3.setText( "Button 03" );
        
        final Label lbl = new Label( compCenter, SWT.NONE );
        lbl.setText( "time: xxxxx" );
        
        
//        final String[] strText = { "0" };
        
        if ( bRAP ) {
	        
//	        ProgressBarTab pbar = new ProgressBarTab();
//	        pbar.createExampleControls( compCenter );
//	        pbar.createStateControl();
//	        
//	        UpdatingControls uc = new UpdatingControls();
//	        uc.createExampleControls( compCenter );
//	//        uc.createStateControl();
        }
        
        
        final StringBuffer strbuf = new StringBuffer();
        
        final String CR = Text.DELIMITER;
        
        strbuf.append( "Display: " + display.toString() + CR );
        strbuf.append( "\twidth: " + display.getBounds().width + CR );
        strbuf.append( "\theight: " + display.getBounds().height + CR );
        strbuf.append( "\tcalc w: " + iCalcWidth + CR );
        
        try {
	        
//        	if ( bRAP ) {
//		        final HttpServletRequest request = RWT.getRequest();
//	
//		        strbuf.append( "RWT.getRequest: " + request + CR );
//		        strbuf.append( "\tgetRemoteAttr(): " + request.getRemoteAddr() + CR );
//		        strbuf.append( "\tgetRemoteHost(): " + request.getRemoteHost() + CR );
//		        strbuf.append( "\tgetRequestURI(): " + request.getRequestURI() + CR );
//		        
//		        final UISession session = RWT.getUISession();
//		
//		        strbuf.append( "RWT.getUISession: " + session + CR );
//		        strbuf.append( "\tgetConnection: " + session.getConnection() + CR );
//		        strbuf.append( "\tgetId(): " + session.getId() + CR );
//        	}
	        
        } catch ( final Throwable t ) {
	        strbuf.append( CR + "ERROR: " + t.toString() + CR );
	        strbuf.append( "\tgetMessage: " + t.getMessage() + CR );
        }
        
        final Text txtInfo = new Text( compCenter, SWT.MULTI | SWT.BORDER );
        txtInfo.setText( strbuf.toString() );
		final GridData gdFill = new GridData( SWT.FILL, SWT.FILL, true, true );
//		gdFill.grabExcessHorizontalSpace = true;
//		gdFill.grabExcessVerticalSpace = true;
		txtInfo.setLayoutData( gdFill );
	}
	
	
	public static void main( final String[] args ) {

//		final Display display = Display.getDefault();
//		final Display display = Display.getCurrent();
		final Display display = new Display();
		
		final Shell shell = new Shell( display );
//		HomeEntryPoint.buildUI( shell, false );
		
		shell.open();
		while ( !shell.isDisposed() ) {
	    	if ( !display.readAndDispatch() ) {
	    		display.sleep();
	    	}
		}
		System.exit( 0 );
	}
	
}
