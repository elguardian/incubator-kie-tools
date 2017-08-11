/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.workbench.common.dmn.showcase.client.screens.preview;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.kie.workbench.common.dmn.api.qualifiers.DMNEditor;
import org.kie.workbench.common.dmn.showcase.client.screens.BaseSessionScreen;
import org.kie.workbench.common.dmn.showcase.client.screens.SessionScreenView;
import org.kie.workbench.common.stunner.client.widgets.menu.MenuUtils;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionPresenterFactory;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionPreview;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionViewer;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvas;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.service.ClientRuntimeError;
import org.kie.workbench.common.stunner.core.client.session.ClientSession;
import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientFullSession;
import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientReadOnlySession;
import org.kie.workbench.common.stunner.core.client.session.impl.AbstractClientSession;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

@Dependent
@WorkbenchScreen(identifier = SessionDiagramPreviewScreen.SCREEN_ID)
public class SessionDiagramPreviewScreen extends BaseSessionScreen {

    private static Logger LOGGER = Logger.getLogger(SessionDiagramPreviewScreen.class.getName());

    public static final String SCREEN_ID = "SessionDiagramPreviewScreen";
    public static final String TITLE = "Preview";
    public static final int WIDTH = 420;
    public static final int HEIGHT = 280;

    private SessionScreenView view;
    private SessionPresenterFactory<Diagram, AbstractClientReadOnlySession, AbstractClientFullSession> sessionPresenterFactory;
    private SessionPreview<AbstractClientSession, Diagram> sessionPreview;
    private Menus menu = null;

    public SessionDiagramPreviewScreen() {
        //CDI proxy
    }

    @Inject
    public SessionDiagramPreviewScreen(final SessionScreenView view,
                                       final @DMNEditor SessionPresenterFactory<Diagram, AbstractClientReadOnlySession, AbstractClientFullSession> sessionPresenterFactory) {
        this.view = view;
        this.sessionPresenterFactory = sessionPresenterFactory;
    }

    @PostConstruct
    public void init() {
        view.showEmptySession();
    }

    @OnStartup
    @SuppressWarnings("unused")
    public void onStartup(final PlaceRequest placeRequest) {
        this.menu = makeMenuBar();
    }

    @OnClose
    public void onClose() {
        close();
    }

    @WorkbenchMenu
    public Menus getMenu() {
        return menu;
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return TITLE;
    }

    @WorkbenchPartView
    public IsWidget getWidget() {
        return view;
    }

    @Override
    protected void doOpenSession() {
        // No need to initialize state or views if no diagram is present.
    }

    @Override
    protected void doOpenDiagram() {
        final AbstractClientSession session = getSession();
        if (null != session) {
            newPreview(() -> sessionPreview.open(session,
                                                 WIDTH,
                                                 HEIGHT,
                                                 new SessionViewer.SessionViewerCallback<AbstractClientSession, Diagram>() {
                                                     @Override
                                                     public void afterCanvasInitialized() {
                                                     }

                                                     @Override
                                                     public void onSuccess() {
                                                         LOGGER.log(FINE,
                                                                    "Session's preview completed for [" + session + "]");
                                                         view.showScreenView(sessionPreview.getView());
                                                     }

                                                     @Override
                                                     public void onError(final ClientRuntimeError error) {
                                                         LOGGER.log(SEVERE,
                                                                    "Error while showing session preview for [" + session + "]. " +
                                                                            "Error=[" + error + "]");
                                                     }
                                                 }));
        } else {
            LOGGER.log(WARNING,
                       "Trying to open a null session!");
        }
    }

    private void newPreview(final Command callback) {
        destroyPreview();
        sessionPreview = sessionPresenterFactory.newPreview();
        callback.execute();
    }

    @Override
    protected void doCloseSession() {
        destroyPreview();
        view.showEmptySession();
    }

    private void destroyPreview() {
        // Clear the session's preview presenter if it has been initialized before.
        if (null != sessionPreview) {
            sessionPreview.destroy();
            sessionPreview = null;
        }
    }

    private Menus makeMenuBar() {
        return MenuFactory.newTopLevelCustomMenu(new MenuFactory.CustomMenuBuilder() {
            @Override
            public void push(MenuFactory.CustomMenuBuilder element) {

            }

            @Override
            public MenuItem build() {
                return buildRefreshMenuItem();
            }
        }).endMenu().build();
    }

    private MenuItem buildRefreshMenuItem() {
        return MenuUtils.buildItem(new Button() {{
            setIcon(IconType.REFRESH);
            setSize(ButtonSize.SMALL);
            setTitle("Refresh");
            addClickHandler(e -> SessionDiagramPreviewScreen.this.refresh());
        }});
    }

    private void refresh() {
        final ClientSession<AbstractCanvas, AbstractCanvasHandler> session = getSession();
        open(session);
    }
}
