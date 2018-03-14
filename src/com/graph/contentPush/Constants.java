package com.graph.contentPush;

public class Constants {
	public final static String clientId = "CLIENT-ID";
	public final static String clientSecret = "CLIENT-SECRET";
	public final static String tenant = "TENANT";
	public final static String username = "EMAIL";
	public final static String password = "PASSWORD";
	public final static String htmlContent = "<!DOCTYPE html>\r\n" +
            "<html lang=\"en-US\">\r\n" +
            "<head>\r\n" +
            "<title>Recent Content from OneDrive</title>\r\n" +
            "<meta name=\"created\" content=\"2001-01-01T01:01+0100\">\r\n" +
            "</head>\r\n" +
            "<body>\r\n" +
            "<p>\r\n" +
            "[replace-content]" +
            "<img src=\"name:bike\" />\r\n" +
            "</p>\r\n" +
            "<object data=\"name:hardware-request-process\" data-attachment=\"hardware-request-process.pdf\" /></p>\r\n" +
            "<p>\r\n" +
            "\r\n" +
            "</body>\r\n" +
            "</html>";
}
