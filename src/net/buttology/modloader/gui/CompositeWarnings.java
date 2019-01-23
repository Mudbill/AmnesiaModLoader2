package net.buttology.modloader.gui;

import net.buttology.modloader.file.UserSettings;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormAttachment;

public class CompositeWarnings extends Composite {
	private Button checkWarnExec;
	private Button checkWarnShaders;
	private Button checkWarnSteam;
	private Button checkWarnAmnesiaVersion;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeWarnings(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Group group = new Group(this, SWT.NONE);
		group.setText("Warnings");
		group.setLayout(new FormLayout());
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(100, -10);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		fd_group.right = new FormAttachment(100, -10);
		group.setLayoutData(fd_group);
		
		Label label = new Label(group, SWT.NONE);
		label.setText("Display a warning when:");
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 10);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		
		checkWarnExec = new Button(group, SWT.CHECK);
		checkWarnExec.setText("A mod wants to start a custom executable.");
		checkWarnExec.setSelection(true);
		FormData fd_checkWarnExec = new FormData();
		checkWarnExec.setLayoutData(fd_checkWarnExec);
		
		checkWarnShaders = new Button(group, SWT.CHECK);
		checkWarnShaders.setEnabled(false);
		fd_checkWarnExec.bottom = new FormAttachment(checkWarnShaders, -6);
		fd_checkWarnExec.left = new FormAttachment(checkWarnShaders, 0, SWT.LEFT);
		checkWarnShaders.setText("The modloader is about to install custom shaders (if enabled).");
		checkWarnShaders.setSelection(true);
		FormData fd_checkWarnShaders = new FormData();
		fd_checkWarnShaders.top = new FormAttachment(0, 62);
		fd_checkWarnShaders.left = new FormAttachment(0, 20);
		checkWarnShaders.setLayoutData(fd_checkWarnShaders);
		
		checkWarnSteam = new Button(group, SWT.CHECK);
		checkWarnSteam.setText("Launching a mod through the Steam protocol (if enabled) which will trigger a pop-up message.");
		checkWarnSteam.setSelection(true);
		FormData fd_checkWarnSteam = new FormData();
		fd_checkWarnSteam.top = new FormAttachment(checkWarnShaders, 6);
		fd_checkWarnSteam.left = new FormAttachment(checkWarnExec, 0, SWT.LEFT);
		checkWarnSteam.setLayoutData(fd_checkWarnSteam);
		
		checkWarnAmnesiaVersion = new Button(group, SWT.CHECK);
		checkWarnAmnesiaVersion.setSelection(true);
		FormData fd_checkWarnAmnesiaVersion = new FormData();
		fd_checkWarnAmnesiaVersion.top = new FormAttachment(checkWarnSteam, 6);
		fd_checkWarnAmnesiaVersion.left = new FormAttachment(checkWarnExec, 0, SWT.LEFT);
		checkWarnAmnesiaVersion.setLayoutData(fd_checkWarnAmnesiaVersion);
		checkWarnAmnesiaVersion.setText("Amnesia's version appears lower than the mod's specified requirement.");

		loadUserSettings();
	}
	
	private void loadUserSettings() {
		if(UserSettings.isSet("WarnExec"))
			checkWarnExec.setSelection(toBool("WarnExec"));
		if(UserSettings.isSet("WarnShader"))
			checkWarnShaders.setSelection(toBool("WarnShader"));
		if(UserSettings.isSet("WarnSteam"))
			checkWarnSteam.setSelection(toBool("WarnSteam"));
		if(UserSettings.isSet("WarnAmnesiaVersion"))
			checkWarnAmnesiaVersion.setSelection(toBool("WarnAmnesiaVersion"));
	}
	
	private boolean toBool(String value) {
		String val = UserSettings.getVar(value);
		if(val.isEmpty()) return false;
		return Boolean.parseBoolean(val);
	}

	public void saveSettings() {
		UserSettings.setVar("WarnExec", checkWarnExec.getSelection());
		UserSettings.setVar("WarnShader", checkWarnShaders.getSelection());
		UserSettings.setVar("WarnSteam", checkWarnSteam.getSelection());
		UserSettings.setVar("WarnAmnesiaVersion", checkWarnAmnesiaVersion.getSelection());
	}
}
