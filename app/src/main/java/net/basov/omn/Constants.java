/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.basov.omn;

import android.os.Build;

/**
 * Created by mvb on 10/6/17.
 */

public class Constants {

    public final static int EDIT_PAGE_REQUEST = 333;
    public final static int PREFERENCES_REQUEST = 111;
    public static final String TAG = "#OMN#";

    public static final String WELCOME_PAGE = Build.VERSION.SDK_INT >=21
            ? "default/Welcome":"default/Welcome_legacy";
    public static final String HELP_PAGE = Build.VERSION.SDK_INT >=21
            ? "default/Help":"default/Help_legacy";
    public static final String START_PAGE = "Start";
    public static final String SYNTAX_PAGE = "default/Markdown";
    public static final String SYNTAX_EXT_PAGE = Build.VERSION.SDK_INT >=21
            ? "default/MarkdownExt":"default/MarkdownExt_legacy";
    public static final String CHANGELOG = "default/Changelog";
    public static final String BUILD_PAGE = "default/Build";
    public static final String QUICKNOTES_PAGE = "QuickNotes";
    public static final String COMON_CSS = "css/common.css";
    public static final String CUSTOM_CSS = "css/custom.css";
    public static final String HIGHLIGHT_CSS = "css/highlight.css";
    public static final String ICONS_FONT = "fonts/google-material/MaterialIcons-Regular.woff2";
    public static final String ICONS_CSS = "fonts/google-material/material-icons.css";
    public static final String FUNCTIONS_JS = "js/functions.js";
    public static final String GITIGNORE = "_gitignore";
    public static final String JS_DEBUG_PAGE = "default/JSDebug";
    public static final String EMA = "omn-platform-stat";
    public static final String EMA_DOM = "basov.net";
    public static final String EMA_MARK_START = "!-!-! the start of note";
    public static final String EMA_MARK_STOP = "!-!-! the end of note";
    public static final String EMA_H_VER = "OMNotesVersion";
    public static final String EMA_H_PFN = "PageFileName";
    public static final String EMA_H_SUBJECT = "Subject";
    public static final String EMA_H_SENT = "Sent";
    public static final String EMA_H_FROM = "From";
    public static final String EMA_H_TO = "To";
    public static final String INCOMING_INDEX_PAGE = "/incoming/Incoming";
    public static final String URL_INCOMING_PAGE = "bookmarks/Incoming";
    public static final String URL_INCOMING_CSS = "css/Bookmarker.css";
    public static final String URL_INCOMING_JS = "js/Bookmarker.js";
    public static final String URL_INCOMING_HELP_PAGE = "default/BookmarkerHelp";
}
