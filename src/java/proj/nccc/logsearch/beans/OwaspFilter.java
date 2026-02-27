package proj.nccc.logsearch.beans;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 做為 Web.xml Filter
 * 主要在解決當Internet時的漏洞
 * 1. 非同站參考,通常被砍入到別的網站的frame
 * 2. cookie https
 * 3. 只允許 GET / POST 其他回應405 
 *
 */
public class OwaspFilter implements Filter {
	private Map<String, String> enabledHttpMethods;

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		// response.addHeader("X-Frame-Options", "DENY");
		response.addHeader("X-Frame-Options", "SAMEORIGIN");

		// Cookie without Secure flag set
		String sessionid = request.getSession().getId();
		response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid
				+ "; httpOnly; secure");
		String method = request.getMethod();
		if (enabledHttpMethods.get(method) == null) {
			response.setStatus(405);
			response.sendError(405, "'" + method.toUpperCase()
					+ "' HTTP method is unsupported.");
			System.out.println("'" + method.toUpperCase()
					+ "' HTTP method is unsupported, send to error 405.");
		}
		filterChain.doFilter(servletRequest, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.enabledHttpMethods = new HashMap<String, String>();
		String methods = filterConfig.getInitParameter("enableHttpMethods");
		if ((methods != null) && (!methods.isEmpty())) {
			for (String m : methods.split(","))
				this.enabledHttpMethods.put(m.trim(), m.trim());
		} else {
			this.enabledHttpMethods.put("GET", "GET");
			this.enabledHttpMethods.put("POST", "POST");
		}
	}

	public void destroy() {
	}
}
