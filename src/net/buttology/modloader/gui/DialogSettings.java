package net.buttology.modloader.gui;

import net.buttology.modloader.Modloader;
import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.util.Log;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;

public class DialogSettings extends Dialog {

	protected Object result;
	protected Shell shell;
	private Button buttonSave;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogSettings(Shell parent, int style) {
		super(parent, style);
		createContents(parent, style);
	}
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogSettings(int style) {
		super(new Shell(), style);
		createContents(null, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		Log.info("Opening Settings GUI.");
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		Log.info("Closing Settings GUI.");
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(Shell parent, int style) {
		if(parent == null)
			shell = new Shell(style);
		else
			shell = new Shell(parent, style);
		shell.setImage(ResourceManager.loadInternalImage("/res/icons/cogwheel.png"));
		shell.setSize(635, 482);
		shell.setMinimumSize(400, 430);
		shell.setText("Settings");
		shell.setLayout(new FormLayout());
		Util.centerShellOnParent(shell);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.left = new FormAttachment(0, 10);
		fd_tabFolder.right = new FormAttachment(100, -10);
		fd_tabFolder.top = new FormAttachment(0, 10);
		tabFolder.setLayoutData(fd_tabFolder);
		
		Button buttonCancel = new Button(shell, SWT.NONE);
		fd_tabFolder.bottom = new FormAttachment(buttonCancel, -6);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		TabItem tabSettings = new TabItem(tabFolder, SWT.NONE);
		tabSettings.setText("Setup");		
		
		ScrolledComposite tabSettingsPane = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabSettings.setControl(tabSettingsPane);
		tabSettingsPane.setExpandHorizontal(true);
		tabSettingsPane.setExpandVertical(true);
		
		CompositeSettings compositeSettings = new CompositeSettings(tabSettingsPane, SWT.NONE);
		tabSettingsPane.setContent(compositeSettings);
		tabSettingsPane.setMinSize(compositeSettings.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		TabItem tabPreferences = new TabItem(tabFolder, SWT.NONE);
		tabPreferences.setText("Preferences");
		
		ScrolledComposite tabPreferencesPane = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabPreferences.setControl(tabPreferencesPane);
		tabPreferencesPane.setExpandHorizontal(true);
		tabPreferencesPane.setExpandVertical(true);
		
		CompositePreferences compositePreferences = new CompositePreferences(tabPreferencesPane, SWT.NONE);
		tabPreferencesPane.setContent(compositePreferences);
		tabPreferencesPane.setMinSize(compositePreferences.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		TabItem tabDefaults = new TabItem(tabFolder, SWT.NONE);
		tabDefaults.setText("Game defaults");
		
		ScrolledComposite tabDefaultsPane = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabDefaults.setControl(tabDefaultsPane);
		tabDefaultsPane.setExpandHorizontal(true);
		tabDefaultsPane.setExpandVertical(true);
		
		Composite compositeDefaults = new Composite(tabDefaultsPane, SWT.NONE);
		compositeDefaults.setLayout(new FormLayout());
		
		Button checkDefaults = new Button(compositeDefaults, SWT.CHECK);
		FormData fd_checkDefaults = new FormData();
		fd_checkDefaults.top = new FormAttachment(0, 10);
		fd_checkDefaults.left = new FormAttachment(0, 10);
		checkDefaults.setLayoutData(fd_checkDefaults);
		checkDefaults.setText("Apply a specific set of settings to all new mods");
		checkDefaults.setSelection(UserSettings.getBool("UseDefaults"));
		
		Button buttonDefaults = new Button(compositeDefaults, SWT.NONE);
		buttonDefaults.setEnabled(UserSettings.getBool("UseDefaults"));
		buttonDefaults.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_buttonDefaults = new FormData();
		fd_buttonDefaults.top = new FormAttachment(checkDefaults, 6);
		fd_buttonDefaults.left = new FormAttachment(checkDefaults, 0, SWT.LEFT);
		buttonDefaults.setLayoutData(fd_buttonDefaults);
		buttonDefaults.setText("Click here to specify settings");
		buttonDefaults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogEditSettings d = new DialogEditSettings(shell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL, null);
				d.open();
			}
		});
		tabDefaultsPane.setContent(compositeDefaults);
		tabDefaultsPane.setMinSize(compositeDefaults.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		checkDefaults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonDefaults.setEnabled(checkDefaults.getSelection());
			}
		});
		
//		TabItem tabGameDefaults = new TabItem(tabFolder, SWT.NONE);
//		tabGameDefaults.setText("Game defaults");
//		
//		ScrolledComposite tabGameDefaultsPane = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
//		tabGameDefaults.setControl(tabGameDefaultsPane);
//		tabGameDefaultsPane.setExpandHorizontal(true);
//		tabGameDefaultsPane.setExpandVertical(true);
//		
//		CompositeDefaults compositeDefaults = new CompositeDefaults(tabGameDefaultsPane, SWT.NONE);
//		tabGameDefaultsPane.setContent(compositeDefaults);
//		tabGameDefaultsPane.setMinSize(compositeDefaults.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		TabItem tabWarnings = new TabItem(tabFolder, SWT.NONE);
		tabWarnings.setText("Warnings");
		
		ScrolledComposite tabWarningsPane = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tabWarnings.setControl(tabWarningsPane);
		tabWarningsPane.setExpandHorizontal(true);
		tabWarningsPane.setExpandVertical(true);
		
		CompositeWarnings compositeWarnings = new CompositeWarnings(tabWarningsPane, SWT.NONE);
		tabWarningsPane.setContent(compositeWarnings);
		tabWarningsPane.setMinSize(compositeWarnings.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
						
		FormData fd_buttonCancel = new FormData();
		fd_buttonCancel.bottom = new FormAttachment(100, -10);
		fd_buttonCancel.left = new FormAttachment(100, -90);
		fd_buttonCancel.right = new FormAttachment(100, -10);
		buttonCancel.setLayoutData(fd_buttonCancel);
		buttonCancel.setText("Cancel");
		
		buttonSave = new Button(shell, SWT.NONE);
		FormData fd_buttonSave = new FormData();
		fd_buttonSave.left = new FormAttachment(buttonCancel, -86, SWT.LEFT);
		fd_buttonSave.bottom = new FormAttachment(100, -10);
		fd_buttonSave.right = new FormAttachment(buttonCancel, -6);
		buttonSave.setLayoutData(fd_buttonSave);
		buttonSave.setText("OK");
		buttonSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compositeSettings.saveSettings();
//				compositeDefaults.saveSettings();
				compositePreferences.saveSettings();
				compositeWarnings.saveSettings();
				UserSettings.setVar("UseDefaults", checkDefaults.getSelection());
				
				UserSettings.saveToFile(Modloader.getAppSettings().getConfigFileName());
				shell.close();
				Modloader.getForm().loadUserSettings();
			}
		});
		
		shell.setDefaultButton(buttonSave);
	}

}
