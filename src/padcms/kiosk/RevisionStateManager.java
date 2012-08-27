package padcms.kiosk;

import padcms.bll.ApplicationFileHelper;
import padcms.bll.ApplicationManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Object of <code>FileStateManager</code> class converts values of processing
 * file state which saves in <code>SharedPreferences</code>. It gives access to
 * read and write state of file with unique name.
 */
public class RevisionStateManager {
	private final String SHARED_VALUE_INSTALL = "APP_SHARE_INSTALL";
	private final String SHARED_VALUE_PURCHASED = "APP_SHARE_PURCHASED";
	private SharedPreferences prefs;
	private static RevisionStateManager _inctance;
	private Context mContext;

	public static RevisionStateManager getInstance(Context mContext) {
		if (_inctance == null) {
			_inctance = new RevisionStateManager(mContext);
		}
		return _inctance;

	}

	/**
	 * Object of <code>FileState</code> class describes state of processing
	 * file.
	 */
	public enum RevisionInstallState {
		NOT_INTALLED(0), INSTALLING(1), INSTALLED(2), DOWNLOADED(3);
		private int state;

		private RevisionInstallState(int state) {
			this.state = state;
		}

		public int getCode() {
			return state;
		}

		/**
		 * @param val
		 *            - int value
		 * @return Instance of this enum or null if no instance for
		 *         <code>val</code>
		 */
		static public RevisionInstallState getTypeValueOf(int val) {
			for (RevisionInstallState typeValue : RevisionInstallState.values()) {
				if (val == typeValue.getCode()) {
					return typeValue;
				}
			}
			return null;
		}
	}

	public RevisionStateManager(Context mContext) {
		this.mContext = mContext;
		this.prefs = mContext.getSharedPreferences(
				String.valueOf(ApplicationManager.getAppicationID()),
				Context.MODE_PRIVATE);
		if (this.prefs.getInt(SHARED_VALUE_INSTALL + "_exist", -1) == -1) {
			ApplicationFileHelper.deleteFiles(ApplicationFileHelper
					.getApplicationToRootFolder());

			SharedPreferences.Editor editor = this.prefs.edit();
			editor.putInt(SHARED_VALUE_INSTALL + "_exist", 1);
			editor.commit();
		}

	}

	/**
	 * @param revisionID
	 *            of dowloading file
	 * @return FileState object or null if there is error of saved state(wrong
	 *         code of state)
	 */
	public RevisionInstallState getState(int revisionID) {
		int stateInt = this.prefs.getInt(SHARED_VALUE_INSTALL + revisionID,
				RevisionInstallState.NOT_INTALLED.getCode());
		return RevisionInstallState.getTypeValueOf(stateInt);
	}

	/**
	 * Sets state of file
	 * 
	 * @param name
	 *            of file which state it's need to set.
	 * @param state
	 *            of file
	 */
	public void setState(int revisionID, RevisionInstallState state) {
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putInt(SHARED_VALUE_INSTALL + revisionID, state.getCode());
		editor.commit();
	}

}