package controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.soap.Text;

import model.TextDocument;
import model.Cluster;

public class computeClusters {
	private final int numberOFCLusters=4;
	private final int secondNumberOfClusters=7;
	private ArrayList<Cluster> clusters;
	
	public ArrayList<TextDocument> setDocs() {
		ArrayList<TextDocument> Docs = TextDocument.readDocsWithTermFrequecy();
		return Docs;
	}
	public ArrayList<TextDocument> ComputeTFIDF(ArrayList<TextDocument> Docs) {
		Docs=TextDocument.calculateTFIDF(Docs);
		return Docs;
	}
	
	public void setCLustersCentroid(boolean isInsideClusters,Cluster cluster,int numOfCLusters,ArrayList<TextDocument> Docs) {
		if(isInsideClusters==false)
		{
			clusters=Cluster.setRandomClusterCentroids(numOfCLusters, Docs);
		}
		else
		{
			clusters.get(clusters.indexOf(cluster)).setClustersInside(Cluster.setRandomClusterCentroids(numOfCLusters, cluster.getDocsAroundCentroid()));
		}
	}
	
	public Set<String> getAllNativeWords(ArrayList<TextDocument> Docs){
		Set<String> words=new HashSet<String>();
		if(Docs!=null)
		for(TextDocument tD:Docs) {
			words.addAll(tD.getTermFrequency().keySet());
		}
		return words;
	}
	public void setDocsRelatedToClusers(ArrayList<Cluster> clusters,Set<String> words,ArrayList<TextDocument> Docs) {
		double minDistance=0.0;
		int clusterID=-1;
		if(Docs!=null)
		for(TextDocument Doc:Docs) {
			minDistance=0.0;
			if(clusters!=null)
			for(Cluster cl : clusters) {
				double dis=cl.computeDocDistance(Doc,words);
				if(dis>=minDistance) {
					minDistance=dis;
					clusterID=cl.getClusterID();
				}
			}
				clusters.get(clusters.indexOf(new Cluster(clusterID))).addDoc(Doc);
		}
		
		
	}
	public int calculateClustersInseideCluster(Cluster cluster,int numberOfClusters,Set<String> words){
		
		this.setCLustersCentroid(true,cluster,numberOfClusters,cluster.getDocsAroundCentroid());
		this.setDocsRelatedToClusers(cluster.getClustersInside(),this.getAllNativeWords(cluster.getDocsAroundCentroid()),cluster.getDocsAroundCentroid());
		HashMap<Integer,ArrayList<TextDocument>> OLDclusterRelatedDocs=new HashMap<Integer,ArrayList<TextDocument>>(); 
		HashMap<Integer,ArrayList<TextDocument>> NEWclusterRelatedDocs=new HashMap<Integer,ArrayList<TextDocument>>(); 
		int clusteringInsideCounter=0;
		do {
			clusteringInsideCounter++;
			for(int i=0;i<cluster.getClustersInside().size();i++) {
				OLDclusterRelatedDocs.put(cluster.getClustersInside().get(i).getClusterID(),cluster.getClustersInside().get(i).getDocsAroundCentroid());
				}
		this.newAverageCentroids(cluster.getClustersInside(),words);
		setDocsRelatedToClusers(cluster.getClustersInside(),words,cluster.getDocsAroundCentroid());
		for(int i=0;i<cluster.getClustersInside().size();i++) {
			NEWclusterRelatedDocs.put(cluster.getClustersInside().get(i).getClusterID(),cluster.getClustersInside().get(i).getDocsAroundCentroid());
			}
		}while(!OLDclusterRelatedDocs.equals(NEWclusterRelatedDocs));
		return clusteringInsideCounter;
	}
	public void newAverageCentroids(ArrayList<Cluster> clusters,Set<String> words) {
		double meanCoordinate=0.0;
		for(Cluster cluster:clusters) {
			HashMap<String,Double> newCentroid=new HashMap<String,Double>();
			for(String word : words) {
				meanCoordinate=0.0;
			for(TextDocument Doc:cluster.getDocsAroundCentroid()) {
				if(Doc.getTermFrequency().containsKey(word)) {
				meanCoordinate+=Doc.getTermFrequency().get(word);
				}
			}
			if(meanCoordinate!=0)
			{
				newCentroid.put(word, meanCoordinate/cluster.getDocsAroundCentroid().size());
			}
		}
			cluster.setCentroid(newCentroid);
			cluster.setDocsAroundCentroid(new ArrayList<TextDocument>());
		}
		
		System.out.println("new centroids have been set");
	}
	public HashMap<String,Double> getWordTermFrequency(ArrayList<TextDocument> docs){
		Set<String> words=new HashSet<String>();
		HashMap<String,Double> wordsWithTermFrequency=new HashMap<String,Double>();
		if(docs!=null)
		for(TextDocument tD:docs) {
			words.addAll(tD.getTermFrequency().keySet());
			for(String word :words) {
				wordsWithTermFrequency.put(word,tD.getTermFrequency().get(word));
		}
		words=new HashSet<String>();
		}
		
		return wordsWithTermFrequency;
	}
	public HashMap<String,Double> distanceBetweenClusters(ArrayList<Cluster> clusters) {
		HashMap<String,Double> DistanceBetweenCentroids;
		DistanceBetweenCentroids=new HashMap<String,Double>();
		for(int i=0;i<clusters.size();i++) {
			if(i<clusters.size()-1)
			for(int j=i+1;j<clusters.size();j++) {
				DistanceBetweenCentroids.put("("+clusters.get(i).getClusterID()+ " and " +
						clusters.get(j).getClusterID()+") ", clusters.get(i).computeDistanceBetwwenCentroids(
								clusters.get(j).getCentroid(), this.getAllNativeWords(clusters.get(i).getDocsAroundCentroid())));
			}
		}
		return DistanceBetweenCentroids;
	}
	public HashMap<Integer,Double> calculateAvderageDistance() {
		Double dis=0.0;
		HashMap<Integer,Double> AverageDistance=new HashMap<Integer,Double>();
		for (Cluster cluster:clusters) {
			for(TextDocument doc:cluster.getDocsAroundCentroid()) {
				dis+=cluster.computeDocDistance(doc, this.getAllNativeWords(cluster.getDocsAroundCentroid()));
			}
			
			AverageDistance.put(cluster.getClusterID(), dis/cluster.getDocsAroundCentroid().size());
		}
		return AverageDistance;
	}
	public void setLabelsToClusters() {
		for(Cluster cluster:this.clusters) {
			double dis1=0.0;
			double minDis1=0.0;
			String label1="";
			for(Cluster insideCluster:cluster.getClustersInside()) {
				double dis=0.0;
				double minDis=0.0;
				String label="";
				for(TextDocument doc:insideCluster.getDocsAroundCentroid()) {
					dis=insideCluster.computeDocDistance(doc, this.getAllNativeWords(insideCluster.getDocsAroundCentroid()));
					if(dis>minDis && dis!=0) {
						minDis=dis;
						label=doc.getDocName();
					}
				}
				insideCluster.setClusterLabel(label);
				dis1=cluster.computeDistanceBetwwenCentroids(insideCluster.getCentroid(), this.getAllNativeWords(cluster.getDocsAroundCentroid()));
				if(dis1>minDis1 && dis1!=0.0) {
					minDis1=dis1;
					label1=insideCluster.getClusterLabel();
				}
			}
			cluster.setClusterLabel(label1);
		}
	}
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}
	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	public int getNumberOFCLusters() {
		return numberOFCLusters;
	}
	
	
}
