package net.buttology.modloader.gui;

import net.buttology.modloader.Mod;

import org.eclipse.swt.widgets.Shell;

public interface IShellPrimary {

	public void init();
	public void open();
	public void close();
	public boolean isDisposed();
	public void loadUserSettings();
	public void setWelcomeEnabled(boolean b);
	public void addLoadedModToTable(Mod mod);
	public void setModInfo(Mod mod);
	public void setStartButtonEnabled(boolean x);
	public void incrementProgressBarStage();
	public void setProgressBarMax(int x);
	public void setProgressBarVisible(boolean x);
	public void resetProgressBar();
	public CompositeModList getModList();
	public void launch(Mod mod);
	public Shell getShell();
	public void search();
	
}
