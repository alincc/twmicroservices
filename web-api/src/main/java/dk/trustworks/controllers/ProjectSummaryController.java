package dk.trustworks.controllers;

import dk.trustworks.network.clients.*;
import dk.trustworks.network.clients.feign.*;
import dk.trustworks.network.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hans on 06/07/2017.
 */

@Service
public class ProjectSummaryController {

    protected static Logger logger = LoggerFactory.getLogger(ProjectSummaryController.class.getName());

    @Autowired
    private UserClient userClient;

    @Autowired
    private TaskClient taskClient;

    @Autowired
    private TaskClientImpl taskClientImpl;

    @Autowired
    private TaskworkerconstraintClient taskworkerconstraintClient;

    @Autowired
    private TaskworkerconstraintClientImpl taskworkerconstraintClientImpl;

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private ProjectClientImpl projectClientImpl;

    @Autowired
    private ClientdataClient clientdataClient;

    @Autowired
    private ClientClient clientClient;

    @Autowired
    private ClientClientImpl clientClientImpl;

    @Autowired
    private WorkClient workClient;

    @Autowired
    private InvoiceClient invoiceClient;


    @RequestMapping(value = "/search/loadProjectSummaryByYearAndMonth", method = RequestMethod.GET)
    public Collection<ProjectSummary> loadProjectSummaryByYearAndMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        logger.info("InvoiceController.loadProjectSummaryByYearAndMonth");
        logger.info("year = [" + year + "], month = [" + month + "]");
        Resources<Resource<Work>> workResources = workClient.findByYearAndMonth(year, month);
        logger.info("workResources.getContent().size() = " + workResources.getContent().size());

        Collection<Resource<Invoice>> invoices = invoiceClient.findByYearAndMonth(year, month).getContent();

        Map<String, ProjectSummary> projectSummaryMap = new HashMap<>();

        Map<String, Task> taskMap = new HashMap<>();
        for (Resource<Task> taskResource : taskClient.findAll().getContent()) {
            taskMap.put(taskResource.getContent().getUuid(), taskResource.getContent());
        }

        Map<String, Project> projectMap = new HashMap<>();
        for (Resource<Project> projectResource : projectClient.findAll().getContent()) {
            projectMap.put(projectResource.getContent().getUuid(), projectResource.getContent());
        }

        Map<String, Client> clientMap = new HashMap<>();
        for (Resource<Client> clientResource : clientClient.findAll().getContent()) {
            clientMap.put(clientResource.getContent().getUuid(), clientResource.getContent());
        }

        Map<String, Taskworkerconstraint> taskworkerconstraintMap = new HashMap<>();
        for (Resource<Taskworkerconstraint> taskworkerconstraintResource : taskworkerconstraintClient.findAll()) {
            System.out.println("taskworkerconstraintResource.getContent() = " + taskworkerconstraintResource.getContent());
            taskworkerconstraintMap.put(
                    taskworkerconstraintResource.getContent().getUseruuid()+"_"
                    +taskworkerconstraintResource.getContent().getTaskuuid(),
                    taskworkerconstraintResource.getContent()
            );
        }


