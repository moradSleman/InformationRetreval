package controller;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WordCloud {
	protected void initUI(HashMap<String,Double> words,String clusterLabel) {
        JFrame frame = new JFrame(WordCloud.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        double maxValue=0.0;
        String maxWord="";
        LinkedHashMap<String,Double> maxWords=new LinkedHashMap<String,Double>();
        
        for(int i=0;i<20;i++) {
    	   maxValue=0.0;
           maxWord="";
       for(String word:words.keySet()) {
    	   if(words.get(word)!=null && words!=null)
    	   if(words.get(word)>=maxValue) {
    		   maxWord=word;
    		   maxValue=words.get(word);
    	   }
       }
       words.remove(maxWord);
       maxWords.put(maxWord, maxValue);
       }
       words=maxWords;
       System.out.println(maxWords);
	}
      /*  for (String s : words.keySet()) {
        	double multiply=0.0;
        	if(words.get(s)!=null)
        	{
        		multiply=1000.0*words.get(s);
        	}
        	int tagNum=(int)multiply;
        	if(tagNum==0) {
        		tagNum++;
        	}
        	for (int i =tagNum ; i > 0; i--) {
                cloud.addTag(s);
            }
        }
        for (Tag tag : cloud.tags()) {
            final JLabel label = new JLabel(tag.getName());
            label.setOpaque(false);
            label.setFont(label.getFont().deriveFont((float) tag.getWeight() * 10));
           panel.add(label);
        }
        frame.add(panel);
        frame.setTitle(clusterLabel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    */
}

