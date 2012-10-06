package de.deepamehta.plugins.webclient;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlBaseHrefFilter implements Filter {

    public static String getBaseHref(HttpServletRequest r) {
        return r.getScheme() + "://" + r.getServerName() + ":" + r.getServerPort()
                + r.getContextPath() + r.getServletPath() + "/";
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // wrap response
        OutputStream out = response.getOutputStream();
        GenericResponseWrapper wrapper = new GenericResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, wrapper);

        // get content, replace base href attribute value and rewrite response
        byte[] bytes = new String(wrapper.getData()).replace("__DM_BASE_HREF__",
                getBaseHref((HttpServletRequest) request)).getBytes();
        response.setContentLength(bytes.length);
        out.write(bytes);
        out.close();
    }

    @Override
    public void destroy() {
    }

}
