/*
Copyright 2017 Mattia Atzeni, Maurizio Atzori

This file is part of CodeOntology.

CodeOntology is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CodeOntology is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CodeOntology.  If not, see <http://www.gnu.org/licenses/>
*/

package org.codeontology.extraction.statement;

import org.apache.jena.rdf.model.RDFNode;
import org.codeontology.Ontology;
import org.codeontology.extraction.CodeElementEntity;
import org.codeontology.extraction.Entity;
import org.codeontology.extraction.expression.ExpressionEntity;
import org.codeontology.extraction.support.LineTagger;
import org.codeontology.extraction.support.VariableDeclarationEntity;
import org.codeontology.extraction.support.VariableDeclarationTagger;
import spoon.reflect.declaration.CtField;

public class FieldDeclaration extends CodeElementEntity<CtField<?>> implements VariableDeclarationEntity<CtField<?>> {

    public FieldDeclaration(CtField<?> element) {
        super(element);
    }

    @Override
    protected RDFNode getType() {
        return null;
    }

    @Override
    public void extract() {
        tagType();
        tagLine();
        tagVariable();
        tagInitializer();
        tagSourceCode();
    }

    @Override
    public ExpressionEntity<?> getInitializer() {
        return VariableDeclarationTagger.initializerOf(this);
    }

    @Override
    public void tagInitializer() {
        new VariableDeclarationTagger(this).tagInitializer();
    }

    @Override
    public Entity<?> getVariable() {
        return VariableDeclarationTagger.declaredVariableOf(this);
    }

    @Override
    public void tagVariable() {
        getLogger().addTriple(getParent(), Ontology.DECLARATION_PROPERTY, this);
    }

    public void tagLine() {
        new LineTagger(this).tagLine();
    }
}