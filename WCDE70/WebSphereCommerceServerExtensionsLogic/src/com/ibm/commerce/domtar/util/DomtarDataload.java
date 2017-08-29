package com.ibm.commerce.domtar.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Find the CSV file which needs to be processed and then process it. 
 *
 * @version		initial
 */
public class DomtarDataload {

	/**
	 * Read the file which contains the name of CSVs to be uploaded and sort it. 
	 * 
	 * @return Sorted list of file names to be uploaded.
	 */
	public List<String> getSortedCSVToBeUploadedFileList(String orderSystem, String path) {
		BufferedReader br = null;
		List<String> fileList = null;
		try {
			String sCurrentLine;
			String ToBeUploadedFileName = path+orderSystem+"_filesToBeUploaded.txt";
			br = new BufferedReader(new FileReader(ToBeUploadedFileName));
			fileList = new ArrayList<String>();
			while ((sCurrentLine = br.readLine()) != null) {
				fileList.add(sCurrentLine);
			}
			Collections.sort(fileList);

		} catch (IOException e) {
			System.err.println("[DomtarDataload.getSortedCSVToBeUploadedFileList] Error reading file: " + e.getMessage());
		} finally {
			closeIOObject(br, null);
		}

		return fileList;
	}

	/**
	 * Get the name of last file which was processed successfully.
	 * 
	 * @return Processed file name.
	 */
	public String getLastFileSuccessfullyUploaded(String orderSystem, String path) {
		BufferedReader br = null;
		List<String> fileList = null;
		String lastElement = null;
		try {
			String sCurrentLine;
			String UploadedFileName = path+orderSystem+"_uploadedFiles.txt";
			br = new BufferedReader(new FileReader(UploadedFileName));
			fileList = new ArrayList<String>();
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				fileList.add(sCurrentLine);
			}
			Collections.sort(fileList);
			if(fileList.size()>0)
				lastElement = fileList.get(fileList.size() - 1);

		} catch (IOException e) {
			System.err.println("[DomtarDataload.getLastFileSuccessfullyUploaded] Error reading file: " + e.getMessage());
		} finally {
			closeIOObject(br, null);
		}

		return lastElement;
	}
	
	/**
	 * Append the file name to successfully uploaded list.
	 * 
	 * @param fileName file name to be appended.
	 */
	public void appendToSuccessfullyUploadedList(String orderSystem, String fileName, String path) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path+orderSystem+"_uploadedFiles.txt", true));
			writer.append(new String("\n") + fileName);
			writer.close();
			
		} catch (IOException e) {
			System.err.println("[DomtarDataload.appendToSuccessfullyUploadedList] Error appending filename: " + e.getMessage());
		} finally {
			closeIOObject(null, writer);
		}
	}
	
	/**
	 * Close the opened I/O objects. 
	 * 
	 * @param br <object>BufferedReader</object>
	 */
	private void closeIOObject(BufferedReader reader, BufferedWriter writer) {
		
		// Close BufferedReader if open
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException ex) {
				System.err.println("[DomtarDataload.closeIOObject] Error closing buffered reader: " + ex.getMessage());
			}
		}// if
		
		// Close BufferedWriter	if open	
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException ex) {
				System.err.println("[DomtarDataload.closeIOObject] Error closing buffered writer: " + ex.getMessage());
			}
		}// if
	}
}
