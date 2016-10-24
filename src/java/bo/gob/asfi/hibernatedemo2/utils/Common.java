package bo.gob.asfi.hibernatedemo2.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by fernando on 10/10/16.
 */
public class Common
{
	static Logger log = Logger.getLogger(Common.class.getName());
	static Long startTime;
	static Long endTime;

	public static void displayTitle( String title)
	{

		System.out.println(
			"  |_  |                |  ___|       | |  | |    | |    \n" +
				"    | | __ ___   ____ _| |_ ___  _ __| |  | | ___| |__  \n" +
				"    | |/ _` \\ \\ / / _` |  _/ _ \\| '__| |/\\| |/ _ \\ '_ \\ \n" +
				"/\\__/ / (_| |\\ V / (_| | || (_) | |  \\  /\\  /  __/ |_) |\n" +
				"\\____/ \\__,_| \\_/ \\__,_\\_| \\___/|_|   \\/  \\/ \\___|_.__/ \n"

		);
		System.out.println("" + title );

	}

	//simple sanifity function for simple quotes in Postgres queries
	public static String sanitify( String s)
	{
		if (s.matches(".*'.*")) {
			log.warn(s + " needs sanitify");
			s = s.replaceAll("'", "''");
			log.info(s);
			return s;
		} else {
			return s;
		}
	}

	static public Properties readConfigFile(Properties  prop, String configFile)
	{
		InputStream input = null;

		try {
			String filename = System.getProperty("user.dir") + "/" + configFile;
			input = new FileInputStream(filename);
			prop.load(input);
			return prop;

		} catch (IOException e) {
			log.warn(e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.info( e.getMessage() );
				}
			}
			return prop;
		}
	}


	static public Properties readDefaultConfig()
	{
		Properties defaultConfig = new Properties();
		InputStream is =  Common.class.getResourceAsStream( "/hibernate.config" );

		log.info("reading hibernate.config from resources");
		try {
			defaultConfig.load(is);
			log.info( defaultConfig.stringPropertyNames().size() + " config parameters added to config");
		} catch(IOException e) {
			log.error(e.getMessage());
			System.exit(0);
		} finally {
			if(is != null) {
				try {
					is.close();
				}
				catch(IOException e) {
					log.info(e.getMessage());
				}
			}
			return defaultConfig;
		}
	}

	static public Properties readConfig()
	{
		Properties config = new Properties(readDefaultConfig());
		config = readConfigFile(config, "hibernate.config");

		log.info( config.getProperty("postgresql.host"));
		log.info( config.getProperty("postgresql.port"));
		log.info( config.getProperty("postgresql.database"));
		log.info( config.getProperty("postgresql.user"));

		return config;
	}

	static public void startTimer()
	{
		startTime = new Date().getTime();
	}

	static public void endTimer()
	{
		endTime = new Date().getTime();
	}

	static public Long elapsedTime()
	{
		return endTime - startTime;
	}


	//get a buffered reader
	public static BufferedReader getBufferedReader( String filename) {
		BufferedReader br = null;

		log.info("reading file " + filename);

		try {
			br = new BufferedReader(new FileReader(filename));
		} catch(IOException e) {
			log.error(e.getMessage());
			System.exit(0);
		}
		return br;
	}
}
