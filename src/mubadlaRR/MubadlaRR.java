package mubadlaRR;


//import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranker.Status;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrCluster.Status;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.RetrieveAndRank;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrCluster;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrClusterOptions;

public class MubadlaRR {

	public static void main(String[] args) {
		RetrieveAndRank service = new RetrieveAndRank();
		service.setUsernameAndPassword("2b941f9d-3137-46ad-8575-027d3fec86b7","zc2ksSYr5J3V");
		/****** Create Solr Cluster and make sure its up*****/
//		SolrClusterOptions options = new SolrClusterOptions("My cluster", 1);
//		SolrCluster cluster = service.createSolrCluster(options).execute();
//		System.out.println("SolrCluster: " + cluster);
//		try{
//		// wait until the cluster is available
//		while (cluster.getStatus()==Status.NOT_AVAILABLE){
//		   Thread.sleep(10000); // sleep 10 seconds
//		   cluster = (SolrCluster)service.getSolrCluster(cluster.getId()).execute();
//		   System.out.println("SolrCluster status: " + cluster.getStatus());
//		   System.out.println("SolrCluster ID: " + cluster.getId());
//		   }
//		}catch(Exception e){
//			e.printStackTrace();
//			
//		}
		
		/****** End for Create Solr Cluster and make sure its up*****/
		/***** Create Solr Collection *********/
		
		CollectionAdminRequest.Create createCollectionRequest =
		        new CollectionAdminRequest.Create();
		createCollectionRequest.setCollectionName("example_collection");
		createCollectionRequest.setConfigName("example_config");
////		SolrClient solrClient = new SolrClient();
//		createCollectionRequest.
//		System.out.println("Creating collection...");
//		CollectionAdminResponse response = createCollectionRequest.process(solrClient);
//		    if (!response.isSuccess()) {
//		      System.out.println(response.getErrorMessages());
//		      throw new IllegalStateException("Failed to create collection: "
//		          + response.getErrorMessages().toString());
//		    }
//		System.out.println("Collection created.");
//		System.out.println(response);
		/***** End for Create Solr Collection *********/
	}

}
