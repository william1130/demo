package proj.nccc.logsearch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CommonUtility {
	public static String urlEncode(String value) throws UnsupportedEncodingException {
	    return URLEncoder.encode(value, "UTF-8");
	}
	
	public static String urlDecode(String value) throws UnsupportedEncodingException {
	    return URLDecoder.decode(value, "UTF-8");
	}
}
