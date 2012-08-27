package padcms.kiosk;

import padcms.bll.ApplicationFileHelper;
import android.content.SharedPreferences;

/**
 * Object of <code>FileStateManager</code> class converts values of processing
 * file state which saves in <code>SharedPreferences</code>. It gives access to
 * read and write state of file with unique name.
 */
public class FileStateManager {
	private final String FILE_NAME_SUFFIX = "DWLD_FILE_";
	private SharedPreferences prefs;

	/**
	 * Object of <code>FileState</code> class describes state of processing
	 * file.
	 */
	public enum FileState {
		NOT_DOWNLOADED(0), DOWNLOADING(1), READY_TO_EXTRACT(2), EXTRACTED(3), INSTALED(
				4);
		private int state;

		private FileState(int state) {
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
		static public FileState getTypeValueOf(int val) {
			for (FileState typeValue : FileState.values()) {
				if (val == typeValue.getCode()) {
					return typeValue;
				}
			}
			return null;
		}
	}

	/**
	 * Allocates a new <code>FileStateManager</code> object.
	 * 
	 * @param prefs
	 *            - object of <code>SharedPreferences</code>, which points to
	 *            preferences used for saving of files state, which was
	 *            processed at least once.
	 */
	public FileStateManager(SharedPreferences prefs) {
		this.prefs = prefs;
		if (this.prefs.getInt(FILE_NAME_SUFFIX + "_exist", -1) == -1) {
			ApplicationFileHelper.deleteFiles(ApplicationFileHelper
					.getApplicationToRootFolder());

			SharedPreferences.Editor editor = this.prefs.edit();
			editor.putInt(FILE_NAME_SUFFIX + "_exist", 1);
			editor.commit();
		}

	}

	/**
	 * @param name
	 *            of dowloading file
	 * @return FileState object or null if there is error of saved state(wrong
	 *         code of state)
	 */
	public FileState getState(String name) {
		int stateInt = this.prefs.getInt(FILE_NAME_SUFFIX + name,
				FileState.NOT_DOWNLOADED.getCode());
		return FileState.getTypeValueOf(stateInt);
	}

	/**
	 * Sets state of file
	 * 
	 * @param name
	 *            of file which state it's need to set.
	 * @param state
	 *            of file
	 */
	public void setState(String name, FileState state) {
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putInt(FILE_NAME_SUFFIX + name, state.getCode());
		editor.commit();
	}

	public boolean downloadingFile(String name) {
		return this.getState(name).equals(FileState.DOWNLOADING);
	}
}