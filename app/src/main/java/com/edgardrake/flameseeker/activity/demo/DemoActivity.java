package com.edgardrake.flameseeker.activity.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.activity.demo.fragment.CarouselFragment;
import com.edgardrake.flameseeker.activity.demo.fragment.CurrencyEditTextFragment;
import com.edgardrake.flameseeker.activity.demo.fragment.DropdownFragment;
import com.edgardrake.flameseeker.activity.demo.fragment.RatingFragment;
import com.edgardrake.flameseeker.activity.demo.fragment.SmallGalleryFragment;
import com.edgardrake.flameseeker.lib.base.BaseActivity;
import com.edgardrake.flameseeker.lib.widget.textview.CurrencyEditText;

import butterknife.BindView;

public class DemoActivity extends BaseActivity implements OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mSidebarMenu;

    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), mDrawer, mToolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mSidebarMenu.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_carousel) {
            setFragment(CarouselFragment.newInstance());
        } else if (id == R.id.nav_currency_edit_text) {
            setFragment(CurrencyEditTextFragment.newInstance());
        } else if (id == R.id.nav_gallery) {
            setFragment(SmallGalleryFragment.newInstance());
        } else if (id == R.id.nav_rating) {
            setFragment(RatingFragment.newInstance());
        } else if (id == R.id.nav_dropdown) {
            setFragment(DropdownFragment.newInstance());
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activeFragment.onActivityResult(requestCode, resultCode, data);
    }

    private void setFragment(Fragment fragment) {
        if (activeFragment == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        } else if (activeFragment.getClass() != fragment.getClass()) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        }
        activeFragment = fragment;
    }
}
