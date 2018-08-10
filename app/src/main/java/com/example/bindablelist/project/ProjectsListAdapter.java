package com.example.bindablelist.project;

import android.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.bindablelist.R;
import com.example.bindablelist.base.BaseRecyclerAdapter;
import com.example.bindablelist.data.database.Project;
import com.example.bindablelist.databinding.ItemProjectsListBinding;

import javax.inject.Inject;

public class ProjectsListAdapter
        extends BaseRecyclerAdapter<ItemProjectsListBinding, Project, ProjectViewHolder> {

    public ProjectsListListener projectsListListener;

    @Inject
    public ProjectsListAdapter(ProjectsListListener listener) {
        this.projectsListListener = listener;
    }

    @Override
    protected ItemProjectsListBinding getBinding(LayoutInflater inflater, ViewGroup parent) {
        return DataBindingUtil.inflate(inflater, R.layout.item_projects_list, parent, false);
    }

    @Override
    protected ProjectViewHolder getViewHolder(ItemProjectsListBinding binding) {
        binding.setAdapter(this);
        return new ProjectViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ProjectsListListener {
        void onProjectClick(int projectId);
        boolean onProjectLongClick(int projectId, String projectName);
    }
}