package net.buttology.modloader.gui;

import java.util.Arrays;

import net.buttology.modloader.Modloader;
import net.buttology.modloader.UpdateCallback;
import net.buttology.modloader.file.AppSettings;
import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.util.Log;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.wb.swt.SWTResourceManager;

public class CompositePreferences extends Composite {
	private Button checkUseSteam;
	private Button checkAutoUpdate;
	private Button checkCache;
	private Button checkUpdateList;
	private Spinner spinnerIconSize;
	private Button checkCustomShaders;
	private Button checkApplyPatch;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositePreferences(Composite parent, int style) {
		super(parent, style);
		Shell shell = getShell();
		setLayout(new FormLayout());
		
		Group groupModList = new Group(this, SWT.NONE);
		groupModList.setText("Mod list");
		groupModList.setLayout(new FormLayout());
		FormData fd_groupModList = new FormData();
		fd_groupModList.right = new FormAttachment(100, -10);
		groupModList.setLayoutData(fd_groupModList);
		
		checkCache = new Button(groupModList, SWT.CHECK);
		checkCache.setSelection(true);
		checkCache.setToolTipText("Saves the last list to a cache and restores it upon next launch of the modloader, so that you can quickly get started. If new mods are added, the cache needs to be updated.");
		FormData fd_checkCache = new FormData();
		fd_checkCache.top = new FormAttachment(0, 10);
		fd_checkCache.left = new FormAttachment(0, 10);
		checkCache.setLayoutData(fd_checkCache);
		checkCache.setText("Save a cache of the last mod list fetched");
		
		checkUpdateList = new Button(groupModList, SWT.CHECK);
		checkUpdateList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkCache.setEnabled(!checkUpdateList.getSelection());
				if(checkUpdateList.getSelection()) {
					checkCache.setSelection(false);
				}
			}
		});
		checkUpdateList.setToolTipText("Automatically starts searching for mods when the modloader starts, so that the list is always up-to-date. Makes startup slower but more accurate.");
		FormData fd_checkUpdateList = new FormData();
		fd_checkUpdateList.top = new FormAttachment(checkCache, 6);
		fd_checkUpdateList.left = new FormAttachment(checkCache, 0, SWT.LEFT);
		checkUpdateList.setLayoutData(fd_checkUpdateList);
		checkUpdateList.setText("Update the mod list automatically upon modloader launch");
		
		spinnerIconSize = new Spinner(groupModList, SWT.BORDER);
		spinnerIconSize.setToolTipText("Specify how large you want the icons in the mod list to appear, from 16x16 to 64x64.");
		spinnerIconSize.setMaximum(64);
		spinnerIconSize.setMinimum(16);
		spinnerIconSize.setSelection(48);
		FormData fd_spinnerIconSize = new FormData();
		fd_spinnerIconSize.right = new FormAttachment(100, -10);
		fd_spinnerIconSize.left = new FormAttachment(100, -100);
		spinnerIconSize.setLayoutData(fd_spinnerIconSize);
		
		Label labelIconSize = new Label(groupModList, SWT.NONE);
		fd_spinnerIconSize.top = new FormAttachment(labelIconSize, -3, SWT.TOP);
		FormData fd_labelIconSize = new FormData();
		fd_labelIconSize.top = new FormAttachment(checkUpdateList, 6);
		fd_labelIconSize.left = new FormAttachment(checkCache, 0, SWT.LEFT);
		labelIconSize.setLayoutData(fd_labelIconSize);
		labelIconSize.setText("Mod list icon size");
		
		Group groupGeneral = new Group(this, SWT.NONE);
		fd_groupModList.top = new FormAttachment(groupGeneral, 6);
		fd_groupModList.left = new FormAttachment(groupGeneral, 0, SWT.LEFT);
		groupGeneral.setText("General");
		groupGeneral.setLayout(new FormLayout());
		FormData fd_groupGeneral = new FormData();
		fd_groupGeneral.top = new FormAttachment(0, 10);
		fd_groupGeneral.left = new FormAttachment(0, 10);
		fd_groupGeneral.bottom = new FormAttachment(0, 119);
		fd_groupGeneral.right = new FormAttachment(100, -10);
		groupGeneral.setLayoutData(fd_groupGeneral);
		
		checkUseSteam = new Button(groupGeneral, SWT.CHECK);
		FormData fd_checkUseSteam = new FormData();
		fd_checkUseSteam.top = new FormAttachment(0, 10);
		fd_checkUseSteam.left = new FormAttachment(0, 10);
		checkUseSteam.setLayoutData(fd_checkUseSteam);
		checkUseSteam.setToolTipText("If your game is installed through Steam, you can use the Steam protocol to start mods. This spawns a Steam pop-up message before launch confirming the action.");
		checkUseSteam.setText("Use the Steam protocol to start mods");
		
		checkAutoUpdate = new Button(groupGeneral, SWT.CHECK);
		checkAutoUpdate.setToolTipText("This will automatically check online if there is a new version of Amnesia Modloader available.");
		FormData fd_checkAutoUpdate = new FormData();
		fd_checkAutoUpdate.left = new FormAttachment(0, 10);
		fd_checkAutoUpdate.top = new FormAttachment(checkUseSteam, 6);
		checkAutoUpdate.setLayoutData(fd_checkAutoUpdate);
		checkAutoUpdate.setText("Automatically check for updates to the modloader");
		
		Link linkUpdateInfo = new Link(groupGeneral, SWT.NONE);
		linkUpdateInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
				m.setText("Privacy Statement");
				m.setMessage("This function connects to my website and checks a version number and compares it. "
						+ "I do not intentionally collect any information from this, it is simply for convenience, however the website will see your IP address "
						+ "because that's how the Internet works. Feel free to disable this if you do not want that.");
				m.open();
			}
		});
		FormData fd_linkUpdateInfo = new FormData();
		fd_linkUpdateInfo.bottom = new FormAttachment(checkAutoUpdate, 0, SWT.BOTTOM);
		fd_linkUpdateInfo.right = new FormAttachment(100, -10);
		linkUpdateInfo.setLayoutData(fd_linkUpdateInfo);
		linkUpdateInfo.setText("<a>Info</a>");
		
		Button buttonCheckUpdate = new Button(groupGeneral, SWT.NONE);
		buttonCheckUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonCheckUpdate.setEnabled(false);
				Modloader.checkForUpdates(new UpdateCallback() {
					@Override
					public void updated() {
						MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
						m.setText("Update");
						m.setMessage("Already up-to-date with the latest version: " + Arrays.toString(Modloader.APP_VERSION_ARRAY));
						m.open();
						buttonCheckUpdate.setEnabled(true);
					}

					@Override
					public void outdated() {
						MessageBox m = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						m.setText("Update available");
						m.setMessage("There's an update available for Amnesia Modloader.\n"
								+ "Current version: " + Arrays.toString(Modloader.APP_VERSION_ARRAY) + "\n"
								+ "Latest version: " + Arrays.toString(Modloader.tempOnlineVersion) + "\n\n"
								+ "Do you want to open the website where you can download the latest version?");
						if(m.open() == SWT.YES)
						{
							Log.info("Opening URL: " + AppSettings.URL_FORUM);
							Program.launch(AppSettings.URL_FORUM);
						}
						buttonCheckUpdate.setEnabled(true);
					}

					@Override
					public void error() {
						MessageBox m = new MessageBox(shell, SWT.ICON_ERROR);
						m.setText("Update");
						m.setMessage("Failed to check for updates.");
						m.open();
						buttonCheckUpdate.setEnabled(true);
					}
				});
			}
		});
		buttonCheckUpdate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_buttonCheckUpdate = new FormData();
		fd_buttonCheckUpdate.left = new FormAttachment(0, 10);
		fd_buttonCheckUpdate.top = new FormAttachment(checkAutoUpdate, 6);
		buttonCheckUpdate.setLayoutData(fd_buttonCheckUpdate);
		buttonCheckUpdate.setText("Check now");
		
		Group groupAdvanced = new Group(this, SWT.NONE);
		fd_groupModList.bottom = new FormAttachment(0, 234);
		groupAdvanced.setText("Advanced");
		groupAdvanced.setLayout(new FormLayout());
		FormData fd_groupAdvanced = new FormData();
		fd_groupAdvanced.bottom = new FormAttachment(100, -15);
		fd_groupAdvanced.top = new FormAttachment(groupModList, 6);
		fd_groupAdvanced.left = new FormAttachment(0, 10);
		fd_groupAdvanced.right = new FormAttachment(100, -10);
		groupAdvanced.setLayoutData(fd_groupAdvanced);
		
		checkCustomShaders = new Button(groupAdvanced, SWT.CHECK);
		checkCustomShaders.setEnabled(false);
		FormData fd_checkCustomShaders = new FormData();
		fd_checkCustomShaders.top = new FormAttachment(0, 10);
		fd_checkCustomShaders.left = new FormAttachment(0, 10);
		checkCustomShaders.setLayoutData(fd_checkCustomShaders);
		checkCustomShaders.setText("Offer to install custom shaders (if available)");
		
		checkApplyPatch = new Button(groupAdvanced, SWT.CHECK);
		FormData fd_checkApplyPatch = new FormData();
		fd_checkApplyPatch.top = new FormAttachment(checkCustomShaders, 6);
		fd_checkApplyPatch.left = new FormAttachment(checkCustomShaders, 0, SWT.LEFT);
		checkApplyPatch.setLayoutData(fd_checkApplyPatch);
		checkApplyPatch.setText("Offer to patch older mods to prevent a warning message about SDL2");
		
		Link linkShaderInfo = new Link(groupAdvanced, SWT.NONE);
		FormData fd_linkShaderInfo = new FormData();
		fd_linkShaderInfo.top = new FormAttachment(checkCustomShaders, 0, SWT.TOP);
		fd_linkShaderInfo.right = new FormAttachment(100, -10);
		linkShaderInfo.setLayoutData(fd_linkShaderInfo);
		linkShaderInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_WARNING);
				m.setText("Custom shaders");
				m.setMessage("This feature is unavailable until it is properly ready.");
