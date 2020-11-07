package com.warren.ui.views.app;

import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.warren.app.security.CurrentUser;
import com.warren.backend.data.Role;
import com.warren.backend.data.entity.SparkApp;
import com.warren.backend.service.SparkAppService;
import com.warren.ui.MainView;
import com.warren.ui.crud.AbstractBakeryCrudView;
import com.warren.ui.utils.BakeryConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import static com.warren.ui.utils.BakeryConst.PAGE_APPS;

@Route(value = PAGE_APPS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_APPS)
@Secured(Role.ADMIN)
public class AppsView extends AbstractBakeryCrudView<SparkApp> {

    @Autowired
    public AppsView(SparkAppService service, CurrentUser currentUser) {
        super(SparkApp.class, service, new Grid<>(), createForm(), currentUser);
    }

    @Override
    protected void setupGrid(Grid<SparkApp> grid) {
        grid.addColumn(SparkApp::getName).setHeader("Name");
        grid.addColumn(SparkApp::getLivyBody).setHeader("Livy request body");
    }

    @Override
    protected String getBasePage() {
        return PAGE_APPS;
    }

    private static BinderCrudEditor<SparkApp> createForm() {
        TextField name = new TextField("Name");
        name.getElement().setAttribute("colspan", "2");
        TextArea requestBody = new TextArea("Livy request body");

        FormLayout form = new FormLayout(name, requestBody);

        BeanValidationBinder<SparkApp> binder = new BeanValidationBinder<>(SparkApp.class);

        binder.bind(name, "name");
        binder.bind(requestBody, "livyBody");

        requestBody.setPreventInvalidInput(true);

        return new BinderCrudEditor<>(binder, form);
    }
}