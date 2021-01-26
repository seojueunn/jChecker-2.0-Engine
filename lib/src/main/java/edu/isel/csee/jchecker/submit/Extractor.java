package edu.isel.csee.jchecker.submit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Extractor {

	
	public void unzip(String filepath, String output)
	{
		ZipFile zipfile = null;
		
		try {
			zipfile = new ZipFile(filepath, Charset.forName("EUC-KR"));
			Enumeration<? extends ZipEntry> e = zipfile.entries();
			
			
			while(e.hasMoreElements()) {
				ZipEntry entry = e.nextElement();
				File dest = new File(output, entry.getName());
				
				dest.getParentFile().mkdirs();
				
				if (entry.isDirectory())
					continue;
				
				else {
					BufferedInputStream bis = new BufferedInputStream(zipfile.getInputStream(entry));
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest), 1024);
					int len;
					byte[] buffer = new byte[1024];
					
					
					while( (len = bis.read(buffer, 0, 1024)) != -1)
						bos.write(buffer, 0, len);
					
					
					bis.close();
					bos.close();
				}
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("No zip file in the path: " + filepath);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Incorrect type or missed target.");
			e.printStackTrace();
		} finally {
			try {
				if (zipfile != null)
					zipfile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
