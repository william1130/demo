package proj.nccc.atsLog.batch.util.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.googlecode.compress_j2me.lzc.LZCInputStream;
import com.googlecode.compress_j2me.lzc.LZCOutputStream;
public class UnixZ_LZCCompress extends Compress {
    @Override
    protected void doCompress(File srcFile, File destFile) throws IOException {
        OutputStream out = null;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(srcFile), bufferLen);
            out = new LZCOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), bufferLen));
            IOUtils.copy(is, out);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }
    }
    @Override
    protected void doDecompress(File srcFile, File destDir) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.toString()));
            is = new LZCInputStream(new BufferedInputStream(new FileInputStream(srcFile), bufferLen));
            os = new BufferedOutputStream(new FileOutputStream(destFile), bufferLen);
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }
}
