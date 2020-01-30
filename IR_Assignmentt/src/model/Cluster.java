package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Cluster {
	private boolean IsIncludeClusters=false;
	private ArrayList<Cluster> clustersInside;
	private Integer clusterID;
	private HashMap<String,Double> centroid;
	private ArrayList<TextDocument> docsAroundCentroid;
	private String clusterLabel;
	public String getClusterLabel() {
		return clusterLabel;
	}

	public void setClusterLabel(String clusterLabel) {
		this.clusterLabel = clusterLabel;
	}

	public Cluster(int id) {
		super();
		docsAroundCentroid=new ArrayList<TextDocument>();
		this.clusterID=id;
	}
	
	public boolean isIsIncludeClusters() {
		return IsIncludeClusters;
	}

	public void setIsIncludeClusters(boolean isIncludeClusters) {
		IsIncludeClusters = isIncludeClusters;
	}

	public ArrayList<Cluster> getClustersInside() {
		return clustersInside;
	}

	public void setClustersInside(ArrayList<Cluster> clustersInside) {
		this.clustersInside = clustersInside;
	}

	public HashMap<String, Double> getCentroid() {
		return centroid;
	}
	
	public void addDoc(TextDocument Doc) {
		if(Doc!=null)
		this.docsAroundCentroid.add(Doc);
	}
	public Integer getClusterID() {
		return clusterID;
	}

	public void setClusterID(Integer clusterID) {
		this.clusterID = clusterID;
	}

	public void setCentroid(HashMap<String, Double> centroid) {
		this.centroid = centroid;
	}

	public void setDocsAroundCentroid(ArrayList<TextDocument> Docs) {
		this.docsAroundCentroid=Docs;
	}
	
	public ArrayList<TextDocument> getDocsAroundCentroid() {
		return docsAroundCentroid;
	}

	public double computeDocDistance(TextDocument doc,Set<String> words) {
		double value=0.0;
		for(String word : words) {
			if(!doc.getTermFrequency().keySet().contains(word) || !this.centroid.keySet().contains(word)) {
				continue;
			}else
			{
			value+=(this.centroid.get(word)*doc.getTermFrequency().get(word));
			}
		}
		return value;
	}
	public Double computeDistanceBetwwenCentroids(HashMap<String,Double> centroid,Set<String> words) {
		double value=0.0;
		for(String word : words) {
			if(!centroid.keySet().contains(word) || !this.centroid.keySet().contains(word)) {
				continue;
			}else
			{
			value+=(this.centroid.get(word)*centroid.get(word));
			}
		}
		return value;
		}
	public static ArrayList<Cluster> setRandomClusterCentroids(int numOfCLucsters,ArrayList<TextDocument> Docs) {
		ArrayList<Cluster> clusters=new ArrayList<Cluster>(numOfCLucsters);
		ArrayList<Integer> randomNumbers=new ArrayList<Integer>();
		int nDoc=-1;
		if(numOfCLucsters>Docs.size()) {
		for(int i=0;i<Docs.size();i++) {
		Random rand = new Random();
		nDoc = rand.nextInt(Docs.size());
		while(randomNumbers.contains(nDoc)){
			nDoc = rand.nextInt(Docs.size());
		}
			randomNumbers.add(nDoc);
		}
		for(int i=0;i<randomNumbers.size();i++) {
		Cluster cluster=new Cluster(i);
		if(Docs!=null)
		cluster.setCentroid(Docs.get(randomNumbers.get(i)).getTermFrequency());
		clusters.add(cluster);
	}
				for(int i=Docs.size();i<numOfCLucsters;i++) {
					Cluster cluster=new Cluster(i);
					cluster.setCentroid(new HashMap<String,Double>());
					clusters.add(cluster);
				}
		}
		else
		{
			for(int i=0;i<numOfCLucsters;i++) {
				Random rand = new Random();
				nDoc = rand.nextInt(Docs.size());
				while(randomNumbers.contains(nDoc)){
					nDoc = rand.nextInt(Docs.size());
				}
					randomNumbers.add(nDoc);
				}
				for(int i=0;i<randomNumbers.size();i++) {
				Cluster cluster=new Cluster(i);
				if(Docs!=null)
				cluster.setCentroid(Docs.get(randomNumbers.get(i)).getTermFrequency());
				clusters.add(cluster);
			}
		}
		return clusters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clusterID == null) ? 0 : clusterID.hashCode());
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
		Cluster other = (Cluster) obj;
		if (clusterID == null) {
			if (other.clusterID != null)
				return false;
		} else if (!clusterID.equals(other.clusterID))
			return false;
		return true;
	}
	
	
}
