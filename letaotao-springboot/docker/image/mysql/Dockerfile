FROM mysql:8.0.31

ADD https://raw.githubusercontent.com/shengqiangdd/letaotao/refs/heads/main/sql/letao_config.sql /docker-entrypoint-initdb.d/letao_config.sql
ADD https://raw.githubusercontent.com/shengqiangdd/letaotao/refs/heads/main/sql/letao_trade.sql  /docker-entrypoint-initdb.d/letao_trade.sql

RUN chown -R mysql:mysql /docker-entrypoint-initdb.d/*
EXPOSE 3306
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]