package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.AccountmanagerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;



@SpringBootTest(classes = AccountmanagerApplication.class)
public abstract class AbstractBaseIntegrationTest {
    @Autowired
    Environment env;
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

    }
}
