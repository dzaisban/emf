/**
 * <copyright>
 *
 * Copyright (c) 2002-2004 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: CrossResourceReferencesTest.java,v 1.1.2.2 2005/06/08 18:27:45 nickb Exp $
 */
package org.eclipse.emf.test.xml.xmi;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.example.library.Book;
import org.eclipse.example.library.LibraryFactory;
import org.eclipse.example.library.LibraryPackage;
import org.eclipse.example.library.Writer;


/**
 * XMI tests: loading and serializing library model with cross resource references
 * @see data/library.mdl
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=77423
 */
public class CrossResourceReferencesTest extends TestCase
{

  public CrossResourceReferencesTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite ts = new TestSuite("CrossResourceReferencesTest");
    ts.addTestSuite(CrossResourceReferencesTest.class);
    return ts;
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
  }

  /**
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
  }

  public void testHREF() throws Exception
  {
    ResourceSet rs = new ResourceSetImpl();
    LibraryPackage libPackage = LibraryPackage.eINSTANCE;
    LibraryFactory libFactory = LibraryFactory.eINSTANCE;
    
    Book b = libFactory.createBook();
    b.setTitle("Eclipse Modeling Framework");
    
    Writer w = libFactory.createWriter();
    w.setName("Frank Budinsky");
    
    b.setAuthor(w);
    
    rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());

    Resource r = rs.createResource(URI.createURI("books.xml"));
    r.getContents().add(b);
    
    Resource r2 = rs.createResource(URI.createURI("writers.xml"));
    r2.getContents().add(w);
  
    ByteArrayOutputStream outputstream1 = new ByteArrayOutputStream(2064);
    ByteArrayOutputStream outputstream2 = new ByteArrayOutputStream(2064);
    
    r.save(outputstream1, null);
    r2.save(outputstream2, null);
    
    r.unload();
    r2.unload();
    
    r.load(new ByteArrayInputStream(outputstream1.toByteArray()), null);    
    r2.load(new ByteArrayInputStream(outputstream2.toByteArray()), null);
  }

}