package padcms.magazine.menu;

import padcms.application11.R;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.menu.elementView.HorisontalMenuElementView;
import padcms.magazine.page.HorisontalPageView;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

public class HorisontalMenuController {

	private ViewGroup rootHorisontalMenu;
	// private RelativeLayout gallarySripeMenuContainer;
	private Gallery gallaryMenuView;
	private MenuController rootMenuController;

	private IssueViewFactory issueViewFactory;
	private int height;

	public HorisontalMenuController(ViewGroup rootHorisontalMenu) {

		this.rootHorisontalMenu = rootHorisontalMenu;
		issueViewFactory = IssueViewFactory
				.getIssueViewFactoryIstance(rootHorisontalMenu.getContext());

		initMenuControls(rootHorisontalMenu);

		setClickListners();

	}

	private void initMenuControls(ViewGroup rootTopMenu) {

		gallaryMenuView = (Gallery) rootTopMenu
				.findViewById(R.id.GalleryHorisontalMenu);

	}

	public void initData() {
		rootHorisontalMenu.setVisibility(View.VISIBLE);
		configTopMenuSommaryPopUp(rootHorisontalMenu.getContext());
		HorisontalMenuGalleryAdapter menuAdapter = new HorisontalMenuGalleryAdapter(
				rootHorisontalMenu.getContext(),
				issueViewFactory.getHorisontalPageViewCollection());
		gallaryMenuView.setAdapter(menuAdapter);
		gallaryMenuView.setSelection(1);
		rootHorisontalMenu.setVisibility(View.GONE);
		// ApplicationSkinFactory.animateHideFast(rootHorisontalMenu);

	}

	public void destroyData() {
		// ApplicationSkinFactory.animateHideFast(rootHorisontalMenu);
		rootHorisontalMenu.setVisibility(View.GONE);
		if (gallaryMenuView.getAdapter() != null) {
			((HorisontalMenuGalleryAdapter) gallaryMenuView.getAdapter())
					.destroy();
		}
		gallaryMenuView.setAdapter(null);

	}

	private void setClickListners() {

		gallaryMenuView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {

				rootMenuController.menuDisappearanceTimerRun();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		gallaryMenuView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapter, View view,
							int position, long arg3) {

						HorisontalMenuElementView item = (HorisontalMenuElementView) adapter
								.getItemAtPosition(position);

						issueViewFactory.flipToHorisontalPage((HorisontalPageView)item.getParentPageView());

						rootMenuController.hideMenu();
					}
				});

	}

	public MenuController getRootMenuController() {
		return rootMenuController;
	}

	public void setRootMenuController(MenuController rootMenuController) {
		this.rootMenuController = rootMenuController;
	}

	private void configTopMenuSommaryPopUp(Context mContext) {
		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		height = Math.min(display.getWidth(), display.getHeight()) / 4;

		rootHorisontalMenu.getLayoutParams().height = height;

	}

	public void showMenu() {
		if (gallaryMenuView.getAdapter() != null) {
			((HorisontalMenuGalleryAdapter) gallaryMenuView.getAdapter())
					.activate();
		}
		rootHorisontalMenu.setVisibility(View.VISIBLE);

	}

	public void hideMenu() {

		// if (rootHorisontalMenu.getVisibility() != View.GONE) {
		//
		// rootHorisontalMenu.setVisibility(View.GONE);
		// }
		rootHorisontalMenu.setVisibility(View.GONE);
		if (gallaryMenuView.getAdapter() != null) {
			((HorisontalMenuGalleryAdapter) gallaryMenuView.getAdapter())
					.destroy();
		}
		// ApplicationSkinFactory.animateHide(rootHorisontalMenu);

	}

}
