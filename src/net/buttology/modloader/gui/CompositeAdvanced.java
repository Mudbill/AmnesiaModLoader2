package net.buttology.modloader.gui;

import net.buttology.modloader.file.UserSettings;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class CompositeAdvanced extends Composite {
	
	private Button checkDisableShadows;
	private Button checkLimitFps;
	private Spinner spinnerMaxChannels;
	private Spinner spinnerStreamBuffers;
	private Spinner spinnerStreamBufferSize;
	private Button checkSkipPremenu;
	private Button checkDoNotSleep;
	private Button checkLoadFastPhysics;
	private Button checkLoadFastStatic;
	private Button checkLoadFastEntities;
	private Spinner spinnerGamma;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeAdvanced(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Group grpGraphics = new Group(this, SWT.NONE);
		grpGraphics.setText("Graphics");
		grpGraphics.setLayout(new FormLayout());
		FormData fd_grpGraphics = new FormData();
		fd_grpGraphics.bottom = new FormAttachment(0, 110);
		fd_grpGraphics.top = new FormAttachment(0, 10);
		fd_grpGraphics.left = new FormAttachment(0, 10);
		fd_grpGraphics.right = new FormAttachment(100, -10);
		grpGraphics.setLayoutData(fd_grpGraphics);
		
		Group grpOther = new Group(this, SWT.NONE);
		grpOther.setText("Other");
		grpOther.setLayout(new FormLayout());
		FormData fd_grpOther = new FormData();
		fd_grpOther.left = new FormAttachment(0, 10);
		fd_grpOther.right = new FormAttachment(100, -10);
		fd_grpOther.bottom = new FormAttachment(0, 378);
		grpOther.setLayoutData(fd_grpOther);
		
		checkSkipPremenu = new Button(grpOther, SWT.CHECK);
		FormData fd_checkSkipPremenu = new FormData();
		fd_checkSkipPremenu.top = new FormAttachment(0, 10);
		fd_checkSkipPremenu.left = new FormAttachment(0, 10);
		checkSkipPremenu.setLayoutData(fd_checkSkipPremenu);
		checkSkipPremenu.setText("Skip pre-menu");
		
		checkDoNotSleep = new Button(grpOther, SWT.CHECK);
		FormData fd_checkDoNotSleep = new FormData();
		fd_checkDoNotSleep.top = new FormAttachment(checkSkipPremenu, 6);
		fd_checkDoNotSleep.left = new FormAttachment(checkSkipPremenu, 0, SWT.LEFT);
		checkDoNotSleep.setLayoutData(fd_checkDoNotSleep);
		checkDoNotSleep.setText("Do not sleep when out of focus");
		
		checkLoadFastPhysics = new Button(grpOther, SWT.CHECK);
		FormData fd_checkLoadFastPhysics = new FormData();
		fd_checkLoadFastPhysics.top = new FormAttachment(checkDoNotSleep, 6);
		fd_checkLoadFastPhysics.left = new FormAttachment(checkSkipPremenu, 0, SWT.LEFT);
		checkLoadFastPhysics.setLayoutData(fd_checkLoadFastPhysics);
		checkLoadFastPhysics.setText("Load fast physics");
		
		checkLoadFastStatic = new Button(grpOther, SWT.CHECK);
		FormData fd_checkLoadFastStatic = new FormData();
		fd_checkLoadFastStatic.top = new FormAttachment(checkLoadFastPhysics, 6);
		fd_checkLoadFastStatic.left = new FormAttachment(checkSkipPremenu, 0, SWT.LEFT);
		checkLoadFastStatic.setLayoutData(fd_checkLoadFastStatic);
		checkLoadFastStatic.setText("Load fast static objects");
		
		checkLoadFastEntities = new Button(grpOther, SWT.CHECK);
		FormData fd_checkLoadFastEntities = new FormData();
		fd_checkLoadFastEntities.top = new FormAttachment(checkLoadFastStatic, 6);
		fd_checkLoadFastEntities.left = new FormAttachment(checkSkipPremenu, 0, SWT.LEFT);
		checkLoadFastEntities.setLayoutData(fd_checkLoadFastEntities);
		checkLoadFastEntities.setText("Load fast entities");
		
		checkDisableShadows = new Button(grpGraphics, SWT.CHECK);
		FormData fd_checkDisableShadows = new FormData();
		fd_checkDisableShadows.top = new FormAttachment(0, 10);
		fd_checkDisableShadows.left = new FormAttachment(0, 10);
		checkDisableShadows.setLayoutData(fd_checkDisableShadows);
		checkDisableShadows.setText("Disable shadows");
		
		checkLimitFps = new Button(grpGraphics, SWT.CHECK);
		FormData fd_checkLimitFps = new FormData();
		fd_checkLimitFps.top = new FormAttachment(checkDisableShadows, 6);
		fd_checkLimitFps.left = new FormAttachment(checkDisableShadows, 0, SWT.LEFT);
		checkLimitFps.setLayoutData(fd_checkLimitFps);
		checkLimitFps.setText("Limit FPS");
		
		Group grpAudio = new Group(this, SWT.NONE);
		fd_grpOther.top = new FormAttachment(grpAudio, 6);
		grpAudio.setText("Audio");
		grpAudio.setLayout(new FormLayout());
		FormData fd_grpAudio = new FormData();
		fd_grpAudio.top = new FormAttachment(grpGraphics, 6, SWT.BOTTOM);
		
		Label lblGamma = new Label(grpGraphics, SWT.NONE);
		FormData fd_lblGamma = new FormData();
		fd_lblGamma.top = new FormAttachment(checkLimitFps, 6);
		fd_lblGamma.left = new FormAttachment(checkDisableShadows, 16, SWT.LEFT);
		lblGamma.setLayoutData(fd_lblGamma);
		lblGamma.setText("Gamma");
		
		spinnerGamma = new Spinner(grpGraphics, SWT.BORDER);
		spinnerGamma.setDigits(1);
		spinnerGamma.setSelection(10);
		FormData fd_spinnerGamma = new FormData();
		fd_spinnerGamma.top = new FormAttachment(lblGamma, -3, SWT.TOP);
		fd_spinnerGamma.right = new FormAttachment(100, -10);
		fd_spinnerGamma.left = new FormAttachment(100, -110);
		spinnerGamma.setLayoutData(fd_spinnerGamma);
		fd_grpAudio.bottom = new FormAttachment(0, 230);
		fd_grpAudio.left = new FormAttachment(0, 10);
		fd_grpAudio.right = new FormAttachment(100, -10);
		grpAudio.setLayoutData(fd_grpAudio);
		
		Label lblMaxChannels = new Label(grpAudio, SWT.NONE);
		FormData fd_lblMaxChannels = new FormData();
		fd_lblMaxChannels.left = new FormAttachment(0, 12);
		lblMaxChannels.setLayoutData(fd_lblMaxChannels);
		lblMaxChannels.setText("Max channels");
		
		spinnerMaxChannels = new Spinner(grpAudio, SWT.BORDER);
		spinnerMaxChannels.setSelection(UserSettings.DEFAULT_MAX_CHANNELS);
		spinnerMaxChannels.setToolTipText("Default: " + UserSettings.DEFAULT_MAX_CHANNELS);
		fd_lblMaxChannels.top = new FormAttachment(spinnerMaxChannels, 3, SWT.TOP);
		FormData fd_spinnerMaxChannels = new FormData();
		fd_spinnerMaxChannels.top = new FormAttachment(0, 10);
		fd_spinnerMaxChannels.right = new FormAttachment(100, -10);
		fd_spinnerMaxChannels.left = new FormAttachment(100, -110);
		spinnerMaxChannels.setLayoutData(fd_spinnerMaxChannels);
		
		spinnerStreamBuffers = new Spinner(grpAudio, SWT.BORDER);
		spinnerStreamBuffers.setSelection(UserSettings.DEFAULT_STREAM_BUFFERS);
		spinnerStreamBuffers.setToolTipText("Default: " + UserSettings.DEFAULT_STREAM_BUFFERS);
		FormData fd_spinnerStreamBuffers = new FormData();
		fd_spinnerStreamBuffers.top = new FormAttachment(spinnerMaxChannels, 6);
		fd_spinnerStreamBuffers.right = new FormAttachment(spinnerMaxChannels, 0, SWT.RIGHT);
		fd_spinnerStreamBuffers.left = new FormAttachment(100, -110);
		spinnerStreamBuffers.setLayoutData(fd_spinnerStreamBuffers);
		
		spinnerStreamBufferSize = new Spinner(grpAudio, SWT.BORDER);
		spinnerStreamBufferSize.setMaximum(UserSettings.DEFAULT_STREAM_BUFFER_SIZE * 4);
		spinnerStreamBufferSize.setSelection(UserSettings.DEFAULT_STREAM_BUFFER_SIZE);
		spinnerStreamBufferSize.setToolTipText("Default: " + UserSettings.DEFAULT_STREAM_BUFFER_SIZE);
		FormData fd_spinnerStreamBufferSize = new FormData();
		fd_spinnerStreamBufferSize.top = new FormAttachment(spinnerStreamBuffers, 6);
		fd_spinnerStreamBufferSize.right = new FormAttachment(spinnerMaxChannels, 0, SWT.RIGHT);
		fd_spinnerStreamBufferSize.left = new FormAttachment(100, -110);
		spinnerStreamBufferSize.setLayoutData(fd_spinnerStreamBufferSize);
		
		Label lblStreamBuffers = new Label(grpAudio, SWT.NONE);
		FormData fd_lblStreamBuffers = new FormData();
		fd_lblStreamBuffers.top = new FormAttachment(spinnerStreamBuffers, 3, SWT.TOP);
		fd_lblStreamBuffers.left = new FormAttachment(lblMaxChannels, 0, SWT.LEFT);
		lblStreamBuffers.setLayoutData(fd_lblStreamBuffers);
		lblStreamBuffers.setText("Stream buffers");
		
		Label lblStreamBufferSize = new Label(grpAudio, SWT.NONE);
		FormData fd_lblStreamBufferSize = new FormData();
		fd_lblStreamBufferSize.top = new FormAttachment(spinnerStreamBufferSize, 3, SWT.TOP);
		fd_lblStreamBufferSize.left = new FormAttachment(lblMaxChannels, 0, SWT.LEFT);
		lblStreamBufferSize.setLayoutData(fd_lblStreamBufferSize);
		lblStreamBufferSize.setText("Stream buffer size");
		
		loadUserSettings();
	}
	
	private int toInt(String value) {
		String val = UserSettings.getVar(value);
		if(val.isEmpty()) return 0;
		return Integer.parseInt(val);
	}
	
	private float toFloat(String value) {
		String val = UserSettings.getVar(value);
		if(val.isEmpty()) return 0.0f;
		return Float.parseFloat(val);
	}
	
	private boolean toBool(String value) {
		String val = UserSettings.getVar(value);
		if(val.isEmpty()) return false;
		return Boolean.parseBoolean(val);
	}
	
	private void loadUserSettings() {
		checkDisableShadows.setSelection(toBool("DisableShadows"));
		checkLimitFps.setSelection(toBool("LimitFPS"));
		if(UserSettings.isSet("Gamma"))
			spinnerGamma.setSelection((int) (toFloat("Gamma") * 10.0f));
		if(UserSettings.isSet("MaxChannels"))
			spinnerMaxChannels.setSelection(toInt("MaxChannels"));
		if(UserSettings.isSet("StreamBuffers"))
			spinnerStreamBuffers.setSelection(toInt("StreamBuffers"));
		if(UserSettings.isSet("StreamBufferSize"))
			spinnerStreamBufferSize.setSelection(toInt("StreamBufferSize"));
		checkSkipPremenu.setSelection(toBool("SkipPreMenu"));
		checkDoNotSleep.setSelection(toBool("DoNotSleep"));
		checkLoadFastPhysics.setSelection(toBool("LoadFastPhysics"));
		checkLoadFastStatic.setSelection(toBool("LoadFastStaticObjects"));
		checkLoadFastEntities.setSelection(toBool("LoadFastEntities"));
	}
	
	public boolean getShadowsDisabled() {
		return checkDisableShadows.getSelection();
	}
	
	public boolean getLimitFps() {
		return checkLimitFps.getSelection();
	}
	
	public int getMaxChannels() {
		return spinnerMaxChannels.getSelection();
	}
	
	public int getStreamBuffers() {
		return spinnerStreamBuffers.getSelection();
	}
	
	public int getStreamBufferSize() {
		return spinnerStreamBufferSize.getSelection();
	}
	
	public boolean getSkipPremenu() {
		return checkSkipPremenu.getSelection();
	}
	
	public boolean getDoNotSleep() {
		return checkDoNotSleep.getSelection();
	}
	
	public boolean getLoadFastPhysics() {
		return checkLoadFastPhysics.getSelection();
	}
	
	public boolean getLoadFastStatic() {
		return checkLoadFastStatic.getSelection();
	}
	
	public boolean getLoadFastEntities() {
		return checkLoadFastEntities.getSelection();
	}
	
	public float getGamma() {
		return spinnerGamma.getSelection() / 10.0f;
	}
}