        for (Resource<Work> workResource : workResources) {
            Task task = taskMap.get(workResource.getContent().getTaskuuid());
            if(task==null) {
                continue;
            }
            Project project = projectMap.get(task.getProjectuuid());
            if(project==null) {
                continue;
            }
            Client client = clientMap.get(project.getClientuuid());
            if(client==null) {
                continue;
            }

            double invoicedamount = 0.0;

            if(!projectSummaryMap.containsKey(project.getUuid())) {
                int numberOfInvoicesRelatedToProject = 0;
                for (Resource<Invoice> invoice : invoices) {
                    if(invoice.getContent().projectuuid.equals(project.getUuid()) && (
                            invoice.getContent().status.equals(InvoiceStatus.CREATED)
                            || invoice.getContent().status.equals(InvoiceStatus.SUBMITTED)
                            || invoice.getContent().status.equals(InvoiceStatus.PAID)
                            || invoice.getContent().status.equals(InvoiceStatus.CREDIT_NOTE))) {
                        numberOfInvoicesRelatedToProject++;
                        for (InvoiceItem invoiceitem : invoice.getContent().invoiceitems) {
                            invoicedamount += (invoice.getContent().type.equals(InvoiceType.INVOICE)?
                                    (invoiceitem.hours*invoiceitem.rate):
                                    -(invoiceitem.hours*invoiceitem.rate));
                        }
                    }
                }
                ProjectSummary projectSummary = new ProjectSummary(
                        project.getUuid(),
                        project.getName(),
                        client.getName(),
                        project.getCustomerreference(),
                        0,
                        invoicedamount, numberOfInvoicesRelatedToProject);
                projectSummaryMap.put(project.getUuid(), projectSummary);
                logger.info("Created new projectSummary: " + projectSummary);
            }
            System.out.println("workResource.getContent() = " + workResource.getContent());
            if(taskworkerconstraintMap.containsKey(workResource.getContent().getUseruuid()+"_"+workResource.getContent().getTaskuuid())) {
                projectSummaryMap.get(project.getUuid()).addAmount(
                        workResource.getContent().getWorkduration() *
                                (taskworkerconstraintMap.get(
                                        (workResource.getContent().getUseruuid() + "_" + workResource.getContent().getTaskuuid())
                                ).getPrice())
                );
            }

        }
        return projectSummaryMap.values();
    }

    @RequestMapping(value = "/onMethod/createInvoiceFromProject", method = RequestMethod.GET)
    public void createInvoiceFromProject(@RequestParam("projectuuid") String projectuuid, @RequestParam("year") int year, @RequestParam("month") int month) {
        logger.info("InvoiceController.createInvoiceFromProject");
        logger.info("projectuuid = [" + projectuuid + "], year = [" + year + "], month = [" + month + "]");
        Resources<Resource<Work>> workResources = workClient.findByYearAndMonth(year, month);
        Invoice invoice = null;
        Map<String, InvoiceItem> invoiceItemMap = new HashMap<>();

        for (Resource<Work> workResource : workResources) {
            Link taskLink = workResource.getLink("task");
            Link userLink = workResource.getLink("user");

            Resource<Task> taskResource = taskClientImpl.findTaskByRestLink(taskLink.getHref());
            Task task = taskResource.getContent();
            logger.info("task = " + task);

            Resource<Project> projectResource = projectClientImpl.findProjectByRestLink(taskResource.getLink("project").getHref());
            Project project = projectResource.getContent();
            if(!project.getUuid().equals(projectuuid)) continue;
            logger.info("project = " + project);

            Resource<User> userResource = userClient.findUserByRestLink(userLink.getHref());
            User user = userResource.getContent();
            logger.info("user = " + user);

            Resource<Clientdata> clientdataResource = clientdataClient.findClientdataByRestLink(projectResource.getLink("clientData").getHref());
            Clientdata clientdata = (clientdataResource!=null)?clientdataResource.getContent():new Clientdata();

            Resource<Client> clientResource = clientClientImpl.findClientByRestLink(projectResource.getLink("client").getHref());
            Client client = clientResource.getContent();

            Resources<Resource<Taskworkerconstraint>> taskworkerconstraintResources = taskworkerconstraintClientImpl.findTaskworkerconstraintsByRestLink(taskResource.getLink("taskworkerconstraint").getHref());

            if(invoice == null) {
                invoice = new Invoice(InvoiceType.INVOICE,
                        project.getUuid(),
                        project.getName(),
                        year,
                        month,
                        clientdata.getClientname(),
                        clientdata.getStreetnamenumber(),
                        clientdata.getOtheraddressinfo(),
                        clientdata.getPostalcode()+" "+ clientdata.getCity(),
                        clientdata.getEan(),
                        clientdata.getCvr(),
                        clientdata.getContactperson(),
                        LocalDate.now().withYear(year).withMonth(month+1).withDayOfMonth(LocalDate.now().withYear(year).withMonth(month+1).getMonth().maxLength()),
                        project.getCustomerreference(),
                        "");
                invoice.uuid = UUID.randomUUID().toString();
                logger.info("Created new invoice: "+invoice);
            }
            Taskworkerconstraint taskworkerconstraint = null;
            for (Resource<Taskworkerconstraint> taskworkerconstraintResource : taskworkerconstraintResources) {
                if(taskworkerconstraintResource.getContent().getTaskuuid().equals(task.getUuid()) && taskworkerconstraintResource.getContent().getUseruuid().equals(user.getUuid())) {
                    taskworkerconstraint = taskworkerconstraintResource.getContent();
                }
            }
            if(taskworkerconstraint == null) {
                logger.error("Taskworkerconstraint could not be found for user (link: "+user.getUuid()+") and task (link: "+task.getUuid()+")");
                invoice.errors = true;
                continue;
            }
            if(!invoiceItemMap.containsKey(project.getUuid()+taskworkerconstraint.getUuid())) {
                InvoiceItem invoiceItem = new InvoiceItem(user.getFirstname() + " " + user.getLastname(),
                        task.getName(),
                        taskworkerconstraint.getPrice(),
                        0.0);
                invoiceItem.uuid = UUID.randomUUID().toString();
                invoiceItemMap.put(project.getUuid()+taskworkerconstraint.getUuid(), invoiceItem);
                invoice.invoiceitems.add(invoiceItem);
                logger.info("Created new invoice item: "+invoiceItem);
            }
            invoiceItemMap.get(project.getUuid()+taskworkerconstraint.getUuid()).hours += workResource.getContent().getWorkduration();
        }

        invoiceClient.saveInvoice(invoice);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveInvoice(@RequestBody Invoice invoice) {
        logger.debug("InvoiceController.saveInvoice");
        logger.debug("invoice = [" + invoice + "]");
        invoiceClient.saveInvoice(invoice);
    }
}


