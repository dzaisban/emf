/**
 * <copyright>
 *
 * Copyright (c) 2004 IBM Corporation and others.
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
 * $Id: EcoreUtilStaticMethodsTest.java,v 1.4.2.3 2005/06/08 18:27:46 nickb Exp $
 */
package org.eclipse.emf.test.core.ecore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.test.core.TestUtil;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDFactory;

public class EcoreUtilStaticMethodsTest extends TestCase
{
  public EcoreUtilStaticMethodsTest(String name)
  {
    super(name);
  }
  
  public static Test suite()
  {
    TestSuite testSuite = new TestSuite("EcoreUtilStaticMethodsTest");
    testSuite.addTest(new EcoreUtilStaticMethodsTest("testGenerateUUID"));
    testSuite.addTest(new EcoreUtilStaticMethodsTest("testIndexOf"));
    testSuite.addTest(new EcoreUtilStaticMethodsTest("testSetEList"));
    //testSuite.addTest(new EcoreUtilStaticMethodsTest("testCopyUnsettableSetEmptyList")); // not supported in 2.0.2 (yet)
    return testSuite;
  }
  
  public void testCopyUnsettableSetEmptyList()
  {
    XSDComplexTypeDefinition xsdComplexTypeDefinition = XSDFactory.eINSTANCE.createXSDComplexTypeDefinition();
    xsdComplexTypeDefinition.getLexicalFinal().clear();
    assertTrue(xsdComplexTypeDefinition.isSetLexicalFinal());
    XSDComplexTypeDefinition xsdComplexTypeDefinitionCopy = (XSDComplexTypeDefinition)EcoreUtil.copy(xsdComplexTypeDefinition);
    assertTrue(xsdComplexTypeDefinitionCopy.isSetLexicalFinal());
  }
  
  public void testIndexOf()
  {
    assertIndexOf(populateList(new ArrayList()));
    assertIndexOf(populateList(new LinkedList()));
    assertIndexOf(populateList(new BasicEList()));
  }
  
  public void testSetEList()
  {
    assertSetEList(populateList(new ArrayList()));
    assertSetEList(populateList(new LinkedList()));
    assertSetEList(populateList(new UniqueEList()));
  }
  
  public void testGenerateUUID()
  {
    final Collection set = new HashSet();
    
    set.add(EcoreUtil.generateUUID());
    set.add(EcoreUtil.generateUUID());
    set.add(EcoreUtil.generateUUID());
    assertEquals(3, set.size());
    
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        set.add(EcoreUtil.generateUUID());

        try
        {
          Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
        }
        
        set.add(EcoreUtil.generateUUID());
        set.add(EcoreUtil.generateUUID());        
      }
    };
    
    Thread thread1 = new Thread(runnable);
    Thread thread2 = new Thread(runnable);
    Thread thread3 = new Thread(runnable);
    
    try
    {
      thread1.start(); thread1.join();
      thread2.start(); thread2.join();
      thread3.start(); thread3.join();
    }
    catch (InterruptedException e)
    {
    }    
    assertEquals(12, set.size());
  }  
  
  protected List populateList(List list)
  {
    list.add(null);            //0
    list.add(Boolean.FALSE);   //1
    list.add(new Integer(1));  //2
    list.add(new Integer(2));  //3
    list.add(null);            //4
    list.add("String");        //5
    list.add(new Integer(1));  //6
    list.add("String");        //7
    list.add(Boolean.FALSE);   //8

    return list;
  }
  
  protected void assertIndexOf(List list)
  {
    assertEquals(0, EcoreUtil.indexOf(list, null, 0));
    assertEquals(4, EcoreUtil.indexOf(list, null, 1));
    assertEquals(4, EcoreUtil.indexOf(list, null, 4));
    
    assertEquals(1, EcoreUtil.indexOf(list, Boolean.FALSE, 1));
    assertEquals(8, EcoreUtil.indexOf(list, Boolean.FALSE, 2));
    assertEquals(-1, EcoreUtil.indexOf(list, Boolean.FALSE, 9));
    assertEquals(1, EcoreUtil.indexOf(list, Boolean.FALSE, -2));
    
    assertEquals(2, EcoreUtil.indexOf(list, new Integer(1), 0));
    assertEquals(2, EcoreUtil.indexOf(list, new Integer(1), 2));
    assertEquals(6, EcoreUtil.indexOf(list, new Integer(1), 3));
    
    assertEquals(5, EcoreUtil.indexOf(list, "String", 3));
    
    assertEquals(-1, EcoreUtil.indexOf(list, null, 1000));
    assertEquals(-1, EcoreUtil.indexOf(list, "String", 1000));
  }
    
  protected void assertSetEList(List prototypeList)
  {
    EList eList = new BasicEList();
    EcoreUtil.setEList(eList, prototypeList);
    assertTrue("Empty list test", TestUtil.areEqual(prototypeList, eList));
    
    eList = new BasicEList();
    eList.add(0, "String");
    eList.add(Boolean.FALSE);
    EcoreUtil.setEList(eList, prototypeList);
    assertTrue("Smaller list test", TestUtil.areEqual(prototypeList, eList));
    
    eList = (EList)populateList(new BasicEList());
    EcoreUtil.setEList(eList, prototypeList);
    assertTrue("Same list test", TestUtil.areEqual(prototypeList, eList));

    eList.remove(2);
    eList.add(3, this);
    EcoreUtil.setEList(eList, prototypeList);
    assertTrue("Equal size list test", TestUtil.areEqual(prototypeList, eList));

    eList.add(0, "String");
    eList.add(2, Boolean.FALSE);
    eList.add(Boolean.FALSE);
    eList.add(this);
    EcoreUtil.setEList(eList, prototypeList);
    assertTrue("Bigger list test", TestUtil.areEqual(prototypeList, eList));
  }
}