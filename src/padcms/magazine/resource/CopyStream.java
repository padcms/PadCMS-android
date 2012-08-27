package padcms.magazine.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyStream {
	private InputStream _is;
	private ByteArrayOutputStream _copy = new ByteArrayOutputStream();

	/**
	 * 
	 */
	public CopyStream(InputStream is)
	{
		_is = is;
		
		try
		{
			copy();
		}
		catch(IOException ex)
		{
			// do nothing
		}
	}

	private int copy() throws IOException
	{
		int read = 0;
		int chunk = 0;
		byte[] data = new byte[256];
		
		while(-1 != (chunk = _is.read(data)))
		{
			read += data.length;
			_copy.write(data, 0, chunk);
		}
		
		return read;
	}
	
	public InputStream getCopy()
 {
		return (InputStream) new ByteArrayInputStream(_copy.toByteArray());
	}
}
