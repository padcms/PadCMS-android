package padcms.dao;

import padcms.dao.issue.ElementDataFactory;
import padcms.dao.issue.ElementDataPositionFactory;
import padcms.dao.issue.ElementFactory;
import padcms.dao.issue.MenuFactory;
import padcms.dao.issue.PageFactory;
import padcms.dao.issue.PageHorisontalFactory;
import padcms.dao.issue.PageImpositionFactory;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.IdentityScopeType;

public class DBIssueMaster extends AbstractDaoMaster {
	public static final int SCHEMA_VERSION = 1;

	/** Creates underlying database table using DAOs. */
	public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
		// PageFactory.createTable(db, ifNotExists);
		// ElementFactory.createTable(db, ifNotExists);
		// Element_dataFactory.createTable(db, ifNotExists);
	}

	/** Drops underlying database table using DAOs. */
	public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
		PageFactory.dropTable(db, ifExists);
		ElementFactory.dropTable(db, ifExists);
		ElementDataFactory.dropTable(db, ifExists);
		PageHorisontalFactory.dropTable(db, ifExists);
		PageImpositionFactory.dropTable(db, ifExists);
		MenuFactory.dropTable(db, ifExists);
		ElementDataPositionFactory.dropTable(db, ifExists);
	}

	public static abstract class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("greenDAO", "Creating tables for schema version "
					+ SCHEMA_VERSION);
			createAllTables(db, false);
		}
	}

	/** WARNING: Drops all table on Upgrade! Use only during development. */
	public static class DevOpenHelper extends OpenHelper {
		public DevOpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("greenDAO", "Upgrading schema from version " + oldVersion
					+ " to " + newVersion + " by dropping all tables");
			dropAllTables(db, true);
			onCreate(db);
		}
	}

	public DBIssueMaster(SQLiteDatabase db) {
		super(db, SCHEMA_VERSION);
		try {
			registerDaoClass(PageFactory.class);
			registerDaoClass(ElementFactory.class);
			registerDaoClass(ElementDataFactory.class);
			registerDaoClass(PageHorisontalFactory.class);
			registerDaoClass(PageImpositionFactory.class);
			registerDaoClass(ElementDataPositionFactory.class);
			registerDaoClass(MenuFactory.class);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}

	}

	public DBIssueSession newSession() {
		return new DBIssueSession(db, IdentityScopeType.Session, daoConfigMap);
	}

	public DBIssueSession newSession(IdentityScopeType type) {
		return new DBIssueSession(db, type, daoConfigMap);
	}

}
