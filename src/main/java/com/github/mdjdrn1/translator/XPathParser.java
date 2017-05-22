package com.github.mdjdrn1.translator;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.mdjdrn1.translator.exceptions.ConnectionError;

import java.io.IOException;

class XPathParser {
    private final static WebClient client;

    static {
        client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    public static HtmlPage getHtmlPage(String url) throws ConnectionError {
        try {
            return client.getPage(url);
        } catch (IOException e) {
            return null;
        }
    }
}
