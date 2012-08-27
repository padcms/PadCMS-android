package padcms.dao.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import padcms.dao.application.bean.Application;
import padcms.dao.application.bean.Issue;
import padcms.dao.application.bean.Revision;

public class JsonAdapter {

	public JsonAdapter() {

	}

	public static JSONObject streamToJson(InputStream inputStream) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String lineInStream = null;

		try {
			while ((lineInStream = reader.readLine()) != null) {
				stringBuilder.append(lineInStream + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		JSONObject json = makeJsonObjectByString(stringBuilder.toString());

		return json;
	}

	private static JSONObject makeJsonObjectByString(String sb) {
		JSONObject json = new JSONObject();
		try {
			json = new JSONObject(sb.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject getJSONObjectFromJSON(JSONObject parentJSON,
			String keyName) {
		JSONObject json = new JSONObject();
		try {
			json = parentJSON.getJSONObject(keyName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String getJSONObjectName(JSONObject parentJSON, int position) {
		String name = "";
		try {

			name = parentJSON.names().getString(position);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}

	public static String getStringFromJSON(JSONObject parentJSON, String keyName) {
		String resultString = "";
		try {
			resultString = parentJSON.getString(keyName);
			if (resultString.equals("null"))
				resultString = "";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultString;
	}

	public static Long getLongFromJSON(JSONObject parentJSON, String keyName) {
		Long resultLong = new Long(0);
		try {
			resultLong = parentJSON.getLong(keyName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultLong;
	}

	public static Application initApplicationFromJSON(JSONObject jsonApplication) {
		Application application = new Application();
		application.setId(getLongFromJSON(jsonApplication, "application_id"));
		application.setDescription(getStringFromJSON(jsonApplication,
				"application_description"));
		application.setNm_email(getStringFromJSON(jsonApplication,
				"application_notification_email"));
		application.setNm_fbook(getStringFromJSON(jsonApplication,
				"application_notification_facebook"));
		application.setNm_twitter(getStringFromJSON(jsonApplication,
				"application_notification_twitter"));
		application.setNt_email(getStringFromJSON(jsonApplication,
				"application_notification_email_title"));
		application.setTitle(getStringFromJSON(jsonApplication,
				"application_title"));
		application.setVersion(getStringFromJSON(jsonApplication,
				"application_product_id"));
		return application;
	}

	public static void initIssueFromJSON(Issue issue, JSONObject jsonIssue) {

		issue.setId(getLongFromJSON(jsonIssue, "issue_id"));
		issue.setState(getStringFromJSON(jsonIssue, "issue_state"));

		issue.setProduct_id(getLongFromJSON(jsonIssue, "issue_product_id"));
		issue.setTitle(getStringFromJSON(jsonIssue, "issue_title"));
//		issue.setApplication_id(getLongFromJSON(jsonIssue, "application_id"));
		issue.setIssue_number(getLongFromJSON(jsonIssue, "issue_number"));

		// issue.setApplication_id(getLongFromJSON(jsonIssue,
		// "application_id"));
		// issue.setColor(getStringFromJSON(jsonIssue, "issue_color"));
		// issue.setHorizontal_mode(getStringFromJSON(jsonIssue,
		// "issue_horizontal_mode"));
		// JSONObject jsonHelp = getJSONObjectFromJSON(jsonIssue, "help_pages");
		// issue.setHelp_page_horizontal(getStringFromJSON(jsonHelp,
		// "horizontal"));
		// issue.setHelp_page_vertical(getStringFromJSON(jsonHelp, "vertical"));

	}

	public static void initRevisionFromJSON(Revision revision,
			JSONObject jsonRevision) {

		revision.setId(getLongFromJSON(jsonRevision, "revision_id"));
		revision.setTitle(getStringFromJSON(jsonRevision, "revision_title"));

		revision.setCover_image_list(getStringFromJSON(jsonRevision,
				"revision_cover_image_list"));

		revision.setCreated(getStringFromJSON(jsonRevision, "revision_created"));
		revision.setVideo(getStringFromJSON(jsonRevision, "revision_video"));
		revision.setState(getStringFromJSON(jsonRevision, "revision_state"));
		revision.setColor(getStringFromJSON(jsonRevision, "revision_color"));
		revision.setHorizontal_mode(getStringFromJSON(jsonRevision,
				"revision_horizontal_mode"));
		JSONObject jsonHelp = getJSONObjectFromJSON(jsonRevision, "help_pages");
		revision.setHelp_page_horizontal(getStringFromJSON(jsonHelp,
				"horizontal"));
		revision.setHelp_page_vertical(getStringFromJSON(jsonHelp, "vertical"));

	}

	public static ArrayList<Issue> initIssuesCollectiontFromJSON(
			Long applicationID, JSONObject jsonIssues) {
		ArrayList<Issue> issuesCollections = new ArrayList<Issue>();
		for (int i = 0; i < jsonIssues.length(); i++) {

			Issue objectIssue = new Issue();
			issuesCollections.add(objectIssue);
			objectIssue.setApplication_id(applicationID);
			String issueID = getJSONObjectName(jsonIssues, i);

			JSONObject jsonObjectIssue = getJSONObjectFromJSON(jsonIssues,
					issueID);
			initIssueFromJSON(objectIssue, jsonObjectIssue);

			if (jsonObjectIssue.has("revisions")) {

				JSONObject revisionsJsonArray = getJSONObjectFromJSON(
						jsonObjectIssue, "revisions");

				ArrayList<Revision> revisionCollectionFromJSON = getRevisionCollectionFromJSON(revisionsJsonArray);
				for (Revision revision : revisionCollectionFromJSON) {
					revision.setIssue_id(objectIssue.getId());
				}

				objectIssue.setRevisionList(revisionCollectionFromJSON);

				jsonObjectIssue.remove("revisions");
			}

		}
		Collections.sort(issuesCollections);
		return issuesCollections;
	}

	public static ArrayList<Revision> getRevisionCollectionFromJSON(
			JSONObject jsonRevision) {
		ArrayList<Revision> revisionCollections = new ArrayList<Revision>();
		for (int i = 0; i < jsonRevision.length(); i++) {

			Revision objectRevision = new Revision();

			revisionCollections.add(objectRevision);

			String revisionID = getJSONObjectName(jsonRevision, i);

			JSONObject jsonObjectRevision = getJSONObjectFromJSON(jsonRevision,
					revisionID);

			initRevisionFromJSON(objectRevision, jsonObjectRevision);

		}
		return revisionCollections;
	}

	// @SuppressWarnings("rawtypes")
	// public static BaseObject moveDataFromJSONToObjectData(
	// JSONObject jsonObject, Application objectData) {
	//
	// HashMap<String, Object> tableDataContainer = objectData
	// .getDataContainer();
	//
	// Iterator keys = jsonObject.keys();
	// while (keys.hasNext()) {
	// String key = (String) keys.next();
	// String valueInJson = getStringFromJSON(jsonObject, key);
	//
	// tableDataContainer.put(key, valueInJson);
	// }
	//
	// return objectData;
	// }

	public static String getStringValueFromJSONObject(JSONObject jsonObject,
			String keyName) {
		String jsonStringValue = "";

		try {
			jsonStringValue = jsonObject.getString(keyName);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonStringValue;
	}

	public static JSONObject getJSONObjectFromJSONArray(JSONArray jsonArray,
			int position) {
		JSONObject jsonObjectValue = new JSONObject();

		try {
			jsonObjectValue = jsonArray.getJSONObject(position);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObjectValue;
	}

	public static JSONArray getJSONArraFromJSONObject(JSONObject jsonObject,
			String keyName) {
		JSONArray jsonArrayValue = new JSONArray();

		try {
			jsonArrayValue = jsonObject.getJSONArray(keyName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArrayValue;
	}

}
