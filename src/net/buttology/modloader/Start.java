package net.buttology.modloader;

import net.buttology.modloader.util.Log;
import net.buttology.modloader.util.Util;
import net.buttology.util.jeximel.XMLParser;

public class Start
{
	public static boolean firstTime = false;
	private static int[] lastVersion;
	
	public static void main(String[] args)
	{
		lastVersion = Util.detectPreviousVersion();
		if(args.length > 0)
			Log.setLoggingToFile(args[0]);
		XMLParser.setCharset("UTF-8");
		Modloader.init();
	}
	
	public static int[] getPreviouslyRanModloaderVersion() {
		return lastVersion;
	}
}
