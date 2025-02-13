package com.finnacorp.service;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMapper {
    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>();

    static {
        CONTENT_TYPE_MAP.put("html", "text/html");
        CONTENT_TYPE_MAP.put("css", "text/css");
        CONTENT_TYPE_MAP.put("js", "application/javascript");
        CONTENT_TYPE_MAP.put("webp", "image/webp");
    }

    /**
     * Returns the content type for a given file extension.
     *
     * @param extension The file extension (e.g., "html", "css").
     * @return The corresponding content type, or "application/octet-stream" if not found.
     */
    public static String getContentType(String extension) {
        return CONTENT_TYPE_MAP.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }
}
