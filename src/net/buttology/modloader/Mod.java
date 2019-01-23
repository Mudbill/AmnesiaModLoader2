package net.buttology.modloader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.buttology.modloader.file.UserSettings;

public class Mod {

	private String name;
	private String author;
	private String description;
	private File icon;
	private String requiredVersion;
	private String customExecutableName;
	private String mainSaveFolder;
	private boolean customShaders;
	private String mainInitPath;
	
	public Mod(String name, String author, String description, File icon, String requiredVersion, 
			String customExecutableName, boolean customShaders, String mainInitPath, String mainSaveFolder) {
		this.name = name;
		this.author = author;
		this.description = description;
		this.icon = icon;
		this.requiredVersion = requiredVersion;
		this.customExecutableName = customExecutableName;
		this.customShaders = customShaders;
		this.mainInitPath = mainInitPath;
		this.mainSaveFolder = mainSaveFolder;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

	public File getIcon() {
		return icon;
	}

	public String getRequiredVersion() {
		return requiredVersion;
	}

	public String getCustomExecutableName() {
		return customExecutableName;
	}

	public boolean hasCustomShaders() {
		return customShaders;
	}
	
	public String getMainSaveName() {
		return mainSaveFolder;
	}
	
	public String getAbsoluteStartFilePath() {
		return mainInitPath;
	}
	
	public String getRelativeStartFilePath() {
		Path root = Paths.get(UserSettings.getVar("AmnesiaDir"));
		Path mod = Paths.get(this.mainInitPath);
		Path relative = root.relativize(mod);
		return relative.toString();
	}
	
}
