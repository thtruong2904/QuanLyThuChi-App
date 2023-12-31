package com.example.quanlythuchi_app.Fragment.Category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlythuchi_app.API.HTTPRequest;
import com.example.quanlythuchi_app.API.HTTPService;
import com.example.quanlythuchi_app.Adapter.CategoryAdapter;
import com.example.quanlythuchi_app.Model.CategoryModel;
import com.example.quanlythuchi_app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryIncomeFragment extends Fragment {
    private String currentQuery = "";
    CategoryAdapter adapter;
    private RecyclerView lvCategory;
    private List<CategoryModel> mListCategory;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_income, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvCategory = view.findViewById(R.id.lvCategory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        mListCategory = new ArrayList<>();
        getIncomeCategory();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    public void refreshData()
    {
        filterCategoryIncome(currentQuery);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void getIncomeCategory(){
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<List<CategoryModel>> call = httpRequest.incomeCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful())
                {
                    mListCategory = response.body();
                    lvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
                    lvCategory.setHasFixedSize(true);
                    adapter = new CategoryAdapter(getContext(), mListCategory);
                    lvCategory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
            }
        });
    }
    public void filterCategoryIncome(String query) {
        currentQuery = query;
        adapter.filter(currentQuery);
    }
}