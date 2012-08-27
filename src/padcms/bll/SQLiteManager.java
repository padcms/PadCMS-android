package padcms.bll;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteManager  extends SQLiteOpenHelper {

	private static SQLiteManager _inctance;
	private static final int DATABASE_VERSION = 1;
	
	//Tables
	public static final String TABLE_NAME_Revision="Revision";
	public static final String TABLE_NAME_Issue="Issue";
	public static final String TABLE_NAME_Application="Application";
	public static final String TABLE_NAME_Page="Page";
	public static final String TABLE_NAME_Element="Element";
	public static final String TABLE_NAME_Resource="Resource";
	public static final String TABLE_NAME_Menu="Menu";
	
	public SQLiteManager(Context context) {
		super(context, ApplicationManager.dataBasePath, null,
				DATABASE_VERSION);
		set_inctance(this);
	}
	public static Cursor getTableCursor(SQLiteDatabase sqldb,String  tableName) {
		try {
			Cursor cursor= sqldb.query(tableName, null,
					null, null, null, null, null);
			
			return cursor;
		} catch (SQLException e) {
			System.out.println("###########" + e.getMessage());
		}
		return null;
	}
	public static Cursor getTableCursor(SQLiteDatabase sqldb ,String  tableName,String selection,String[] selectionParams,String orederBy) {
		try {
			Cursor cursor =sqldb.query(tableName, null,
					selection, selectionParams, null, null, orederBy);
			
			return cursor;
		} catch (SQLException e) {
			System.out.println("###########" + e.getMessage());
		}
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		
	}
	public static SQLiteManager get_inctance() {
		return _inctance;
	}
	public static void set_inctance(SQLiteManager _inctance) {
		SQLiteManager._inctance = _inctance;
	}
	

}
