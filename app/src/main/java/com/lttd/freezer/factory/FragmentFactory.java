package com.lttd.freezer.factory;


import com.lttd.freezer.base.BaseFragment;
import com.lttd.freezer.fragment.DustbinFragment;
import com.lttd.freezer.fragment.FridgeFragment;

import java.util.HashMap;

public class FragmentFactory {
    public static final int FRAGMENT_DUSTBIN = 0x100;
    public static final int FRAGMENT_FRIDGE = 0x101;


    private static HashMap<Integer, BaseFragment> FRAGMENT_MAP = new HashMap<Integer, BaseFragment>();

    public static BaseFragment getFragment(int pos) {

        BaseFragment baseFragment = null;
        if (FRAGMENT_MAP.containsKey(pos)) {
            baseFragment = FRAGMENT_MAP.get(pos);
            baseFragment.onResume();
        } else {
            switch (pos) {
                case FRAGMENT_DUSTBIN:
                    baseFragment = new DustbinFragment();
                    break;
                case FRAGMENT_FRIDGE:
                    baseFragment = new FridgeFragment();
                    break;

                default:
                    break;
            }
            FRAGMENT_MAP.put(pos, baseFragment);
        }

        return baseFragment;
    }
}
