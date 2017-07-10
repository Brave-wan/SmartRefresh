package com.scwang.refreshlayout.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.practice.FeedlistPracticeActivity;
import com.scwang.refreshlayout.activity.practice.ProfilePracticeActivity;
import com.scwang.refreshlayout.activity.practice.RepastPracticeActivity;
import com.scwang.refreshlayout.activity.practice.WebviewPracticeActivity;
import com.scwang.refreshlayout.activity.practice.WeiboPracticeActivity;
import com.scwang.refreshlayout.adapter.BaseRecyclerAdapter;
import com.scwang.refreshlayout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 实战演示
 * A simple {@link Fragment} subclass.
 */
public class RefreshPractiveFragment extends Fragment implements AdapterView.OnItemClickListener {

    private enum Item {
        Repast("餐饮美食-简单自定义Header-外边距magin", RepastPracticeActivity.class),
        Profile("个人中心-OverScroll", ProfilePracticeActivity.class),
        Webview("网页引用-WebView", WebviewPracticeActivity.class),
        FeedList("微博列表-智能识别", FeedlistPracticeActivity.class),
        Weibo("微博主页-CoordinatorLayout", WeiboPracticeActivity.class),
        ;
        public String name;
        public Class<?> clazz;
        Item(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_practive, container, false);
    }

    @Override
    public void onViewCreated(View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        View view = root.findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
            recyclerView.setAdapter(new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values()), simple_list_item_2,this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                    holder.text(android.R.id.text1, model.name());
                    holder.text(android.R.id.text2, model.name);
                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
                }
            });
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = Item.values()[position];
        if (Activity.class.isAssignableFrom(item.clazz)) {
            startActivity(new Intent(getContext(), item.clazz));
        } else if (RefreshHeader.class.isAssignableFrom(item.clazz)) {
            try {
                Constructor<?> constructor = item.clazz.getConstructor(Context.class);
                RefreshHeader header = (RefreshHeader) constructor.newInstance(getContext());
                RefreshLayout layout = (RefreshLayout) getView().findViewById(R.id.smartLayout);
                layout.setRefreshHeader(header);
                if (!(header instanceof ClassicsHeader)) {
                    layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                }
                layout.autoRefresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
