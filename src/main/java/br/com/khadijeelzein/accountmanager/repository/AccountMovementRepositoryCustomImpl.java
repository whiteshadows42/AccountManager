package br.com.khadijeelzein.accountmanager.repository;

import br.com.khadijeelzein.accountmanager.dto.AccountMovementResponse;
import br.com.khadijeelzein.accountmanager.mapper.AccountMovementMapper;
import br.com.khadijeelzein.accountmanager.model.AccountMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountMovementRepositoryCustomImpl implements AccountMovementRepositoryCustom {
    MongoTemplate mongoTemplate;

    public AccountMovementRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<AccountMovementResponse> findAllByDateAndAccountOriginOrAccountDestination(LocalDate from, LocalDate to, Long account, Pageable pageable) {
        var criteria = new Criteria();
        if (from != null && from.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data inicial não pode ser futura");
        }else if (from != null && to != null && from.isBefore(to)) {
            criteria = Criteria.where("dateTime").gte(from.atStartOfDay()).lte(to.atStartOfDay());
        } else if (from != null && to != null && from.isAfter(to)){
                throw new IllegalArgumentException("Data inicial não pode ser depois da final");
        }else if(from!=null){
            criteria = Criteria.where("dateTime").gte(from.atStartOfDay());
        } else if (to != null){
            criteria = Criteria.where("dateTime").lte(to.atStartOfDay());
        }
        criteria.orOperator(Criteria.where("accountOrigin").is(account),
                Criteria.where("accountDestination").is(account));
        Query query = new Query(criteria);
        var count = mongoTemplate.count(query, AccountMovement.class);
        query.with(pageable);
        var sort = pageable.getSort();
        var list = mongoTemplate.find(query, AccountMovement.class);
        List<AccountMovementResponse> accountMovementResponses = new ArrayList<>();
        if(!list.isEmpty())
            accountMovementResponses = AccountMovementMapper.toAccountMovementResponseList(list);
        return PageableExecutionUtils.getPage(accountMovementResponses, pageable, () -> count);
    }
}
