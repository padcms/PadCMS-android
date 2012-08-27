package padcms.net.listener;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import padcms.bll.ApplicationManager;
import padcms.dao.adapter.JsonAdapter;
import padcms.dao.application.bean.Application;
import padcms.dao.application.bean.Issue;
import android.os.Handler;
import android.os.Message;

public class ResponseListenerGetIssues implements ResponseListener {

	private Handler callBackHandler;

	public ResponseListenerGetIssues(Handler callBackHandler) {
		this.callBackHandler = callBackHandler;

	}

	@Override
	public void onResponseReceived(final HttpResponse response) {

		JSONObject responseJsonObject = null;
		try {
			responseJsonObject = JsonAdapter.streamToJson(response.getEntity()
					.getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			sendCallback(-1, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			sendCallback(-1, e.getMessage());
		}

		if (responseJsonObject != null && responseJsonObject.has("result")) {

			String resultError = JsonAdapter.getStringFromJSON(
					responseJsonObject, "error");

			if (isError(resultError)) {
				sendCallback(-1, resultError);
			} else {

				JSONObject resultObject = JsonAdapter.getJSONObjectFromJSON(
						responseJsonObject, "result");
				Application application = getApplicationFromJSON(resultObject);

				sendCallback(1, application);

			}

		}

	}

	public boolean isError(String errorMessage) {
		boolean isError = false;
		if (errorMessage.length() > 0 && !errorMessage.equals("null")) {
			isError = true;
		}
		return isError;
	}

	private void sendCallback(int what, Object messageObject) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = messageObject;

		if (callBackHandler != null)
			callBackHandler.sendMessage(msg);
	}

	private Application getApplicationFromJSON(JSONObject responseJSON) {
		Application application = null;

		if (responseJSON.has("applications")) {
			JSONObject applicationJsonObject = JsonAdapter
					.getJSONObjectFromJSON(responseJSON, "applications");
			JSONObject firstApplication = null;
			try {
				firstApplication = applicationJsonObject.getJSONObject(String
						.valueOf(ApplicationManager.getAppicationID()));
			} catch (JSONException e) {

				e.printStackTrace();
			}
			if (firstApplication != null) {
				application = JsonAdapter
						.initApplicationFromJSON(firstApplication);
				application.setIssueList(getIssueCollectionFromJSON(
						application.getId(), firstApplication));
			}
		}
		return application;
	}

	private ArrayList<Issue> getIssueCollectionFromJSON(Long applicationID,
			JSONObject responseJSON) {
		ArrayList<Issue> issueCollection = new ArrayList<Issue>();

		if (responseJSON.has("issues")) {
			JSONObject jsonArrayIssue = JsonAdapter.getJSONObjectFromJSON(
					responseJSON, "issues");
			issueCollection = JsonAdapter
					.initIssuesCollectiontFromJSON(applicationID,jsonArrayIssue);

		}

		return issueCollection;
	}

}
