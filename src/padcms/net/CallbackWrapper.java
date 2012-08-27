package padcms.net;
import org.apache.http.HttpResponse;

import padcms.net.listener.ResponseListener;

 
public class CallbackWrapper implements Runnable {
 
	private ResponseListener callbackActivity;
	private HttpResponse response;
 
	public CallbackWrapper(ResponseListener callback) {
		this.callbackActivity = callback;
	}

	public void run() {
		callbackActivity.onResponseReceived(response);
	}
 
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
 
}
