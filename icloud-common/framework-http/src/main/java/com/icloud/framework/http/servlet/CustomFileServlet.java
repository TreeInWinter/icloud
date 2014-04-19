package com.icloud.framework.http.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;


public class CustomFileServlet extends DefaultServlet
{
  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (processRequest(request, response))
      super.doPost(request, response);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (processRequest(request, response))
      super.doGet(request, response);
  }

  public boolean processRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String requestURI = request.getRequestURI();
    String contextPath = request.getContextPath();

    String fileURI = "";
    if (requestURI.indexOf(contextPath) != -1)
    {
      fileURI = requestURI.substring(requestURI.indexOf(contextPath) + 
        contextPath.length());
    }

    File file = new File(request.getSession().getServletContext()
      .getRealPath(fileURI));
    String lowerfile = file.getAbsolutePath().toLowerCase();

    boolean isBasePath = "/".equalsIgnoreCase(fileURI);
    boolean isJSFile = lowerfile.endsWith(".js");
    boolean isCSSFile = lowerfile.endsWith(".css");
    boolean customServing = (!isBasePath) && ((isJSFile) || (isCSSFile));

    if (customServing) {
      String mimetype = request.getSession().getServletContext()
        .getMimeType(fileURI);
      response.setContentType(mimetype);

      String maxage = "86400";
      response.setHeader("Cache-Control", "max-age=" + maxage);
      long relExpiresInMillis = System.currentTimeMillis() + 
        1000L * Long.parseLong(maxage);
      response.setHeader("Expires", getGMTTimeString(relExpiresInMillis));
      response.setHeader("Last-Modified", 
        getGMTTimeString(file.lastModified()));

      FileInputStream fis = new FileInputStream(file);
      OutputStream ostream = response.getOutputStream();
      streamIO(fis, ostream);
      fis.close();
      ostream.flush();
      ostream.close();

      return false;
    }
    return true;
  }

  public static String getGMTTimeString(long milliSeconds) {
    SimpleDateFormat sdf = new SimpleDateFormat(
      "E, d MMM yyyy HH:mm:ss 'GMT'");
    return sdf.format(new Date(milliSeconds));
  }

  public static void streamIO(InputStream is, OutputStream os) throws IOException
  {
    byte[] bytes = new byte[2048];
    int readlen = -1;
    while ((readlen = is.read(bytes)) != -1) {
      os.write(bytes, 0, readlen);
      os.flush();
    }
  }
}