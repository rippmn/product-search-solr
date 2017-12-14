package com.rippmn.product.search.solr.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rippmn.product.search.solr.service.SolrSearchService;

@RestController
public class TheController {

	@Autowired
	private SolrSearchService service;

	@RequestMapping("/health")
	public String health() {
		return "alive at " + new Date().toString();
	}

	@RequestMapping("/search")
	public List<String> search(@RequestParam("term") String term)throws IOException, SolrServerException {
		return service.getNames(term);

	}
}
