package com.example.bindablelist.project;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.bindablelist.data.database.Project;
import com.example.bindablelist.data.repository.Repository;

import java.util.List;

import javax.inject.Inject;


public class ProjectsListViewModel extends ViewModel {

    @Inject
    protected Repository repository;

    private MutableLiveData<List<Project>> projects = new MutableLiveData<>();
    private MutableLiveData<Boolean> isArchivedList = new MutableLiveData<>();

    @Inject
    public ProjectsListViewModel() {
        isArchivedList.setValue(false);
    }

    public void loadProjects() {
        if (isArchivedList.getValue()) {
            projects.postValue(repository.getArchivedProjects());
        } else {
            projects.postValue(repository.getActiveProjects());
        }
    }

    public LiveData<List<Project>> getProjects() {
        return projects;
    }

    public void archiveProject(int projectID) {
        repository.archiveProject(projectID);
        loadProjects();
    }

    public void removeProject(int projectID) {
        repository.removeProjectByID(projectID);
        loadProjects();
    }

    public void removeTaskForProjectId(int projectID) {
        repository.removeTaskForProjectId(projectID);
    }

    public void setIsArchivedList(boolean isArchivedListNeeds) {
        isArchivedList.setValue(isArchivedListNeeds);
        loadProjects();
    }

    public MutableLiveData<Boolean> isArchivedList() {
        return isArchivedList;
    }
}
