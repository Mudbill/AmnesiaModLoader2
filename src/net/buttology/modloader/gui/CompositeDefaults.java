package net.buttology.modloader.gui;

import java.util.ArrayList;
import java.util.List;

import net.buttology.modloader.Modloader;
import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.util.ResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class CompositeDefaults extends Composite {
	
	private Button checkUseDefaults;
	private Combo comboAudioOutput;
	private Combo comboResolution;
	
	private Combo comboPreset;
	private Combo comboTextureQuality;
	private Combo comboShadowQuality;
	private Combo comboShadowRes;
	private Combo comboTextureFilter;
	private Combo comboAnistro;
	private Button checkSsao;
	private Combo comboSsaoRes;
	private Combo comboSsaoSamples;
	private Button checkEdgeSmoothing;
	private Button checkWorldReflection;
	private Button checkParallax;
	private Button checkFullscreen;
	
	private List<Point> resolutions = new ArrayList<Point>();

	private String[] getResolutionsAsStringList() {
		String[] s = new String[resolutions.size()];
		for(int i = 0; i < s.length; i++)
		{
			s[i] = resolutions.get(i).x + "x" + resolutions.get(i).y;
		}
		return s;
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeDefaults(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		Shell shell = getShell();

		resolutions.add(new Point(640, 480));
		resolutions.add(new Point(800, 600));
		resolutions.add(Modloader.getSystemNativeResolution());
		
		ModifyListener setCustomC = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				comboPreset.select(3);
			}
		};
		
		SelectionAdapter setCustomS = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboPreset.select(3);
			}
		};
		
		checkUseDefaults = new Button(this, SWT.CHECK);
		
		FormData fd_checkUseDefaults = new FormData();
		fd_checkUseDefaults.top = new FormAttachment(0, 10);
		fd_checkUseDefaults.left = new FormAttachment(0, 10);
		checkUseDefaults.setLayoutData(fd_checkUseDefaults);
		checkUseDefaults.setText("Use these game defaults for every mod");
		
		Group groupSettings = new Group(this, SWT.NONE);
		groupSettings.setText("Settings");
		groupSettings.setLayout(new FormLayout());
		FormData fd_groupSettings = new FormData();
		fd_groupSettings.top = new FormAttachment(checkUseDefaults, 6);
		fd_groupSettings.left = new FormAttachment(0, 10);
		fd_groupSettings.right = new FormAttachment(100, -10);
		fd_groupSettings.bottom = new FormAttachment(0, 185);
		groupSettings.setLayoutData(fd_groupSettings);
		
		Label labelAudioOutput = new Label(groupSettings, SWT.NONE);
		FormData fd_labelAudioOutput = new FormData();
		fd_labelAudioOutput.top = new FormAttachment(0, 10);
		fd_labelAudioOutput.left = new FormAttachment(0, 10);
		labelAudioOutput.setLayoutData(fd_labelAudioOutput);
		labelAudioOutput.setText("Audio output");
		
		comboAudioOutput = new Combo(groupSettings, SWT.READ_ONLY);
		String[] comboAudioOutputItems = Modloader.getSystemAudioOutputs();
		comboAudioOutput.setItems(comboAudioOutputItems);
		comboAudioOutput.setItem(0, "Default output - " + comboAudioOutput.getItem(0));
		comboAudioOutput.select(0);
		FormData fd_comboAudioOutput = new FormData();
		fd_comboAudioOutput.right = new FormAttachment(100, -10);
		fd_comboAudioOutput.top = new FormAttachment(labelAudioOutput, 6);
		fd_comboAudioOutput.left = new FormAttachment(0, 10);
		comboAudioOutput.setLayoutData(fd_comboAudioOutput);
		
		Link linkAudio = new Link(groupSettings, SWT.NONE);
		linkAudio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
				m.setText("Audio output information");
				m.setMessage("This setting may be inaccurate because of how Amnesia saves it. The first output will always be the system default."
						+ " This means that if you change the system default, this list will change slightly. If you've picked a non-default output"
						+ ", then the output will likely be a different one after changing the system default.");
				m.open();
			}
		});
		FormData fd_linkAudio = new FormData();
		fd_linkAudio.bottom = new FormAttachment(labelAudioOutput, 0, SWT.BOTTOM);
		fd_linkAudio.right = new FormAttachment(comboAudioOutput, 0, SWT.RIGHT);
		linkAudio.setLayoutData(fd_linkAudio);
		linkAudio.setText("<a>Info</a>");
		
		Label labelResolution = new Label(groupSettings, SWT.NONE);
		FormData fd_labelResolution = new FormData();
		fd_labelResolution.top = new FormAttachment(comboAudioOutput, 6);
		fd_labelResolution.left = new FormAttachment(0, 10);
		labelResolution.setLayoutData(fd_labelResolution);
		labelResolution.setText("Resolution");
		
		comboResolution = new Combo(groupSettings, SWT.READ_ONLY);
		comboResolution.setItems(getResolutionsAsStringList());
		comboResolution.setItem(comboResolution.getItemCount()-1, comboResolution.getItem(comboResolution.getItemCount()-1) + " (native)");
		comboResolution.select(comboResolution.getItemCount()-1);
		FormData fd_comboResolution = new FormData();
		fd_comboResolution.left = new FormAttachment(0, 10);
		fd_comboResolution.top = new FormAttachment(labelResolution, 6);
		comboResolution.setLayoutData(fd_comboResolution);
		
		checkFullscreen = new Button(groupSettings, SWT.CHECK);
		FormData fd_checkFullscreen = new FormData();
		fd_checkFullscreen.top = new FormAttachment(labelResolution, 35);
		fd_checkFullscreen.left = new FormAttachment(labelAudioOutput, 0, SWT.LEFT);
		checkFullscreen.setLayoutData(fd_checkFullscreen);
		checkFullscreen.setText("Fullscreen");
		
		Button buttonCustomRes = new Button(groupSettings, SWT.NONE);
		buttonCustomRes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogCustomResolution d = new DialogCustomResolution(shell, SWT.DIALOG_TRIM | SWT.SHEET);
				Object o = d.open();
				if(o != null) {
					Point res = (Point) o;
					UserSettings.setVar("CustomResX", res.x);
					UserSettings.setVar("CustomResY", res.y);
					String value = res.x + "x" + res.y + " (custom)";
					if(comboResolution.getItemCount() == 3) {
						comboResolution.add(value);
					} else {						
						comboResolution.setItem(resolutions.size(), value);
					}
					comboResolution.select(comboResolution.getItemCount()-1);
				}
			}
		});
		fd_comboResolution.right = new FormAttachment(buttonCustomRes, -6);
		buttonCustomRes.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_buttonCustomRes = new FormData();
		fd_buttonCustomRes.top = new FormAttachment(labelResolution, 5);
		fd_buttonCustomRes.right = new FormAttachment(comboAudioOutput, 0, SWT.RIGHT);
		buttonCustomRes.setLayoutData(fd_buttonCustomRes);
		buttonCustomRes.setText("Set custom resolution");
		
		Group groupGraphics = new Group(this, SWT.NONE);
		groupGraphics.setText("Graphics");
		groupGraphics.setLayout(new FormLayout());
		FormData fd_groupGraphics = new FormData();
		fd_groupGraphics.bottom = new FormAttachment(100, -10);
		fd_groupGraphics.right = new FormAttachment(100, -10);
		fd_groupGraphics.top = new FormAttachment(groupSettings, 6);
		fd_groupGraphics.left = new FormAttachment(checkUseDefaults, 0, SWT.LEFT);
		groupGraphics.setLayoutData(fd_groupGraphics);
		
		Label labelPreset = new Label(groupGraphics, SWT.NONE);
		FormData fd_labelPreset = new FormData();
		fd_labelPreset.left = new FormAttachment(0, 10);
		labelPreset.setLayoutData(fd_labelPreset);
		labelPreset.setText("Preset");
		
		comboPreset = new Combo(groupGraphics, SWT.READ_ONLY);
		comboPreset.setItems(new String[] {"Low", "Medium", "High", "Custom"});
		fd_labelPreset.top = new FormAttachment(comboPreset, 3, SWT.TOP);
		FormData fd_comboPreset = new FormData();
		fd_comboPreset.top = new FormAttachment(0, 10);
		fd_comboPreset.left = new FormAttachment(0, 200);
		fd_comboPreset.right = new FormAttachment(100, -10);
		comboPreset.setLayoutData(fd_comboPreset);
		
		Label bar = new Label(groupGraphics, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_bar = new FormData();
		fd_bar.top = new FormAttachment(comboPreset, 10);
		fd_bar.left = new FormAttachment(0);
		fd_bar.right = new FormAttachment(100);
		bar.setLayoutData(fd_bar);
		
		Label labelTextureQuality = new Label(groupGraphics, SWT.NONE);
		FormData fd_labelTextureQuality = new FormData();
		fd_labelTextureQuality.left = new FormAttachment(0, 10);
		labelTextureQuality.setLayoutData(fd_labelTextureQuality);
		labelTextureQuality.setText("Texture quality");
		
		comboTextureQuality = new Combo(groupGraphics, SWT.READ_ONLY);
		comboTextureQuality.addModifyListener(setCustomC);
		comboTextureQuality.setItems(new String[] {"Low", "Medium", "High"});
		fd_labelTextureQuality.top = new FormAttachment(comboTextureQuality, 3, SWT.TOP);
		FormData fd_comboTextureQuality = new FormData();
		fd_comboTextureQuality.top = new FormAttachment(bar, 10, SWT.BOTTOM);
		fd_comboTextureQuality.left = new FormAttachment(0, 200);
		fd_comboTextureQuality.right = new FormAttachment(100, -10);
		comboTextureQuality.setLayoutData(fd_comboTextureQuality);
		
		Label labelShadowQuality = new Label(groupGraphics, SWT.NONE);
		FormData fd_labelShadowQuality = new FormData();
		fd_labelShadowQuality.left = new FormAttachment(labelTextureQuality, 0, SWT.LEFT);
		labelShadowQuality.setLayoutData(fd_labelShadowQuality);
		labelShadowQuality.setText("Shadow quality");
		
		comboShadowQuality = new Combo(groupGraphics, SWT.READ_ONLY);
		comboShadowQuality.addModifyListener(setCustomC);
		comboShadowQuality.setItems(new String[] {"Low", "Medium", "High"});
		fd_labelShadowQuality.top = new FormAttachment(comboShadowQuality, 3, SWT.TOP);
		FormData fd_comboShadowQuality = new FormData();
		fd_comboShadowQuality.right = new FormAttachment(comboTextureQuality, 0, SWT.RIGHT);
		fd_comboShadowQuality.left = new FormAttachment(0, 200);
		fd_comboShadowQuality.top = new FormAttachment(comboTextureQuality, 6);
		comboShadowQuality.setLayoutData(fd_comboShadowQuality);
		
		Label labelShadowResolution = new Label(groupGraphics, SWT.NONE);
		FormData fd_labelShadowResolution = new FormData();
		fd_labelShadowResolution.left = new FormAttachment(labelTextureQuality, 0, SWT.LEFT);
		labelShadowResolution.setLayoutData(fd_labelShadowResolution);
		labelShadowResolution.setText("Shadow resolution");
		
		comboShadowRes = new Combo(groupGraphics, SWT.READ_ONLY);
		comboShadowRes.addModifyListener(setCustomC);
		comboShadowRes.setItems(new String[] {"Low", "Medium", "High"});
		fd_labelShadowResolution.top = new FormAttachment(comboShadowRes, 3, SWT.TOP);
		FormData fd_comboShadowRes = new FormData();
		fd_comboShadowRes.right = new FormAttachment(comboTextureQuality, 0, SWT.RIGHT);
		comboTextureQuality.select(1);
		fd_comboShadowRes.left = new FormAttachment(0, 200);
		fd_comboShadowRes.top = new FormAttachment(comboShadowQuality, 6);
		comboShadowQuality.select(1);
		comboShadowRes.setLayoutData(fd_comboShadowRes);
		
		Label bar2 = new Label(groupGraphics, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_bar2 = new FormData();
		fd_bar2.top = new FormAttachment(comboShadowRes, 10);
		comboShadowRes.select(1);
		fd_bar2.left = new FormAttachment(0);
		fd_bar2.right = new FormAttachment(100);
		bar2.setLayoutData(fd_bar2);
		
		comboTextureFilter = new Combo(groupGraphics, SWT.READ_ONLY);
		comboTextureFilter.addModifyListener(setCustomC);
		comboTextureFilter.setItems(new String[] {"Nearest", "Bilinear", "Trilinear"});
		FormData fd_comboTextureFilter = new FormData();
		fd_comboTextureFilter.top = new FormAttachment(bar2, 10);
		fd_comboTextureFilter.left = new FormAttachment(0, 200);
		fd_comboTextureFilter.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		comboPreset.select(1);
		comboTextureFilter.setLayoutData(fd_comboTextureFilter);
		
		Label labelTextureFilter = new Label(groupGraphics, SWT.NONE);
		labelTextureFilter.setText("Texture filter");
		FormData fd_labelTextureFilter = new FormData();
		fd_labelTextureFilter.top = new FormAttachment(comboTextureFilter, 3, SWT.TOP);
		comboTextureFilter.select(2);
		fd_labelTextureFilter.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		labelTextureFilter.setLayoutData(fd_labelTextureFilter);
		
		comboAnistro = new Combo(groupGraphics, SWT.READ_ONLY);
		comboAnistro.addModifyListener(setCustomC);
		comboAnistro.setItems(new String[] {"Off", "2x", "4x", "8x", "16x"});
		FormData fd_comboAnistro = new FormData();
		fd_comboAnistro.top = new FormAttachment(comboTextureFilter, 6);
		fd_comboAnistro.left = new FormAttachment(0, 200);
		fd_comboAnistro.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		comboAnistro.setLayoutData(fd_comboAnistro);
		comboAnistro.select(2);
		
		Label labelAnistro = new Label(groupGraphics, SWT.NONE);
		labelAnistro.setText("Anistropic filtering");
		FormData fd_labelAnistro = new FormData();
		fd_labelAnistro.top = new FormAttachment(comboAnistro, 3, SWT.TOP);
		fd_labelAnistro.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		labelAnistro.setLayoutData(fd_labelAnistro);
		
		checkSsao = new Button(groupGraphics, SWT.CHECK);
		checkSsao.addSelectionListener(setCustomS);
		FormData fd_checkSsao = new FormData();
		fd_checkSsao.top = new FormAttachment(labelAnistro, 13);
		fd_checkSsao.left = new FormAttachment(0, 10);
		checkSsao.setLayoutData(fd_checkSsao);
		checkSsao.setText("SSAO");
		
		Label labelSsaoRes = new Label(groupGraphics, SWT.NONE);
		FormData fd_labelSsaoRes = new FormData();
		fd_labelSsaoRes.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		labelSsaoRes.setLayoutData(fd_labelSsaoRes);
		labelSsaoRes.setText("SSAO resolution");
		
		comboSsaoRes = new Combo(groupGraphics, SWT.READ_ONLY);
		comboSsaoRes.addModifyListener(setCustomC);
		fd_labelSsaoRes.top = new FormAttachment(comboSsaoRes, 3, SWT.TOP);
		comboSsaoRes.setItems(new String[] {"Medium", "High"});
		FormData fd_comboSsaoRes = new FormData();
		fd_comboSsaoRes.top = new FormAttachment(comboAnistro, 32);
		fd_comboSsaoRes.left = new FormAttachment(0, 200);
		fd_comboSsaoRes.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		comboSsaoRes.setLayoutData(fd_comboSsaoRes);
		comboSsaoRes.select(0);
		
		Label labelSsaoSamples = new Label(groupGraphics, SWT.NONE);
		FormData fd_labelSsaoSamples = new FormData();
		fd_labelSsaoSamples.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		labelSsaoSamples.setLayoutData(fd_labelSsaoSamples);
		labelSsaoSamples.setText("SSAO samples");
		
		comboSsaoSamples = new Combo(groupGraphics, SWT.READ_ONLY);
		comboSsaoSamples.addModifyListener(setCustomC);
		fd_labelSsaoSamples.top = new FormAttachment(comboSsaoSamples, 3, SWT.TOP);
		comboSsaoSamples.setItems(new String[] {"4", "8", "16", "32"});
		FormData fd_comboSsaoSamples = new FormData();
		fd_comboSsaoSamples.top = new FormAttachment(comboSsaoRes, 6);
		fd_comboSsaoSamples.left = new FormAttachment(0, 200);
		fd_comboSsaoSamples.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		comboSsaoSamples.setLayoutData(fd_comboSsaoSamples);
		comboSsaoSamples.select(0);
		
		checkEdgeSmoothing = new Button(groupGraphics, SWT.CHECK);
		checkEdgeSmoothing.addSelectionListener(setCustomS);
		FormData fd_checkEdgeSmoothing = new FormData();
		fd_checkEdgeSmoothing.top = new FormAttachment(labelSsaoSamples, 13);
		fd_checkEdgeSmoothing.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		checkEdgeSmoothing.setLayoutData(fd_checkEdgeSmoothing);
		checkEdgeSmoothing.setText("Edge smoothing");
		
		checkWorldReflection = new Button(groupGraphics, SWT.CHECK);
		checkWorldReflection.addSelectionListener(setCustomS);
		FormData fd_checkWorldReflection = new FormData();
		fd_checkWorldReflection.top = new FormAttachment(checkEdgeSmoothing, 13);
		fd_checkWorldReflection.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		checkWorldReflection.setLayoutData(fd_checkWorldReflection);
		checkWorldReflection.setText("World reflection");
		
		checkParallax = new Button(groupGraphics, SWT.CHECK);
		checkParallax.addSelectionListener(setCustomS);
		FormData fd_checkParallax = new FormData();
		fd_checkParallax.top = new FormAttachment(checkWorldReflection, 13);
		fd_checkParallax.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		checkParallax.setLayoutData(fd_checkParallax);
		checkParallax.setText("Parallax");
		
		Label padding = new Label(groupGraphics, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_NONE);
		FormData fd_padding = new FormData();
		fd_padding.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		padding.setLayoutData(fd_padding);
		
		Button btnAdvancedOptions = new Button(groupGraphics, SWT.NONE);
		btnAdvancedOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogSettingsAdvanced fa = new DialogSettingsAdvanced(shell, SWT.SHEET);
				fa.open();
			}
		});
		btnAdvancedOptions.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		fd_padding.top = new FormAttachment(btnAdvancedOptions, 6);
		FormData fd_btnAdvancedOptions = new FormData();
		fd_btnAdvancedOptions.top = new FormAttachment(comboSsaoSamples, 88);
		fd_btnAdvancedOptions.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		btnAdvancedOptions.setLayoutData(fd_btnAdvancedOptions);
		btnAdvancedOptions.setText("Advanced settings");
		
		checkUseDefaults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setChildrenEnabled(groupGraphics, checkUseDefaults.getSelection());
				setChildrenEnabled(groupSettings, checkUseDefaults.getSelection());
			}
		});
		
		comboPreset.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				switch(comboPreset.getSelectionIndex()) {
				case 0: // Low
					comboTextureQuality.select(1);
					comboShadowQuality.select(0);
					comboShadowRes.select(1);
					comboTextureFilter.select(1);
					comboAnistro.select(0);
					checkSsao.setSelection(false);
					checkEdgeSmoothing.setSelection(false);
					checkWorldReflection.setSelection(false);
					comboSsaoSamples.select(1);
					comboSsaoRes.select(0);
					checkParallax.setSelection(false);
					comboPreset.select(0);
					break;
				case 1: // Medium
					comboTextureQuality.select(2);
					comboShadowQuality.select(1);
					comboShadowRes.select(2);
					comboTextureFilter.select(2);
					comboAnistro.select(0);
					checkSsao.setSelection(true);
					checkEdgeSmoothing.setSelection(false);
					checkWorldReflection.setSelection(true);
					comboSsaoSamples.select(1);
					comboSsaoRes.select(0);
					checkParallax.setSelection(true);
					comboPreset.select(1);
					break;
				case 2: // High
					comboTextureQuality.select(2);
					comboShadowQuality.select(2);
					comboShadowRes.select(2);
					comboTextureFilter.select(2);
					comboAnistro.select(4);
					checkSsao.setSelection(true);
					checkEdgeSmoothing.setSelection(true);
					checkWorldReflection.setSelection(true);
					comboSsaoSamples.select(2);
					comboSsaoRes.select(0);
					checkParallax.setSelection(true);
					comboPreset.select(2);
					break;
				}
			}
		});
				
		loadUserSettings();
		setChildrenEnabled(groupGraphics, checkUseDefaults.getSelection());
		setChildrenEnabled(groupSettings, checkUseDefaults.getSelection());
		
		Link link = new Link(this, 0);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
				m.setText("Game defaults");
				m.setMessage("If enabled, these settings will be set for every mod you start, whether it is the first time or consequtive launches."
						+ " Keep in mind that if you change the settings in-game, this setting will overwrite those upon next launch unless they are mirrored here."
						+ " Other than that, these settings can be useful for setting standards for new mods, like screen resolution, without having to start the mod once first.");
				m.open();
			}
		});
		FormData fd_link = new FormData();
		fd_link.bottom = new FormAttachment(checkUseDefaults, 0, SWT.BOTTOM);
		fd_link.right = new FormAttachment(groupSettings, 0, SWT.RIGHT);
		link.setText("<a>Info</a>");
		link.setLayoutData(fd_link);
	}
	
	private void loadUserSettings()
	{
		if(!UserSettings.getVar("CustomResX").isEmpty())
			comboResolution.add(UserSettings.getVar("CustomResX") + "x" + UserSettings.getVar("CustomResY") + " (custom)");
		checkUseDefaults.setSelection(toBool("UseDefaults"));
		comboAudioOutput.select(toInt("AudioOutput"));
		if(UserSettings.isSet("ResolutionIndex"))
			comboResolution.select(toInt("ResolutionIndex"));
		checkFullscreen.setSelection(toBool("Fullscreen"));
		comboTextureQuality.select(toInt("TextureQuality"));
		comboShadowQuality.select(toInt("ShadowQuality"));
		comboShadowRes.select(toInt("ShadowResolution"));
		comboTextureFilter.select(toInt("TextureFilter"));
		comboAnistro.select(toInt("Anistropic"));
		checkSsao.setSelection(toBool("SSAO"));
		comboSsaoRes.select(toInt("SSAOResolution"));
		comboSsaoSamples.select(comboSsaoSamples.indexOf(UserSettings.getVar("SSAOSamples")));
		checkEdgeSmoothing.setSelection(toBool("EdgeSmoothing"));
		checkWorldReflection.setSelection(toBool("WorldReflection"));
		checkParallax.setSelection(toBool("Parallax"));
	}
	
	private int toInt(String value) {
		String val = UserSettings.getVar(value);
		if(val.isEmpty()) return 0;
		return Integer.parseInt(val);
	}
	
	private boolean toBool(String value) {
		String val = UserSettings.getVar(value);
		if(val.isEmpty()) return false;
		return Boolean.parseBoolean(val);
	}
	
	private void setChildrenEnabled(Composite composite, boolean enabled)
	{
		for(Control c : composite.getChildren())
		{
			if(c instanceof Composite)
			{
				setChildrenEnabled((Composite) c, enabled);
			}
			c.setEnabled(enabled);
		}
		composite.setEnabled(enabled);
	}
	
	public boolean getUseDefaults() {
		return checkUseDefaults.getSelection();
	}
	
	public int getAudioOutputIndex() {
		return comboAudioOutput.getSelectionIndex();
	}
	
	public int getResolutionIndex() {
		return comboResolution.getSelectionIndex();
	}
	
	public int getTextureQuality() {
		return comboTextureQuality.getSelectionIndex();
	}
	
	public int getShadowQuality() {
		return comboShadowQuality.getSelectionIndex();
	}
	
	public int getShadowRes() {
		return comboShadowRes.getSelectionIndex();
	}
	
	public int getTextureFilter() {
		return comboTextureFilter.getSelectionIndex();
	}
	
	public int getAnistropicFiltering() {
		int index = comboAnistro.getSelectionIndex();
		switch(index) {
		case 0: return 0;
		case 1: return 2;
		case 2: return 4;
		case 3: return 8;
		case 4: return 16;
		default: return 0;
		}
	}
	
	public void saveSettings() {
		UserSettings.setVar("UseDefaults", 		getUseDefaults());
		UserSettings.setVar("AudioOutput", 		getAudioOutputIndex());
		UserSettings.setVar("ResolutionIndex", 	getResolutionIndex());
		UserSettings.setVar("Fullscreen", 		getFullscreen());
		UserSettings.setVar("TextureQuality", 	getTextureQuality());
		UserSettings.setVar("ShadowQuality", 	getShadowQuality());
		UserSettings.setVar("ShadowResolution",	getShadowRes());
		UserSettings.setVar("TextureFilter", 	getTextureFilter());
		UserSettings.setVar("Anistropic", 		getAnistropicFiltering());
		UserSettings.setVar("SSAO",		 		getSsao());
		UserSettings.setVar("SSAOResolution", 	getSsaoRes());
		UserSettings.setVar("SSAOSamples", 		getSsaoSamples());
		UserSettings.setVar("EdgeSmoothing", 	getEdgeSmoothing());
		UserSettings.setVar("WorldReflection", 	getWorldReflection());
		UserSettings.setVar("Parallax",		 	getParallax());
	}
	
	public boolean getSsao() {
		return checkSsao.getSelection();
	}
	
	public int getSsaoRes() {
		return comboSsaoRes.getSelectionIndex();
	}
	
	public int getSsaoSamples() {
		return Integer.parseInt(comboSsaoSamples.getItem(comboSsaoSamples.getSelectionIndex()));
	}
	
	public boolean getEdgeSmoothing() {
		return checkEdgeSmoothing.getSelection();
	}
	
	public boolean getWorldReflection() {
		return checkWorldReflection.getSelection();
	}
	
	public boolean getParallax() {
		return checkParallax.getSelection();
	}
	public boolean getFullscreen() {
		return checkFullscreen.getSelection();
	}
}
