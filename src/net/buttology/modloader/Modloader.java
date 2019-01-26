package net.buttology.modloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import net.buttology.modloader.file.AppSettings;
import net.buttology.modloader.file.FileHandler;
import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.gui.DialogChangelog;
import net.buttology.modloader.gui.DialogGameRunning;
import net.buttology.modloader.gui.DialogSettings;
import net.buttology.modloader.gui.IShellPrimary;
import net.buttology.modloader.gui.ShellPrimary;
import net.buttology.modloader.util.EOperatingSystem;
import net.buttology.modloader.util.Log;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;
import net.buttology.util.jeximel.Document;
import net.buttology.util.jeximel.Element;
import net.buttology.util.jeximel.XMLParser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Modloader
{
	public static final String APP_NAME = "Amnesia Modloader";
	public static final String APP_VERSION = "2.0.0";
	public static final int[] APP_VERSION_ARRAY = new int[] {2, 0, 0};
	
	private static final String STEAM_PROTOCOL = "steam://";
	private static final String STEAM_COMMAND = "rungameid";
	private static final int STEAM_GAME_ID = 57300;
		
	private static int[] gameVersion;
	private static EOperatingSystem os;
	private static AppSettings appSettings;
	private static IShellPrimary form;
	private static String[] audioOutputList;
	private static Point resolutions;
	
	public static void init()
	{
		try
		{
			Log.info("Starting %s version %s", APP_NAME, APP_VERSION);
			Log.info("=== Startup initiating ===");
			Log.info("Operating system: %s", System.getProperty("os.name"));
			
			os = Util.getOperatingSystem();
			if(os == EOperatingSystem.WINDOWS)
			{
				form = new ShellPrimary();
			}
			else
			{
				Log.error("Operating system not supported.");
				return;
			}
			
			Log.info("Setting up application settings...");
			appSettings = new AppSettings(os);
			
			Log.info("Locking session...");
			if(!ResourceManager.lockSessionFile(appSettings.getSessionLockFile()))
				return;
			
			Log.info("Loading user settings...");
			if(UserSettings.readFromFile(appSettings.getConfigFileName()) != 0)
			{
				Log.info("Assuming first boot.");
				Start.firstTime = true;
			}
			
			Log.info("Detecing audio outputs...");
			audioOutputList = Util.getAudioOutputList();
			
			Log.info("Detecting screen resolution...");
			resolutions = Util.getNativeResolution();
			
			Log.info("Detecting Amnesia version...");
			gameVersion = FileHandler.getAmnesiaVersionFromLogFile();
			
			
			Log.info("Setting up GUI...");
			form.init();
			
			if(UserSettings.getBool("CheckForUpdates"))
			{
				update();
			}
			
			Log.info("=== Startup complete ===");
			
			if(isPreviousVersionOlder())
			{
				Log.info("Previously ran version is older, opening changelog.");
				DialogChangelog d = new DialogChangelog(SWT.TITLE | SWT.RESIZE | SWT.CLOSE);
				d.open();
			}

			if(Start.firstTime)
			{
				Shell s = new Shell();
				MessageBox m = new MessageBox(s, SWT.ICON_INFORMATION);
				m.setText("Welcome");
				m.setMessage("Thank you for downloading Amnesia Modloader. First we need to set some settings. "
						+ "The only thing required is the Amnesia game directory, which might be automatically detected.");
				m.open();
				DialogSettings d = new DialogSettings(SWT.TITLE | SWT.RESIZE | SWT.MAX);
				d.open();
			}
			
			if("true".equals(UserSettings.getVar("UpdateModList")))
			{
				form.search();
			}
			
			Log.info("Entering GUI loop.");
			form.open();
			shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			promptErrorMessage("Something went wrong! Sorry about that. Maybe the log file can help pinpoint what caused it.\n\n"+e.getMessage());
		}
	}
	
	private static boolean isPreviousVersionOlder()
	{
		int[] current = APP_VERSION_ARRAY;
		int[] previous = Start.getPreviouslyRanModloaderVersion();
		if(previous != null)
		{
			for(int i = 0; i < current.length; i++)
			{
				if(current[i] > previous[i])
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private static void update()
	{
		checkForUpdates(new UpdateCallback() {
			@Override
			public void updated() {}
			@Override
			public void outdated() {
				MessageBox m = new MessageBox(form.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				m.setText("Update available");
				m.setMessage("There's an update available for Amnesia Modloader.\n"
						+ "Current version: " + Arrays.toString(APP_VERSION_ARRAY) + "\n"
						+ "Latest version: " + Arrays.toString(tempOnlineVersion) + "\n\n"
						+ "Do you want to open the website where you can download the latest version?");
				if(m.open() == SWT.YES)
				{
					Log.info("Opening URL: " + AppSettings.URL_FORUM);
					Program.launch(AppSettings.URL_FORUM);
				}
			}
			@Override
			public void error() {}
		});
	}
	
	public static int getAmnesiaMajorVersion() {
		if(gameVersion != null) return gameVersion[0];
		return -1;
	}
	
	public static int getAmnesiaMinorVersion() {
		if(gameVersion != null) return gameVersion[1];
		return -1;
	}
	
	public static int[] tempOnlineVersion;
	
	/**
	 * Connect and check whether the app is up-to-date. <br>
	 * Should be called in a separate thread to avoid hangs while waiting for connections.
	 * @return false if update is available, otherwise true.
	 */
	public static boolean isUpToDate() throws Exception
	{
		int[] online = Util.fetchLatestOnlineVersion();
		if(online == null || online.length < APP_VERSION_ARRAY.length) {
			Log.info("Internal error while checking for updates. Assuming up-to-date.");
			return true;
		}
		
		boolean latest = false;
		
		for(int i = 0; i < APP_VERSION_ARRAY.length; i++)
		{
			if(online[i] >= APP_VERSION_ARRAY[i])
			{
				latest = true;
				break;
			}
		}
		tempOnlineVersion = online;
		
		if(!latest)
		{
			Log.info("Local version found to be less than the online version. Local: %s, Online: %s", 
					Arrays.toString(APP_VERSION_ARRAY), Arrays.toString(online));
		}
		return latest;
	}
	
	/**
	 * Checks online for whether the app is up-to-date, then calls the given callback's appropriate function based on the result.
	 * @param callback
	 */
	public static void checkForUpdates(UpdateCallback callback)
	{
		Log.info("Checking for updates...");
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Shell shell = Modloader.getForm().getShell();
				if(!shell.isDisposed())
				{
					shell.getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								if(isUpToDate()) {
									Log.info("App is up-to-date.");
									callback.updated();
								} else {
									Log.info("App is outdated.");
									callback.outdated();
								}
							} catch (Exception e) {
								Log.error("Failed to check for updates.");
								callback.error();
							}
						}
					});
				}
			}
		},
		"check-for-updates").start();
	}
	
	public static void shutdown()
	{
		FileHandler.writeCache();
		ResourceManager.flush();
		ResourceManager.unlockSessionFile();
		Log.info("=== Shutdown complete ===");
	}
	
	public static void promptErrorMessage(String message) {
		promptErrorMessage(new Shell(), message);
	}
	
	public static void promptErrorMessage(Shell parent, String message)
	{
		MessageBox m = new MessageBox(parent, SWT.ICON_ERROR);
		m.setText("AMNESIA MODLOADER");
		m.setMessage(message);
		m.open();
	}
	
	private static void backupShaders()
	{
		File shadersDir = new File(UserSettings.getVar("AmnesiaDir") + "/core/shaders");
		if(!shadersDir.isDirectory())
		{
			Log.error("Shaders dir not found, can't back up!");
			promptErrorMessage(form.getShell(), "Shaders directory not found. Is the game properly installed?");
			return;
		}
		
		
	}
	
	private static int checkAndOfferPatch(Mod mod)
	{
		try
		{
			File file = new File(mod.getAbsoluteStartFilePath());
			Document d = XMLParser.read(new FileInputStream(file));
			Element eConfigFiles = d.getChild("ConfigFiles");
			if(!eConfigFiles.hasAttribute("DefaultMainSettingsSDL2")
			|| !eConfigFiles.hasAttribute("DefaultUserKeysSDL2"))
			{
				MessageBox m = new MessageBox(form.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
				m.setText("Apply patch");
				m.setMessage("This mod appears to be old, and lacks the SDL2 content that came with Amnesia 1.3. Shall I add this patch?\n"
						+ "The only effect from not patching is that Amnesia will give you a warning before you start it. Patching will remove that warning.");
				int result = m.open();
				if(result == SWT.YES)
				{
					Log.info("Applying patch to: " + file);
					eConfigFiles.addAttribute("DefaultMainSettingsSDL2", "config/default_main_settings_sdl2.cfg");
					eConfigFiles.addAttribute("DefaultUserKeysSDL2", "config/default_user_keys_sdl2.cfg");
					XMLParser.write(d, new FileOutputStream(file), XMLParser.OPTION_ATTR_NEWLINE_INLINE);
				}
				if(result == SWT.CANCEL)
				{
					return 1;
				}
			}
		}
		catch (Exception e) {
			promptErrorMessage(form.getShell(), "Failed to apply patch.");
		}
		return 0;
	}
	
	public static void startOfficialLauncher(Mod mod)
	{
		if(mod == null) return; 
		
		String executable = UserSettings.getVar("AmnesiaDir") + File.separator 
				+ appSettings.getLauncherExecutableName() + appSettings.getExecutableExtension()
				+ " " + mod.getAbsoluteStartFilePath();
		File workingDir = new File(UserSettings.getVar("AmnesiaDir"));
		
		Log.info("Starting official launcher for: " + mod.getAbsoluteStartFilePath());
		
		try
		{
			Runtime.getRuntime().exec(executable, null, workingDir);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void copyDefaultsFileToMod(Mod mod)
	{
		File mainSettings = new File(FileHandler.DOCUMENTS_DIR, "/Amnesia/" + mod.getMainSaveName() + "/main_settings.cfg");
		mainSettings.getParentFile().mkdirs();
		if(!mainSettings.isFile())
		{
			File defaultsFile = new File("default_mod_settings.cfg");
			if(defaultsFile.isFile())
			{				
				try
				{
					Files.copy(defaultsFile.toPath(), mainSettings.toPath(), StandardCopyOption.REPLACE_EXISTING);
					Log.info("Successfully copied defaults file to: " + mainSettings.getAbsolutePath());
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				Log.warn("No defaults file exists to copy over.");
			}
		}
	}
	
	/**
	 * Perform the preparations for starting a mod and then launch it. Preparations include:<br>
	 * <ul>
	 * <li>Checking the mod's required version to the Amnesia version and warn.</li>
	 * <li>Apply SDL2 patch to config if needed and requested.</li>
	 * <li>Check for custom executable and warn.</li>
	 * <li>Warn for Steam pop-up if applicable.</li>
	 * <li>Install custom shaders if present and requested.</li>
	 * </ul>
	 * @param mod
	 */
	public static void prepareLaunchMod(Mod mod)
	{
		if(mod == null) return;
		
		boolean warnSteam = UserSettings.getBool("WarnSteam");
		boolean warnExecutable = UserSettings.getBool("WarnExec");
		boolean warnAmnesiaVersion = UserSettings.getBool("WarnAmnesiaVersion");
		boolean hasCustomExecutable = !mod.getCustomExecutableName().isEmpty();
		boolean installCustomShaders =  mod.hasCustomShaders() && UserSettings.getBool("InstallCustomShaders");
		boolean applyPatch = UserSettings.getBool("ApplyPatch");
//		boolean minimize = UserSettings.getBool("MinimizeModloader");
		boolean useSteam = UserSettings.getBool("UseSteam") && !hasCustomExecutable;
		boolean useDefaults = UserSettings.getBool("UseDefaults");
		
		if(warnAmnesiaVersion)
		{			
			try
			{
				String[] modVersionS = mod.getRequiredVersion().split("\\.");
				int[] modVersion = new int[] {Integer.parseInt(modVersionS[0]), Integer.parseInt(modVersionS[1])};
				int[] gameVersion = new int[] {getAmnesiaMajorVersion(), getAmnesiaMinorVersion()};
				if(gameVersion[0] != -1 && gameVersion[0] <= modVersion[0])
				{
					if(gameVersion[1] != -1 && gameVersion[1] < modVersion[1])
					{
						MessageBox m = new MessageBox(form.getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
						m.setText("Start mod");
						m.setMessage("The author of this mod says it requires Amnesia version " + mod.getRequiredVersion()
								+ ", but it looks like your version is " + getAmnesiaMajorVersion() + "." + getAmnesiaMinorVersion()
								+ ". Are you sure you want to start this mod? It might not work as expected, if at all.");
						if(m.open() != SWT.OK) return;
					}
				}
			}
			catch(NumberFormatException e) {
				Log.error("Mod's claimed version requirement is not a valid number, so I can't check for compatibility.");
			}
		}
		
		if(applyPatch && (checkAndOfferPatch(mod) == 1)) return;
		
		String execPath = UserSettings.getVar("AmnesiaDir") + File.separator;
		
		if(hasCustomExecutable)
		{
			if(warnExecutable)
			{
				MessageBox m = new MessageBox(form.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				m.setText("Start mod");
				m.setMessage("This mod features a custom executable. The author has requested that this executable be started instead of the normal one. Do you want to continue?");
				int result = m.open();
				if(result == SWT.NO) return;
			}
			
			execPath += mod.getCustomExecutableName();
		}
		else
		{
			execPath += appSettings.getGameExecutableName();
		}
		
		execPath += appSettings.getExecutableExtension();
		
		File exec = new File(execPath);
		if(!exec.isFile())
		{
			Log.error("Could not find Amnesia executable file in directory: %s", exec);
			promptErrorMessage(form.getShell(), "Amnesia executable file not found in the specified directory:\n\n" + exec.getAbsolutePath());
		}
		else
		{
			String param;
			
			if(useSteam)
			{
				if(warnSteam)
				{
					MessageBox m = new MessageBox(form.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					m.setText("Start mod");
					m.setMessage("You are about to start this mod through Steam. Before it starts, Steam requires you to accept a prompt confirming this, "
							+ "so this is just a heads-up. After you click OK, if you don't see it, open Steam yourself.\n\nYou can disable this warning in the settings.");
					int result = m.open();
					if(result != SWT.OK) return;
				}
				
				if(mod.getRelativeStartFilePath().contains(".")) 
					param = STEAM_PROTOCOL + STEAM_COMMAND + "/" + STEAM_GAME_ID + "//" + mod.getAbsoluteStartFilePath();
				else
					param = STEAM_PROTOCOL + STEAM_COMMAND + "/" + STEAM_GAME_ID + "//" + mod.getRelativeStartFilePath();
			}
			else
			{
				param = "\"" + exec.getAbsolutePath() + "\" " + mod.getRelativeStartFilePath();
			}
			
			if(installCustomShaders) backupShaders();
			if(useDefaults) copyDefaultsFileToMod(mod);

			Log.info(param);
			
			if(useSteam)
			{
				Log.info("Invoking Steam protocol...");
				Program.launch(param);
			}
			else
			{
				final DialogGameRunning d = new DialogGameRunning(form.getShell(), SWT.BORDER | SWT.TITLE | SWT.PRIMARY_MODAL);
				
				gameStartThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Process p = Runtime.getRuntime().exec(param, null, exec.getParentFile());
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									d.open();
								}
							});
							
							try
							{
								p.waitFor();
							}
							catch(InterruptedException e)
							{
								Log.info("Game thread interrupted, destroying process...");
								p.destroyForcibly();
							}
							
							Thread.sleep(500);
							
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									d.close();
								}
							});
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "game-start");
				gameStartThread.start();
			}
			
//			if(minimize) minimize();
		}
	}
	
	private static Thread gameStartThread;
	
	public static void forceQuitGame()
	{
		if(gameStartThread.isAlive()) gameStartThread.interrupt();
	}
	
//	private static void minimize() {
//		form.getShell().setMinimized(true);
//	}
	
	public static IShellPrimary getForm() {
		return form;
	}
	
	public static AppSettings getAppSettings() {
		return appSettings;
	}
	
	public static String[] getSystemAudioOutputs() {
		return audioOutputList;
	}
	
	public static Point getSystemNativeResolution() {
		return resolutions;
	}
}