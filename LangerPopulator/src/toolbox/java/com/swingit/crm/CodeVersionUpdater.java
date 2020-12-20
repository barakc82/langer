package com.langer.crm;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class CodeVersionUpdater {

	private static final String crmClientMain = "src\\main\\java\\com\\swingit\\crm\\client\\CrmClient.java";

	public static void main(String [] args) 
			throws Exception {


		System.out.println(args[0]);
		String path = args[0] + "\\" + crmClientMain;

		BufferedReader reader = createReader(path);
		String line = "";
		String newText = "";
		while((line = reader.readLine()) != null)
		{
			String[] parts = line.split("public static final int version = ");
			if (parts.length == 2) {
				int version = extractCodeVersionFromLine(parts);
				System.out.println("Next version: " + ++version);
				newText += "\tpublic static final int version = " + version + ";\r\n";
			}
			else
				newText += line + "\r\n";
		}
		reader.close();
		
		FileWriter writer = new FileWriter(path);
        writer.write(newText);
        writer.flush();
        writer.close();
	}

	public static int extractCodeVersion() throws IOException 
	{
		return extractCodeVersion("..\\SwingItCrmClient\\" + crmClientMain);
	}
	
	public static int extractCodeVersion(String path) throws IOException 
	{
		BufferedReader reader = createReader(path);
		String line = "";
		while((line = reader.readLine()) != null)
		{
			String[] parts = line.split("public static final int version = ");
			if (parts.length == 2) {
				int version = extractCodeVersionFromLine(parts);
				reader.close();
				return version;
			}
		}
		reader.close();
		return 0;
	}

	private static BufferedReader createReader(String path) throws FileNotFoundException 
	{
		File file = new File(path);
		System.out.println(file.getAbsolutePath());
		BufferedReader reader = new BufferedReader(new FileReader(file));
		return reader;
	}

	private static int extractCodeVersionFromLine(String[] parts) {
		parts = parts[1].split(";");
		int version = Integer.valueOf(parts[0]);
		System.out.println("Current version: " + version);
		return version;
	}
}