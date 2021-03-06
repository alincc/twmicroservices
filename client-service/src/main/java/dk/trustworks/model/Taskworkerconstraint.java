package dk.trustworks.model;

import javax.persistence.*;

@Entity
public class Taskworkerconstraint {
    @Id private String uuid;
    private Double price;

    @ManyToOne()
    @JoinColumn(name = "taskuuid")
    private Task task;
    private String useruuid;
    @Column(insertable = false, updatable = false)
    private String taskuuid;

    public Taskworkerconstraint() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }

    public String getTaskuuid() {
        return taskuuid;
    }

    public void setTaskuuid(String taskuuid) {
        this.taskuuid = taskuuid;
    }
}
