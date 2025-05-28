package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.model.Account;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {

    MongoTemplate mongoTemplate;

    public AccountRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void updateAccountByAccountNbr(List<Account> accounts) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, Account.class);
        for (Account account : accounts) {
            Update update = new Update().set("currentBalance", account.getCurrentBalance());
            FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(false);
            mongoTemplate.findAndModify(
                    query(where("accountNbr").is(account.getAccountNbr())),
                    update,
                    options,
                    Account.class
            );
        }
    }
}
