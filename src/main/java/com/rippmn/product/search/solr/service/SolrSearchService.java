package com.rippmn.product.search.solr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

@Service
public class SolrSearchService {

	private CloudSolrClient client;

	@PostConstruct
	public void init() {
		client = new CloudSolrClient(System.getenv("ZK_HOST"));
		client.setDefaultCollection("products");
	}

	public List<String> getNames(String phrase) throws IOException, SolrServerException {

		ArrayList<String> names = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(phrase);
		StringBuffer sb = new StringBuffer();
		boolean singleToken = true;
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
			if (st.hasMoreTokens()) {
				sb.append("&&");
				singleToken = false;
			}

			if (singleToken) {
				// if there is only one term lets append a wildcard
				sb.append("*");
			}
		}

		SolrQuery query = new SolrQuery("name:" + sb.toString());
		query.addField("name");
		query.setRows(10);

		QueryResponse rsp = client.query(query);

		for (SolrDocument document : rsp.getResults()) {
			String name = document.getFieldValue("name").toString();
			names.add(name.substring(1, name.length() - 1));
		}
		return names;
	}

}
