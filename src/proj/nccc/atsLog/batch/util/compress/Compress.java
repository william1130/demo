package proj.nccc.atsLog.batch.util.compress;
import java.io.File;
import java.io.IOException;
public abstract class Compress {
    public void compress(File srcFile, File destFile) {
        destFile.getParentFile().mkdirs();
        try {
            doCompress(srcFile, destFile);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void decompress(File srcFile, File destDir) {
        destDir.mkdirs();
        try {
            doDecompress(srcFile, destDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    protected int bufferLen = 1024 * 1024;//buffer size: 1MByte
    protected abstract void doCompress(File srcFile, File destFile) throws IOException;
    protected abstract void doDecompress(File srcFile, File destDir) throws IOException;
}