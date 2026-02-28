package proj.nccc.atsLog.batch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DecodeUtil {
	/**
	 * dec filename would be: decFileName.randString ex:
	 * cfg/db.properties.1234567890(hhmmss)
	 * 
	 * @param encFileName : input encryption File name, ex: db.properties.Z
	 * @param decFileName : output decryption File name, ex: db.properties
	 * @param privateKey  : NCCCPRI2
	 * @param keyType     : 2(RSA2048+DES2) or 3(RSA4096+AES128) // M2023089 支援AES演算法
	 * @param randString  : a rand number for multi running
	 * @return ./encrypt.sh cfg/db.properties cfg/db.properties.Z NCCCPUB2 2
	 *         ./decrypt.sh cfg/db.properties.Z cfg/db.properties.xyz NCCCPRI2 2
	 */
	private String fileName;
	private String extFileName;

	public DecodeUtil() {
		String AB = "0123456789ABCDEF";
		SecureRandom rnd = new SecureRandom();
		StringBuffer sb = new StringBuffer(6);
		for (int i = 0; i < 6; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		extFileName = sb.toString();
	}
	
	/**
	 * 刪除該檔案所在的目錄的暫存檔案
	 * @param file name : encFileName
	 * @param srcFileName
	 */
	private void deleteTempFile(String encFileName, String srcFileName) {
		
		Date deleteTime = MyDateUtil.nextTime(new Date(), 10, "BEFORE", "MINUTE");
		String encFolder = new File(encFileName).getParentFile().getAbsolutePath();
		File srcFile = new File(srcFileName);
		String srcFullPath = srcFile.getAbsolutePath();
		
		File dir = new File(encFolder);
		File [] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// System.out.println(name);
				if (name.endsWith(".Z") || name.endsWith(".properties")) {
					return false;
				}
				try {
					BasicFileAttributes fa = Files.readAttributes(Paths.get(dir.getAbsolutePath() + File.separator + name), BasicFileAttributes.class,
							LinkOption.NOFOLLOW_LINKS);
					Date fileTime = new Date(fa.lastModifiedTime().toMillis());
					String targetFile = dir.getAbsolutePath() + File.separator + name;
					
					if (fileTime.before(deleteTime) && 
							targetFile.startsWith(srcFullPath) && !targetFile.equals(srcFullPath)
							) {
						String[] s1 = targetFile.substring(srcFullPath.length()).split("\\.");
						for(String h : s1) {
							// -- 判斷是否含有6碼的亂數數字
							if (Pattern.matches("[A-F0-9]{6}", h)) {
								log.info(name + " to be delete");
								return true;
							}
						}
					}
					return false;
				}catch(Exception x) {
					log.error(x);
					return false;
				}
			}
		});
		
		for(File f : files) {
			try {
				log.info("delete temp file : " + f.getAbsolutePath());
				f.delete();
			}catch(Exception x) {
				log.error(x);
			}
		}
	}

	/**
	 * @param encFileName : db.properties.Z
	 * @param decFileName : db.properties
	 * @return : real file name:db.properties.123456
	 */
	public String execDecode(String encFileName, String decFileName) {
		// ---------------------------------------------------------
		// -- 刪除因為 timeout (decrypt.sh) 殘留的檔案(30 分鐘之前)
		this.deleteTempFile(encFileName, decFileName);

		fileName = decFileName + "." + extFileName;
		File fileOrg = new File(encFileName);
		// 若 .Z 存在且 size >1 則進行解密,
		// 否則不進行解密直接用明文檔.
		if (fileOrg.exists() && fileOrg.length() > 1) {
			try {
				String theShellCommand = "./decrypt.sh " + encFileName + " " + fileName;
				String s = "";
				try {
					Process p = Runtime.getRuntime().exec(theShellCommand);
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					DecryptProcess dProc = new DecryptProcess(p);
					dProc.start();
					try {
						if (encFileName.startsWith("AUTHLOGDATA")) {
							dProc.join(1000 * 60 * 5); // 5min timeout, file size 132014016 = 125MB
						} else {
							dProc.join(45000); // 45sec timeout
						}
						if (dProc.exit == null) {
							throw new TimeoutException(new Date() + " / NMIP解密逾時超過45秒!");
						}
						while ((s = stdInput.readLine()) != null) {
							System.out.println("StdOut:" + s);
						}
						while ((s = stdError.readLine()) != null) {
							System.out.println("StdError:" + s);
						}
					} catch (InterruptedException ex) {
						dProc.interrupt();
						Thread.currentThread().interrupt();
//						System.exit(-1);
						throw ex;
					} finally {
						p.destroy();
						p = null;
					}
					return fileName;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
					return null;
				}
			} catch (Exception x) {
				x.printStackTrace();
				System.out.println(x);
				return null;
			}
		} else {
			// .Z , not exist.
			String srcFileName = encFileName.replaceAll(".Z", "");
			System.out.println("srcFileName=" + srcFileName + ",fileName=" + fileName);
			this.copyfile(srcFileName, fileName);
			System.out.println(
					new java.util.Date() + " .Z not exist, copy SrcFile to DestFine by direct #### " + fileName);
			return fileName;
		}
	}

	/**
	 * @param decFileName : db.properties
	 * @return : real file name:db.properties.Z
	 */
	public String execEncode(String decFileName) {
		
		fileName = decFileName;
		File fileOrg = new File(decFileName);
		// 若 .Z 存在且 size >1 則進行解密,
		// 否則不進行解密直接用明文檔.
		if (fileOrg.exists() && fileOrg.length() > 1) {
			try {
				String theShellCommand = "./encrypt.sh " + decFileName + " " + fileName + ".Z ";
				String s = "";
				try {
					Process p = Runtime.getRuntime().exec(theShellCommand);
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					DecryptProcess dProc = new DecryptProcess(p);
					dProc.start();
					try {
						if (decFileName.startsWith("AUTHLOGDATA")) {
							dProc.join(1000 * 60 * 5); // 5min timeout, file size 132014016 = 125MB
						} else {
							dProc.join(30000); // 30sec timeout
						}
						if (dProc.exit == null) {
							throw new TimeoutException(new Date() + " / NMIP解密逾時超過30秒!");
						}
						while ((s = stdInput.readLine()) != null) {
							System.out.println("StdOut:" + s);
						}
						while ((s = stdError.readLine()) != null) {
							System.out.println("StdError:" + s);
						}
					} catch (InterruptedException ex) {
						dProc.interrupt();
						Thread.currentThread().interrupt();
//						System.exit(-1);
						throw ex;
					} finally {
						p.destroy();
						p = null;
					}
//	                p.destroy();
//	                p = null;
					return fileName;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
					return null;
				}
			} catch (Exception x) {
				x.printStackTrace();
				System.out.println(x);
				return null;
			}
		} else {
			// file , not exist.
			String srcFileName = decFileName.replaceAll(".Z", "");
			System.out.println("srcFileName=" + srcFileName + ",fileName=" + fileName);
			this.copyfile(srcFileName, fileName);
			System.out.println(
					new java.util.Date() + " file not exist, copy SrcFile to DestFine by direct #### " + fileName);
			return fileName;
		}
	}

	public boolean deleteFile() {

		File file = new File(this.fileName);
		file.delete();
		return true;
	}

	private void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage() + " in the specified directory.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private static class DecryptProcess extends Thread {
		private final Process p;
		private Integer exit;

		private DecryptProcess(Process p) {
			this.p = p;
		}

		public void run() {
			try {
				exit = p.waitFor();
			} catch (InterruptedException ignore) {
				return;
			}
		}
	}
	
	public void removeFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.delete()) {
				log.info(file.getName() + " is deleted!");
			} else {
				log.error(filePath + " delete operation is failed.");
			}
		} catch (Exception e) {
			log.error(filePath + " delete operation is failed.");
		}
	}

	/**
	 * 檔案加密
	 * @param sourceFileName
	 * @param targetFileName
	 * @param key
	 * @param keyType
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void encrypt(String sourceFileName, String targetFileName, String key, String keyType)
			throws InterruptedException, IOException {
		String s = "./encrypt.sh " + sourceFileName + " " + targetFileName + " " + key + " " + keyType;
		Process p = Runtime.getRuntime().exec(s);
		int exitVal = p.waitFor();
		if (exitVal != 0)
			throw new IOException("Encrypt file failed");

	}

	/**
	 * 檔案解密
	 * @param sourceFileName
	 * @param targetFileName
	 * @param key
	 * @param keyType
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void decrypt(String sourceFileName, String targetFileName, String key, String keyType)
			throws IOException, InterruptedException {
		String s = "./decrypt.sh " + sourceFileName + " " + targetFileName + " " + key + " " + keyType;
		Process p = Runtime.getRuntime().exec(s);
		int exitVal = p.waitFor();
		if (exitVal != 0)
			throw new IOException("Decrypt file failed");
	}
}