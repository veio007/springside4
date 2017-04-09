package com.veio;

/*
 * 1 数据库事务 ACID 原子性 一致性 隔离性 持久性
 * 2 数据并发问题
 *  数据读问题 =>
 *      脏读 A事务读取到B事务未提交的更改数据
 *      不可重复读 A事务读到B事务提交的更改数据(A事务过程中B事务修改数据，
 *          现象是A事务两次查询的结果不一致)
 *      幻象读 A事务读取到B事务提交的新增数据(和不可重复读类似，区别是幻象读
 *          是读取到新增数据)
 *  数据更新问题 =>
 *      第一类丢失更新 A事务撤消时，把B事务提交的更新数据覆盖掉
 *      第二类丢失更新 A事务提交时覆盖B事务已提交的数据，造成B事务数据丢失
 *  3 事务隔离级别
 *      read uncommited, read commited, repeatable read, serializable
 *      sql92 推荐使用repeatable级别
 *  4 设置不自动提交
 *      conn.setAutoCommit(false)
 *  5 设置事务隔离级别
 *      conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE)
 *  6 提交事务 回滚事务 回滚事务到保务点
 *      conn.commit() conn.rollback()
 *      Savepoint s = conn.setSavepoint("savePoint"); conn.rollback(s)
 *
 *
 */


public class Main {
	public static void main(String[] args) {
		System.out.println("ok");
	}
}
