package padcms.dao;

import padcms.dao.application.ApplicationFactory;
import padcms.dao.application.IssueFactory;
import padcms.dao.application.RevisionFactory;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.IdentityScopeType;

public class DBApplicationMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        ApplicationFactory.createTable(db, ifNotExists);
        IssueFactory.createTable(db, ifNotExists);
        RevisionFactory.createTable(db, ifNotExists);
       
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        ApplicationFactory.dropTable(db, ifExists);
        IssueFactory.dropTable(db, ifExists);
        RevisionFactory.dropTable(db, ifExists);
       
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
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
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DBApplicationMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ApplicationFactory.class);
        registerDaoClass(IssueFactory.class);
        registerDaoClass(RevisionFactory.class);
    }
    
    
    public DBApplicationSession newSession() {
        return new DBApplicationSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DBApplicationSession newSession(IdentityScopeType type) {
        return new DBApplicationSession(db, type, daoConfigMap);
    }
    
}
