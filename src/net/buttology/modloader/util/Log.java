package net.buttology.modloader.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log 
{
	private static final String INFO = "[INFO]\t\t" ;
	private static final String ERROR = "[ERROR]\t\t";
	private static final String WARNING = "[WARNING]\t";
	
	/**
	 * Print a simple message.
	 * @param text
	 */
	public static void temp(String msg, Object... args)
	{
		System.out.printf(msg + "%n", args);
	}

	/**
	 * Print an info-level message to the console/log.
	 * @param msg
	 * @param args
	 */
	public static void info(String msg, Object... args)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String output = "("+timeStamp+")\t" + INFO + msg;
		System.out.printf(output + "%n", args);
	}
	
	/**
	 * Print an info-level message to the console/log.
	 * @param msg
	 */
	public static void info(String msg) {
		info(msg, (Object[]) null);
	}
	
	public static void info(int msg) {
		info(msg + "");
	}
	
	public static void info(float msg) {
		info(msg + "");
	}
	
	public static void info(boolean msg) {
		info(msg + "");
	}
	
	public static void info(long msg) {
		info(msg + "");
	}
	
	public static void info(double msg) {
		info(msg + "");
	}
	
	public static void info(byte msg) {
		info(msg + "");
	}
	
	public static void info(char msg) {
		info(msg + "");
	}
	
	/**
	 * Print an info-level message to the console/log without any carriage return at the end.
	 * @param msg
	 * @param args
	 */
	public static void info_nocr(String msg, Object... args)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String output = "("+timeStamp+")\t" + INFO + msg;
		System.out.printf(output, args);
	}
	
	/**
	 * Print an info-level message to the console/log without a datetime prefix.
	 * @param msg
	 * @param args
	 */
	public static void info_nopre(String msg, Object... args)
	{
		System.out.printf(msg+"%n", args);
	}

	
	/**
	 * Print an info-level message to the console/log with no additional formatting.
	 * @param msg
	 * @param args
	 */
	public static void info_nof(String msg, Object... args)
	{
		System.out.printf(msg, args);
	}
	
	/**
	 * Print a warning-level message to the console/log.
	 * @param msg
	 * @param args
	 */
	public static void warn(String msg, Object... args)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + WARNING + msg;
		System.err.printf(s + "%n", args);
	}
	
	public static void warn(String msg)
	{
		warn(msg, (Object[]) null);
	}
	
	/**
	 * Print a warning-level message to the console/log without any carriage return at the end.
	 * @param msg
	 * @param args
	 */
	public static void warn_nocr(String msg, Object... args)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + WARNING + msg;
		System.err.printf(s, args);
	}
	
	/**
	 * Print a warning-level message to the console/log with no additional formatting.
	 * @param msg
	 * @param args
	 */
	public static void warn_nof(String msg, Object... args)
	{
		System.err.printf(msg, args);
	}
	
	/**
	 * Print an error-level message to the console/log.
	 * @param msg
	 * @param args
	 */
	public static void error(String msg, Object... args)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + ERROR + msg;
		System.err.printf(s + "%n", args);
	}
	
	public static void error(String msg)
	{
		error(msg, (Object[]) null);
	}
	
	/**
	 * Print an error-level message to the console/log without any carriage return at the end.
	 * @param msg
	 * @param args
	 */
	public static void error_nocr(String msg, Object... args)
	{
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String s = "("+timeStamp+")\t" + ERROR + msg;
		System.err.printf(s, args);
	}
	
	/**
	 * Print an error-level message to the console/log with no additional formatting.
	 * @param msg
	 * @param args
	 */
	public static void error_nof(String msg, Object... args)
	{
		System.err.printf(msg, args);
	}
	
	/**
	 * Tells this logger to print log messages to the given file rather than the console.
	 * @param file
	 */
	public static void setLoggingToFile(String file)
	{
		try 
		{
			PrintStream ps = new PrintStream(file);
			System.setOut(ps);
			System.setErr(ps);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
}
