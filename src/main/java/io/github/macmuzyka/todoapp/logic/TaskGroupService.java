package io.github.macmuzyka.todoapp.logic;

import io.github.macmuzyka.todoapp.model.Project;
import io.github.macmuzyka.todoapp.model.TaskGroup;
import io.github.macmuzyka.todoapp.model.TaskGroupRepository;
import io.github.macmuzyka.todoapp.model.TaskRepository;
import io.github.macmuzyka.todoapp.model.projection.GroupReadModel;
import io.github.macmuzyka.todoapp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

//@Service
public class TaskGroupService {
        private TaskGroupRepository taskGroupRepository;
        private TaskRepository repository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.taskGroupRepository = repository;
        this.repository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (repository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Do all the tasks first!");
        }
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }
}
