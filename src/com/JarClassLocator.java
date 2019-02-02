package com;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarClassLocator {
	public static void scan(String[] args) {
		File dir = getDir(args);
		ArrayList<File> jars = getJarsFromDir(dir);

		for(File jar : jars) {
			printAllClassesInsideJar(jar);
		}
	}
	
	public static ArrayList<String> getAllFileInsideStream(InputStream fis) {
		ArrayList<String> fileRelativePaths = new ArrayList<String>();
		try {
			ZipInputStream zip = new ZipInputStream(fis);
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				fileRelativePaths.add(entry.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileRelativePaths;
	}
	
	private static void printAllClassesInsideJar(File jar) {
		List<String> classNames = new ArrayList<String>();
		System.out.println("Listing inside: "+jar.getName());
		try {
			ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				System.out.println(entry.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static File getDir(String[] args) {
		String dirStr = "";
		if (args.length != 1) {
			System.out.print("Enter directory where to look: ");
			Scanner sc = new Scanner(System.in);
			dirStr = sc.nextLine().trim();
		} else if (args.length == 1) {
			dirStr = args[0];
		} else {
			System.out.println("can't take more than one argument");
			System.exit(0);
		}
		File dir = new File(dirStr);
		if (!dir.exists()) {
			System.out.println("'" + dirStr + "'" + " dosn't exist");
			System.exit(0);
		}
		if (!dir.isDirectory()) {
			System.out.println("'" + dirStr + "'" + " is not directory");
			System.exit(0);
		}
		return dir;
	}

	private static ArrayList<File> getJarsFromDir(File dir) {
		ArrayList<File> files = new ArrayList<File>();
		File[] fs = dir.listFiles();
		for(int i=0;i<fs.length;i++) {
			File f = fs[i];
			if(!f.isDirectory()) {
				String fName = f.getName();
				if(fName.length() - fName.indexOf(".jar") == 4) {
					files.add(f);
				}
			}
		}
		return files;
	}
}
