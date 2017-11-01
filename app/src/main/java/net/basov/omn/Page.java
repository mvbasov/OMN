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

import java.util.LinkedHashMap;

/**
 * Created by mvb on 10/10/17.
 */

public class Page {

    private LinkedHashMap<String, String> mMeta;
    private String mPageName;
    private String mInPageReference;
    private String mMdContent;
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

    public String getMetaHeaderAsString() {
        StringBuilder sb = new StringBuilder();
        for (String key: mMeta.keySet()){
            String metaName;
            if (key.equals("authors") && ! key.contains(","))
                metaName = "Author";
            else
                metaName = key.substring(0,1).toUpperCase() + key.substring(1);
            sb.append(metaName);
            sb.append(": ");
            sb.append(mMeta.get(key));
            sb.append("\n");
        }
        if(!this.mMeta.isEmpty())
            sb.append("\n");
        return sb.toString();
    }

    public LinkedHashMap<String,String> getPageMeta() {
        return mMeta;
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
        Boolean firstPage = true;
        String[] splitMd = mMdContent.split("\\n");
        for(String str: splitMd) {
            String trim = str.trim();
            String fName;
            String fContent;
            if (inHeader) {
                if (trim.length() == 0) {
                    inHeader = false;
                    if (firstPage) sb.append("\n");
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
                            case "date":
                            case "modified":
                            case "category":
                            case "tags":
                            case "slug":
                            case "lang":
                            case "summary":
                            case "authors":
                            case "author":
                            case "status":
                            case "keywords":
                                emptyHeader = false;
                                this.mMeta.put(fName, fContent);
                                break;
                            default:
                                addLog("Unknown pelican header string:>" + fName + "<");
                                inHeader = false;
                                sb.append(str).append("\n");
                        }
                    }
                } else {
                    sb.append(str).append("\n");
                }
            } else {
                sb.append(str).append("\n");
            }
            firstPage = false;
        }
        this.mMdContent = sb.toString();
    }

    public String getPageTitleOrName() {
        return this.mMeta.get("title") != null ? (String) this.mMeta.get("title") : mPageName;
    }

    public void setMetaModified(String mMetaModified) {
        this.mMeta.put("modified",mMetaModified);
    }

}
