// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mobilewallet.adapters;

import android.support.v4.app.FixedFragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.mobilewallet.LearnGkFragment;
import com.mobilewallet.MoreFragment;
import com.mobilewallet.recharge.RechargeFragment;
import com.mobilewallet.users.InviteFragment;

public class TabsAdapter extends FixedFragmentStatePagerAdapter {

	public TabsAdapter(FragmentManager fragmentmanager) {
		super(fragmentmanager);
	}

	public int getCount() {
		return 4;
	}

	public Fragment getItem(int i) {
		switch (i) {
		
		case 0:
			return new LearnGkFragment();
		case 1:
			return new InviteFragment();
		case 2:
			return new RechargeFragment();
		case 3:
			return new MoreFragment();
		default:
			return null;

		}
	}
}
