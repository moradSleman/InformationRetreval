package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Dictionary;
public class TextDocument {
	private static ArrayList<String> stopWords=new ArrayList<String>(Arrays.asList("deliba","belgrade","osns","mhrw","rwrw"," ","i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));
	private String DocName;
	private LinkedHashMap<String,Double> termFrequency;
	private int numOfWords;
	public TextDocument(String docName) {
		super();
		DocName = docName;
		termFrequency=new LinkedHashMap<String,Double>();
	}
	public String getDocName() {
		return DocName;
	}
	
	public int getNumOfWords() {
		return numOfWords;
	}
	public void setNumOfWords(int numOfWords) {
		this.numOfWords = numOfWords;
	}
	public void setDocName(String docName) {
		DocName = docName;
	}
	public HashMap<String, Double> getTermFrequency() {
		return termFrequency;
	}
	public void setTermFrequency(LinkedHashMap<String, Double> termFrequency) {
		this.termFrequency = termFrequency;
	}
	public boolean addTermFrequency(String word) {
		if(!termFrequency.keySet().contains(word)) {
			termFrequency.put(word,1.0);
			return true;
		}
		else
			{
			termFrequency.put(word,termFrequency.get(word)+1);
			return true;
			}
	}
	
	
	public static ArrayList<TextDocument> readDocsWithTermFrequecy(){
		HashSet<String> formedWords=new HashSet<String>();
		File dir1=new File("src/words_alpha.txt");
		try(BufferedReader br=new BufferedReader(new FileReader(dir1))){
			String line;
			while((line = br.readLine())!=null) {
				ArrayList<String> seperatedWords=new ArrayList<String>(Arrays.asList((line.split("\\s+"))));
				formedWords.addAll(seperatedWords);
			}
		}
		 catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ArrayList<TextDocument> txtDocs=new ArrayList<TextDocument>();
		int wordCounter=0;
		File dir = new File("src/IR_Docs_Txt");
		  File[] directoryListing = dir.listFiles();
		  for(File file : directoryListing) {
			 TextDocument tDoc=new TextDocument(file.getName());
		  try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			    String line;
			    int i=1;
			    while ((line = br.readLine()) != null) {
			    	if(i>10) {
			    	//remove numbers and punctuations
			    	line=line.replaceAll("[^a-zA-Z- ]", " ");
			    	
			    	//lower case all words
			    	line=line.toLowerCase();
			    
			    	//remove stop words
			       ArrayList<String> seperatedWords=new ArrayList<String>(Arrays.asList((line.split("\\s+"))));
			       seperatedWords.removeAll(stopWords);
			       for(String s:seperatedWords) {
			    	   if(formedWords.contains(s) && s!=null && s!="" && s!=" " && s.length()!=1 && s.length()!=2 && s.length()!=3) 
			    	   {
			    		   tDoc.addTermFrequency(s);
			    		   wordCounter++;
			    	   }
			    	   
			       }
			    	}
			    	else
			    		i++;
			    }
			    tDoc.setNumOfWords(wordCounter);
			    if(tDoc.getTermFrequency().keySet().contains("references")) {
			    	Double j=tDoc.getTermFrequency().get("references");
			    	Object[] array=tDoc.getTermFrequency().keySet().toArray();
			    	for(int k=0;k<array.length;k++) {
			    		if(!array[k].equals("references")) {
			    			continue;
			    		}else {
			    			if(j>1.0){
			    				j--;
			    			}
			    			else
			    		{
			    			tDoc.getTermFrequency().remove(array[k]);
			    			tDoc.setNumOfWords(tDoc.getNumOfWords()-1);
			    		}
			    		}
			    	}
			    }
		/*	    if(tDoc.getTermFrequency().keySet().contains("cryptography")) {
			    	Double j=tDoc.getTermFrequency().get("cryptography");
			    	for(String word:tDoc.getTermFrequency().keySet()) {
			    		if(word!="cryptography") {
			    			System.out.println(j);
			    			continue;
			    		}else
			    			if(j>1){
			    				j--;
			    				continue;
			    			}
			    			else
			    				
			    		{
			    			tDoc.getTermFrequency().remove(word);
			    			tDoc.setNumOfWords(tDoc.getNumOfWords()-1);
			    		}
			    	}
			    }*/
			    
			  /*  for(TextDocument t:txtDocs) {
			    	System.out.println(t.getTermFrequency().keySet());
			    }*/
			    txtDocs.add(tDoc);
		  }
			 catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		return txtDocs;
	}
	
	public static ArrayList<TextDocument> calculateTFIDF(ArrayList<TextDocument> txtDocs) {
		int counter=0;
		if(txtDocs!=null)
		for(TextDocument textDoc : txtDocs) {
			for(String word : textDoc.getTermFrequency().keySet()) {
				if(word=="") {
					continue;
				}
				counter=0;
				for(TextDocument tD : txtDocs) {
					if(tD.getTermFrequency().containsKey(word)) {
						counter++;
					}
				}
			txtDocs.get(txtDocs.indexOf(textDoc)).getTermFrequency().replace(word,
					txtDocs.get(txtDocs.indexOf(textDoc)).getTermFrequency().get(word),(1+Math.log10(
					txtDocs.get(txtDocs.indexOf(textDoc)).getTermFrequency().get(word)))*
					Math.log10(txtDocs.size()/counter));
			}
		}
		return txtDocs;
	}
	@Override
	public String toString() {
		return "TextDocument [DocName=" + DocName + ",termFrequency=" + termFrequency + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DocName == null) ? 0 : DocName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextDocument other = (TextDocument) obj;
		if (DocName == null) {
			if (other.DocName != null)
				return false;
		} else if (!DocName.equals(other.DocName))
			return false;
		return true;
	}
	

}
