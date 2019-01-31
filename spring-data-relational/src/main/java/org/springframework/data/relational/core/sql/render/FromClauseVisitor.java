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
package org.springframework.data.relational.core.sql.render;

import org.springframework.data.relational.core.sql.From;
import org.springframework.data.relational.core.sql.Visitable;

/**
 * Renderer for {@link From}. Uses a {@link RenderTarget} to call back for render results.
 *
 * @author Mark Paluch
 * @author Jens Schauder
 */
class FromClauseVisitor extends TypedSubtreeVisitor<From> {

	private final StringBuilder builder = new StringBuilder();
	private final RenderTarget parent;

	private boolean first = true;
	private final FromTableVisitor visitor = new FromTableVisitor(it -> {

		if (first) {
			first = false;
		} else {
			builder.append(", ");
		}

		builder.append(it);
	});

	FromClauseVisitor(RenderTarget parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.sql.render.TypedSubtreeVisitor#enterNested(org.springframework.data.relational.core.sql.Visitable)
	 */
	@Override
	Delegation enterNested(Visitable segment) {
		return Delegation.delegateTo(visitor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.sql.render.TypedSubtreeVisitor#leaveMatched(org.springframework.data.relational.core.sql.Visitable)
	 */
	@Override
	Delegation leaveMatched(From segment) {
		parent.onRendered(builder);
		return super.leaveMatched(segment);
	}
}
