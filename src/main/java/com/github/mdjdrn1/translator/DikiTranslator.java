package com.github.mdjdrn1.translator;

import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DikiTranslator implements Translator {
    private static final String websiteURL = "https://www.diki.pl/";

    @Override
    public String translate(String word) {
        String wordAdaptedToURL = adaptWordToURL(word);
        String wordWebsiteURL = websiteURL + wordAdaptedToURL;

        HtmlPage page = XPathParser.getHtmlPage(wordWebsiteURL);
        if (page == null)
            return null;

        HtmlMeta meta = page.getFirstByXPath("//meta[@name='description']");

        if (meta == null)
            return null;

        String content = meta.getContentAttribute();

        String invalidWordPartialContent = "zdania po angielsku, wymowa";
        String validWordDelimiter = " - ";
        String newWord;
        if (!content.contains(invalidWordPartialContent)) {
            newWord = content.substring(content.lastIndexOf(validWordDelimiter) + validWordDelimiter.length());
            if (newWord.contains(";")) {
                newWord = newWord.substring(0, newWord.indexOf(";"));
            }
        } else {
            newWord = null;
        }

        return newWord;
    }

    private String adaptWordToURL(String word) {
        return word.trim().replaceAll(" ", "+");
    }
}
