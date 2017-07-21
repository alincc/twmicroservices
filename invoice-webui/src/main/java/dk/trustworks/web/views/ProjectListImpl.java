package dk.trustworks.web.views;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import dk.trustworks.network.clients.ProjectSummaryClient;
import dk.trustworks.network.dto.ProjectSummary;
import dk.trustworks.web.Broadcaster;
import dk.trustworks.web.model.YearMonthSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.producttour.actions.TourActions;
import org.vaadin.addons.producttour.button.StepButton;
import org.vaadin.addons.producttour.step.StepBuilder;
import org.vaadin.addons.producttour.tour.Tour;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hans on 12/07/2017.
 */
@SpringComponent
@UIScope
public class ProjectListImpl extends ProjectListDesign
        implements Broadcaster.BroadcastListener {

    private final ProjectSummaryClient projectSummaryClient;

    @Autowired
    public ProjectListImpl(ProjectSummaryClient projectSummaryClient) {
        super();
        Broadcaster.register(this);
        this.projectSummaryClient = projectSummaryClient;
        List<YearMonthSelect> yearMonthList = createYearMonthSelector();
        cbSelectYearMonth.setItems(yearMonthList);
        cbSelectYearMonth.setItemCaptionGenerator(c -> c.getDate().format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        cbSelectYearMonth.setEmptySelectionAllowed(false);
        cbSelectYearMonth.setSelectedItem(yearMonthList.get(1));
        cbSelectYearMonth.addValueChangeListener(event -> {
            reloadData();
        });

        gridProjectSummaryList.setSelectionMode(Grid.SelectionMode.MULTI);
        gridProjectSummaryList.addSelectionListener(event -> {
            System.out.println("event = " + event);
            Set<ProjectSummary> selectedItems = event.getAllSelectedItems();
            switch (selectedItems.size()) {
                case 0:
                    btnCreateInvoice.setCaption("Create invoice");
                    btnCreateInvoice.setEnabled(false);
                    break;
                case 1:
                    btnCreateInvoice.setCaption("Create invoice");
                    btnCreateInvoice.setEnabled(true);
                    break;
                default:
                    btnCreateInvoice.setCaption("Create invoices");
                    btnCreateInvoice.setEnabled(true);
            }
        });

        btnCreateInvoice.addClickListener(event -> {
            for (ProjectSummary projectSummary : gridProjectSummaryList.getSelectedItems()) {
                projectSummaryClient.createInvoiceFromProject(projectSummary.getProjectuuid(), cbSelectYearMonth.getValue().getDate().getYear(), cbSelectYearMonth.getValue().getDate().getMonthValue()-1);
                reloadData();
            }
        });

        gridProjectSummaryList.setSizeFull();
        reloadData();
        addTour();
    }

    public void reloadData() {
        List<ProjectSummary> projectSummaries = projectSummaryClient.loadProjectSummaryByYearAndMonth(cbSelectYearMonth.getValue().getDate().getYear(), cbSelectYearMonth.getValue().getDate().getMonthValue() - 1);
        gridProjectSummaryList.setItems(projectSummaries);
        gridProjectSummaryList.getDataProvider().refreshAll();
    }

    private List<YearMonthSelect> createYearMonthSelector() {
        List<YearMonthSelect> yearMonthSelectList = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2014, 2, 1);
        LocalDate countDate = LocalDate.now();
        while (countDate.isAfter(startDate)) {
            yearMonthSelectList.add(new YearMonthSelect(countDate));
            countDate = countDate.minusMonths(1);
        }

        return yearMonthSelectList;
    }

    @Override
    public void receiveBroadcast(final String message) {
        if(this.getUI() != null) {
            this.getUI().access(this::reloadData);
        }
    }

    private void addTour() {
        Tour tour = new Tour();

        //Button button = new Button("Click to start tour", e -> tour.start());
        //this.addComponent(button);
        btnTour.addClickListener(e -> tour.start());

        tour.addStep(new StepBuilder()
                .withAttachTo(cbSelectYearMonth)
                .withTitle("Step 1: title")
                .withText("Step text")
                .addButton(new StepButton("Back", TourActions::back))
                .addButton(new StepButton("Next", ValoTheme.BUTTON_PRIMARY, TourActions::next))
                .build());

        tour.addStep(new StepBuilder()
                .withAttachTo(gridProjectSummaryList)
                .withTitle("Step 2: title")
                .withText("Step text")
                .addButton(new StepButton("Back", TourActions::back))
                .addButton(new StepButton("Finish", ValoTheme.BUTTON_PRIMARY, TourActions::next))
                .build());
    }
}
