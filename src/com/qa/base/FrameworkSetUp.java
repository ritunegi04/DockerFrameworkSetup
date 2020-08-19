package com.qa.base;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;

import org.junit.Assert;

public class FrameworkSetUp {
	
	public static void main(String[] arg) throws InterruptedException,IOException
	{
		Runtime runtime=Runtime.getRuntime();
		FileReader fr;
		BufferedReader br;
		int count=0;
		boolean flag=false,err=false;
		  File file=new File("setup.bat"); 
		  runtime.exec("cmd /c start "+file);
		   File f;
		        
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.SECOND, 900);
			long stopNow=cal.getTimeInMillis();	
			Thread.sleep(120000);
			while((System.currentTimeMillis()<stopNow)&& count==0)
			{
				if(flag==true||err==true)
					break;
			f=new File(".\\performance-testing-framework\\FrameworkSetupProgress.txt");
			if(!f.exists())
			{
				continue;
			}
			fr = new FileReader(f);
			br=new BufferedReader(fr);
			String currLine=br.readLine();
			while(currLine!=null && !flag)
			{
				
				if (currLine.contains("ERROR"))
				{
					currLine=br.readLine();
					err=true;
					break;
				}
				else if(currLine.contains("CREATED")&&currLine.contains("STATUS") )
				{
					currLine=br.readLine();
					if (currLine==null)
					{
						err=true;
						break;
					}
					else
						continue;
				}
				else if((currLine.contains("grafana")|| currLine.contains("influxdb-timeshift-proxy")
						||currLine.contains("jenkins")||currLine.contains("portainer")||
						currLine.contains("telegraf")||currLine.contains("influxdb"))
						&& currLine.contains("Up"))
					{
						count++;
					}
				
				if(count==6)
				{
					flag=true;
				}
				currLine=br.readLine();
			}
			br.close();
			}
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(".\\performance-testing-framework\\FrameworkSetupProgress.txt", 
					true)));
		try
		{
	Assert.assertTrue(flag);
	out.write("Framework components started successfully");
		}
		catch(AssertionError e)
		{
			out.write("Issue Occurred while starting framework components.");
		}
	finally
	{
		out.close();
		runtime.exec("taskkill /f /im cmd.exe");
	}
	}


}
