/********************************************************************
Class:     	    CSCI 680
Application:   	Movie Recommendation System
Author:    	    Aditya Sabbineni
Z-number:  	    z1780715
Date Due:  	    12/01/16

Purpose:   	    Movies Handling program and suggestions

*********************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hw5a {

    /**
     * @param args the command line arguments
     */
    static int cntNumLines=0;
    static int cntNonAscii=0;
    static boolean valid=false;
    static int cntTotNumLines=0;
    public static void main(String[] args) throws IOException {
        openProcessFile();	// Opens files and does the reading of data
        
        openRatingsFile();	// Opens ratings file and process the data
       
        readStoreData();	// Writes the data to the serialised file
        
        
    }
    
    public static void readStoreData() throws FileNotFoundException
    {
        BufferedReader br;
        String path = "movie-names2.txt";
        br = new BufferedReader(new FileReader(path));
        
        
    }
    public static void openRatingsFile() throws FileNotFoundException, IOException{
        BufferedReader br;
        String path = "/home/turing/t90rkf1/d470/dhw/hw5-movies/movie-matrix.txt";
        br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
       
	// Logic to count the number of columns 
        int count = 0;
        for(int i=0;i<line.length();i++)    if(line.charAt(i) == ';')   count++;
        
	// Putting data into a 2d array list
        int[][] ratings = new int[cntTotNumLines+1][count+1];
        ArrayList<ArrayList<Integer>> rat = new ArrayList<>(); 
        
        while(line != null)
        {
            ArrayList<Integer> rat1 = new ArrayList<>();
                for(int j=0;j<line.length();j++)
                {
                    
                    if(Character.isDigit(line.charAt(j))) 
                    {
                        rat1.add(Character.getNumericValue(line.charAt(j)));
                        j++;
                    } 
                    else    
                        rat1.add(0);
                    
                    
                }
                
                rat.add(rat1);
            line = br.readLine();
        }
        try(FileOutputStream fos = new FileOutputStream("movie-matrix2.ser"); 
        ObjectOutputStream oos = new ObjectOutputStream(fos)) 
        {
            oos.writeObject(rat);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }
    public static void openProcessFile()
    {
        
        try{
            BufferedReader br;
            String path = "/home/turing/t90rkf1/d470/dhw/hw5-movies/movie-names.txt";
            
            File newFile =new File("movie-names2.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
            
            FileInputStream fr = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(fr, "ISO-8859-1"));
            String line = br.readLine();
            
                    
            System.out.println("Non-Ascii character lines : ");
            while(line != null){
                line = processNonAscii(line);
                separateData(line, writer);
                line = br.readLine();
                cntTotNumLines++;
            }
            System.out.println("Number of lines containing non-Ascii characters = "+cntNumLines);
            System.out.println("Total number of non-Ascii characters = "+cntNonAscii);
            writer.close();
        }
        catch (IOException ioException)
        {
            System.err.println("Error opening file. Terminating. here");
            System.exit(1);
        } 
    }
    
    public static String processNonAscii(String line){
           
        for(int i=0;i<line.length();i++)
        {
            if(line.charAt(i) > 127)
            {
                cntNumLines++;
                line = updateText(line, i);
                break;
            }
        }
        return line;
        
    }
    public static String updateText(String text, int pos){
        
        StringBuilder sb = new StringBuilder(text);
        // Map constrcution
        HashMap<Character,Character> map = new HashMap<Character, Character>(); 
        map.put('è', 'e');
        map.put('é', 'e');
        map.put('ö', 'o');
        map.put('Á', 'A');
        
        
        for(int i=pos;i<sb.length();i++)
        {
            if(sb.charAt(i) > 127)
            {
                cntNonAscii++;
                char c = map.get(sb.charAt(i));
                sb.setCharAt(i,c);
            }
        }
        text = sb.toString();
        System.out.println(text);
        // Logic for conversion
        return text;
    }
    
    public static void separateData(String text, BufferedWriter writer) throws IOException
    {
        
        String pattern = "([0-9]+)[|](.*)";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        
        // Now create matcher object.
        Matcher m = r.matcher(text);
        if (m.find( )) 
        {
            StringBuilder num = new StringBuilder(m.group(1));
            for(int i=0;num.length()!=4;i++) num.insert(0, '0');  
            writer.write (num.toString()+" ");
            writer.write (m.group(2)+"\n");
        }
        
    }
}

