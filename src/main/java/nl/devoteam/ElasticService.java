package nl.devoteam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;


public class ElasticService {

	 	RestHighLevelClient client = new RestHighLevelClient(
	       RestClient.builder(
	                new HttpHost("192.168.99.100", 9200, "http"),
	                new HttpHost("192.168.99.100", 9201, "http")));
	 	
	 	

/*public void sendRestData() throws IOException {

	Map<String, Object> jsonMap = new HashMap<>();
	jsonMap.put("user", "kimchy");
	jsonMap.put("postDate", new Date());
	jsonMap.put("message", "trying out Elasticsearch");
	request.source(jsonMap, XContentType.JSON);
	client.indexAsync(request, RequestOptions.DEFAULT, listener);
	IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
	String index = indexResponse.getIndex();
	String type = indexResponse.getType();
	String id = indexResponse.getId();
	System.out.println("\\n"+index+"\n"+type+"\n"+id);
}*/

public void To_elastic(Exchange exchange, String Index, String type) {
	
	IndexRequest request = new IndexRequest(Index, type);
	Map<String, Object> all_headers = exchange.getIn().getHeaders();
	Map<String, Object> collect = 
			all_headers.entrySet().stream()
				.filter(x-> x.getKey().startsWith("log_"))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	
	collect.put("exchangeId", exchange.getExchangeId());
	collect.put("messageId", exchange.getIn().getMessageId());
	request.source(collect, XContentType.JSON);
	client.indexAsync(request, RequestOptions.DEFAULT, listener);
}

ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
    @Override
    public void onResponse(IndexResponse indexResponse) {
        
    }

    @Override
    public void onFailure(Exception e) {
        
    }
};
/*public static void main(String[] args) throws IOException {
	System.out.println("starting process");
	ElasticService elasticService = new ElasticService();
	elasticService.sendRestData();
	elasticService.client.close();
	System.out.println("ending process");
}*/
}