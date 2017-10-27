package net.basov.omn;

import net.basov.util.MyLog;

import java.util.ArrayList;

/**
 * Created by mvb on 10/10/17.
 */

public class Page {

    public Page(String mPageName) {
        setPageName(mPageName);
        mMetaCategory = new ArrayList<String>();
    }

    private String mPageName;
    private String mInPageReference;
    private String mMdContent;
    private String mMetaTitle;
    private String mMetaDate;
    private String mMetaModified;
    private ArrayList<String> mMetaCategory;
    private String[] mMetaTags;
    private String mMetaSlug;
    private String mMetaLang;
    private String[] mMetaAuthors;
    private String mMetaSummary;
    private String mMetaStatus;
    private String[] mMetaKeywords;

    public String getHeaderMeta() {
        return mHeaderMeta;
    }

    private String mHeaderMeta;

    public boolean appendOnNoteTop(String mPageName, String header) {
        setMdContent(header + getMdContent());
        return true;
    }

    public String getPageName() {
        return mPageName;
    }

    public void setPageName(String mPageName) {
        if (null != mPageName && mPageName.length() > 0 ) {
            int endIndex = mPageName.lastIndexOf("#");
            if (endIndex != -1) {
                this.mPageName = mPageName.substring(0, endIndex);
                this.mInPageReference = mPageName.substring(endIndex);
            } else {
                this.mPageName = mPageName;
                this.mInPageReference = "";	   
			}
        }
	}

    public String getInPageReference() {
        return mInPageReference;
    }

    public String getMdContent() {
        return mMdContent;
    }

    public void setMdContent(String mMdContent) {
        StringBuilder sb = new StringBuilder();
        Boolean inHeader = true;
        Boolean emptyHeader = true;
        String mdHeader = "";
        String[] splitMd = mMdContent.split("\\n");
        for(String str: splitMd) {
            String trim = str.trim();
            String fName;
            String fContent;
            if (inHeader) {
                if (trim.length() == 0) {
                    inHeader = false;
                    continue;
                }
                String[] filds = trim.split(":", 2);
                if (filds.length == 2) {
                    fName = filds[0].trim().toLowerCase();
                    fContent = filds[1].trim();
                    if (fName != null
                            && fContent != null
                            && fName.length() != 0
                            && fContent.length() != 0
                            ) {
                        switch (fName) {
                            case "title":
                                setMetaTitle(fContent);
                                emptyHeader = false;
                                break;
                            case "date":
                                setMetaDate(fContent);
                                emptyHeader = false;
                                break;
                            case "modified":
                                emptyHeader = false;
                                setMetaModified(fContent);
                                break;
                            case "category":
                                emptyHeader = false;
                                addMetaCategory(fContent);
                                break;
                            case "tags":
                                emptyHeader = false;
                                setMetaTags(fContent.split(","));
                                break;
                            case "slug":
                                emptyHeader = false;
                                setMetaSlug(fContent);
                                break;
                            case "lang":
                                emptyHeader = false;
                                setMetaLang(fContent);
                                break;
                            case "summary":
                                emptyHeader = false;
                                setMetaSummary(fContent);
                                break;
                            case "authors":
                                emptyHeader = false;
                                setMetaAuthors(fContent.split(","));
                                break;
                            case "author":
                                emptyHeader = false;
                                String[] s = new String[1];
                                s[0] = fContent;
                                setMetaAuthors(s);
                                break;
                            case "status":
                                emptyHeader = false;
                                setMetaStatus(fContent);
                                break;
                            case "keywords":
                                emptyHeader = false;
                                setMetaKeywords(fContent.split(","));
                                break;
                            default:
                                MyLog.LogE("Unknown pelican header string:>" + fName + "<");
                                inHeader = false;
                                sb.append(str).append("\n");
                        }
                        if(inHeader) {
                            mdHeader += str + "\n";
                        }
                    }
                } else {
                    sb.append(str).append("\n");
                }
            } else {
                sb.append(str).append("\n");
            }
        }
        if(!emptyHeader) sb.append(" \n");
        this.mMdContent = sb.toString();
        this.mHeaderMeta = mdHeader;
    }

    public String getMetaTitle() {
        return mMetaTitle != null ? mMetaTitle : mPageName;
    }

    public void setMetaTitle(String mMetaTitle) {
        this.mMetaTitle = mMetaTitle;
    }

    public String getMetaDate() {
        return mMetaDate;
    }

    public void setMetaDate(String mMetaDate) {
        this.mMetaDate = mMetaDate;
    }

    public String getMetaModified() {
        return mMetaModified;
    }

    public void setMetaModified(String mMetaModified) {
        this.mMetaModified = mMetaModified;
    }

    public String[] getMetaCategory() {
        return mMetaCategory.toArray(new String[mMetaCategory.size()]);
    }

    public void addMetaCategory(String mMetaCategory) {
        this.mMetaCategory.add(mMetaCategory);
    }

    public String[] getMetaTags() {
        return mMetaTags;
    }

    public void setMetaTags(String[] mMetaTags) {
        ArrayList<String> tags = new ArrayList<String>();
        for(String s : mMetaTags) {
            String s1 = s.trim();
            if (s1.length() > 0) {
                tags.add(s1);
            }

        }
        this.mMetaTags = tags.toArray(new String[tags.size()]);
    }

    public String getMetaSlug() {
        return mMetaSlug;
    }

    public void setMetaSlug(String mMetaSlug) {
        this.mMetaSlug = mMetaSlug;
    }

    public String getMetaLang() {
        return mMetaLang;
    }

    public void setMetaLang(String mMetaLang) {
        this.mMetaLang = mMetaLang;
    }

    public String[] getMetaAuthors() {
        return mMetaAuthors;
    }

    public void setMetaAuthors(String[] mMetaAuthors) {
        this.mMetaAuthors = mMetaAuthors;
    }

    public String getMetaSummary() {
        return mMetaSummary;
    }

    public void setMetaSummary(String mMetaSummary) {
        this.mMetaSummary = mMetaSummary;
    }

    public String getMetaStatus() {
        return mMetaStatus;
    }

    public void setMetaStatus(String mMetaStatus) {
        this.mMetaStatus = mMetaStatus;
    }

    public String[] getMetaKeywords() {
        return mMetaKeywords;
    }

    public void setMetaKeywords(String[] mMetaKeywords) {
        this.mMetaKeywords = mMetaKeywords;
    }
}
