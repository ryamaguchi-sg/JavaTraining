package com.s_giken.traning.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.s_giken.traning.webapp.model.Member;
import com.s_giken.traning.webapp.model.MemberSearchCondition;
import com.s_giken.traning.webapp.repository.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> findById(int memberId) {
        var member = memberRepository.findById(memberId);
        return member;
    }

    @Override
    public List<Member> findByConditions(MemberSearchCondition memberSearchCondition) {
        return memberRepository.findByNameLikeOrMailLike(
                "%" + memberSearchCondition.getName() + "%",
                "%" + memberSearchCondition.getMail() + "%");
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void deleteById(int memberId) {
        memberRepository.deleteById(memberId);
    }
}
