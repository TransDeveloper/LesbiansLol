package com.finnacorp.service;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;

public class JettyServer implements Runnable {
    private final int port;
    private final Server server;
    private final Logger logger;

    public JettyServer(int port) {
        this.port = port;
        this.server = new Server(port);
        this.logger = new Logger("Jetty");
    }

    static {
        // Redirect Jetty logs to SLF4J
        try {
            Log.setLog(new Slf4jLog());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set Jetty log to SLF4J", e);
        }
    }

    @Override
    public void run() {
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        server.setSendServerVersion(false);

        handler.addServletWithMapping(DownloadServlet.class, "/*");

        try {
            server.start();
            logger.Log("Server is running and accepting connections.");
            server.join();
        } catch (BindException e) {
            logger.Log(String.format("ERROR-> Port %d is already in use", port));
            System.exit(1);
        } catch (Exception e) {
            logger.Log(String.format("ERROR-> Failed to start Jetty server: %s", e));
            System.exit(1);
        }
    }

    @SuppressWarnings("serial")
    public static class DownloadServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String filename = getFilenameFromRequest(request);
            try (InputStream in = getFileResource(filename)) {
                if (in != null) {
                    serveFile(response, filename, in);
                } else {
                    serveNotFound(response);
                }
            }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request, response);
        }

        private String getFilenameFromRequest(HttpServletRequest request) {
            String filename = request.getRequestURI().substring(1);
            if (filename.isEmpty()) return "index.html";
            if (filename.startsWith("getFrame/")) {
                filename = String.format("yuri/frames/yuri_%s.webp", filename.replace("getFrame/", ""));
            }
            return filename;
        }

        private void serveFile(HttpServletResponse response, String filename, InputStream in) throws IOException {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(ContentTypeMapper.getContentType(getFileExtension(filename)));
            writeStreamToOutput(in, response.getOutputStream());
        }

        private void serveNotFound(HttpServletResponse response) throws IOException {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType(ContentTypeMapper.getContentType("html"));
            try (InputStream in = getFileResource("404.html")) {
                writeStreamToOutput(in, response.getOutputStream());
            }
        }
    }

    private static InputStream getFileResource(String filename) {
        String filepath = String.format("static/%s", filename);
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath);
    }

    private static void writeStreamToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
    }

    private static String getFileExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return index != -1 ? filename.substring(index + 1) : "";
    }
}
