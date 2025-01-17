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
package org.eclipse.glsp.server.features.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelIndex;
import org.eclipse.glsp.server.actions.AbstractActionHandler;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.model.GModelState;

import com.google.inject.Inject;

public class RequestMarkersHandler extends AbstractActionHandler<RequestMarkersAction> {

   private static final Logger LOG = Logger.getLogger(RequestMarkersHandler.class);

   @Inject
   protected Optional<ModelValidator> validator;

   @Inject
   protected GModelState modelState;

   @Override
   @SuppressWarnings("checkstyle:cyclomaticComplexity")
   public List<Action> executeAction(final RequestMarkersAction action) {
      List<String> elementsIDs = action.getElementsIDs();
      if (validator.isEmpty()) {
         LOG.warn("Cannot compute markers! No implementation for " + ModelValidator.class + " has been bound");
         return none();
      }

      // if no element ID is provided, compute the markers for the complete model
      if (elementsIDs == null || elementsIDs.size() == 0
         || (elementsIDs.size() == 1 && "EMPTY".equals(elementsIDs.get(0)))) {
         elementsIDs = Arrays.asList(modelState.getRoot().getId());
      }
      List<Marker> markers = new ArrayList<>();
      GModelIndex currentModelIndex = modelState.getIndex();
      for (String elementID : elementsIDs) {
         Optional<GModelElement> modelElement = currentModelIndex.get(elementID);
         if (modelElement.isPresent()) {
            markers.addAll(validator.get().validate(modelElement.get()));
         }

      }

      return listOf(new SetMarkersAction(markers));
   }
}
