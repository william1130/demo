package proj.nccc.atsLog.batch.util.compress;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import proj.nccc.atsLog.batch.util.Util;
public class CommonsTarCompress extends Compress {
    @Override
    protected void doCompress(File srcFile, File destFile) throws IOException {
        TarArchiveOutputStream out = null;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(srcFile), bufferLen);
            out = new TarArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), bufferLen));
            TarArchiveEntry entry = new TarArchiveEntry(srcFile.getName());
            entry.setSize(srcFile.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(is, out);
            out.closeArchiveEntry();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }
    }
    @Override
    protected void doDecompress(File srcFile, File destDir) throws IOException {
        TarArchiveInputStream is = null;
        try {
            is = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile), bufferLen));
            TarArchiveEntry entry = null;
            while ((entry = is.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(destDir, Util.validFilePath(entry.getName()));
                    directory.mkdirs();
                } else {
                    BufferedOutputStream bos = null;
                    try {
                        byte[] buffer = new byte[bufferLen];
                        bos = new BufferedOutputStream(new FileOutputStream(
                                new File(destDir, Util.validFilePath(entry.getName()))), bufferLen);
                        int len;
                        long size = entry.getSize();
                        while (size > 0) {
                            if (size < bufferLen) {
                                len = is.read(buffer, 0, (int) size);
                                size -= len;
                            } else {
                                len = is.read(buffer);
                                size -= len;
                            }
                            bos.write(buffer, 0, len);
                        }
                    } finally {
                        IOUtils.closeQuietly(bos);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
