package padcms.kiosk.view;

import padcms.dao.application.bean.Revision;
import padcms.kiosk.RevisionStateManager;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.kiosk.view.action.InstallerController;

/**
 * Interface of <code>BasicViewController</code> specifies which method should
 * be implemented in the specific View controllers to ensure appropriate display
 * the GUI when user switches from one mode (for example, the gallery) to
 * another (for example, grid).
 */
public interface BasicViewController {
	RevisionStateManager.RevisionInstallState getRevisionState(Revision revision);

	void setRevisionState(Revision revision, RevisionInstallState revisionState);

	void setVisibleStateOpening(Revision revision);

	void setVisibleOnState(RevisionInstallState state, Revision revision);

	void setVisibleOnState(Revision revision);

	Revision getProcessingRevision();

	void startInstall(Revision revision);

	void interruptInstall(Revision revision);

	boolean isInstallerALive();

	void resetProgress();

	void setProgressBarMax(int max);

	void updateProgressWith(long current);

	void setErrMsg(int resId);

	void refresh();

	InstallerController getInstallThread();

	void setInstallThread(InstallerController installThread);
}