//				m.setMessage("It's possible for a mod to supply custom shaders, however these need to explicitly replace the built-in shaders to work. "
//						+ "This feature can backup the default shaders, overwrite them with a mod's shaders, "
//						+ "then revert the shaders back to the originals after shutting down.");
				m.open();
			}
		});
		linkShaderInfo.setText("<a>Info</a>");
		
		loadUserSettings();
	}
	
	private void loadUserSettings()
	{
		checkUseSteam.setSelection(toBool("UseSteam"));
		if(UserSettings.isSet("CheckForUpdates"))
			checkAutoUpdate.setSelection(toBool("CheckForUpdates"));
		if(UserSettings.isSet("UseCache"))
			checkCache.setSelection(toBool("UseCache"));
		if(toBool("UpdateModList")) {
			checkUpdateList.setSelection(true);
			checkCache.setEnabled(false);
		}
		if(UserSettings.isSet("IconSize"))
			spinnerIconSize.setSelection(toInt("IconSize"));
		checkCustomShaders.setSelection(toBool("InstallCustomShaders"));
		checkApplyPatch.setSelection(toBool("ApplyPatch"));
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
	
	public void saveSettings()
	{
		if(UserSettings.isSet("IconSize")) {
			try {
				int i = Integer.parseInt(UserSettings.getVar("IconSize"));
				if(i != spinnerIconSize.getSelection()) {
					MessageBox m = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
					m.setText("Change settings");
					m.setMessage("Changing the icon size requires a restart.");
					m.open();
				}
			} catch(NumberFormatException e) { Log.error("IconSize has an invalid value."); }
		}
//		UserSettings.setVar("MinimizeModloader", checkMinimize.getSelection());
		UserSettings.setVar("UseSteam", checkUseSteam.getSelection());
		UserSettings.setVar("CheckForUpdates", checkAutoUpdate.getSelection());
		UserSettings.setVar("UseCache", checkCache.getSelection());
		UserSettings.setVar("UpdateModList", checkUpdateList.getSelection());
		UserSettings.setVar("IconSize", spinnerIconSize.getSelection());
		UserSettings.setVar("InstallCustomShaders", checkCustomShaders.getSelection());
		UserSettings.setVar("ApplyPatch", checkApplyPatch.getSelection());
	}
}
