package com.example.bindablelist.dagger;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

import com.example.bindablelist.project.ProjectsListAdapter;
import com.example.bindablelist.project.ProjectsListFragment;
import com.example.bindablelist.project.ProjectsListViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ProjectsListModule {

    @Provides
    ProjectsListViewModel provideViewModel(ProjectsListFragment fragment, ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(fragment, factory).get(ProjectsListViewModel.class);
    }

    @Provides
    ProjectsListAdapter provideProjectAdapter(ProjectsListAdapter.ProjectsListListener listener) {
        return new ProjectsListAdapter(listener);
    }

    @Provides
    ProjectsListAdapter.ProjectsListListener provideProjectsListListener(ProjectsListFragment fragment) {
        return fragment;
    }
}