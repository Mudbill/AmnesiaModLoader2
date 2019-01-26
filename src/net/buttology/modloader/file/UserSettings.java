package net.buttology.modloader.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.buttology.modloader.util.Log;
import net.buttology.util.jeximel.Document;
import net.buttology.util.jeximel.Element;
import net.buttology.util.jeximel.XMLException;
import net.buttology.util.jeximel.XMLParser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class UserSettings {
	
	public static final int DEFAULT_MAX_CHANNELS 		= 32;
	public static final int DEFAULT_STREAM_BUFFERS 		= 4;
	public static final int DEFAULT_STREAM_BUFFER_SIZE 	= 262144;
	
	private static Map<String, String> vars = new HashMap<String, String>();
	
	public static void setVar(String name, String val) {
		vars.put(name, val);
		Log.info("VAR SET: '"+name+"' = "+val);
	}
	
	public static void setVar(String name, boolean val) {
		setVar(name, ""+val);
	}
	
	public static void setVar(String name, int val) {
		setVar(name, ""+val);
	}
	
	public static void setVar(String name, float val) {
		setVar(name, ""+val);
	}
	
	public static String getVar(String name) {
		if(vars.containsKey(name)) return vars.get(name);
		return "";
	}
	
	public static boolean getBool(String name) {
		if(vars.containsKey(name)) {
			try
			{
				boolean b = Boolean.parseBoolean(vars.get(name));
				return b;
			}
			catch(Exception e) {}
		}
		return false;
	}
	
	public static int getInt(String name) {
		if(vars.containsKey(name)) {
			try
			{
				int i = Integer.parseInt(vars.get(name));
				return i;
			} catch(Exception e) {}
		}
		return 0;
	}
	
	public static boolean isSet(String name) {
		return vars.containsKey(name);
	}
	
	public static String getModDirectory() {
		if("true".equals(vars.get("UseCustomModDir"))) return vars.get("ModDir");
		else return vars.get("AmnesiaDir");
	}
	
	public static String print() {
		return vars.toString();
	}
	
	/**
	 * Attempts to read the settings in the given file and set them for the application.
	 * @param fileName
	 * @return 0 = Ok, 1 = no file found, 2 = error
	 */
	public static int readFromFile(String fileName) {
		FileInputStream fis;
		Document doc = null;
		try {
			fis = new FileInputStream(fileName);
			doc = XMLParser.read(fis);
		}
		catch (FileNotFoundException e) {
			Log.warn("User settings file not found, assuming defaults.");
			return 1;
		}
		catch (XMLException e) {
			e.printStackTrace();
			MessageBox m = new MessageBox(new Shell(), SWT.ICON_WARNING);
			m.setText("Amnesia Modloader");
			m.setMessage("Failed to read settings file. Either it is incorrectly written or it might be from an older version of the Modloader."
					+ "\nIt will now do a fresh start.");
			m.open();
			return 2;
		}
		
		if(doc != null)
		{
			Element modloader = doc.getChild("Modloader");
			if(modloader == null) return 1;
			
			Element main = modloader.getChild("Main");
			if(main != null)
				for(String s : main.getAttributes().keySet())
					setVar(s, main.getAttribute(s));
			
			Element defaults = modloader.getChild("Defaults");
			if(defaults != null)
			{
				for(String s : defaults.getAttributes().keySet()) 
					setVar(s, defaults.getAttribute(s));
				
//				Element advanced = defaults.getChild("Advanced");
//				if(advanced != null)
//					for(String s : advanced.getAttributes().keySet()) 
//						setVar(s, advanced.getAttribute(s));
			}
			
			
			Element preferences = modloader.getChild("Preferences");
			if(preferences != null)
				for(String s : preferences.getAttributes().keySet())
					setVar(s, preferences.getAttribute(s));
			
			Element warnings = modloader.getChild("Warnings");
			if(warnings != null)
				for(String s : warnings.getAttributes().keySet())
					setVar(s, warnings.getAttribute(s));
		}
		return 0;
	}
	
	public static void saveToFile(String fileName) {
		Log.info("Writing values to '%s'", fileName);
		Document doc = new Document();
		Element modloader = new Element("Modloader");
		doc.addChild(modloader);
		Element main = new Element("Main");
		modloader.addChild(main);
		
		// --- START CUSTOM SETTINGS --- //
		
		String[] mainAttribs = new String[] {"AmnesiaDir", "UseCustomModDir", "ModDir", "ExcludedFolders"};
		for(String s : mainAttribs)
			if(isSet(s)) main.addAttribute(s, getVar(s));
		
		Element defaults = new Element(modloader, "Defaults");
		defaults.addAttribute("UseDefaults", getVar("UseDefaults"));
//		String[] defaultsSettings = new String[] {
//				"AudioOutput", "ResolutionIndex", "CustomResX", "CustomResY", "Fullscreen", 
//				"TextureQuality", "ShadowQuality", "ShadowResolution", "TextureFilter", "Anistropic", 
//				"SSAO", "SSAOResolution", "SSAOSamples", "EdgeSmoothing", "WorldReflection", "Parallax"};
//		for(String s : defaultsSettings)
//			if(isSet(s)) defaults.addAttribute(s, getVar(s));
		
//		Element advanced = new Element(defaults, "Advanced");
//		String[] advancedAttribs = new String[] {
//				"DisableShadows", "LimitFPS", "MaxChannels", "StreamBuffers", "StreamBufferSize",
//				"SkipPreMenu", "DoNotSleep", "LoadFastPhysics", "LoadFastStaticObjects", "LoadFastEntities", "Gamma"};
//		for(String s : advancedAttribs)
//			if(isSet(s)) advanced.addAttribute(s, getVar(s));
		
		Element preferences = new Element(modloader, "Preferences");
		String[] preferencesAttribs = new String[] {
				"MinimizeModloader", "UseSteam", "CheckForUpdates", "UseCache",
				"UpdateModList", "IconSize", "InstallCustomShaders", "ApplyPatch"
		};
		for(String s : preferencesAttribs)
			if(isSet(s)) preferences.addAttribute(s, getVar(s));
		
		Element warnings = new Element(modloader, "Warnings");
		String[] warningsAttribs = new String[] {
				"WarnExec", "WarnShader", "WarnSteam", "WarnAmnesiaVersion"
		};
		for(String s : warningsAttribs)
			if(isSet(s)) warnings.addAttribute(s, getVar(s));
		
		// --- END CUSTOM SETTINGS --- //
		
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			XMLParser.write(doc, fos, XMLParser.OPTION_ATTR_NEWLINE_INLINE);
			fos.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(XMLException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
