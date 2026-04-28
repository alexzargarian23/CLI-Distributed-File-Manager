package Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
    private List<Project> allProjects = new ArrayList<>();

    public void createNewProject(String name, String ownerId) {
        Project p = new Project(name, ownerId);
        allProjects.add(p);
        System.out.println("Project '" + name + "' created with ID: " + p.getProjectId());
    }

    // Method to find projects for a specific user
    public List<Project> getProjectsForUser(String userId) {
        List<Project> userProjects = new ArrayList<>();
        for (Project p : allProjects) {
            if (p.isMember(userId)) {
                userProjects.add(p);
            }
        }
        return userProjects;
    }
}