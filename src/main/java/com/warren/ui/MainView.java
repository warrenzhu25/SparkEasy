package com.warren.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.warren.ui.views.HasConfirmation;
import com.warren.ui.views.app.AppsView;
import com.warren.ui.views.run.AppRunsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.warren.ui.utils.BakeryConst.*;

@Viewport(VIEWPORT)
public class MainView extends AppLayout {

	private final ConfirmDialog confirmDialog = new ConfirmDialog();
	private final Tabs menu;

	public MainView() {
		confirmDialog.setCancelable(true);
		confirmDialog.setConfirmButtonTheme("raised tertiary error");
		confirmDialog.setCancelButtonTheme("raised tertiary");

		this.setDrawerOpened(false);
		Span appName = new Span("SparkEasy");
		appName.addClassName("hide-on-mobile");

		menu = createMenuTabs();

		this.addToNavbar(appName);
		this.addToNavbar(true, menu);
		this.getElement().appendChild(confirmDialog.getElement());

		getElement().addEventListener("search-focus", e -> {
			getElement().getClassList().add("hide-navbar");
		});

		getElement().addEventListener("search-blur", e -> {
			getElement().getClassList().remove("hide-navbar");
		});
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		confirmDialog.setOpened(false);
		if (getContent() instanceof HasConfirmation) {
			((HasConfirmation) getContent()).setConfirmDialog(confirmDialog);
		}
		RouteConfiguration configuration = RouteConfiguration.forSessionScope();
		if (configuration.isRouteRegistered(this.getContent().getClass())) {
			String target = configuration.getUrl(this.getContent().getClass());
			Optional < Component > tabToSelect = menu.getChildren().filter(tab -> {
				Component child = tab.getChildren().findFirst().get();
				return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
			}).findFirst();
			tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
		} else {
			menu.setSelectedTab(null);
		}
	}

	private static Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
		Tab[] availableTabs = getAvailableTabs();
		tabs.add(availableTabs);
		tabs.setSelectedTab(availableTabs[0]);
		return tabs;
	}

	private static Tab[] getAvailableTabs() {
		final List<Tab> tabs = new ArrayList<>();
		tabs.add(createTab(VaadinIcon.LAPTOP, TITLE_APPS, AppsView.class));
		tabs.add(createTab(VaadinIcon.CLOCK, TITLE_RUNS, AppRunsView.class));
		return tabs.toArray(new Tab[tabs.size()]);
	}

	private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
		return createTab(populateLink(new RouterLink(null, viewClass), icon, title));
	}

	private static Tab createTab(Component content) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(content);
		return tab;
	}

	private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
		a.add(icon.create());
		a.add(title);
		return a;
	}
}