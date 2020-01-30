package controller;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.swing.SwingUtilities;

import model.Cluster;
import model.TextDocument;

public class MainClass {

	public static void main(String[] args) {
		ArrayList<TextDocument> allDocs;
		computeClusters computeClusters=new computeClusters();
		allDocs=computeClusters.setDocs();
		System.out.println("TFcomputed");
		allDocs=computeClusters.ComputeTFIDF(allDocs);
		System.out.println("TFIDF computed");
		int mainClusteringCounter=0;
		System.out.println("Main 4 clusters clustering started!");
		computeClusters.setCLustersCentroid(false,null,4,allDocs);
		computeClusters.setDocsRelatedToClusers(computeClusters.getClusters(),computeClusters.getAllNativeWords(allDocs),allDocs); 
		HashMap<Integer,ArrayList<TextDocument>> NEWclusterRelatedDocs=new HashMap<Integer,ArrayList<TextDocument>>();
		HashMap<Integer,ArrayList<TextDocument>> OLDclusterRelatedDocs=new HashMap<Integer,ArrayList<TextDocument>>(); 
		do {
			mainClusteringCounter++;
			for(int i=0;i<computeClusters.getClusters().size();i++) {
				OLDclusterRelatedDocs.put(computeClusters.getClusters().get(i).getClusterID(),computeClusters.getClusters().get(i).getDocsAroundCentroid());
			}
		computeClusters.newAverageCentroids(computeClusters.getClusters(),computeClusters.getAllNativeWords(allDocs));
		computeClusters.setDocsRelatedToClusers(computeClusters.getClusters(),computeClusters.getAllNativeWords(allDocs),allDocs);
		for(int i=0;i<computeClusters.getClusters().size();i++) {
			NEWclusterRelatedDocs.put(computeClusters.getClusters().get(i).getClusterID(),computeClusters.getClusters().get(i).getDocsAroundCentroid());
		}
		}while(!OLDclusterRelatedDocs.equals(NEWclusterRelatedDocs));
		int i=1;
		HashMap<Integer,Integer> numberOFClusteringIteration=new HashMap<Integer,Integer>();
		for(Cluster cluster:computeClusters.getClusters()) {
			System.out.println("cluster number "+i+" calculate inside clusters started!");
			int num=computeClusters.calculateClustersInseideCluster(cluster, 7,computeClusters.getAllNativeWords(cluster.getDocsAroundCentroid()));
			numberOFClusteringIteration.put(cluster.getClusterID(), num);
			i++;
		}
		
		
	     computeClusters.setLabelsToClusters();
	  
	     
	     System.out.println("\n-------------------------------------------------------------------------\n\nreport here : \n");
	     System.out.println("number of document by cluster label and ID :");
	     for(Cluster cluster:computeClusters.getClusters()) {
	    	 System.out.println("outside cluster :");
	    	 System.out.print(" "+cluster.getClusterID()+" "+cluster.getClusterLabel()+" : "+cluster.getDocsAroundCentroid().size()+"\n");
	    	 System.out.println("     inside clusters :");
	    	 for(Cluster insideCluster:cluster.getClustersInside()) {
	    		 System.out.print("      "+insideCluster.getClusterID()+" "+insideCluster.getClusterLabel()+" : "+insideCluster.getDocsAroundCentroid().size()+"\n");
	    	 }
	     }
	     System.out.println("---------------------------------------------------------------------\n");
	     System.out.println("Average length for every cluster by label and ID :");
	     for(Cluster cluster : computeClusters.getClusters()) {
	    	 System.out.println("otside cluster :");
	    	 int avgLength=0;
	    	 if(cluster.getDocsAroundCentroid()!=null)
	    	 for(TextDocument doc:cluster.getDocsAroundCentroid()) {
	    		 avgLength+=doc.getNumOfWords();
	    	 }
	    	 if(cluster.getDocsAroundCentroid().size()==0) {
	    		 avgLength=0;
	    	 }
	    	 else {
	    		 avgLength=avgLength/cluster.getDocsAroundCentroid().size();
	    	 }
	    	 System.out.println(" "+cluster.getClusterID()+" "+ cluster.getClusterLabel()+ " : "+avgLength);
	    	 System.out.println("    inside clusters :");
	    	 for(Cluster clusterInside : cluster.getClustersInside()) {
	    		 int avgLength1=0;
	    		 if(clusterInside.getDocsAroundCentroid()!=null)
	    		 for(TextDocument doc:clusterInside.getDocsAroundCentroid()) {
	    			 avgLength1+=doc.getNumOfWords();
	    		 }
	    		 if(clusterInside.getDocsAroundCentroid().size()==0) {
		    		 avgLength1=0;
		    	 }
		    	 else {
		    		 avgLength1=avgLength1/cluster.getDocsAroundCentroid().size();
		    	 }
	    		System.out.println("     "+clusterInside.getClusterID()+" "
	    	 + clusterInside.getClusterLabel()+ " : "+avgLength1);
	    	 }
	     }
	     System.out.println("\n-----------------------------------------------------------------------------");
	     System.out.println("Dictionary size is : ");
	     int wordCounter=0;
	     for(TextDocument doc:allDocs) {
	    	 wordCounter+=doc.getTermFrequency().size();
	     }
	     System.out.println(wordCounter);
	     System.out.println("\n-----------------------------------------------------------------------------------");
	     System.out.println("outside clusters size (first iteration) : ");
	     for(Cluster cluster:computeClusters.getClusters())
	     System.out.println(cluster.getDocsAroundCentroid().size());
	     System.out.println("\n inside clusters size(second iteration) : ");
	     int j=1;
	     for(Cluster cl:computeClusters.getClusters()) {
	    	 System.out.println("group "+j+" :");
	    	 for(Cluster clus:cl.getClustersInside()) {
	    		 System.out.println("  "+clus.getDocsAroundCentroid().size());
	    	 }
	    	 j++;
	     }
	     System.out.println("\n--------------------------------------------------------------------------");
	     System.out.println("name of every group and it's properties : \n");
	     for(Cluster cluster : computeClusters.getClusters()) {
	    	 System.out.println("   name of outside clusters (first iteration) :");
	    	 System.out.println("     "+cluster.getClusterLabel()+" : ");

		     System.out.println("        docs inside cluster");
		     for(TextDocument txt: cluster.getDocsAroundCentroid()) {
		    	 System.out.println("outoutout  "+txt.getDocName());
		     }
    		 System.out.println("       name of inside clusters (second iteration) : ");
	    	 for(Cluster insideCluster: cluster.getClustersInside()) {
	    		 System.out.println("         "+insideCluster.getClusterLabel()+ " : ");
	    		 System.out.println("        docs inside cluster");
			     for(TextDocument txt: insideCluster.getDocsAroundCentroid()) {
			    	 System.out.println("ininin  "+txt.getDocName());
			     }
	    	 }
	     }
	     WordCloud wordCloud=new WordCloud();
	     SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	for(Cluster cluster: computeClusters.getClusters()) {
	            		/*for(Cluster insideCluster:cluster.getClustersInside()) {
	            			 wordCloud.initUI(computeClusters.getWordTermFrequency(insideCluster.getDocsAroundCentroid()),
	            					 insideCluster.getClusterLabel());
	            		}*/
	            		 wordCloud.initUI(computeClusters.getWordTermFrequency(cluster.getDocsAroundCentroid()),
            					 cluster.getClusterLabel());
	            	}
	            }
	        });
	        
	}
		
	}