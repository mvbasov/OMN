package net.basov.omn;
/*
Open Markdown Notes (android application to take and organize everyday notes)

Copyright (c) 2017-2022 Mikhail Basov (https://github.com/mvbasov/OMN)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */



/**
 * Created by mvb on 24/12/22.
 */

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.basov.util.MyLog;

public class NoteProvider extends FileProvider implements ContentProvider.PipeDataWriter<InputStream> {
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Don't support deletes.
        throw new UnsupportedOperationException("No external delete");
    }

    @Override
    public void writeDataToPipe(ParcelFileDescriptor output, Uri uri, String mimeType,
                                Bundle opts, InputStream args) {
        // Transfer data from the asset to the pipe the client is reading.
        byte[] buffer = new byte[8192];
        int n;
        FileOutputStream fout = new FileOutputStream(output.getFileDescriptor());
        try {
            fout.write("!-!-!\n".getBytes("UTF-8"));
            while ((n=args.read(buffer)) >= 0) {
                fout.write(buffer, 0, n);
            }
            fout.write("!-!-!\n".getBytes("UTF-8"));
        } catch (IOException e) {
            MyLog.LogI(e,"Note Failed transferring");
        } finally {
            try {
                args.close();
            } catch (IOException e) {
            }
            try {
                fout.close();
            } catch (IOException e) {
            }
        }
    }

    /*
    // Code from https://g00se.org/2019/08/android-content-provider.html
    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        PipeDataWriter pipeDataWriter = new PipeDataWriter<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void writeDataToPipe(@NonNull ParcelFileDescriptor output, @NonNull Uri uri, @NonNull String mimeType, @Nullable Bundle opts, @Nullable String args) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(output.getFileDescriptor())) {
                    //TODO: Write your actual data
                    byte[] data = new byte[]{(byte) 255, (byte) 255, (byte) 255};
                    fileOutputStream.write(data);
                } catch (IOException e) {
                    MyLog.LogW(e, "there occurred an error while sharing a file: ");
                }
            }
        };

        return openPipeHelper(uri, getType(uri), null, null, pipeDataWriter);
    }

     */
}
