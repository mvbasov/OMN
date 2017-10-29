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

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by mvb on 10/10/17.
 */

public class Page {

    private LinkedHashMap<String, Object> mMeta;
    private String mPageName;
    private String mInPageReference;
    private String mMdContent;
    private String mHeaderMeta;
    private String mLog;


    public String getLog() {
        return mLog;
    }

    public void addLog(String mLog) {
        this.mLog = this.mLog + mLog + "\n";
    }

    public Page(String mPageName) {
        setPageName(mPageName);
        mMeta = new LinkedHashMap<>();
        mLog = "";
    }

    public String getHeaderMeta() {
        return mHeaderMeta;
    }

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
                    // TODO: *** (part 1 of 2)the following string is workarround to fix endless loop at new page creation
                    //sb.append("\n");
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
                                addLog("Unknown pelican header string:>" + fName + "<");
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
        // TODO: *** (part 2 of 2)the following string is workarround to fix endless loop at new page creation
        if(!emptyHeader) sb.append(" \n");
        this.mMdContent = sb.toString();
        this.mHeaderMeta = mdHeader;
    }

    public String getMetaTitle() {
        return this.mMeta.get("title") != null ? (String) this.mMeta.get("title") : mPageName;
    }

    public void setMetaTitle(String mMetaTitle) {
        this.mMeta.put("title", mMetaTitle);
        //this.mMetaTitle = mMetaTitle;
    }

    public String getMetaDate() {
        return (String) this.mMeta.get("date");
    }

    public void setMetaDate(String mMetaDate) {
        this.mMeta.put("date", mMetaDate);
    }

    public String getMetaModified() {
        return (String) this.mMeta.get("modified");
    }

    public void setMetaModified(String mMetaModified) {
        this.mMeta.put("modified",mMetaModified);
    }

    public String getMetaCategory() {
        return (String) this.mMeta.get("category");
    }

    public void addMetaCategory(String mMetaCategory) {
        this.mMeta.put("category", mMetaCategory);
    }

    public String[] getMetaTags() {
        return (String[]) this.mMeta.get("tags");
    }

    public void setMetaTags(String[] mMetaTags) {
        ArrayList<String> tags = new ArrayList<String>();
        for(String s : mMetaTags) {
            String s1 = s.trim();
            if (s1.length() > 0) {
                tags.add(s1);
            }

        }
        this.mMeta.put("tags", tags.toArray(new String[tags.size()]));
    }

    public String getMetaSlug() {
        return (String) mMeta.get("slug");
    }

    public void setMetaSlug(String mMetaSlug) {
        this.mMeta.put("slug", mMetaSlug);
    }

    public String getMetaLang() {
        return (String) mMeta.get("lang");
    }

    public void setMetaLang(String mMetaLang) {
        this.mMeta.put("lang", mMetaLang);
    }

    public String[] getMetaAuthors() {
        return (String[]) mMeta.get("authors");
    }

    public void setMetaAuthors(String[] mMetaAuthors) {
        this.mMeta.put("authors", mMetaAuthors);
    }

    public String getMetaSummary() {
        return (String) this.mMeta.get("summary");
    }

    public void setMetaSummary(String mMetaSummary) {
        this.mMeta.put("summary", mMetaSummary);
    }

    public String getMetaStatus() {
        return (String) this.mMeta.get("status");
    }

    public void setMetaStatus(String mMetaStatus) {
        this.mMeta.put("status", mMetaStatus);
    }

    public String[] getMetaKeywords() {
        return (String[]) this.mMeta.get("keywords");
    }

    public void setMetaKeywords(String[] mMetaKeywords) {
        this.mMeta.put("keywords", mMetaKeywords);
    }
}
