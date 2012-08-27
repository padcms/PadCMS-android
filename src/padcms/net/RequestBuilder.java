package padcms.net;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestBuilder {

	public static String getServiseHost() {
		return NetHepler.serviceHost;
	}

	public static String getUrlForGetIssues() {
		return getServiseHost() + "/api/v1/jsonrpc.php";
	}

	public static void setBaseRequestHeader(HttpUriRequest request) {

		request.setHeader("Accept", "application/json");
		request.setHeader("Content-Type", "application/json; charset=UTF-8");
	}

	public static HttpPost buildRequest_GetApplication(int applicationID) {
		HttpPost request = new HttpPost(getUrlForGetIssues());

		JSONObject jsonGetIssuesObject = makeRequestParamsFroGetIssueMethod(applicationID);

		setBaseRequestHeader(request);

		try {
			request.setEntity(new ByteArrayEntity(jsonGetIssuesObject
					.toString().getBytes("UTF8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return request;
	}

	private static JSONObject makeRequestParamsFroGetIssueMethod(
			int applicationID) {
		JSONObject jsonGetIssuesObject = new JSONObject();
		try {
			JSONObject methodParams = new JSONObject();
			//methodParams.put("iClientId", applicationID);
			methodParams.put("iApplicationId", applicationID);
			methodParams.put("sPlatform", "android");
			
			// methodParams
			// .put("sUdid", "F2D19DEB-54EF-52E0-A214-0D92CFE7F510");
			// 63b83b48eb90d49070e87bfce2762a649947e0ce

			// F2D19DEB-54EF-52E0-A214-0D92CFE7F510

			jsonGetIssuesObject.put("method", "client.getIssues");
			jsonGetIssuesObject.put("params", methodParams);
			jsonGetIssuesObject.put("id", 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonGetIssuesObject;
	}

}