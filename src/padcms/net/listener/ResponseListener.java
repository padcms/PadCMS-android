package padcms.net.listener;
import org.apache.http.HttpResponse;
 
public interface ResponseListener {
 
	public void onResponseReceived(HttpResponse response);
 
}
