package proj.nccc.atsLog.batch.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.googlecode.compress_j2me.lzc.LZCOutputStream;

import lombok.extern.log4j.Log4j2;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import proj.nccc.atsLog.batch.util.compress.JdkZipCompress;

@Log4j2
public class FileUtil {

	public static boolean copy(String fileA, String fileB, boolean overwrite) {
		log.info("copy file \"" + fileA + "\" to \"" + fileB + "\"");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			fis = new FileInputStream(fileA);
			fos = new FileOutputStream(fileB, !overwrite);
			inChannel = fis.getChannel();
			outChannel = fos.getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (FileNotFoundException e) {
			log.error("File not found! \"" + fileA + "\"", e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error("closing file stream error! [" + e.getMessage() + "]", e);
			}
		}
		return false;
	}

	public static boolean zipFile(String sourcePath, String targetPath, String zipFileName) {
		log.info(String.format("sourcePath[%s], targetPath[%s], zipFileName[%s]", sourcePath, targetPath, zipFileName));
		ZipOutputStream zos = null;
		try {
			File source = new File(sourcePath);
			zos = new ZipOutputStream(new FileOutputStream(new File(targetPath, zipFileName)));
			if (source.isDirectory()) {
				for (File sourceFile : source.listFiles()) {
					checkFileType(sourceFile, zos, sourceFile.getName());
				}
			} else {
				addZipFile(source, zos, source.getName());
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (zos != null)
					zos.closeEntry();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void addZipFile(File sourceFile, ZipOutputStream zos, String fileName) throws IOException {
		byte[] b = new byte[128];
		FileInputStream fis = new FileInputStream(sourceFile);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);
		int i;
		while ((i = fis.read(b)) != -1) {
			zos.write(b, 0, i);
		}
		fis.close();
		zipEntry = null;
		b = null;
		return;
	}

	private static void checkFileType(File sourceFile, ZipOutputStream zos, String fileName) throws IOException {
		if (sourceFile.isDirectory()) {
			for (File file : sourceFile.listFiles()) {
				checkFileType(file, zos, fileName + "/" + file.getName());
			}
		} else {
			addZipFile(sourceFile, zos, fileName);
		}
	}

//	remark cause fortify : Path Manipulation: Zip Entry Overwrite
//	public static boolean unZipFile(String fileName) {
//		byte[] buffer = new byte[1024];
//		String folderName = fileName.substring(0, fileName.length() - 4);
//		ZipInputStream zis = null;
//		try {
//			File unZipFolder = new File(folderName);
//			if (!unZipFolder.exists())
//				unZipFolder.mkdirs();
//			zis = new ZipInputStream(new FileInputStream(fileName));
//			ZipEntry ze = zis.getNextEntry();
//			while (ze != null) {
//				if (ze.getName().indexOf("./") >= 0) {
//					throw new Exception("Path Manipulation: Zip Entry Overwrite");
//				}
//				File contentFile = new File(folderName + File.separator + ze.getName());
//				System.out.println("file unzip : " + contentFile.getAbsoluteFile());
//				if (ze.isDirectory()) {
//					File newDirectory = new File(contentFile.getAbsolutePath());
//					if (!newDirectory.exists()) {
//						newDirectory.mkdirs();
//					}
//				} else {
//					new File(contentFile.getParent()).mkdirs();
//					FileOutputStream fos = new FileOutputStream(contentFile);
//					int len;
//					while ((len = zis.read(buffer)) > 0) {
//						fos.write(buffer, 0, len);
//					}
//					fos.flush();
//					fos.close();
//				}
//				ze = zis.getNextEntry();
//			}
//			zis.closeEntry();
//			zis.close();
//			return true;
//		} catch (FileNotFoundException e) {
//			log.error("File not found. " + fileName, e);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}finally {
//			if (zis != null) {
//				try {
//					zis.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		return false;
//	}

	public static boolean ZFile(String sourcePath, String targetPath, String zipFileName) {
		boolean finalResult = false;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new BufferedInputStream(new FileInputStream(sourcePath), 1024);
			os = new LZCOutputStream(new BufferedOutputStream(new FileOutputStream(new File(targetPath, zipFileName))));
			IOUtils.copy(is, os);
		} catch (IOException e) {
			log.error("Compress Z file error.", e);
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
		return finalResult;
	}

	public static int writeToFile(String fileName, List<String> sources) throws IOException {
		log.info("Write file, save to " + fileName);
		try {
			Files.write(Paths.get(fileName), sources, Charset.defaultCharset(), new StandardOpenOption[] {
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE });
		} catch (IOException e) {
			log.error("Write File error. [" + fileName + "]", e);
			throw new IOException();
		}
		return 0;
	}

	public static boolean delete(String target) {
		return delete(target, true);
	}

	public static boolean delete(String target, boolean deleteDirectory) {
		try {
			File file = new File(target);
			if (file.isDirectory()) {
				if (file.list().length == 0) {
					return Files.deleteIfExists(Paths.get(target));
				} else {
					// as Directory
					Path targetPath = Files.walkFileTree(Paths.get(target), new deleteFileVisitor(deleteDirectory));
					if (Files.exists(targetPath, LinkOption.NOFOLLOW_LINKS)) {
						delete(targetPath.toString(), deleteDirectory);
					}
				}
			} else {
				// as file.
				Files.delete(Paths.get(target));
				return true;
			}
		} catch (IOException e) {
			log.error(String.format("delete file error! [%s]", target));
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return false;
	}

	public static class deleteFileVisitor extends SimpleFileVisitor<Path> {

		private final boolean deleteDirectory;

		public deleteFileVisitor(boolean deleteDirectory) {
			this.deleteDirectory = deleteDirectory;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.deleteIfExists(file);
			log.info("delete file:" + file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			if (deleteDirectory) {
				Files.deleteIfExists(dir);
				log.info("delete directory:" + dir);
			} else {
				log.info("Not delete directory...skip:" + dir);
			}
			if (exc != null)
				throw exc;
			return FileVisitResult.CONTINUE;
		}

	}

	public static byte[] fileToByteArray(File file) throws Exception {
		byte[] buffer = null;

		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} finally {
			if (null != bos) {
				bos.close();
			}
			if (null != fis) {
				fis.close();
			}
		}
		return buffer;
	}

	public static void bytesToFile(byte[] buffer, String filePath) throws Exception {
		IOUtils.write(buffer, new FileOutputStream(new File(filePath)));
	}
	
	/**
	 * - 解壓縮檔案
	 * @param destPath : folder
	 * @param fullZipFileName : 絕對路徑
	 * @param fileName
	 * @param isDelSource
	 * @throws ZipException
	 */
	public static void myDecompress(String destPath, String fullZipFileName, boolean isDelSource)
			throws ZipException {
		File zipFile = new File(fullZipFileName);
		File path = new File(destPath);
		log.debug("Decompress File : " + fullZipFileName + " ,path=" + destPath);
		(new JdkZipCompress()).decompress(zipFile, path);
		
		if (isDelSource) {
			zipFile.delete();
		}

	}
	
	/**
	 * - 轉為 bytes
	 * @param list
	 * @param encoding
	 * @param separator : \r\n
	 * @return
	 * @throws Exception
	 */
	public static byte[] listToBytes(List<String> list, String encoding, String separator) throws Exception {
		
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			for(String s : list) {
				bos.write(s.getBytes(encoding));
				if (separator != null) {
					bos.write(separator.getBytes());
				}
			}
			bos.flush();
			return bos.toByteArray();
		}finally {
			bos.close();
		}
		
	}
	
	public static void compress(String fullFileName, boolean isDelSource) throws ZipException {

		ZipFile zipFile = new ZipFile(fullFileName + ".zip");
		ArrayList<File> filesToAdd = new ArrayList<File>();
		filesToAdd.add(new File(fullFileName));
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		// parameters.setEncryptFiles(true);
		parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
		// 
		zipFile.addFiles(filesToAdd, parameters);
		
		if (isDelSource) {
			File delFile = null;
			delFile = new File(fullFileName);
			delFile.delete();
		}
	}
	
	public static Map<String, byte[]> unZip(String zipFileName, byte[] fileData) throws IOException {
		Map<String, byte[]> map = new HashMap<String, byte[]>();
		byte[] buffer = new byte[1024];

		// get the zip file content
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(fileData));

		// get the zipped file list entry
		ZipEntry ze;
		try {
			ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len;
				while ((len = zis.read(buffer)) > 0) {
					bos.write(buffer, 0, len);
				}
				bos.close();
				map.put(fileName, bos.toByteArray());
				ze = zis.getNextEntry();
			}
		} catch (IOException e) {
			log.error("zipUtil unzip failed.", e);
			throw e;
		} finally {

			try {
				zis.closeEntry();
				zis.close();
			} catch (IOException e) {
				log.error("close zipUtil failed.", e);
			}

		}

		return map;
	}

	public static byte[] zipFile(byte[] data, String fileName) throws IOException {

		ByteArrayOutputStream fos = new ByteArrayOutputStream(); // tempSavePath+rptFileName
		// open rpt
		ByteArrayInputStream fis = new ByteArrayInputStream(data);
		// create zip file and write rpt
		ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(fos));
		try {
			zOut.putNextEntry(new ZipEntry(fileName));
			zOut.setLevel(5);
			int byteNo;
			byte[] b = new byte[1024];
			while ((byteNo = fis.read(b)) > 0) {
				zOut.write(b, 0, byteNo);
			}
		} catch (IOException e) {
			log.error("zipUtil failed.", e);
			throw e;
		} finally {
			try {
				zOut.close();
				fis.close();
			} catch (IOException e) {
				log.error("close zipUtil failed.", e);
			}
			zOut = null;
			fis = null;
		}

		return fos.toByteArray();
	}
}
