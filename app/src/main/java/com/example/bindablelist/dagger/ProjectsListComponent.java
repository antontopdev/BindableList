package com.example.bindablelist.dagger;

import com.example.bindablelist.project.ProjectsListFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = ProjectsListModule.class)
public interface ProjectsListComponent extends AndroidInjector<ProjectsListFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ProjectsListFragment> {}
}
