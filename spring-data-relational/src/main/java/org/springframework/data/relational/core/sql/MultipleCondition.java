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

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.util.Assert;

/**
 * @author Jens Schauder
 */
public abstract class MultipleCondition implements Condition {

	private final List<Condition> conditions;
	private final String delimiter;

	MultipleCondition(String delimiter, Condition... conditions) {

		this.delimiter = delimiter;
		this.conditions = Arrays.asList(conditions);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.sql.Visitable#visit(org.springframework.data.relational.core.sql.Visitor)
	 */
	@Override
	public void visit(Visitor visitor) {

		Assert.notNull(visitor, "Visitor must not be null!");

		visitor.enter(this);
		conditions.forEach(c -> c.visit(visitor));
		visitor.leave(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringJoiner joiner = new StringJoiner(delimiter);
		conditions.forEach(c -> joiner.add(c.toString()));
		return joiner.toString();
	}
}