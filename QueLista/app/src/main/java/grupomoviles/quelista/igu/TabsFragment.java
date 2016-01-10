package grupomoviles.quelista.igu;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import grupomoviles.quelista.R;

public class TabsFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private static TabLayout tabLayout;
    private static ViewPager viewPager;
    private static int int_items = 3 ;
    private static FragmentPagerAdapter myAdapter;
    private View v = null;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private View searchContainer;
    private EditText toolbarSearchView;
    private ImageView searchClearButton;
    private ImageView botonBusqueda;

    private int posicion = 0;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                //handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.fragment_tabs,null);
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);


        SearchView mSearchView = (SearchView)v.findViewById(R.id.searchView);

        // Execute this when searching
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                Log.i("SEARCH_SUBMIT", "Estoy buscando: " + arg0);
                ((MainActivity) getActivity()).getPantryAdapter().filtrar(arg0);
                ((MainActivity) getActivity()).getShoppingListAdapter().filtrar(arg0);
                ((MainActivity) getActivity()).getCartAdapter().filtrar(arg0);


                ((MainActivity) getActivity()).getPantryAdapter().notifyDataSetChanged();

                ((MainActivity) getActivity()).getShoppingListAdapter().notifyDataSetChanged();

                ((MainActivity) getActivity()).getCartAdapter().notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                ((MainActivity) getActivity()).getPantryAdapter().filtrar(arg0);
                ((MainActivity) getActivity()).getShoppingListAdapter().filtrar(arg0);
                ((MainActivity) getActivity()).getCartAdapter().filtrar(arg0);
                Log.i("SEARCH","Estoy buscando: " + arg0);
                return false;
            }
        });





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
                ((FloatingActionButton)getActivity().findViewById(R.id.fabScan)).show();

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
        posicion = tab;
        viewPager.setCurrentItem(tab);
        ((MainActivity) getActivity()).getPantryAdapter().getPantry().actualizar();
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

    public void buscador(boolean visible) {
        if (visible) {
            // Stops user from being able to open drawer while searching
            //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            // Hide search button, display EditText
            //menu.findItem(R.id.action_search).setVisible(false);
            v.findViewById(R.id.search_button).setVisibility(View.GONE);
            searchContainer.setVisibility(View.VISIBLE);

            // Animate the home icon to the back arrow
            //toggleActionBarIcon(ActionDrawableState.ARROW, mDrawerToggle, true);

            // Shift focus to the search EditText
            toolbarSearchView.requestFocus();

            // Pop up the soft keyboard
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    toolbarSearchView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                    toolbarSearchView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                }
            }, 200);
        } else {
            // Allows user to open drawer again
            //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            // Hide the EditText and put the search button back on the Toolbar.
            // This sometimes fails when it isn't postDelayed(), don't know why.
            toolbarSearchView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbarSearchView.setText("");
                    searchContainer.setVisibility(View.GONE);
                    //menu.findItem(R.id.action_search).setVisible(true);
                    v.findViewById(R.id.search_button).setVisibility(View.VISIBLE);
                }
            }, 200);

            // Turn the home button back into a drawer icon
            //toggleActionBarIcon(ActionDrawableState.BURGER, mDrawerToggle, true);

            // Hide the keyboard because the search box has been hidden
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(toolbarSearchView.getWindowToken(), 0);
        }

    }
}
