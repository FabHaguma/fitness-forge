package com.gymmis.service;

import com.gymmis.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

public class MemberService {

    @PersistenceContext
    EntityManager entityManager;

    public List<Member> findAll() {
        return entityManager.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    public Member findById(Long id) {
        return entityManager.find(Member.class, id);
    }

    @Transactional
    public Member save(Member member) {
        if (member.getId() == null) {
            entityManager.persist(member);
            return member;
        } else {
            return entityManager.merge(member);
        }
    }

    @Transactional
    public void delete(Long id) {
        Member member = findById(id);
        if (member != null) {
            entityManager.remove(member);
        }
    }
}