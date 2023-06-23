package com.bbva.minibank;

import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.infrastructure.entities.ClientEntity;
import com.bbva.minibank.infrastructure.repositories.ClientRepositoryImpl;
import com.bbva.minibank.infrastructure.repositories.springdatajpa.IClientSpringRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiniBankApplicationTests {

	@Test
	void contextLoads() {

	}


}
