package de.deepamehta.plugins.webclient;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Wraps the Servlet output stream and writer.
 */
public class GenericResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    public GenericResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public byte[] getData() {
        return output.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new FilterServletOutputStream(output);
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(getOutputStream(), true);
    }
}
