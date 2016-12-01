package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class NewsActivity extends AppCompatActivity
        implements NewsActivityFragment.OnPostItemSelectedListener {
    private static final int PICK_LOGIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NewsPagerAdapter pagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pagerAdapter.getPageTitle(i).toString()
                    .toUpperCase()));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_in) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, PICK_LOGIN_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_LOGIN_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                String user = data.getStringExtra("user");

                Snackbar.make(findViewById(R.id.container),
                        String.format("User %s logged in", user), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPostItemPicked(PostModel post) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("Post", post);
        startActivity(intent);
    }

    @Override
    public void onPostButtonBrowserPicked(String url) {
        Intent intent = new Intent(this, PostWebView.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public class NewsPagerAdapter extends FragmentStatePagerAdapter {
        private final String[] categories = {"hot", "new", "top"};

        NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return NewsActivityFragment.newInstance(categories[position]);
        }

        @Override
        public int getCount() {
            return categories.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categories[position];
        }
    }
}
