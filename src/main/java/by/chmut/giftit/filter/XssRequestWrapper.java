package by.chmut.giftit.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;

/**
 * The Xss request wrapper class extends the HttpServletRequest interface
 * by overriding some methods to provide control over the XSS attack.
 *
 * @author Sergei Chmut.
 */
class XssRequestWrapper extends HttpServletRequestWrapper {

    /**
     * The constant patterns to search for possible scripts.
     */
    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    /**
     * Instantiates a new request wrapper.
     *
     * @param request the request object that is passed to the servlet
     */
    XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * When calling the method, the method calls the super class
     * method version and attempts to filter for the presence
     * of possible scripts in the parameters.
     *
     * @param parameter the parameter name
     * @return array of parameters
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }
        return encodedValues;
    }

    /**
     * When calling the method, the method calls the super class
     * method version and attempts to filter for the presence
     * of possible scripts in the parameter.
     *
     * @param parameter the parameter name
     * @return parameter value
     */
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }

    /**
     * When calling the method, the method calls the super class
     * method version and attempts to filter for the presence
     * of possible scripts in the header.
     *
     * @param name the name of header
     * @return the header
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    /**
     * The method searches for possible scripts
     * and if it finds deletes them
     *
     * @param value the value for filtering
     * @return the string after filtering
     */
    private String stripXSS(String value) {
        if (value != null) {
            value = value.replaceAll("\0", "");
            for (Pattern scriptPattern : patterns) {
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}
