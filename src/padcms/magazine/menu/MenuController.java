package padcms.magazine.menu;

import java.util.Timer;
import java.util.TimerTask;

import padcms.application11.R;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.ViewGroup;

public class MenuController {

	private Timer menuDisappearanceTimer;
	private boolean isVisible;
	private TopMenuController topMenuController;
	private BottomMenuController bottomMenuController;
	private HorisontalMenuController horisontalMenuController;
	private boolean isPortraitOrientation;
	private final Handler getViewHandler = new Handler();
	private static MenuController instance;

	public static MenuController getIstance(Context context) {
		if (instance == null) {
			instance = new MenuController(context);
		}
		return instance;
	}

	public MenuController(Context context) {

		topMenuController = new TopMenuController(
				(ViewGroup) ((Activity) context)
						.findViewById(R.id.TopMenuContainer));
		bottomMenuController = new BottomMenuController(
				(ViewGroup) ((Activity) context)
						.findViewById(R.id.BottomMenuContainer));
		horisontalMenuController = new HorisontalMenuController(
				(ViewGroup) ((Activity) context)
						.findViewById(R.id.RelativeHorisontalMenu));
		topMenuController.setRootMenuController(this);
		bottomMenuController.setRootMenuController(this);
		horisontalMenuController.setRootMenuController(this);
		configMenuController(context);
		isVisible = false;
	}

	public void configMenuController(Context context) {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		hideMenu();
		isPortraitOrientation = display.getWidth() < display.getHeight();

		if (isPortraitOrientation) {
			topMenuController.initDataVertical();
			bottomMenuController.initData();
			horisontalMenuController.destroyData();
		} else {

			topMenuController.initDataHorisontal();
			bottomMenuController.destroyData();
			horisontalMenuController.initData();
		}

	}

	public void menuDisappearanceTimerRun() {

		if (menuDisappearanceTimer != null)
			menuDisappearanceTimer.cancel();

		menuDisappearanceTimer = new Timer();
		menuDisappearanceTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				getViewHandler.post(new Runnable() {

					@Override
					public void run() {
						hideMenu();
					}
				});
			}
		}, 7000);
	}

	public void showHideMenu() {
		if (isVisible) {
			hideMenu();
		} else {
			showMenu();
		}

	}

	public void showMenu() {
		if (!isVisible) {
			if (isPortraitOrientation) {

				topMenuController.showMenu();
				bottomMenuController.showMenu();

			} else {
				topMenuController.showMenu();

				horisontalMenuController.showMenu();

			}

			menuDisappearanceTimerRun();
			isVisible = !isVisible;

		}
	}

	public void hideMenu() {
		if (isVisible) {
			if (isPortraitOrientation) {
				topMenuController.hideMenu();

				bottomMenuController.hideMenu();
			} else {
				topMenuController.hideMenu();

				horisontalMenuController.hideMenu();

			}

			isVisible = !isVisible;
		}
	}

	public static void destroy() {
		instance = null;
	}

}
