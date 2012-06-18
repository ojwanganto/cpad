package org.openmrs.module.amrsreport.web.dwr;

import net.sf.saxon.value.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nicholas Ingosi magaja
 * Date: 6/6/12
 * Time: 8:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class DWRAmrsReportService {
    private static final Log log = LogFactory.getLog(DWRAmrsReportService.class);

    public String viewMoreDetails(String file,String id){



        //open the file and do all the manipulation
        AdministrationService as = Context.getAdministrationService();
        String folderName=as.getGlobalProperty("amrsreport.file_dir");

        File fileDirectory = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);

        File amrsFile=null;
        FileInputStream fstream = null;
        DataInputStream in =null;
        BufferedReader br=null;
        String record=null;
        String [] splitByComma=null;
        String finaldata=new String();
        List<String> recordsAfterAll=new ArrayList<String>();
        StringBuilder strColumnData = new StringBuilder();
        String columns=new String();
        String [] columnsSplit=null;
        String [] valuesArray;
        try {
            amrsFile=new File(fileDirectory,file);
            fstream = new FileInputStream(amrsFile);
            in      = new DataInputStream(fstream);
            br      = new BufferedReader(new InputStreamReader(in));
            //get the first row to be used as columns
            columns=br.readLine();
            columnsSplit=columns.split(",");
            //log.info(columnsSplit);
            while ((record = br.readLine()) != null)   {


                    splitByComma=record.split(",");

                if(stripLeadingAndTrailingQuotes(splitByComma[0]).equals(id)){
                    for (int i = 0; i < columnsSplit.length; i++) {
                        String columnName =stripLeadingAndTrailingQuotes(columnsSplit[i]);
                        String value = stripLeadingAndTrailingQuotes(splitByComma[i]);


                        strColumnData.append(columnName).append("    :").append(value).append(",");
                    }

               }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //log.info("We are returning now "+recordsAfterAll);
        return strColumnData.toString();
    }
    public String viewMoreDetailsRender(String bff,String id){
        String line=new String();
        String columns=new String();
        String [] columnsSplitDetails=null;
        String [] splitByCommaDetails=null;
        StringBuilder strColumnDataDetails = new StringBuilder();


        //open the file and do all the manipulation
        AdministrationService as = Context.getAdministrationService();
        String folderName=as.getGlobalProperty("amrsreport.file_dir");

        File fileDirectory = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);

        ///
        File amrsFile=null;
        FileInputStream fstream = null;
        DataInputStream in =null;
        BufferedReader br=null;

        //log.info("lets log the buffer here "+bff);

        try {
            amrsFile=new File(fileDirectory,bff);
            fstream = new FileInputStream(amrsFile);
            in      = new DataInputStream(fstream);
            br      = new BufferedReader(new InputStreamReader(in));
            columns=br.readLine();
            columnsSplitDetails=columns.split(",");
            while (( line = br.readLine()) != null){

                splitByCommaDetails=line.split(",");

                if(stripLeadingAndTrailingQuotes(splitByCommaDetails[0]).equals(id)){
                    for (int v = 0; v < columnsSplitDetails.length; v++) {
                        String columnName =stripLeadingAndTrailingQuotes(columnsSplitDetails[v]);
                        String value = stripLeadingAndTrailingQuotes(splitByCommaDetails[v]);

                        strColumnDataDetails.append(columnName).append("    :").append(value).append(",");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return strColumnDataDetails.toString();
    }
    /*public List<List<String>> populateHistory(String history){

        String filename="";
        List<String> filenames=new ArrayList<String>();

        AdministrationService as = Context.getAdministrationService();
        String folderName=as.getGlobalProperty("amrsreport.file_dir");

        File fileDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);

        String[] children = fileDir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i=0; i<children.length; i++) {
                // Get filename of file or directory
                filename = children[i];
                filenames.add(filename);
            }
        }

        //map.addAttribute("reportHistory",filenames);

        ///end of interface population after submitting
        List<List<String>> records=new ArrayList<List<String>>();
        String [] linedata=null;
        String first=null;
        String second=null;
        String third=null;
        String fourth=null;
        try{
            File amrsFile=new File(fileDir,history);
            FileInputStream fstream = new FileInputStream(amrsFile);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null)   {

                List<String> intlist=new ArrayList<String>();
                linedata=line.split(",");
                intlist.add(StringUtils.defaultString(stripLeadingAndTrailingQuotes(linedata[0])));
                intlist.add(StringUtils.defaultString(stripLeadingAndTrailingQuotes(linedata[1])));
                intlist.add(StringUtils.defaultString(stripLeadingAndTrailingQuotes(linedata[2])));
                intlist.add(StringUtils.defaultString(stripLeadingAndTrailingQuotes(linedata[3])));


                records.add(intlist) ;
            }
            records.remove(0);
            //map.addAttribute("records",records);
            fstream.close();
            in.close();
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
           return records;
    }*/
    static String stripLeadingAndTrailingQuotes(String str)
    {
        if (str.startsWith("\""))
        {
            str = str.substring(1, str.length());
        }
        if (str.endsWith("\""))
        {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}