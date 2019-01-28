/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.relational.core.sql;

import java.util.Collection;

/**
 * Entry point to construct a {@link Select} statement.
 *
 * @author Mark Paluch
 */
public interface SelectBuilder {

	/**
	 * Apply a {@code TOP} clause given {@code count}.
	 *
	 * @param count the top count.
	 * @return {@code this} {@link SelectBuilder}.
	 * @see SelectTop
	 */
	SelectBuilder top(int count);

	/**
	 * Include a {@link Expression} in the select list.
	 *
	 * @param expression the expression to include.
	 * @return {@code this} builder.
	 * @see Table#column(String)
	 */
	SelectAndFrom select(Expression expression);

	/**
	 * Include one or more {@link Expression}s in the select list.
	 *
	 * @param expressions the expressions to include.
	 * @return {@code this} builder.
	 * @see Table#columns(String...)
	 */
	SelectAndFrom select(Expression... expressions);

	/**
	 * Include one or more {@link Expression}s in the select list.
	 *
	 * @param expressions the expressions to include.
	 * @return {@code this} builder.
	 * @see Table#columns(String...)
	 */
	SelectAndFrom select(Collection<? extends Expression> expressions);

	/**
	 * Builder exposing {@code select} and {@code from} methods.
	 */
	interface SelectAndFrom extends SelectFrom {

		/**
		 * Include a {@link Expression} in the select list. Multiple calls to this or other {@code select} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param expression the expression to include.
		 * @return {@code this} builder.
		 * @see Table#column(String)
		 */
		SelectFrom select(Expression expression);

		/**
		 * Include one or more {@link Expression}s in the select list.  Multiple calls to this or other {@code select} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param expressions the expressions to include.
		 * @return {@code this} builder.
		 * @see Table#columns(String...)
		 */
		SelectFrom select(Expression... expressions);

		/**
		 * Include one or more {@link Expression}s in the select list.  Multiple calls to this or other {@code select} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param expressions the expressions to include.
		 * @return {@code this} builder.
		 * @see Table#columns(String...)
		 */
		SelectFrom select(Collection<? extends Expression> expressions);

		/**
		 * Declare a {@link Table} to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param table the table to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		@Override
		SelectFromAndJoin from(Table table);

		/**
		 * Declare one or more {@link Table}s to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param tables the tables to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		@Override
		SelectFromAndJoin from(Table... tables);

		/**
		 * Declare one or more {@link Table}s to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param tables the tables to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		@Override
		SelectFromAndJoin from(Collection<? extends Table> tables);
	}

	/**
	 * Builder exposing {@code from} methods.
	 */
	interface SelectFrom extends BuildSelect {

		/**
		 * Declare a {@link Table} to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param table the table name to {@code SELECT … FROM} must not be {@literal null} or empty.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		SelectFromAndOrderBy from(String table);

		/**
		 * Declare a {@link Table} to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param table the table to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		SelectFromAndOrderBy from(Table table);

		/**
		 * Declare one or more {@link Table}s to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param tables the tables to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		SelectFromAndOrderBy from(Table... tables);

		/**
		 * Declare one or more {@link Table}s to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param tables the tables to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		SelectFromAndOrderBy from(Collection<? extends Table> tables);
	}

	/**
	 * Builder exposing {@code from} and {@code order by} methods.
	 */
	interface SelectFromAndOrderBy extends SelectFrom, SelectOrdered, SelectLimitOffset, BuildSelect {

		@Override
		SelectFromAndOrderBy limitOffset(long limit, long offset);

		@Override
		SelectFromAndOrderBy limit(long limit);

		@Override
		SelectFromAndOrderBy offset(long offset);

		@Override
		SelectFromAndOrderBy from(String table);

		@Override
		SelectFromAndOrderBy from(Table table);

		@Override
		SelectFromAndOrderBy from(Table... tables);

		@Override
		SelectFromAndOrderBy from(Collection<? extends Table> tables);

		@Override
		SelectFromAndOrderBy orderBy(Column... columns);

		@Override
		SelectFromAndOrderBy orderBy(int... indexes);

