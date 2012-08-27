package padcms.net;

import org.apache.http.client.methods.HttpUriRequest;

import padcms.net.listener.ResponseListener;
import android.os.Handler;

public class Client {

	public static void sendRequest(final HttpUriRequest request,
			ResponseListener callback) {
		(new AsynchronousSender(request, new Handler(), new CallbackWrapper(
				callback))).start();
	}

}