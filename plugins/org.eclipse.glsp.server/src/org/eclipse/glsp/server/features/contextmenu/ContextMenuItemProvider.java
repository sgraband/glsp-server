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
package org.eclipse.glsp.server.features.contextmenu;

import static org.eclipse.glsp.graph.util.GraphUtil.point;

import java.util.List;
import java.util.Map;

import org.eclipse.glsp.graph.GPoint;
import org.eclipse.glsp.server.features.contextactions.ContextActionsProvider;
import org.eclipse.glsp.server.features.directediting.LabeledAction;
import org.eclipse.glsp.server.types.EditorContext;

@FunctionalInterface
public interface ContextMenuItemProvider extends ContextActionsProvider {

   String KEY = "context-menu";

   @Override
   default String getContextId() { return ContextMenuItemProvider.KEY; }

   List<MenuItem> getItems(List<String> selectedElementIds, GPoint position, Map<String, String> args);

   @Override
   default List<? extends LabeledAction> getActions(final EditorContext editorContext) {
      final GPoint position = editorContext.getLastMousePosition().orElse(point(0, 0));
      return getItems(editorContext.getSelectedElementIds(), position, editorContext.getArgs());
   }
}