		@Override
		SelectFromAndOrderBy orderBy(OrderByField... orderByFields);

		@Override
		SelectFromAndOrderBy orderBy(Collection<? extends OrderByField> orderByFields);
	}

	interface SelectFromAndJoin extends SelectFromAndOrderBy, BuildSelect, SelectJoin, SelectWhere, SelectLimitOffset {

		/**
		 * Declare a {@link Table} to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param table the table to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		@Override
		SelectFromAndJoin from(Table table);

		/**
		 * Declare one or more {@link Table}s to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param tables the tables to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		@Override
		SelectFromAndJoin from(Table... tables);

		/**
		 * Declare one or more {@link Table}s to {@code SELECT … FROM}.
		 * Multiple calls to this or other {@code from} methods keep adding items to the select list and do not replace previously contained items.
		 *
		 * @param tables the tables to {@code SELECT … FROM} must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see From
		 * @see SQL#table(String)
		 */
		@Override
		SelectFromAndJoin from(Collection<? extends Table> tables);

		/**
		 * Apply {@code limit} and {@code offset} parameters to the select statement.
		 * To read the first 20 rows from start use {@code limitOffset(20, 0)}. to read the next 20 use {@code limitOffset(20, 20)}.
		 *
		 * @param limit rows to read.
		 * @param offset row offset, zero-based.
		 * @return {@code this} builder.
		 */
		SelectFromAndJoin limitOffset(long limit, long offset);

		/**
		 * Apply a limit of rows to read.
		 *
		 * @param limit rows to read.
		 * @return {@code this} builder.
		 */
		SelectFromAndJoin limit(long limit);

		/**
		 * Apply an offset where to start reading rows.
		 *
		 * @param offset start offset.
		 * @return {@code this} builder.
		 */
		SelectFromAndJoin offset(long offset);
	}

	/**
	 * Builder exposing join/where/and {@code JOIN … ON} continuation methods.
	 */
	interface SelectFromAndJoinCondition extends BuildSelect, SelectJoin, SelectWhere, SelectOnCondition, SelectLimitOffset {

		/**
		 * Apply {@code limit} and {@code offset} parameters to the select statement.
		 * To read the first 20 rows from start use {@code limitOffset(20, 0)}. to read the next 20 use {@code limitOffset(20, 20)}.
		 *
		 * @param limit rows to read.
		 * @param offset row offset, zero-based.
		 * @return {@code this} builder.
		 */
		SelectFromAndJoin limitOffset(long limit, long offset);

		/**
		 * Apply a limit of rows to read.
		 *
		 * @param limit rows to read.
		 * @return {@code this} builder.
		 */
		SelectFromAndJoin limit(long limit);

		/**
		 * Apply an offset where to start reading rows.
		 *
		 * @param offset start offset.
		 * @return {@code this} builder.
		 */
		SelectFromAndJoin offset(long offset);
	}

	/**
	 * Limit/offset methods.
	 */
	interface SelectLimitOffset {

		/**
		 * Apply {@code limit} and {@code offset} parameters to the select statement.
		 * To read the first 20 rows from start use {@code limitOffset(20, 0)}. to read the next 20 use {@code limitOffset(20, 20)}.
		 *
		 * @param limit rows to read.
		 * @param offset row offset, zero-based.
		 * @return {@code this} builder.
		 */
		SelectLimitOffset limitOffset(long limit, long offset);

		/**
		 * Apply a limit of rows to read.
		 *
		 * @param limit rows to read.
		 * @return {@code this} builder.
		 */
		SelectLimitOffset limit(long limit);

		/**
		 * Apply an offset where to start reading rows.
		 *
		 * @param offset start offset.
		 * @return {@code this} builder.
		 */
		SelectLimitOffset offset(long offset);
	}

	/**
	 * Builder exposing {@code ORDER BY} methods.
	 */
	interface SelectOrdered extends BuildSelect {

		/**
		 * Add one or more {@link Column columns} to order by.
		 *
		 * @param columns the columns to order by.
		 * @return {@code this} builder.
		 */
		SelectOrdered orderBy(Column... columns);

