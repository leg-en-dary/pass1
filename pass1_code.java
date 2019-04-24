import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import javax.management.StringValueExp;

public class pass1_code 
{
	public static void main(String args[])
	{
	try 
	{
		String file1="/home/swapnil/Desktop/PASS1-2/PASS1/src/input.asm";
		FileReader fr=new FileReader(file1);
		BufferedReader br=new BufferedReader(fr);
	
		String file2="IC.txt";
		FileWriter fw=new FileWriter(file2);
		BufferedWriter bw=new BufferedWriter(fw);		
		
		Hashtable<String,String>is=new Hashtable<String, String>();		
		is.put("ADD","01");
		is.put("MOVER","04");
		is.put("MOVEM","05");
		Hashtable<String,String>dl=new Hashtable<String, String>();
		dl.put("DC","01");
		Hashtable<String,String>ad=new Hashtable<String, String>();
		ad.put("START","01");
		ad.put("END","02");
		ad.put("LTORG","05");
		
		Hashtable<String,String>symtab=new Hashtable<String, String>();
		Hashtable<String,String>littab=new Hashtable<String, String>();
		ArrayList<Integer>pooltab=new ArrayList<Integer>();
		
		String scurrentline;
		int locptr=0;
		int symptr=1;
		int poolptr=1;
		int litptr=1;
		
		scurrentline = br.readLine();
		
		String s1=scurrentline.split(" ")[1];
		
		if(s1.equals("START"))
		{
			bw.write("AD\t01\t");
			String s2=scurrentline.split(" ")[2];
			bw.write("C\t"+s2+"\n");			
			locptr=Integer.parseInt(s2);
		}
		
		while((scurrentline=br.readLine())!=null)
		{
				int mind_the_lc=0;
				int flag1=0;
				int flag2=0;
				String type=null;
				
				String s=scurrentline.split(" |\\,")[0];
			
				for(Map.Entry m:symtab.entrySet())
				{
					if(s.equals(m.getKey()))
					{
						m.setValue(locptr);
						flag1=1;
					}	
				}
				
				if(s.length()!=0 && flag1==0)
				{
						symtab.put(s,String.valueOf(locptr));
						symptr++;
				}
				
			//0 end
				
				int isopcode=0;
				s=scurrentline.split(" |\\,")[1];
				
				for(Map.Entry m: is.entrySet())
				{
					if(s.equals(m.getKey()))
					{
						bw.write("IS\t"+m.getValue()+"\t");
						type="is";
						isopcode=1;
					}					
				}
				
				for(Map.Entry m : ad.entrySet())
				{
					if(s.equals(m.getKey()))
					{
						bw.write("AD\t"+m.getValue()+"\t");
						type="ad";
						isopcode=1;
					}
				}
				
				for(Map.Entry m : dl.entrySet())
				{
					if(s.equals(m.getKey()))
					{
						bw.write("DL\t"+m.getValue()+"\t");
						type="dl";
						isopcode=1;
					}
				}
			
				
				if(s.equals("LTORG"))
				{
					pooltab.add(poolptr);
					
					for(Map.Entry m:littab.entrySet())
					{
							if(m.getValue()=="")
							{
								m.setValue(locptr);
								locptr++;
								poolptr++;
								mind_the_lc=1;
								isopcode=1;					
								flag2=1;
							}
					}					
				}
				
				if(s.equals("END"))
				{
					if(flag1==1)
					{
						pooltab.add(poolptr);
					}
					
					for(Map.Entry m : littab.entrySet())
					{
						if(m.getValue()=="")
						{
							m.setValue(locptr);
							locptr++;
							mind_the_lc=1;
						}					
					}		
				}
				
				//end 1
				
				if(scurrentline.split(" |\\,").length >2)
				{
					s=scurrentline.split(" |\\,")[2];
					
					if(s.equals("AREG"))
					{
							bw.write("1\t");
							isopcode=1;
					}
					else if(s.equals("BREG"))
					{
						bw.write("2\t");
						isopcode=1;	
					}
					else if(s.equals("CREG"))
					{
						bw.write("3\t");
						isopcode=1;
					}
					else if(s.equals("DREG"))
					{
						bw.write("4\t");
						isopcode=1;
						
					}
					else if(type=="dl")
					{
						bw.write("C\t"+s+"\t");
					}
					else
					{
						symtab.put(s,"");						
					}	
				}
				//end 2
				if(scurrentline.split(" |\\,").length>3)
				{
					s=scurrentline.split(" |\\,")[3];
					
					if(s.contains("="))
					{
						littab.put(s,"");						
						bw.write("L\t"+litptr+"\t");
						isopcode=1;
						litptr++;
					}
					else
					{
						symtab.put(s,"");						
						bw.write("S\t"+symptr+"\t");
						symptr++;						
					}
				}
				//end 3.
				
				bw.write("\n");
				if(mind_the_lc==0)
				{
					locptr++;					
				}
		}
		
		
		String f1="symbol_table.txt";
		FileWriter fw1=new FileWriter(f1);
		BufferedWriter bw1=new BufferedWriter(fw1);
		
		for(Map.Entry m :symtab.entrySet())
		{
			bw1.write(m.getKey()+"\t"+m.getValue()+"\n");			
		}
		
		String f2="literal_table.txt";
		FileWriter fw2=new FileWriter(f2);
		BufferedWriter bw2=new BufferedWriter(fw2);
		
		for(Map.Entry m :littab.entrySet())
		{
			bw2.write(m.getKey()+"\t"+m.getValue()+"\n");			
		}
		
		String f3="pool_table.txt";
		FileWriter fw3=new FileWriter(f3);
		BufferedWriter bw3=new BufferedWriter(fw3);
		
		for(Integer item : pooltab)
		{
			bw1.write(item+"\t");			
		}
		
		bw.close();
		bw1.close();
		bw2.close();
		bw3.close();
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}

	}
}
