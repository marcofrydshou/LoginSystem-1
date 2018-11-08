package demo.config;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("test")
public class H2DatabaseInitializer {
	private final static String DB_CONNECTION_STRING = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1";
	private final static String DB_RESOURCE_FOLDER = "/db/h2testdata/";
	private static Connection conn;
	private static Class currentClass;

	public static H2DatabaseInitializer setup(Object testClass) {
		currentClass = testClass.getClass();
		return new H2DatabaseInitializer();
	}

	/**
	 * Sets up test data in the H2 database
	 */
	public static void initDB() {
		try {
			log.info("RUNNING INITDB");
			conn = DriverManager.getConnection(DB_CONNECTION_STRING, "h2", "h2");
			ScriptUtils.executeSqlScript(conn, new ClassPathResource(DB_RESOURCE_FOLDER + "init-setup-data.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource(DB_RESOURCE_FOLDER + "test-data.sql"));
		} catch (Exception e) {
			log.error("Error occurred attempting to run SQL script against H2 database.");
			e.printStackTrace();
		}
	}

	/**
	 * Sets up test data in the H2 database, taking in script names as varargs and executing them.
	 *
	 * @param sqlFileNames The names of the SQL files to be executed.
	 */
	public static H2DatabaseInitializer initDB(String... sqlFileNames) {
		try {
			String pathForCurrentClass = currentClass.getPackage().getName().replaceAll("\\.", "/") + "/";
			callScripts(pathForCurrentClass, sqlFileNames);
		} catch (NullPointerException e) {
			log.error("Classpath not found. Did you remember to run .setup(this) before trying to run a script?");
		}
		return new H2DatabaseInitializer();
	}

	/**
	 * Sets up test data in the H2 database, taking in script names as varargs and executing them. Use this for reusable scripts in the h2testdata folder.
	 *
	 * @param sqlFileNames The names of the SQL files to be executed. Path names assume a base path in the 'h2testdata' folder.
	 */
	public static H2DatabaseInitializer initDBFromResources(String... sqlFileNames) {
		callScripts(DB_RESOURCE_FOLDER, sqlFileNames);
		return new H2DatabaseInitializer();
	}

	/**
	 * Calls a script to force a full truncate of all tables in the H2 test database.
	 */
	public static H2DatabaseInitializer truncate() {
		callScripts(DB_RESOURCE_FOLDER, "truncate");
		return new H2DatabaseInitializer();
	}

	private static void callScripts(String basepath, String... sqlFileNames) {
		try {
			log.info("RUNNING INITDB");
			conn = DriverManager.getConnection(DB_CONNECTION_STRING, "h2", "h2");

			for (String sqlFile : sqlFileNames) {
				if (sqlFile.endsWith(".sql")) {
					sqlFile = sqlFile.substring(0, sqlFile.length() - 4);
				}
				String sqlFilePath = String.format("%s%s.sql", basepath, sqlFile);
				ScriptUtils.executeSqlScript(conn, new ClassPathResource(sqlFilePath));
			}
		} catch (Exception e) {
			log.error("Error occurred attempting to run SQL script against H2 database.");
			log.error("Please provide the names of the files without the '.sql' extension attached.");
			e.printStackTrace();
		}
	}

	/**
	 * Sets up test data in the H2 database, taking in script names as varargs and executing them.
	 *
	 * @param sqlFileNames The names of the SQL files to be executed. Path names assume a base path in the 'h2testdata' folder.
	 */
	public H2DatabaseInitializer thenInitDB(String... sqlFileNames) {
		try {
			String pathForCurrentClass = currentClass.getPackage().getName().replaceAll("\\.", "/") + "/";
			callScripts(pathForCurrentClass, sqlFileNames);
		} catch (NullPointerException e) {
			log.error("Classpath not found. Did you remember to run .setup(this) before trying to run a script?");
		}
		return this;
	}

	/**
	 * Sets up test data in the H2 database, taking in script names as varargs and executing them.
	 *
	 * @param sqlFileNames The names of the SQL files to be executed. Path names assume a base path in the 'h2testdata' folder.
	 */
	public H2DatabaseInitializer thenInitDBFromResources(String... sqlFileNames) {
		callScripts(DB_RESOURCE_FOLDER, sqlFileNames);
		return this;
	}

	/**
	 * Calls a script to force a full truncate of all tables in the H2 test database.
	 */
	public H2DatabaseInitializer thenTruncate() {
		callScripts(DB_RESOURCE_FOLDER, "truncate");
		return this;
	}
}
