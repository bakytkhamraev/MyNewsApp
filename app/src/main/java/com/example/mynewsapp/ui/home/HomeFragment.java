package com.example.mynewsapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mynewsapp.Extension;
import com.example.mynewsapp.R;
import com.example.mynewsapp.retriofit.RetrofitBuilder;
import com.example.mynewsapp.models.Example;
import com.example.mynewsapp.recycler.NewsAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    NewsAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    int page = 1, limit = 10;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        initRecycler();
        initListeners();
        return root;
    }


    private void initListeners() {
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresher);
        progressBar = root.findViewById(R.id.my_progressBar);
        nestedScrollView = root.findViewById(R.id.scroll_view);
        getBusiness(page, limit);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    limit = +10;
                    progressBar.setVisibility(View.VISIBLE);
                    getBusiness(page, limit);
                }
            }
        });


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerViewMessages();
            }
        });
    }

    private void refreshRecyclerViewMessages() {
        adapter.clear();
        getBusiness(page, limit);
        swipeRefreshLayout.setRefreshing(true);
    }

    private void getBusiness(int page, int limit) {
        RetrofitBuilder.getService().
                getExample("4ad5f4c40f664a5ebb6fc75a231d2b97", "ru", "business", page, limit).
                enqueue(new Callback<Example>() {
                    @Override
                    public void onResponse(Call<Example> call, Response<Example> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setBusiness(response.body().getArticles());
                            swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);

                        } else {
                            Extension.showToast(getActivity(), "Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Example> call, Throwable t) {
                        Extension.showToast(getActivity(), "Failure");
                        swipeRefreshLayout.setRefreshing(true);
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    private void initRecycler() {
        recyclerView = root.findViewById(R.id.recycler_view);
        adapter = new NewsAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
}