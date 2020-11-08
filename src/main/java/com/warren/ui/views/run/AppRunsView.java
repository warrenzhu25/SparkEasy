package com.warren.ui.views.run;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.warren.app.security.CurrentUser;
import com.warren.backend.data.Role;
import com.warren.backend.data.entity.AppRun;
import com.warren.backend.data.entity.Cluster;
import com.warren.backend.data.entity.SparkApp;
import com.warren.backend.service.AppRunService;
import com.warren.backend.service.SparkAppService;
import com.warren.ui.MainView;
import com.warren.ui.crud.AbstractBakeryCrudView;
import com.warren.ui.crud.CrudEntityDataProvider;
import com.warren.ui.utils.BakeryConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;


import static com.warren.ui.utils.BakeryConst.PAGE_RUNS;

@Route(value = PAGE_RUNS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_RUNS)
@Secured(Role.ADMIN)
public class AppRunsView extends AbstractBakeryCrudView<AppRun> {

    @Autowired
    public AppRunsView(AppRunService service, SparkAppService sparkAppService, CurrentUser currentUser) {
        super(AppRun.class, service, new Grid<>(), createForm(sparkAppService), currentUser);
    }

    @Override
    public void setupGrid(Grid<AppRun> grid) {
        grid.addColumn(r -> r.getSparkApp().getName()).setHeader("Name");
        grid.addColumn(AppRun::getCluster).setHeader("Cluster");
        grid.addColumn(AppRun::getLivyId).setHeader("Livy Id");
        grid.addColumn(AppRun::getAppId).setHeader("Yarn App Id");
        grid.addColumn(AppRun::getState).setHeader("State");
        grid.addColumn(AppRun::getCreatedDate).setHeader("Created Time");
        grid.addColumn(AppRun::getLastModifiedDate).setHeader("Updated Time");
        //grid.addColumn(r -> r.getSparkApp().getLivyBody()).setHeader("Request body");
        grid.addComponentColumn(this::createHistoryUrlButton).setHeader("History Url");
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
    }

    @Override
    protected String getBasePage() {
        return PAGE_RUNS;
    }

    private static BinderCrudEditor<AppRun> createForm(SparkAppService sparkAppService) {
        ComboBox<SparkApp> sparkApp = new ComboBox<>();
        sparkApp.setDataProvider(new CrudEntityDataProvider<>(sparkAppService));
        sparkApp.setItemLabelGenerator(SparkApp::getName);
        sparkApp.setLabel("Spark App");
        ComboBox<Cluster> cluster = new ComboBox<>();
        cluster.getElement().setAttribute("colspan", "2");
        cluster.setLabel("Cluster");
        cluster.setItems(Cluster.values());
        cluster.setValue(Cluster.Prime_CO4_0);

        FormLayout form = new FormLayout(sparkApp, cluster);

        BeanValidationBinder<AppRun> binder = new BeanValidationBinder<>(AppRun.class);

        cluster.setDataProvider(DataProvider.ofItems(Cluster.values()));

        binder.bind(sparkApp, "sparkApp");
        binder.bind(cluster, "cluster");

        return new BinderCrudEditor<>(binder, form);
    }

    private Button createHistoryUrlButton(AppRun appRun) {
        @SuppressWarnings("unchecked")
        Button button = new Button("History Url", clickEvent -> {
            getUI().get().getPage().open(appRun.getHistoryUrl());
        });
        return button;

    }
}

