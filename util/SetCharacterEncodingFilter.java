package com.zk.util;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * Character encoding filter, process request encodings
 * 
 * @author seiya
 *
 */
public class SetCharacterEncodingFilter implements Filter {
// ----------------------------------------------------- Instance Variables
    /**
     * The default character encoding to set for requests that pass through
     * this filter.
     */
    protected String encoding = null;
    /**
     * The filter configuration object we are associated with. If this value
     * is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;
    /**
     * Should a character encoding specified by the client be ignored?
     */
    protected boolean ignore = true;

    /**
     * Take this filter out of service.
     */
    public void destroy()
    {
        this.encoding = null;
        this.filterConfig = null;
    }
    /**
     * Select and set (if specified) the character encoding to be used to
     * interpret request parameters for this request.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        // Conditionally select and set the character encoding to be used
        if (ignore || null == request.getCharacterEncoding())
        {
            String encoding = selectEncoding(request);
            if (null != encoding) {
            	request.setCharacterEncoding(encoding);
            }
        }
        // Pass control on to the next filter
        chain.doFilter(request, response);
    }
    
    /**
     * Place this filter into service.
     *
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        if (null == value) {
        	this.ignore = true;
        } else if ("true".equalsIgnoreCase(value)) {
        	this.ignore = true;
        } else if ("yes".equalsIgnoreCase(value)) {
        	this.ignore = true;
        } else {
        	this.ignore = false;
        }
    }

    /**
     * Select an appropriate character encoding to be used, based on the
     * characteristics of the current request and/or filter initialization
     * parameters. If no character encoding should be set, return
     * <code>null</code>.
     * <p>
     * The default implementation unconditionally returns the value configured
     * by the <strong>encoding</strong> initialization parameter for this
     * filter.
     *
     * @param request The servlet request we are processing
     */
    protected String selectEncoding(ServletRequest request)
    {
        return this.encoding;
    }
}

