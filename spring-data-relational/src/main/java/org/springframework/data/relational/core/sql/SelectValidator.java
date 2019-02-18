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

import java.util.HashSet;
import java.util.Set;

/**
 * Validator for {@link Select} statements.
 * <p/>
 * Validates that all {@link Column}s using a table qualifier have a table import from either the {@code FROM} or
 * {@code JOIN} clause.
 *
 * @author Mark Paluch
 * @since 1.1
 */
class SelectValidator implements Visitor {

	private int selectFieldCount;
	private Set<Table> requiredBySelect = new HashSet<>();
	private Set<Table> requiredByWhere = new HashSet<>();
	private Set<Table> requiredByOrderBy = new HashSet<>();

	private Set<Table> from = new HashSet<>();
	private Set<Table> join = new HashSet<>();

	private Visitable parent;

	public static void validate(Select select) {
		new SelectValidator().doValidate(select);
	}

	private void doValidate(Select select) {

		select.visit(this);

		if (selectFieldCount == 0) {
			throw new IllegalStateException("SELECT does not declare a select list");
		}

		for (Table table : requiredBySelect) {
			if (!join.contains(table) && !from.contains(table)) {
				throw new IllegalStateException(String
						.format("Required table [%s] by a SELECT column not imported by FROM %s or JOIN %s", table, from, join));
			}
		}

		for (Table table : requiredByWhere) {
			if (!join.contains(table) && !from.contains(table)) {
				throw new IllegalStateException(String
						.format("Required table [%s] by a WHERE predicate not imported by FROM %s or JOIN %s", table, from, join));
			}
		}

		for (Table table : requiredByOrderBy) {
			if (!join.contains(table) && !from.contains(table)) {
				throw new IllegalStateException(String
						.format("Required table [%s] by a ORDER BY column not imported by FROM %s or JOIN %s", table, from, join));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.sql.Visitor#enter(org.springframework.data.relational.core.sql.Visitable)
	 */
	@Override
	public void enter(Visitable segment) {

		if (segment instanceof AsteriskFromTable && parent instanceof Select) {

			Table table = ((AsteriskFromTable) segment).getTable();
			requiredBySelect.add(table);
			selectFieldCount++;
		}

		if (segment instanceof Column && (parent instanceof Select || parent instanceof SimpleFunction)) {

			selectFieldCount++;
			Table table = ((Column) segment).getTable();

			if (table != null) {
				requiredBySelect.add(table);
			}
		}

		if (segment instanceof Table && parent instanceof From) {
			from.add((Table) segment);
		}

		if (segment instanceof Column && parent instanceof OrderByField) {

			Table table = ((Column) segment).getTable();

			if (table != null) {
				requiredByOrderBy.add(table);
			}
		}

		if (segment instanceof Table && parent instanceof Join) {
			join.add((Table) segment);
		}

		if (segment instanceof Where) {

			segment.visit(item -> {

				if (item instanceof Table) {
					requiredByWhere.add((Table) item);
				}
			});
		}

		if (segment instanceof Join || segment instanceof OrderByField || segment instanceof From
				|| segment instanceof Select || segment instanceof Where || segment instanceof SimpleFunction) {
			parent = segment;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.sql.Visitor#leave(org.springframework.data.relational.core.sql.Visitable)
	 */
	@Override
	public void leave(Visitable segment) {}
}
