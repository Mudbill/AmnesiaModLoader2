package net.buttology.modloader.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import net.buttology.modloader.Mod;
import net.buttology.modloader.Modloader;
import net.buttology.modloader.util.LangList;
import net.buttology.modloader.util.Log;
import net.buttology.util.jeximel.Document;
import net.buttology.util.jeximel.Element;
import net.buttology.util.jeximel.XMLException;
import net.buttology.util.jeximel.XMLParser;

import org.eclipse.swt.widgets.Display;

import sun.awt.shell.ShellFolder;

public class FileHandler {

	/** A File representing the current user's Documents directory. */
	public static final File DOCUMENTS_DIR = (File) ShellFolder.get("fileChooserDefaultFolder");
	
	/** A File representing a "modlist.cache" file in the current working directory. */
	private static final File CACHE = new File("modlist.cache");
	
	/**
	 * Gets a list of supported languages for the given mod. This is based on the main_init.cfg file, matching with the existing .lang files.
	 * @param mod
	 * @return
	 */
	public static LangList getLanguages(Mod mod)
	{
		Map<String, String> map = new HashMap<String, String>();
		String defLang = "";
		
		File mainInit = new File(mod.getAbsoluteStartFilePath());
		
		try
		{
			Document mainInitDoc = XMLParser.read(new FileInputStream(mainInit));
			String amnesiaDir = UserSettings.getVar("AmnesiaDir");
			String baseLangFolder = mainInitDoc.getChild("Directories").getAttribute("BaseLanguageFolder");
			String baseLangFileName = mainInitDoc.getChild("ConfigFiles").getAttribute("DefaultBaseLanguage");
			String langFolder = mainInitDoc.getChild("Directories").getAttribute("GameLanguageFolder");
			String langFileName = mainInitDoc.getChild("ConfigFiles").getAttribute("DefaultGameLanguage");
			File baseLang = new File(amnesiaDir + File.separator + baseLangFolder + baseLangFileName);
			if(!baseLang.isFile()) baseLang = new File(amnesiaDir + File.separator + baseLangFileName);
			if(baseLang.isFile())
			{
				Document lang = XMLParser.read(new FileInputStream(baseLang));
				Element[] langCat = lang.getChild("LANGUAGE").getChildren("CATEGORY");
				for(Element c : langCat)
				{
					if(c.getAttribute("Name").equals("Languages"))
					{
						for(Element entry : c.getChildren("Entry"))
						{
							String eName = entry.getAttribute("Name");
							String eText = entry.getText();
							eText = processUnicodes(eText);
							if((eName + ".lang").equals(langFileName)) defLang = eName;
							if(new File(amnesiaDir + File.separator + langFolder + eName + ".lang").isFile())
								map.put(eName, eText);
						}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return new LangList(map, defLang);
	}
	
	/**
	 * Convert all unicode codes from the input string into unicode characters, and return the resulting string.<br>
	 * Unicode codes are written as [u0000] where 0000 is the 4 digit code.
	 * @param input
	 * @return
	 */
	private static String processUnicodes(String input)
	{
		String result = input;
		int idx = 0, endIdx = 0;
		String sub;
		while((idx = input.indexOf("[u", idx)) != -1)
		{
			idx += 2;
			endIdx = input.indexOf(']', idx);
			if(endIdx != -1)
			{
				sub = input.substring(idx, endIdx);
				try
				{
					sub = ("0000" + sub).substring(sub.length());
					sub = Character.toString((char) Integer.parseInt(sub));
					result = result.substring(0, idx - 2) + sub + result.substring(endIdx + 1, result.length() - 1);
				}
				catch(Exception e) {
					Log.error("Error getting unicode character in string.");
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * Prints out the main_settings.cfg file for the given mod based on the given document information.
	 * @param mod
	 * @param d
	 */
	public static void writeMainSettingsDocument(Mod mod, Document d)
	{
		File f = new File(DOCUMENTS_DIR, "Amnesia/" + mod.getMainSaveName() + "/main_settings.cfg");
		if(!f.isFile()) f.getParentFile().mkdirs();
		try
		{
			XMLParser.write(d, new FileOutputStream(f));
		}
		catch (FileNotFoundException | XMLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Acquires the document information for the main_settings.cfg file for the given mod.
	 * @param mod
	 * @return
	 */
	public static Document getMainSettingsDocument(Mod mod)
	{
		File f = new File(DOCUMENTS_DIR, "Amnesia/" + mod.getMainSaveName() + "/main_settings.cfg");
		if(f.isFile())
		{
			try
			{
				return XMLParser.read(new FileInputStream(f));
			}
			catch (FileNotFoundException | XMLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Read the XML document for the given mod and username.
	 * @param mod
	 * @param username
	 * @return
	 */
	public static Document getUserSettingsDocument(Mod mod, String username)
	{
		File f = new File(DOCUMENTS_DIR, "Amnesia/" + mod.getMainSaveName() + "/" + username + "/user_settings.cfg");
		if(f.isFile())
		{
			try
			{
				return XMLParser.read(new FileInputStream(f));
			}
			catch (FileNotFoundException | XMLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Write the user_settings.cfg file for the given mod and username, based on the given document.
	 * @param mod
	 * @param username
	 * @param d
	 */
	public static void writeUserSettingsDocument(Mod mod, String username, Document d)
	{
		if(username.isEmpty()) return;
		File f = new File(DOCUMENTS_DIR, "Amnesia/" + mod.getMainSaveName() + "/" + username + "/user_settings.cfg");
		Log.info("Writing user document for: " + f);
		if(!f.isFile()) f.getParentFile().mkdirs();
		try
		{
			XMLParser.write(d, new FileOutputStream(f));
		}
		catch (FileNotFoundException | XMLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a list of all users currently existing for the given mod.
	 * @param mod
	 * @return
	 */
	public static String[] getListOfUsers(Mod mod)
	{
		List<String> list = new ArrayList<String>();
		File dir = new File(DOCUMENTS_DIR, "Amnesia/" + mod.getMainSaveName());
		
		try
		{
			Files.list(dir.toPath()).forEach(path -> {
				if(path.toFile().isDirectory()) list.add(path.getFileName().toString());
			});
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] s = new String[0];
		return list.toArray(s);
	}
	
	/**
	 * Loads a list of unique, per-user settings based on the given mod and matching username of a user.<br>
	 * If user doesn't exist, the result is an empty map.
	 * @param mod
	 * @param username
	 * @return
	 */
	public static Map<String, Boolean> loadUserSettings(Mod mod, String username)
	{
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		File settingsFile = new File(DOCUMENTS_DIR, "Amnesia/" + mod.getMainSaveName() + "/" + username + "/user_settings.cfg"); 
		if(settingsFile.isFile())
		{
			try
			{
				Document d = XMLParser.read(new FileInputStream(settingsFile));
				Element e = d.getChild("Debug");
				if(e != null)
				{
					map.put("ShowFPS", Boolean.parseBoolean(e.getAttribute("ShowFPS")));
					map.put("ShowDebugMessages", Boolean.parseBoolean(e.getAttribute("ShowDebugMessages")));
					map.put("ShowErrorsAndWarnings", Boolean.parseBoolean(e.getAttribute("ShowErrorsAndWarnings")));
					map.put("ScriptDebugOn", Boolean.parseBoolean(e.getAttribute("ScriptDebugOn")));
					map.put("AllowQuickSave", Boolean.parseBoolean(e.getAttribute("AllowQuickSave")));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * Creates a new user with the given username for the given mod. Users are just directories.
	 * @param mod
	 * @param username
	 */
	public static void createNewUser(Mod mod, String username)
	{
		File dir = new File(DOCUMENTS_DIR, "Amnesia/" + mod.getMainSaveName() + "/" + username);
		if(!dir.isDirectory()) dir.mkdirs();
	}

	public static Document getDefaultSettingsFile()
	{
		File defaultsFile = new File("default_mod_settings.cfg");
		if(defaultsFile.isFile())
		{
			try
			{
				Document d = XMLParser.read(new FileInputStream(defaultsFile));
				return d;
			}
			catch (FileNotFoundException | XMLException e) {
				e.printStackTrace();
			}
		}
		return new Document();
	}
	
	public static void writeDefaultSettingsFile(Document d)
	{
		File defaultsFile = new File("default_mod_settings.cfg");
		try
		{
			FileOutputStream fos = new FileOutputStream(defaultsFile);
			XMLParser.write(d, fos);
			fos.close();
		}
		catch(FileNotFoundException | XMLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		if(UserSettings.getBool("UseDefaults"))
//		{
//			Log.info("Attempting to write defaults...");
//			
//			File mainSettingsFile = new File(DOCUMENTS_DIR, "Amnesia/" + saveGameName + "/main_settings.cfg");
//			if(!mainSettingsFile.getParentFile().isDirectory() && !mainSettingsFile.getParentFile().mkdirs())
//			{
//				Log.error("Failed creating directories.");
//				return;
//			}
//			
//			Document mainSettings;
//			if(mainSettingsFile.isFile())
//			{
//				try
//				{
//					FileInputStream fis = new FileInputStream(mainSettingsFile);
//					mainSettings = XMLParser.read(fis);
//					fis.close();
//				}
//				catch(FileNotFoundException e) {
//					e.printStackTrace();
//					return;
//				}
//				catch(IOException e) {
//					e.printStackTrace();
//					return;
//				}
//				catch(XMLException e) {
//					Log.error("Failed reading XML file for main settings.");
//					return;
//				}
//			}
//			else
//			{
//				mainSettings = new Document();
//				mainSettings.addChild(new Element("Main"));
//				mainSettings.addChild(new Element("Screen"));
//				mainSettings.addChild(new Element("MapLoad"));
//				mainSettings.addChild(new Element("Graphics"));
//				mainSettings.addChild(new Element("Sound"));
//				mainSettings.addChild(new Element("Engine"));
//			}
//			
//			try
//			{
//				Element element;
//				if((element = mainSettings.getChild("Main")) != null)
//				{
//					if(UserSettings.isSet("SkipPreMenu"))
//						element.addAttribute("ShowPreMenu", ""+!UserSettings.getBool("SkipPreMenu"));						
//				}
//				
//				if((element = mainSettings.getChild("Screen")) != null)
//				{
//					if(UserSettings.isSet("Fullscreen"))
//						element.addAttribute("FullScreen", UserSettings.getVar("Fullscreen"));
//					if(UserSettings.isSet("CustomResX"))
//						element.addAttribute("Width", UserSettings.getVar("CustomResX"));
//					if(UserSettings.isSet("CustomResY"))
//						element.addAttribute("Height", UserSettings.getVar("CustomResY"));
//				}
//				
//				if((element = mainSettings.getChild("MapLoad")) != null)
//				{
//					if(UserSettings.isSet("LoadFastPhysics"))
//						element.addAttribute("FastPhysicsLoad", UserSettings.getVar("LoadFastPhysics"));
//					if(UserSettings.isSet("LoadFastEntities"))
//						element.addAttribute("FastEntityLoad", UserSettings.getVar("LoadFastEntities"));
//					if(UserSettings.isSet("LoadFastStaticObjects"))
//						element.addAttribute("FastStaticLoad", UserSettings.getVar("LoadFastStaticObjects"));
//				}
//				
//				if((element = mainSettings.getChild("Graphics")) != null)
//				{
//					if(UserSettings.isSet("Gamma"))
//						element.addAttribute("Gamma", UserSettings.getVar("Gamma"));
//					if(UserSettings.isSet("TextureQuality"))
//						element.addAttribute("TextureQuality", UserSettings.getVar("TextureQuality"));
//					if(UserSettings.isSet("TextureFilter"))
//						element.addAttribute("TextureFilter", UserSettings.getVar("TextureFilter"));
//					if(UserSettings.isSet("Anisotropic"))
//						element.addAttribute("TextureAnisotropy", UserSettings.getVar("Anisotropic"));
//					if(UserSettings.isSet("SSAO"))
//						element.addAttribute("SSAOActive", UserSettings.getVar("SSAO"));
//					if(UserSettings.isSet("SSAOResolution"))
//						element.addAttribute("SSAOResolution", UserSettings.getVar("SSAOResolution"));
//					if(UserSettings.isSet("SSAOSamples"))
//						element.addAttribute("SSAOSamples", UserSettings.getVar("SSAOSamples"));
//					if(UserSettings.isSet("WorldReflection"))
//						element.addAttribute("WorldReflection", UserSettings.getVar("WorldReflection"));
//					if(UserSettings.isSet("DisableShadows"))
//						element.addAttribute("ShadowsActive", ""+!UserSettings.getBool("DisableShadows"));
//					if(UserSettings.isSet("ShadowQuality"))
//						element.addAttribute("ShadowQuality", UserSettings.getVar("ShadowQuality"));
//					if(UserSettings.isSet("ShadowResolution"))
//						element.addAttribute("ShadowResolution", UserSettings.getVar("ShadowResolution"));
//					if(UserSettings.isSet("Parallax"))
//						element.addAttribute("ParallaxEnabled", UserSettings.getVar("Parallax"));
//					if(UserSettings.isSet("EdgeSmoothing"))
//						element.addAttribute("EdgeSmooth", UserSettings.getVar("EdgeSmoothing"));
//				}
//				
//				if((element = mainSettings.getChild("Sound")) != null)
//				{
//					if(UserSettings.isSet("AudioOutput"))
//						element.addAttribute("Device", UserSettings.getVar("AudioOutput"));
//					if(UserSettings.isSet("MaxChannels"))
//						element.addAttribute("MaxChannels", UserSettings.getVar("MaxChannels"));
//					if(UserSettings.isSet("StreamBuffers"))
//						element.addAttribute("StreamBuffers", UserSettings.getVar("StreamBuffers"));
//					if(UserSettings.isSet("StreamBufferSize"))
//						element.addAttribute("StreamBufferSize", UserSettings.getVar("StreamBufferSize"));
//				}
//				
//				if((element = mainSettings.getChild("Engine")) != null)
//				{
//					if(UserSettings.isSet("LimitFPS"))
//						element.addAttribute("LimitFPS", UserSettings.getVar("LimitFPS"));
//					if(UserSettings.isSet("DoNotSleep"))
//						element.addAttribute("SleepWhenOutOfFocus", ""+!UserSettings.getBool("DoNotSleep"));
//				}
//				
//				FileOutputStream fos = new FileOutputStream(mainSettingsFile);
//				XMLParser.write(mainSettings, fos);
//				fos.close();
//			}
//			catch(Exception e) {
//				Log.error("Failed writing defaults!");
//				e.printStackTrace();
//			}
//		}
	}
	
	/**
	 * Writes a cache file for the current modlist loaded into the modloader. This file is written into the current working directory.
	 */
	public static void writeCache()
	{
		if(UserSettings.getBool("UseCache"))
		{
			Log.info("Creating cache for current list...");
			Mod[] mods = Modloader.getForm().getModList().getAllMods();
			if(mods.length == 0) return;
			
			Document cacheDoc = new Document();
			cacheDoc.addChild(new Element("ModloaderCache"));
			
			for(Mod mod : mods)
			{
				Element e = new Element(cacheDoc.getChild("ModloaderCache"), "Mod");
				e.addAttribute("File", mod.getAbsoluteStartFilePath());
				
//				File icon = mod.getIcon();
//				String iconPath = "";
//				if(icon != null)
//				{
//					iconPath = icon.getAbsolutePath();
//				}
//				Element cache = new Element(cacheDoc.getChild("ModloaderCache"), "Mod");
//				cache.addAttribute("Name", mod.getName());
//				cache.addAttribute("Author", mod.getAuthor());
//				cache.addAttribute("IconFile", iconPath);
//				cache.addAttribute("MainInitFile", mod.getAbsoluteStartFilePath());
//				cache.addAttribute("Description", mod.getDescription());
//				cache.addAttribute("MinVersion", mod.getRequiredVersion());
//				cache.addAttribute("CustomExec", mod.getCustomExecutableName());
//				cache.addAttribute("MainSaveFolder", mod.getMainSaveName());
			}
			
			try
			{
				FileOutputStream fos = new FileOutputStream(CACHE);
				XMLParser.write(cacheDoc, fos);
				fos.close();
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			catch(XMLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Reads a cache file in the current working directory, if one is written. If found, loads all mods listed in the cache.
	 * @return
	 */
	public static Mod[] readCache()
	{
		List<Mod> mods = new ArrayList<Mod>();
		try
		{
			if(!CACHE.isFile())
			{
				return new Mod[0];
			}
			
			Log.info("Reading saved cache...");
			FileInputStream is = new FileInputStream(CACHE);
			Document cacheDoc = XMLParser.read(is);
			is.close();
			
			for(Element e : cacheDoc.getChild("ModloaderCache").getChildren())
			{
				File file = new File(e.getAttribute("File"));
				Mod mod = loadMod(file);
				if(mod != null) mods.add(mod);
				
//				File icon = new File(e.getAttribute("IconFile"));
//				File init = new File(e.getAttribute("MainInitFile"));
//				boolean shaders = new File(init.getParentFile().getParentFile(), "shaders").isDirectory();
//				if(!icon.isFile()) icon = null;
//				mods.add(new Mod(
//						e.getAttribute("Name"),
//						e.getAttribute("Author"),
//						e.getAttribute("Description"),
//						icon,
//						e.getAttribute("MinVersion"),
//						e.getAttribute("CustomExec"),
//						shaders,
//						e.getAttribute("MainInitFile"),
//						e.getAttribute("MainSaveFolder")
//					));
			}
			
		}
		catch (FileNotFoundException | XMLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Mod[] m = new Mod[0];
		return mods.toArray(m);
	}
	
	/**
	 * Attempts to detect which version of Amnesia last ran, based on the first line of text in the hpl.log file.<br>
	 * The result is an array where [0] is the major version and [1] is the minor version.<br>
	 * Each may be -1 if this function failed to detect it.
	 * @return
	 */
	public static int[] getAmnesiaVersionFromLogFile()
	{
		try
		{
			File logFile = new File(
					(File) ShellFolder.get("fileChooserDefaultFolder"), 
					"Amnesia" + File.separator + "Main" + File.separator + "hpl.log"
				);
			
			if(logFile.isFile())
			{
				Scanner scanner = new Scanner(logFile);
				String line = scanner.nextLine();
				scanner.close();
				String[] lineSplit = line.split(" ");
				if(lineSplit.length > 1)
				{
					String[] versionSplit = lineSplit[1].split("\\.");
					if(versionSplit.length > 1)
					{
						int[] version = new int[]{
								Integer.parseInt(versionSplit[0]),
								Integer.parseInt(versionSplit[1])
						};
						return version;
					}
				}
			}
			else
			{
				Log.warn("Failed to detect Amnesia version: Log file not found.");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loads a mod from the given main_init.cfg config file.
	 * @param configFile
	 * @return
	 */
	public static Mod loadMod(File configFile)
	{
		if(!configFile.isFile())
		{
			Log.error("Failed to load mod: " + configFile);
			return null;
		}
		
		String name = "Untitled";
		String author = "Unknown";
		String iconPath = "";
		String description = "No description.";
		String requiredVersion = "1.0";
		String execName = "";
		String mainSaveFolder = "";
		boolean ignoreShaders = false;
		
		try {
			Document d = XMLParser.read(new FileInputStream(configFile));
			Element variables = d.getChild("Variables");
			if(variables != null)
				name = variables.getAttribute("GameName");
			
			Element directories = d.getChild("Directories");
			if(directories != null)
				mainSaveFolder = directories.getAttribute("MainSaveFolder");
			
			File extraFile = new File(
					configFile.getParent() + File.separator + Modloader.getAppSettings().getModConfigFileName());
			if(!extraFile.isFile()) extraFile = new File(
					configFile.getParent() + File.separator + Modloader.getAppSettings().getModLegacyConfigFileName());
			if(extraFile.isFile())
			{
				Properties p = new Properties();
				p.load(new FileInputStream(extraFile));
				String pAuthor 				= p.getProperty("Author");
				String pIconPath 			= p.getProperty("IconFile");
				String pDescription 		= p.getProperty("Description");
				String pRequiredVersion 	= p.getProperty("MinVersion");
				String pExecName 			= p.getProperty("CustomExecName");
				
				if(pAuthor != null) 		author = pAuthor;
				if(pIconPath != null) 		iconPath = configFile.getParent() + File.separator + pIconPath;
				if(pDescription != null) 	description = pDescription;
				if(pRequiredVersion != null)requiredVersion = pRequiredVersion;
				if(pExecName != null) 		execName = pExecName;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (XMLException e) {
			Log.error("Error loading config for '%s'", configFile.getAbsolutePath());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		File icon = new File(iconPath);
		if(!icon.isFile()) icon = null;
		return new Mod(name, author, description, icon, requiredVersion, execName, ignoreShaders, configFile.getAbsolutePath(), mainSaveFolder);
	}
	
	/**
	 * Start a thread which walks the file tree and immediately loads a newly found mod into the modlist.
	 * @param start - A String representing the starting path.
	 * @param fileName - The filename String to look for, E.G. main_init.
	 * @param filters - A list of filters to apply.
	 */
	public static void loadAll(String start, String fileName, String[] filters)
	{
		File startFile = new File(start);
		if(!startFile.isDirectory())
		{
			Log.error("Starting directory for mod searching is not found!");
			Modloader.getForm().resetProgressBar();
			return;
		}
		
		Log.info("Start searching for mods...");
		
		Modloader.getForm().setProgressBarVisible(true);
		Modloader.getForm().setProgressBarMax(countDirectories(startFile, filters));
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				
				try
				{
					Files.walkFileTree(startFile.toPath(), new FileVisitor<Path>()
						{
						@Override
						public FileVisitResult postVisitDirectory(Path path, IOException attrs) throws IOException
						{
							if(path.getParent().equals(startFile.toPath()))
							{
//								System.out.println("Path: " + path.toString());
								Display.getDefault().asyncExec(new Runnable()
								{
									@Override
									public void run()
									{
										Modloader.getForm().incrementProgressBarStage();
									}
								});
							}
							return FileVisitResult.CONTINUE;
						}
						
						@Override
						public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {

							// Debug stuff
//							try {
//								Thread.sleep(10);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
							
							if(!isFiltered(path, filters))
							{
								return FileVisitResult.CONTINUE;
							}
							else
							{
								Log.info("Skipping: " + path);
								return FileVisitResult.SKIP_SUBTREE;
							}
						}
						
						@Override
						public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
							if(path.getFileName().toString().contains(fileName)) {
								Log.info("Found config file: " + path);
//								files.add(path.toFile());
								Mod mod = loadMod(path.toFile());
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {										
										Modloader.getForm().addLoadedModToTable(mod);
									}
								});
							}
							return FileVisitResult.CONTINUE;
						}
						
						@Override
						public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
							System.err.println(e);
							return FileVisitResult.CONTINUE;
						}
					});
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				
				Log.info("Time spent finding mods: %dms", System.currentTimeMillis() - startTime);
				
				// Wait a second and then remove progress bar.
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Display.getDefault().asyncExec(new Runnable()
				{
					@Override
					public void run()
					{
						Modloader.getForm().resetProgressBar();
					}
				});
			}
		}, "loading-mods");
		t.start();
	}
	
	private static int tempCounter = 0;
	
	/**
	 * Counts the number of subdirectories in the given directory, exclusing those filtered out.
	 * @param directory
	 * @param filters
	 * @return
	 */
	private static int countDirectories(File directory, String[] filters)
	{
		tempCounter = 0;
		try
		{
			Files.list(directory.toPath()).forEach(path -> {
				if(path.toFile().isDirectory() && !isFiltered(path, filters)) tempCounter++;
			});
			return tempCounter;
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Check if this path is filtered out by the filters.
	 * @param path
	 * @param filters
	 * @return
	 */
	private static boolean isFiltered(Path path, String[] filters)
	{
		File gameDir = new File(UserSettings.getVar("AmnesiaDir"));
		String name = path.getFileName().toString();
		boolean result = false;
		for(String filter : filters)
		{
			if(filter.isEmpty()) continue;
			String filterName = filter.replace("/", "").replace("*", "");

			if(filter.charAt(0) == '/')
				result = path.getParent().equals(gameDir.toPath()) && name.equals(filterName);
			else if(filter.charAt(0) == '*' && filter.charAt(filter.length() -1) == '*')
				result = name.contains(filterName);
			else if(filter.charAt(0) == '*')
				result = name.endsWith(filterName);
			else if(filter.charAt(filter.length() -1) == '*')
				result = name.startsWith(filterName);
			else
				result = name.equalsIgnoreCase(filterName);
			if(result) return true;
		}
		return false;
	}
	
}