/*
logger.info("InvoiceController.loadProjectSummaryByYearAndMonth");
        logger.info("year = [" + year + "], month = [" + month + "]");
        Resources<Resource<Work>> workResources = workClient.findByYearAndMonth(year, month);
        logger.info("workResources.getContent().size() = " + workResources.getContent().size());

        Map<String, Invoice> invoiceMap = new HashMap<>();
        Map<String, InvoiceItem> invoiceItemMap = new HashMap<>();

        for (Resource<Work> workResource : workResources) {
            Link taskLink = workResource.getLink("task");
            Link userLink = workResource.getLink("user");
            Resource<User> userResource = userClient.findUserByRestLink(userLink.getHref());
            Resource<Task> taskResource = taskClient.findTaskByRestLink(taskLink.getHref());
            Resources<Resource<Taskworkerconstraint>> taskworkerconstraintResources = taskworkerconstraintClient.findTaskworkerconstraintsByRestLink(taskResource.getLink("taskworkerconstraint").getHref());
            Resource<Project> projectResource = projectClient.findProjectByRestLink(taskResource.getLink("project").getHref());
            Resource<Clientdata> clientdataResource = clientdataClient.findClientdataByRestLink(projectResource.getLink("clientData").getHref());
            Resource<Client> clientResource = clientClient.findClientByRestLink(projectResource.getLink("client").getHref());

            User user = userResource.getContent();
            Task task = taskResource.getContent();
            Project project = projectResource.getContent();
            Clientdata clientdata = (clientdataResource!=null)?clientdataResource.getContent():new Clientdata();
            Client client = clientResource.getContent();

            if(!invoiceMap.containsKey(project.getUuid())) {
                Invoice invoice = new Invoice(InvoiceType.INVOICE,
                        project.getUuid(),
                        project.getName(),
                        year,
                        month,
                        client.getName(),
                        clientdata.getStreetnamenumber(),
                        clientdata.getPostalcode()+" "+ clientdata.getCity(),
                        clientdata.getEan(),
                        clientdata.getCvr(),
                        clientdata.getContactperson(),
                        LocalDate.now(),
                        project.getCustomerreference());
                invoiceMap.put(project.getUuid(), invoice);
                logger.info("Created new invoice: "+invoice);
            }
            Taskworkerconstraint taskworkerconstraint = null;
            for (Resource<Taskworkerconstraint> taskworkerconstraintResource : taskworkerconstraintResources) {
                if(taskworkerconstraintResource.getContent().getTaskuuid().equals(task.getUuid()) && taskworkerconstraintResource.getContent().getUseruuid().equals(user.getUuid())) {
                    taskworkerconstraint = taskworkerconstraintResource.getContent();
                }
            }
            if(taskworkerconstraint == null) {
                logger.error("Taskworkerconstraint could not be found for user (link: "+user.getUuid()+") and task (link: "+task.getUuid()+")");
                invoiceMap.get(project.getUuid()).errors = true;
                continue;
            }
            if(!invoiceItemMap.containsKey(project.getUuid()+taskworkerconstraint.getUuid())) {
                InvoiceItem invoiceItem = new InvoiceItem(task.getName(),
                        "",
                        user.getFirstname() + " " + user.getLastname(),
                        taskworkerconstraint.getPrice(),
                        0.0);

                invoiceItemMap.put(project.getUuid()+taskworkerconstraint.getUuid(), invoiceItem);
                invoiceMap.get(project.getUuid()).invoiceitems.add(invoiceItem);
                logger.info("Created new invoice item: "+invoiceItem);
            }
            invoiceItemMap.get(project.getUuid()+taskworkerconstraint.getUuid()).hours += workResource.getContent().getWorkduration();
        }
        return invoiceMap.values();
 */