/********************************************************************
Class:          CSCI 680
Application:   	Movie Recommendation System
Author:         Aditya Sabbineni
Z-number:       z1780715
Date Due:       12/01/16

Purpose:        Listing/Recommedning top 20 movies

*********************************************************************/

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Adi
 */
public class Hw5b {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    static ArrayList<ArrayList<Integer>> rat = new ArrayList<>(); 
    
    static ArrayList<String> movieList = new ArrayList<>(); 
    static ArrayList<Integer> recList1 = new ArrayList<>(); 
    static ArrayList<Float> recList2 = new ArrayList<>(); 
    static ArrayList<Integer> recList3 = new ArrayList<>(); 
    static boolean flag = false;
    public static void main(String[] args) throws IOException {
        readSerialisedFile();		// Read and Store data
        menuForUser();			// Menu for the user
    }
    
    public static void menuForUser() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice;
        while(true){
            System.out.print("Enter movie number : ");
            String input = br.readLine();
            if((input.equals("q")) || (input.equals("quit")))    {
                System.out.println("User preses quit");
                System.exit(0);
            }
            else {	// Checking for various cases of input data
                if((input.length() > 4) || (input.length() <= 0)){
                    System.out.println("Invalid input, Please enter again"); 
                    continue;
                }
                choice = 0;
                int i=0;
                for( i=0;i<input.length();i++){
                    if(!Character.isDigit(input.charAt(i)))
                    {
                        System.out.println("Invalid input, Please enter again");                        
                        break;
                    }
                           
                }
                if(i >= input.length()) 
                {
                    choice = Integer.parseInt(input);
                    System.out.println(choice+" "+movieList.get(choice));
                    if(choice > 1682)   System.out.println("Invalid input, Please enter again"); 
                    
                    else {
                        recList1.clear();
                        recList2.clear();
			recList3.clear();
                        processRank(choice);
                        System.out.println("\n****-----Printing the top movies-----*****");
                        displayTopList();		// Display the recommendations
                    }
                }
            }
        }
    }
    
    public static void displayTopList(){
        System.out.println("S.No.       R              Count        Num   Movie Name");
        for(int i=0;i<recList1.size();i++){
            if(i == 20) break;
            System.out.println(i+1+"        "+ recList2.get(i)+"         "+recList3.get(i)+"    "+recList1.get(i)+" | "+movieList.get(recList1.get(i)));
        }
        
        if(recList1.size() < 20)    System.out.println("Insufficient comparison movies");
    }
    public static void processRank(int choice){
        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        choice = choice-1;
        //System.out.println("Choice = "+choice+" Size = "+rat.size()+ " In Size = "+rat.get(0).size());
	int i=0;
	int j=0;
        try{
        
        for( i=0;i<rat.size();i++){
            list1.clear();
            list2.clear();
            for( j=0;j<942;j++){
                if((rat.get(i).get(j) != 0) && (rat.get(choice).get(j) != 0)){
                    list1.add(rat.get(i).get(j));
                    list2.add(rat.get(choice).get(j));
                }
            }
            if(list1.size()<10){
            	// CHecking for the number of reviewers number to be less than 10(Ignoring that movie)
		//System.out.println("Number of reviewers less than 10 so ignoring this movie "+list1.size());
                //return;
            }
            else
            {
		// Computing the Pearson coeff once your lists are ready
                computePearson(i, list1, list2);
                
            }
        }
        }catch(IndexOutOfBoundsException e){
            //System.out.println("i = "+i+" j = "+j);
        }
        
    }
    
    public static void computePearson(int compMovie, ArrayList<Integer> list1, ArrayList<Integer> list2){
        //ystem.out.println("computer Pearson");
        float mX=0.0f, sX=0.0f;
        float mY=0.0f, sY=0.0f;
        for (float i: list1)  mX+=i;
        
	//Calculating the Mean
	mX = mX/list1.size();
        for (float i: list2)  mY+=i;
        mY = mY/list2.size();    
        float temp=0.0f;
        for (float i: list1) {
            temp = (i-mX)*(i-mX);
            sX += temp;
        }
        for (float i: list2) {
            temp = (i-mY)*(i-mY);
            sY += temp;
        }
     	
	//Calculating the Standard Deviation   
        sX = sX/(list1.size()-1);
        sY = sY/(list2.size()-1);
        
        sX = (float) Math.sqrt(sX);
        sY = (float) Math.sqrt(sY);
        
        float r=0.0f;
     	
	//Calculating the Pearson coefficient    
        for(int i=0;i<list1.size();i++){
            r += ((list1.get(i)-mX)/sX) * ((list2.get(i)-mY)/sY);
        }
        r = r/(list1.size()-1);
     
	//Adding the movie names to a list and sorting it
        addMovie(r, compMovie+1, list1.size());
        
            
    }
    
   
    public static void addMovie(float r, int index, int size){
        boolean insertFlag = false;
        for(int i=0;i<recList2.size();i++){
            if(recList2.get(i)<r)    {
                recList1.add(i,index);
                recList2.add(i,r);
		recList3.add(i,size);
                insertFlag = true;
                break;
            }
            
        }
        if(false == insertFlag) {
                recList1.add(index);
                recList2.add(r);
		recList3.add(size);
            }
    }

    
    public static void readSerialisedFile() throws FileNotFoundException, IOException{
        
        
        
        try
        {
            try (FileInputStream fis = new FileInputStream("movie-matrix2.ser"); 
                ObjectInputStream ois = new ObjectInputStream(fis)) {
                rat =  (ArrayList<ArrayList<Integer>>)ois.readObject();
            }
         }catch(IOException ioe){
             ioe.printStackTrace();
             
          }catch(ClassNotFoundException c){
             System.out.println("Class not found");
             c.printStackTrace();
             
          }
        
        BufferedReader br;
        String path = "movie-names2.txt";
        br = new BufferedReader(new FileReader(path));
        
        //String []movieList = new String[1683];
        movieList.add("");
        String line;
        while((line = br.readLine()) != null){
            String pattern = "([0-9]+)[ ](.*)";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        
        // Now create matcher object.
        Matcher m = r.matcher(line);
        if (m.find( )) movieList.add(m.group(2));
                      
        }
        br.close();
        /*for(int i=0;i<movieList.size();i++)
            System.out.println(movieList.get(i));*/
    }
}

