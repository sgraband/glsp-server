/********************************************************************************
 * Copyright (c) 2019 EclipseSource and others.
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
package org.eclipse.glsp.server.features.directediting;

import java.util.Optional;

import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.operations.AbstractOperationHandler;

import com.google.inject.Inject;

public class ApplyLabelEditOperationHandler extends AbstractOperationHandler<ApplyLabelEditOperation> {

   @Inject
   protected GModelState modelState;

   @Override
   public void executeOperation(final ApplyLabelEditOperation operation) {
      Optional<GModelElement> element = modelState.getIndex().get(operation.getLabelId());
      if (!element.isPresent() && !(element.get() instanceof GLabel)) {
         throw new IllegalArgumentException("Element with provided ID cannot be found or is not a GLabel");
      }
      GLabel sLabel = (GLabel) element.get();
      sLabel.setText(operation.getText());
   }
}
