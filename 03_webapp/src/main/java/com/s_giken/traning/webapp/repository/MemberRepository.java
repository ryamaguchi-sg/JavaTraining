package com.s_giken.traning.webapp.repository;

import com.s_giken.traning.webapp.model.Member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    // 参考
    // https://docs.spring.io/spring-data/jpa/docs/1.11.1.RELEASE/reference/html/#jpa.query-methods
    public List<Member> findByNameLikeOrMailLike(String name, String mail);
}