		/**
		 * Add an order by field using {@code indexes} using default sort semantics.
		 *
		 * @param indexes field indexes as declared in the select list.
		 * @return {@code this} builder.
		 * @see OrderByField#index(int)
		 */
		SelectOrdered orderBy(int... indexes);

		/**
		 * Add one or more {@link OrderByField order by fields}.
		 *
		 * @param orderByFields the fields to order by.
		 * @return {@code this} builder.
		 */
		SelectOrdered orderBy(OrderByField... orderByFields);

		/**
		 * Add one or more {@link OrderByField order by fields}.
		 *
		 * @param orderByFields the fields to order by.
		 * @return {@code this} builder.
		 */
		SelectOrdered orderBy(Collection<? extends OrderByField> orderByFields);
	}

	/**
	 * Interface exposing {@code WHERE} methods.
	 */
	interface SelectWhere extends SelectOrdered, BuildSelect {

		/**
		 * Apply a {@code WHERE} clause.
		 *
		 * @param condition the {@code WHERE} condition.
		 * @return {@code this} builder.
		 * @see Where
		 * @see Condition
		 */
		SelectWhereAndOr where(Condition condition);
	}

	/**
	 * Interface exposing {@code AND}/{@code OR} combinatior methods for {@code WHERE} {@link Condition}s.
	 */
	interface SelectWhereAndOr extends SelectOrdered, BuildSelect {

		/**
		 * Combine the previous {@code WHERE} {@link Condition} using {@code AND}.
		 *
		 * @param condition the condition, must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see Condition#and(Condition)
		 */
		SelectWhereAndOr and(Condition condition);

		/**
		 * Combine the previous {@code WHERE} {@link Condition} using {@code OR}.
		 *
		 * @param condition the condition, must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see Condition#or(Condition)
		 */
		SelectWhereAndOr or(Condition condition);
	}

	/**
	 * Interface exposing {@code JOIN} methods.
	 */
	interface SelectJoin extends BuildSelect {

		/**
		 * Declare a {@code JOIN} {@code table}.
		 *
		 * @param table name of the table, must not be {@literal null} or empty.
		 * @return {@code this} builder.
		 * @see Join
		 * @see SQL#table(String)
		 */
		SelectOn join(String table);

		/**
		 * Declare a {@code JOIN} {@link Table}.
		 *
		 * @param table name of the table, must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see Join
		 * @see SQL#table(String)
		 */
		SelectOn join(Table table);
	}

	/**
	 * Interface exposing {@code ON} methods to declare {@code JOIN} relationships.
	 */
	interface SelectOn {


		/**
		 * Declare the source column in the {@code JOIN}.
		 *
		 * @param column the source column, must not be {@literal null} or empty.
		 * @return {@code this} builder.
		 * @see Table#column(String)
		 */
		SelectOnConditionComparison on(Expression column);
	}

	/**
	 * Interface declaring the target column comparison relationship.
	 */
	interface SelectOnConditionComparison {

		/**
		 * Declare an equals {@link Condition} between the source column and the target {@link Column}.
		 *
		 * @param column the target column, must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see Table#column(String)
		 */
		SelectFromAndJoinCondition equals(Expression column);
	}

	/**
	 * Builder exposing JOIN and {@code JOIN … ON} continuation methods.
	 */
	interface SelectOnCondition extends SelectJoin, BuildSelect {

		/**
		 * Declare an additional source column in the {@code JOIN}.
		 *
		 * @param column the column, must not be {@literal null}.
		 * @return {@code this} builder.
		 * @see Table#column(String)
		 */
		SelectOnConditionComparison and(Expression column);
	}

	/**
	 * Interface exposing the {@link Select} build method.
	 */
	interface BuildSelect {

		/**
		 * Build the {@link Select} statement and verify basic relationship constraints such as all referenced columns have a {@code FROM} or {@code JOIN} table import.
		 *
		 * @return the build and immutable {@link Select} statement.
		 */
		Select build();
	}
}