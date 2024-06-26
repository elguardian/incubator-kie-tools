/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.kie.workbench.common.stunner.bpmn.client.forms.fields.collaborationEditor.popup.editor;

import java.util.List;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import org.kie.workbench.common.stunner.bpmn.definition.property.collaboration.Correlation;

public interface CorrelationsEditorWidgetView {

    void init(final CorrelationsEditorWidgetView.Presenter presenter);

    Widget getWidget();

    List<Correlation> getCorrelations();

    void setCorrelations(final List<Correlation> correlations);

    void update(List<CorrelationsEditorValidationItem> validationItems);

    interface Presenter extends HasValueChangeHandlers<List<Correlation>> {

        Widget getWidget();

        List<Correlation> getCorrelations();

        void setCorrelations(final List<Correlation> correlations);

        void addCorrelation();

        void removeCorrelation(final Correlation correlation);

        void update(List<CorrelationsEditorValidationItem> validationItems);

        HandlerRegistration addValueChangeHandler(final ValueChangeHandler<List<Correlation>> handler);
    }
}
