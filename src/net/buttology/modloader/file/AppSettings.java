package net.buttology.modloader.file;

import java.net.URL;

import net.buttology.modloader.util.EOperatingSystem;

public class AppSettings {

	public static final String URL_YOUTUBE = "https://youtube.com/user/mrmudbill";
	public static final String URL_TWITTER = "https://twitter.com/mudbill";
	public static final String URL_FORUM = "https://www.frictionalgames.com/forum/thread-25806.html";

	private static final String EXECUTABLE_NAME_GAME 		= "Amnesia";
	private static final String EXECUTABLE_NAME_LAUNCHER 	= "Launcher";
	private static final String CONFIG_FILE 				= "preferences.cfg";
	private static final String SESSION_LOCK_FILE 			= "session.lock";
	private static final String LOG_FILE 					= "modloader.log";
	private static final String MOD_CONFIG_FILE				= "modloader.cfg";
	private static final String MOD_CONFIG_FILE_LEGACY		= "aml.cfg";
	private static final String MAIN_INIT_CONFIG			= "main_init.cfg";
	
	private EOperatingSystem os;
	private String configDirectoryPath;
	private String gameExecutableName = EXECUTABLE_NAME_GAME;
	private String launcherExecutableName = EXECUTABLE_NAME_LAUNCHER;
	private String configFile = CONFIG_FILE;
	
	public AppSettings(EOperatingSystem os) {
		this.os = os;
		this.configDirectoryPath = System.getProperty("user.dir");
	}
	
	public String getMainInitConfigName() {
		return MAIN_INIT_CONFIG;
	}
	
	public String getModConfigFileName() {
		return MOD_CONFIG_FILE;
	}
	
	public String getModLegacyConfigFileName() {
		return MOD_CONFIG_FILE_LEGACY;
	}
	
	public String getSessionLockFile() {
		return SESSION_LOCK_FILE;
	}
	
	public String getLogFile() {
		return LOG_FILE;
	}
	
	public URL getUpdateUrl() {
		return os.getUpdateUrl();
	}
	
	public String getExecutableExtension() {
		return os.getExecutableExtension();
	}

	public String getGameExecutableName() {
		return gameExecutableName;
	}

	public String getLauncherExecutableName() {
		return launcherExecutableName;
	}

	public String getConfigDirectoryPath() {
		return configDirectoryPath;
	}

	public String getConfigFileName() {
		return configFile;
	}
	
	public void setGameExecutableName(String name) {
		this.gameExecutableName = name;
	}
	
	public void setLauncherExecutableName(String name) {
		this.launcherExecutableName = name;
	}
	
	public void resetGameExecutableName() {
		this.gameExecutableName = EXECUTABLE_NAME_GAME;
	}
	
	public void resetLauncherExecutableName() {
		this.launcherExecutableName = EXECUTABLE_NAME_LAUNCHER;
	}
}
