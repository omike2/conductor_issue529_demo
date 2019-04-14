package com.example.demoapp.main;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.example.demoapp.R;
import com.example.demoapp.main.test.TestController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    private Router router;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout cl;
    @BindView(R.id.controller_container)
    ViewGroup container;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    ControllerChangeHandler changeHandler = new FadeChangeHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        router = Conductor
                .attachRouter(this, container, savedInstanceState);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_test1:
                            if (!setActiveController(TestController.class, TestController.TAG, changeHandler)) {
                                router.pushController(RouterTransaction.with(new TestController()).tag(TestController.TAG).pushChangeHandler(changeHandler).popChangeHandler(changeHandler));
                            }
                            break;
                    }
                    return true;
                });
        if (router.getBackstack().size() == 0) {
            bottomNavigationView.setSelectedItemId(R.id.action_test1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected boolean setActiveController(Class<? extends Controller> controllerClass, String routerTransactionTag, ControllerChangeHandler changeHandler) {
        if (router == null || (controllerClass == null && routerTransactionTag == null)) {
            return false;
        }

        List<RouterTransaction> backstack = router.getBackstack();

        //only one controller instance is allowed
        if (backstack.size() > 0 && TextUtils.equals(routerTransactionTag, backstack.get(backstack.size() - 1).tag())) {
            router.setBackstack(backstack, changeHandler);
            return true;
        }

        int newActiveIndex = -1;
        int oldActiveIndex = backstack.size() - 1;
        for (int i = oldActiveIndex; i >= 0; --i) {
            RouterTransaction routerTransaction = backstack.get(i);
            if (controllerClass != null && !controllerClass.isInstance(routerTransaction.controller())) {
                continue;
            }
            if (routerTransactionTag != null && !TextUtils.equals(routerTransactionTag, routerTransaction.tag())) {
                continue;
            }
            if (i != oldActiveIndex) {
                newActiveIndex = i;
            }
            break;
        }

        if (newActiveIndex != -1) {
            backstack.add(backstack.remove(newActiveIndex));
            router.setBackstack(backstack, changeHandler);
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolBarTitle(String title) {
        toolbar.setTitle(title);
    }
}
