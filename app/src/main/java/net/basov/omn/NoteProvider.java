package net.basov.omn;
/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017-2024 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */



/**
 * Created by mvb on 24/12/22.
 */

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.basov.util.AppDetails;
import net.basov.util.FileIO;
import net.basov.util.MyLog;

/**
 * The custom file provider need to "wrap" file with footer/header.
 * This need to provide original file name of page when transfered
 * as file (Telegram, for example. Information about OMN version also provided.
 **/

public class NoteProvider extends FileProvider {
// some code used from here: https://stackoverflow.com/questions/27591756/android-contentprovider-openfile-need-to-serve-modified-file
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        MyLog.LogD("URI in openFile: " + uri.toString());
        MyLog.LogD("URI path: " + uri.getPath());
        // basic uri/mode check here
        File pageFile = new File(FileIO.getFilesDir(getContext()) + uri.getPath());
        return openPipeHelper(uri, getType(uri), null, new FileInputStream(pageFile), new PipeDataWriter<InputStream>() {
            @Override
            public void writeDataToPipe(ParcelFileDescriptor output, Uri uri, final String mimeType, Bundle opts, InputStream input) {

                InputStream fin = input;
                OutputStream fout = new ParcelFileDescriptor.AutoCloseOutputStream(output);
                byte[] buf = new byte[1024 * 1024];
                try {
                    fout.write(
                            (Constants.EMA_H_VER
                            + ": " + AppDetails.getAppName(getContext())
                            + "\n"
                            + Constants.EMA_H_PFN
                            + ": " + uri.getPath()
                                    .replaceFirst("^/md", "")
                                    .replaceFirst("\\.md$", "")
                            + "\n"
                            ).getBytes("UTF-8")
                    );

                    fout.write((Constants.EMA_MARK_START+"\n").getBytes("UTF-8"));
                    while (true) {
                        int n = fin.read(buf);
                        if (n == -1) break;
                        MyLog.LogI("openFile get n=" + n);
                        fout.write(buf, 0, n);
                        fout.flush();
                    }
                    fout.write((Constants.EMA_MARK_STOP+"\n").getBytes("UTF-8"));
                } catch (IOException | PackageManager.NameNotFoundException ex) {
                    // EPIPE likely means pipe closed on other end; treat it as WAI.
                    if (!ex.getMessage().contains("EPIPE")) {
                        MyLog.LogE(ex, "openFile failed");
                    }
                } finally {
                    try {
                        fin.close();
                    } catch (IOException ex) {
                        MyLog.LogW( ex,"openFile failed closing input");
                    }
                    try {
                        fout.close();
                    } catch (IOException ex) {
                        MyLog.LogW(ex,"openFile failed closing output");
                    }
                }
            }
        });
    }
}
