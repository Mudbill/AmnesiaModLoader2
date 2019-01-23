package net.buttology.modloader.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.buttology.modloader.Mod;
import net.buttology.modloader.Modloader;
import net.buttology.modloader.file.FileHandler;
import net.buttology.modloader.gui.widgets.BalloonMessage;
import net.buttology.modloader.util.LangList;
import net.buttology.modloader.util.Log;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;
import net.buttology.util.jeximel.Document;
import net.buttology.util.jeximel.Element;
import net.buttology.util.jeximel.XMLParser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class DialogEditSettings extends Dialog {

	protected Object result;
	protected Shell shell;
	private Button checkShowFps;
	private Button checkShowDebug;
	private Button checkShowErrors;
	private Button checkEnableScript;
	private Button checkEnableQuickSave;
	private Button checkFullscreen;
	private Button checkVsync;
	private Spinner spinnerGamma;
	private Combo comboLanguage;
	private Combo comboResolution;
	private Combo comboTextureQuality;
	private Combo comboTextureFilter;
	private Combo comboAnisotropicFiltering;
	private Button checkParallax;
	private Button checkEnableShadows;
	private Combo comboShadowQuality;
	private Combo comboShadowResolution;
	private Button checkBloom;
	private Button checkImageTrail;
	private Button checkInsanity;
	private Button checkSepia;
	private Button checkRadialBlur;
	private Button checkSsao;
	private Combo comboSsaoResolution;
	private Combo comboSsaoSamples;
	private Button checkWorldReflection;
	private Button checkEdgeSmoothing;
	private Button checkRefraction;
	private Combo comboSoundDevice;
	private Spinner spinnerVolume;
	private Spinner spinnerMaxChannels;
	private Spinner spinnerStreamBuffers;
	private Spinner spinnerStreamBufferSize;
	private Button checkShowPremenu;
	private Button checkLimitFps;
	private Button checkSleep;
	private Button checkSaveConfig;
	private Button checkLoadDebug;
	private Button checkLoadFastEntities;
	private Button checkLoadFastStatic;
	private Button checkLoadFastPhysics;
	private Button radioLoadCache;
	private Button radioWriteCache;
	private Combo comboPreset;
	private ModifyListener setCustomC = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			comboPreset.select(3);
		}
	};
	private SelectionAdapter setCustomS = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			comboPreset.select(3);
		}
	};
	
	private Mod mod;
	private LangList langs;
	private String[] langKeys;
	private boolean customResAdded = false;
	private List<Point> resolutions = new ArrayList<Point>();
	private Combo comboUser;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogEditSettings(Shell parent, int style, Mod mod) {
		super(parent, style);
		this.mod = mod;
		Log.info("Opening Edit Settings dialog.");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		if(mod != null)
		{
			if(mod.getMainSaveName() == null || mod.getMainSaveName().isEmpty())
			{
				Log.error("MainSaveName missing from mod! Is it incomplete?");
				Modloader.promptErrorMessage(shell, "This mod lacks a save directory name! A complete mod requires a MainSaveName entry. Without it I can't save these settings.");
				shell.close();
			}
		}
		
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		Log.info("Closing Edit Settings dialog.");
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle() | SWT.RESIZE);
		shell.setImage(ResourceManager.loadInternalImage("/res/icons/launcher.png"));
		shell.setMinimumSize(new Point(400, 300));
		shell.setSize(601, 481);
		shell.setText("Edit settings");
		shell.setLayout(new FormLayout());
		Util.centerShellOnParent(shell);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.top = new FormAttachment(0, 10);
		fd_tabFolder.left = new FormAttachment(0, 10);
		fd_tabFolder.right = new FormAttachment(100, -10);
		tabFolder.setLayoutData(fd_tabFolder);
		
		TabItem tabDisplay = new TabItem(tabFolder, SWT.NONE);
		tabDisplay.setText("Display");
		
		ScrolledComposite scDisplay = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabDisplay.setControl(scDisplay);
		scDisplay.setExpandHorizontal(true);
		scDisplay.setExpandVertical(true);
		
		Composite compositeDisplay = new Composite(scDisplay, SWT.NONE);
		compositeDisplay.setLayout(new FormLayout());
		
		Label labelResolution = new Label(compositeDisplay, SWT.NONE);
		labelResolution.setText("Resolution");
		FormData fd_labelResolution = new FormData();
		fd_labelResolution.top = new FormAttachment(0, 15);
		fd_labelResolution.left = new FormAttachment(0, 10);
		labelResolution.setLayoutData(fd_labelResolution);
		
		comboResolution = new Combo(compositeDisplay, SWT.READ_ONLY);
		FormData fd_comboResolution = new FormData();
		fd_comboResolution.top = new FormAttachment(labelResolution, 11);
		fd_comboResolution.left = new FormAttachment(labelResolution, 0, SWT.LEFT);
		comboResolution.setLayoutData(fd_comboResolution);
		comboResolution.select(-1);
		
		Button buttonCustomResolution = new Button(compositeDisplay, SWT.NONE);
		buttonCustomResolution.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogCustomResolution d = new DialogCustomResolution(shell, SWT.SHEET);
				Point p = (Point) d.open();
				if(p != null)
				{
					if(!customResAdded)
					{						
						comboResolution.add(p.x + "x" + p.y + " (custom)");
						resolutions.add(p);
						customResAdded = true;
					}
					else
					{
						comboResolution.setItem(comboResolution.getItemCount() - 1, p.x + "x" + p.y + " (custom)");
						resolutions.set(comboResolution.getItemCount() - 1, p);
					}
					comboResolution.select(comboResolution.getItemCount() - 1);
				}
			}
		});
		fd_comboResolution.right = new FormAttachment(buttonCustomResolution, -6);
		buttonCustomResolution.setText("Set custom resolution");
		buttonCustomResolution.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_buttonCustomResolution = new FormData();
		fd_buttonCustomResolution.right = new FormAttachment(100, -10);
		fd_buttonCustomResolution.top = new FormAttachment(0, 40);
		buttonCustomResolution.setLayoutData(fd_buttonCustomResolution);
		
		Group groupOptions = new Group(compositeDisplay, SWT.NONE);
		groupOptions.setText("Options");
		groupOptions.setLayout(new FormLayout());
		FormData fd_groupOptions = new FormData();
		fd_groupOptions.top = new FormAttachment(comboResolution, 6);
		fd_groupOptions.right = new FormAttachment(buttonCustomResolution, 0, SWT.RIGHT);
		fd_groupOptions.left = new FormAttachment(0, 10);
		fd_groupOptions.bottom = new FormAttachment(0, 240);
		groupOptions.setLayoutData(fd_groupOptions);
		
		checkFullscreen = new Button(groupOptions, SWT.CHECK);
		FormData fd_checkFullscreen = new FormData();
		fd_checkFullscreen.top = new FormAttachment(0, 10);
		fd_checkFullscreen.left = new FormAttachment(0, 10);
		checkFullscreen.setLayoutData(fd_checkFullscreen);
		checkFullscreen.setText("Fullscreen");
		
		checkVsync = new Button(groupOptions, SWT.CHECK);
		FormData fd_checkVsync = new FormData();
		fd_checkVsync.top = new FormAttachment(checkFullscreen, 6);
		fd_checkVsync.left = new FormAttachment(checkFullscreen, 0, SWT.LEFT);
		checkVsync.setLayoutData(fd_checkVsync);
		checkVsync.setText("Vsync");
		
		spinnerGamma = new Spinner(groupOptions, SWT.BORDER);
		spinnerGamma.setSelection(12);
		spinnerGamma.setDigits(1);
		FormData fd_spinnerGamma = new FormData();
		fd_spinnerGamma.right = new FormAttachment(100, -10);
		fd_spinnerGamma.left = new FormAttachment(100, -104);
		spinnerGamma.setLayoutData(fd_spinnerGamma);
		
		Label labelGamma = new Label(groupOptions, SWT.NONE);
		fd_spinnerGamma.top = new FormAttachment(labelGamma, -3, SWT.TOP);
		FormData fd_labelGamma = new FormData();
		fd_labelGamma.top = new FormAttachment(checkVsync, 11);
		fd_labelGamma.left = new FormAttachment(checkFullscreen, 0, SWT.LEFT);
		labelGamma.setLayoutData(fd_labelGamma);
		labelGamma.setText("Gamma");
		
		Label labelLanguage = new Label(groupOptions, SWT.NONE);
		FormData fd_labelLanguage = new FormData();
		fd_labelLanguage.top = new FormAttachment(labelGamma, 16);
		fd_labelLanguage.right = new FormAttachment(checkVsync, 0, SWT.RIGHT);
		labelLanguage.setLayoutData(fd_labelLanguage);
		labelLanguage.setText("Language");
		
		comboLanguage = new Combo(groupOptions, SWT.READ_ONLY);
		FormData fd_comboLanguage = new FormData();
		fd_comboLanguage.right = new FormAttachment(spinnerGamma, 0, SWT.RIGHT);
		fd_comboLanguage.left = new FormAttachment(0, 10);
		fd_comboLanguage.top = new FormAttachment(labelLanguage, 6);
		comboLanguage.setLayoutData(fd_comboLanguage);
		scDisplay.setContent(compositeDisplay);
		scDisplay.setMinSize(compositeDisplay.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		TabItem tabGraphics = new TabItem(tabFolder, SWT.NONE);
		tabGraphics.setText("Graphics");
		
		ScrolledComposite scGraphics = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabGraphics.setControl(scGraphics);
		scGraphics.setExpandHorizontal(true);
		scGraphics.setExpandVertical(true);
		
		Composite compositeGraphics = new Composite(scGraphics, SWT.NONE);
		compositeGraphics.setLayout(new FormLayout());
		
		comboPreset = new Combo(compositeGraphics, SWT.READ_ONLY);
		comboPreset.setItems(new String[] {"Low", "Medium", "High", "Custom"});
		FormData fd_comboPreset = new FormData();
		fd_comboPreset.left = new FormAttachment(100, -293);
		fd_comboPreset.top = new FormAttachment(0, 10);
		fd_comboPreset.right = new FormAttachment(100, -10);
		comboPreset.setLayoutData(fd_comboPreset);
		comboPreset.select(3);
		comboPreset.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				switch(comboPreset.getSelectionIndex()) {
				case 0: // Low
					comboTextureQuality.select(1);
					comboShadowQuality.select(0);
					comboShadowResolution.select(1);
					comboTextureFilter.select(1);
					comboAnisotropicFiltering.select(0);
					checkSsao.setSelection(false);
					checkEdgeSmoothing.setSelection(false);
					checkWorldReflection.setSelection(false);
					comboSsaoSamples.select(1);
					comboSsaoResolution.select(0);
					checkParallax.setSelection(false);
					comboPreset.select(0);
					break;
				case 1: // Medium
					comboTextureQuality.select(2);
					comboShadowQuality.select(1);
					comboShadowResolution.select(2);
					comboTextureFilter.select(2);
					comboAnisotropicFiltering.select(0);
					checkSsao.setSelection(true);
					checkEdgeSmoothing.setSelection(false);
					checkWorldReflection.setSelection(true);
					comboSsaoSamples.select(1);
					comboSsaoResolution.select(0);
					checkParallax.setSelection(true);
					comboPreset.select(1);
					break;
				case 2: // High
					comboTextureQuality.select(2);
					comboShadowQuality.select(2);
					comboShadowResolution.select(2);
					comboTextureFilter.select(2);
					comboAnisotropicFiltering.select(4);
					checkSsao.setSelection(true);
					checkEdgeSmoothing.setSelection(true);
					checkWorldReflection.setSelection(true);
					comboSsaoSamples.select(2);
					comboSsaoResolution.select(0);
					checkParallax.setSelection(true);
					comboPreset.select(2);
					break;
				}
			}
		});
		
		Label labelPreset = new Label(compositeGraphics, SWT.NONE);
		labelPreset.setText("Preset");
		FormData fd_labelPreset = new FormData();
		fd_labelPreset.top = new FormAttachment(comboPreset, 3, SWT.TOP);
		fd_labelPreset.left = new FormAttachment(0, 10);
		labelPreset.setLayoutData(fd_labelPreset);
		
		Label separator = new Label(compositeGraphics, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_separator = new FormData();
		fd_separator.top = new FormAttachment(comboPreset, 6);
		fd_separator.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		fd_separator.bottom = new FormAttachment(comboPreset, 8, SWT.BOTTOM);
		fd_separator.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		separator.setLayoutData(fd_separator);
		
		Group groupTextures = new Group(compositeGraphics, SWT.NONE);
		groupTextures.setText("Textures");
		groupTextures.setLayout(new FormLayout());
		FormData fd_groupTextures = new FormData();
		fd_groupTextures.top = new FormAttachment(separator, 6);
		fd_groupTextures.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		fd_groupTextures.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		fd_groupTextures.bottom = new FormAttachment(0, 189);
		groupTextures.setLayoutData(fd_groupTextures);
		
		comboTextureQuality = new Combo(groupTextures, SWT.READ_ONLY);
		FormData fd_comboTextureQuality = new FormData();
		fd_comboTextureQuality.left = new FormAttachment(100, -266);
		fd_comboTextureQuality.top = new FormAttachment(0, 10);
		fd_comboTextureQuality.right = new FormAttachment(100, -10);
		comboTextureQuality.setLayoutData(fd_comboTextureQuality);
		comboTextureQuality.setItems(new String[] {"Low", "Medium", "High"});
		comboTextureQuality.select(1);
		comboTextureQuality.addModifyListener(setCustomC);
		
		Label labelTextureQuality = new Label(groupTextures, SWT.NONE);
		FormData fd_labelTextureQuality = new FormData();
		fd_labelTextureQuality.top = new FormAttachment(0, 13);
		fd_labelTextureQuality.left = new FormAttachment(0, 10);
		labelTextureQuality.setLayoutData(fd_labelTextureQuality);
		labelTextureQuality.setText("Texture quality");
		
		comboTextureFilter = new Combo(groupTextures, SWT.READ_ONLY);
		FormData fd_comboTextureFilter = new FormData();
		fd_comboTextureFilter.left = new FormAttachment(comboTextureQuality, 0, SWT.LEFT);
		fd_comboTextureFilter.top = new FormAttachment(comboTextureQuality, 6);
		fd_comboTextureFilter.right = new FormAttachment(comboTextureQuality, 0, SWT.RIGHT);
		comboTextureFilter.setLayoutData(fd_comboTextureFilter);
		comboTextureFilter.setItems(new String[] {"Nearest", "Bilinear", "Trilinear"});
		comboTextureFilter.select(2);
		comboTextureFilter.addModifyListener(setCustomC);
		
		Label labelTextureFilter = new Label(groupTextures, SWT.NONE);
		FormData fd_labelTextureFilter = new FormData();
		fd_labelTextureFilter.top = new FormAttachment(comboTextureFilter, 3, SWT.TOP);
		fd_labelTextureFilter.left = new FormAttachment(labelTextureQuality, 0, SWT.LEFT);
		labelTextureFilter.setLayoutData(fd_labelTextureFilter);
		labelTextureFilter.setText("Texture filter");
		
		comboAnisotropicFiltering = new Combo(groupTextures, SWT.READ_ONLY);
		FormData fd_comboAnistropicFiltering = new FormData();
		fd_comboAnistropicFiltering.left = new FormAttachment(comboTextureQuality, 0, SWT.LEFT);
		fd_comboAnistropicFiltering.top = new FormAttachment(comboTextureFilter, 6);
		fd_comboAnistropicFiltering.right = new FormAttachment(comboTextureQuality, 0, SWT.RIGHT);
		comboAnisotropicFiltering.setLayoutData(fd_comboAnistropicFiltering);
		comboAnisotropicFiltering.setItems(new String[] {"Off", "2x", "4x", "8x", "16x"});
		comboAnisotropicFiltering.select(2);
		comboAnisotropicFiltering.addModifyListener(setCustomC);
		
		Label labelAnistropicFiltering = new Label(groupTextures, SWT.NONE);
		FormData fd_labelAnistropicFiltering = new FormData();
		fd_labelAnistropicFiltering.top = new FormAttachment(comboAnisotropicFiltering, 3, SWT.TOP);
		fd_labelAnistropicFiltering.left = new FormAttachment(labelTextureQuality, 0, SWT.LEFT);
		labelAnistropicFiltering.setLayoutData(fd_labelAnistropicFiltering);
		labelAnistropicFiltering.setText("Anistropic filtering");
		
		checkParallax = new Button(groupTextures, SWT.CHECK);
		FormData fd_checkParallax = new FormData();
		fd_checkParallax.top = new FormAttachment(labelAnistropicFiltering, 14);
		fd_checkParallax.left = new FormAttachment(labelTextureQuality, 0, SWT.LEFT);
		checkParallax.setLayoutData(fd_checkParallax);
		checkParallax.setText("Parallax");
		checkParallax.addSelectionListener(setCustomS);
		
		Group groupShadows = new Group(compositeGraphics, SWT.NONE);
		groupShadows.setText("Shadows");
		groupShadows.setLayout(new FormLayout());
		FormData fd_groupShadows = new FormData();
		fd_groupShadows.top = new FormAttachment(groupTextures, 6);
		fd_groupShadows.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		fd_groupShadows.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		fd_groupShadows.bottom = new FormAttachment(0, 309);
		groupShadows.setLayoutData(fd_groupShadows);
		
		checkEnableShadows = new Button(groupShadows, SWT.CHECK);
		FormData fd_checkEnableShadows = new FormData();
		fd_checkEnableShadows.top = new FormAttachment(0, 10);
		fd_checkEnableShadows.left = new FormAttachment(0, 10);
		checkEnableShadows.setLayoutData(fd_checkEnableShadows);
		checkEnableShadows.setSelection(true);
		checkEnableShadows.setText("Enable shadows");
		
		comboShadowQuality = new Combo(groupShadows, SWT.READ_ONLY);
		FormData fd_comboShadowQuality = new FormData();
		fd_comboShadowQuality.left = new FormAttachment(100, -266);
		fd_comboShadowQuality.top = new FormAttachment(checkEnableShadows, 10);
		fd_comboShadowQuality.right = new FormAttachment(100, -10);
		comboShadowQuality.setLayoutData(fd_comboShadowQuality);
		comboShadowQuality.setItems(new String[] {"Low", "Medium", "High"});
		comboShadowQuality.select(1);
		comboShadowQuality.addModifyListener(setCustomC);

		
		Label labelShadowQuality = new Label(groupShadows, SWT.NONE);
		FormData fd_labelShadowQuality = new FormData();
		fd_labelShadowQuality.top = new FormAttachment(comboShadowQuality, 3, SWT.TOP);
		fd_labelShadowQuality.left = new FormAttachment(checkEnableShadows, 0, SWT.LEFT);
		labelShadowQuality.setLayoutData(fd_labelShadowQuality);
		labelShadowQuality.setText("Shadow quality");
		
		comboShadowResolution = new Combo(groupShadows, SWT.READ_ONLY);
		FormData fd_comboShadowResolution = new FormData();
		fd_comboShadowResolution.left = new FormAttachment(comboShadowQuality, 0, SWT.LEFT);
		fd_comboShadowResolution.top = new FormAttachment(comboShadowQuality, 6);
		fd_comboShadowResolution.right = new FormAttachment(comboShadowQuality, 0, SWT.RIGHT);
		comboShadowResolution.setLayoutData(fd_comboShadowResolution);
		comboShadowResolution.setItems(new String[] {"Low", "Medium", "High"});
		comboShadowResolution.select(1);
		comboShadowResolution.addModifyListener(setCustomC);

		Label labelShadowResolution = new Label(groupShadows, SWT.NONE);
		FormData fd_labelShadowResolution = new FormData();
		fd_labelShadowResolution.top = new FormAttachment(comboShadowResolution, 3, SWT.TOP);
		fd_labelShadowResolution.left = new FormAttachment(checkEnableShadows, 0, SWT.LEFT);
		labelShadowResolution.setLayoutData(fd_labelShadowResolution);
		labelShadowResolution.setText("Shadow resolution");
		
		Group groupPostEffects = new Group(compositeGraphics, SWT.NONE);
		groupPostEffects.setText("Post effects");
		groupPostEffects.setLayout(new FormLayout());
		FormData fd_groupPostEffects = new FormData();
		fd_groupPostEffects.top = new FormAttachment(groupShadows, 6);
		fd_groupPostEffects.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		fd_groupPostEffects.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		fd_groupPostEffects.bottom = new FormAttachment(0, 409);
		groupPostEffects.setLayoutData(fd_groupPostEffects);
		
		checkBloom = new Button(groupPostEffects, SWT.CHECK);
		FormData fd_checkBloom = new FormData();
		fd_checkBloom.top = new FormAttachment(0, 10);
		fd_checkBloom.left = new FormAttachment(0, 10);
		checkBloom.setLayoutData(fd_checkBloom);
		checkBloom.setText("Bloom");
		
		checkImageTrail = new Button(groupPostEffects, SWT.CHECK);
		FormData fd_checkImageTrail = new FormData();
		fd_checkImageTrail.top = new FormAttachment(checkBloom, 6);
		fd_checkImageTrail.left = new FormAttachment(checkBloom, 0, SWT.LEFT);
		checkImageTrail.setLayoutData(fd_checkImageTrail);
		checkImageTrail.setText("Image trail");
		
		checkSepia = new Button(groupPostEffects, SWT.CHECK);
		FormData fd_checkSepia = new FormData();
		fd_checkSepia.top = new FormAttachment(checkBloom, 0, SWT.TOP);
		fd_checkSepia.left = new FormAttachment(50);
		checkSepia.setLayoutData(fd_checkSepia);
		checkSepia.setText("Sepia");
		
		checkRadialBlur = new Button(groupPostEffects, SWT.CHECK);
		FormData fd_checkRadialBlur = new FormData();
		fd_checkRadialBlur.top = new FormAttachment(checkImageTrail, 0, SWT.TOP);
		fd_checkRadialBlur.left = new FormAttachment(checkSepia, 0, SWT.LEFT);
		checkRadialBlur.setLayoutData(fd_checkRadialBlur);
		checkRadialBlur.setText("Radial blur");
		
		checkInsanity = new Button(groupPostEffects, SWT.CHECK);
		FormData fd_checkInsanity = new FormData();
		fd_checkInsanity.top = new FormAttachment(checkImageTrail, 6);
		fd_checkInsanity.left = new FormAttachment(checkBloom, 0, SWT.LEFT);
		checkInsanity.setLayoutData(fd_checkInsanity);
		checkInsanity.setText("Insanity");
		
		Group groupSsao = new Group(compositeGraphics, SWT.NONE);
		groupSsao.setText("SSAO");
		groupSsao.setLayout(new FormLayout());
		FormData fd_groupSsao = new FormData();
		fd_groupSsao.top = new FormAttachment(groupPostEffects, 6);
		fd_groupSsao.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		fd_groupSsao.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		fd_groupSsao.bottom = new FormAttachment(0, 529);
		groupSsao.setLayoutData(fd_groupSsao);
		
		checkSsao = new Button(groupSsao, SWT.CHECK);
		FormData fd_checkSsao = new FormData();
		fd_checkSsao.top = new FormAttachment(0, 10);
		fd_checkSsao.left = new FormAttachment(0, 10);
		checkSsao.setLayoutData(fd_checkSsao);
		checkSsao.setText("Enable SSAO");
		checkSsao.addSelectionListener(setCustomS);

		comboSsaoResolution = new Combo(groupSsao, SWT.READ_ONLY);
		FormData fd_comboSsaoResolution = new FormData();
		fd_comboSsaoResolution.left = new FormAttachment(100, -266);
		fd_comboSsaoResolution.top = new FormAttachment(checkSsao, 10);
		fd_comboSsaoResolution.right = new FormAttachment(100, -10);
		comboSsaoResolution.setLayoutData(fd_comboSsaoResolution);
		comboSsaoResolution.setItems(new String[] {"Medium", "High"});
		comboSsaoResolution.select(0);
		comboSsaoResolution.addModifyListener(setCustomC);

		Label labelSsaoResolution = new Label(groupSsao, SWT.NONE);
		FormData fd_labelSsaoResolution = new FormData();
		fd_labelSsaoResolution.top = new FormAttachment(comboSsaoResolution, 3, SWT.TOP);
		fd_labelSsaoResolution.left = new FormAttachment(checkSsao, 0, SWT.LEFT);
		labelSsaoResolution.setLayoutData(fd_labelSsaoResolution);
		labelSsaoResolution.setText("SSAO resolution");
		
		comboSsaoSamples = new Combo(groupSsao, SWT.READ_ONLY);
		FormData fd_comboSsaoSamples = new FormData();
		fd_comboSsaoSamples.left = new FormAttachment(comboSsaoResolution, 0, SWT.LEFT);
		fd_comboSsaoSamples.top = new FormAttachment(comboSsaoResolution, 6);
		fd_comboSsaoSamples.right = new FormAttachment(comboSsaoResolution, 0, SWT.RIGHT);
		comboSsaoSamples.setLayoutData(fd_comboSsaoSamples);
		comboSsaoSamples.setItems(new String[] {"4", "8", "16", "32"});
		comboSsaoSamples.select(0);
		comboSsaoSamples.addModifyListener(setCustomC);

		Label labelSsaoSamples = new Label(groupSsao, SWT.NONE);
		FormData fd_labelSsaoSamples = new FormData();
		fd_labelSsaoSamples.top = new FormAttachment(comboSsaoSamples, 3, SWT.TOP);
		fd_labelSsaoSamples.left = new FormAttachment(checkSsao, 0, SWT.LEFT);
		labelSsaoSamples.setLayoutData(fd_labelSsaoSamples);
		labelSsaoSamples.setText("SSAO samples");
		
		Group groupMiscellaneous = new Group(compositeGraphics, SWT.NONE);
		groupMiscellaneous.setText("Miscellaneous");
		groupMiscellaneous.setLayout(new FormLayout());
		FormData fd_groupMiscellaneous = new FormData();
		fd_groupMiscellaneous.top = new FormAttachment(groupSsao, 6);
		fd_groupMiscellaneous.right = new FormAttachment(comboPreset, 0, SWT.RIGHT);
		fd_groupMiscellaneous.left = new FormAttachment(labelPreset, 0, SWT.LEFT);
		fd_groupMiscellaneous.bottom = new FormAttachment(0, 607);
		groupMiscellaneous.setLayoutData(fd_groupMiscellaneous);
		
		checkWorldReflection = new Button(groupMiscellaneous, SWT.CHECK);
		FormData fd_checkWorldReflection = new FormData();
		fd_checkWorldReflection.top = new FormAttachment(0, 10);
		fd_checkWorldReflection.left = new FormAttachment(0, 10);
		checkWorldReflection.setLayoutData(fd_checkWorldReflection);
		checkWorldReflection.setText("World reflection");
		checkWorldReflection.addSelectionListener(setCustomS);

		checkEdgeSmoothing = new Button(groupMiscellaneous, SWT.CHECK);
		FormData fd_checkEdgeSmoothing = new FormData();
		fd_checkEdgeSmoothing.top = new FormAttachment(checkWorldReflection, 6);
		fd_checkEdgeSmoothing.left = new FormAttachment(checkWorldReflection, 0, SWT.LEFT);
		checkEdgeSmoothing.setLayoutData(fd_checkEdgeSmoothing);
		checkEdgeSmoothing.setText("Edge smoothing");
		checkEdgeSmoothing.addSelectionListener(setCustomS);

		checkRefraction = new Button(groupMiscellaneous, SWT.CHECK);
		FormData fd_checkRefraction = new FormData();
		fd_checkRefraction.top = new FormAttachment(checkWorldReflection, 0, SWT.TOP);
		fd_checkRefraction.left = new FormAttachment(50);
		checkRefraction.setLayoutData(fd_checkRefraction);
		checkRefraction.setText("Refraction");
		checkRefraction.addSelectionListener(setCustomS);
		scGraphics.setContent(compositeGraphics);
		scGraphics.setMinSize(compositeGraphics.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		TabItem tabAudio = new TabItem(tabFolder, SWT.NONE);
		tabAudio.setText("Audio");
		
		ScrolledComposite scAudio = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabAudio.setControl(scAudio);
		scAudio.setExpandHorizontal(true);
		scAudio.setExpandVertical(true);
		
		Composite compositeAudio = new Composite(scAudio, SWT.NONE);
		compositeAudio.setLayout(new FormLayout());
		
		Label labelDevice = new Label(compositeAudio, SWT.NONE);
		FormData fd_labelDevice = new FormData();
		fd_labelDevice.top = new FormAttachment(0, 10);
		fd_labelDevice.left = new FormAttachment(0, 10);
		labelDevice.setLayoutData(fd_labelDevice);
		labelDevice.setText("Device");
		
		comboSoundDevice = new Combo(compositeAudio, SWT.READ_ONLY);
		FormData fd_comboSoundDevice = new FormData();
		fd_comboSoundDevice.top = new FormAttachment(labelDevice, 6);
		fd_comboSoundDevice.left = new FormAttachment(labelDevice, 0, SWT.LEFT);
		fd_comboSoundDevice.right = new FormAttachment(100, -10);
		comboSoundDevice.setLayoutData(fd_comboSoundDevice);
		comboSoundDevice.setToolTipText("Select audio output (see info)");
		
		Link linkAudioInfo = new Link(compositeAudio, SWT.NONE);
		linkAudioInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
				m.setText("Audio output information");
				m.setMessage("Changing the output device might be unreliable because Amnesia only saves the device index. The first index is always the default system output."
						+ " This means that changing the default system output also affects Amnesia's output device. Keep this in mind.");
				m.open();
			}
		});
		FormData fd_linkAudioInfo = new FormData();
		fd_linkAudioInfo.top = new FormAttachment(labelDevice, 0, SWT.TOP);
		fd_linkAudioInfo.right = new FormAttachment(100, -10);
		linkAudioInfo.setLayoutData(fd_linkAudioInfo);
		linkAudioInfo.setText("<a>Info</a>");
		
		Label labelVolume = new Label(compositeAudio, SWT.NONE);
		FormData fd_labelVolume = new FormData();
		fd_labelVolume.left = new FormAttachment(labelDevice, 0, SWT.LEFT);
		labelVolume.setLayoutData(fd_labelVolume);
		labelVolume.setText("Volume (%)");
		
		spinnerVolume = new Spinner(compositeAudio, SWT.BORDER);
		fd_labelVolume.top = new FormAttachment(spinnerVolume, 3, SWT.TOP);
		FormData fd_spinnerVolume = new FormData();
		fd_spinnerVolume.top = new FormAttachment(comboSoundDevice, 6);
		fd_spinnerVolume.right = new FormAttachment(comboSoundDevice, 0, SWT.RIGHT);
		fd_spinnerVolume.left = new FormAttachment(comboSoundDevice, -100, SWT.RIGHT);
		spinnerVolume.setLayoutData(fd_spinnerVolume);
		spinnerVolume.setToolTipText("Set the audio volume (from 0 to 100)");
		spinnerVolume.setSelection(100);
		
		Group groupAdvanced = new Group(compositeAudio, SWT.NONE);
		groupAdvanced.setText("Advanced");
		groupAdvanced.setLayout(new FormLayout());
		FormData fd_groupAdvanced = new FormData();
		fd_groupAdvanced.top = new FormAttachment(labelVolume, 20);
		fd_groupAdvanced.bottom = new FormAttachment(0, 207);
		fd_groupAdvanced.left = new FormAttachment(0, 10);
		fd_groupAdvanced.right = new FormAttachment(100, -10);
		groupAdvanced.setLayoutData(fd_groupAdvanced);
		
		spinnerMaxChannels = new Spinner(groupAdvanced, SWT.BORDER);
		spinnerMaxChannels.setToolTipText("Default: 32");
		spinnerMaxChannels.setMaximum(128);
		spinnerMaxChannels.setSelection(32);
		FormData fd_spinnerMaxChannels = new FormData();
		fd_spinnerMaxChannels.top = new FormAttachment(0, 5);
		fd_spinnerMaxChannels.left = new FormAttachment(100, -110);
		fd_spinnerMaxChannels.right = new FormAttachment(100, -10);
		spinnerMaxChannels.setLayoutData(fd_spinnerMaxChannels);
		
		spinnerStreamBuffers = new Spinner(groupAdvanced, SWT.BORDER);
		spinnerStreamBuffers.setToolTipText("Default: 4");
		spinnerStreamBuffers.setMaximum(64);
		spinnerStreamBuffers.setSelection(4);
		FormData fd_spinnerStreamBuffers = new FormData();
		fd_spinnerStreamBuffers.top = new FormAttachment(0, 33);
		fd_spinnerStreamBuffers.left = new FormAttachment(100, -110);
		fd_spinnerStreamBuffers.right = new FormAttachment(100, -10);
		spinnerStreamBuffers.setLayoutData(fd_spinnerStreamBuffers);
		
		spinnerStreamBufferSize = new Spinner(groupAdvanced, SWT.BORDER);
		spinnerStreamBufferSize.setToolTipText("Default: 262144");
		spinnerStreamBufferSize.setMaximum(9999999);
		spinnerStreamBufferSize.setSelection(262144);
		FormData fd_spinnerStreamBufferSize = new FormData();
		fd_spinnerStreamBufferSize.top = new FormAttachment(0, 61);
		fd_spinnerStreamBufferSize.left = new FormAttachment(100, -110);
		fd_spinnerStreamBufferSize.right = new FormAttachment(100, -10);
		spinnerStreamBufferSize.setLayoutData(fd_spinnerStreamBufferSize);
		
		Label lblMaxChannels = new Label(groupAdvanced, SWT.NONE);
		FormData fd_lblMaxChannels = new FormData();
		fd_lblMaxChannels.top = new FormAttachment(spinnerMaxChannels, 3, SWT.TOP);
		fd_lblMaxChannels.left = new FormAttachment(0, 10);
		lblMaxChannels.setLayoutData(fd_lblMaxChannels);
		lblMaxChannels.setText("Max channels");
		
		Label lblStreamBuffers = new Label(groupAdvanced, SWT.NONE);
		FormData fd_lblStreamBuffers = new FormData();
		fd_lblStreamBuffers.top = new FormAttachment(spinnerStreamBuffers, 3, SWT.TOP);
		fd_lblStreamBuffers.left = new FormAttachment(lblMaxChannels, 0, SWT.LEFT);
		lblStreamBuffers.setLayoutData(fd_lblStreamBuffers);
		lblStreamBuffers.setText("Stream buffers");
		
		Label lblStreamBufferSize = new Label(groupAdvanced, SWT.NONE);
		FormData fd_lblStreamBufferSize = new FormData();
		fd_lblStreamBufferSize.top = new FormAttachment(spinnerStreamBufferSize, 3, SWT.TOP);
		fd_lblStreamBufferSize.left = new FormAttachment(lblMaxChannels, 0, SWT.LEFT);
		lblStreamBufferSize.setLayoutData(fd_lblStreamBufferSize);
		lblStreamBufferSize.setText("Stream buffer size");
		scAudio.setContent(compositeAudio);
		scAudio.setMinSize(compositeAudio.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Button buttonCancel = new Button(shell, SWT.NONE);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		fd_tabFolder.bottom = new FormAttachment(buttonCancel, -6);
		
		TabItem tabAdvanced = new TabItem(tabFolder, SWT.NONE);
		tabAdvanced.setText("Advanced");
		
		ScrolledComposite scAdvanced = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabAdvanced.setControl(scAdvanced);
		scAdvanced.setExpandHorizontal(true);
		scAdvanced.setExpandVertical(true);
		
		Composite compositeAdvanced = new Composite(scAdvanced, SWT.NONE);
		compositeAdvanced.setLayout(new FormLayout());
		
		checkShowPremenu = new Button(compositeAdvanced, SWT.CHECK);
		checkShowPremenu.setSelection(true);
		FormData fd_checkShowPremenu = new FormData();
		fd_checkShowPremenu.top = new FormAttachment(0, 10);
		fd_checkShowPremenu.left = new FormAttachment(0, 10);
		checkShowPremenu.setLayoutData(fd_checkShowPremenu);
		checkShowPremenu.setText("Show pre-menu");
		
		checkLimitFps = new Button(compositeAdvanced, SWT.CHECK);
		checkLimitFps.setSelection(true);
		FormData fd_checkLimitFps = new FormData();
		fd_checkLimitFps.top = new FormAttachment(checkShowPremenu, 6);
		fd_checkLimitFps.left = new FormAttachment(checkShowPremenu, 0, SWT.LEFT);
		checkLimitFps.setLayoutData(fd_checkLimitFps);
		checkLimitFps.setText("Limit engine FPS");
		
		Group groupDebug = new Group(compositeAdvanced, SWT.NONE);
		groupDebug.setText("Debug");
		groupDebug.setLayout(new FormLayout());
		FormData fd_groupDebug = new FormData();
		fd_groupDebug.left = new FormAttachment(checkShowPremenu, 0, SWT.LEFT);
		fd_groupDebug.bottom = new FormAttachment(0, 283);
		fd_groupDebug.right = new FormAttachment(100, -10);
		groupDebug.setLayoutData(fd_groupDebug);
		
		checkLoadFastPhysics = new Button(groupDebug, SWT.CHECK);
		FormData fd_checkLoadFastPhysics = new FormData();
		checkLoadFastPhysics.setLayoutData(fd_checkLoadFastPhysics);
		checkLoadFastPhysics.setText("Load fast physics");
		
		checkLoadFastEntities = new Button(groupDebug, SWT.CHECK);
		fd_checkLoadFastPhysics.left = new FormAttachment(checkLoadFastEntities, 0, SWT.LEFT);
		FormData fd_checkLoadFastEntities = new FormData();
		fd_checkLoadFastEntities.top = new FormAttachment(0, 32);
		fd_checkLoadFastEntities.left = new FormAttachment(0, 10);
		checkLoadFastEntities.setLayoutData(fd_checkLoadFastEntities);
		checkLoadFastEntities.setText("Load fast entities");
		
		checkLoadFastStatic = new Button(groupDebug, SWT.CHECK);
		fd_checkLoadFastPhysics.top = new FormAttachment(checkLoadFastStatic, 6);
		FormData fd_checkLoadFastStatic = new FormData();
		fd_checkLoadFastStatic.left = new FormAttachment(0, 10);
		fd_checkLoadFastStatic.top = new FormAttachment(checkLoadFastEntities, 6);
		checkLoadFastStatic.setLayoutData(fd_checkLoadFastStatic);
		checkLoadFastStatic.setText("Load fast static objects");
		
		checkSleep = new Button(compositeAdvanced, SWT.CHECK);
		
		checkLoadDebug = new Button(groupDebug, SWT.CHECK);
		FormData fd_checkLoadDebug = new FormData();
		fd_checkLoadDebug.left = new FormAttachment(0, 10);
		fd_checkLoadDebug.bottom = new FormAttachment(checkLoadFastEntities, -6);
		checkLoadDebug.setLayoutData(fd_checkLoadDebug);
		checkLoadDebug.setText("Load debug tools");
		checkSleep.setSelection(true);
		FormData fd_checkSleep = new FormData();
		fd_checkSleep.top = new FormAttachment(checkLimitFps, 6);
		fd_checkSleep.left = new FormAttachment(checkShowPremenu, 0, SWT.LEFT);
		checkSleep.setLayoutData(fd_checkSleep);
		checkSleep.setText("Sleep when out of focus");
		
		checkSaveConfig = new Button(compositeAdvanced, SWT.CHECK);
		fd_groupDebug.top = new FormAttachment(checkSaveConfig, 6);
		
		Link link = new Link(groupDebug, SWT.NONE);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
				m.setText("Debug tools");
				m.setMessage("This loads the debug tools in-game, useful for developing mods.\n\nF1 - Opens/closes the debug tools panel.\nF2 - Quick-reloads the map."
						+ "\nF3 - Toggles 3x game speed on/off\nF4 - Creates a quick save (must be enabled in user specific settings)"
						+ "\nF5 - Loads a quick save (must be enabled in user specific settings)\nF8 - Crashes the game.");
				m.open();
			}
		});
		FormData fd_link = new FormData();
		fd_link.bottom = new FormAttachment(checkLoadDebug, 0, SWT.BOTTOM);
		fd_link.right = new FormAttachment(100, -10);
		link.setLayoutData(fd_link);
		link.setText("<a>Info</a>");
		
		Label labelCacheFiles = new Label(groupDebug, SWT.NONE);
		FormData fd_labelCacheFiles = new FormData();
		fd_labelCacheFiles.top = new FormAttachment(checkLoadFastPhysics, 6);
		fd_labelCacheFiles.left = new FormAttachment(checkLoadFastPhysics, 0, SWT.LEFT);
		labelCacheFiles.setLayoutData(fd_labelCacheFiles);
		labelCacheFiles.setText("Cache files");
		
		radioLoadCache = new Button(groupDebug, SWT.RADIO);
		radioLoadCache.setSelection(true);
		FormData fd_radioLoadCache = new FormData();
		fd_radioLoadCache.left = new FormAttachment(0, 20);
		fd_radioLoadCache.top = new FormAttachment(labelCacheFiles, 6);
		radioLoadCache.setLayoutData(fd_radioLoadCache);
		radioLoadCache.setText("Load map cache files but don't generate them");
		
		radioWriteCache = new Button(groupDebug, SWT.RADIO);
		FormData fd_radioWriteCache = new FormData();
		fd_radioWriteCache.top = new FormAttachment(radioLoadCache, 6);
		fd_radioWriteCache.left = new FormAttachment(radioLoadCache, 0, SWT.LEFT);
		radioWriteCache.setLayoutData(fd_radioWriteCache);
		radioWriteCache.setText("Generate map cache files but don't load them");
		checkSaveConfig.setSelection(true);
		FormData fd_checkSaveConfig = new FormData();
		fd_checkSaveConfig.top = new FormAttachment(checkSleep, 6);
		fd_checkSaveConfig.left = new FormAttachment(checkShowPremenu, 0, SWT.LEFT);
		checkSaveConfig.setLayoutData(fd_checkSaveConfig);
		checkSaveConfig.setText("Save config changes done in-game");
		scAdvanced.setContent(compositeAdvanced);
		scAdvanced.setMinSize(compositeAdvanced.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		TabItem tabUserSpecific = new TabItem(tabFolder, SWT.NONE);
		tabUserSpecific.setText("User specific");
		
		ScrolledComposite scUserSpecific = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabUserSpecific.setControl(scUserSpecific);
		scUserSpecific.setExpandHorizontal(true);
		scUserSpecific.setExpandVertical(true);
		
		Composite compositeUserSpecific = new Composite(scUserSpecific, SWT.NONE);
		compositeUserSpecific.setLayout(new FormLayout());
		
		Label labelUser = new Label(compositeUserSpecific, SWT.NONE);
		FormData fd_labelUser = new FormData();
		fd_labelUser.top = new FormAttachment(0, 10);
		fd_labelUser.left = new FormAttachment(0, 10);
		labelUser.setLayoutData(fd_labelUser);
		labelUser.setText("User");
		
		comboUser = new Combo(compositeUserSpecific, SWT.READ_ONLY);
		comboUser.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				loadSettingsForUser(comboUser.getText());
			}
		});
		FormData fd_comboUser = new FormData();
		fd_comboUser.left = new FormAttachment(0, 10);
		fd_comboUser.top = new FormAttachment(labelUser, 6);
		comboUser.setLayoutData(fd_comboUser);
		
		Button buttonCreateUser = new Button(compositeUserSpecific, SWT.NONE);
		fd_comboUser.right = new FormAttachment(buttonCreateUser, -6);
		buttonCreateUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogNewUser d = new DialogNewUser(shell, SWT.SHEET);
				String username = (String) d.open();
				if(username != null && !username.isEmpty())
				{					
					FileHandler.createNewUser(mod, username);
					comboUser.setItems(FileHandler.getListOfUsers(mod));
				}
			}
		});
		buttonCreateUser.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_buttonCreateUser = new FormData();
		fd_buttonCreateUser.top = new FormAttachment(comboUser, -1, SWT.TOP);
		buttonCreateUser.setLayoutData(fd_buttonCreateUser);
		buttonCreateUser.setText("Create user");
		
		Group groupUserSettings = new Group(compositeUserSpecific, SWT.NONE);
		fd_buttonCreateUser.right = new FormAttachment(groupUserSettings, 0, SWT.RIGHT);
		groupUserSettings.setText("Debug settings");
		groupUserSettings.setLayout(new FormLayout());
		FormData fd_groupUserSettings = new FormData();
		fd_groupUserSettings.bottom = new FormAttachment(100, -10);
		fd_groupUserSettings.top = new FormAttachment(comboUser, 5);
		fd_groupUserSettings.left = new FormAttachment(0, 10);
		fd_groupUserSettings.right = new FormAttachment(100, -10);
		groupUserSettings.setLayoutData(fd_groupUserSettings);
		
		checkShowFps = new Button(groupUserSettings, SWT.CHECK);
		FormData fd_checkShowFps = new FormData();
		fd_checkShowFps.top = new FormAttachment(0, 10);
		fd_checkShowFps.left = new FormAttachment(0, 10);
		checkShowFps.setLayoutData(fd_checkShowFps);
		checkShowFps.setText("Show FPS");
		
		checkShowDebug = new Button(groupUserSettings, SWT.CHECK);
		FormData fd_checkShowDebug = new FormData();
		fd_checkShowDebug.top = new FormAttachment(checkShowFps, 6);
		fd_checkShowDebug.left = new FormAttachment(checkShowFps, 0, SWT.LEFT);
		checkShowDebug.setLayoutData(fd_checkShowDebug);
		checkShowDebug.setText("Show debug messages");
		
		checkShowErrors = new Button(groupUserSettings, SWT.CHECK);
		FormData fd_checkShowErrors = new FormData();
		fd_checkShowErrors.top = new FormAttachment(checkShowDebug, 6);
		fd_checkShowErrors.left = new FormAttachment(checkShowFps, 0, SWT.LEFT);
		checkShowErrors.setLayoutData(fd_checkShowErrors);
		checkShowErrors.setText("Show errors and warnings");
		
		checkEnableScript = new Button(groupUserSettings, SWT.CHECK);
		FormData fd_checkEnableScript = new FormData();
		fd_checkEnableScript.top = new FormAttachment(checkShowErrors, 6);
		fd_checkEnableScript.left = new FormAttachment(checkShowFps, 0, SWT.LEFT);
		checkEnableScript.setLayoutData(fd_checkEnableScript);
		checkEnableScript.setText("Enable script debug mode");
		
		checkEnableQuickSave = new Button(groupUserSettings, SWT.CHECK);
		FormData fd_checkEnableQuickSave = new FormData();
		fd_checkEnableQuickSave.top = new FormAttachment(checkEnableScript, 6);
		fd_checkEnableQuickSave.left = new FormAttachment(checkShowFps, 0, SWT.LEFT);
		checkEnableQuickSave.setLayoutData(fd_checkEnableQuickSave);
		checkEnableQuickSave.setText("Enable quick saving/loading (F4/F5)");
		scUserSpecific.setContent(compositeUserSpecific);
		scUserSpecific.setMinSize(compositeUserSpecific.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		FormData fd_buttonCancel = new FormData();
		fd_buttonCancel.left = new FormAttachment(100, -90);
		fd_buttonCancel.right = new FormAttachment(100, -10);
		fd_buttonCancel.bottom = new FormAttachment(100, -10);
		buttonCancel.setLayoutData(fd_buttonCancel);
		buttonCancel.setText("Cancel");
		
		Button buttonOk = new Button(shell, SWT.NONE);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveSettings();
				shell.close();
			}
		});
		buttonOk.setText("OK");
		FormData fd_buttonOk = new FormData();
		fd_buttonOk.bottom = new FormAttachment(buttonCancel, 0, SWT.BOTTOM);
		fd_buttonOk.left = new FormAttachment(buttonCancel, -86, SWT.LEFT);
		fd_buttonOk.right = new FormAttachment(buttonCancel, -6);
		buttonOk.setLayoutData(fd_buttonOk);

		shell.setDefaultButton(buttonOk);
		
		comboSoundDevice.setItems(Modloader.getSystemAudioOutputs());
		comboSoundDevice.setItem(0, comboSoundDevice.getItem(0) + " - (default)");
		comboSoundDevice.select(0);
		loadResolutions();

		if(mod != null)
		{			
			comboUser.setItems(FileHandler.getListOfUsers(mod));
		}
		else
		{
			BalloonMessage b = new BalloonMessage(compositeUserSpecific, SWT.ICON_INFORMATION);
			b.setText("These settings can only be edited specifically for each mod.");
			FormData fd_b = new FormData();
			fd_b.top = new FormAttachment(0, 10);
			fd_b.left = new FormAttachment(0, 10);
			fd_b.right = new FormAttachment(100, -10);
			fd_b.bottom = new FormAttachment(0, 66);
			b.setLayoutData(fd_b);
			fd_labelUser.top = new FormAttachment(b, 10, SWT.BOTTOM);
			comboResolution.select(1);
			comboLanguage.add("Default");
			comboLanguage.select(0);
			comboLanguage.setEnabled(false);
			comboUser.setEnabled(false);
			checkEnableQuickSave.setEnabled(false);
			checkEnableScript.setEnabled(false);
			checkShowDebug.setEnabled(false);
			checkShowErrors.setEnabled(false);
			checkShowFps.setEnabled(false);
			buttonCreateUser.setEnabled(false);
		}

		loadSettings();
	}
	
	private void loadResolutions()
	{
		Point nativeRes = Modloader.getSystemNativeResolution();
		resolutions.add(new Point(640, 480));
		comboResolution.add("640x480");
		resolutions.add(new Point(800, 600));
		comboResolution.add("800x600");
		resolutions.add(nativeRes);
		comboResolution.add(resolutions.get(2).x + "x" + resolutions.get(2).y + " (native)");
	}
	
	private void loadSettings()
	{
		Document mainSettings;
		
		if(mod == null)
		{
			mainSettings = FileHandler.getDefaultSettingsFile();
		}
		else
		{
			Log.info("Loading mod settings for: " + mod.getAbsoluteStartFilePath());
			langs = FileHandler.getLanguages(mod);
			langKeys = new String[langs.getLangs().size()];
			int langKeyCount = 0;
			
			for(String key : langs.getLangs().keySet())
			{
				langKeys[langKeyCount++] = key;
				String value = langs.getLangs().get(key);
				comboLanguage.add(value);
				if(key.equals(langs.getDefaultLang())) comboLanguage.select(comboLanguage.getItemCount() - 1);
			}

			mainSettings = FileHandler.getMainSettingsDocument(mod);
		}
		
		if(mainSettings != null)
		{
			Element e;
			if((e = mainSettings.getChild("Main")) != null)
			{
				checkSaveConfig.setSelection(toBool(e, "SaveConfig", true));
				checkLoadDebug.setSelection(toBool(e, "LoadDebugMenu", false));
				if(!toBool(e, "ForceCacheLoadingAndSkipSaving", true))
				{
					radioLoadCache.setSelection(false);
					radioWriteCache.setSelection(true);
				}
				checkShowPremenu.setSelection(toBool(e, "ShowPreMenu", true));
				
				if(mod != null)
				{
					String lang = e.getAttribute("StartLanguage");
					for(int i = 0; i < langKeys.length; i++)
					{
						if((langKeys[i] + ".lang").equals(lang))
						{
							Log.info("Selecting %s as default start language.", langKeys[i]);
							comboLanguage.select(i);
						}
					}
				}
			}
			
			if((e = mainSettings.getChild("Graphics")) != null)
			{
				comboTextureQuality.select(new int[] {2, 1, 0}[toInt(e, "TextureQuality", 2, 2)]);
				comboTextureFilter.select(toInt(e, "TextureFilter", 0, comboTextureFilter.getItemCount()));
				int[] anis = new int[] {1, 2, 4, 8, 16};
				for(int i = 0; i < anis.length; i++) 
					if((int) toFloat(e, "TextureAnisotropy", 1, 16) == anis[i]) 
						comboAnisotropicFiltering.select(i);
				spinnerGamma.setSelection((int) (toFloat(e, "Gamma", 1.2f, 2.0f) * 10));
				checkSsao.setSelection(toBool(e, "SSAOActive", true));
				String[] items = comboSsaoSamples.getItems();
				for(int i = 0; i < items.length; i++) if(items[i].equals(e.getAttribute("SSAOSamples"))) comboSsaoSamples.select(i);
				comboSsaoResolution.select(toInt(e, "SSAOResolution", 0, 1));
				checkWorldReflection.setSelection(toBool(e, "WorldReflection", true));
				checkRefraction.setSelection(toBool(e, "Refraction", true));
				checkEnableShadows.setSelection(toBool(e, "ShadowsActive", true));
				comboShadowQuality.select(toInt(e, "ShadowQuality", 0, 2));
				comboShadowResolution.select(toInt(e, "ShadowResolution", 0, 2));
				checkParallax.setSelection(toBool(e, "ParallaxEnabled", false));
				checkEdgeSmoothing.setSelection(toBool(e, "EdgeSmooth", false));
				checkBloom.setSelection(toBool(e, "PostEffectBloom", true));
				checkImageTrail.setSelection(toBool(e, "PostEffectImageTrail", true));
				checkSepia.setSelection(toBool(e, "PostEffectSepia", true));
				checkRadialBlur.setSelection(toBool(e, "PostEffectRadialBlur", true));
				checkInsanity.setSelection(toBool(e, "PostEffectInsanity", true));
			}
			
			if((e = mainSettings.getChild("Engine")) != null)
			{
				checkLimitFps.setSelection(toBool(e, "LimitFPS", true));
				checkSleep.setSelection(toBool(e, "SleepWhenOutOfFocus", true));
			}
			
			if((e = mainSettings.getChild("Screen")) != null)
			{
				checkVsync.setSelection(toBool(e, "Vsync", false));
				checkFullscreen.setSelection(toBool(e, "FullScreen", false));
				try
				{
					int x = Integer.parseInt(e.getAttribute("Width"));
					int y = Integer.parseInt(e.getAttribute("Height"));
					Point p = new Point(x, y);
					boolean exists = false;
					for(int i = 0; i < resolutions.size(); i++)
					{
						if(p.equals(resolutions.get(i)))
						{
							exists = true;
							comboResolution.select(i);
						}
					}
					if(!exists)
					{						
						comboResolution.add(p.x + "x" + p.y + " (custom)");
						comboResolution.select(comboResolution.getItemCount() - 1);
					}
				}
				catch(NumberFormatException ex) {}
			}
			
			if((e = mainSettings.getChild("MapLoad")) != null)
			{
				checkLoadFastStatic.setSelection(toBool(e, "FastStaticLoad", false));
				checkLoadFastPhysics.setSelection(toBool(e, "FastPhysicsLoad", false));
				checkLoadFastEntities.setSelection(toBool(e, "FastEntityLoad", false));
			}
			
			if((e = mainSettings.getChild("Sound")) != null)
			{
				spinnerMaxChannels.setSelection(toInt(e, "MaxChannels", 32, 128));
				spinnerStreamBuffers.setSelection(toInt(e, "StreamBuffers", 4, 16));
				spinnerStreamBufferSize.setSelection(toInt(e, "StreamBufferSize", 262144, 262144 * 4));
				comboSoundDevice.select(toInt(e, "Device", 0, comboSoundDevice.getItemCount()));
				spinnerVolume.setSelection((int) (toFloat(e, "Volume", 1.0f, 1.0f) * 100));
			}
		}
	}
	
	private void saveSettings()
	{
		XMLParser.debug = true;
		Document mainSettings = null;
		if(mod == null)
		{
			Log.info("Saving default settings file.");
		}
		else
		{
			Log.info("Writing main settings file for: " + mod.getAbsoluteStartFilePath());
			mainSettings = FileHandler.getMainSettingsDocument(mod);
		}

		if(mainSettings == null)
		{
			mainSettings = new Document();
			mainSettings.addChild(new Element("Main"));
			mainSettings.addChild(new Element("Graphics"));
			mainSettings.addChild(new Element("Engine"));
			mainSettings.addChild(new Element("Screen"));
//			mainSettings.addChild(new Element("Physics"));
			mainSettings.addChild(new Element("MapLoad"));
			mainSettings.addChild(new Element("Sound"));
			for(Element ee : mainSettings.getChildren()) System.out.println(ee);
		}
		
		Element e;
		if((e = mainSettings.getChild("Main")) != null)
		{
			e.addAttribute("SaveConfig", ""+checkSaveConfig.getSelection());
			e.addAttribute("LoadDebugMenu", ""+checkLoadDebug.getSelection());
			e.addAttribute("ForceCacheLoadingAndSkipSaving", ""+radioLoadCache.getSelection());
			e.addAttribute("ShowPreMenu", ""+checkShowPremenu.getSelection());
			if(mod != null && comboLanguage.getSelectionIndex() != -1)
				e.addAttribute("StartLanguage", langKeys[comboLanguage.getSelectionIndex()] + ".lang");
		}
		
		if((e = mainSettings.getChild("Graphics")) != null)
		{
			e.addAttribute("TextureQuality", ""+new int[] {2, 1, 0}[comboTextureQuality.getSelectionIndex()]);
			e.addAttribute("TextureFilter", ""+comboTextureFilter.getSelectionIndex());
			e.addAttribute("TextureAnisotropy", ""+ Math.pow(2, comboAnisotropicFiltering.getSelectionIndex()));
			e.addAttribute("Gamma", ""+ spinnerGamma.getSelection() / 10.0f);
			e.addAttribute("SSAOActive", ""+checkSsao.getSelection());
			e.addAttribute("SSAOSamples", ""+comboSsaoSamples.getText());
			e.addAttribute("SSAOResolution", ""+comboSsaoResolution.getSelectionIndex());
			e.addAttribute("WorldReflection", ""+checkWorldReflection.getSelection());
			e.addAttribute("Refraction", ""+checkRefraction.getSelection());
			e.addAttribute("ShadowsActive", ""+checkEnableShadows.getSelection());
			e.addAttribute("ShadowQuality", ""+comboShadowQuality.getSelectionIndex());
			e.addAttribute("ShadowResolution", ""+comboShadowResolution.getSelectionIndex());
			e.addAttribute("ParallaxEnabled", ""+checkParallax.getSelection());
			e.addAttribute("EdgeSmooth", ""+checkEdgeSmoothing.getSelection());
			e.addAttribute("PostEffectBloom", ""+checkBloom.getSelection());
			e.addAttribute("PostEffectImageTrail", ""+checkImageTrail.getSelection());
			e.addAttribute("PostEffectSepia", ""+checkSepia.getSelection());
			e.addAttribute("PostEffectRadialBlur", ""+checkRadialBlur.getSelection());
			e.addAttribute("PostEffectInsanity", ""+checkInsanity.getSelection());
		}
		
		if((e = mainSettings.getChild("Engine")) != null)
		{
			e.addAttribute("LimitFPS", ""+checkLimitFps.getSelection());
			e.addAttribute("SleepWhenOutOfFocus", ""+checkSleep.getSelection());
		}
		
		if((e = mainSettings.getChild("Screen")) != null)
		{
			Point selectedRes = resolutions.get(comboResolution.getSelectionIndex());
			e.addAttribute("Width", ""+selectedRes.x);
			e.addAttribute("Height", ""+selectedRes.y);
			e.addAttribute("FullScreen", ""+checkFullscreen.getSelection());
			e.addAttribute("Vsync", ""+checkVsync.getSelection());
		}
		
		if((e = mainSettings.getChild("MapLoad")) != null)
		{
			e.addAttribute("FastPhysicsLoad", ""+checkLoadFastPhysics.getSelection());
			e.addAttribute("FastStaticLoad", ""+checkLoadFastStatic.getSelection());
			e.addAttribute("FastEntityLoad", ""+checkLoadFastEntities.getSelection());
		}
		
		if((e = mainSettings.getChild("Sound")) != null)
		{
			e.addAttribute("Device", ""+comboSoundDevice.getSelectionIndex());
			e.addAttribute("Volume", ""+((float) spinnerVolume.getSelection() / 100));
			e.addAttribute("MaxChannels", ""+spinnerMaxChannels.getSelection());
			e.addAttribute("StreamBuffers", ""+spinnerStreamBuffers.getSelection());
			e.addAttribute("StreamBufferSize", ""+spinnerStreamBufferSize.getSelection());
		}
		
		if(mod == null)
		{
			for(Element ee : mainSettings.getChildren()) System.out.println(ee);
			FileHandler.writeDefaultSettingsFile(mainSettings);
		}
		else
		{
			FileHandler.writeMainSettingsDocument(mod, mainSettings);
			saveUserSettings();
		}
		XMLParser.debug = false;
	}
		
	private void loadSettingsForUser(String username)
	{
		Log.info("Loading settings for user: " + username);
		Map<String, Boolean> settings = FileHandler.loadUserSettings(mod, username);
		checkShowFps.setSelection(settings.containsKey("ShowFPS") ? settings.get("ShowFPS") : false);
		checkShowDebug.setSelection(settings.containsKey("ShowDebugMessages") ? settings.get("ShowDebugMessages") : false);
		checkShowErrors.setSelection(settings.containsKey("ShowErrorsAndWarnings") ? settings.get("ShowErrorsAndWarnings") : false);
		checkEnableScript.setSelection(settings.containsKey("ScriptDebugOn") ? settings.get("ScriptDebugOn") : false);
		checkEnableQuickSave.setSelection(settings.containsKey("AllowQuickSave") ? settings.get("AllowQuickSave") : false);
	}
	
	private void saveUserSettings()
	{
		String username = comboUser.getText();
		Document d = FileHandler.getUserSettingsDocument(mod, username);
		if(d == null)
		{
			d = new Document();
			d.addChild(new Element("Debug"));
		}
		
		Element e;
		if((e = d.getChild("Debug")) != null)
		{
			e.addAttribute("ShowFPS", ""+checkShowFps.getSelection());
			e.addAttribute("ShowDebugMessages", ""+checkShowDebug.getSelection());
			e.addAttribute("ShowErrorsAndWarnings", ""+checkShowErrors.getSelection());
			e.addAttribute("ScriptDebugOn", ""+checkEnableScript.getSelection());
			e.addAttribute("AllowQuickSave", ""+checkEnableQuickSave.getSelection());
		}
		
		FileHandler.writeUserSettingsDocument(mod, username, d);
	}
	
	private float toFloat(Element e, String attribName, float def, float max) {
		if(e.hasAttribute(attribName)) {
			try {
				return Math.min(max, Float.parseFloat(e.getAttribute(attribName)));
			} catch(Exception ex) {}
		}
		return def;
	}
	
	private int toInt(Element e, String attribName, int def, int max) {
		if(e.hasAttribute(attribName)) {
			try {
				return Math.min(max, Integer.parseInt(e.getAttribute(attribName)));
			} catch(Exception ex) {}
		}
		return def;
	}
	
	private boolean toBool(Element e, String attribName, boolean def) {
		if(e.hasAttribute(attribName)) {
			try {
				return Boolean.parseBoolean(e.getAttribute(attribName));
			} catch(Exception ex) {}
		}
		return def;
	}
}
