package com.gymmis.service;

import com.gymmis.dao.MemberDao;
import com.gymmis.entity.Member;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class MemberService {

    @Inject
    MemberDao memberDao;

    public List<Member> findAll() throws SQLException {
        return memberDao.findAll();
    }

    public Member findById(Long id) throws SQLException {
        return memberDao.findById(id);
    }

    @Transactional
    public Member save(Member member) throws SQLException {
        memberDao.save(member);
        return member;
    }

    @Transactional
    public void delete(Long id) throws SQLException {
        memberDao.delete(id);
    }
}