/********************************************************************************
 * Copyright (c) 2019-2021 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 ********************************************************************************/
package org.eclipse.glsp.server.features.core.model;

import java.util.List;

import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.server.actions.AbstractActionHandler;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.utils.LayoutUtil;

import com.google.inject.Inject;

public class ComputedBoundsActionHandler extends AbstractActionHandler<ComputedBoundsAction> {

   @Inject
   protected ModelSubmissionHandler submissionHandler;

   @Inject
   protected GModelState modelState;

   @Override
   public List<Action> executeAction(final ComputedBoundsAction action) {
      synchronized (submissionHandler.getModelLock()) {
         GModelRoot model = modelState.getRoot();
         if (model != null && model.getRevision() == action.getRevision()) {
            LayoutUtil.applyBounds(model, action, modelState);
            return submissionHandler.submitModelDirectly();
         }
      }
      return none();
   }

}
