package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.handlerApi.Interface.UserRepositoryCustom;
import com.handler.excel2word.handlerApi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /* ===== FIND ALL ===== */
    @Override
    public List<User> findAll() {
        return em.createQuery(
                "select distinct u from User u left join fetch u.authorities",
                User.class
        ).getResultList();
    }

    /* ===== FIND BY ID ===== */
    @Override
    public User findById(Long id) {
        return em.createQuery(
                        "select u from User u left join fetch u.authorities where u.id = :id",
                        User.class
                ).setParameter("id", id)
                .getSingleResult();
    }

    /* ===== SEARCH ===== */
    @Override
    public List<User> searchByKeyword(String keyword) {
        String jpql = "select distinct u from User u  left join fetch u.authorities where " +
                "lower(u.login) like :kw or lower(u.firstName) like :kw or lower(u.lastName) like :kw ";

        return em.createQuery(jpql, User.class)
                .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                .getResultList();
    }

    /* ===== SAVE (CREATE / UPDATE) ===== */
    @Override
    public User save(User user) {
        if (user.getId() == null) {
            em.persist(user);
            return user;
        }
        return em.merge(user);
    }

    /* ===== DELETE ===== */
    @Override
    public void deleteById(Long id) {
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }

    @Override
    public Page<User> searchByKeyword(String keyword, Pageable pageable) {

        String baseQuery = "from User u where lower(u.login) " +
                "like :kw or lower(u.firstName) like :kw or lower(u.lastName) like :kw ";

        // ===== QUERY DATA =====
        TypedQuery<User> query = em.createQuery(
                "select u " + baseQuery + " order by u.id desc",
                User.class
        );

        query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<User> result = query.getResultList();

        // ===== QUERY COUNT =====
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(u) " + baseQuery,
                Long.class
        );

        countQuery.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(result, pageable, total);
    }
}
