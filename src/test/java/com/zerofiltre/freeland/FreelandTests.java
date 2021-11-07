package com.zerofiltre.freeland;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FreelandTests {

	@Autowired
	private ServiceContractOrchestrator serviceContractOrchestrator;

	@Test
	void contextLoads() {
		serviceContractOrchestrator.saveServiceContract();
	}



}
