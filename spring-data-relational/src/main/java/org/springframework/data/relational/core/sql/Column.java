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

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Column name within a {@code SELECT … FROM} clause.
 * <p/>
 * Renders to: {@code <name>} or {@code <table(alias)>.<name>}.
 *
 * @author Mark Paluch
 */
public class Column extends AbstractSegment implements Expression, Named {

	private final String name;
	private final Table table;

	Column(String name, Table table) {

		Assert.notNull(name, "Name must not be null");

		this.name = name;
		this.table = table;
	}

	/**
	 * Creates a new {@link Column} associated with a {@link Table}.
	 *
	 * @param name column name, must not {@literal null} or empty.
	 * @param table the table, must not be {@literal null}.
	 * @return the new {@link Column}.
	 */
	public static Column create(String name, Table table) {

		Assert.hasText(name, "Name must not be null or empty");
		Assert.notNull(table, "Table must not be null");

		return new Column(name, table);
	}

	/**
	 * Creates a new aliased {@link Column} associated with a {@link Table}.
	 *
	 * @param name column name, must not {@literal null} or empty.
	 * @param table the table, must not be {@literal null}.
	 * @param alias column alias name, must not {@literal null} or empty.
	 * @return the new {@link Column}.
	 */
	public static Column aliased(String name, Table table, String alias) {

		Assert.hasText(name, "Name must not be null or empty");
		Assert.notNull(table, "Table must not be null");
		Assert.hasText(alias, "Alias must not be null or empty");

		return new AliasedColumn(name, table, alias);
	}

	/**
	 * Create a new aliased {@link Column}.
	 *
	 * @param alias column alias name, must not {@literal null} or empty.
	 * @return the aliased {@link Column}.
	 */
	public Column as(String alias) {

		Assert.hasText(alias, "Alias must not be null or empty");

		return new AliasedColumn(name, table, alias);
	}

	/**
	 * Create a new {@link Column} associated with a {@link Table}.
	 *
	 * @param table the table, must not be {@literal null}.
	 * @return a new {@link Column} associated with {@link Table}.
	 */
	public Column from(Table table) {

		Assert.notNull(table, "Table must not be null");

		return new Column(name, table);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.sql.Visitable#visit(org.springframework.data.relational.core.sql.Visitor)
	 */
	@Override
	public void visit(Visitor visitor) {

		Assert.notNull(visitor, "Visitor must not be null!");

		visitor.enter(this);

		if (table != null) {
			table.visit(visitor);
		}
		visitor.leave(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.sql.Named#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the column name as it is used in references. This can be the actual {@link #getName() name} or an
	 *         {@link Aliased#getAlias() alias}.
	 */
	public String getReferenceName() {
		return name;
	}

	/**
	 * @return the {@link Table}. Can be {@literal null} if the column was not referenced in the context of a
	 *         {@link Table}.
	 */
	@Nullable
	public Table getTable() {
		return table;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return getPrefix() + name;
	}

	String getPrefix() {
		String prefix = "";
		if (table != null) {
			prefix = (table instanceof Aliased ? ((Aliased) table).getAlias() : table.getName()) + ".";
		}
		return prefix;
	}

	/**
	 * {@link Aliased} {@link Column} implementation.
	 */
	static class AliasedColumn extends Column implements Aliased {

		private final String alias;

		private AliasedColumn(String name, Table table, String alias) {
			super(name, table);
			this.alias = alias;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.core.sql.Aliased#getAlias()
		 */
		@Override
		public String getAlias() {
			return alias;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.core.sql.Column#getReferenceName()
		 */
		@Override
		public String getReferenceName() {
			return getAlias();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.core.sql.Column#from(org.springframework.data.relational.core.sql.Table)
		 */
		@Override
		public Column from(Table table) {

			Assert.notNull(table, "Table must not be null");

			return new AliasedColumn(getName(), table, getAlias());
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.core.sql.Column#toString()
		 */
		@Override
		public String toString() {
			return getPrefix() + getName() + " AS " + getAlias();
		}
	}
}