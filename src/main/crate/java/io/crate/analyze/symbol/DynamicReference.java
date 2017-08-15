/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.analyze.symbol;

import io.crate.metadata.ReferenceIdent;
import io.crate.metadata.ReferenceInfo;
import io.crate.metadata.RowGranularity;
import io.crate.metadata.table.ColumnPolicy;
import io.crate.types.DataType;
import io.crate.types.UndefinedType;

public class DynamicReference extends Reference {

    public static final SymbolFactory FACTORY = new SymbolFactory() {
        @Override
        public Symbol newInstance() {
            return new DynamicReference();
        }
    };

    public DynamicReference() {}

    public DynamicReference(ReferenceInfo info) {
        this.info = info;
    }

    public DynamicReference(ReferenceIdent ident, RowGranularity rowGranularity) {
        this.info = new ReferenceInfo(ident, rowGranularity, UndefinedType.INSTANCE);
    }

    public void valueType(DataType dataType) {
        assert this.info != null;
        this.info = new ReferenceInfo(info.ident(), info.granularity(), dataType,
                info.columnPolicy(), info.indexType());
    }

    public void columnPolicy(ColumnPolicy objectType) {
        assert this.info != null;
        this.info = new ReferenceInfo(info.ident(), info.granularity(), info.type(),
                objectType, info.indexType());
    }

    @Override
    public SymbolType symbolType() {
        return SymbolType.DYNAMIC_REFERENCE;
    }

    @Override
    public <C, R> R accept(SymbolVisitor<C, R> visitor, C context) {
        return visitor.visitDynamicReference(this, context);
    }
}
