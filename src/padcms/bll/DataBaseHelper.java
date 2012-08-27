package padcms.bll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "";
	

	private static String DB_NAME = "padcmsclient.db3";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		
		super(context, DB_NAME, null, 1);
		DB_PATH = context.getFilesDir().getAbsolutePath()+ "/";// context.getFilesDir().getAbsolutePath()
		this.myContext = context;
	 

	}

	public void initDataBaseHelper() throws IOException {
		boolean isExistDatabase = checkDataBase();
		try {
			if (!isExistDatabase) {
				createDataBase();
			}
			openDataBase();
		} catch (IOException e) {
			throw new Error("Not Exist database");
		}
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		// By calling this method and empty database will be created into
		// the default system path
		// of your application so we are gonna be able to overwrite that
		// database with our database.
		SQLiteDatabase db_Read= this.getReadableDatabase();
		db_Read.close();
		
		try {
			copyDataBase();
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		String myPath = DB_PATH + DB_NAME;
		if (new File(myPath).exists()) {
			try {

				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);

			} catch (SQLiteException e) {
				e.printStackTrace();
			}
			if (checkDB != null) {
				checkDB.close();
			}
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Cursor getTableCursor(String tableName) {
		try {
			Cursor cursor = myDataBase.query(tableName, null, null, null, null,
					null, null);
			return cursor;
		} catch (SQLException e) {
			System.out.println("###########" + e.getMessage());
		}
		return null;
	}

	public Cursor getTableCursor(String tableName, String selection,
			String[] selectionParams, String orederBy) {
		try {
			Cursor cursor = myDataBase.query(tableName, null, selection,
					selectionParams, null, null, orederBy);
			return cursor;
		} catch (SQLException e) {
			System.out.println("###########" + e.getMessage());
		}
		return null;
	}

}
// public class SQLiteManager extends SQLiteOpenHelper {
//
// private static SQLiteManager _inctance;
// private static final int DATABASE_VERSION = 1;
//
// //Tables
// public static final String TABLE_NAME_Revision="Revision";
// public static final String TABLE_NAME_Issue="Issue";
// public static final String TABLE_NAME_Application="Application";
// public static final String TABLE_NAME_Page="Page";
// public static final String TABLE_NAME_Element="Element";
// public static final String TABLE_NAME_Resource="Resource";
// public static final String TABLE_NAME_Menu="Menu";
//
// public SQLiteManager(Context context) {
// super(context, ApplicationManager.dataBasePath, null,
// DATABASE_VERSION);
// set_inctance(this);
// }
// public static Cursor getTableCursor(SQLiteDatabase sqldb,String tableName) {
// try {
// Cursor cursor= sqldb.query(tableName, null,
// null, null, null, null, null);
//
// return cursor;
// } catch (SQLException e) {
// System.out.println("###########" + e.getMessage());
// }
// return null;
// }
// public static Cursor getTableCursor(SQLiteDatabase sqldb ,String
// tableName,String selection,String[] selectionParams,String orederBy) {
// try {
// Cursor cursor =sqldb.query(tableName, null,
// selection, selectionParams, null, null, orederBy);
//
// return cursor;
// } catch (SQLException e) {
// System.out.println("###########" + e.getMessage());
// }
// return null;
// }
//
// @Override
// public void onCreate(SQLiteDatabase db) {
//
// }
//
// @Override
// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//
// }
// public static SQLiteManager get_inctance() {
// return _inctance;
// }
// public static void set_inctance(SQLiteManager _inctance) {
// SQLiteManager._inctance = _inctance;
// }
//
//
// }
