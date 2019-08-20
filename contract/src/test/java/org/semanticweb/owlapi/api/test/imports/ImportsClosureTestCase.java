/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.api.test.imports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;

import java.io.File;

import org.junit.Test;
import org.semanticweb.owlapi.api.test.baseclasses.TestBase;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

/**
 * @author Matthew Horridge, The University of Manchester, Bio-Health Informatics Group
 * @since 3.1.0
 */
@SuppressWarnings("javadoc")
public class ImportsClosureTestCase extends TestBase {

    @Test
    public void testImportsClosureUpdate() throws OWLOntologyCreationException {
        IRI aIRI = IRI("http://a.com");
        OWLOntology ontA = m.createOntology(aIRI);
        IRI bIRI = IRI("http://b.com");
        OWLOntology ontB = m.createOntology(bIRI);
        m.applyChange(new AddImport(ontA, df.getOWLImportsDeclaration(bIRI)));
        assertEquals(2, m.getImportsClosure(ontA).size());
        m.removeOntology(ontB);
        assertEquals(1, m.getImportsClosure(ontA).size());
        m.createOntology(bIRI);
        assertEquals(2, m.getImportsClosure(ontA).size());
    }

    @Test
    public void testAutoIRIMapperShouldNotBeConfusedByPrefixes() {
        AutoIRIMapper mapper = new AutoIRIMapper(new File(RESOURCES, "imports"), true);
        assertTrue(mapper.getOntologyIRIs()
            .contains(IRI.create("http://owlapitestontologies.com/thesubont")));
    }

    @Test
    public void testAutoIRIMapperShouldRecogniseRdfAboutInOwlOntology() {
        AutoIRIMapper mapper = new AutoIRIMapper(new File(RESOURCES, "imports"), true);
        assertTrue(
            mapper.getOntologyIRIs().contains(IRI.create("http://test.org/compleximports/A.owl")));
    }
}