/*
Open Markdown Notes (android application to take and organize everydays notes)

Copyright (C) 2017 Mikahil Basov mikhail[at]basov[dot]net

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
                this.realPage.getMetaTitle()
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
                mdContent,
                this.realPage.getMdContent()
        );

        Assert.assertEquals(
                mdHeader,
                this.realPage.getHeaderMeta()
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
                this.realPage.getHeaderMeta()
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
                this.realPage.getHeaderMeta()
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
                this.realPage.getHeaderMeta()
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
                "Title: title\n",
                this.realPage.getHeaderMeta()
        );
    }

}