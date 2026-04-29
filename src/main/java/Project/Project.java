package Project;

import java.util.HashSet;
import java.util.UUID;

public class Project {

    private String projectName;
    private String projectId;
    private String ownerId;


    private HashSet<String> memberUserIds;

    public Project(String projectName, String ownerId) {
        this.projectId = UUID.randomUUID().toString();
        this.projectName = projectName;
        this.ownerId = ownerId;
        this.memberUserIds = new HashSet<>();


        this.memberUserIds.add(ownerId);
    }


    public String getProjectName() { return projectName; }
    public String getProjectId() { return projectId; }
    public String getOwnerId() { return ownerId; }
    public HashSet<String> getMemberUserIds() { return memberUserIds; }


    public void addMember(String userId) {
        if (userId != null && !userId.isEmpty()) {
            memberUserIds.add(userId);
        }
    }

    public boolean isMember(String userId) {
        return memberUserIds.contains(userId);
    }
}