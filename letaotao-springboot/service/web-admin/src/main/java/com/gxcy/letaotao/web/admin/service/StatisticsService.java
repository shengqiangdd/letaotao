package com.gxcy.letaotao.web.admin.service;


public interface StatisticsService {
    long countRegisteredUsers(Integer year, Integer month);

    long countProductsOnSale(Integer year, Integer month);

    long countProductComments(Integer year, Integer month);

    long countOrders(Integer year, Integer month);

    long countPosts(Integer year, Integer month);

    long countPostComments(Integer year, Integer month);
}
