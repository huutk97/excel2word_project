package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.core.utils.StringUtil;
import com.handler.excel2word.handlerApi.Interface.SoThuLyKiemSoatRepositoryCustom;
import com.handler.excel2word.handlerApi.entity.SoThuLyKiemSoat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class SoThuLyKiemSoatRepositoryImpl
        implements SoThuLyKiemSoatRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SoThuLyKiemSoat> findByDateRange(Date from, Date to, Long userId, String account) {

        StringBuilder jpql = new StringBuilder(
                "SELECT s FROM SoThuLyKiemSoat s WHERE 1=1 "
        );

        if (from != null) {
            jpql.append(" AND TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') >= :from ");
        }

        if (to != null) {
            jpql.append(" AND TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') <= :to ");
        }
        if (userId != null) {
            jpql.append(" AND s.userId = :userId ");
        }
        if (StringUtil.isNotBlank(account)) {
            jpql.append(" AND lower(s.account) = :account ");
        }

        jpql.append(" ORDER BY s.orderNumber ASC, s.id DESC");

        TypedQuery<SoThuLyKiemSoat> query =
                em.createQuery(jpql.toString(), SoThuLyKiemSoat.class);

        if (from != null) {
            query.setParameter("from", from);
        }

        if (to != null) {
            query.setParameter("to", to);
        }
        if (userId != null) {
            query.setParameter("userId", userId);
        }
        if (StringUtil.isNotBlank(account)) {
            query.setParameter("account", account.toLowerCase());
        }

        return query.getResultList();
    }

    @Override
    public Page<SoThuLyKiemSoat> findByDateRange(
            Date from, Date to, Long userId, String account,
            Pageable pageable) {

        String baseWhere = " WHERE 1=1 ";

        if (from != null) {
            baseWhere += " AND TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') >= :from ";
        }

        if (to != null) {
            baseWhere += " AND TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') <= :to ";
        }
        if (userId != null) {
            baseWhere += " AND s.userId = :userId ";
        }
        if (StringUtil.isNotBlank(account)) {
            baseWhere += " AND lower(s.account) = :account ";
        }

        // ===== QUERY DATA =====
        String dataJpql =
                "SELECT s FROM SoThuLyKiemSoat s " +
                        baseWhere +
                        buildOrderBy(pageable);

        TypedQuery<SoThuLyKiemSoat> query =
                em.createQuery(dataJpql, SoThuLyKiemSoat.class);

        // set param
        if (from != null) query.setParameter("from", from);
        if (to != null) query.setParameter("to", to);
        if (userId != null) query.setParameter("userId", userId);
        if (StringUtil.isNotBlank(account)) {
            query.setParameter("account", account.toLowerCase());
        }

        // paging
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<SoThuLyKiemSoat> data = query.getResultList();

        // ===== QUERY COUNT =====
        String countJpql =
                "SELECT COUNT(s) FROM SoThuLyKiemSoat s " + baseWhere;

        TypedQuery<Long> countQuery =
                em.createQuery(countJpql, Long.class);

        if (from != null) countQuery.setParameter("from", from);
        if (to != null) countQuery.setParameter("to", to);
        if (userId != null) countQuery.setParameter("userId", userId);
        if (StringUtil.isNotBlank(account)) {
            countQuery.setParameter("account", account.toLowerCase());
        }

        Long total = countQuery.getSingleResult();

        return new PageImpl<>(data, pageable, total);
    }

    private String buildOrderBy(Pageable pageable) {

        if (!pageable.getSort().isSorted()) {
            return " ORDER BY s.orderNumber ASC, s.id DESC";
        }

        StringBuilder sb = new StringBuilder(" ORDER BY ");

        pageable.getSort().forEach(order -> {
            sb.append("s.")
                    .append(order.getProperty())
                    .append(" ")
                    .append(order.getDirection())
                    .append(",");
        });

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
