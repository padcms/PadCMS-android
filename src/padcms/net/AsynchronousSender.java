package padcms.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.util.Log;

public class AsynchronousSender extends Thread {

	private static DefaultHttpClient httpClient = new DefaultHttpClient();

	private HttpUriRequest request;
	private Handler handler;
	private CallbackWrapper wrapper;

	protected AsynchronousSender(HttpUriRequest request, Handler handler,
			CallbackWrapper wrapper) {
		this.request = request;
		this.handler = handler;
		this.wrapper = wrapper;
	}

	public void run() {
		try {
			final HttpResponse response;
			response = getClient().execute(request);
			wrapper.setResponse(response);
			handler.post(wrapper);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Eror AsynchronousSender", " error" + e.getMessage());
		}
	}

	private HttpClient getClient() {
		return httpClient;
	}

}