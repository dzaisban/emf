/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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
 * $Id: OpenEMFProjectWizardAction.java,v 1.1 2006/06/15 23:33:43 marcelop Exp $
 */
package org.eclipse.emf.cheatsheets.actions;

import org.eclipse.emf.importer.ui.EMFModelWizard;
import org.eclipse.emf.importer.ui.EMFProjectWizard;


/**
 * <p>Action that opens the new EMF project wizard.
 * @since 2.2.0
 */
public class OpenEMFProjectWizardAction extends OpenEMFModelWizardAction
{
  /**
   * Create a new EMF model wizard
   * @return New EMF project wizard
   */
  protected EMFModelWizard createEMFModelWizard()
  {
    return new EMFProjectWizard();
  }
}