package mubadlaRR;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.RetrieveAndRank;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;


public class MyRR {
	private static final String USERNAME = "2b941f9d-3137-46ad-8575-027d3fec86b7";
	private static final String PASSWORD = "zc2ksSYr5J3V";
	private static final String SOLR_CLUSTER_ID = "sca7bf5206_bca3_472a_9ea8_ebcebb02d6da";
	private static final String RANKER_ID = "1eec7cx29-rank-3428";
	private static final String COLLECTION_NAME = "policydocument";
	// Schema Fields
	public static final String SCHEMA_FIELD_BODY = "body";
	public static final String SCHEMA_FIELD_CONTENT_HTML = "contentHtml";
	public static final String SCHEMA_FIELD_ID = "id";
	public static final String SCHEMA_FIELD_SOURCE_URL = "sourceUrl";
	public static final String SCHEMA_FIELD_TITLE = "title";
	public static final String SCHEMA_FIELD_CONFIDENCE = "ranker.confidence";
	// Number of results to fetch in Query
	public static final Integer RESULTS_TO_FETCH = 5;
	private static HttpSolrClient solrClient;
	private static RetrieveAndRank service;
	public MyRR(){
		service = new RetrieveAndRank();
		service.setUsernameAndPassword(USERNAME, PASSWORD);
	}

	private static HttpSolrClient getSolrClient(String uri, String username, String password) {
	    return new HttpSolrClient(service.getSolrUrl(SOLR_CLUSTER_ID), createHttpClient(uri, username, password));
	}

	private static HttpClient createHttpClient(String uri, String username, String password) {
	    final URI scopeUri = URI.create(uri);

	    final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	    credentialsProvider.setCredentials(new AuthScope(scopeUri.getHost(), scopeUri.getPort()),
	        new UsernamePasswordCredentials(username, password));

	    final HttpClientBuilder builder = HttpClientBuilder.create()
	        .setMaxConnTotal(128)
	        .setMaxConnPerRoute(32)
	        .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT).setRedirectsEnabled(true).build())
	        .setDefaultCredentialsProvider(credentialsProvider)
	        .addInterceptorFirst(new PreemptiveAuthInterceptor());
	    return builder.build();
	}
	private static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
	    public void process(final HttpRequest request, final HttpContext context) throws HttpException {
	      final AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);

	      if (authState.getAuthScheme() == null) {
	        final CredentialsProvider credsProvider = (CredentialsProvider) context
	            .getAttribute(HttpClientContext.CREDS_PROVIDER);
	        final HttpHost targetHost = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
	        final Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(),
	            targetHost.getPort()));
	        if (creds == null) {
	          throw new HttpException("No creds provided for preemptive auth.");
	        }
	        authState.update(new BasicScheme(), creds);
	      }
	    }
	  }
	public static void main(String[] args) {

		MyRR myrr = new MyRR();
		
		try{
		
		solrClient = getSolrClient(service.getSolrUrl(SOLR_CLUSTER_ID), USERNAME, PASSWORD);
//		SolrQuery query = new SolrQuery("Who is advisory board?");
//		SolrQuery query = new SolrQuery("What is the role of advisory board?");
		SolrQuery query = new SolrQuery("What is meant by civic organization?");
		query.setFields(SCHEMA_FIELD_ID, SCHEMA_FIELD_BODY,
	            SCHEMA_FIELD_TITLE, SCHEMA_FIELD_CONFIDENCE, SCHEMA_FIELD_SOURCE_URL)
	        // The size of the SOLR snippet that we show as our initial answers
	        .setHighlight(true).setHighlightFragsize(150).setHighlightSnippets(1)
	        // The field to perform highlighting on
	        .setParam("hl.fl", SCHEMA_FIELD_BODY)
	        // The number of answers to return
	        .setRows(RESULTS_TO_FETCH) // $NON-NLS-1$
	        // The retrieve and rank endpoint to hit
	        .setRequestHandler("/fcselect")
	        // The ranker to rank the potential answers
	        .setParam("ranker_id", RANKER_ID); //$NON-NLS-1$ //$NON-NLS-2$
		QueryResponse response = solrClient.query(COLLECTION_NAME, query);
		System.out.println(response); 
		SolrDocumentList solrlist = response.getResults();
		Iterator<SolrDocument> iter = solrlist.iterator();
		SolrDocument solrDoc;
		Map temp;
		while(iter.hasNext()){
			solrDoc = (SolrDocument)iter.next();
			temp =  solrDoc.getFieldValuesMap();
			System.out.println("the body value is "+temp.get("body"));
			System.out.println("the rank value is "+temp.get("ranker.confidence"));			
		}

		}catch(Exception e){ 
			e.printStackTrace();
	
		}

	}

}
