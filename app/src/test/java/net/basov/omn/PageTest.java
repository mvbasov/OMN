/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mvb on 10/27/17.
 */
public class PageTest {

    private Page expectedPage;
    private Page realPage;

    public PageTest() {
        this.realPage = new Page("/some/Page#refer");
    }

    @Test
    public void testGetInPageReference() throws Exception {
        Assert.assertEquals(
                "#refer",
                this.realPage.getInPageReference()
        );
    }

    @Test
    public void testGetPageName() throws Exception {
        Assert.assertEquals(
                "/some/Page",
                this.realPage.getPageName()
        );
    }

    @Test
    public void testParseTitle() throws Exception {
        String mdHeader =
                "Title: title\n"
                        + "Date: 2017-10-10 10:10:10\n";
        String mdContent =
                "\n"
                        + "Note text.\n";

        this.realPage.setMdContent(mdHeader + mdContent);

        Assert.assertEquals(
                "title",
                this.realPage.getPageTitleOrName()
        );
    }

    @Test
    public void testOrdinaryPage() throws Exception {
        String mdHeader =
                "Title: title\n"
                + "Date: 2017-10-10 10:10:10\n"
                + "Author: Mikhail Basov\n"
                + "Keywords: test, test2\n\n";
        String mdContent = "Note text.\n";
        this.realPage.setMdContent(mdHeader + mdContent);

        Assert.assertEquals(
                mdHeader,
                this.realPage.getMetaHeaderAsString()
        );

        Assert.assertEquals(
                mdContent,
                this.realPage.getMdContent()
        );
    }

    @Test
    public void testEmptyHeaderPage() throws Exception {
        String mdContent = "Note text.\n";
        this.realPage.setMdContent(mdContent);

        Assert.assertEquals(
                mdContent,
                this.realPage.getMdContent()
        );

        Assert.assertEquals(
                "",
                this.realPage.getMetaHeaderAsString()
        );
    }

    @Test
    public void testEmptyHeaderAndFirstLinePage() throws Exception {
        String mdContent = "\nNote text.\n";
        this.realPage.setMdContent(mdContent);

        Assert.assertEquals(
                mdContent,
                this.realPage.getMdContent()
        );

        Assert.assertEquals(
                "",
                this.realPage.getMetaHeaderAsString()
        );
    }

    @Test
    public void testPageWithoutEndCR() throws Exception {
        String mdContent = "Note text.";
        this.realPage.setMdContent(mdContent);

        Assert.assertEquals(
                mdContent + "\n",
                this.realPage.getMdContent()
        );

        Assert.assertEquals(
                "",
                this.realPage.getMetaHeaderAsString()
        );
    }

    @Test
    public void testPageWithBrokenHeader() throws Exception {
        String mdHeader =
                "Title: title\n"
                + "Unknown: test\n"
                + "Date: 2017-10-10 10:10:10\n\n";
        String mdContent = "Note text.\n";
        this.realPage.setMdContent(mdHeader + mdContent);

        Assert.assertEquals(
                 "Unknown: test\n"
                + "Date: 2017-10-10 10:10:10\n\n"
                + mdContent,
                this.realPage.getMdContent()
        );

        Assert.assertEquals(
                "Title: title\n\n",
                this.realPage.getMetaHeaderAsString()
        );
    }

}