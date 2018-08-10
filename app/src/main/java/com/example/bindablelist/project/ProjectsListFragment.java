package com.example.bindablelist.project;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bindablelist.R;
import com.example.bindablelist.MainActivity;
import com.example.bindablelist.project.ProjectFragment;
import com.example.bindablelist.project.add.NewProjectFragment;
import com.example.bindablelist.base.BaseFragment;
import com.example.bindablelist.dagger.Injectable;
import com.example.bindablelist.databinding.FragmentProjectsListBinding;
import com.example.bindablelist.view.DialogProjectActions;
import com.example.bindablelist.viewmodel.ProjectsListViewModel;

import javax.inject.Inject;

import javax.inject.Inject;

public class ProjectsListFragment extends BaseFragment<FragmentProjectsListBinding>
        implements Injectable,
        ProjectsListAdapter.ProjectsListListener,
        DialogProjectActions.OnProjectActionListener {

    private static final String KEY_PROJECT_ID = "ID_PROJECT";

    @Inject
    protected ProjectsListAdapter adapter;

    @Inject
    protected ProjectsListViewModel projectsViewModel;

    private boolean isArchivedList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_projects_list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandler(this);
        projectsViewModel.getProjects().observe(this, projects -> adapter.setItems(projects));
        projectsViewModel.isArchivedList().observe(this, mBoolean -> {
            isArchivedList = mBoolean;
            binding.newProject.setVisibility(mBoolean ? View.GONE : View.VISIBLE);
        });
        initProjectList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateActivityUi();
        projectsViewModel.loadProjects();
    }

    private void updateActivityUi() {
        if(getActivity() != null) {
            ((MainActivity) getActivity()).setTitleOnToolbar(getString(R.string.list_of_project));
        }
    }

    // Sets settings for projects RecyclerView.
    private void initProjectList() {
        binding.projects.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.projects.setAdapter(adapter);
    }

    public void addProject() {
        if (getActivity() != null)
            ((MainActivity) getActivity()).addFragment(new NewProjectFragment());
    }

    private void openConfirmationDialog(String message, DialogProjectActions.ProjectAction action, int projectID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            onDialogConfirmReceived(action, projectID);
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void onDialogConfirmReceived(DialogProjectActions.ProjectAction action, int projectID) {
        switch (action) {
            case archive:
                projectsViewModel.archiveProject(projectID);
                break;
            case delete:
                projectsViewModel.removeTaskForProjectId(projectID);
                projectsViewModel.removeProject(projectID);
                break;
        }
    }

    @Override
    public void onProjectClick(int projectId) {
        ProjectFragment projectFragment = new ProjectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PROJECT_ID, projectId);
        projectFragment.setArguments(bundle);

        if (getActivity() != null)
            ((MainActivity) getActivity()).addFragment(projectFragment);
    }

    @Override
    public boolean onProjectLongClick(int projectId, String projectName) {
        DialogProjectActions dialog = new DialogProjectActions(getContext(), projectName, projectId, isArchivedList);
        dialog.setOnActionListener(this);
        dialog.show();
        return true;
    }

    @Override
    public void onActionSelected(DialogProjectActions.ProjectAction action, int projectID) {
        switch (action){
            case archive:
                openConfirmationDialog(getString(R.string.action_confirm_archiving), action, projectID);
                break;
            case delete:
                openConfirmationDialog(getString(R.string.action_confirm_deleting), action, projectID);
                break;
            case refactor:
                NewProjectFragment editProjectFragment = new NewProjectFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_PROJECT_ID, projectID);
                editProjectFragment.setArguments(bundle);
                if (getActivity() != null)
                    ((MainActivity) getActivity()).addFragment(editProjectFragment);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_project_list_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_projects_list:
                projectsViewModel.setIsArchivedList(false);
                ((MainActivity) getActivity()).setTitleOnToolbar(getString(R.string.list_of_project));
                return true;
            case R.id.show_archive_list:
                projectsViewModel.setIsArchivedList(true);
                ((MainActivity) getActivity()).setTitleOnToolbar(getString(R.string.archive_of_projects));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

