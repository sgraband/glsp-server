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
package org.eclipse.glsp.server.operations.gmodel;

import static org.eclipse.glsp.server.utils.GModelUtil.IS_CONNECTABLE;

import java.util.Optional;

import org.eclipse.glsp.graph.GEdge;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelIndex;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.server.model.GModelState;
import org.eclipse.glsp.server.operations.CreateEdgeOperation;
import org.eclipse.glsp.server.operations.AbstractCreateOperationHandler;

import com.google.inject.Inject;

public abstract class CreateEdgeOperationHandler extends AbstractCreateOperationHandler<CreateEdgeOperation> {

   protected final String label;

   @Inject
   protected GModelState modelState;

   public CreateEdgeOperationHandler(final String elementTypeId, final String label) {
      super(elementTypeId);
      this.label = label;
   }

   @Override
   public void executeOperation(final CreateEdgeOperation operation) {
      if (operation.getSourceElementId() == null || operation.getTargetElementId() == null) {
         throw new IllegalArgumentException("Incomplete create connection action");
      }

      GModelIndex index = modelState.getIndex();

      Optional<GModelElement> source = index.findElement(operation.getSourceElementId(), IS_CONNECTABLE);
      Optional<GModelElement> target = index.findElement(operation.getTargetElementId(), IS_CONNECTABLE);
      if (!source.isPresent() || !target.isPresent()) {
         throw new IllegalArgumentException("Invalid source or target for source ID " + operation.getSourceElementId()
            + " and target ID " + operation.getTargetElementId());
      }

      Optional<GEdge> connection = createEdge(source.get(), target.get(), modelState);
      if (!connection.isPresent()) {
         throw new IllegalArgumentException(
            String.format("Creation of connection failed for source: %s , target: %s", source.get().getId(),
               target.get().getId()));
      }
      GModelRoot currentModel = modelState.getRoot();
      currentModel.getChildren().add(connection.get());
   }

   protected abstract Optional<GEdge> createEdge(GModelElement source, GModelElement target,
      GModelState modelState);

   @Override
   public String getLabel() { return label; }

}
