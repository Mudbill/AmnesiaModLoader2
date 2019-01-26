package net.buttology.modloader.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;

import mslinks.ShellLink;
import net.buttology.modloader.Modloader;
import net.buttology.modloader.file.UserSettings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Util {

	public static int[] detectPreviousVersion()
	{
		File logFile = new File("log.txt");
		if(!logFile.isFile()) logFile = new File("log.log");
		if(logFile.isFile())
		{
			String line = null;
			try {
				Scanner scanner = new Scanner(logFile);
				if(scanner.hasNextLine()) line = scanner.nextLine();
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			if(line != null)
			{
				try
				{
					String[] parts = line.split(" ");
					line = parts[parts.length - 1];
					parts = line.split("\\.");
					int[] version = new int[3];
					for(int i = 0; i < parts.length; i++)
					{
						version[i] = Integer.parseInt(parts[i]);
					}
					return version;
				}
				catch(Exception e) {}
			}
		}
		return null;
	}
	
	public static void createShortcut(String target, String destination)
	{
		ShellLink sl = ShellLink.createLink(
				UserSettings.getVar("AmnesiaDir") + File.separator + 
				Modloader.getAppSettings().getGameExecutableName() + 
				Modloader.getAppSettings().getExecutableExtension())
		.setCMDArgs(target)
		.setWorkingDir(UserSettings.getVar("AmnesiaDir"));
		
		Log.info("Writing .lnk shortcut file to: " + destination);

		try
		{
			sl.saveTo(destination);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the host's operating system.
	 * @return EOperatingSystem type of the found OS.
	 */
	public static EOperatingSystem getOperatingSystem()
	{
		String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0))
			return EOperatingSystem.MACOS;
		if(os.indexOf("win") >= 0)
			return EOperatingSystem.WINDOWS;
		if(os.indexOf("nux") >= 0)
			return EOperatingSystem.LINUX;
		return EOperatingSystem.UNKNOWN;
	}

	/**
	 * Attach a simple right-click context menu to a Text field,
	 * only containing the default actions cut, copy, paste and select all.
	 * @param text
	 */
	public static void attachDefaultTextContextMenu(Text text)
	{
		Menu menu = new Menu(text);
		MenuItem itemCut = new MenuItem(menu, SWT.PUSH);
		itemCut.setText("Cut");
		itemCut.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				text.cut();
			}
		});
		MenuItem itemCopy = new MenuItem(menu, SWT.PUSH);
		itemCopy.setText("Copy");
		itemCopy.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				text.copy();
			}
		});
		MenuItem itemPaste = new MenuItem(menu, SWT.PUSH);
		itemPaste.setText("Paste");
		itemPaste.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				text.paste();
			}
		});
		new MenuItem(menu, SWT.SEPARATOR);
		MenuItem itemSelectAll = new MenuItem(menu, SWT.PUSH);
		itemSelectAll.setText("Select All");
		itemSelectAll.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				text.selectAll();
			}
		});
		text.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				if(text.getSelectionCount() == 0) {
					itemCut.setEnabled(false);
					itemCopy.setEnabled(false);
				} else {
					itemCut.setEnabled(true);
					itemCopy.setEnabled(true);
				}
			}
		});
		text.setMenu(menu);
	}

	/**
	 * Positions a Shell at the center of the screen.
	 * @param shell = the Shell to center.
	 */
	public static void centerShell(Shell shell)
	{
		Rectangle bds = shell.getDisplay().getPrimaryMonitor().getBounds();
		Point p = shell.getSize();

		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setLocation(nLeft, nTop);
	}
	
	public static void centerShellOnParent(Shell shell)
	{
		Composite parent = shell.getParent();
		if(parent == null) return;
		Point pRect = parent.getLocation();
		shell.setLocation(pRect.x + (parent.getSize().x - shell.getSize().x) / 2, pRect.y);
	}

	/**
	 * Reads the previous log file in the directory and returns the version. If not found, returns empty.
	 * @return version
	 */
	public static String readVersionFromLog(String logFilePath) {
		File log = new File(logFilePath);
		if(!log.isFile()) return "";

		String line = "";
		try {
			Scanner scanner = new Scanner(log);
			line = scanner.nextLine();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[] parts = line.split(" ");
		String version = parts[parts.length - 1];

		return version;
	}

	/**
	 * Gets the latest version claimed by the online update URL.
	 * @return an integer array where [0] is the major version, [1] is minor version etc, or returns null if failed.
	 */
	public static int[] fetchLatestOnlineVersion() throws Exception
	{
		int[] version = null;
		URL url = Modloader.getAppSettings().getUpdateUrl();
		Scanner scanner;

		scanner = new Scanner(url.openStream());
		if(scanner.hasNext())
		{
			String s = scanner.nextLine();
			try
			{
				String[] s2 = s.split("\\.");
				version = new int[s2.length];
				for(int i = 0; i < version.length; i++)
				{
					version[i] = Integer.parseInt(s2[i]);
				}
			}
			catch(Exception e)
			{
				Log.error("Something went wrong while parsing the online version String.");
			}
		}
		scanner.close();
		
		return version;
	}
	
	/**
	 * Fetches the version string of the latest release online.
	 * @return a string representing the version, or null if failed.
	 */
	public static String getLatestVersion() {
		try {
			URL url = Modloader.getAppSettings().getUpdateUrl();
			Scanner s = new Scanner(url.openStream());
			String version = s.nextLine();
			s.close();
			return version;					
		} catch (Exception e) {
			Log.warn("Could not fetch current version. Offline?");
			return null;
		}
	}

	public static String[] getAudioOutputList() {
		List<String> devices = new ArrayList<String>();
		Line.Info line = new Line.Info(SourceDataLine.class);
		for(Info info : AudioSystem.getMixerInfo()) {
			Mixer mixer = AudioSystem.getMixer(info);
			if(mixer.isLineSupported(line))
				devices.add(info.getName());
		}
		devices.remove(0);
		String[] output = new String[0];
		output = devices.toArray(output);
		return output;
	}
//
//	public static String[] getSupportedResolutions() {
//		Display display = Display.getDefault();
//		Rectangle screen = display.getPrimaryMonitor().getBounds();
//		String res = screen.width+"x"+screen.height;
//		return new String[] {"640x480", "800x600", res};
//	}
	
	public static Point getNativeResolution() {
		Display display = Display.getDefault();
		Rectangle screen = display.getPrimaryMonitor().getBounds();
		return new Point(screen.width, screen.height);
	}

//	public static void openWebsiteInBrowser(String url) {
//		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
//		if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
//			try {
//				Log.info("Opening URL in browser '%s'", url);
//				desktop.browse(new URI(url));
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//		}
//	}	
}
