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
    public void testSetMdContent() throws Exception {
        String mdHeader =
                "Title: title\n"
                + "Date: 2017-10-10 10:10:10\n";
        String mdContent =
                "\n"
                + "Note text.\n";
        this.realPage.setMdContent(mdHeader + mdContent);

        Assert.assertEquals(
                mdContent,
                this.realPage.getMdContent()
        );

        Assert.assertEquals(
                mdHeader,
                this.realPage.getHeaderMeta()
        );

        Assert.assertEquals(
                "title",
                this.realPage.getMetaTitle()
        );
    }

}