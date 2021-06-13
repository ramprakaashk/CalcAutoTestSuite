package com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  

import org.apache.commons.collections4.ResettableIterator;
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;  
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;  

import java.io.File;  
import java.io.FileInputStream;  
import java.util.Iterator;  

import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
public class TestCalcAPI2 {
	
	static String str;
	static int num1;
	static int num2;
	static int expResult;
	public static int rowCount = 0;
	private static final String USER_AGENT = "Mozilla/5.0";

	//private static final String GET_URL = "https://localhost:8080/Calc";
	//private static String url1 = "http://localhost:8080/calc";

	public static void main(String[] args) throws IOException {
		/*String str = sendGet("mul",450,45);
		System.out.println("The API response outside is => "+str);
		String input = str;
		input = input.substring(input.indexOf(":")+1, input.lastIndexOf(","));
		System.out.println("Result => "+input);*/
		processExcel();
		validateResults();
	}

	private static void validateResults(){
		
		
		
	}
	
	
	
	private static void processExcel() throws IOException{
		
		//obtaining inputstream obj from the file  
		FileInputStream fis=new FileInputStream(new File("D:\\Innovation\\Calctest.xlsx"));  
		
		// Create workbook object
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); 
		CellStyle style = myWorkBook.createCellStyle();
		CellStyle style1 = myWorkBook.createCellStyle(); 
		// Return first sheet 
		XSSFSheet mySheet = myWorkBook.getSheetAt(0); 
		
		Cell cellobj = null;
		Cell cellobjResult = null;
		Cell cellobjTeststatus = null;
		// Get iterator 
		Iterator<Row> rowIterator = mySheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum()!=0){
				rowCount=rowCount+1;
				System.out.println("rowCount is "+rowCount);
				cellobj = mySheet.getRow(rowCount).getCell(4);
				cellobjResult = mySheet.getRow(rowCount).getCell(5);
				cellobjTeststatus = mySheet.getRow(rowCount).getCell(6);
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator(); 
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next(); 
					if (cell.getColumnIndex() == 0 || cell.getColumnIndex() == 1 || cell.getColumnIndex() == 2 || cell.getColumnIndex() == 3  )
					{
							
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING: 
							//System.out.print(cell.getStringCellValue() + "\t"); 
							str = null;
							str = cell.getStringCellValue();
							break; 
						case Cell.CELL_TYPE_NUMERIC: 
							//System.out.print(cell.getNumericCellValue() + "\t"); 
							//System.out.println("Cell counter is "+cell.getColumnIndex());
							if (cell.getColumnIndex()==0){
								num1=0;
								num1 = (int)cell.getNumericCellValue();
								break; 
							}
							if (cell.getColumnIndex()==1){
								num2=0;
								num2 = (int)cell.getNumericCellValue();
								break; 
							}
							if (cell.getColumnIndex()==3){
								expResult=0;
								System.out.println("Inside cell to read exp result");
								expResult = (int)cell.getNumericCellValue();
								System.out.println("Expected val from excel is "+expResult+" and column is "+cell.getColumnIndex());
								break; 
							}
							default : 
						}
					}
					// end of If condition for getting cell inputs
					//start to write the cell
					if (cell.getColumnIndex()==4){
						/*System.out.println("Params are ---->"+num1+"  "+num2+" "+str);
						String apiResponse =sendGet(str,num1,num2);
						System.out.println(apiResponse);
						System.out.println(cell.getColumnIndex()+" "+cell.getStringCellValue());
						String input = apiResponse;
						input = input.substring(input.indexOf(":")+1, input.lastIndexOf(","));
						cellobj.setCellValue(apiResponse);*/
						}
					// end of If condition for getting cell inputs	
				} //end of while loop of cell iterator
				
				// to call APIrequest for each record
				System.out.println("Params are ---->"+num1+"  "+num2+" "+str);
				String apiResponse =sendGet(str,num1,num2);
				System.out.println(apiResponse);
				String input = apiResponse;
				input = input.substring(input.indexOf(":")+1, input.lastIndexOf(","));
				System.out.println("result from API is "+input);
				cellobj.setCellValue(apiResponse);
				cellobjResult.setCellValue(input);
				// end to call APIrequest for each record
				System.out.println("Expected val from excel is "+expResult);
				int comp = Integer.compare(expResult, Integer.parseInt(input));
				if(comp == 0){
					cellobjTeststatus.setCellValue("Pass");
					System.out.println("Validation is Pass");
					style.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());  
		            style.setFillPattern(FillPatternType.LESS_DOTS);  
		            cellobjTeststatus.setCellStyle(style); 
				}else{
					cellobjTeststatus.setCellValue("Fail");
					style1.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());  
		            style1.setFillPattern(FillPatternType.LESS_DOTS);
		            cellobjTeststatus.setCellStyle(style1); 
					System.out.println("Validation is Fail");
				}
				System.out.println("");
				
			}// if ends here
		}// end of row iterator
		
		 fis.close();
		 FileOutputStream outFile =new FileOutputStream(new File("D:\\Innovation\\Calctest.xlsx"));
		 myWorkBook.write(outFile);
         outFile.close();
	}
	
	private static String sendGet(String opn,int int1,int int2) throws IOException {
	
		String url1 = "http://localhost:8080/calc";
		url1 = url1+"/"+opn+"/"+int1+"/"+int2;
		System.out.println(url1);
		URL obj = new URL(url1);
		//URL obj = new URL("http://localhost:8080/calc/add/21/22");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		// working response code
		int responseCode = con.getResponseCode();
		//System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print result
			//System.out.println("The API response inside is => "+response.toString());
			//string split methods to get the exact result alone
			return response.toString();
		} else {
			System.out.println("GET request not worked");
			return null;
		}
		}
}