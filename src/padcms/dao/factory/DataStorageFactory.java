package padcms.dao.factory;

import java.io.File;
import java.util.ArrayList;

import padcms.bll.ApplicationFileHelper;
import padcms.bll.ApplicationManager;
import padcms.dao.DBApplicationMaster;
import padcms.dao.DBApplicationMaster.DevOpenHelper;
import padcms.dao.DBApplicationSession;
import padcms.dao.DBIssueMaster;
import padcms.dao.DBIssueSession;
import padcms.dao.application.bean.Application;
import padcms.dao.application.bean.Issue;
import padcms.dao.application.bean.Revision;
import padcms.dao.issue.bean.Menu;
import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.service.RevisionServiceController;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

public class DataStorageFactory {

	private Application application;

	private Revision currentRevisionIssue;
	private ArrayList<Page> issuePageCollection;
	private ArrayList<Menu> issueMenuCollection;

	private DBApplicationMaster dbApplicationMaster;
	private DBApplicationSession applicationSesion;

	private DBIssueMaster dbIssueMaster;
	private DBIssueSession issueSesion;

	// private DataBaseHelper dataBaseHelper;
	private DevOpenHelper helper;
	private static DataStorageFactory _instance;

	private Context context;
//	private RevisionServiceController revisionServiceController;

	public static DataStorageFactory getInstance(Context mContext) {
		if (_instance == null) {
			_instance = new DataStorageFactory(mContext);
		}
		return _instance;
	}

	public DataStorageFactory(Context mContext) {
		this.context = mContext;

		initApplicationFromDatabase();

	}

	public ArrayList<Page> getIssuePageCollection() {
		return issuePageCollection;
	}

	public void setIssuePageCollection(ArrayList<Page> issuePageCollection) {
		this.issuePageCollection = issuePageCollection;
	}

	public ArrayList<Menu> getIssueMenuCollection() {
		return issueMenuCollection;
	}

	public void setIssueMenuCollection(ArrayList<Menu> issueMenuCollection) {
		this.issueMenuCollection = issueMenuCollection;
	}

	public void initApplicationFromDatabase() {
		if (helper == null) {
			helper = new DBApplicationMaster.DevOpenHelper(context,
					ApplicationManager.dataBasePath, null);
			SQLiteDatabase db = helper.getWritableDatabase();
			dbApplicationMaster = new DBApplicationMaster(db);
			applicationSesion = dbApplicationMaster.newSession();
		}
		application = applicationSesion.getApplicationFactory()
				.loadFirstApplication();
		helper.close();
		helper = null;

	}

	public Revision getCurrentRevisionIssue() {
		return currentRevisionIssue;
	}

	public void setCurrentRevisionIssue(Revision currentRevisionIssue) {
		this.currentRevisionIssue = currentRevisionIssue;
	}

	// public void initIssuePages(final Issue issue, final Handler
	// callbackHandler) {
	// // setCurrentIssue(issue);
	// new Thread() {
	// public void run() {
	// Revision revision = issue.getPublishedRevision();
	// setCurrentRevisionIssue(revision);
	//
	// String absolutePathRevisionDB = ApplicationFileHelper
	// .getFileInstallRevisionFolder(
	// revision.getId().intValue()).getAbsolutePath()
	// + "/sqlite.db";
	//
	// DevOpenHelper helper = new DBApplicationMaster.DevOpenHelper(
	// context, absolutePathRevisionDB, null);
	// SQLiteDatabase db = helper.getWritableDatabase();
	// dbIssueMaster = new DBIssueMaster(db);
	// issueSesion = dbIssueMaster.newSession();
	//
	// issuePageCollection = issueSesion.getPageFactory()
	// .loadIssuePageCollection();
	// issueMenuCollection = (ArrayList<Menu>) issueSesion
	// .getMenuFactory().loadAll();
	// helper.close();
	//
	// Message msg = new Message();
	// msg.what = 1;
	// callbackHandler.sendMessage(msg);
	// };
	//
	// }.start();
	//
	// }

	public void initRevisionPages(final Revision revision,
			final Handler callbackHandler) {
		// setCurrentIssue(issue);

		new Thread() {
			public void run() {
				// Revision revisioni = issue.getPublishedRevision();
				setCurrentRevisionIssue(revision);

				String absolutePathRevisionDB = ApplicationFileHelper
						.getFileInstallRevisionFolder(
								revision.getId().intValue()).getAbsolutePath()
						+ "/sqlite.db";

				// nameCurrentIssueFolder = "content_" + revision.getId();
				// String absolutePathIssueDB = ApplicationFileHelper
				// .getAbsolutePathToInstallFolder(revision);

				DevOpenHelper helper = new DBApplicationMaster.DevOpenHelper(
						context, absolutePathRevisionDB, null);
				SQLiteDatabase db = helper.getWritableDatabase();
				dbIssueMaster = new DBIssueMaster(db);
				issueSesion = dbIssueMaster.newSession();

				issuePageCollection = issueSesion.getPageFactory()
						.loadIssuePageCollection();
				issueMenuCollection = (ArrayList<Menu>) issueSesion
						.getMenuFactory().loadAll();

				helper.close();

				Message msg = new Message();
				msg.what = 1;

				callbackHandler.sendMessage(msg);

			};

		}.start();

	}

	// public void startRevisionService(final Revision revision) {
	//
	// IssueViewFactory.getIssueViewFactoryIstance(context)
	// .initPageViewCollection();
	//
	// if (revisionServiceController == null) {
	// revisionServiceController = new RevisionServiceController(context);
	// } else if (revisionServiceController.getRevisionID() !=
	// currentRevisionIssue
	// .getId().intValue()) {
	// revisionServiceController.doUnbindService();
	// }
	//
	// revisionServiceController.doBindService(currentRevisionIssue.getId()
	// .intValue());
	//
	// }

	public void destroyRevisionService() {

	}

	public void saveChangesInDatabse(Application application) {
		// DevOpenHelper helper =
		if (helper == null) {
			helper = new DBApplicationMaster.DevOpenHelper(context,
					ApplicationManager.dataBasePath, null);
			SQLiteDatabase db = helper.getWritableDatabase();
			dbApplicationMaster = new DBApplicationMaster(db);
			applicationSesion = dbApplicationMaster.newSession();
		}
		if (this.application == null) {
			applicationSesion.getApplicationFactory().insert(application);
			applicationSesion.getIssueFactory().insertList(
					application.getIssueList());
		} else {
			applicationSesion.getApplicationFactory().update(application);
			setIssueInDataBase(application.getIssueList());
		}
		this.application = application;
		helper.close();
		helper = null;
	}

	private void setIssueInDataBase(ArrayList<Issue> issueCollection) {
		for (Issue issue : issueCollection) {
			Issue currentIssue = application.getIssue(issue.getId());
			if (currentIssue == null) {
				applicationSesion.getIssueFactory().insert(issue);
			} else {
				applicationSesion.getIssueFactory().update(issue);
				setRevisionInDataBase(currentIssue, issue.getRevisionList());
			}
		}
	}

	private void setRevisionInDataBase(Issue currentIssue,
			ArrayList<Revision> revisionCollection) {
		for (Revision revision : revisionCollection) {
			Revision currentRevision = currentIssue.getRevision(revision
					.getId());
			if (currentRevision == null) {
				applicationSesion.getRevisionFactory().insert(revision);
			} else {
				applicationSesion.getRevisionFactory().update(revision);
			}
		}
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}
