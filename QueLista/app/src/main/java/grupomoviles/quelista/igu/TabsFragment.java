package grupomoviles.quelista.igu;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import grupomoviles.quelista.R;

public class TabsFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private static TabLayout tabLayout;
    private static ViewPager viewPager;
    private static int int_items = 3 ;
    private static FragmentPagerAdapter myAdapter;
    private View v = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.fragment_tabs,null);
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        /* Ya redefinido en el onAttach
        if(myAdapter == null)
            myAdapter = new MyAdapter(getFragmentManager());
        */
        viewPager.setAdapter(myAdapter);

        tabLayout.setOnTabSelectedListener(null);

        //Soluciona error de destrucción de fragmentos...
        viewPager.setOffscreenPageLimit(int_items-1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTab(position);
                switch (position) {
                    case 0:
                        ((MainActivity) getActivity()).getPantryAdapter().notifyDataSetChanged();
                        break;
                    case 1:
                        ((MainActivity) getActivity()).getShoppingListAdapter().notifyDataSetChanged();
                        break;
                    case 2:
                        ((MainActivity) getActivity()).getCartAdapter().notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Esto es necesario debido a un bug presente en la Design Support Library 22.2.1
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setTab(0);
            }
        });

        Toolbar mToolbar = (Toolbar) v.findViewById(R.id.toolbar);

        DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        return v;
    }

    public void setTab(int tab) {
        viewPager.setCurrentItem(tab);
        ((Toolbar)getActivity().findViewById(R.id.toolbar)).setTitle(myAdapter.getPageTitle(tab));
    }

    @Override
    public void onAttach(Activity activity) {
        myAdapter = new MyAdapter(getChildFragmentManager());
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppBarLayout)v.findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppBarLayout)v.findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(this);
    }

    /**
     * Soluciona el problema de scroll con el refresh layout cuando la toolbar está oculta
     * @param appBarLayout
     * @param i
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (v.findViewById(R.id.swipeRefresh) != null) {
            if (i == 0) {
                v.findViewById(R.id.swipeRefresh).setEnabled(true);
            } else {
                v.findViewById(R.id.swipeRefresh).setEnabled(false);
            }
        }
    }

    static class MyAdapter extends FragmentPagerAdapter {

        private static PantryFragment pantryFragment;
        private static ShoppingListFragment shoppingListFragment;
        private static CartFragment cartFragment;

        public MyAdapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
        }

        /**
         * Devolvemos el fragmento correspondiente en base a la posicion
         */
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0 : return getPantryFragment();
                case 1 : return getShoppingListFragment();
                case 2 : return getCartFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        /**
         * Devolvemos el titulo de la pestaña en base a la posicion
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :
                    return "Despensa";
                case 1 :
                    return "Lista";
                case 2 :
                    return "Carrito";
            }
            
            return null;
        }

        public PantryFragment getPantryFragment() {
            if (pantryFragment == null)
                pantryFragment = new PantryFragment();

            return pantryFragment;
        }

        public ShoppingListFragment getShoppingListFragment() {
            if (shoppingListFragment == null)
                shoppingListFragment = new ShoppingListFragment();

            return shoppingListFragment;
        }

        public CartFragment getCartFragment() {
            if (cartFragment == null)
                cartFragment = new CartFragment();

            return cartFragment;
        }
    }
}
