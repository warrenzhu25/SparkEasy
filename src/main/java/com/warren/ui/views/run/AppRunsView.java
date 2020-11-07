package com.warren.ui.views.run;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static com.warren.ui.utils.BakeryConst.PAGE_RUNS;
import static com.warren.ui.utils.BakeryConst.PAGE_USERS;

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
        grid.addColumn(r -> r.getSparkApp().getName()).setHeader("Name").setWidth("200px").setFlexGrow(5);
        grid.addColumn(r -> r.getSparkApp().getLivyBody()).setHeader("Request body").setFlexGrow(5);
        grid.addColumn(AppRun::getCluster).setHeader("Cluster").setFlexGrow(5);
//        grid.addColumns("name", "cluster", "state", "appId", "livyId", "createdDate", "lastModifiedDate");
    }

    @Override
    protected String getBasePage() {
        return PAGE_RUNS;
    }

    private static BinderCrudEditor<AppRun> createForm(SparkAppService sparkAppService) {
        EmailField email = new EmailField("Email (login)");
        email.getElement().setAttribute("colspan", "2");

        ComboBox<SparkApp> sparkApp = new ComboBox<>();
        sparkApp.setDataProvider(new CrudEntityDataProvider<>(sparkAppService));
        sparkApp.setItemLabelGenerator(SparkApp::getName);
        sparkApp.setLabel("Spark App");
        ComboBox<Cluster> cluster = new ComboBox<>();
        cluster.getElement().setAttribute("colspan", "2");
        cluster.setLabel("Cluster");

        FormLayout form = new FormLayout(email, sparkApp, cluster);

        BeanValidationBinder<AppRun> binder = new BeanValidationBinder<>(AppRun.class);

        cluster.setDataProvider(DataProvider.ofItems(Cluster.values()));

        binder.bind(sparkApp, "sparkApp");
        binder.bind(cluster, "cluster");

        return new BinderCrudEditor<>(binder, form);
    }
}

