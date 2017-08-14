package dk.trustworks.invoicewebui.web.client.components;

import com.vaadin.ui.Notification;
import dk.trustworks.invoicewebui.network.clients.ClientClient;
import dk.trustworks.invoicewebui.network.dto.Client;
import org.springframework.hateoas.Resource;
import server.droporchoose.UploadComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;

/**
 * Created by hans on 13/08/2017.
 */
public class ClientImpl extends ClientDesign {

    private ClientClient clientClient;
    private Resource<Client> clientResource;

    public ClientImpl(ClientClient clientClient, Resource<Client> clientResource) {
        this.clientClient = clientClient;
        this.clientResource = clientResource;
        UploadComponent uploadComponent = new UploadComponent(this::uploadReceived);
        uploadComponent.setStartedCallback(this::uploadStarted);
        uploadComponent.setProgressCallback(this::uploadProgress);
        uploadComponent.setFailedCallback(this::uploadFailed);
        uploadComponent.setWidth(500, Unit.PIXELS);
        uploadComponent.setHeight(300, Unit.PIXELS);
        uploadComponent.setCaption("File upload");

        getFormLayout().addComponent(uploadComponent);
    }

    private void uploadReceived(String fileName, Path file) {
        Notification.show("Upload finished: " + fileName, HUMANIZED_MESSAGE);
        try {
            byte[] bytes = Files.readAllBytes(file);
            System.out.println("bytes.length = " + bytes.length);
            clientResource.getContent().setLogo(bytes);
            System.out.println("clientResource = " + clientResource.getContent());
            clientClient.save(clientResource.getContent().getUuid(), clientResource.getContent());
        } catch (IOException e) {
            uploadFailed(fileName, file);
        }
    }

    private void uploadStarted(String fileName) {
        Notification.show("Upload started: " + fileName, HUMANIZED_MESSAGE);
    }

    private void uploadProgress(String fileName, long readBytes, long contentLength) {
        Notification.show(String.format("Progress: %s : %d/%d", fileName, readBytes, contentLength), TRAY_NOTIFICATION);
    }

    private void uploadFailed(String fileName, Path file) {
        Notification.show("Upload failed: " + fileName, ERROR_MESSAGE);
    }
}