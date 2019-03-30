package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;

@Slf4j
public class GroupProjectProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    public void process(StaxStreamProcessor processor) throws XMLStreamException {
        val projectMap = projectDao.getAsMap();
        val groupMap = groupDao.getAsMap();

        while (processor.startElement("Project", "Projects")) {
            String projectName = processor.getAttribute("name");
            String projectDescription = processor.getElementValue("description");
            if (!projectMap.containsKey(projectName)) {
                Project project = new Project(projectName, projectDescription);
                log.info("Insert new project " + project.toString());
                projectDao.insert(project);
            }
            while (processor.startElement("Group", "Project")) {
                String groupName = processor.getAttribute("name");
                GroupType groupType = GroupType.valueOf(processor.getAttribute("type"));
                int id = projectDao.getProjectId(projectName);
                if (!groupMap.containsKey(groupName)) {
                    Group group = new Group(groupName, groupType, id);
                    log.info("Insert new group " + group.toString());
                    groupDao.insert(group);
                }
            }
        }
    }
}
